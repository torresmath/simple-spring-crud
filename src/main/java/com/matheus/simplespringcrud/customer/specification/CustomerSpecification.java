package com.matheus.simplespringcrud.customer.specification;

import com.matheus.simplespringcrud.customer.model.Customer;
import com.matheus.simplespringcrud.customer.model.Customer_;
import org.springframework.data.jpa.domain.Specification;

public class CustomerSpecification {

    private CustomerSpecification() { throw new IllegalStateException("Utility Class"); }

    public static Specification<Customer> withIdCustomer(Long idCustomer) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(Customer_.idCustomer), idCustomer);
    }

    public static Specification<Customer> withIdCustomerNotEqual(Long idCustomer) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.notEqual(root.get(Customer_.idCustomer), idCustomer);
    }

    public static Specification<Customer> withEmail(String email) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(Customer_.email), email);
    }

    public static Specification<Customer> withCpf(String cpf) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(Customer_.cpf), cpf);
    }

    public static Specification<Customer> withStatusActive() {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(Customer_.status), true);
    }
}
