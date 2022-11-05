package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Data
@AllArgsConstructor
public class TaskDbStore {

    private final CrudRepository crudRepository;

    private static final Logger LOG = LoggerFactory.getLogger(TaskDbStore.class.getName());

    /**
     * находит все задания в базе данных
     *
     * @return List с заданиями
     */
    public List<Task> findAll() {
        return crudRepository.query("select t from Task t join fetch t.priority", Task.class);
    }

    /**
     * добавляет задание в базу данных
     *
     * @param task - задание
     */
    public Task add(Task task) {
        task.setCreated(LocalDate.now());
        crudRepository.run(session -> session.merge(task));
        return task;
    }

    /**
     * удаляет задание из базы данных
     *
     * @param task - задание
     */
    public void delete(Task task) {
        crudRepository.run(
                "delete from Task where id = :fId",
                Map.of("fId", task.getId())
        );
    }

    /**
     * обновляет информацию в задании
     *
     * @param task - задание
     */
    public void update(Task task) {
        crudRepository.run(session -> session.merge(task));
    }

    /**
     * находит задание в базе данных по id
     *
     * @param id - id задания
     * @return возвращает объект Task
     */
    public Optional<Task> findById(int id) {
        return crudRepository.optional(
                "select t from Task t join fetch t.priority where t.id = :fId", Task.class,
                Map.of("fId", id)
        );
    }

    /**
     * ищет все выполненные задания
     *
     * @return List с выполненныи заданиями
     */
    public List<Task> findCompletedTasks() {
        return crudRepository.query("select t from Task t join fetch t.priority where t.done = :fDone",
                Task.class, Map.of("fDone", true));
    }

    /**
     * ищет все не выполненные задания
     *
     * @return List с невыполненными заданиями
     */
    public List<Task> findNotCompletedTasks() {
        return crudRepository.query("select t from Task t join fetch t.priority where t.done = :fDone",
                Task.class, Map.of("fDone", false));
    }

    /**
     * делает задание выполненным изменяя значение поля done
     *
     * @param task - задание
     */
    public void makeTaskComplete(Task task) {
        crudRepository.run(
                "update Task t set t.done = true where id = :fId",
                Map.of("fId", task.getId()));
    }

}
