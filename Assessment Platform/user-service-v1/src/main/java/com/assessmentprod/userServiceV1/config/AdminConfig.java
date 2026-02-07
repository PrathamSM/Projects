package com.assessmentprod.userServiceV1.config;

import com.assessmentprod.userServiceV1.entity.UserData;
import com.assessmentprod.userServiceV1.entity.role;
import com.assessmentprod.userServiceV1.repository.UserDataRepository;
import com.assessmentprod.userServiceV1.service.CustUserDetailsService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

//@Configuration
public class AdminConfig {

//    @Autowired
//    private UserDataRepository userDataRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;

//    @PostConstruct
//    public void createAdmin() {
//        if(userDataRepository.findByUsername("Admin007").isEmpty()) {
//            UserData adminUser = new UserData();
//            adminUser.setEmail("admin@gmail.com");
//            adminUser.setUsername("Admin007");
//            adminUser.setPassword(passwordEncoder.encode("admin123"));
//            adminUser.setRole(role.ADMIN);
//            userDataRepository.save(adminUser);
//            System.out.println("Admin created successfully!");
//        }
//    }
}
