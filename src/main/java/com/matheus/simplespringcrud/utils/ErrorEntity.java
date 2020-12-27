package com.matheus.simplespringcrud.utils;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.matheus.simplespringcrud.exceptions.HttpException;
import org.springframework.http.HttpStatus;
import springfox.documentation.annotations.ApiIgnore;

@JsonPropertyOrder({
        "code",
        "status_code",
        "message"
})
@ApiIgnore
public class ErrorEntity extends HttpException {

    public ErrorEntity(String message, HttpStatus statusCode) { super(message, statusCode); }

    public int getCode() { return this.getStatusCode().value(); }

}
