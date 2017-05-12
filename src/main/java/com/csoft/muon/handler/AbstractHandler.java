package com.csoft.muon.handler;

import java.io.IOException;
import java.util.Map;

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

    private ObjectMapper mapper;
    protected Repository repo;
    
    public AbstractHandler(Repository repo) {
        this.repo = repo;
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
    
    public abstract Result process(Item item, Map<String, String> params);
    
    @Override
    public Object handle(Request request, Response response) throws Exception {
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
        return result.getBody();
    }
    
   
    private static Item nullItem() {
        return new Item(null, null);
    }

    protected String dumpJson(Object object) throws IOException {
        return mapper.writeValueAsString(object);
    }
    
    protected Item parseJson(String string) throws IOException {
        return mapper.readValue(string, Item.class);
    }
}
