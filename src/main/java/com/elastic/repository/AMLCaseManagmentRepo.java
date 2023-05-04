package com.elastic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elastic.trueid.entity.AMLWorldCheckCaseManagment;





public interface AMLCaseManagmentRepo extends JpaRepository<AMLWorldCheckCaseManagment, Long> {

	List<AMLWorldCheckCaseManagment> findByStatus(String status);

	

}
