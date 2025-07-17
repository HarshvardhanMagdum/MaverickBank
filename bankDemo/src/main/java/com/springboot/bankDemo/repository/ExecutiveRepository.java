package com.springboot.bankDemo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.bankDemo.model.Executive;

public interface ExecutiveRepository extends JpaRepository<Executive, Integer>{

	@Query("select e from Executive e where e.user.username=?1")
	Executive getExecutiveByUsername(String username);
	
	@Query("select e from Executive e where e.branch.id=?1")
	List<Executive> getExecutiveByBranch(int branchId);
}
