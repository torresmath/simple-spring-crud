package com.matheus.simplespringcrud.customer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.matheus.simplespringcrud.customer.model.Customer;
import com.matheus.simplespringcrud.utils.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({
        "id_customer",
        "name",
        "email",
        "cpf",
        "birth_date",
        "create_date",
        "update_date"
})
@ApiModel(value = "Customer")
public class CustomerDto implements Entity {

    public CustomerDto(Customer customer) {
        this.idCustomer = customer.getIdCustomer();
        this.name = customer.getName();
        this.cpf = customer.getCpf();
        this.email = customer.getEmail();
        this.birthDate = customer.getBirthDate();
        this.createDate = customer.getCreateDate();
        this.updateDate = customer.getUpdateDate();
        this.status = customer.getStatus();
    }

    @JsonProperty("id_customer")
    @ApiModelProperty(position = 1, example = "123456")
    private Long idCustomer;

    @ApiModelProperty(position = 2, example = "John Doe")
    private String name;

    @ApiModelProperty(position = 3, example = "johndoe@gmail.com")
    private String email;

    @ApiModelProperty(position = 4, example = "425497123-45")
    private String cpf;

    @JsonProperty("birth_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(position = 5, example = "1995-10-04")
    private Date birthDate;

    @JsonProperty("create_date")
    @ApiModelProperty(position = 6, example = "2020-12-20T16:30:00")
    private Date createDate;

    @JsonProperty("update_date")
    @ApiModelProperty(position = 7, example = "2020-12-20T16:30:00")
    private Date updateDate;

    @JsonIgnore
    private Boolean status = true;
}
