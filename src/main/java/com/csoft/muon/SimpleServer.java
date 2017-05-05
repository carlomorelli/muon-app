package com.csoft.muon;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.stop;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csoft.muon.domain.Item;
import com.csoft.muon.repository.Repository;
import com.csoft.muon.repository.RepositoryException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import spark.Request;
import spark.Response;

public class SimpleServer {

    private static final int WEB_PORT = 8080;
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleServer.class);

    private final int webPort;
    private final Repository repo;
    
    private final ObjectMapper mapper = new ObjectMapper();

    @Inject
    public SimpleServer(Repository repo) {
        this(WEB_PORT, repo);
    }

    @Inject
    public SimpleServer(int webPort, Repository repo) {
        this.webPort = webPort;
        this.repo = repo;
    }
    
    
    public String handleGet(Request req, Response res) {
        int index = Integer.parseInt(req.params(":id"));
        try {
            Item item = repo.fetchItemAtIndex(index);
            LOGGER.info("Handing GET: " + mapper.writeValueAsString(item));
            res.type("application/json");
            return mapper.writeValueAsString(item);
        } catch (RepositoryException e) {
            res.status(404);
            return "ClientError: requested index " + index + " not found";
        } catch (IOException e) {
            res.status(503);
            return "ServerError: unable to provide correctly item";
        }
    }

    public String handleGetList(Request req, Response res) {
        try {
            List<Item> items = repo.fetchAllItems();
            LOGGER.info("Handing GET: " + mapper.writeValueAsString(items));
            res.type("application/json");
            return mapper.writeValueAsString(items);
        } catch (IOException e) {
            res.status(503);
            return "ServerError: unable to provide correctly items";
        }
    }

    public String handlePost(Request req, Response res) {
        try {
            Item item = mapper.readValue(req.body(), Item.class);
            LOGGER.info("Handing POST: " + req.body());
            if (!item.isValid()) {
                throw new IllegalArgumentException("Non null / non empty fields are required in input");
            }
            repo.insertItem(item);
            res.type("application/json");
            return req.body();
        } catch (RepositoryException e) {
            res.status(403);
            return "ClientError: invalid / null index or already used index in input item";
        } catch (IOException | IllegalArgumentException e) {
            //note: IOException is thrown by ObjectMapper when cast class is not respected
            res.status(503);
            return "ServerError: unable to process correctly input item";
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
        
        Injector injector = Guice.createInjector(new AppConfig());
        Repository repo = injector.getInstance(Repository.class);
        int webPort = WEB_PORT;
        if (args.length > 0) {
            webPort = Integer.parseInt(args[0]);
        }
        
        SimpleServer server = new SimpleServer(webPort, repo);
        server.startServer();
        Thread.sleep(60000);
        server.stopServer();
    }
    

    
}
