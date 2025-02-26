package base;


import org.hibernate.Session;
import java.util.List;
import java.util.Optional;

public interface BaseRepository<T extends BaseEntity, ID> {
    Optional<T> findById(Session session, Class<T> clazz, ID id);
    List<T> findAll(Session session, Class<T> clazz);
    void persist(Session session, T entity);
    void update(Session session, T entity);
    void deleteById(Session session, Class<T> clazz, ID id);
}
