package br.ejcb.cfp.seguranca.ms.rest.api;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;

@QuarkusTest
public class UsuarioResourceTest {

	@Test
    public void testGetRecords() {
        given()
          .when().get("/v1/usuarios")
          .then()
             .statusCode(Response.Status.OK.getStatusCode())
//             .contentType(ContentType.JSON)
//             .body("$.size()", equalTo(2))
             ;
    }
}