package integration;

import static com.csoft.muon.utils.JsonFunctions.dumpJson;
import static com.csoft.muon.utils.RandomFunctions.randomItem;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isEmptyOrNullString;


import java.io.IOException;
import java.security.SecureRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.csoft.muon.domain.Item;
import com.csoft.muon.domain.ItemsDto;
import com.csoft.muon.events.HttpErrorEvent;
import com.csoft.muon.utils.RandomFunctions;

import io.restassured.http.ContentType;

/**
 * Test class covering the HTTP API interactions with frontend clients.
 * 
 * @author Carlo Morelli
 *
 */
public class RestApiTest extends BaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestApiTest.class);

    @Test
    public void testGetAllContent() {
        LOGGER.info("Asserting retrieval of all objects in table...");
        ItemsDto items = when().get("/webapi/items")
                .then().assertThat().statusCode(200)
                .extract().as(ItemsDto.class);
        assertThat(items.getTotal(), greaterThanOrEqualTo(0));

    }

    @Test
    public void testSubmitARandomItemAndAssertItIsRetrievable() throws IOException {
        Integer index = new SecureRandom().nextInt(10000);
        Item item = randomItem(index);
        LOGGER.info("Asserting post and retrieval of object...");

        // submit item
        given().contentType(ContentType.JSON).body(dumpJson(item))
                .when().post("/webapi/items")
                .then().assertThat().statusCode(200);

        // check that it can be fetched back
        Item savedItem = when().get("/webapi/items/" + index)
                .then().assertThat().statusCode(200)
                .extract().as(Item.class);
        assertThat(savedItem, equalTo(item));

        // check that is appears also in the list of stored items
        long count = when().get("/webapi/items")
                .then().assertThat().statusCode(200)
                .body("total", greaterThanOrEqualTo(0))
                .body("items", hasSize(greaterThanOrEqualTo(0)))
                .extract().as(ItemsDto.class)
                .getItems().stream().filter(x -> x.equals(item)).count();
        assertThat(count, equalTo(1L));
    }

    @DataProvider
    public Object[][] postNegativeCases() {
        return new Object[][] {
                {
                    "{\"index\":-1,\"label\":\"label-3436\"}",
                    HttpErrorEvent.SC_403_INVALID_INDEX_IN_BODY
                },
                {
                    "{\"index\":135,\"label\":\"\"}",
                    HttpErrorEvent.SC_403_MISSING_FIELDS_IN_BODY
                },
                {
                    "{\"label\":\"yadayada\"}",
                    HttpErrorEvent.SC_403_MISSING_FIELDS_IN_BODY
                },
                {
                    RandomFunctions.randomString(20),
                    HttpErrorEvent.SC_403_MALFORMED_BODY
                },
                {
                    "",
                    HttpErrorEvent.SC_403_MALFORMED_BODY
                }
        };
    }

    @Test(dataProvider = "postNegativeCases")
    public void testPostNegativeCases(String bodyToSend, HttpErrorEvent expectedEvent) {
        given().body(bodyToSend).contentType(ContentType.JSON)
                .when().post("/webapi/items")
                .then().assertThat()
                .statusCode(expectedEvent.getStatusCode())
                .body(equalTo(expectedEvent.getErrorMsg()));
    }

    @Test
    public void testGetWithBody() {
        String bodyToSend = RandomFunctions.randomString(20);
        HttpErrorEvent expectedEvent = HttpErrorEvent.SC_400_FORBIDDEN_BODY;
        given().body(bodyToSend).contentType(ContentType.JSON)
                .when().get("/webapi/items/1")
                .then().assertThat()
                .statusCode(expectedEvent.getStatusCode())
                .body(equalTo(expectedEvent.getErrorMsg()));
    }

    @Test
    public void testGetAllWithBody() {
        String bodyToSend = RandomFunctions.randomString(20);
        HttpErrorEvent expectedEvent = HttpErrorEvent.SC_400_FORBIDDEN_BODY;
        given().body(bodyToSend).contentType(ContentType.JSON)
                .when().get("/webapi/items")
                .then().assertThat()
                .statusCode(expectedEvent.getStatusCode())
                .body(equalTo(expectedEvent.getErrorMsg()));
    }
    
    @Test
    public void testVersionPage() {
        when().get("/version")
                .then().assertThat()
                .statusCode(200)
                .body("name", not(isEmptyOrNullString()))
                .body("version", not(isEmptyOrNullString()));
    }

}
