package service;

import base.baseImplements.BaseServiceImpl;
import config.SessionFactoryInstance;
import entity.Account;
import entity.BankBranch;
import entity.Customer;
import exceptions.CustomerNotFoundException;
import exceptions.PasswordIsWrongException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import repository.repoImplements.CustomerRepoImpl;
import repository.repoInterface.CustomerRepository;
import util.ValidatorUtil;

import java.time.LocalDate;

public class CustomerServiceImpl extends BaseServiceImpl<Customer> {

    private final CustomerRepository customerRepository = new CustomerRepoImpl();
    private final CardServiceImpl cardService = new CardServiceImpl();
    private final AccountServiceImpl accountService = new AccountServiceImpl();


    public CustomerServiceImpl() {
        super(Customer.class);
    }

    public static Customer loggedInCustomer;


    public void register(String firstname, String lastname, String nationalID, String phone, String password,
                         LocalDate birthdate, BankBranch branch) {
        Transaction tx = null;
        try (Session session = SessionFactoryInstance.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Customer customer = new Customer();
            customer.setFirstname(firstname);
            customer.setLastname(lastname);
            customer.setNationalID(nationalID);
            customer.setPhone(phone);
            customer.setPassword(password);
            customer.setBirthdate(birthdate);

            if (!ValidatorUtil.validate(customer)) {
                throw new IllegalArgumentException("Customer validation failed");
            }

            customerRepository.persist(session, customer);
            Account account = accountService.createAccountInSession(session, customer, branch, 0.0);
            cardService.createCardInSession(session, account, "1234");

            tx.commit();
            System.out.println("Customer registered successfully: " + firstname + " " + lastname);
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error during registration: " + e.getMessage(), e);
        }
    }



    public void login(String nationalID, String password)
            throws CustomerNotFoundException, PasswordIsWrongException {
        Transaction tx = null;
        try (Session session = SessionFactoryInstance.getSessionFactory().openSession()) {
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


        } catch (CustomerNotFoundException | PasswordIsWrongException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Unexpected error during login: " + e.getMessage(), e);
        }
    }
}

