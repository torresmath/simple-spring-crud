package com.matheus.simplespringcrud.customer.service;

import com.matheus.simplespringcrud.customer.dto.CustomerDto;
import com.matheus.simplespringcrud.customer.form.CustomerForm;
import com.matheus.simplespringcrud.customer.model.Customer;
import com.matheus.simplespringcrud.customer.repository.CustomerRepository;
import com.matheus.simplespringcrud.customer.specification.CustomerSpecification;
import com.matheus.simplespringcrud.exceptions.ConflictException;
import com.matheus.simplespringcrud.exceptions.HttpException;
import com.matheus.simplespringcrud.exceptions.NotFoundException;
import com.matheus.simplespringcrud.utils.ResponseUtils;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class CustomerService {

    private static final String CUSTOMER_NOT_FOUND = "Customer not found";
    private static final String EMAIL_ALREADY_IN_USE = "Provided email is already in use";
    private static final String CPF_ALREADY_IN_USE = "Provided CPF is already in use";

    @Autowired
    private CustomerRepository customerRepository;

    private Optional<Customer> getCustomer(Long idCustomer) {
        log.info("SERVICE - GET CUSTOMER {}", idCustomer);

        Specification<Customer> query = CustomerSpecification.withIdCustomer(idCustomer)
                .and(CustomerSpecification.withStatusActive());

        return customerRepository.findOne(query);
    }

    public Optional<CustomerDto> getCustomerDto(Long idCustomer) {
        return getCustomer(idCustomer).map(CustomerDto::new);
    }

    public Optional<List<CustomerDto>> getCustomers(Specification<Customer> query) {
        log.info("SERVICE - GET CUSTOMERS");

        return ResponseUtils.listToOptional(customerRepository.findAll(query).stream()
                .map(CustomerDto::new).collect(Collectors.toList()));
    }

    public Either<HttpException, CustomerDto> saveCustomer(CustomerForm customerForm) {
        log.info("SERVICE - SAVE CUSTOMER");
        Specification<Customer> cpfQuery = CustomerSpecification.withCpf(customerForm.getCpf())
                .and(CustomerSpecification.withStatusActive());

        Specification<Customer> emailQuery = CustomerSpecification.withEmail(customerForm.getEmail())
                .and(CustomerSpecification.withStatusActive());

        Optional<HttpException> exceptionPromise = Stream.of(
                getCustomers(cpfQuery).map(e -> new ConflictException(CPF_ALREADY_IN_USE)),
                getCustomers(emailQuery).map(e -> new ConflictException((EMAIL_ALREADY_IN_USE)))
        ).filter(Optional::isPresent)
                .findFirst()
                .map(Optional::get);

        if (exceptionPromise.isPresent()) {
            HttpException exception = exceptionPromise.get();
            log.error("SERVICE - Validation error: {}", exception.getMessage());
            return Either.left(exception);
        }

        Customer customer = customerRepository.save(new Customer(customerForm));

        log.info("SERVICE - SUCCESS SAVE CUSTOMER {}", customer.getIdCustomer());
        return Either.right(new CustomerDto(customer));
    }

    public Either<HttpException, CustomerDto> updateCustomer(Long idCustomer, CustomerForm customerForm) {
        log.info("SERVICE - UPDATE CUSTOMER {}", idCustomer);

        Optional<Customer> customerPromise = getCustomer(idCustomer);

        if (!customerPromise.isPresent()) {
            log.error("SERVICE - ERROR - Customer not found {}", idCustomer);
            return Either.left(new NotFoundException(CUSTOMER_NOT_FOUND));
        }

        Specification<Customer> cpfQuery = CustomerSpecification.withCpf(customerForm.getCpf())
                .and(CustomerSpecification.withIdCustomerNotEqual(idCustomer))
                .and(CustomerSpecification.withStatusActive());

        Specification<Customer> emailQuery = CustomerSpecification.withEmail(customerForm.getEmail())
                .and(CustomerSpecification.withIdCustomerNotEqual(idCustomer))
                .and(CustomerSpecification.withStatusActive());

        Optional<HttpException> exceptionPromise = Stream.of(
                getCustomers(cpfQuery).map(e -> new ConflictException(CPF_ALREADY_IN_USE)),
                getCustomers(emailQuery).map(e -> new ConflictException((EMAIL_ALREADY_IN_USE)))
        ).filter(Optional::isPresent)
                .findFirst()
                .map(Optional::get);

        if (exceptionPromise.isPresent()) {
            HttpException exception = exceptionPromise.get();
            log.error("SERVICE - Validation error: {}", exception.getMessage());
            return Either.left(exception);
        }

        Customer customer = customerPromise.get();
        customer.update(customerForm);

        log.info("SERVICE - SUCCESS UPDATE CUSTOMER {}", idCustomer);
        return Either.right(new CustomerDto(customerRepository.save(customer)));
    }

    public Either<HttpException, CustomerDto> updateCustomerEmail(Long idCustomer, String email) {
        log.info("SERVICE - UPDATE CUSTOMER {} EMAIL", idCustomer);

        Optional<Customer> customerPromise = getCustomer(idCustomer);

        if (!customerPromise.isPresent()) {
            log.error("SERVICE - ERROR - Customer not found {}", idCustomer);
            return Either.left(new NotFoundException(CUSTOMER_NOT_FOUND));
        }

        Specification<Customer> emailQuery = CustomerSpecification.withEmail(email)
                .and(CustomerSpecification.withIdCustomerNotEqual(idCustomer))
                .and(CustomerSpecification.withStatusActive());

        if (getCustomers(emailQuery).isPresent()) {
            log.error("SERVICE - Validation error: Email {} already in use", email);
            return Either.left(new ConflictException((EMAIL_ALREADY_IN_USE)));
        }

        Customer customer = customerPromise.get();
        customer.setEmail(email);

        log.info("SERVICE - SUCCESS UPDATE CUSTOMER {} EMAIL", idCustomer);
        return Either.right(new CustomerDto(customerRepository.save(customer)));
    }

    public Optional<HttpException> deleteCustomer(Long idCustomer) {
        log.info("SERVICE - DELETE CUSTOMER {}", idCustomer);

        Optional<Customer> customerPromise = getCustomer(idCustomer);

        if (customerPromise.isPresent()) {
            Customer customer = customerPromise.get();
            customer.delete();
            customerRepository.save(customer);
            return Optional.empty();
        }

        return Optional.of(new NotFoundException(CUSTOMER_NOT_FOUND));
    }
}
