package com.cakeshop.app.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "employees", schema = "cakeshop")
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "employee_id", nullable = false)
	private Integer employeeId;

	@Column(name = "name", nullable = false, length = 100)
	private String name;

	@Column(name = "position", length = 50)
	private String position;

	@Column(name = "salary", precision = 10, scale = 2)
	private BigDecimal salary;

	@Column(name = "date_joined")
	private LocalDate dateJoined;

	@Column(name = "phone", length = 15)
	private String phone;

	@Column(name = "email", unique = true, length = 100)
	private String email;

}
