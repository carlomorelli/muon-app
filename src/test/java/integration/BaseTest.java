package integration;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Guice;

import com.csoft.muon.App;
import com.csoft.muon.AppConfig;
import com.google.inject.Inject;

import io.restassured.RestAssured;

/**
 * Base class for all Integration Tests
 * @author Carlo Morelli
 *
 */
@Guice(modules = AppConfig.class)
public class BaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseTest.class);

    @Inject
    protected App application;
    
    @Inject
    protected DataSource ds;
    
    @BeforeSuite
    public void setupSuite() {
        LOGGER.info("Starting application...");
        application.startServer();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
    
    @AfterSuite
    public void teardownSuite() {
        LOGGER.info("Stopping application...");
        application.stopServer();
    }
	
}
