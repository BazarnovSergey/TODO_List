package ru.job4j.todo.service;

import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.store.PriorityDBStore;

import java.util.List;
import java.util.Optional;

@Service
public class PriorityService {

    private final PriorityDBStore priorityDBStore;

    public PriorityService(PriorityDBStore priorityDBStore) {
        this.priorityDBStore = priorityDBStore;
    }

    public List<Priority> findAll() {
        return priorityDBStore.findAll();
    }

    public Optional<Priority> findById(int id) {
        return priorityDBStore.findById(id);
    }
}