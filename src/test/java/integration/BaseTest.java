package integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Guice;

import com.csoft.muon.App;
import com.csoft.muon.AppConfig;
import com.google.inject.Inject;

import io.restassured.RestAssured;

@Guice(modules = AppConfig.class)
public class BaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseTest.class);

    @Inject
    protected App application;
    
    @BeforeClass
    public void setupClass() {
        LOGGER.info("Starting application...");
        application.startServer();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
    
    @AfterClass
    public void teardownClass() {
        LOGGER.info("Stopping application...");
        application.stopServer();
    }
	
}
