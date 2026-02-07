package com.smeprod.authservice.controller;

import com.smeprod.authservice.dto.AuthRequest;
import com.smeprod.authservice.dto.CreateUserReq;
import com.smeprod.authservice.entity.User;
import com.smeprod.authservice.repository.UserRepository;
import com.smeprod.authservice.service.AuthService;
import com.smeprod.authservice.service.JwtUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtilsService jwtUtilsService;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody CreateUserReq request) {
        String role = request.role();

        if (!role.matches("ADMIN|PROJ_MANAGER|RES_MANAGER")) {
            return ResponseEntity.badRequest().body("Invalid role!");
        }

        return authService.createUser(request);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        String email = request.email();
        String password = request.password();

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty() || !passwordEncoder.matches(password, userOptional.get().getPassword())) {
            return ResponseEntity.badRequest().body("Invalid Credentials!");
        }

        User user = userOptional.get();
        String token = jwtUtilsService.generateToken(user.getEmail(), user.getId(), String.valueOf(user.getRole()));

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("token", token));
    }


}
