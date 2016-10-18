package com.csoft.muon;

import java.util.Arrays;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class SimpleServer {

    public static void main(String... args) {
        
        JsonObject sample = new JsonObject().put("one", "one").put("two", "two").put("array", new JsonArray(Arrays.asList(1, 2, 3)));

        Vertx vertx = Vertx.vertx(); 
        
        Router router = Router.router(vertx);
        
        router.route("/test").handler(routingContext -> 
            routingContext.response().putHeader("content-type", "text/plain").end(sample.encodePrettily())
            );
        
        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }
}
