package br.ejcb.cfp.financeiro.ms.rest.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/v1/accounts")
public class ContaResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Test";
    }

}
