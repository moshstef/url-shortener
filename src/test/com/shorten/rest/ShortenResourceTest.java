package com.shorten.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shorten.api.ShortenRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class ShortenResourceTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @TestHTTPEndpoint(ShortenResource.class)

    @Test
    public void testShorten() throws MalformedURLException, JsonProcessingException {
        for (int i = 0; i < 1000; i++) {
            System.out.println("Running test " + i);
            String realUrl = "http://mylongurl.com/" + UUID.randomUUID();
            String shortUrl = given()
                    .body(objectMapper.writeValueAsString(new ShortenRequest(URI.create(realUrl).toURL())))
                    .header("Content-type", MediaType.APPLICATION_JSON)
                    .header("X-API-Key", "12345678")
                    .when().post("/api/shorten")
                    .then()
                    .statusCode(200)
                    .body("shortUrl", notNullValue())
                    .extract().path("shortUrl");

            given()
                    .when()
                    .get(shortUrl)
                    .then()
                    .statusCode(302)
                    .header("Location", realUrl);
        }

    }
}
