package com.springboot.bankDemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.bankDemo.model.LoanDetails;

public interface LoanDetailsRepository extends JpaRepository<LoanDetails, Integer>{

}
