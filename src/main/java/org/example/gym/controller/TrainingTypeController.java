package org.example.gym.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.entity.TrainingTypeEntity;
import org.example.gym.mapper.TrainingTypeMapper;
import org.example.gym.paylod.response.TrainingTypesResponseDto;
import org.example.gym.service.TrainingTypeService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<TrainingTypesResponseDto>> getTrainingTypes() {
        log.info("Fetching all training types");

        List<TrainingTypeEntity> trainingTypes = trainingTypeService.findAll();
        List<TrainingTypesResponseDto> responseDtos = mapper.entityMapToResponse(trainingTypes);

        return ResponseEntity.ok(responseDtos);
    }
}
