package org.example.repository.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.example.entity.TrainerEntity;
import org.example.entity.UserEntity;
import org.example.repository.UserDAO;
import org.example.storage.InMemoryStorage;
import org.springframework.stereotype.Repository;

/**
 * Implementation of UserDAO using an in-memory data store.
 */
@Repository
public class UserDAOImpl implements UserDAO {

    private final InMemoryStorage storage;
    public UserDAOImpl(InMemoryStorage storage) {
        this.storage = storage;
    }

    @Override
    public void save(UserEntity userEntity) {
        storage.getUserStorage().put(userEntity.getUserName(), userEntity);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return Optional.ofNullable(storage.getUserStorage().get(username));
    }

    @Override
    public void deleteByUsername(String username) {
        storage.getUserStorage().remove(username);
    }

    @Override
    public List<UserEntity> findAll() {
        return new ArrayList<>(storage.getUserStorage().values());
    }

    @Override
    public void updateUser(String username, UserEntity userEntity) {
        storage.getUserStorage().put(username, userEntity);
    }
}
