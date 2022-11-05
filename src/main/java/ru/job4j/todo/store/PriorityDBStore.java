package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Priority;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Data
@AllArgsConstructor
public class PriorityDBStore {

    private final CrudRepository crudRepository;

    public List<Priority> findAll() {
        return crudRepository.query("select p from Priority p", Priority.class);
    }

    public Optional<Priority> findById(int id) {
        return crudRepository.optional(
                "from Priority where id = :fId", Priority.class,
                Map.of("fId", id)
        );
    }
}
