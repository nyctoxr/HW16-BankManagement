package repository.repoInterface;

import base.BaseRepository;
import entity.Employee;
import org.hibernate.Session;

public interface EmployeeRepository extends BaseRepository<Employee,Long> {


    Employee findByNationalId(Session session, String nationalId);
}
