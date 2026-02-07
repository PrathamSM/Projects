package com.assessmentprod.authService.service;

import com.assessmentprod.authService.entity.UserData;
import com.assessmentprod.authService.entity.role;
import com.assessmentprod.authService.exception.UserAlreadyExistsException;
import com.assessmentprod.authService.repository.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDataRepository userDataRepository;

    @Autowired
    private JwtService jwtService;

    public ResponseEntity<String> createUser(UserData user) {
//        userData.setPassword(passwordEncoder.encode(userData.getPassword()));
//        userDataRepository.save(userData);
//        return ResponseEntity.status(HttpStatus.OK).body("User Registered Successfully!");
        if(userDataRepository.findByUsername(user.getUsername()).isPresent()) {
//            return ResponseEntity.status(HttpStatus.OK).body("User already exists! with username : " + user.getUsername());
            throw new UserAlreadyExistsException("Username", user.getUsername());
        }
        if(userDataRepository.findByEmail(user.getEmail()).isPresent()) {
//            return ResponseEntity.status(HttpStatus.OK).body("User already exists! with email : " + user.getEmail());
            throw new UserAlreadyExistsException("Email", user.getEmail());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(role.USER);
        userDataRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body("USER REGISTERED!!");
    }

    public String generateToken(String username) {

        //new
        Optional<UserData> userOp = userDataRepository.findByUsername(username);
        if(userOp.isPresent()) {
            UserData user = userOp.get();
            List<String> roles = List.of(user.getRole().name());
            return jwtService.generateToken(username, roles);
        }
        throw new RuntimeException("User not found!");
    }

    public boolean validateToken(String token) {
       return jwtService.isTokenValid(token);
    }
}
