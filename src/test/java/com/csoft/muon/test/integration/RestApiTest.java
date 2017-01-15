package com.csoft.muon.test.integration;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.csoft.muon.SimpleServer;
import com.csoft.muon.lib.RestUtils;
import com.csoft.muon.repository.Repository;
import com.csoft.muon.repository.RepositoryImpl;

import io.restassured.http.ContentType;

public class RestApiTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestApiTest.class);

    private SimpleServer server;

    @BeforeClass
    public void setupClass() {
        server = new SimpleServer(new RepositoryImpl());
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
    public void testPostItem() {
        given()
            .contentType(ContentType.JSON)
            .body(RestUtils.getRandomBody(3).toString())
            .when()
            .post("/webapi/items")
            .then()
            .assertThat()
            .statusCode(200);
    }

}
