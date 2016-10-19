package com.csoft.muon;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class SimpleServer {

    private static final int WEB_PORT = 8080;
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleServer.class);

    private Vertx vertx;
    private Router router;
    private Map<Integer, JsonObject> items;
    
    // some sample data as init database
    private JsonObject product1 = new JsonObject()
            .put("id", 1)
            .put("label", "item1")
            .put("array", new JsonArray(Arrays.asList(1, 2, 3)));
    private JsonObject product2 = new JsonObject()
            .put("id", 2)
            .put("label", "item2")
            .put("array", new JsonArray(Arrays.asList(7, 8, 9, 10)));
    
    SimpleServer() {
        vertx = Vertx.vertx();
        router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.get("/webapi/items").handler(this::handleGetList);
        router.get("/webapi/items/:item").handler(this::handleGet);
        router.put("/webapi/items/:item").handler(this::handlePut);
        items = new HashMap<>();
        items.put(product1.getInteger("id"), product1);
        items.put(product2.getInteger("id"), product2);
    }

    private void handleGet(RoutingContext routingContext) {
        int productID = Integer.parseInt(routingContext.request().getParam("item"));
        JsonObject product = items.get(productID);
        LOGGER.info("Handing GET: {}", product.toString());
        routingContext.response().putHeader("content-type", "application/json").end(product.encodePrettily());
    }

    private void handleGetList(RoutingContext routingContext) {
        JsonArray productList = new JsonArray();
        items.forEach((k, v) -> productList.add(v));
        LOGGER.info("Handing GET: {}", productList.toString());
        routingContext.response().putHeader("content-type", "application/json").end(productList.encodePrettily());
    }

    private void handlePut(RoutingContext routingContext) {
        JsonObject product = routingContext.getBodyAsJson();
        LOGGER.info("Handing PUT: {}", product.toString());
        items.put(product.getInteger("id"), product);
        routingContext.response().end();
    }

    private void start() {
        vertx.createHttpServer().requestHandler(router::accept).listen(WEB_PORT);

    }



    public static void main(String... args) {
        SimpleServer server = new SimpleServer();
        LOGGER.info("Starting SimpleServer...");
        server.start();

    }

}
