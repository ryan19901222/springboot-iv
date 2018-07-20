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
import www.com.springboot_iv.dao.DepartmentDAO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DepartmentApiTest extends TestCase {

	@Autowired
	private WebApplicationContext wac;
	
	@Autowired
	private DepartmentDAO departmentDao;
	
	private MockMvc mockMvc;
	
	@Before
	public void before() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	//測試新增部門API
	@Transactional
	@Test
	public void createDepartmentTest() throws Exception {
		long olderCount = departmentDao.count();
		
		String json = "{\"name\" : \"法務部\"}";
		mockMvc.perform(
				MockMvcRequestBuilders.post("/createDepartment")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(json))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print());
		long newCount = departmentDao.count();
		//以原資料長度比較新資料長度，判斷此筆資料是否新增
		Assert.assertNotEquals(olderCount, newCount);
	}
	
	//測試更新部門資料API
	@Transactional
	@Test
	public void updateDepartmentTest() throws Exception {
		String olderDepartmentName = departmentDao.findOne(3).getName();
		String json = "{ \"id\" : 3 , \"name\" : \"法務部\"}";
		mockMvc.perform(
				MockMvcRequestBuilders.put("/updateDepartment")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(json))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print());
		String newDepartmentName = departmentDao.findOne(3).getName();
		//以原資料name比較新資料name，判斷此筆資料name是否更新
		Assert.assertNotEquals(olderDepartmentName, newDepartmentName);
	}
	
	//測試刪除部門API
	@Test
	public void deleteDepartmentTest() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/deleteDepartment")
				.param("id", "4"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print());
		//判斷部門id 4號資料是否已刪除，如已刪除應回傳null
		Assert.assertNull(departmentDao.findOne(4));
	}

}
