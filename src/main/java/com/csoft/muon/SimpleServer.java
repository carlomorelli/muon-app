package com.csoft.muon;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.stop;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csoft.muon.lib.Item;
import com.csoft.muon.lib.RestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import spark.Request;
import spark.Response;

public class SimpleServer {

    private static final int WEB_PORT = 8080;
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleServer.class);

    private final int webPort;
    private Map<Integer, JsonObject> inMemoryDb;

    // some sample data as init database
    private JsonObject product1 = RestUtils.getRandomBody(1);
    private JsonObject product2 = RestUtils.getRandomBody(2);

    public SimpleServer() {
        this(WEB_PORT);
    }

    public SimpleServer(int webPort) {
        this.webPort = webPort;
        inMemoryDb = new HashMap<>();
        inMemoryDb.put(product1.get("id").getAsInt(), product1);
        inMemoryDb.put(product2.get("id").getAsInt(), product2);
    }
    
    
    private String handleGet(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));
        JsonObject item = inMemoryDb.get(id);
        LOGGER.info("Handing GET: " + item.toString());
        res.type("application/json");
        return item.toString();
    }

    private String handleGetList(Request req, Response res) {
        JsonArray items = new JsonArray();
        inMemoryDb.forEach((k, v) -> items.add(v));
        JsonObject itemsFull = new JsonObject();
        itemsFull.addProperty("total", items.size());
        itemsFull.add("items", items);
        LOGGER.info("Handing GET: " + itemsFull.toString());
        res.type("application/json");
        return itemsFull.toString();
    }

    private String handlePost(Request req, Response res) {
        JsonObject item = new JsonParser().parse(req.body()).getAsJsonObject();
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            Item newItem = mapper.readValue(req.body(), Item.class);
            if (!newItem.isValid()) {
                throw new IllegalArgumentException("Non null / non empty fields are required in input");
            }
        } catch (IOException | IllegalArgumentException e) {
            //note: IOException is thrown by ObjectMapper when cast class is not respected
            res.status(404);
            return "";
        }
        
        
        LOGGER.info("Handing PUT: " + item.toString());
        inMemoryDb.put(item.get("id").getAsInt(), item);
        res.type("application/json");
        return item.toString();
    }

     public void startServer() {
         LOGGER.info("Starting service on port " + webPort + "...");
         port(webPort);
         get("/webapi/items/:id", this::handleGet);
         get("/webapi/items", this::handleGetList);
         post("/webapi/items", this::handlePost);
     }

     public void stopServer() {
         LOGGER.info("Stopping service...");
         stop();
     }
}
