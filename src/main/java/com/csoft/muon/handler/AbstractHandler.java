package com.csoft.muon.handler;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csoft.muon.domain.Item;
import com.csoft.muon.repository.Repository;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Abstract base class for Route actions
 * @author Carlo Morelli
 *
 */
public abstract class AbstractHandler implements Route {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractHandler.class);

    private ObjectMapper mapper;
    protected Repository repo;
    
    public AbstractHandler(Repository repo) {
        this.repo = repo;
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
    
    public abstract Result process(String requestBody, Map<String, String> requestParams);
    
    @Override
    public Object handle(Request request, Response response) {
    	    Result result = process(request.body(), request.params());
	        response.type("application/json");
	        response.status(result.getStatus());
	        response.body(result.getBody());
	        LOGGER.info("Handling {} request: {}", request.requestMethod(), result.getBody());
	        return result.getBody();
    }
    
    
    protected String dumpJson(Object object) throws IOException {
        return mapper.writeValueAsString(object);
    }
    
    protected Item parseJson(String string) throws IOException {
        return mapper.readValue(string, Item.class);
    }
}
