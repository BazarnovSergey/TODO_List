package ru.job4j.todo.service;

import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.store.TaskDbStore;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskDbStore taskDbStore;

    public TaskService(TaskDbStore taskDbStore) {
        this.taskDbStore = taskDbStore;
    }

    public List<Task> findAll() {
        return taskDbStore.findAll();
    }

    public Task add(Task task) {
        return taskDbStore.add(task);
    }

    public void delete(Task task) {
        taskDbStore.delete(task);
    }

    public void update(Task task) {
        taskDbStore.update(task);
    }

    public Optional<Task> findById(int id) {
        return taskDbStore.findById(id);
    }

    public List<Task> findCompletedTasks() {
        return taskDbStore.findCompletedTasks();
    }

    public List<Task> findNotCompletedTasks() {
        return taskDbStore.findNotCompletedTasks();
    }

    public void makeTaskComplete(Task task) {
        taskDbStore.makeTaskComplete(task);
    }


}
