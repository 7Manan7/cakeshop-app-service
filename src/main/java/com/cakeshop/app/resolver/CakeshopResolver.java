package com.cakeshop.app.resolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.cakeshop.app.entities.Customer;
import com.cakeshop.app.entities.Employee;
import com.cakeshop.app.repositories.CustomerRepository;
import com.cakeshop.app.repositories.EmployeeRepository;

import graphql.schema.DataFetchingEnvironment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@Controller
public class CakeshopResolver {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private EntityManager entityManager;

	@QueryMapping
	public List<Customer> customers() {
		return customerRepository.findAll();
	}

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
		List<String> requestedFields = environment.getSelectionSet().getFields().stream().map(field -> field.getName())
				.collect(Collectors.toList());

		String queryString = buildDynamicQuery(requestedFields);

		Query query = entityManager.createQuery(queryString);

		List<?> queryResults = query.getResultList();

		return mapResultsToFields(queryResults, requestedFields);
	}

	private String buildDynamicQuery(List<String> fields) {
		String joinedFields = String.join(", ", fields.stream().map(f -> "e." + f).collect(Collectors.toList()));
		return "SELECT " + joinedFields + " FROM Employee e";
	}

	private List<Map<String, Object>> mapResultsToFields(List<?> queryResults, List<String> fields) {
		List<Map<String, Object>> mappedResults = new ArrayList<>();

		if (fields.size() == 1) {
			// Handle single-field queries like `SELECT name FROM Employee`
			for (Object result : queryResults) {
				Map<String, Object> rowMap = new HashMap<>();
				rowMap.put(fields.get(0), result);
				mappedResults.add(rowMap);
			}
		} else {
			// Handle multi-field queries like `SELECT name, phone FROM Employee`
			for (Object result : queryResults) {
				if (result instanceof Object[]) {
					Object[] row = (Object[]) result;
					Map<String, Object> rowMap = new HashMap<>();
					for (int i = 0; i < fields.size(); i++) {
						rowMap.put(fields.get(i), row[i]);
					}
					mappedResults.add(rowMap);
				}
			}
		}

		return mappedResults;
	}
}
