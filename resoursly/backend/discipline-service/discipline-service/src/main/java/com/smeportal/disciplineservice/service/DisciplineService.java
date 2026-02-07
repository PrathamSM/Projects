package com.smeportal.disciplineservice.service;

import com.smeportal.disciplineservice.dto.DisciplineDTO;
import com.smeportal.disciplineservice.model.Discipline;
import com.smeportal.disciplineservice.repository.DisciplineRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DisciplineService {

    @Autowired
    private DisciplineRepository disciplineRepository;

    private DisciplineDTO mapToDTO(Discipline discipline) {
        DisciplineDTO dto = new DisciplineDTO();
        dto.setId(discipline.getId());
        dto.setName(discipline.getName());
        return dto;
    }


    @Transactional
    public List<DisciplineDTO> saveNewDisciplines(List<String> disciplineNames) {

        if (disciplineNames == null || disciplineNames.isEmpty()) {
            return Collections.emptyList();
        }

        // Convert discipline names to lowercase and remove duplicates
        List<String> lowerCaseNames = disciplineNames.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet()).stream().toList();

        // Fetch existing disciplines in a single query
        List<Discipline> existingDisciplines = disciplineRepository.findByNameIn(lowerCaseNames);
        Set<String> existingNames = existingDisciplines.stream()
                .map(Discipline::getName)
                .collect(Collectors.toSet());

        // Determine new discipline names
        Set<String> newDisciplineNames = lowerCaseNames.stream()
                .filter(name -> !existingNames.contains(name))
                .collect(Collectors.toSet());

        // Create and save new disciplines
        List<Discipline> newDisciplines = newDisciplineNames.stream()
                .map(name -> {
                    Discipline discipline = new Discipline();
                    discipline.setName(name);
                    return discipline;
                }).toList();

        List<Discipline> savedNewDisciplines = disciplineRepository.saveAll(newDisciplines);

        // Combine new and existing disciplines
        List<Discipline> allDisciplines = new ArrayList<>();
        allDisciplines.addAll(existingDisciplines);
        allDisciplines.addAll(savedNewDisciplines);

        // Map to DTOs
        return allDisciplines.stream().map(this::mapToDTO).toList();

    }

    public List<String> getDisciplineNamesByIds(List<Long> ids) {
        return disciplineRepository.findAllById(ids)
                .stream().map(Discipline::getName)
                .collect(Collectors.toList());
    }


    //GET LIST OF IDS AS PER DISCIPLINES
    public List<Long> getIdsByDisciplineName(List<String> disciplineNames) {
        List<Discipline> fetchedDisciplines = disciplineRepository.findByNameIn(disciplineNames.stream().map(String::toLowerCase).collect(Collectors.toList()));
        return fetchedDisciplines.stream()
                .map(Discipline::getId)
                .collect(Collectors.toList());
    }
}
