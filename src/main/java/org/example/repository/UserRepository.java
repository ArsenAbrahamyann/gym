package org.example.repository;

import java.util.List;
import java.util.Optional;
import org.example.entity.UserEntity;

public interface UserRepository {

    Optional<UserEntity> findByUsername(String username);

    Optional<List<String>> findAllUsername();

    void save(UserEntity user);

    void update(UserEntity user);

    void deleteByUsername(String username);
}
