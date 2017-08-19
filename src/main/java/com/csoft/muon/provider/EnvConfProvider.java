package com.csoft.muon.provider;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum EnvConfProvider {

    INSTANCE;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(EnvConfProvider.class);
    private static final String NOT_CONFIGURED = "notConfigured";
    
    private Properties p;
    
    EnvConfProvider() {
        p = new Properties();

        p.setProperty("datasourcetype", System.getProperty("DB_TYPE", NOT_CONFIGURED));
        p.setProperty("hostname", System.getProperty("DB_HOSTNAME", NOT_CONFIGURED));
        p.setProperty("port", System.getProperty("DB_PORT", NOT_CONFIGURED));
        p.setProperty("database", System.getProperty("DB_NAME", NOT_CONFIGURED));
        p.setProperty("username", System.getProperty("DB_USERNAME", NOT_CONFIGURED));
        p.setProperty("password", System.getProperty("DB_PASSWORD", NOT_CONFIGURED));
    }
    
    public static Boolean isValid() {
        INSTANCE.p.keySet().forEach(key -> {
            if (INSTANCE.p.get(key).equals(NOT_CONFIGURED)) {
                LOGGER.warn("Env variable <{}> not assigned", key);
            }
        });
        return !INSTANCE.p.containsValue(NOT_CONFIGURED);
    }
    
    public static Properties get() {
        return INSTANCE.p;
    }
}
