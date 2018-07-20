package www.com.springboot_iv.api;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import www.com.springboot_iv.dao.EmployeeDAO;
import www.com.springboot_iv.dto.ModifyEmployeeDTO;
import www.com.springboot_iv.entity.Department;
import www.com.springboot_iv.entity.Employee;

@RestController
public class EmployeeAPI {

	@Autowired
	private EmployeeDAO employeeDao;
	
	@RequestMapping(value = "/createEmployee", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Employee createEmployee(@RequestBody ModifyEmployeeDTO modifyEmployeeDTO) {
		try {
			Employee employee =new Employee();
			BeanUtils.copyProperties(modifyEmployeeDTO, employee);
			return employeeDao.save(employee);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/updateEmployee", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public @ResponseBody Employee updateEmployee(@RequestBody ModifyEmployeeDTO modifyEmployeeDTO) {
		try {
			Integer employeeId = modifyEmployeeDTO.getId();
			if (employeeId == null) {
				return null;
			}
			Employee employee = employeeDao.findOne(employeeId);
			BeanUtils.copyProperties(modifyEmployeeDTO, employee);
			return employeeDao.save(employee);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/deleteEmployee", method = RequestMethod.DELETE)
	public Boolean deleteEmployee(@RequestParam Integer id) {
		try {
			employeeDao.delete(id);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@RequestMapping(value = "/findEmployee", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Page<Employee> findEmployee(
			@RequestParam(value = "employeeId", required = false) Integer employeeId,
			@RequestParam(value = "employeeName", required = false) String employeeName,
			@RequestParam(value = "employeeAge", required = false) Integer employeeAge,
			@RequestParam(value = "departmentName", required = false) String departmentName,
			@RequestParam(value = "pageNum", required = false) Integer pageNum) {
		try {

			Specification<Employee> specification = new Specification<Employee>() {
				public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					if (employeeId != null || employeeName != null || employeeAge != null || departmentName != null) {
						Path<Integer> id = root.get("id");
						Path<String> name = root.get("name");
						Path<Integer> age = root.get("age");
						Path<Department> department = root.get("department");
						Path<String> $departmentName = department.get("name");
						Predicate p1 = cb.equal(id, employeeId);
						Predicate p2 = cb.equal(name, employeeName);
						Predicate p3 = cb.equal(age, employeeAge);
						Predicate p4 = cb.equal($departmentName, departmentName);
						Predicate p = cb.or(p1, p2, p3, p4);
						query.where(p);
					}
					return null;
				}
			};
			int $pageNum = 0;
			if (pageNum != null) {
				$pageNum = pageNum - 1;
			}
			Page<Employee> employeeList = employeeDao.findAll(specification, new PageRequest($pageNum,10));
			return employeeList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
