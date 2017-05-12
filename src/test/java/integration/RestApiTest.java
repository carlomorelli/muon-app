package integration;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.csoft.muon.App;
import com.csoft.muon.domain.Item;
import com.csoft.muon.repository.Repository;
import com.csoft.muon.repository.RepositoryImpl;
import com.csoft.muon.repository.datasource.DataSourceFactory;
import com.csoft.muon.utils.RandomUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.http.ContentType;

public class RestApiTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestApiTest.class);

    private final ObjectMapper mapper = new ObjectMapper();
    private App server;
    

    @BeforeClass
    public void setupClass() {
        server = new App(new RepositoryImpl(DataSourceFactory.getH2DataSource()));
        LOGGER.info("Starting SimpleServer...");
        server.startServer();
    }

    @AfterClass
    public void tearDown() {
        LOGGER.info("Stopping SimpleServer...");
        server.stopServer();
    }

    @Test
    public void testGetItem() {
        when()
            .get("/webapi/items/1")
            .then()
            .assertThat()
            .statusCode(200)
            .and()
            .body("id", equalTo(1));
        when()
            .get("/webapi/items/2")
            .then()
            .assertThat()
            .statusCode(200)
            .and()
            .body("id", equalTo(2));
        
    }

    @Test
    public void testGetAllItems() {
        when()
            .get("/webapi/items")
            .then()
            .assertThat()
            .statusCode(200)
            .and()
            .body("total", greaterThanOrEqualTo(2))
            .and()
            .body("items", hasSize(greaterThanOrEqualTo(2)));
    }

    @Test
    public void testPostItem() throws IOException {
        Item item = RandomUtils.randomItem(3);
        String body = mapper.writeValueAsString(item);
        given()
            .contentType(ContentType.JSON)
            .body(body)
            .when()
            .post("/webapi/items")
            .then()
            .assertThat()
            .statusCode(200);
    }

}
