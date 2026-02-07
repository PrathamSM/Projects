package com.assessmentprod.userServiceV1.controller;

import com.assessmentprod.userServiceV1.dto.UserInfoRes;
import com.assessmentprod.userServiceV1.entity.UserData;
import com.assessmentprod.userServiceV1.entity.role;
import com.assessmentprod.userServiceV1.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class AdminController {

    @Autowired
    private AdminService adminService;


    @GetMapping
    public List<UserInfoRes> getAllUsers() {
        return adminService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        UserInfoRes userInfo = adminService.getUser(id);


        return ResponseEntity.status(HttpStatus.OK).body(userInfo);
    }


    @PutMapping("/{id}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable("id") Long id, @RequestParam("updateTo") role roleToUpdate) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.updateUserRole(id, roleToUpdate));
    }
}
