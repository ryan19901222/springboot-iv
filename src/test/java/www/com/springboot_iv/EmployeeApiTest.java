package www.com.springboot_iv;

import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import junit.framework.TestCase;
import www.com.springboot_iv.component.EmployeeSpecification;
import www.com.springboot_iv.dao.EmployeeDAO;
import www.com.springboot_iv.dto.CreateEmployeeDTO;
import www.com.springboot_iv.dto.UpdateEmployeeDTO;
import www.com.springboot_iv.entity.Department;
import www.com.springboot_iv.entity.Employee;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeApiTest extends TestCase {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private EmployeeDAO employeeDao;
	
	@MockBean
	private EmployeeSpecification employeeSpecification;
	
	@Before
	public void before() throws Exception {
		MockitoAnnotations.initMocks(this);
		Mockito.reset(employeeDao);
	}

	//測試新增員工API
	@Transactional
	@Test
	public void verifySuccessCreateEmployee() throws Exception {
		CreateEmployeeDTO createEmployeeDTO=new CreateEmployeeDTO();
		createEmployeeDTO.setName("jay");
		Department department=new Department();
		department.setId(1);
		department.setName("人資部");
		createEmployeeDTO.setDepartment(department);
		createEmployeeDTO.setGender("male");
		createEmployeeDTO.setPhone("0987654332");
		createEmployeeDTO.setAddress("南投市");
		createEmployeeDTO.setAge(24);
		
		Employee employee = new Employee();
		BeanUtils.copyProperties(createEmployeeDTO, employee);
		Employee returnEmployee = new Employee();
		BeanUtils.copyProperties(createEmployeeDTO, returnEmployee);
		returnEmployee.setId(12);
		Mockito.when(employeeDao.save(employee)).thenReturn(returnEmployee);
		
		mockMvc.perform(
				MockMvcRequestBuilders.post("/createEmployee")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(new ObjectMapper().writeValueAsString(createEmployeeDTO)))
				//驗證是否為200
				.andExpect(MockMvcResultMatchers.status().isOk())
				//驗證是否有id,name,departmentId,departmentName,gender,phone,address,age,createTime,updateTime回傳值
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").exists())
				.andExpect(jsonPath("$.department.id").exists())
				.andExpect(jsonPath("$.department.name").exists())
				.andExpect(jsonPath("$.gender").exists())
				.andExpect(jsonPath("$.phone").exists())
				.andExpect(jsonPath("$.address").exists())
				.andExpect(jsonPath("$.age").exists())
				.andExpect(jsonPath("$.createTime").isEmpty())
				.andExpect(jsonPath("$.updateTime").isEmpty())
				//驗證新增後回傳值是否與預期符合
				.andExpect(jsonPath("$.id").value(12))
				.andExpect(jsonPath("$.name").value("jay"))
				.andExpect(jsonPath("$.department.id").value(1))
				.andExpect(jsonPath("$.department.name").value("人資部"))
				.andExpect(jsonPath("$.gender").value("male"))
				.andExpect(jsonPath("$.phone").value("0987654332"))
				.andExpect(jsonPath("$.address").value("南投市"))
				.andExpect(jsonPath("$.age").value(24))
				.andDo(MockMvcResultHandlers.print());
		
		//驗證預期動作是否已執行
		Mockito.verify(employeeDao,times(1)).save(employee);
		Mockito.verifyNoMoreInteractions(employeeDao);
	}
	
	//測試更新員工資料API，成功狀況
	@Transactional
	@Test
	public void verifySuccessUpdateEmployee() throws Exception {
		UpdateEmployeeDTO updateEmployeeDTO=new UpdateEmployeeDTO();
		updateEmployeeDTO.setId(1);
		updateEmployeeDTO.setName("dylan");
		Department department=new Department();
		department.setId(1);
		department.setName("人資部");
		updateEmployeeDTO.setDepartment(department);
		updateEmployeeDTO.setGender("male");
		updateEmployeeDTO.setPhone("0912345678");
		updateEmployeeDTO.setAddress("台南市");
		updateEmployeeDTO.setAge(25);
		
		Employee findReturnEmployee=new Employee();
		BeanUtils.copyProperties(updateEmployeeDTO, findReturnEmployee);
		findReturnEmployee.setPhone("0987654321");
		Employee updateReturnEmployee=new Employee();
		BeanUtils.copyProperties(updateEmployeeDTO, updateReturnEmployee);
		
		Mockito.when(employeeDao.findOne(updateEmployeeDTO.getId())).thenReturn(findReturnEmployee);
		Mockito.when(employeeDao.save(findReturnEmployee)).thenReturn(updateReturnEmployee);
		
		mockMvc.perform(
				MockMvcRequestBuilders.put("/updateEmployee")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(new ObjectMapper().writeValueAsString(updateEmployeeDTO)))
				//驗證是否為200
				.andExpect(MockMvcResultMatchers.status().isOk())
				//驗證是否有id,name,departmentId,departmentName,gender,phone,address,age,createTime,updateTime回傳值
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").exists())
				.andExpect(jsonPath("$.department.id").exists())
				.andExpect(jsonPath("$.department.name").exists())
				.andExpect(jsonPath("$.gender").exists())
				.andExpect(jsonPath("$.phone").exists())
				.andExpect(jsonPath("$.address").exists())
				.andExpect(jsonPath("$.age").exists())
				.andExpect(jsonPath("$.createTime").isEmpty())
				.andExpect(jsonPath("$.updateTime").isEmpty())
				//驗證更新後回傳值是否與預期符合
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value("dylan"))
				.andExpect(jsonPath("$.department.id").value(1))
				.andExpect(jsonPath("$.department.name").value("人資部"))
				.andExpect(jsonPath("$.gender").value("male"))
				.andExpect(jsonPath("$.phone").value("0912345678"))
				.andExpect(jsonPath("$.address").value("台南市"))
				.andExpect(jsonPath("$.age").value(25))
				.andDo(MockMvcResultHandlers.print());
		
		//驗證預期動作是否已執行
		Mockito.verify(employeeDao,times(1)).findOne(updateEmployeeDTO.getId());
		Mockito.verify(employeeDao,times(1)).save(findReturnEmployee);
		Mockito.verifyNoMoreInteractions(employeeDao);
	}
	
	//驗證更新員工資料API，資料庫無此id狀況
	@Transactional
	@Test
	public void verifyIdNotFoundUpdateEmployee() throws Exception {
		UpdateEmployeeDTO updateEmployeeDTO=new UpdateEmployeeDTO();
		updateEmployeeDTO.setId(12);
		updateEmployeeDTO.setName("dylan");
		Department department=new Department();
		department.setId(1);
		department.setName("人資部");
		updateEmployeeDTO.setDepartment(department);
		updateEmployeeDTO.setGender("male");
		updateEmployeeDTO.setPhone("0912345678");
		updateEmployeeDTO.setAddress("台南市");
		updateEmployeeDTO.setAge(25);
		Mockito.when(employeeDao.findOne(updateEmployeeDTO.getId())).thenReturn(null);
		
		mockMvc.perform(
				MockMvcRequestBuilders.put("/updateEmployee")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(new ObjectMapper().writeValueAsString(updateEmployeeDTO)))
				//驗證是否為404
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				//驗證是否有錯誤訊息回傳值
				.andExpect(jsonPath("$.message").exists())
				//驗證錯誤訊息是否為id找不到
				.andExpect(jsonPath("$.message").value("Id is not found"))
				.andDo(MockMvcResultHandlers.print());
		
		//驗證預期動作是否已執行
		Mockito.verify(employeeDao,times(1)).findOne(updateEmployeeDTO.getId());
		Mockito.verifyNoMoreInteractions(employeeDao);
	}
		
	//驗證刪除員工API，成功狀況
	@Transactional
	@Test
	public void verifySuccessRemoveEmployee() throws Exception {
		Integer id = 1;
		Mockito.doNothing().when(employeeDao).delete(id);
		
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/removeEmployee")
				.param("id", Integer.toString(id)))
				//驗證是否為200
				.andExpect(MockMvcResultMatchers.status().isOk())
				//驗證是否有訊息回傳值
				.andExpect(jsonPath("$.message").exists())
				//驗證回傳訊息是否為移除成功
				.andExpect(jsonPath("$.message").value("Remove success"))
				.andDo(MockMvcResultHandlers.print());
		
		//驗證預期動作是否已執行
		Mockito.verify(employeeDao,times(1)).delete(id);
		Mockito.verifyNoMoreInteractions(employeeDao);
	}
	
	//驗證刪除員工API，資料庫無此id狀況
	@Test
	public void verifyIdNotFoundRemoveEmployee() throws Exception {
		Integer id = 12;
		//刪除員工時無此id故須拋出EmptyResultDataAccessException
		Mockito.doThrow(EmptyResultDataAccessException.class).when(employeeDao).delete(id);
		
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/removeEmployee")
				.param("id",Integer.toString(id)))
				//驗證是否為404
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				//驗證是否有訊息回傳值
				.andExpect(jsonPath("$.message").exists())
				//驗證回傳訊息是否為移除成功
				.andExpect(jsonPath("$.message").value("Id is not found"))
				.andDo(MockMvcResultHandlers.print());
		
		//驗證預期動作是否已執行
		Mockito.verify(employeeDao,times(1)).delete(id);
		Mockito.verifyNoMoreInteractions(employeeDao);
	}
	
	//驗證刪除員工API，程式發生錯誤狀況
	@Test
	public void verifyIdServerErrorRemoveEmployee() throws Exception {
		Integer id = 1;
		//預期刪除員工時需拋出Exception
		Mockito.doThrow(Exception.class).when(employeeDao).delete(id);
		
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/removeEmployee")
				.param("id",Integer.toString(id)))
				//驗證是否為500
				.andExpect(MockMvcResultMatchers.status().is5xxServerError())
				//驗證是否有訊息回傳值
				.andExpect(jsonPath("$.message").exists())
				//驗證回傳訊息是否為移除成功
				.andExpect(jsonPath("$.message").value("API is error"))
				.andDo(MockMvcResultHandlers.print());
		
		//驗證預期動作是否已執行
		Mockito.verify(employeeDao,times(1)).delete(id);
		Mockito.verifyNoMoreInteractions(employeeDao);
	}
	
	//驗證查詢員工API，帶選填參數
	@Test
	public void verifyFindEmployeeByDepartmentName() throws Exception {
		Integer pageNum=1;
		
		//預期回傳值Page<T>
		List<Employee> list=new ArrayList<>();
		for(int i=1 ;i<=4; i++) {
			list.add(new Employee(i+1, null , null,null, null, null, 25));
		}
		
		Page<Employee> employeePage=new PageImpl<Employee>(list,new PageRequest(pageNum - 1, 10),4);
		
		//設定動態查詢條件
		Integer employeeAge=25;
		Specification<Employee> specification = employeeSpecification
		.queryByIdAndNameAndAgeAndDepartmentName(null, null, employeeAge, null);
		Mockito.when(employeeDao.findAll(specification, new PageRequest(pageNum - 1, 10))).thenReturn(employeePage);
		
		mockMvc.perform(
				MockMvcRequestBuilders.get("/findEmployee")
				.param("employeeAge", Integer.toString(employeeAge)))
				//驗證是否為200
				.andExpect(MockMvcResultMatchers.status().isOk())
				//驗證是否有content,totalElements,totalPages,last,size,number,first,numberOfElements,sort回傳值
				.andExpect(jsonPath("$.content").exists())
				.andExpect(jsonPath("$.totalElements").exists())
				.andExpect(jsonPath("$.totalPages").exists())
				.andExpect(jsonPath("$.last").exists())
				.andExpect(jsonPath("$.size").exists())
				.andExpect(jsonPath("$.number").exists())
				.andExpect(jsonPath("$.first").exists())
				.andExpect(jsonPath("$.numberOfElements").exists())
				//驗證content回傳值是否不為空
				.andExpect(jsonPath("$.content").isNotEmpty())
				//驗證totalPages是否只有1頁
				.andExpect(jsonPath("$.totalPages").value(1))
				//驗證totalElements是否只有4個
				.andExpect(jsonPath("$.totalElements").value(4))
				//驗證size是否為10
				.andExpect(jsonPath("$.size").value(10))
				.andDo(MockMvcResultHandlers.print());
		
		//驗證預期動作是否已執行
		Mockito.verify(employeeDao,times(1)).findAll(specification, new PageRequest(pageNum - 1, 10));
		Mockito.verifyNoMoreInteractions(employeeDao);
	}
	
	//驗證查詢員工API，帶頁數參數
	@Test
	public void verifyFindEmployeeByPageNum() throws Exception {
		Integer pageNum=2;
		
		//預期回傳值Page<T>
		List<Employee> list=new ArrayList<>();
		list.add(new Employee(11,"Dylan", null, "male", "0912345678", null, 25));
		Page<Employee> employeePage=new PageImpl<Employee>(list,new PageRequest(pageNum - 1, 10),10); 
		
		//設定動態查詢條件
		Specification<Employee> specification = employeeSpecification
		.queryByIdAndNameAndAgeAndDepartmentName(null, null, null, null);
		
		Mockito.when(employeeDao.findAll(specification, new PageRequest(pageNum - 1, 10))).thenReturn(employeePage);
		
		mockMvc.perform(
				MockMvcRequestBuilders.get("/findEmployee")
				.param("pageNum", Integer.toString(pageNum)))
				//驗證是否為200
				.andExpect(MockMvcResultMatchers.status().isOk())
				//驗證是否有content,totalElements,totalPages,last,size,number,first,numberOfElements,sort回傳值
				.andExpect(jsonPath("$.content").exists())
				.andExpect(jsonPath("$.totalElements").exists())
				.andExpect(jsonPath("$.totalPages").exists())
				.andExpect(jsonPath("$.last").exists())
				.andExpect(jsonPath("$.size").exists())
				.andExpect(jsonPath("$.number").exists())
				.andExpect(jsonPath("$.first").exists())
				.andExpect(jsonPath("$.numberOfElements").exists())
				//驗證content回傳值是否不為空
				.andExpect(jsonPath("$.content").isNotEmpty())
				//驗證totalPages是否有2頁
				.andExpect(jsonPath("$.totalPages").value(2))
				//驗證totalElements是否有11個
				.andExpect(jsonPath("$.totalElements").value(11))
				//驗證size是否為10
				.andExpect(jsonPath("$.size").value(10))
				//驗證是否為最後一頁
				.andExpect(jsonPath("$.last").value(true))
				.andDo(MockMvcResultHandlers.print());
		
		//驗證預期動作是否已執行
		Mockito.verify(employeeDao,times(1)).findAll(specification, new PageRequest(pageNum - 1, 10));
		Mockito.verifyNoMoreInteractions(employeeDao);
	}
}
