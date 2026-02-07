package com.smeportal.proexpservice.feign;

import com.smeportal.proexpservice.dto.DisciplineDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "discipline-service", url = "http://localhost:8083/disciplines")
public interface DisciplineFeignClient {

    @PostMapping("/names")
    ResponseEntity<List<Long>> getDisciplineIdsByName(@RequestBody List<String> disciplineNames);

    @PostMapping("/ids")
    ResponseEntity<List<String>> getDisciplineNamesByIds(@RequestBody List<Long> ids);

    @PostMapping("/batch")
    ResponseEntity<List<DisciplineDTO>> saveNewDisciplines(@RequestBody List<String> newDisciplines);
}