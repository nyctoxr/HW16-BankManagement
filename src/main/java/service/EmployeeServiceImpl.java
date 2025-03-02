package service;

import config.SessionFactoryInstance;
import entity.Customer;
import entity.Employee;

import exceptions.EmployeeNotFoundException;
import exceptions.PasswordIsWrongException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import repository.repoImplements.EmployeeRepoImpl;
import repository.repoInterface.EmployeeRepository;

public class EmployeeServiceImpl {

    public static Employee loggedInEmployee= new Employee();

    private final EmployeeRepository employeeRepository = new EmployeeRepoImpl();


    public void loginEmployee(String nationalID, String password) throws EmployeeNotFoundException, PasswordIsWrongException {
        Transaction tx = null;
        try (Session session = SessionFactoryInstance.
                getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Employee employee = employeeRepository.findByNationalId(session, nationalID);
            if (employee == null) {
                throw new EmployeeNotFoundException(
                        "Employee with this nationalID doesn't exist");
            }
            if (!employee.getPassword().equals(password)) {
                throw new PasswordIsWrongException("password doesn't match");

            }
            loggedInEmployee = employee;
            System.out.println("Successfully logged in as "
                    + employee.getFirstname()
                    + " " + employee.getLastname());
            tx.commit();

        } catch (EmployeeNotFoundException | PasswordIsWrongException | HibernateException e) {
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
