package com.csoft.muon;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import com.csoft.muon.repository.Repository;
import com.csoft.muon.repository.RepositoryImpl;
import com.csoft.muon.repository.datasource.DataSourceFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

/**
 * Runtime application configuration using Google Guice
 * 
 * @author Carlo Morelli
 * 
 */
public class AppConfig extends AbstractModule {

    private static final String CONF_FILE = "app.properties";
    
    
    
    @Override
    protected void configure() {
        
        Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream(CONF_FILE));
        } catch (IOException e) {
            throw new RuntimeException("Unable to access configuration file '" + CONF_FILE + "'.", e);
        }
        
        bind(Repository.class).to(RepositoryImpl.class);
        bindConstant().annotatedWith(Names.named("type")).to(properties.getProperty("dataSource.type"));
    }

    
    
    @Provides
    @Singleton
    public DataSource getDataSource(@Named("type") String type) {
        if (type.equals("postgres-hikari")) {
            return DataSourceFactory.getPostgresHikariCPDataSource();
        } else if (type.equals("postgres-simple")) {
            return DataSourceFactory.getPosgresSimpleDataSource();
        } else {
            return DataSourceFactory.getH2DataSource();
        }
    }
}