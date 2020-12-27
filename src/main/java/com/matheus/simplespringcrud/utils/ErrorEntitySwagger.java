package com.matheus.simplespringcrud.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@ApiModel(value = "ErrorEntity")
@Getter
@Setter(value = AccessLevel.NONE)
public class ErrorEntitySwagger {
    @ApiModelProperty(example = "400")
    private int code;
    @ApiModelProperty(example = "BAD_REQUEST")
    private HttpStatus statusCode;
    @ApiModelProperty(example = "Error message")
    private String message;
}
