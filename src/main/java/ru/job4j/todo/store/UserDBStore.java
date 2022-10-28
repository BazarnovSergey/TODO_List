package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import ru.job4j.todo.model.User;

import java.util.Map;
import java.util.Optional;

@Repository
@Data
@AllArgsConstructor
public class UserDBStore {

    private final CrudRepository crudRepository;

    private static final Logger LOG = LoggerFactory.getLogger(TaskDbStore.class.getName());

    public Optional<User> add(User user) {
        crudRepository.run(session -> session.save(user));
        return Optional.of(user);
    }

    public Optional<User> findByLoginAndPassword(String login, String password) {
        return crudRepository.optional(
                "from User u where u.login = :fLogin and u.password = :fPassword", User.class,
                Map.of("fLogin", login, "fPassword", password)
        );
    }
}