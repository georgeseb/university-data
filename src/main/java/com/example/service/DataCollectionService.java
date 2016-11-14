package com.example.service;

import com.google.gson.JsonObject;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DataCollectionService {

    @Autowired
    private HttpHelperService httpHelperService;

    @Autowired
    private FileHelperService fileHelperService;

    private final String basicUniversityDataId = "38625c3d-5388-4c16-a30f-d105432553a4";

    public JsonObject getBasicUniversityData(boolean refreshed) {
        return findData(basicUniversityDataId, refreshed);
    }

    private JsonObject findData(String resourceId, boolean refreshed) {

        if (refreshed || !fileHelperService.fileExists(resourceId)) {
            JsonObject data = findDataOnline(resourceId);
            fileHelperService.storeJsonAsFile(resourceId, data);
            return data;
        }
        log.info("Retrieving data from local copy for: " + resourceId);
        return fileHelperService.getJsonFromFile(resourceId);
    }

    private JsonObject findDataOnline(String resourceId) {

        log.info("Finding data online for: " + resourceId);

        URI uri = null;

        try {
            uri = new URIBuilder()
                .setScheme("https")
                .setHost("inventory.data.gov")
                .setPath("/api/action/datastore_search")
                .addParameter("resource_id", resourceId)
                .addParameter("limit", (new Integer(Integer.MAX_VALUE)).toString())
                .build();
        } catch (URISyntaxException e) {
            log.error(e.getMessage());
        }
        return httpHelperService.getJsonData(uri);
    }
}
