package com.example.resource;

import com.example.resource.ApplicationResource;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

/**
 * Created by gsebastian on 11/5/16.
 */

@Component
public class JerseyConfiguration extends ResourceConfig {

    public JerseyConfiguration(){
        register(ApplicationResource.class);
    }
}
