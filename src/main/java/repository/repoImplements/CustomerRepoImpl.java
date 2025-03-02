package repository.repoImplements;

import base.BaseRepositoryImpl;
import entity.Customer;
import org.hibernate.Session;
import org.hibernate.query.Query;
import repository.repoInterface.CustomerRepository;

public class CustomerRepoImpl extends BaseRepositoryImpl<Customer> implements CustomerRepository {

    @Override
    public Customer findCustomerByNationalID(Session session, String nationalID) {
        Query<Customer> query = session.createQuery("from Customer where nationalID = :nationalID", Customer.class);
        query.setParameter("nationalID", nationalID);
        return query.uniqueResult();
    }
}
