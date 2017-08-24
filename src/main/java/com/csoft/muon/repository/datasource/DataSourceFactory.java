package com.csoft.muon.repository.datasource;

import java.util.Properties;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.postgresql.ds.PGSimpleDataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Static factory to provide DataSource implementations for database access to
 * the DI management framework
 * 
 * @author Carlo Morelli
 *
 */
public class DataSourceFactory {

    final String hostname;
    final String port;
    final String database;
    final String username;
    final String password;
    
    
    private DataSourceFactory(Properties dbProps) {
        this.hostname = dbProps.getProperty("hostname");
        this.port = dbProps.getProperty("port");
        this.database = dbProps.getProperty("database");
        this.username = dbProps.getProperty("username");
        this.password = dbProps.getProperty("password");
    }
    
    private DataSource withPostgresHikariCPDataSource() {
        Properties props = new Properties();
        props.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
        props.setProperty("dataSource.user", username);
        props.setProperty("dataSource.password", password);
        props.setProperty("dataSource.databaseName", database);
        props.setProperty("dataSource.portNumber", port);
        props.setProperty("dataSource.serverName", hostname);
        HikariConfig config = new HikariConfig(props);
        return new HikariDataSource(config);
        
//        config.setDataSourceClassName(className);
//        config.setData
//        ds.setJdbcUrl(String.format("jdbc:postgresql://%s:%s/%s", hostname, port, database));
//        ds.setUsername(username);
//        ds.setPassword(password);
//        ds.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
//        //STILL NEED TO DO THIS OR LINUX FAILS ds.setDriverClassName("org.postgresql.ds.PGSimpleDataSource");
//        return ds;
    }

    private DataSource withPosgresSimpleDataSource() {
        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setUrl(String.format("jdbc:postgresql://%s:%s/%s", hostname, port, database));
        ds.setUser(username);
        ds.setPassword(password);
        return ds;
    }

    public static DataSource getPostgresHikariCPDataSource(Properties dbProps) {
        return new DataSourceFactory(dbProps).withPostgresHikariCPDataSource();
    }

    public static DataSource getPosgresSimpleDataSource(Properties dbProps) {
        return new DataSourceFactory(dbProps).withPosgresSimpleDataSource();
    }
    public static DataSource getH2DataSource() {
        
        // this factory does not depend on injected properties, used for test only
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:./test");
        ds.setUser("sa");
        ds.setPassword("sa");
        return ds;
    }

}
