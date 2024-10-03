package org.example.controller;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TrainingTypeEntity;
import org.example.mapper.TrainingTypeMapper;
import org.example.paylod.response.TrainingTypesResponseDto;
import org.example.service.TrainingTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/trainingType")
@RequiredArgsConstructor
@Slf4j
public class TrainingTypeController {
    private final TrainingTypeService trainingTypeService;
    private final TrainingTypeMapper mapper;

    @GetMapping
    public ResponseEntity<?> getTrainingType() {
        log.info("Controller: Get training types");
        List<TrainingTypeEntity> all = trainingTypeService.findAll();
        List<TrainingTypesResponseDto> responseDtos = mapper.entityMapToResponse(all);
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }
}
