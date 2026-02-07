package com.assessmentprod.authService.repository;

import com.assessmentprod.authService.entity.UserData;
import com.assessmentprod.authService.entity.role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDataRepository extends JpaRepository<UserData, Long> {

    Optional<UserData> findByUsername(String username);
    Optional<UserData> findByEmail(String email);

    List<UserData> findByRoleNot(role roleToIgnore);
}
