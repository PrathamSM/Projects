package com.smeportal.disciplineservice.repository;

import com.smeportal.disciplineservice.model.Discipline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DisciplineRepository extends JpaRepository<Discipline, Long>{

    Optional<Discipline> findByName(String name);

    List<Discipline> findByNameIn(List<String> names);

}
