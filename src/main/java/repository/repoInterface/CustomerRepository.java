package repository.repoInterface;

import base.interfaces.BaseRepository;
import entity.Customer;
import org.hibernate.Session;

public interface CustomerRepository extends BaseRepository<Customer,Long> {


    Customer findCustomerByNationalID(Session session, String nationalID);
}
