package repository.repoImplements;

import base.baseImplements.BaseRepositoryImpl;
import entity.Account;
import org.hibernate.Session;
import org.hibernate.query.Query;
import repository.repoInterface.AccountRepository;

import java.util.List;

public class AccountRepoImpl extends BaseRepositoryImpl<Account> implements AccountRepository {

    @Override
    public List<Account> getAllAccountsByCustomerId(Session session, Long customerId) {
        Query<Account> query = session.createQuery("from Account where customer.id = :customerId", Account.class);
        query.setParameter("customerId", customerId);
        return query.list();
    }


}
