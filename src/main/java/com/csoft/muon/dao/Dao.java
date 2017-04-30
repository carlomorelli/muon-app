package com.csoft.muon.dao;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import com.csoft.muon.domain.Item;

public class Dao {

    private static final Logger LOGGER = LoggerFactory.getLogger(Dao.class);

    private Sql2o sql2o;
        
    public Dao(DataSource ds) {
        LOGGER.info("Connecting to database [ds='{}']...", ds);
        sql2o = new Sql2o(ds);
    }

    public List<Item> fetchAllItems() {
        String sql = "SELECT * FROM items";
        try(Connection conn = sql2o.open()) {
            return conn.createQuery(sql).executeAndFetch(Item.class);
        }
    }

    public void insertItem(Item item) throws DaoException {
        if (item.getIndex() == null) {
            throw new DaoException("Forbidden: Index is null");
        }
        if (item.getIndex() <= 0) {
            throw new DaoException("Forbidden: Index must be strictly positive");
        }
        String sql = "INSERT INTO items(index, label) VALUES (:index,:label)";
        try (Connection conn = sql2o.open()) {
            try {
                conn.createQuery(sql)
                .bind(item)
                .executeUpdate();
            } catch (Sql2oException e) {
                throw new DaoException("Forbidden: Index is already under use", e);
            }
        }
    }

    public void updateItem(Item item) throws DaoException {
        fetchItemAtIndex(item.getIndex());
        String sql = "UPDATE items SET label=:label WHERE index=:index";
        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql)
                .bind(item)
                .executeUpdate();
        }
    }
    public void deleteItemAtIndex(int index) throws DaoException {
        try {
            fetchItemAtIndex(index);
        } catch (DaoException e) {
            throw new DaoException("Forbidden: Index is not existing");
        }
        String sql = "DELETE FROM items WHERE (index) IS (:index)";
        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql)
                .addParameter("index", index)
                .executeUpdate();
        }
    }
    
    public Item fetchItemAtIndex(int index) throws DaoException {
        String sql = "SELECT * FROM items WHERE (index) IS (:index)";
        try (Connection conn = sql2o.open()) {
            List<Item> list = conn.createQuery(sql)
                .addParameter("index", index)
                .executeAndFetch(Item.class);
            if (list == null || list.isEmpty()) {
                throw new DaoException("Item with given index cannot be retrieved");
            }
            if (list.size() > 1) {
                // should be impossible if index is the primary key
                throw new DaoException("Inconsistency in db, found two items with given index");
            }
            return list.get(0);
        }
    }
    
    public void flushTable() {
        LOGGER.warn("Flushing table...");
        String sql = "TRUNCATE TABLE items";
        try (Connection conn = sql2o.open()) {
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
        Connection conn = sql2o.open();
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
