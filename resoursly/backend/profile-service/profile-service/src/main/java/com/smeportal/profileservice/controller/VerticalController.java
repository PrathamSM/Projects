package com.smeportal.profileservice.controller;

import com.smeportal.profileservice.model.Role;
import com.smeportal.profileservice.model.Vertical;
import com.smeportal.profileservice.repository.VerticalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/profiles/verticals")
@CrossOrigin("*")
public class VerticalController {

    @Autowired
    private VerticalRepository verticalRepository;

    @PostMapping
    public ResponseEntity<?> postVerticals(@RequestBody List<String> verticalsNames) {
        Set<String> existingVerticals = verticalRepository.findAll()
                .stream()
                .map(Vertical::getVerticalName)
                .collect(Collectors.toSet());

        List<Vertical> newVerticals = verticalsNames.stream()
                .map(verticalName -> {
                    if(existingVerticals.contains(verticalName.toLowerCase())) {
                        return null;
                    }
                    if(verticalName.length() <= 3) {
                        verticalName = verticalName.toUpperCase();
                    }

                    Vertical vertical = new Vertical();
                    vertical.setVerticalName(verticalName);
                    return vertical;


                })
                .filter(vertical -> vertical != null)
                .collect(Collectors.toList());

        if(newVerticals.isEmpty() || newVerticals.contains(null)) {
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("response", "No new verticals saved"));
        }
        verticalRepository.saveAll(newVerticals);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("savedVerticals", newVerticals));
    }

    @GetMapping
    public ResponseEntity<?> getAllVerticals() {
        List<Vertical> allVerticals = verticalRepository.findAll();
        if(allVerticals.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("response", "There is no data in vertical tables"));
        }
        Collections.sort(allVerticals, Comparator.comparing(
                Vertical::getId, Comparator.nullsLast(Comparator.naturalOrder()))
        );
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("verticals", allVerticals));
    }
    }


