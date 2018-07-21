package www.com.springboot_iv;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import junit.framework.TestCase;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DepartmentApiTest extends TestCase {

	@Autowired
	private WebApplicationContext wac;
	
	private MockMvc mockMvc;
	
	@Before
	public void before() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	//驗證新增部門API
	@Transactional
	@Test
	public void verifyCreateDepartment() throws Exception {
		String json = "{\"name\" : \"法務部\"}";
		mockMvc.perform(
				MockMvcRequestBuilders.post("/createDepartment")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(json))
				//驗證是否為200
				.andExpect(MockMvcResultMatchers.status().isOk())
				//驗證是否有id,name回傳值
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").exists())
				//驗證新增後回傳值id是否為5,name是否為法務部
				.andExpect(jsonPath("$.id").value(5))
				.andExpect(jsonPath("$.name").value("法務部"))
				.andDo(MockMvcResultHandlers.print());
	}
	
	//驗證更新部門資料API成功狀況
	@Transactional
	@Test
	public void verifyUpdateDepartment() throws Exception {
		String json = "{ \"id\" : 3 , \"name\" : \"法務部\"}";
		mockMvc.perform(
				MockMvcRequestBuilders.put("/updateDepartment")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(json))
				//驗證是否為200
				.andExpect(MockMvcResultMatchers.status().isOk())
				//驗證是否有id,name回傳值
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").exists())
				//驗證更新後回傳值id是否為3,name是否變更為法務部
				.andExpect(jsonPath("$.id").value(3))
				.andExpect(jsonPath("$.name").value("法務部"))
				.andDo(MockMvcResultHandlers.print());
	}
	
	//驗證更新部門資料API，資料庫無此id狀況
	@Transactional
	@Test
	public void verifyIdNotFoundUpdateDepartment() throws Exception {
		String json = "{ \"id\" : 5 , \"name\" : \"法務部\"}";
		mockMvc.perform(
				MockMvcRequestBuilders.put("/updateDepartment")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(json))
				//驗證是否為404
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				//驗證是否有錯誤訊息回傳值
				.andExpect(jsonPath("$.message").exists())
				//驗證錯誤訊息是否為id找不到
				.andExpect(jsonPath("$.message").value("Id is not found"))
				.andDo(MockMvcResultHandlers.print());
	}
	
	//驗證更新部門資料API，沒帶id狀況
	@Transactional
	@Test
	public void verifyNotHasIdUpdateDepartment() throws Exception {
		String json = "{ \"name\" : \"法務部\"}";
		mockMvc.perform(
				MockMvcRequestBuilders.put("/updateDepartment")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(json))
				//驗證是否為400
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				//驗證是否有錯誤訊息回傳值
				.andExpect(jsonPath("$.message").exists())
				//驗證錯誤訊息是否為id找不到
				.andExpect(jsonPath("$.message").value("Id is not nullable"))
				.andDo(MockMvcResultHandlers.print());
	}
	
	//驗證刪除部門API，成功狀況
	@Test
	public void verifyDeleteDepartment() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/removeDepartment")
				.param("id", "4"))
				//驗證是否為200
				.andExpect(MockMvcResultMatchers.status().isOk())
				//驗證是否有訊息回傳值
				.andExpect(jsonPath("$.message").exists())
				//驗證回傳訊息是否為移除成功
				.andExpect(jsonPath("$.message").value("Remove success"))
				.andDo(MockMvcResultHandlers.print());
	}
	
	//驗證刪除部門API，資料庫無此id狀況
	@Test
	public void verifyIdNotFoundDeleteDepartment() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/removeDepartment")
				.param("id", "5"))
				//驗證是否為404
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				//驗證是否有訊息回傳值
				.andExpect(jsonPath("$.message").exists())
				//驗證回傳訊息是否為移除成功
				.andExpect(jsonPath("$.message").value("Id is not found"))
				.andDo(MockMvcResultHandlers.print());
	}
	
	//驗證刪除部門API，部門底下還有員工資料狀況
	@Test
	public void verifyHasEmployeesDeleteDepartment() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/removeDepartment")
				.param("id", "1"))
				//驗證是否為500
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				//驗證是否有訊息回傳值
				.andExpect(jsonPath("$.message").exists())
				//驗證回傳訊息是否為移除成功
				.andExpect(jsonPath("$.message").value("This department has employees"))
				.andDo(MockMvcResultHandlers.print());
	}
	
	

}
