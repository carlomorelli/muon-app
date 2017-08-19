package com.csoft.muon.provider;

import java.util.Map;
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
        Map<String, String> env = System.getenv();
        p.setProperty("datasourcetype", env.getOrDefault("DB_TYPE", NOT_CONFIGURED));
        p.setProperty("hostname", env.getOrDefault("DB_HOSTNAME", NOT_CONFIGURED));
        p.setProperty("port", env.getOrDefault("DB_PORT", NOT_CONFIGURED));
        p.setProperty("database", env.getOrDefault("DB_NAME", NOT_CONFIGURED));
        p.setProperty("username", env.getOrDefault("DB_USERNAME", NOT_CONFIGURED));
        p.setProperty("password", env.getOrDefault("DB_PASSWORD", NOT_CONFIGURED));
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
