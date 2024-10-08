//package org.example.repository;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import org.example.entity.UserEntity;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//public class UserRepositoryTest {
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private UserEntity user;
//
//    /**
//     * Sets up the test environment by initializing a {@link UserEntity} instance with predefined sample data.
//     * This method ensures that a {@code UserEntity} object with specific attributes is available for use in test methods,
//     * providing a consistent starting state for each test.
//     */
//    @BeforeEach
//    public void setUp() {
//        user = new UserEntity();
//        user.setId(1L);
//        user.setUsername("testuser");
//        user.setPassword("password");
//        user.setFirstName("John");
//        user.setLastName("Doe");
//        user.setIsActive(true);
//    }
//
//    @Test
//    public void testFindByUsername() {
//        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
//
//        Optional<UserEntity> result = userRepository.findByUsername("testuser");
//
//        assertTrue(result.isPresent());
//        assertEquals(user.getUsername(), result.get().getUsername());
//        verify(userRepository, times(1)).findByUsername(anyString());
//    }
//
//    @Test
//    public void testFindAllUsername() {
//        List<String> usernames = new ArrayList<>();
//        usernames.add("testuser");
//        when(userRepository.findAllUsername()).thenReturn(usernames);
//
//        List<String> result = userRepository.findAllUsername();
//
//        assertEquals(usernames.size(), result.size());
//        assertEquals("testuser", result.get(0));
//        verify(userRepository, times(1)).findAllUsername();
//    }
//
//    @Test
//    public void testSave() {
//        userRepository.save(user);
//
//        verify(userRepository, times(1)).save(any(UserEntity.class));
//    }
//
//    @Test
//    public void testUpdate() {
//        doNothing().when(userRepository).update(any(UserEntity.class));
//
//        userRepository.update(user);
//
//        verify(userRepository, times(1)).update(any(UserEntity.class));
//    }
//
//    @Test
//    public void testDeleteByUsername() {
//        doNothing().when(userRepository).deleteByUsername(anyString());
//
//        userRepository.deleteByUsername("testuser");
//
//        verify(userRepository, times(1)).deleteByUsername(anyString());
//    }
//}
