package com.csoft.muon.provider;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum FileConfProvider {

    INSTANCE;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(FileConfProvider.class);
    private static final String NOT_CONFIGURED = "notConfigured";
    
    private static final String CONF_FILE = "application.properties";
    
    private Properties p;
    
    FileConfProvider() {
        try {
            p = new Properties();
            p.load(getClass().getClassLoader().getResourceAsStream(CONF_FILE));
        } catch (IOException e) {
            throw new RuntimeException("Unable to access configuration file <" + CONF_FILE + ">.", e);
        }
        p.setProperty("datasourcetype", (String) p.getOrDefault("datasourcetype", NOT_CONFIGURED));
        p.setProperty("hostname", (String) p.getOrDefault("hostname", NOT_CONFIGURED));
        p.setProperty("port", (String) p.getOrDefault("port", NOT_CONFIGURED));
        p.setProperty("database", (String) p.getOrDefault("database", NOT_CONFIGURED));
        p.setProperty("username", (String) p.getOrDefault("username", NOT_CONFIGURED));
        p.setProperty("password", (String) p.getOrDefault("password", NOT_CONFIGURED));
    }
    
    public static Boolean isValid() {
        INSTANCE.p.keySet().forEach(key -> {
            if (INSTANCE.p.get(key).equals(NOT_CONFIGURED)) {
                LOGGER.debug("File variable <{}> not assigned", key);
            }
        });
        return !INSTANCE.p.containsValue(NOT_CONFIGURED);
    }

    public static Properties get() {
        return INSTANCE.p;
    }

}
