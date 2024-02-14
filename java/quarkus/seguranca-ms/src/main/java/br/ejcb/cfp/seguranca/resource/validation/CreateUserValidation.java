package br.ejcb.cfp.seguranca.resource.validation;

import java.util.Set;

import br.ejcb.cfp.seguranca.api.dto.CreateUserRequest;
import br.ejcb.cfp.seguranca.resource.exceptions.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class CreateUserValidation {
	
	/**
	 * validar dados obrigatorios da requisicao
	 * @param request
	 * @throws ValidationException
	 */
	public static void validate(CreateUserRequest request) throws ValidationException {
		requestValidate(request);
		constraintsValidate(request);
	}

	private static void requestValidate(CreateUserRequest request) throws ValidationException {
		if (request == null) {
			throw new ValidationException("'request' não informado!");
		}
	}
	
	private static void constraintsValidate(CreateUserRequest request) throws ValidationException {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        Set<ConstraintViolation<CreateUserRequest>> constraintViolation = validator.validate(request);
        if (constraintViolation != null && !constraintViolation.isEmpty()) {
        	StringBuilder erros = new StringBuilder();
        	int contador = 0;
        	for (ConstraintViolation<CreateUserRequest> violation : constraintViolation) {
        		erros.append(++contador)
        				.append(". ")
        				.append(violation.getMessage())
        				.append("; ");
        	}
        	throw new ValidationException("Validação dos atributos da requisição: "
        			+ erros.substring(0, erros.length()-2));
        }
	}
	
	// validar uma pre existencia de usuario com mesmo nome
	
	// validar uma pre existencia de usuario com mesmo email
	
	// validar uma pre existencia de usuario com mesmo login


	
}
