package soget.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import soget.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ManageControllerTest {

	private MockMvc mockMvc;
	
	@Autowired
	ManageController manageController;
	
	
	@Before
	public void initMockMvc(){
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true);
		mockMvc = MockMvcBuilders.standaloneSetup(manageController).addFilter(filter).build();
	}
	
	//Delete all user
		@Test
		public void a_deleteAllUserTest() throws Exception {
			MockHttpServletRequestBuilder requestBuilder = 
					MockMvcRequestBuilders.delete("/manage/user").accept(MediaType.APPLICATION_JSON);
			mockMvc.perform(requestBuilder).andDo(print()).andExpect(status().isOk());
		}
			
		//Delete all bookmark
		@Test
		public void a_delteAllBookmarkTest() throws Exception {
			MockHttpServletRequestBuilder requestBuilder = 
					MockMvcRequestBuilders.delete("/manage/bookmark").accept(MediaType.APPLICATION_JSON);
			mockMvc.perform(requestBuilder).andDo(print()).andExpect(status().isOk());
		}
}