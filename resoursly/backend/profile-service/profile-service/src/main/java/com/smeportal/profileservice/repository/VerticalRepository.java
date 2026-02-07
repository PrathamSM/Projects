package com.smeportal.profileservice.repository;

import com.smeportal.profileservice.model.Vertical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerticalRepository extends JpaRepository<Vertical, Long> {
}
