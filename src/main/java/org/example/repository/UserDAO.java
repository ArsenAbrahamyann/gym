package org.example.repository;

import java.util.List;

public interface UserDAO {
    List<String> findAllUsernames();
}
