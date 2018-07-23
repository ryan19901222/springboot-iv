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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import junit.framework.TestCase;
import www.com.springboot_iv.dao.DepartmentDAO;
import www.com.springboot_iv.dto.CreateDepartmentDTO;
import www.com.springboot_iv.dto.UpdateDepartmentDTO;
import www.com.springboot_iv.entity.Department;
import www.com.springboot_iv.entity.Employee;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DepartmentApiTest extends TestCase {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private DepartmentDAO departmentDao;

	
	@Before
	public void before() throws Exception {
		MockitoAnnotations.initMocks(this);
		Mockito.reset(departmentDao);
	}

	//驗證新增部門API
	@Transactional
	@Test
	public void verifySuccessCreateDepartment() throws Exception {
		//設定請求參數值
		CreateDepartmentDTO createDepartmentDTO = new CreateDepartmentDTO();
		createDepartmentDTO.setName("法務部");
		
		//設定新增部門後預期回傳值
		Department department = new Department();
		BeanUtils.copyProperties(createDepartmentDTO, department);
		Department returnDepartment = new Department();
		returnDepartment.setId(5);
		returnDepartment.setName("法務部");
		Mockito.when(departmentDao.save(department)).thenReturn(returnDepartment);
		
		this.mockMvc.perform(MockMvcRequestBuilders.post("/createDepartment").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(new ObjectMapper().writeValueAsString(createDepartmentDTO)))
				// 驗證是否為200
				.andExpect(MockMvcResultMatchers.status().isOk())
				// 驗證是否有id,name回傳值
				.andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.name").exists())
				// 驗證新增後回傳值id是否為5,name是否為法務部
				.andExpect(jsonPath("$.id").value(5)).andExpect(jsonPath("$.name").value("法務部"))
				.andDo(MockMvcResultHandlers.print());
		
		//驗證預期動作是否已執行
		Mockito.verify(departmentDao,times(1)).save(department);
		Mockito.verifyNoMoreInteractions(departmentDao);
	}

	//驗證更新部門資料API成功狀況
	@Transactional
	@Test
	public void verifySuccessUpdateDepartment() throws Exception {
		//設定請求參數值
		UpdateDepartmentDTO updateDepartmentDTO = new UpdateDepartmentDTO();
		updateDepartmentDTO.setId(3);
		updateDepartmentDTO.setName("法務部");
		
		//設定更新部門後預期回傳值
		Department department = new Department();
		BeanUtils.copyProperties(updateDepartmentDTO, department);
		Department findReturnDepartment = new Department(3,"設計部");
		Department UpdateDepartment = new Department(3,"法務部");
		Mockito.when(departmentDao.findOne(updateDepartmentDTO.getId())).thenReturn(findReturnDepartment);
		Mockito.when(departmentDao.save(department)).thenReturn(UpdateDepartment);
		
		mockMvc.perform(MockMvcRequestBuilders.put("/updateDepartment").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(new ObjectMapper().writeValueAsString(updateDepartmentDTO)))
				// 驗證是否為200
				.andExpect(MockMvcResultMatchers.status().isOk())
				// 驗證是否有id,name回傳值
				.andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.name").exists())
				// 驗證更新後回傳值id是否為3,name是否變更為法務部
				.andExpect(jsonPath("$.id").value(3)).andExpect(jsonPath("$.name").value("法務部"))
				.andDo(MockMvcResultHandlers.print());

		//驗證預期動作是否已執行
		Mockito.verify(departmentDao,times(1)).findOne(updateDepartmentDTO.getId());
		Mockito.verify(departmentDao,times(1)).save(department);
		Mockito.verifyNoMoreInteractions(departmentDao);
	}

	//驗證更新部門資料API，資料庫無此id狀況
	@Transactional
	@Test
	public void verifyIdNotFoundUpdateDepartment() throws Exception {
		//設定請求參數值
		UpdateDepartmentDTO updateDepartmentDTO = new UpdateDepartmentDTO();
		updateDepartmentDTO.setId(5);
		updateDepartmentDTO.setName("法務部");
		
		//設定更新部門查無此id預期回傳值
		Mockito.when(departmentDao.findOne(updateDepartmentDTO.getId())).thenReturn(null);
		
		mockMvc.perform(MockMvcRequestBuilders.put("/updateDepartment").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(new ObjectMapper().writeValueAsString(updateDepartmentDTO)))
				// 驗證是否為404
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				// 驗證是否有錯誤訊息回傳值
				.andExpect(jsonPath("$.message").exists())
				// 驗證錯誤訊息是否為id找不到
				.andExpect(jsonPath("$.message").value("Id is not found")).andDo(MockMvcResultHandlers.print());
		
		//驗證預期動作是否已執行
		Mockito.verify(departmentDao,times(1)).findOne(updateDepartmentDTO.getId());
		Mockito.verifyNoMoreInteractions(departmentDao);
	}

	
	//驗證刪除部門API，成功狀況
	
	// 驗證刪除部門API，成功狀況
	@Test
	public void verifySuccessRemoveDepartment() throws Exception {
		Integer id = 4;
		String name="行銷部";
		//設定刪除部門預期回傳值
		Department returnDepartment = new Department();
		returnDepartment.setId(id);
		returnDepartment.setName(name);
		Mockito.when(departmentDao.findOne(id)).thenReturn(returnDepartment);
		Mockito.doNothing().when(departmentDao).delete(returnDepartment);
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/removeDepartment")
				.param("id" ,Integer.toString(id)))
				// 驗證是否為200
				.andExpect(MockMvcResultMatchers.status().isOk())
				// 驗證是否有訊息回傳值
				.andExpect(jsonPath("$.message").exists())
				// 驗證回傳訊息是否為移除成功
				.andExpect(jsonPath("$.message").value("Remove success"))
				.andDo(MockMvcResultHandlers.print());
		
		//驗證預期動作是否已執行
		Mockito.verify(departmentDao).findOne(id);
		Mockito.verify(departmentDao).delete(returnDepartment);
		Mockito.verifyNoMoreInteractions(departmentDao);
	}
	
	//驗證刪除部門API，底下有員工狀況

	// 驗證刪除部門API，資料庫無此id狀況
	@Test
	public void verifyIdNotFoundDeleteDepartment() throws Exception {
		Integer id = 5;
		//設定刪除部門查詢資料庫無此id預期回傳值
		Mockito.when(departmentDao.findOne(id)).thenReturn(null);
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/removeDepartment").param("id", Integer.toString(id)))
				// 驗證是否為404
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				// 驗證是否有訊息回傳值
				.andExpect(jsonPath("$.message").exists())
				// 驗證回傳訊息是否為移除成功
				.andExpect(jsonPath("$.message").value("Id is not found")).andDo(MockMvcResultHandlers.print());
		
		//驗證預期動作是否已執行
		Mockito.verify(departmentDao).findOne(id);
		Mockito.verifyNoMoreInteractions(departmentDao);
	}

	// 驗證刪除部門API，部門底下還有員工資料狀況
	@Test
	public void verifyHasEmployeesRemoveDepartment() throws Exception {
		Integer id = 3;
		String name="設計部";
		//設定刪除部門底下有員工狀況預期回傳值
		Department returnDepartment = new Department();
		returnDepartment.setId(id);
		returnDepartment.setName(name);
		List<Employee> employees = new ArrayList<>();
		employees.add(new Employee());
		returnDepartment.setEmployees(employees);
		Mockito.when(departmentDao.findOne(id)).thenReturn(returnDepartment);
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/removeDepartment")
				.param("id", Integer.toString(id)))
				// 驗證是否為500
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				// 驗證是否有訊息回傳值
				.andExpect(jsonPath("$.message").exists())
				// 驗證回傳訊息是否為移除成功
				.andExpect(jsonPath("$.message").value("This department has employees"))
				.andDo(MockMvcResultHandlers.print());
		
		//驗證預期動作是否已執行
		Mockito.verify(departmentDao).findOne(id);
		Mockito.verifyNoMoreInteractions(departmentDao);
	}

	//驗證刪除部門API，程式發生錯誤狀況
	@Test
	public void verifyIdServerErrorRemoveDepartment() throws Exception {
		Integer id = 1;
		//預期刪除部門時需拋出Exception
		Mockito.doThrow(Exception.class).when(departmentDao).findOne(id);
			
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/removeDepartment")
				.param("id",Integer.toString(id)))
				//驗證是否為500
				.andExpect(MockMvcResultMatchers.status().is5xxServerError())
				//驗證是否有訊息回傳值
				.andExpect(jsonPath("$.message").exists())
				//驗證回傳訊息是否為移除成功
				.andExpect(jsonPath("$.message").value("API is error"))
				.andDo(MockMvcResultHandlers.print());
			
		//驗證預期動作是否已執行
		Mockito.verify(departmentDao,times(1)).findOne(id);
		Mockito.verifyNoMoreInteractions(departmentDao);
	}
			
}
