package config;

import entity.*;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionFactoryInstance {

    @Getter
    private static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration()
                    .configure()
                    .addAnnotatedClass(Account.class)
                    .addAnnotatedClass(BankBranch.class)
                    .addAnnotatedClass(Card.class)
                    .addAnnotatedClass(Customer.class)
                    .addAnnotatedClass(Employee.class)
                    .addAnnotatedClass(Transaction.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }
}