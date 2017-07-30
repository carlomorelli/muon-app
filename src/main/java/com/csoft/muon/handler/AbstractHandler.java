package com.csoft.muon.handler;

import static com.csoft.muon.utils.RandomUtils.nullItem;

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
    
    public abstract Result process(Item item, Map<String, String> params);
    
    @Override
    public Object handle(Request request, Response response) {
    	try {
	        Item item;
	        if (request.body().isEmpty() || request.body()== null) {
	            item = nullItem();
	        } else {
	            item = parseJson(request.body());
	        }
	        Result result = process(item, request.params());
	        response.type("application/json");
	        response.status(result.getStatus());
	        response.body(result.getBody());
	        LOGGER.info("Handling {} request: {}", request.requestMethod(), result.getBody());
	        return result.getBody();
    	} catch (IOException e) {
    		LOGGER.error("Unable to handle {} request - StackTrace <{}>", request.requestMethod(), e);
    		throw new RuntimeException(e);
    	}
    }
    
    
    protected String dumpJson(Object object) throws IOException {
        return mapper.writeValueAsString(object);
    }
    
    protected Item parseJson(String string) throws IOException {
        return mapper.readValue(string, Item.class);
    }
}
