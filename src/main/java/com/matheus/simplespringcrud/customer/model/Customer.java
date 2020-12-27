package com.matheus.simplespringcrud.customer.model;

import com.matheus.simplespringcrud.customer.form.CustomerForm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.*;
import java.util.Date;

@Getter
@Entity
@Builder
@Table(name = "customer")
@AllArgsConstructor
@NoArgsConstructor
@ApiIgnore
public class Customer {

    public Customer(CustomerForm customerForm) {
        this.name = customerForm.getName();
        this.cpf = customerForm.getCpf();
        this.email = customerForm.getEmail();
        this.birthDate = customerForm.getBirthDate();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCustomer;

    private String name;

    private String email;

    private String cpf;

    private Date birthDate;

    @CreatedDate
    @Column(name = "create_date", updatable = false)
    private Date createDate;

    @LastModifiedDate
    @Column(name = "update_date", insertable = false)
    private Date updateDate;

    private Boolean status = true;

    public void setEmail(String email) {
        this.email = email;
    }

    @PrePersist
    protected void onCreate() {
        createDate = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updateDate = new Date();
    }

    public void update(CustomerForm customerForm) {
        this.name = customerForm.getName();
        this.cpf = customerForm.getCpf();
        this.email = customerForm.getEmail();
        this.birthDate = customerForm.getBirthDate();
    }

    public void delete() {
        this.status = false;
    }
}
