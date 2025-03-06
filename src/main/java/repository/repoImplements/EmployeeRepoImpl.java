package repository.repoImplements;

import base.baseImplements.BaseRepositoryImpl;
import entity.Employee;
import org.hibernate.Session;
import org.hibernate.query.Query;
import repository.repoInterface.EmployeeRepository;

public class EmployeeRepoImpl extends BaseRepositoryImpl<Employee> implements EmployeeRepository {

    @Override
    public Employee findByNationalId(Session session, String nationalId) {
        Query<Employee> query = session.createQuery("from Employee where nationalID = :nationalId", Employee.class);
        query.setParameter("nationalId", nationalId);
        return query.uniqueResult();
    }
}
