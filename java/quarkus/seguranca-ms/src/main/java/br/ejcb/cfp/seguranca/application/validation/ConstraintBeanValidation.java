package br.ejcb.cfp.seguranca.application.validation;

import java.util.Set;

import br.ejcb.cfp.seguranca.application.exceptions.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class ConstraintBeanValidation {

	/**
	 * validar as constraints anotadas para o bean
	 * @param bean
	 * @throws ValidationException
	 */
	public static <T> void validate(T bean) throws ValidationException {
		
		if (bean == null) {
			throw new ValidationException("Nenhum dados foi não informado!");
		}
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        Set<ConstraintViolation<T>> constraintViolation = validator.validate(bean);
        if (constraintViolation != null && !constraintViolation.isEmpty()) {
        	StringBuilder erros = new StringBuilder();
        	int contador = 0;
        	for (ConstraintViolation<T> violation : constraintViolation) {
        		erros.append(++contador)
        				.append(". ")
        				.append(violation.getMessage())
        				.append("; ");
        	}
        	throw new ValidationException("Validação dos atributos informados: "
        			+ erros.substring(0, erros.length()-2));
        }
	}
	
}
