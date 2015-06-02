package soget.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
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
import soget.model.Bookmark;
import soget.model.User;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ControllerTest {
	
	private MockMvc mockMvc;
	
	@Autowired
	UserController userController;
	
	@Autowired
	BookmarkController bookmarkController;
	
	
	@Before
	public void initMockMvc(){
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true);
		mockMvc = MockMvcBuilders.standaloneSetup(userController,bookmarkController).addFilter(filter).build();
	}
		
	//Register User 
	@Test
	public void a_createUserTest() throws Exception {
		createUser("test", "test", "1234", "test@gmail.com", "test@gmail.com","5C2SDf/Ael5T39Mi8zoqSOkVZH6ZlHlvxNr0dLSC3UInFrXL57bIifllaEzXUuh0");
		createUser("정원묵", "jngwnmk", "1234", "jngwnmk@gmail.com", "jngwnmk@gmail.com","AAhGZeOsYhirzeNyy5wEP8/bYcUWRBaJ/4A/qWqw41HrYMFMlO2QA9X0ksjS1QL3");
		createUser("정우성", "woosung", "1234", "woosung@gmail.com", "woosung@gmail.com","rN2o6ja7VQx0H4iXHSWVImUqV7OcjIDEUyWSow9KNzZOa+GMvOEbaBakQQevM1zP");
		createUser("신민아", "mina", "1234", "mina@gmail.com", "mina@gmail.com","7fIwFDosooRDbDxmptCqBC4eMY8L7FoPaSWfFIeLKQ6W+sznCRMKOQIkPQ5R792O");
		createUser("전지현", "jihyeon", "1234", "jihyeon@gmail.com", "jihyeon@gmail.com","9EhVRbzD9RnoEiZ6eYvokP596MPFi1mgs/m0/iCujwLFm5hBmJ+/B/9ssJjodmrL");
	}
	
	//Change password
	@Test
	public void b_changeUserPasswordTest() throws Exception {
		MockHttpServletRequestBuilder requestBuilder = 
				MockMvcRequestBuilders.put("/user/test")
				.content("5678")
				.accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andDo(print()).andExpect(status().isOk());
	}

	//Send friend request
	@Test
	public void c_sendFriendRequestTest() throws Exception {
		
		//jngwnmk -> mina
		MockHttpServletRequestBuilder requestBuilder = 
				MockMvcRequestBuilders.post("/user/friends/jngwnmk/mina")
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder).andDo(print()).andExpect(status().isOk());	
		
		//jngwnmk -> jihyeon
		requestBuilder = 
				MockMvcRequestBuilders.post("/user/friends/jngwnmk/jihyeon")
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder).andDo(print()).andExpect(status().isOk());	
		
	}
	
	@Test
	public void d_accepFriendRequestTest() throws Exception {
		MockHttpServletRequestBuilder requestBuilder = 
				MockMvcRequestBuilders.put("/user/friends/mina/jngwnmk")
				.accept(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder).andDo(print()).andExpect(status().isOk());
		
		requestBuilder = 
				MockMvcRequestBuilders.put("/user/friends/jihyeon/jngwnmk")
				.accept(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder).andDo(print()).andExpect(status().isOk());
	}
	
	//Find friend list
	@Test
	public void e_getFriendListTest() throws Exception {
		MockHttpServletRequestBuilder requestBuilder = 
				MockMvcRequestBuilders.get("/user/friends/jngwnmk").accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void f_addBookmark() throws Exception{
		createBookmark("test bookmark1", "google.com", "jngwnmk", false);
		//createBookmark("test bookmark2", "google.com", "mina", false);
		//createBookmark("test bookmark3", "google.com", "jngwnmk", false);
		//createBookmark("test bookmark4", "google.com", "jihyeon", false);
		//createBookmark("test bookmark5", "google.com", "mina", false);
		//createBookmark("test bookmark6", "google.com", "woosung", false);
		//createBookmark("test bookmark7", "google.com", "woosung", false);
		
	}
	
	@Test
	public void g_getFriendsRecentBookmark() throws Exception {
		MockHttpServletRequestBuilder requestBuilder = 
				MockMvcRequestBuilders.get("/bookmark/home/friends/jngwnmk/0").accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andDo(print())
		.andExpect(status().isOk());
	}
	
	private void createUser(String name, String user_id, String password, String email, String facebook, String invitationCode) throws Exception {
	    ObjectMapper mapper = new ObjectMapper();
		
		User user = new User();
		user.setUserId(user_id);
		user.setName(name);
		user.setPassword(password);
		user.setEmail(email);
		user.setFacebookProfile(facebook);
		user.setInvitationCode(invitationCode);
		String test = mapper.writeValueAsString(user);
		System.out.println(test);
		MockHttpServletRequestBuilder requestBuilder = 
				MockMvcRequestBuilders.post("/user/register")
				.content(test)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder).andDo(print()).andExpect(status().isOk());	
	}
	
	private void deleteUser(String user_id) throws Exception {
		MockHttpServletRequestBuilder requestBuilder = 
				MockMvcRequestBuilders.delete("/user/"+user_id).accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andDo(print()).andExpect(status().isOk());
	}
	
	private void createBookmark(String title, String url, String initUserId, boolean privacy) throws Exception{
	    ObjectMapper mapper = new ObjectMapper();
		
		Bookmark bookmark = new Bookmark();
		bookmark.setTitle(title);
		bookmark.setUrl(url);
		bookmark.setInitUserId(initUserId);
		bookmark.setPrivacy(privacy);
		String test = mapper.writeValueAsString(bookmark);
		System.out.println(test);
		MockHttpServletRequestBuilder requestBuilder = 
					MockMvcRequestBuilders.post("/bookmark")
					.content(test)
					.contentType(MediaType.APPLICATION_JSON);
			
		mockMvc.perform(requestBuilder).andDo(print()).andExpect(status().isOk());	
	}
	
}
