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
 * Base class for all Integration Tests.
 * 
 * The integration tests assume that the DB is not flushed, as application lifecycle is tested.
 * The test classes will be injected via the AppConfig.class Guice configuration, so the production
 * database will be used (H2, Postgres).
 * This differs from the unit tests, where only H2 is used.
 * 
 * WARNING: If the application is configured to use H2 and the integration tests are run after unit tests
 * (as in 'mvn clean verify'), the database will in any case be truncated: unit tests always flush the
 * db content.
 * 
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
