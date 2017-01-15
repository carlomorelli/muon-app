package com.csoft.muon.test.integration;

import java.sql.SQLException;

import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.csoft.muon.lib.Dao;
import com.csoft.muon.lib.Item;

public class DaoTest {

    
    private static final Logger LOGGER = LoggerFactory.getLogger(DaoTest.class);

    private Server server;
    
    @BeforeClass
    public void setupClass() {
        try {
            server = Server.createTcpServer("-tcpPort", "9093").start();
            LOGGER.info("Started H2 server [{}]", server.getStatus());
        } catch (SQLException e) {
            LOGGER.error("Starting H2 server failed!");
            throw new RuntimeException(e);
        }
    }
    
    @AfterClass
    public void teardownClass() {
        server.stop();
        LOGGER.info("Stopped H2 server [{}]", server.getStatus());
    }
    
    @Test
    public void testItemInsert() {
        Dao db = new Dao("jdbc:h2:./test.db", "sa", "");
        db.prepareDb();
        Item myItem = new Item(1, "label");
        db.insertItem(myItem);
    }
    
    
    @Test
    public void testItemFetchAll() {
        Dao db = new Dao("jdbc:h2:./test.db", "sa", "");
        db.prepareDb();
        
    }
    
}
