package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.time.LocalDate;
import java.util.List;

@Repository
@Data
@AllArgsConstructor
public class TaskDbStore {

    private final SessionFactory sf;

    private static final Logger LOG = LoggerFactory.getLogger(TaskDbStore.class.getName());

    /**
     * находит все задания в базе данных
     *
     * @return List с заданиями
     */
    public List<Task> findAll() {
        Session session = sf.openSession();
        List<Task> taskList;
        taskList = (List<Task>) session.createQuery(
                        "from Task", Task.class)
                .list();
        session.close();
        return taskList;
    }

    /**
     * добавляет задание в базу данных
     *
     * @param task - задание
     */
    public void add(Task task) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            task.setCreated(LocalDate.now());
            session.save(task);
            session.getTransaction().commit();

        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        }
        session.close();
    }

    /**
     * удаляет задание из базы данных
     *
     * @param task - задание
     */
    public void delete(Task task) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.delete(task);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            LOG.error("Ошибка удаления из базы данных :", e);
        }
        session.close();
    }

    /**
     * обновляет информацию в задании
     *
     * @param task - задание
     */
    public void update(Task task) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            task.setCreated(LocalDate.now());
            session.update(task);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        session.close();
    }

    /**
     * находит задание в базе данных по id
     *
     * @param id - id задания
     * @return возвращает объект Task
     */
    public Task findById(int id) {
        Session session = sf.openSession();
        Task task = session.createQuery(
                        "from Task where id = :fId ", Task.class)
                .setParameter("fId", id)
                .uniqueResult();
        session.close();
        return task;
    }

    /**
     * ищет все выполненные задания
     *
     * @return List с выполненныи заданиями
     */
    public List<Task> findCompletedTasks() {
        Session session = sf.openSession();
        List<Task> taskList;
        taskList = (List<Task>) session.createQuery(
                        "from Task where done = true", Task.class)
                .list();
        session.close();
        return taskList;
    }

    /**
     * ищет все не выполненные задания
     *
     * @return List с невыполненными заданиями
     */
    public List<Task> findNotCompletedTasks() {
        Session session = sf.openSession();
        List<Task> taskList;
        taskList = (List<Task>) session.createQuery(
                        "from Task where done = false", Task.class)
                .list();
        session.close();
        return taskList;
    }

}
