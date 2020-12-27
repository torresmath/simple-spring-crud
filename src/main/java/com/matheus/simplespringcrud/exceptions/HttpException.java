package com.matheus.simplespringcrud.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.matheus.simplespringcrud.utils.Entity;
import org.springframework.http.HttpStatus;
import springfox.documentation.annotations.ApiIgnore;

@JsonIgnoreProperties({
        "cause",
        "stackTrace",
        "suppressed",
        "localizedMessage"
})
@ApiIgnore
public class HttpException extends RuntimeException implements Entity {

    @JsonProperty("status_code")
    private HttpStatus statusCode;

    public HttpException() { }

    public HttpException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public HttpStatus getStatusCode() {
        return this.statusCode;
    }
}