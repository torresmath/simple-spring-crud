package com.matheus.simplespringcrud.customer.service;

import com.matheus.simplespringcrud.customer.dto.CustomerDto;
import com.matheus.simplespringcrud.customer.form.CustomerForm;
import com.matheus.simplespringcrud.customer.model.Customer;
import com.matheus.simplespringcrud.customer.repository.CustomerRepository;
import com.matheus.simplespringcrud.exceptions.HttpException;
import io.vavr.control.Either;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceTest {

    private static final String CUSTOMER_NOT_FOUND = "Customer not found";
    private static final String EMAIL_ALREADY_IN_USE = "Provided email is already in use";
    private static final String CPF_ALREADY_IN_USE = "Provided CPF is already in use";

    @Mock
    private CustomerRepository customerRepository;

    @Autowired
    @InjectMocks
    private CustomerService customerService;

    @Test
    public void test_save_customer() {

        Customer mockCustomer = Customer.builder()
                .idCustomer(1L)
                .build();

        when(customerRepository.save(any(Customer.class)))
                .thenReturn(mockCustomer);

        when(customerRepository.findAll(ArgumentMatchers.<Specification<Customer>>any()))
                .thenReturn(Collections.emptyList())
                .thenReturn(Collections.emptyList());

        CustomerForm customerForm = new CustomerForm();

        Either<HttpException, CustomerDto> customer = customerService.saveCustomer(customerForm);

        assertTrue(customer.isRight());
    }

    @Test
    public void test_save_customer_invalid_cpf() {
        when(customerRepository.findAll(ArgumentMatchers.<Specification<Customer>>any()))
                .thenReturn(Collections.singletonList(Customer.builder().build()))
                .thenReturn(Collections.emptyList());

        CustomerForm customerForm = new CustomerForm();

        Either<HttpException, CustomerDto> customer = customerService.saveCustomer(customerForm);

        assertFalse(customer.isRight());
        assertEquals(
                CPF_ALREADY_IN_USE,
                customer.getLeft().getMessage()
        );
        verify(customerRepository, times(0)).save(any(Customer.class));
    }

    @Test
    public void test_save_customer_invalid_email() {
        when(customerRepository.findAll(ArgumentMatchers.<Specification<Customer>>any()))
                .thenReturn(Collections.emptyList())
                .thenReturn(Collections.singletonList(Customer.builder().build()));

        Either<HttpException, CustomerDto> customer = customerService.saveCustomer(new CustomerForm());

        assertFalse(customer.isRight());
        assertEquals(
                EMAIL_ALREADY_IN_USE,
                customer.getLeft().getMessage()
        );
        verify(customerRepository, times(0)).save(any(Customer.class));
    }

    @Test
    public void test_update_customer() {

        Customer mockCustomer = Customer.builder()
                .idCustomer(1L)
                .build();

        when(customerRepository.save(any(Customer.class)))
                .thenReturn(mockCustomer);

        when(customerRepository.findOne(ArgumentMatchers.<Specification<Customer>>any()))
                .thenReturn(Optional.of(new Customer()));

        when(customerRepository.findAll(ArgumentMatchers.<Specification<Customer>>any()))
                .thenReturn(Collections.emptyList())
                .thenReturn(Collections.emptyList());

        Either<HttpException, CustomerDto> customer = customerService.updateCustomer(1L, new CustomerForm());

        assertTrue(customer.isRight());
    }

    @Test
    public void test_update_customer_not_found() {
        when(customerRepository.findOne(ArgumentMatchers.<Specification<Customer>>any()))
                .thenReturn(Optional.empty());

        Either<HttpException, CustomerDto> customer = customerService.updateCustomer(1L, new CustomerForm());

        assertFalse(customer.isRight());
        verify(customerRepository, times(0)).save(any(Customer.class));
    }

    @Test
    public void test_update_customer_invalid_cpf() {
        when(customerRepository.findOne(ArgumentMatchers.<Specification<Customer>>any()))
                .thenReturn(Optional.of(new Customer()));

        when(customerRepository.findAll(ArgumentMatchers.<Specification<Customer>>any()))
                .thenReturn(Collections.singletonList(new Customer()))
                .thenReturn(Collections.emptyList());

        Either<HttpException, CustomerDto> customer = customerService.updateCustomer(1L, new CustomerForm());

        assertFalse(customer.isRight());
        assertEquals(
                CPF_ALREADY_IN_USE,
                customer.getLeft().getMessage()
        );
        verify(customerRepository, times(0)).save(any(Customer.class));
    }

    @Test
    public void test_update_customer_invalid_email() {
        when(customerRepository.findOne(ArgumentMatchers.<Specification<Customer>>any()))
                .thenReturn(Optional.of(new Customer()));

        when(customerRepository.findAll(ArgumentMatchers.<Specification<Customer>>any()))
                .thenReturn(Collections.emptyList())
                .thenReturn(Collections.singletonList(new Customer()));

        Either<HttpException, CustomerDto> customer = customerService.updateCustomer(1L, new CustomerForm());

        assertFalse(customer.isRight());
        assertEquals(
                EMAIL_ALREADY_IN_USE,
                customer.getLeft().getMessage()
        );
        verify(customerRepository, times(0)).save(any(Customer.class));
    }
}