package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import ru.job4j.todo.model.User;

import java.util.Optional;

@Repository
@Data
@AllArgsConstructor
public class UserDBStore {

    private final SessionFactory sf;

    private static final Logger LOG = LoggerFactory.getLogger(TaskDbStore.class.getName());

    public Optional<User> add(User user) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            return Optional.of(user);
        } catch (Exception e) {
            session.getTransaction().rollback();
            LOG.error("Ошибка добавления пользователя в базу данных: ", e);
        }
        session.close();
        return Optional.empty();
    }

    public Optional<User> findByLoginAndPassword(String login, String password) {
        Optional<User> rsl = Optional.empty();
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            rsl = (Optional<User>) session.createQuery(
                            "from User where login = :fLogin and password = :fPassword", User.class)
                    .setParameter("fLogin", login)
                    .setParameter("fPassword", password)
                    .uniqueResultOptional();
            session.getTransaction().commit();
            session.close();
        } catch (HibernateException e) {
            LOG.error("Ошибка поиска пользователя по логину и паролю в базе данных: ", e);
        }
        return rsl;
    }

}
