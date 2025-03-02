package service;

import base.BaseServiceImpl;
import config.SessionFactoryInstance;
import entity.Customer;
import exceptions.CustomerNotFoundException;
import exceptions.PasswordIsWrongException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import repository.repoImplements.CustomerRepoImpl;
import repository.repoInterface.CustomerRepository;

public class CustomerServiceImpl extends BaseServiceImpl<Customer> {

    private final CustomerRepository customerRepository = new CustomerRepoImpl();


    public CustomerServiceImpl() {
        super(Customer.class);
    }

    public static Customer loggedInCustomer;

    public void login(String nationalID, String password) throws CustomerNotFoundException, PasswordIsWrongException {
        Transaction tx = null;
        Session session = SessionFactoryInstance.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            Customer customer = customerRepository.findCustomerByNationalID(session, nationalID);


            if (customer == null) {
                throw new CustomerNotFoundException("Customer with this nationalID doesn't exist");
            }
            if (!customer.getPassword().equals(password)) {
                throw new PasswordIsWrongException("password doesn't match");

            }
            loggedInCustomer = customer;
            tx.commit();


        } catch (CustomerNotFoundException|PasswordIsWrongException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;

        }catch (Exception e){
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Unexpected error during login: " + e.getMessage(), e);
        }
    }
}

