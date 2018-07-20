package www.com.springboot_iv.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import www.com.springboot_iv.dao.DepartmentDAO;
import www.com.springboot_iv.entity.Department;

@RestController
public class DepartmentAPI {

	@Autowired
	private DepartmentDAO departmentDao;

	@RequestMapping(value = "/createDepartment", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Department createDepartment(@RequestBody Department department) {
		try {
			return departmentDao.save(department);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/updateDepartment", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public @ResponseBody Department updateDepartment(@RequestBody Department department) {
		try {
			if(department.getId()==null) {
				return null;
			}
			return departmentDao.save(department);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/deleteDepartment", method = RequestMethod.DELETE)
	public Boolean deleteDepartment(@RequestParam Integer id) {
		try {
			departmentDao.delete(id);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
