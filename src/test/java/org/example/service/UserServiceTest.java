package org.example.service;

import org.example.repository.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserDAO userDao;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() throws Exception {
        // Optional: Use reflection to test internals (e.g., check final field assignment).
        Field userDaoField = UserService.class.getDeclaredField("userDao");
        userDaoField.setAccessible(true);
        userDaoField.set(userService, userDao);
    }

    @Test
    public void testGetAllUsernames_ReturnsUsernames() {
        // Arrange: mock the behavior of userDao
        List<String> mockUsernames = Arrays.asList("user1", "user2", "user3");
        when(userDao.findAllUsernames()).thenReturn(mockUsernames);

        // Act: call the method to be tested
        List<String> usernames = userService.getAllUsernames();

        // Assert: verify the expected result
        assertEquals(mockUsernames, usernames);
    }

    @Test
    public void testGetAllUsernames_EmptyList() {
        // Arrange: mock the behavior of userDao to return an empty list
        when(userDao.findAllUsernames()).thenReturn(Arrays.asList());

        // Act: call the method to be tested
        List<String> usernames = userService.getAllUsernames();

        // Assert: verify the expected result
        assertEquals(0, usernames.size());
    }

    @Test
    public void testGetAllUsernames_SingleUser() {
        // Arrange: mock the behavior of userDao to return one user
        List<String> mockUsernames = Arrays.asList("user1");
        when(userDao.findAllUsernames()).thenReturn(mockUsernames);

        // Act: call the method to be tested
        List<String> usernames = userService.getAllUsernames();

        // Assert: verify the expected result
        assertEquals(1, usernames.size());
        assertEquals("user1", usernames.get(0));
    }
}
