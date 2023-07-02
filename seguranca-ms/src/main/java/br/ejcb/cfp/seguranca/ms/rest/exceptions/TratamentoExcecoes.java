package br.ejcb.cfp.seguranca.ms.rest.exceptions;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class TratamentoExcecoes implements ExceptionMapper<Exception> {

	@Override
	public Response toResponse(Exception exception) {
		
		Response response = tratarExcecaoDeValidacao(null, exception);
		response = tratarExcecaoDeRegistroNaoEncontrado(response, exception);
		
		response = tratarExcecaoFalhaInesperada(response, exception);
		
		return response;
	}
	
	private Response tratarExcecaoDeValidacao(Response response, Exception exception) {
		if (response == null && exception instanceof ValidationException) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erro de validação")
                    .build();
		}
        return null;
	}
	
	private Response tratarExcecaoDeRegistroNaoEncontrado(Response response, Exception exception) {
		if (response == null && exception instanceof EntityNotFoundException) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Registro não encontrado")
                    .build();
        }
        return null;
	}
	
	private Response tratarExcecaoFalhaInesperada(Response response, Exception exception) {
		if (response == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Ocorreu um erro interno no servidor")
                    .build();
        }
        return response;
	}
	
}
