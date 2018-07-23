package www.com.springboot_iv.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import www.com.springboot_iv.dao.DepartmentDAO;
import www.com.springboot_iv.dto.CreateDepartmentDTO;
import www.com.springboot_iv.dto.UpdateDepartmentDTO;
import www.com.springboot_iv.entity.Department;
import www.com.springboot_iv.entity.Employee;

@RestController
public class DepartmentAPI {

	@Autowired
	private DepartmentDAO departmentDao;

	@RequestMapping(value = "/createDepartment", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody ResponseEntity<Object> createDepartment(@RequestBody CreateDepartmentDTO createDepartmentDTO) {
		Department department = new Department();
		BeanUtils.copyProperties(createDepartmentDTO, department);
		return new ResponseEntity<>(departmentDao.save(department), HttpStatus.OK);
	}

	@RequestMapping(value = "/updateDepartment", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public @ResponseBody ResponseEntity<Object> updateDepartment(
			@Validated @RequestBody UpdateDepartmentDTO updateDepartmentDTO) {
		
		Department department = departmentDao.findOne(updateDepartmentDTO.getId());
		Optional<Department> departmentOptional = Optional.ofNullable(department);
		if (!departmentOptional.isPresent()) {
			return new ResponseEntity<>("{ \"message\": \"Id is not found\"}", HttpStatus.NOT_FOUND);
		}
		BeanUtils.copyProperties(updateDepartmentDTO, department);
		return new ResponseEntity<>(departmentDao.save(department), HttpStatus.OK);
	}

	@RequestMapping(value = "/removeDepartment", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
	public @ResponseBody ResponseEntity<Object> removeDepartment(@RequestParam Integer id) {
		Department department = departmentDao.findOne(id);
		Optional<Department> departmentOptional = Optional.ofNullable(department);
		if (!departmentOptional.isPresent()) {
			return new ResponseEntity<>("{ \"message\": \"Id is not found\"}", HttpStatus.NOT_FOUND);
		}
		Optional<List<Employee>> employeesOptional = Optional.ofNullable(department.getEmployees());
		if (employeesOptional.isPresent() ) {
			return new ResponseEntity<>("{ \"message\": \"This department has employees\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		departmentDao.delete(department);
		return new ResponseEntity<>("{ \"message\": \"Remove success\"}", HttpStatus.OK);
	}

}
