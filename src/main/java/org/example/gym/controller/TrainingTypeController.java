package org.example.gym.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.dto.response.TrainingTypesResponseDto;
import org.example.gym.entity.TrainingTypeEntity;
import org.example.gym.mapper.TrainingTypeMapper;
import org.example.gym.service.TrainingTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing training types.
 * Provides endpoints to fetch training types.
 */
@RestController
@RequestMapping("trainingType")
@RequiredArgsConstructor
@Slf4j
public class TrainingTypeController {
    private final TrainingTypeService trainingTypeService;
    private final TrainingTypeMapper mapper;

    /**
     * Fetches all training types.
     *
     * @return ResponseEntity containing a list of TrainingTypesResponseDto objects.
     */
    @GetMapping
    @Operation(summary = "Fetch all training types", description = "Retrieves a list of all available training types.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully fetched the list of training types"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<TrainingTypesResponseDto>> getTrainingTypes() {
        log.info("Controller: Fetching all training types");

        List<TrainingTypeEntity> trainingTypes = trainingTypeService.findAll();
        List<TrainingTypesResponseDto> responseDtos = mapper.entityMapToResponse(trainingTypes);

        log.info("Successfully fetched {} training types", responseDtos.size());
        return ResponseEntity.ok(responseDtos);
    }
}
