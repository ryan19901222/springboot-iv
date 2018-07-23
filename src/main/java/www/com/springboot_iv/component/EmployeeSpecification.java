package www.com.springboot_iv.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import www.com.springboot_iv.entity.Department;
import www.com.springboot_iv.entity.Employee;

@Component
public class EmployeeSpecification {

	public Specification<Employee> queryByIdAndNameAndAgeAndDepartmentName(Integer employeeId, String employeeName,
			Integer employeeAge, String departmentName) {
		
		Optional<Integer> employeeIdOptional = Optional.ofNullable(employeeId);
		Optional<String> employeeNameOptional = Optional.ofNullable(employeeName);
		Optional<Integer> employeeAgeOptional = Optional.ofNullable(employeeAge);
		Optional<String> departmentNameOptional = Optional.ofNullable(departmentName);
		return new Specification<Employee>() {
			public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicatesList = new ArrayList<>();
				if (employeeIdOptional.isPresent()) {
					Path<Integer> idPath = root.get("id");
					predicatesList.add(cb.equal(idPath, employeeId));
				}
				if (employeeNameOptional.isPresent()) {
					Path<String> namePath = root.get("name");
					predicatesList.add(cb.equal(namePath, employeeName));
				}
				if (employeeAgeOptional.isPresent()) {
					Path<Integer> agePath = root.get("age");
					predicatesList.add(cb.equal(agePath, employeeAge));
				}
				if (departmentNameOptional.isPresent()) {
					Path<Department> departmentPath = root.get("department");
					Path<String> departmentNamePath = departmentPath.get("name");
					predicatesList.add(cb.equal(departmentNamePath, departmentName));
				}
				Predicate[] predicates = new Predicate[predicatesList.size()];
				Predicate predicate = cb.and(predicatesList.toArray(predicates));
				query.where(predicate);
				return null;
			}
		};
	}

}
