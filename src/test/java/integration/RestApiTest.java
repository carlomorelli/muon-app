package integration;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;

import java.io.IOException;
import java.security.SecureRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.csoft.muon.App;
import com.csoft.muon.domain.Item;
import com.csoft.muon.domain.ItemsDto;
import com.csoft.muon.handler.Result;
import com.csoft.muon.repository.RepositoryImpl;
import com.csoft.muon.repository.datasource.DataSourceFactory;
import com.csoft.muon.utils.RandomFunctions;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

/**
 * The integration test will assume that the DB is not flushed. 
 * Currently the database is forcefully truncated when the unit tests run, but this
 * will change in the future.
 * 
 * @author Carlo Morelli
 *
 */
public class RestApiTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestApiTest.class);

    private final ObjectMapper mapper = new ObjectMapper();
    private App server;
    

    @BeforeClass
    public void setupClass() {
        server = new App(new RepositoryImpl(DataSourceFactory.getH2DataSource()));
        LOGGER.info("Starting application...");
        server.startServer();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @AfterClass
    public void teardownClass() {
        LOGGER.info("Stopping application...");
        server.stopServer();
    }
    

    @Test
    public void testSubmitARandomItemAndAssertItIsPersisted() throws IOException {
    	Integer index = new SecureRandom().nextInt(10000);
        Item item = RandomFunctions.randomItem(index);

        // submit item
        given()
            .contentType(ContentType.JSON)
            .body(mapper.writeValueAsString(item))
            .when()
            .post("/webapi/items")
            .then()
            .assertThat()
            .statusCode(200);
    	
        // check that it can be fetched back
        Item savedItem = when()
            .get("/webapi/items/" + index)
            .then()
            .assertThat()
            .statusCode(200)
            .extract().body().as(Item.class);
        assertThat(savedItem, equalTo(item));

        // check that is appears also in the list of stored items
        long count = when()
        	.get("/webapi/items")
	        .then()
	        .assertThat()
	        .statusCode(200)
	        .body("total", greaterThanOrEqualTo(0))
	        .body("items", hasSize(greaterThanOrEqualTo(0)))
	        .and()
	        .extract().body().as(ItemsDto.class).getItems()
	        .stream().filter(x -> x.equals(item)).count();
        assertThat(count, equalTo(1L));
    }
    

    @DataProvider
    public Object[][] postNegativeCases() {
    	return new Object[][] {
    		{
    			"{\"index\":-1,\"label\":\"label-3436\"}",
    			new Result(403, "ClientError: invalid / null index or already used index in input item")
    		},
    		{
    			RandomFunctions.randomString(20),
    			new Result(403, "ClientError: malformed / unparsable input body")
    		}
    	};
    }
    
    @Test(dataProvider = "postNegativeCases")
    public void testPostNegativeCases(String bodyToSend, Result expectedResult) {
    	given()
    	.body(bodyToSend)
    	.contentType(ContentType.JSON)
    	.post("/webapi/items")
    	.then()
    	.assertThat()
    	.statusCode(expectedResult.getStatus())
    	.body(equalTo(expectedResult.getBody()));
    }
    
    @Test
    public void testGetWithBody() {
    	String bodyToSend = RandomFunctions.randomString(20);
    	Result expectedResult = new Result(400, "Forbidded to send body");
    	given()
    	.body(bodyToSend)
    	.contentType(ContentType.JSON)
    	.get("/webapi/items/1")
    	.then()
    	.assertThat()
    	.statusCode(expectedResult.getStatus())
    	.body(equalTo(expectedResult.getBody()));
    }

    @Test
    public void testGetAllWithBody() {
    	String bodyToSend = RandomFunctions.randomString(20);
    	Result expectedResult = new Result(400, "Forbidded to send body");
    	given()
    	.body(bodyToSend)
    	.contentType(ContentType.JSON)
    	.get("/webapi/items")
    	.then()
    	.assertThat()
    	.statusCode(expectedResult.getStatus())
    	.body(equalTo(expectedResult.getBody()));
    }

}
