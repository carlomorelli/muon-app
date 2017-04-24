package com.csoft.muon;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.stop;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csoft.muon.domain.Item;
import com.csoft.muon.repository.Repository;
import com.csoft.muon.repository.RepositoryImpl;
import com.csoft.muon.utils.RandomUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import spark.Request;
import spark.Response;

public class SimpleServer {

    private static final int WEB_PORT = 8080;
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleServer.class);

    private final int webPort;
    private Repository repository;
    
    private Map<Integer, Item> inMemoryDb;

    // some sample data as init database
    private Item product1 = RandomUtils.getRandomItem(1);
    private Item product2 = RandomUtils.getRandomItem(2);

    private ObjectMapper mapper = new ObjectMapper();
    
    public SimpleServer(Repository repository) {
        this(WEB_PORT, repository);
    }

    public SimpleServer(int webPort, Repository repository) {
        this.webPort = webPort;
        inMemoryDb = new HashMap<>();
        inMemoryDb.put(product1.getIndex(), product1);
        inMemoryDb.put(product2.getIndex(), product2);
    }
    
    
    public String handleGet(Request req, Response res) {
        try {
            int id = Integer.parseInt(req.params(":id"));
            Item item = inMemoryDb.get(id);
            LOGGER.info("Handing GET: " + mapper.writeValueAsString(item)); //item.toString());
            res.type("application/json");
            return mapper.writeValueAsString(item);
        } catch (IOException e) {
            res.status(404);
            return "";
        }
    }

    public String handleGetList(Request req, Response res) {
        try {
            List<Item> items = inMemoryDb.keySet().stream()
                .sorted()
                .map(inMemoryDb::get)
                .collect(Collectors.toList());
            res.type("application/json");
            LOGGER.info("Handing GET: " + items.toString());
            return mapper.writeValueAsString(items);
        } catch (IOException e) {
            res.status(404);
            return "";
        }
    }

    public String handlePost(Request req, Response res) {
        try {
            Item item = mapper.readValue(req.body(), Item.class);
            LOGGER.info("Handing POST: " + item.toString());
            if (!item.isValid()) {
                throw new IllegalArgumentException("Non null / non empty fields are required in input");
            }
            inMemoryDb.put(item.getIndex(), item);
            res.type("application/json");
            return item.toString();
        } catch (IOException | IllegalArgumentException e) {
            //note: IOException is thrown by ObjectMapper when cast class is not respected
            res.status(404);
            return "";
        }
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

    public static void main(String... args) throws InterruptedException {
        int webPort = WEB_PORT;
        if (args.length > 0) {
            webPort = Integer.parseInt(args[0]);
        }
        SimpleServer server = new SimpleServer(webPort, new RepositoryImpl());
        server.startServer();
        Thread.sleep(60000);
        server.stopServer();
    }
    

    
}
