package com.cakeshop.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.cakeshop.app")
public class CakeshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(CakeshopApplication.class, args);
	}

}
