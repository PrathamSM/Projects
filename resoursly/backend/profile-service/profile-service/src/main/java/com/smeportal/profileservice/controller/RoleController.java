package com.smeportal.profileservice.controller;

import com.smeportal.profileservice.model.Role;
import com.smeportal.profileservice.repository.RoleRepository;
import feign.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/profiles/roles")
@CrossOrigin("*")
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping
    public ResponseEntity<?> postRoles(@RequestBody List<String> roleNames) {
        Set<String> existingRoles = roleRepository.findAll()
                .stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());

        List<Role> newRoles = roleNames.stream()
                .map(roleName -> {
                    if(existingRoles.contains(roleName.toLowerCase())) {
                        return null;
                    }
                    if(roleName.length() <= 3) {
                        roleName = roleName.toUpperCase();
                    }

                    Role role = new Role();
                    role.setRoleName(roleName);
                    return role;


                })
                .filter(role -> role != null)
                .collect(Collectors.toList());

        if(newRoles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("response", "No new roles saved"));
        }
        roleRepository.saveAll(newRoles);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("savedRoles", newRoles));
    }

    @GetMapping
    public ResponseEntity<?> getAllRoles() {
        List<Role> allRoles = roleRepository.findAll();
        if (allRoles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("response", "There is no data in roles table"));
        }
        Collections.sort(allRoles, Comparator.comparing(
                Role::getId, Comparator.nullsLast(Comparator.naturalOrder()))
        );
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("roles", allRoles));
    }
}
