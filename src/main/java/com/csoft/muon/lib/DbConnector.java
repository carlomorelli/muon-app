package com.csoft.muon.lib;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;

import org.h2.tools.Server;

public class DbConnector {

    private JDBCClient client;
    private SQLConnection connection;
    
    private static final JsonObject DB_CONFIG = new JsonObject()
            .put("url", "jdbc:h2:tcp://localhost:9093/C:\\workspace\\dbtest")
            .put("driver_class", "org.h2.Driver")
            .put("max_pool_size", 30);
    
    public DbConnector(Vertx vertx) {
        client = JDBCClient.createNonShared(vertx, DB_CONFIG);
    }
    
    public SQLConnection getConnection() {
        client.getConnection(res -> {
            if (res.succeeded()) {
                connection = res.result();
            } else {
                throw new RuntimeException("Unable to connect to database.");
            }
        });
        return connection;
    }
    
    public void close() {
        client.close();
    }
    
    public static void main(String... args) throws Exception {
        
        // start the tcp H2 Server
        Server server = Server.createTcpServer("-tcpPort", "9093").start();
        System.out.println("Started server at: " + server.getURL());
        System.out.println("Port:              " + server.getPort());
        System.out.println("Status:            " + server.getStatus());
        // connect to server
        //DbConnector conn = new DbConnector(Vertx.vertx());
        //SQLConnection cursor = conn.getConnection();
        
        Vertx vertx = Vertx.vertx();
        JDBCClient client = JDBCClient.createShared(vertx, DB_CONFIG);
        
        String createQuery = "CREATE TABLE TEST"
                + "(COL1 INTEGER NOT NULL,"
                + "COL2 VARCHAR(250),"
                + "COL3 VARCHAR(250) NOT NULL,"
                + "COL4 DATE,"
                + "PRIMARY KEY (COL1))";
        client.getConnection(res -> {
            if (res.succeeded()) {
                SQLConnection connection = res.result();
                connection.query(createQuery, res2 -> {
                    if (res2.succeeded()) {
                        System.out.println("table created");
                        
                    }
                });
            }
        });
        
        client.close();
        
        // stop the server
        Thread.sleep(1000);
        server.stop();
        System.out.println("Status:            " + server.getStatus());
        
    }
}
