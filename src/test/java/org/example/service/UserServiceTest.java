package org.example.service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import org.example.repository.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() throws Exception {
        Field userDaoField = UserService.class.getDeclaredField("userDao");
        userDaoField.setAccessible(true);
        userDaoField.set(userService, userDao);
    }

    @Test
    public void testGetAllUsernames_ReturnsUsernames() {
        List<String> mockUsernames = Arrays.asList("user1", "user2", "user3");
        when(userDao.findAllUsernames()).thenReturn(mockUsernames);

        List<String> usernames = userService.getAllUsernames();

        assertEquals(mockUsernames, usernames);
    }

    @Test
    public void testGetAllUsernames_EmptyList() {
        when(userDao.findAllUsernames()).thenReturn(Arrays.asList());

        List<String> usernames = userService.getAllUsernames();

        assertEquals(0, usernames.size());
    }

    @Test
    public void testGetAllUsernames_SingleUser() {
        List<String> mockUsernames = Arrays.asList("user1");
        when(userDao.findAllUsernames()).thenReturn(mockUsernames);

        List<String> usernames = userService.getAllUsernames();

        assertEquals(1, usernames.size());
        assertEquals("user1", usernames.get(0));
    }
}
