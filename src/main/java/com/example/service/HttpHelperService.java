package com.example.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;

import lombok.extern.slf4j.Slf4j;

/**
 * Credits: http://hc.apache.org/httpcomponents-client-ga/tutorial/html/fundamentals.html
 */

@Component
@Slf4j
public class HttpHelperService {

    private Gson gson;

    public HttpHelperService() {
        gson = new GsonBuilder().create();
    }

    public ResponseHandler<JsonObject> getJsonResponseHandler() {

        return (response) -> {

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode >= 300) {
                throw new HttpResponseException(statusCode, response.getStatusLine().getReasonPhrase());
            }

            HttpEntity entity = response.getEntity();
            if (entity == null) {
                throw new ClientProtocolException("Empty entity.");
            }

            ContentType contentType = ContentType.get(entity);

            if (!contentType.getMimeType().equals(ContentType.APPLICATION_JSON.getMimeType())) {
                throw new ClientProtocolException("Content must be in JSON to process.");
            }

            Reader reader = new InputStreamReader(entity.getContent(), contentType.getCharset());
            return gson.fromJson(reader, JsonObject.class);
        };
    }

    public JsonObject getJsonData(URI uri) {
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            HttpGet get = new HttpGet(uri);
            return client.execute(get, getJsonResponseHandler());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
