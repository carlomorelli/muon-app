package com.csoft.muon.lib;

import java.sql.SQLException;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;

public class DataSourceFactory {

    public static DataSource getPostgresHikariCPDataSource() throws SQLException {
        System.setProperty("hikaricp.configurationFile", "src/main/resources/configuration.properties");
        return new HikariDataSource();
    }

}
