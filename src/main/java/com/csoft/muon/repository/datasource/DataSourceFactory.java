package com.csoft.muon.repository.datasource;

import java.util.Properties;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.postgresql.ds.PGSimpleDataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Factory to provide DataSource implementations for database access to
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

    private DataSourceFactory(Properties p) {
        this.hostname = p.getProperty("hostname");
        this.port = p.getProperty("port");
        this.database = p.getProperty("database");
        this.username = p.getProperty("username");
        this.password = p.getProperty("password");
    }

    private DataSource withPostgresHikariCPDataSource() {
        Properties dsConf = new Properties();
        dsConf.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
        dsConf.setProperty("dataSource.user", username);
        dsConf.setProperty("dataSource.password", password);
        dsConf.setProperty("dataSource.databaseName", database);
        dsConf.setProperty("dataSource.portNumber", port);
        dsConf.setProperty("dataSource.serverName", hostname);
        HikariConfig config = new HikariConfig(dsConf);
        return new HikariDataSource(config);
    }

    private DataSource withPosgresSimpleDataSource() {
        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setUrl(String.format("jdbc:postgresql://%s:%s/%s", hostname, port, database));
        ds.setUser(username);
        ds.setPassword(password);
        return ds;
    }

    public static DataSource getPostgresHikariCPDataSource(Properties p) {
        return new DataSourceFactory(p).withPostgresHikariCPDataSource();
    }

    public static DataSource getPosgresSimpleDataSource(Properties p) {
        return new DataSourceFactory(p).withPosgresSimpleDataSource();
    }
    public static DataSource getH2DataSource() {
        
        // this factory does not depend on injected properties, used for test only
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        ds.setUser("sa");
        ds.setPassword("sa");
        return ds;
    }

}
