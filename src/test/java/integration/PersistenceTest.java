package integration;

import static com.csoft.muon.utils.JsonFunctions.dumpJson;
import static com.csoft.muon.utils.RandomFunctions.randomItem;
import static com.csoft.muon.utils.RandomFunctions.randomString;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.csoft.muon.domain.Item;

import io.restassured.http.ContentType;

/**
 * The integration test will assume that the DB is not flushed. 
 * Currently the database is forcefully truncated when the unit tests run, but this
 * will change in the future.
 * 
 * @author Carlo Morelli
 *
 */
public class PersistenceTest extends BaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersistenceTest.class);
    private Sql2o sql2o;
    
    @BeforeClass
    public void setupClass() {
    	sql2o = new Sql2o(ds); //datasouce is a protected field from BaseClass
    }
	
//    private Server server;
    
//    @BeforeClass
//    public void setupClass() {
//        try {
//            server = Server.createTcpServer("-tcpPort", "9093").start();
//            LOGGER.info("Started H2 server [{}]", server.getStatus());
//        } catch (SQLException e) {
//            LOGGER.error("Starting H2 server failed!");
//            throw new RuntimeException(e);
//        }
//    }
//    
//    @AfterClass
//    public void teardownClass() {
//        server.stop();
//        LOGGER.info("Stopped H2 server [{}]", server.getStatus());
//    }
    
    @Test
    public void testInsertItemsAndVerify() throws IOException {
    	
    	// items to insert
    	final int NUM = 10;

    	// generate NUM random indexes and attached Item objects
    	final Random rnd = new SecureRandom();
    	List<Integer> indexes = IntStream.range(0, NUM).mapToObj(it -> rnd.nextInt(10000)).collect(toList());
    	List<Item> items = IntStream.range(0, NUM).mapToObj(it -> randomItem(indexes.get(it))).collect(toList());

    	// submit NUM items via REST API
    	for (int it = 0; it < NUM; it++) {
	    	String body = dumpJson(items.get(it));
	    	LOGGER.info("Registering item '{}'...", body);
	    	given()
	    		.contentType(ContentType.JSON)
	    		.body(body)
	    		.when()
	    		.post("/webapi/items")
	    		.then()
	    		.assertThat()
	    		.statusCode(200);
    	}

    	// assert all NUM items are persisted in DB
    	LOGGER.info("Verifying storage of items...");
    	try (Connection conn = sql2o.open()) {
    		for (int it=0; it < NUM; it++) {
	    		List<Item> list = conn.createQuery("SELECT * FROM items WHERE (index) IS (:index)")
	    				.addParameter("index", indexes.get(it))
	                    .executeAndFetch(Item.class);
	            assertThat(list.size(), equalTo(1));
	            assertThat(list.get(0), equalTo(items.get(it)));
    		}
        }
    }
    
    @Test
    public void testModifyItemInDb() throws IOException {
    	
    	// generate random item and submit via REST API
    	Integer index = new SecureRandom().nextInt(10000);
    	Item originalItem = randomItem(index);
    	Item modifiedItem = new Item(originalItem.getIndex(), randomString(10));

    	String body = dumpJson(originalItem);
    	LOGGER.info("Registering item '{}'...", body);
    	Item submittedItem = given()
    		.contentType(ContentType.JSON)
    		.body(body)
    		.when()
    		.post("/webapi/items")
    		.then()
    		.assertThat()
    		.statusCode(200)
    		.contentType(ContentType.JSON)
    		.extract().body().as(Item.class);
    	assertThat(submittedItem, equalTo(originalItem));

    	// find item in DB and change label value
    	LOGGER.info("Verifying storage of item and modifying attribute...");
    	try (Connection conn = sql2o.open()) {
    		List<Item> list = conn.createQuery("SELECT * FROM items WHERE (index) IS (:index)")
    				.addParameter("index", index)
                    .executeAndFetch(Item.class);
            assertThat(list.size(), equalTo(1));
            assertThat(list.get(0), equalTo(originalItem));
            conn.createQuery("UPDATE items SET label=:label WHERE index=:index")
		            .bind(modifiedItem)
		            .executeUpdate();
        }
    	
    	// retrieve item from REST API and assert is modified
    	LOGGER.info("Retrieving modified item...");
    	Item retrievedItem = when()
			.get("/webapi/items/" + originalItem.getIndex())
			.then()
			.assertThat()
			.statusCode(200)
			.contentType(ContentType.JSON)
			.extract().body().as(Item.class);
    	assertThat(retrievedItem, equalTo(modifiedItem));
    	assertThat(retrievedItem, not(equalTo(originalItem)));
    }
    
}
