package www.com.springboot_iv.dao;

import org.springframework.data.repository.CrudRepository;

import www.com.springboot_iv.entity.Department;

public interface DepartmentDAO extends CrudRepository<Department, Integer> {
}
