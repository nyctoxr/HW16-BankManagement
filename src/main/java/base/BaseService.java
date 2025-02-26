package base;


import java.util.List;
import java.util.Optional;

public interface BaseService<T extends BaseEntity> {
    void create(T entity);

    void update(T entity);

    void deleteById(Long id);

    Optional<T> findById(Long id);

    List<T> findAll();
}