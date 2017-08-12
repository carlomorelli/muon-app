package com.csoft.muon.repository.datasource;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;

import com.zaxxer.hikari.HikariDataSource;

/**
 * Static factory to provide DataSource implementations for database access to the DI management framework
 * @author Carlo Morelli
 *
 */
public class DataSourceFactory {

    public static DataSource getPostgresHikariCPDataSource() {
        System.setProperty("hikaricp.configurationFile", "src/main/resources/configuration.properties");
        return new HikariDataSource();
    }
    
    public static DataSource getH2DataSource() {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:./test");
        ds.setUser("sa");
        ds.setPassword("sa");
        return ds;
    }

}
