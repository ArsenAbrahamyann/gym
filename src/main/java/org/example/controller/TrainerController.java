package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TrainerEntity;
import org.example.mapper.TrainerMapper;
import org.example.paylod.request.TrainerRegistrationRequestDto;
import org.example.paylod.request.UpdateTrainerRequestDto;
import org.example.paylod.response.GetTrainerProfileResponseDto;
import org.example.paylod.response.RegistrationResponseDto;
import org.example.paylod.response.UpdateTrainerProfileResponseDto;
import org.example.service.TrainerService;
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
import org.springframework.web.bind.annotation.RequestPart;
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

    @PostMapping(value = "/trainer/registration")
    public ResponseEntity<?> trainerRegistration(@RequestPart String firstName,
                                              @RequestPart String lastName,
                                              @PathVariable("trainingTypeId") Long trainingTypeId) {
        log.info("Controller: Registration trainer");
        TrainerRegistrationRequestDto registrationDto = new TrainerRegistrationRequestDto(firstName,
                lastName, trainingTypeId);
        TrainerEntity trainer = mapper.trainerRegistrationMapToEntity(registrationDto);
        TrainerEntity trainerProfile = trainerService.createTrainerProfile(trainer);
        RegistrationResponseDto responseDto = mapper.TrainerMapToEntity(trainerProfile);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getTrainerProfile(@RequestPart String username) {
        log.info("Controller: Get trainer profile");
        TrainerEntity trainer = trainerService.getTrainer(username);
        GetTrainerProfileResponseDto responseDto = mapper.trainerEntityMapToGetResponse(trainer);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<?> updateTrainerProfile(@RequestPart String username,
                                                  @RequestPart String firstName,
                                                  @RequestPart String lastName,
                                                  @PathVariable("trainingTypeId") Long trainingTypeId,
                                                  @RequestParam boolean isActive) {
        log.info("Controller: update trainer profile");
        UpdateTrainerRequestDto updateTrainerRequestDto = new UpdateTrainerRequestDto(username, firstName, lastName,
                trainingTypeId, isActive);
        TrainerEntity trainer = mapper.UpdateRequestDtoMapToTrainerEntity(updateTrainerRequestDto);
        TrainerEntity updateTrainerProfile = trainerService.updateTrainerProfile(trainer);
        UpdateTrainerProfileResponseDto responseDto = mapper.updateTrainerProfileMapToResponseDto(
                updateTrainerProfile);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PatchMapping(value = "/activate")
    public ResponseEntity<?> activateTrainer(@RequestPart String username,
                                             @RequestParam boolean isActive) {
        log.info("Controller: Activate trainer.");
        trainerService.toggleTrainerStatus(username, isActive);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(value = "/de-activate")
    public ResponseEntity<?> deActivateTrainer(@RequestPart String username,
                                               @RequestParam boolean isActive) {
        log.info("Controller: DeActivate trainer.");
        trainerService.toggleTrainerStatus(username, isActive);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
