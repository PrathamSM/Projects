package com.assessmentprod.authService.controller;

import com.assessmentprod.authService.dto.AuthRequest;
import com.assessmentprod.authService.entity.UserData;
import com.assessmentprod.authService.exception.InvalidCredentialsException;
import com.assessmentprod.authService.service.AuthService;
import com.assessmentprod.authService.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<String> addNewUser(@RequestBody UserData userData) {
        if (userData.getUsername() == null || userData.getUsername().isEmpty() ||
                userData.getPassword() == null || userData.getPassword().isEmpty() ||
                userData.getEmail() == null || userData.getEmail().isEmpty()) {
            return new ResponseEntity<>("Missing required fields", HttpStatus.BAD_REQUEST);
        }
         return authService.createUser(userData);
    }


    @PostMapping("/token")
    public String getToken(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );


                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                List<String> roles = userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList());
                return jwtService.generateToken(authRequest.getUsername(), roles);
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException("Verify your username and password and try again! : " + e.getMessage());
        }
    }

    @PostMapping("/validate")
    public ResponseEntity validateToken(@RequestParam("token") String token) {
        if(jwtService.isTokenValid(token)) {
            return new ResponseEntity(HttpStatus.OK);
        }
        else{
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
