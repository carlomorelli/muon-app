package com.csoft.muon.repository;

import java.util.List;

import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csoft.muon.domain.Item;
import com.google.inject.Inject;

public class RepositoryOrmImpl implements Repository {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryOrmImpl.class);

    private SessionFactory sessionFactory;
    
    @Inject
    public RepositoryOrmImpl(DataSource ds) {

        LOGGER.info("Connecting to database [ds='{}']...", ds);

        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySetting(Environment.DATASOURCE, ds)
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }


    @Override
    public List<Item> fetchAllItems() {
        Session session = sessionFactory.openSession();
        List<Item> result = (List<Item>) session.createQuery("from ITEMS").list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    @Override
    public Item fetchItemAtIndex(int index) throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void insertItem(Item item) throws RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateItem(Item item) throws RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteItemAtIndex(int index) throws RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public Boolean isHealthy() {
        // TODO Auto-generated method stub
        return null;
    }

}
