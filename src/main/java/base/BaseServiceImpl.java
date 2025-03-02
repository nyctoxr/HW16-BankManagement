package base;


import config.SessionFactoryInstance;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public abstract class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {
    private final BaseRepository<T, Long> repository;
    private final Class<T> entityClass;


    public BaseServiceImpl(Class<T> entityClass) {
        this.repository = new BaseRepositoryImpl<>();
        this.entityClass = entityClass;
    }


    private SessionFactory getSessionFactory() {
        return SessionFactoryInstance.getSessionFactory();
    }

    @Override
    public void create(T entity) {
        try (Session session = getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            repository.persist(session, entity);
            tx.commit();
        }
    }

    @Override
    public void update(T entity) {
        try (Session session = getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            repository.update(session, entity);
            tx.commit();
        }
    }

    @Override
    public void deleteById(Long id) {
        try (Session session = getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            repository.deleteById(session, entityClass, id);
            tx.commit();
        }
    }

    @Override
    public Optional<T> findById(Long id) {
        try (Session session = getSessionFactory().openSession()) {
            return repository.findById(session, entityClass, id);
        }
    }

    @Override
    public List<T> findAll() {
        try (Session session = getSessionFactory().openSession()) {
            return repository.findAll(session, entityClass);
        }
    }
}