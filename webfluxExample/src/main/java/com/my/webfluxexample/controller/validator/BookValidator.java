package com.my.webfluxexample.controller.validator;

import com.my.webfluxexample.dto.BookDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Component
public class BookValidator<T>{
    private final Validator validator;

    public BookValidator(Validator validator) {
        this.validator = validator;
    }

    public void validate(T body){
        Errors errors = new BeanPropertyBindingResult(body, body.getClass().getName());

        this.validator.validate(body, errors);

        if(errors.hasErrors()){
            onValidationErrors(errors);
        }
    }

    private void onValidationErrors(Errors errors) {
        log.error(errors.getAllErrors().toString());
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                errors.getAllErrors().toString());
    }
}
