package ru.job4j.todo.service;

import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.store.TaskDbStore;

import java.util.List;

@Service
public class TaskService {

    private final TaskDbStore taskDbStore;

    public TaskService(TaskDbStore taskDbStore) {
        this.taskDbStore = taskDbStore;
    }

    public List<Task> findAll() {
        return taskDbStore.findAll();
    }

    public boolean add(Task task) {
        return taskDbStore.add(task);
    }

    public boolean delete(Task task) {
        return taskDbStore.delete(task);
    }

    public boolean update(Task task) {
        return taskDbStore.update(task);
    }

    public Task findById(int id) {
        return taskDbStore.findById(id);
    }

    public List<Task> findCompletedTasks() {
        return taskDbStore.findCompletedTasks();
    }

    public List<Task> findNotCompletedTasks() {
        return taskDbStore.findNotCompletedTasks();
    }

    public boolean makeTaskComplete(Task task) {
        return taskDbStore.makeTaskComplete(task);
    }


}
