package com.matheus.simplespringcrud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@ComponentScan("com.matheus.simplespringcrud")
public class SimpleSpringCrudApplication {

	public static void main(String[] args) {

		SpringApplication.run(SimpleSpringCrudApplication.class, args).setId("simple-spring-crud");
	}

}
