package com.cakeshop.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cakeshop.app.entities.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
	// Custom query methods can be added here if needed
	Employee findByEmail(String email);
}