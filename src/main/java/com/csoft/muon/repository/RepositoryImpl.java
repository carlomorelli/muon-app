package com.csoft.muon.repository;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import com.csoft.muon.domain.Item;
import com.google.inject.Inject;

/**
 * Repository implementation using database as storage backend
 * Database is configured by dependency injection of DataSource object
 * Exception {@link RepositoryException} is used to handle errors
 * @author Carlo Morelli
 *
 */
public class RepositoryImpl implements Repository {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryImpl.class);

    private Sql2o sql2o;
    
    @Inject
    public RepositoryImpl(DataSource ds) {
        LOGGER.info("Connecting to database [ds='{}']...", ds);
        sql2o = new Sql2o(ds);
    }

    public List<Item> fetchAllItems() {
        String sql = "SELECT * FROM items";
        try(Connection conn = sql2o.open()) {
            return conn.createQuery(sql).executeAndFetch(Item.class);
        }
    }

    public void insertItem(Item item) throws RepositoryException {
        if (item.getIndex() == null) {
            throw new RepositoryException("Forbidden: Index is null");
        }
        if (item.getIndex() <= 0) {
            throw new RepositoryException("Forbidden: Index must be strictly positive");
        }
        String sql = "INSERT INTO items(index, label) VALUES (:index,:label)";
        try (Connection conn = sql2o.open()) {
            try {
                conn.createQuery(sql)
                .bind(item)
                .executeUpdate();
            } catch (Sql2oException e) {
                throw new RepositoryException("Forbidden: Index is already under use", e);
            }
        }
    }

    public void updateItem(Item item) throws RepositoryException {
        fetchItemAtIndex(item.getIndex());
        String sql = "UPDATE items SET label=:label WHERE index=:index";
        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql)
                .bind(item)
                .executeUpdate();
        }
    }
    public void deleteItemAtIndex(int index) throws RepositoryException {
        try {
            fetchItemAtIndex(index);
        } catch (RepositoryException e) {
            throw new RepositoryException("Forbidden: Index is not existing");
        }
        String sql = "DELETE FROM items WHERE index = :index";
        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql)
                .addParameter("index", index)
                .executeUpdate();
        }
    }
    
    public Item fetchItemAtIndex(int index) throws RepositoryException {
        String sql = "SELECT * FROM items WHERE index = :index";
        try (Connection conn = sql2o.open()) {
            List<Item> list = conn.createQuery(sql)
                .addParameter("index", index)
                .executeAndFetch(Item.class);
            if (list == null || list.isEmpty()) {
                throw new RepositoryException("Item with given index cannot be retrieved");
            }
            if (list.size() > 1) {
                // should be impossible if index is the primary key
                throw new RepositoryException("Inconsistency in db, found two items with given index");
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
