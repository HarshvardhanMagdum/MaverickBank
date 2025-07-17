package com.springboot.bankDemo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.bankDemo.model.AccountType;

public interface AccountTypeRepository extends JpaRepository<AccountType, Integer>{

	@Query("select at from AccountType at where at.type=?1")
	Optional<AccountType> getByType(String type);		// user written JPQL to fetch account type
	Optional<AccountType> findByType(String type);	// jpa writes to fetch account type
}
