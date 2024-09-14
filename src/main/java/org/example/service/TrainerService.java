package org.example.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.TrainerDto;
import org.example.entity.TrainerEntity;
import org.example.entity.UserEntity;
import org.example.exeption.ResourceNotFoundException;
import org.example.repository.TrainerRepository;
import org.example.repository.UserRepository;
import org.example.utils.UserUtils;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainerService {
    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;
    private final UserUtils userUtils;

    public TrainerDto createTrainerProfile(TrainerDto trainerDto) {
        log.info("Creating trainer profile ");

        List<String> allUsername = userRepository.findAllUsername()
                .orElseThrow(() -> new ResourceNotFoundException("username not found"));
        String generatedUsername = userUtils.generateUsername(trainerDto.getUser().getFirstName(),
                trainerDto.getUser().getLastName(), allUsername);
        String generatedPassword = userUtils.generatePassword();

        // Create User entity
        UserEntity user = new UserEntity();
        user.setUsername(generatedUsername);
        user.setPassword(generatedPassword);
        user.setFirstName(trainerDto.getUser().getFirstName());
        user.setLastName(trainerDto.getUser().getLastName());
        user.setIsActive(trainerDto.getUser().getIsActive());
        userRepository.save(user);

        // Create Trainee entity and associate with User
        TrainerEntity trainer = new TrainerEntity();
        trainer.setSpecialization(trainerDto.getSpecialization());
        trainer.setTrainees(trainerDto.getTrainees());
        trainer.setUser(user);
        trainerRepository.save(trainer);

        log.info("Trainee profile created successfully for {}", user.getUsername());

        return trainerDto;
    }

    public void changeTrainerPassword(String username, String newPassword) {
        log.info("Changing password for trainer {}", username);
        TrainerEntity trainer = trainerRepository.findByTrainerFromUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Trainer not found"));
        UserEntity user = trainer.getUser();
        user.setPassword(newPassword);
        userRepository.update(user);
        log.info("Password updated successfully for trainer {}", username);
    }

    public void toggleTrainerStatus(String username) {
        log.info("Toggling trainer status for {}", username);
        TrainerEntity trainer = trainerRepository.findByTrainerFromUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Trainer not found"));

        UserEntity user = trainer.getUser();
        user.setIsActive(! user.getIsActive());
        userRepository.update(user);
        log.info("Trainer status toggled successfully for {}", username);
    }

}
