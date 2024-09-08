package org.example.repository;

import java.util.List;

public interface UserDao {
    List<String> findAllUsernames();
}
