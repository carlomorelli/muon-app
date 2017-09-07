package com.csoft.muon;

import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csoft.muon.provider.EnvConfProvider;
import com.csoft.muon.provider.FileConfProvider;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(AppConfig.class);
    
    private Properties properties;
    
    AppConfig() {
        super();

        // try getting configuration from Env, otherwise try from File
        if (EnvConfProvider.isValid()) {
            LOGGER.info("Using env-variables based configuration.");
            properties = EnvConfProvider.get();
        } else if (FileConfProvider.isValid()) {
            LOGGER.info("Env-variables based configuration incomplete. Using file based configuration.");
            properties = FileConfProvider.get();
        } else {
            throw new RuntimeException("Unable to retrive valid configuration.");
        }
    }

    @Override
    protected void configure() {

        // bind configuration
        bind(Repository.class).to(RepositoryImpl.class);
        bindConstant().annotatedWith(Names.named("type")).to(properties.getProperty("datasourcetype"));
    }

    @Provides
    @Singleton
    public DataSource getDataSource(@Named("type") String type) {
        if (type.equals("postgres-hikari")) {
            return DataSourceFactory.getPostgresHikariCPDataSource(properties);
        } else if (type.equals("postgres-simple")) {
            return DataSourceFactory.getPosgresSimpleDataSource(properties);
        } else {
            throw new RuntimeException("Unable to configure database, wrong 'datasourcetype' property received.");
        }
    }
}