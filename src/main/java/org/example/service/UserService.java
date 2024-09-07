package org.example.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.repository.UserDAO;
import org.springframework.stereotype.Service;

/**
 * Service class for managing user entities.
 * <p>
 * This service provides methods for creating, updating, deleting, and retrieving user entities.
 * It interacts with the {@link UserDAO} to handle persistence and retrieval of user data.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDAO userDao;

    /**
     * Retrieves a list of all usernames from the data store.
     * <p>
     * This method fetches all usernames currently stored in the data store. The usernames are
     * retrieved using the {@link UserDAO} and returned as a list of strings.
     * </p>
     *
     * @return a list of all usernames
     */
    public List<String> getAllUsernames() {
        return userDao.findAllUsernames();
    }
}
