package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import ru.job4j.todo.model.User;

import javax.persistence.NoResultException;
import java.util.Map;
import java.util.Optional;

@Repository
@Data
@AllArgsConstructor
public class UserDBStore {

    private final CrudRepository crudRepository;

    private static final Logger LOG = LoggerFactory.getLogger(UserDBStore.class.getName());

    public Optional<User> add(User user) {
        crudRepository.run(session -> session.persist(user));
        return Optional.of(user);
    }

    public Optional<User> findByLoginAndPassword(String login, String password) {
        try {
            return crudRepository.optional(
                    "from User u where u.login = :fLogin and u.password = :fPassword", User.class,
                    Map.of("fLogin", login, "fPassword", password)
            );
        } catch (NoResultException e) {
            LOG.error("Ошибка поиска по логину и паролю:", e);
            return Optional.empty();
        }
    }

    public Optional<User> findByLogin(String id) {
        try {
            return crudRepository.optional(
                    "from User u where u.login = :fLogin", User.class,
                    Map.of("fLogin", id)
            );
        } catch (NoResultException e) {
            LOG.error("Ошибка поиска по логину :", e);
            return Optional.empty();
        }
    }
}