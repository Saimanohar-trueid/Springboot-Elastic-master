package com.elastic.trueid.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elastic.trueid.entity.KeycloakUserDetailsEntity;

public interface KeycloakUserDetailsRepository extends JpaRepository<KeycloakUserDetailsEntity, Long>{

}
