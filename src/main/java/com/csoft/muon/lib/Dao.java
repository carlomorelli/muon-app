package com.csoft.muon.lib;

import java.util.List;

import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

public class Dao {

    private static final Logger LOGGER = LoggerFactory.getLogger(Dao.class);

    private Sql2o dao;
    
    public Dao(String url, String username, String password) {
        LOGGER.info("Connecting to database [url='{}']...", url);
        dao = new Sql2o(url, username, password);
    }

    public List<Item> fetchAllItems() {
        String sql = "SELECT * FROM items";
        try(Connection conn = dao.open()) {
            return conn.createQuery(sql).executeAndFetch(Item.class);
        }
    }

    public void insertItem(Item item) {
        String sql = "INSERT INTO items(index, label) VALUES (:index,:label)";
        try(Connection conn = dao.open()) {
            conn.createQuery(sql)
                .bind(item)
                .executeUpdate();
        }
    }
    
    public void prepareDb() {
        LOGGER.info("Preparing MainSchema...");
        String sql = "CREATE TABLE items (\n"
                + "index    INT NOT NULL IDENTITY PRIMARY KEY,\n"
                + "label    VARCHAR(255) NOT NULL\n"
                + ")";
        Connection conn = dao.open();
        try {
            conn.createQuery(sql)
                .executeUpdate();
        } catch (Sql2oException e) {
            LOGGER.warn("MainSchema already existing. Flushing tables...");
            sql = "TRUNCATE TABLE items";
            conn.createQuery(sql)
                .executeUpdate();
        }
        conn.close();
    }

}
