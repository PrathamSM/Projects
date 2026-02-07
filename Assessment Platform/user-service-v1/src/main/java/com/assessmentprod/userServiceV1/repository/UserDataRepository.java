package com.assessmentprod.userServiceV1.repository;

import com.assessmentprod.userServiceV1.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.assessmentprod.userServiceV1.entity.role;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserDataRepository extends JpaRepository<UserData, Long> {
    Optional<UserData> findByUsername(String username);

    Optional<UserData> findByEmail(String email);

    List<UserData> findByRoleNot(role roleToIgnore);
}