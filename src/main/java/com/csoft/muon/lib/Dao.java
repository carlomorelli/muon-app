package com.csoft.muon.lib;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.IntStream;

import javax.sql.DataSource;

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
    
    public Dao(DataSource ds) {
        LOGGER.info("Connecting to database [ds='{}']...", ds);
        dao = new Sql2o(ds);
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
    
    public void flushTable() {
        LOGGER.warn("Flushing table...");
        String sql = "TRUNCATE TABLE items";
        try(Connection conn = dao.open()) {
            conn.createQuery(sql)
            .executeUpdate();
        }
    }
    
    public void prepareDb() {
        LOGGER.info("Preparing MainSchema...");
        String sql = "CREATE TABLE items "
                + "(index INTEGER not NULL,"
                + " label VARCHAR(255),"
                + " PRIMARY KEY ( index ))";
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

    // TODO convert in a test with mock db
    public static void main(String...args) throws SQLException {
        
        DataSource ds = DataSourceFactory.getH2DataSource();
        Dao dao = new Dao(ds);
        dao.prepareDb();
        
        IntStream.range(0, 100).forEach(
            x -> dao.insertItem(new Item(x+1, "prova" +x))
        );
        List<Item> list = dao.fetchAllItems();
        list.forEach(item -> LOGGER.info("Found item [index {}, label {}]", item.getIndex(), item.getLabel()));
    }

}
