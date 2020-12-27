package com.matheus.simplespringcrud.customer.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerForm {

    @NotNull(message = "Field name cannot be null")
    @NotEmpty(message = "Field name cannot be empty")
    @ApiModelProperty(position = 1, example = "John Doe")
    private String name;

    @NotNull(message = "Field email cannot be null")
    @NotEmpty(message = "Field email cannot be empty")
    @ApiModelProperty(position = 2, example = "johndoe@gmail.com")
    private String email;

    @NotNull(message = "Field cpf cannot be null")
    @NotEmpty(message = "Field cpf cannot be empty")
    @ApiModelProperty(position = 3, example = "425497123-45")
    private String cpf;

    @NotNull(message = "Field birth Date cannot be null")
    @JsonProperty("birth_date")
    @ApiModelProperty(position = 4, example = "1995-10-04")
    private Date birthDate;
}
