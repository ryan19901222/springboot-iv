package www.com.springboot_iv;

import org.junit.Assert;
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
import www.com.springboot_iv.dao.EmployeeDAO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeApiTest extends TestCase {

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private EmployeeDAO employeeDao;
	
	private MockMvc mockMvc;
	
	@Before
	public void before() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	//測試新增員工API
	@Transactional
	@Test
	public void createEmployeeTest() throws Exception {
		long olderCount = employeeDao.count();
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
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print());
		long newCount = employeeDao.count();
		//以原資料長度比較新資料長度，判斷此筆資料是否新增
		Assert.assertNotEquals(olderCount, newCount);
	}
	
	//測試更新員工資料API
	@Transactional
	@Test
	public void updateEmployeeTest() throws Exception {
		String olderEmployeePhone = employeeDao.findOne(1).getPhone();
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
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print());
		String newEmployeePhone = employeeDao.findOne(1).getPhone();
		//以原資料phone比較新資料phone，判斷此筆資料phone是否更新
		Assert.assertNotEquals(olderEmployeePhone, newEmployeePhone);
		
	}
	
	//測試刪除員工API
	@Transactional
	@Test
	public void deleteEmployeeTest() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/deleteEmployee")
				.param("id", "1"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print());
		//判斷員編1號資料是否已刪除，如已刪除應回傳null
		Assert.assertNull(employeeDao.findOne(1));
	}
	
	//測試查詢員工API，帶選填參數
	@Test
	public void findEmployeeTest() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.get("/findEmployee")
				.param("employeeId", "1")
				.param("employeeAge", "22"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print());
	}
	
	//測試查詢員工API，帶頁數參數
	@Test
	public void findEmployeeByPageNumTest() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.get("/findEmployee")
				.param("pageNum", "2"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print());
	}
}
