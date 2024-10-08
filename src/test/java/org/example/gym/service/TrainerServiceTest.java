//package org.example.service;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//import org.example.dto.TrainerDto;
//import org.example.dto.TrainingTypeDto;
//import org.example.dto.UserDto;
//import org.example.entity.TrainerEntity;
//import org.example.entity.TrainingTypeEntity;
//import org.example.entity.UserEntity;
//import org.example.repository.TrainerRepository;
//import org.example.utils.ValidationUtils;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.modelmapper.ModelMapper;
//
//@ExtendWith(MockitoExtension.class)
//public class TrainerServiceTest {
//
//    @Mock
//    private TrainerRepository trainerRepository;
//
//    @Mock
//    private UserService userService;
//
//    @Mock
//    private TrainingService trainingService;
//
//    @Mock
//    private TrainingTypeService trainingTypeService;
//
//    @Mock
//    private ValidationUtils validationUtils;
//
//    @Mock
//    private ModelMapper modelMapper;
//
//    @InjectMocks
//    private TrainerService trainerService;
//
//    private TrainerDto trainerDto;
//    private TrainerEntity trainerEntity;
//    private UserEntity userEntity;
//
//    /**
//     * Sets up the test environment before each test case.
//     * <p>
//     * This method initializes the necessary entities required for testing the {@link UserService} and {@link TrainerService}.
//     * It creates a {@link UserEntity}, a {@link TrainingTypeEntity}, and a {@link TrainerEntity}, along with their corresponding DTOs.
//     * These entities are configured with sample data to ensure that the tests have valid input to work with.
//     * </p>
//     * <p>
//     * The following entities are created:
//     * <ul>
//     *     <li>{@link UserEntity} with ID 1, username "testUser", password "password", and active status set to true.</li>
//     *     <li>{@link TrainingTypeEntity} with ID 1 and training type name "Yoga".</li>
//     *     <li>{@link TrainerEntity} that links the above {@link UserEntity} and {@link TrainingTypeEntity}.</li>
//     * </ul>
//     * </p>
//     * <p>
//     * Additionally, the method initializes a corresponding {@link TrainerDto} to facilitate testing of service methods
//     * that involve DTO conversions.
//     * </p>
//     */
//
//    @BeforeEach
//    public void setUp() {
//        userEntity = new UserEntity();
//        userEntity.setId(1L);
//        userEntity.setUsername("testUser");
//        userEntity.setPassword("password");
//        userEntity.setIsActive(true);
//
//        TrainingTypeEntity trainingTypeEntity = new TrainingTypeEntity();
//        trainingTypeEntity.setId(1L);
//        trainingTypeEntity.setTrainingTypeName("Yoga");
//
//        trainerEntity = new TrainerEntity();
//        trainerEntity.setId(1L);
//        trainerEntity.setSpecialization(trainingTypeEntity);
//        trainerEntity.setUser(userEntity);
//
//        trainerDto = new TrainerDto();
//        trainerDto.setSpecialization(new TrainingTypeDto("Yoga"));
//        trainerDto.setUser(new UserDto("firstName", "testUser", true,
//                "testUser", "password"));
//    }
//
//    @Test
//    public void testCreateTrainerProfile_Success() {
//        when(modelMapper.map(trainerDto, TrainerEntity.class)).thenReturn(trainerEntity);
//        when(userService.findByUsername(trainerDto.getUser().getUsername())).thenReturn(Optional.empty());
//
//        TrainerDto result = trainerService.createTrainerProfile(trainerDto);
//
//        assertThat(result).isNotNull();
//        assertThat(result.getUser().getUsername()).isEqualTo("testUser");
//        verify(trainerRepository, times(1)).save(trainerEntity);
//    }
//
//    @Test
//    public void testChangeTrainerPassword_Success() {
//        when(userService.findByUsername(userEntity.getUsername())).thenReturn(Optional.of(userEntity));
//
//        trainerService.changeTrainerPassword(userEntity.getUsername(), "newPassword");
//
//        assertThat(userEntity.getPassword()).isEqualTo("newPassword");
//        verify(userService, times(1)).update(userEntity);
//    }
//
//    @Test
//    public void testToggleTrainerStatus_Success() {
//        when(userService.findByUsername(userEntity.getUsername())).thenReturn(Optional.of(userEntity));
//
//        trainerService.toggleTrainerStatus(userEntity.getUsername());
//
//        assertThat(userEntity.getIsActive()).isFalse();
//        verify(userService, times(1)).update(userEntity);
//    }
//
//
//    @Test
//    public void testFindAll_Success() {
//        when(trainerRepository.findAll()).thenReturn(Collections.singletonList(trainerEntity));
//
//        List<TrainerEntity> trainers = trainerService.findAll();
//
//        assertThat(trainers).isNotNull();
//    }
//
//    @Test
//    public void testFindAssignedTrainers_Success() {
//        when(trainerRepository.findAssignedTrainers(1L))
//                .thenReturn(Collections.singletonList(trainerEntity));
//
//        List<TrainerEntity> trainers = trainerService.findAssignedTrainers(1L);
//
//        assertThat(trainers).isNotNull();
//    }
//
//    @Test
//    public void testFindAllById_Success() {
//        when(trainerRepository.findAllById(Collections.singletonList(1L)))
//                .thenReturn(Collections.singletonList(trainerEntity));
//
//        List<TrainerEntity> trainers = trainerService.findAllById(Collections.singletonList(1L));
//
//        assertThat(trainers).isNotNull();
//    }
//
//    @Test
//    public void testFindById_Success() {
//        when(trainerRepository.findById(1L)).thenReturn(Optional.of(trainerEntity));
//
//        Optional<TrainerEntity> trainer = trainerService.findById(1L);
//
//        assertThat(trainer).isPresent();
//        assertThat(trainer.get()).isEqualTo(trainerEntity);
//    }
//
//    @Test
//    public void testFindByTrainerFromUsername_Success() {
//        when(trainerRepository.findByTrainerFromUsername(userEntity.getUsername()))
//                .thenReturn(Optional.of(trainerEntity));
//
//        Optional<TrainerEntity> trainer = trainerService.findByTrainerFromUsername(userEntity.getUsername());
//
//        assertThat(trainer).isPresent();
//        assertThat(trainer.get()).isEqualTo(trainerEntity);
//    }
//}
