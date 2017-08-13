package com.csoft.muon;

import javax.sql.DataSource;

import com.csoft.muon.repository.Repository;
import com.csoft.muon.repository.RepositoryImpl;
import com.csoft.muon.repository.datasource.DataSourceFactory;
import com.google.inject.AbstractModule;

/**
 * Runtime application configuration using Google Guice
 * @author Carlo Morelli
 * 
 */
public class AppConfig extends AbstractModule {

    @Override
    protected void configure() {
        bind(Repository.class).to(RepositoryImpl.class);
        bind(DataSource.class).toInstance(DataSourceFactory.getPosgresSimpleDataSource());
    }

}