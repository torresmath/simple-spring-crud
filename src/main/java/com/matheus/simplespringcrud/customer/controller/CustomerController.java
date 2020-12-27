package com.matheus.simplespringcrud.customer.controller;

import com.matheus.simplespringcrud.customer.dto.CustomerDto;
import com.matheus.simplespringcrud.customer.form.CustomerForm;
import com.matheus.simplespringcrud.customer.service.CustomerService;
import com.matheus.simplespringcrud.customer.specification.CustomerSpecification;
import com.matheus.simplespringcrud.exceptions.BadRequestException;
import com.matheus.simplespringcrud.exceptions.HttpException;
import com.matheus.simplespringcrud.exceptions.NotFoundException;
import com.matheus.simplespringcrud.utils.*;
import io.swagger.annotations.*;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/customer")
@Api(tags = "Customer Controller")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved Customer", response = CustomerDto.class),
            @ApiResponse(code = 404, message = "Customer not found", response = ErrorEntitySwagger.class)
    })
    @GetMapping("/{idCustomer}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Retrieve a customer")
    public ResponseEntity<Entity> getCustomer(@PathVariable Long idCustomer) {
        log.info("CONTROLLER - GET CUSTOMER {}", idCustomer);

        Optional<CustomerDto> customer = customerService.getCustomerDto(idCustomer);
        return customer
                .map(ResponseUtils::toOkResponse)
                .orElseGet(() -> ResponseUtils.errorToResponse(new NotFoundException("Customer not found")));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved Customer", response = ListEntity.class),
            @ApiResponse(code = 404, message = "Customer not found", response = ErrorEntitySwagger.class)
    })
    @ApiOperation(value = "Retrieve a list of active customers")
    public ResponseEntity<Entity> getCustomers() {
        log.info("CONTROLLER - GET ALL ACTIVE CUSTOMERS");

        Optional<List<CustomerDto>> customers = customerService.getCustomers(CustomerSpecification.withStatusActive());
        return customers
                .map(ResponseUtils::listToOkResponse)
                .orElse(ResponseUtils.errorToResponse(new NotFoundException("Not found customers")));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Successfully created customer", response = CustomerDto.class),
            @ApiResponse(code = 400, message = "Invalid form param", response = ErrorEntitySwagger.class),
            @ApiResponse(code = 409, message = "Provided CPF or Email already in use", response = ErrorEntitySwagger.class)
    })
    @ApiOperation(value = "Create a customer")
    public ResponseEntity<Entity> saveCustomer(@RequestBody @Valid CustomerForm customerForm,
                                               BindingResult bindingResult) {
        log.info("CONTROLLER - SAVE CUSTOMER");

        if (bindingResult.hasErrors()) {
            log.info("CONTROLLER - ERROR - Validation Errors");

            return ResponseUtils.errorToResponse(bindingResult);
        }

        Either<HttpException, CustomerDto> customer = customerService.saveCustomer(customerForm);
        return customer
                .fold(
                        (ResponseUtils::errorToResponse),
                        (s -> ResponseUtils.toCreatedResponse(customer))
                );
    }

    @PutMapping("/{idCustomer}")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully updated customer", response = CustomerDto.class),
            @ApiResponse(code = 400, message = "Invalid form param", response = ErrorEntitySwagger.class),
            @ApiResponse(code = 404, message = "Customer not found", response = ErrorEntitySwagger.class),
            @ApiResponse(code = 409, message = "Provided CPF or Email already in use", response = ErrorEntitySwagger.class)
    })
    @ApiOperation(value = "Update a customer")
    public ResponseEntity<Entity> updateCustomer(@PathVariable Long idCustomer,
                                                 @RequestBody @Valid CustomerForm customerForm,
                                                 BindingResult bindingResult) {
        log.info("CONTROLLER - UPDATE CUSTOMER {}", idCustomer);

        if (bindingResult.hasErrors()) {
            log.info("CONTROLLER - ERROR - Validation Errors");

            return ResponseUtils.errorToResponse(bindingResult);
        }

        Either<HttpException, CustomerDto> updatedCustomer = customerService.updateCustomer(idCustomer, customerForm);

        return updatedCustomer
                .fold(
                        (ResponseUtils::errorToResponse),
                        (s -> ResponseUtils.toOkResponse(updatedCustomer.get()))
                );
    }

    @PatchMapping("/{idCustomer}")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully updated customer's email", response = CustomerDto.class),
            @ApiResponse(code = 404, message = "Customer not found", response = ErrorEntitySwagger.class),
            @ApiResponse(code = 409, message = "Email already in use", response = ErrorEntitySwagger.class)
    })
    @ApiOperation(value = "Return customer's email")
    public ResponseEntity<Entity> updateCustomerEmail(@PathVariable Long idCustomer,
                                                      @RequestParam String email) {

        log.info("CONTROLLER - UPDATE CUSTOMER {} EMAIL", idCustomer);

        if (email == null || email.isEmpty()) {
            return ResponseUtils.errorToResponse(new BadRequestException("Email cannot be null or empty"));
        }

        Either<HttpException, CustomerDto> updatedCustomer = customerService.updateCustomerEmail(idCustomer, email);

        return updatedCustomer
                .fold(
                        (ResponseUtils::errorToResponse),
                        (s -> ResponseUtils.toOkResponse(updatedCustomer.get()))
                );
    }

    @DeleteMapping("/{idCustomer}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(code = 204, message = "Successfully deleted customer", response = CustomerDto.class),
            @ApiResponse(code = 404, message = "Customer not found", response = ErrorEntitySwagger.class),
    })
    @ApiOperation(value = "Delete a customer")
    public ResponseEntity<Entity> deleteCustomer(@PathVariable Long idCustomer) {
        log.info("CONTROLLER - DELETE CUSTOMER {}", idCustomer);

        return customerService.deleteCustomer(idCustomer)
                .map(ResponseUtils::errorToResponse)
                .orElse(ResponseEntity.noContent().build());
    }

}
