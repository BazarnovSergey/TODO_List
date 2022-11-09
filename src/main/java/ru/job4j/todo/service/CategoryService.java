package ru.job4j.todo.service;

import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.store.CategoryDBStore;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryDBStore categoryDBStore;

    public CategoryService(CategoryDBStore priorityDBStore) {
        this.categoryDBStore = priorityDBStore;
    }

    public Category add(Category category) {
        return categoryDBStore.add(category);
    }

    public List<Category> findAll() {
        return categoryDBStore.findAll();
    }

    public Optional<Category> findById(int id) {
        return categoryDBStore.findById(id);
    }

}
