package br.ejcb;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class AuthenticateResourceTest {
    @Test
    void testHelloEndpoint() {
        given()
          .when().get("/authenticate")
          .then()
             .statusCode(200)
             .body(is("Hello from RESTEasy Reactive"));
    }

}