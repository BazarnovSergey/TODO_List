package ru.job4j.todo.store;

import ru.job4j.todo.Main;
import ru.job4j.todo.model.Task;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TaskDbStoreTest {

    @Test
    void whenAdd() {
        TaskDbStore store = new TaskDbStore(new Main().sf());
        Task task = new Task(0, "name", "description", LocalDate.now(), true);
        store.add(task);
        Task taskInDb = store.findById(task.getId());
        assertThat(taskInDb.getDescription()).isEqualTo(task.getDescription());
    }

    @Test
    void whenDelete() {
        TaskDbStore store = new TaskDbStore(new Main().sf());
        Task task = new Task(0, "name", "description", LocalDate.now(), true);
        store.add(task);
        store.delete(task);
        Task taskInDb = store.findById(task.getId());
        assertThat(taskInDb).isEqualTo(null);
    }

    @Test
    void whenUpdate() {
        TaskDbStore store = new TaskDbStore(new Main().sf());
        Task task = new Task(0, "name", "description", LocalDate.now(), true);
        store.add(task);
        store.update(new Task(task.getId(), "name", "description2", LocalDate.now(), false));
        Task taskInDb = store.findById(task.getId());
        assertThat(taskInDb.getDescription()).isEqualTo("description2");
    }

    @Test
    void whenFindById() {
        TaskDbStore store = new TaskDbStore(new Main().sf());
        Task task1 = new Task(0, "name1", "description1", LocalDate.now(), true);
        Task task2 = new Task(0, "name2", "description2", LocalDate.now(), true);
        store.add(task1);
        store.add(task2);
        Task task1InDb = store.findById(task1.getId());
        Task task2InDb = store.findById(task2.getId());
        assertThat(task1InDb.getId()).isEqualTo(task1.getId());
        assertThat(task2InDb.getId()).isEqualTo(task2.getId());
    }

}