package integration;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import org.testng.annotations.Test;

import com.csoft.muon.domain.HealthsDto;
import com.csoft.muon.events.HttpErrorEvent;
import com.csoft.muon.utils.RandomFunctions;

import io.restassured.http.ContentType;

public class HealthcheckTest extends BaseTest {


    @Test
    public void testVersionPage() {
        when().get("/version")
                .then().assertThat()
                .statusCode(200)
                .body("name", not(isEmptyOrNullString()))
                .body("version", not(isEmptyOrNullString()));
    }

    @Test
    public void testVersionPageWithBody() {
        String bodyToSend = RandomFunctions.randomString(20);
        HttpErrorEvent expectedEvent = HttpErrorEvent.SC_400_FORBIDDEN_BODY;
        given().body(bodyToSend).contentType(ContentType.JSON)
                .when().get("/version")
                .then().assertThat()
                .statusCode(expectedEvent.getStatusCode())
                .body(equalTo(expectedEvent.getErrorMsg()));
    }

    @Test
    public void testHealth() {
        HealthsDto health = when().get("/health")
                .then().assertThat()
                .statusCode(200)
                .extract().as(HealthsDto.class);
        assertThat(health.getComponents().size(), greaterThanOrEqualTo(1));
        assertThat(health.getComponents().get(0).getStatus(), equalTo("active"));
        
    }

    @Test
    public void testHealthWithBody() {
        String bodyToSend = RandomFunctions.randomString(20);
        HttpErrorEvent expectedEvent = HttpErrorEvent.SC_400_FORBIDDEN_BODY;
        given().body(bodyToSend).contentType(ContentType.JSON)
                .when().get("/version")
                .then().assertThat()
                .statusCode(expectedEvent.getStatusCode())
                .body(equalTo(expectedEvent.getErrorMsg()));
    }
    

    
}
