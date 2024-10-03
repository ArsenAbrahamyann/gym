package org.example.gym.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.mapper.TrainerMapper;
import org.example.gym.paylod.request.TrainerRegistrationRequestDto;
import org.example.gym.paylod.request.UpdateTrainerRequestDto;
import org.example.gym.paylod.response.GetTrainerProfileResponseDto;
import org.example.gym.paylod.response.RegistrationResponseDto;
import org.example.gym.paylod.response.UpdateTrainerProfileResponseDto;
import org.example.gym.service.TrainerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class responsible for handling trainer-related operations.
 * Acts as a facade between the client and the TrainerService.
 */
@RestController
@RequestMapping("api/trainer")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class TrainerController {
    private final TrainerService trainerService;
    private final TrainerMapper mapper;

    @PostMapping("/registration/{trainingTypeId}")
    public ResponseEntity<RegistrationResponseDto> registerTrainer(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @PathVariable Long trainingTypeId) {
        log.info("Registering trainer: {} {}", firstName, lastName);

        TrainerRegistrationRequestDto registrationDto = new TrainerRegistrationRequestDto(firstName, lastName, trainingTypeId);
        TrainerEntity trainerEntity = mapper.trainerRegistrationMapToEntity(registrationDto);
        TrainerEntity savedTrainer = trainerService.createTrainerProfile(trainerEntity);

        RegistrationResponseDto responseDto = mapper.trainerMapToResponse(savedTrainer);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{username}")
    public ResponseEntity<GetTrainerProfileResponseDto> getTrainerProfile(@PathVariable String username) {
        log.info("Fetching profile for trainer: {}", username);

        TrainerEntity trainer = trainerService.getTrainer(username);
        GetTrainerProfileResponseDto responseDto = mapper.trainerEntityMapToGetResponse(trainer);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/update")
    public ResponseEntity<UpdateTrainerProfileResponseDto> updateTrainerProfile(
            @RequestParam String username,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam Long trainingTypeId,
            @RequestParam boolean isActive) {
        log.info("Updating trainer profile for: {}", username);

        UpdateTrainerRequestDto updateDto = new UpdateTrainerRequestDto(username, firstName,
                lastName, trainingTypeId, isActive);
        TrainerEntity updatedTrainer = mapper.updateRequestDtoMapToTrainerEntity(updateDto);
        TrainerEntity savedTrainer = trainerService.updateTrainerProfile(updatedTrainer);

        UpdateTrainerProfileResponseDto responseDto = mapper.updateTrainerProfileMapToResponseDto(savedTrainer);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/activate")
    public ResponseEntity<Void> activateTrainer(@RequestParam String username, @RequestParam boolean isActive) {
        log.info("Setting trainer status to active: {}", username);
        trainerService.toggleTrainerStatus(username, isActive);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/de-activate")
    public ResponseEntity<Void> deactivateTrainer(@RequestParam String username, @RequestParam boolean isActive) {
        log.info("Setting trainer status to inactive: {}", username);
        trainerService.toggleTrainerStatus(username, isActive);
        return ResponseEntity.ok().build();
    }
}
