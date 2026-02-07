package com.assessmentprod.userServiceV1.controller;

//import com.assessmentprod.userServiceV1.dto.UserInfoRes;
//import com.assessmentprod.userServiceV1.service.CustUserDetailsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;

import com.assessmentprod.userServiceV1.dto.UserInfoRes;
import com.assessmentprod.userServiceV1.dto.UserUpdateReq;
import com.assessmentprod.userServiceV1.service.CustUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private CustUserDetailsService userServ;


    @PutMapping("/profile/{id}")
    public ResponseEntity<String> updateProfile(@PathVariable Long id, @RequestBody UserUpdateReq userUpdateReq) {
            return userServ.updateProfile(id, userUpdateReq);
    }
}
