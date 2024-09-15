package org.example.repository.impl;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.entity.UserEntity;
import org.example.repository.UserRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory;

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return Optional.ofNullable(sessionFactory.getCurrentSession()
                .createQuery("from UserEntity where username = :username", UserEntity.class)
                .setParameter("username", username)
                .uniqueResult());
    }

    @Override
    public Optional<List<String>> findAllUsername() {
        List<String> usernames = sessionFactory.getCurrentSession()
                .createQuery("select u.username from UserEntity u", String.class)
                .getResultList();
        return Optional.of(usernames);
    }

    @Override
    public void save(UserEntity user) {
        sessionFactory.getCurrentSession().persist(user);
    }

    @Override
    public void update(UserEntity user) {
        sessionFactory.getCurrentSession().detach(user);
    }

    @Override
    public void deleteByUsername(String username) {
        Optional<UserEntity> user = findByUsername(username);
        if (user.isPresent()) {
            UserEntity userEntity = user.get();
            sessionFactory.getCurrentSession().remove(userEntity);
        }
    }
}
