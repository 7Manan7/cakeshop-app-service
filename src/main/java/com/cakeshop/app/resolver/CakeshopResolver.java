package com.cakeshop.app.resolver;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.cakeshop.app.entities.Customer;
import com.cakeshop.app.entities.Employee;
import com.cakeshop.app.repositories.CustomerRepository;
import com.cakeshop.app.repositories.EmployeeRepository;

@Controller
public class CakeshopResolver {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@QueryMapping
	public List<Customer> customers() {
		return customerRepository.findAll();
	}

	@QueryMapping
	public Customer customerById(@Argument Integer id) {
		return customerRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
	}

	@QueryMapping
	public List<Employee> employees() {
		return employeeRepository.findAll();
	}

	@QueryMapping
	public Employee employeeById(@Argument Integer id) {
		return employeeRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
	}
}
