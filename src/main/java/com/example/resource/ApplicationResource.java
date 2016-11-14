package com.example.resource;

import com.example.service.DataCollectionService;
import com.example.service.DataSyncService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;


@Path("data")
@Component
public class ApplicationResource {

    @Autowired
    DataCollectionService collectionService;

    @Autowired
    DataSyncService dataSyncService;

    @Path("/source")
    @GET
    public String getData(@QueryParam("refreshed") boolean refreshed) {
        return collectionService.getBasicUniversityData(refreshed).toString();
    }

    //TODO Change to put/post
    @Path("/sync")
    @GET
    public void syncData(@QueryParam("refreshed") boolean refreshed) {
        dataSyncService.syncDataToElasticsearch(refreshed);
    }
}
