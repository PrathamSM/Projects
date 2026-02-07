package com.smeportal.disciplineservice.controller;


import com.smeportal.disciplineservice.dto.DisciplineDTO;
import com.smeportal.disciplineservice.model.Discipline;
import com.smeportal.disciplineservice.service.DisciplineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/disciplines")
public class DisciplineController {


    @Autowired
    private DisciplineService disciplineService;


//    @PostMapping("/batch")
//    public ResponseEntity<List<Discipline>> saveNewDisciplines(@RequestBody List<String> newDisciplines) {
//        List<Discipline> savedDisciplines = disciplineService.saveNewDisciplines(newDisciplines);
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedDisciplines);
//    }

    @PostMapping("/batch")
    public ResponseEntity<List<DisciplineDTO>> saveNewDisciplines(@RequestBody List<String> disciplineNames) {
        List<DisciplineDTO> savedDisciplines = disciplineService.saveNewDisciplines(disciplineNames);
        return ResponseEntity.ok(savedDisciplines);
    }


    @PostMapping("/ids")
    public ResponseEntity<List<String>> getDisciplineNamesByIds(@RequestBody List<Long> ids) {
        List<String> disciplineNames = disciplineService.getDisciplineNamesByIds(ids);
        return ResponseEntity.status(HttpStatus.OK).body(disciplineNames);
    }


    @PostMapping("/names")
    public ResponseEntity<List<Long>> getDisciplineIdsByName(@RequestBody List<String> disciplineNames) {
        List<Long> disciplineIds = disciplineService.getIdsByDisciplineName(disciplineNames);

        return ResponseEntity.status(HttpStatus.OK).body(disciplineIds);

    }

}
