package com.example.service;


import com.example.resource.Constants;
import static com.example.resource.Constants.Elasticsearch.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataSyncService {

    @Autowired
    ElasticService elasticService;

    @Autowired
    DataCollectionService dataCollectionService;

    public void syncDataToElasticsearch(boolean refreshed){

        JsonObject universityData = dataCollectionService.getBasicUniversityData(refreshed);

        JsonObject result = universityData.get("result").getAsJsonObject();

        JsonArray records = result.get("records").getAsJsonArray();

        elasticService.bulkIndexDocuments(records, BASIC_UNIVERSITY_INDEX, DEFAULT_TYPE, "UNITID");
    }
}
