package com.csoft.muon;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.stop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csoft.muon.handler.GetHandler;
import com.csoft.muon.handler.GetListHandler;
import com.csoft.muon.handler.PostHandler;
import com.csoft.muon.repository.Repository;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * Frontend web application
 * @author Carlo Morelli
 *
 */
public class App {

    private static final int WEB_PORT = 8080;
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    private int webPort;
    private Repository repo;
    
    @Inject
    public App(Repository repo) {
        this(WEB_PORT, repo);
    }

    public App(int webPort, Repository repo) {
        this.webPort = webPort;
        this.repo = repo;
    }
    
    public void startServer() {
        LOGGER.info("Starting service on port " + webPort + "...");
        port(webPort);
        get("/webapi/items/:index", new GetHandler(repo));
        get("/webapi/items", new GetListHandler(repo));
        post("/webapi/items", new PostHandler(repo));
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
        
        App server = new App(webPort, repo);
        server.startServer();
    }
    

    
}
