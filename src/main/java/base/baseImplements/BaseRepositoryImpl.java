package base.baseImplements;


import base.BaseEntity;
import base.interfaces.BaseRepository;
import org.hibernate.Session;
import java.util.List;
import java.util.Optional;

public class BaseRepositoryImpl<T extends BaseEntity> implements BaseRepository<T, Long> {

    @Override
    public Optional<T> findById(Session session, Class<T> clazz, Long id) {
        return Optional.ofNullable(session.get(clazz, id));
    }

    @Override
    public List<T> findAll(Session session, Class<T> clazz) {
        return session.createQuery("SELECT e FROM " + clazz.getSimpleName() + " e", clazz)
                .getResultList();
    }

    @Override
    public void persist(Session session, T entity) {
        session.persist(entity);
    }

    @Override
    public void update(Session session, T entity) {
        session.merge(entity);
    }

    @Override
    public void deleteById(Session session, Class<T> clazz, Long id) {
        T entity = session.get(clazz, id);
        if (entity != null) {
            session.remove(entity);
        }
    }
}