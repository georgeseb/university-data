package com.example.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by gsebastian on 11/6/16.
 */

@Slf4j
@Component
public class ElasticService {

    private TransportClient client;
    private final int bulkSize = 1000;

    public ElasticService() {
        try {
            InetAddress address = InetAddress.getByName("localhost");
            InetSocketTransportAddress transportAddress = new InetSocketTransportAddress(address, 9300);
            client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddress(transportAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendBulkRequest(BulkRequestBuilder bulkRequest){
        BulkResponse response = bulkRequest.get();
        if (response.hasFailures()) {
            log.error(response.buildFailureMessage());
        }
    }

    public void bulkIndexDocuments(JsonArray dataArray, String index, String type, String idField) {

        BulkRequestBuilder bulkIndex = client.prepareBulk();

        for (int i = 0; i < dataArray.size(); i++) {

            JsonObject document = dataArray.get(i).getAsJsonObject();

            IndexRequestBuilder indexBuilder = client.prepareIndex()
                .setIndex(index)
                .setType(type)
                .setId(document.get(idField).toString())
                .setSource(document.toString().replaceAll("_id", "id"));
            bulkIndex.add(indexBuilder);

            if (i % bulkSize == 0 && i > 0) {
                log.info("Indexing batch #" + i/bulkSize);
                sendBulkRequest(bulkIndex);
            }
        }

        if (bulkIndex.numberOfActions() > 0) {
            log.info("Indexing final batch");
            sendBulkRequest(bulkIndex);
        }
    }
}
