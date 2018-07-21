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
public class EmployeeApiTest extends TestCase {

	@Autowired
	private WebApplicationContext wac;
	
	private MockMvc mockMvc;
	
	@Before
	public void before() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	//測試新增員工API
	@Transactional
	@Test
	public void verifyCreateEmployee() throws Exception {
		String json ="{"
						+"\"name\" : \"jay\","
						+"\"department\" : {"
							+"\"id\":1,"
							+"\"name\":\"人資部\""
						+"},"
						+"\"gender\" : \"male\","
						+"\"phone\" : \"0987654332\","
						+"\"address\" : \"南投市\","
						+"\"age\" : 24"
					+"}";
		mockMvc.perform(
				MockMvcRequestBuilders.post("/createEmployee")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(json))
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
				.andExpect(jsonPath("$.createTime").exists())
				.andExpect(jsonPath("$.updateTime").exists())
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
	}
	
	//測試更新員工資料API，成功狀況
	@Transactional
	@Test
	public void verifyUpdateEmployee() throws Exception {
		String json ="{"
						+"\"id\" : 1,"
						+"\"name\" : \"dylan\","
						+"\"department\" : {"
							+"\"id\":1,"
							+"\"name\":\"人資部\""
						+"},"
						+"\"gender\" : \"male\","
						+"\"phone\" : \"0912345678\","
						+"\"address\" : \"台南市\","
						+"\"age\" : 25"
					+"}";
		mockMvc.perform(
				MockMvcRequestBuilders.put("/updateEmployee")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(json))
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
				.andExpect(jsonPath("$.createTime").exists())
				.andExpect(jsonPath("$.updateTime").exists())
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
	}
	
	//驗證更新員工資料API，資料庫無此id狀況
	@Transactional
	@Test
	public void verifyIdNotFoundUpdateEmployee() throws Exception {
		String json ="{"
						+"\"id\" : 12,"
						+"\"name\" : \"dylan\","
						+"\"department\" : {"
							+"\"id\":1,"
							+"\"name\":\"人資部\""
						+"},"
						+"\"gender\" : \"male\","
						+"\"phone\" : \"0912345678\","
						+"\"address\" : \"台南市\","
						+"\"age\" : 25"
					+"}";
		mockMvc.perform(
				MockMvcRequestBuilders.put("/updateEmployee")
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
	
	//驗證更新員工資料API，沒帶id狀況
	@Transactional
	@Test
	public void verifyNotHasIdUpdateEmployee() throws Exception {
		String json ="{"
						+"\"name\" : \"dylan\","
						+"\"department\" : {"
							+"\"id\":1,"
							+"\"name\":\"人資部\""
						+"},"
						+"\"gender\" : \"male\","
						+"\"phone\" : \"0912345678\","
						+"\"address\" : \"台南市\","
						+"\"age\" : 25"
					+"}";
		mockMvc.perform(
				MockMvcRequestBuilders.put("/updateEmployee")
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
		
	//驗證刪除員工API，成功狀況
	@Transactional
	@Test
	public void verifyDeleteEmployee() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/removeEmployee")
				.param("id", "1"))
				//驗證是否為200
				.andExpect(MockMvcResultMatchers.status().isOk())
				//驗證是否有訊息回傳值
				.andExpect(jsonPath("$.message").exists())
				//驗證回傳訊息是否為移除成功
				.andExpect(jsonPath("$.message").value("Remove success"))
				.andDo(MockMvcResultHandlers.print());
	}
	
	//驗證刪除員工API，資料庫無此id狀況
	@Test
	public void verifyIdNotFoundDeleteEmployee() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/removeEmployee")
				.param("id", "12"))
				//驗證是否為404
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				//驗證是否有訊息回傳值
				.andExpect(jsonPath("$.message").exists())
				//驗證回傳訊息是否為移除成功
				.andExpect(jsonPath("$.message").value("Id is not found"))
				.andDo(MockMvcResultHandlers.print());
	}
	
	
	
	//驗證查詢員工API，帶選填參數
	@Test
	public void verifyFindEmployeeByDepartmentName() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.get("/findEmployee")
				.param("departmentName", "人資部"))
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
	}
	
	//驗證查詢員工API，帶頁數參數
	@Test
	public void verifyFindEmployeeByPageNum() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.get("/findEmployee")
				.param("pageNum", "2"))
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
	}
}
