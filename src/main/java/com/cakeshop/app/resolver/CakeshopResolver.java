package com.cakeshop.app.resolver;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.cakeshop.app.entities.Customer;
import com.cakeshop.app.entities.Employee;
import com.cakeshop.app.repositories.CustomerRepository;
import com.cakeshop.app.repositories.EmployeeRepository;
import com.cakeshop.app.utils.DynamicQueryUtil;

import graphql.schema.DataFetchingEnvironment;

@Controller
public class CakeshopResolver {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private DynamicQueryUtil dynamicQueryUtil;

	@QueryMapping
	public Customer customerById(@Argument Integer id) {
		return customerRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
	}

	/**
	 * @QueryMapping public List<Employee> employees() { return
	 *               employeeRepository.findAll(); }
	 **/

	@QueryMapping
	public Employee employeeById(@Argument Integer id) {
		return employeeRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
	}

	@SchemaMapping(typeName = "Query", field = "employees")
	public List<Map<String, Object>> fetchEmployees(DataFetchingEnvironment environment) {
		return dynamicQueryUtil.fetchSelectiveData(environment, "Employee");
	}

	@SchemaMapping(typeName = "Query", field = "customers")
	public List<Map<String, Object>> fetchCustomers(DataFetchingEnvironment environment) {
		return dynamicQueryUtil.fetchSelectiveData(environment, "Customer");
	}
}
