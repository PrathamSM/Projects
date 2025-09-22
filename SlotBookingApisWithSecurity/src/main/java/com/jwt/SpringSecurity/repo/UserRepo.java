package com.jwt.SpringSecurity.repo;
import com.jwt.SpringSecurity.model.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<UserData,Integer> {

    List<UserData> findByExpertise(String expertise);
    UserData findByUsername(String username);
}
