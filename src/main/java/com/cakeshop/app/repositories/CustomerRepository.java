package com.cakeshop.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cakeshop.app.entities.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	// Custom query methods can be added here if needed
	Customer findByEmail(String email);
}
