package com.springboot.bankDemo.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "customer")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(nullable = false)
	private String firstName;
	@Column(nullable = false)
	private String lastName;
	@Column(unique = true, nullable = false)
	private String email;
	@Column(unique = true, nullable = false)
	private String phoneNumber;
	@Column(nullable = false)
	private String gender;
	@Column(name = "date_of_birth", nullable = false)
	private LocalDate dateOfBirth;
	@Column(nullable = false)
	private String address;
	@Column(nullable = false)
	private LocalDate registrationDate;
	@OneToOne(optional = false, cascade = CascadeType.ALL)
	private User user;
	
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public String getFirstName() { return firstName; }
	public void setFirstName(String firstName) { this.firstName = firstName; }
	public String getLastName() { return lastName; }
	public void setLastName(String lastName) { this.lastName = lastName; }
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	public String getPhoneNumber() { return phoneNumber; }
	public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
	public String getAddress() { return address; }
	public void setAddress(String address) { this.address = address; }
	public String getGender() { return gender; }
	public void setGender(String gender) { this.gender = gender; }
	public LocalDate getDateOfBirth() { return dateOfBirth; }
	public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
	public LocalDate getRegistrationDate() { return registrationDate; } 
	public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }
	public User getUser() { return user; }
	public void setUser(User user) { this.user = user; }
	
}
