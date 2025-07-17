package com.springboot.bankDemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.bankDemo.model.Manager;

public interface ManagerRepository extends JpaRepository<Manager, Integer>{

	@Query("select m from Manager m where m.user.username=?1")
	Manager getManagerByUsername(String username);
}
