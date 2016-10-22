package com.csoft.muon.test.integration;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import static org.hamcrest.Matchers.hasSize;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.csoft.muon.SimpleServer;
import com.csoft.muon.lib.RestUtils;

import io.restassured.http.ContentType;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class RestApiTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleServer.class);

    private SimpleServer server;

    @BeforeClass
    public void setupClass() {
        server = new SimpleServer();
        LOGGER.info("Starting SimpleServer...");
        server.start();
    }

    @AfterClass
    public void tearDown() {
        LOGGER.info("Stopping SimpleServer...");
        server.stop();
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
    public void testPutItem() {
        given()
            .contentType(ContentType.JSON)
            .body(RestUtils.getRandomBody(3).toString())
            .when()
            .put("/webapi/items/3")
            .then()
            .assertThat()
            .statusCode(200);
    }

}
