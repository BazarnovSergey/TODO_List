package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@Data
@AllArgsConstructor
public class CategoryDBStore {
    private final CrudRepository crudRepository;

    public Category add(Category category) {
        crudRepository.run(session -> session.merge(category));
        return category;
    }

    public List<Category> findAll() {
        return crudRepository.query("select c from Category c", Category.class);
    }

    public Optional<Category> findById(int id) {
        return crudRepository.optional(
                "from Category where id = :fId", Category.class,
                Map.of("fId", id)
        );
    }
}
