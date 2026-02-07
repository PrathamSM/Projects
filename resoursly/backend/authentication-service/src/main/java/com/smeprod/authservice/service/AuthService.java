package com.smeprod.authservice.service;

import com.smeprod.authservice.dto.CreateUserReq;
import com.smeprod.authservice.entity.Role;
import com.smeprod.authservice.entity.User;
import com.smeprod.authservice.exception.UserAlreadyExistsException;
import com.smeprod.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public ResponseEntity<?> createUser(CreateUserReq req) {
        if(userRepository.findByEmail(req.email()).isPresent()) {
            throw new UserAlreadyExistsException("Email", req.email());
        }

        User user = new User();
        user.setEmail(req.email());
        user.setPassword(passwordEncoder.encode(req.password()));
        Role role = Role.valueOf(req.role().toUpperCase());
        user.setRole(role);

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("User Created Successfully");
    }

}
