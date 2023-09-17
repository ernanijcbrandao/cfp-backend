package br.ejcb.cfp.seguranca.ms.rest.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;

@QuarkusTest
public class UsuarioResourceTest {

	@Test
    public void listarUsuariosAtivosTest() {
        given()
          .when().get("/api/v1/usuarios")
          .then()
             .statusCode(Response.Status.OK.getStatusCode())
             .contentType(ContentType.JSON)
             .body("$.size()", is(2))
             ;
    }
	
}