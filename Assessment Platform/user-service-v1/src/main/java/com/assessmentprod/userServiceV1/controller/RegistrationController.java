package com.assessmentprod.userServiceV1.controller;

//import com.assessmentprod.userServiceV1.dto.LoginReq;
//import com.assessmentprod.userServiceV1.entity.UserData;
//import com.assessmentprod.userServiceV1.entity.role;
//import com.assessmentprod.userServiceV1.repository.UserDataRepository;
//import com.assessmentprod.userServiceV1.service.CustUserDetailsService;
//import com.assessmentprod.userServiceV1.utils.JwtUtils;
//import org.springframework.security.core.Authentication;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;

//@RestController
public class RegistrationController {
//
//    @Autowired
//    private UserDataRepository userDataRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private CustUserDetailsService custUserDetailsService;
//
//    @Autowired
//    private AuthenticationManager authManager;
//
//    @Autowired
//    private JwtUtils jwtUtils;
//
//
//
//    @PostMapping("/register")
//    public ResponseEntity<?> createUser(@RequestBody UserData user) {
////        user.setPassword(passwordEncoder.encode(user.getPassword()));
////        user.setRole(role.USER);
////        userDataRepository.save(user);
////        return ResponseEntity.status(HttpStatus.CREATED).body("User Created Successfully!");
//
//        if(userDataRepository.findByUsername(user.getUsername()).isPresent()) {
//            return ResponseEntity.status(HttpStatus.OK).body("User already exists! with username : " + user.getUsername());
//        }
//        if(userDataRepository.findByEmail(user.getEmail()).isPresent()) {
//            return ResponseEntity.status(HttpStatus.OK).body("User already exists! with email : " + user.getEmail());
//        }
//
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setRole(role.USER);
//        userDataRepository.save(user);
//        return ResponseEntity.status(HttpStatus.CREATED).body("User Created Successfully!");
//    }
//
//
//    @PostMapping("/authenticate")
//    public ResponseEntity<String> authenticateAndGetToken(@RequestBody LoginReq loginReq) {
//        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(loginReq.username(), loginReq.password()));
//
//        if(authentication.isAuthenticated()) {
//            String jwtToken = jwtUtils.generateToken(custUserDetailsService.loadUserByUsername(loginReq.username()));
//            return ResponseEntity.status(HttpStatus.OK).body(jwtToken);
//        }
//        else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials!");
//        }
//
//    }
}
