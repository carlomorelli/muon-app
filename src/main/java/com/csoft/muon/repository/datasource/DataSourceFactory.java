package com.csoft.muon.repository.datasource;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.postgresql.ds.PGSimpleDataSource;

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
    
    public static DataSource getPosgresSimpleDataSource() {
    	try {
        	Properties props = new Properties();
			props.load(ClassLoader.getSystemResourceAsStream("configuration.properties"));
	    	PGSimpleDataSource ds = new PGSimpleDataSource();
			ds.setServerName(props.getProperty("dataSource.serverName"));
			ds.setPortNumber(Integer.parseInt(props.getProperty("dataSource.portNumber")));
			ds.setDatabaseName(props.getProperty("dataSource.databaseName"));
			ds.setUser(props.getProperty("dataSouce.username"));
			ds.setPassword(props.getProperty("dataSource.password"));
	    	return ds;
		} catch (IOException e) {
			throw new RuntimeException("Cannot load DB properties correctly from file.", e);		}
    	
    }
    
    public static DataSource getH2DataSource() {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:./test");
        ds.setUser("sa");
        ds.setPassword("sa");
        return ds;
    }

}
