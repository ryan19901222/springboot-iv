package www.com.springboot_iv.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import www.com.springboot_iv.entity.Employee;

public interface EmployeeDAO extends CrudRepository<Employee, Integer>, JpaSpecificationExecutor<Employee> {
}
