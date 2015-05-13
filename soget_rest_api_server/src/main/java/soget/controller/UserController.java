package soget.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import soget.model.User;
import soget.repository.UserRepository;
import soget.security.Util;

@EnableAutoConfiguration
@RestController
@RequestMapping("/user")
public class UserController {
    
	private static int PAGE_SIZE = 10;
	
	@Autowired
	private UserRepository user_repository;
	
	@RequestMapping(value = "/login", produces = "text/plain")
	public String login() {
		System.out.println(" *** MainRestController.login");
		return "There is nothing special about login here, just use Authorization: Basic, or provide secure token.\n" +
			"For testing purposes you can use headers X-Username and X-Password instead of HTTP Basic Access Authentication.\n" +
			"THIS APPLIES TO ANY REQUEST protected by Spring Security (see filter-mapping).\n\n" +
			"Realize, please, that Authorization request (or the one with testing X-headers) must be POST, otherwise they are ignored.";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout() {
		System.out.println(" *** MainRestController.logout");
		return "Logout invalidates token on server-side. It must come as a POST request with valid X-Auth-Token, URL is configured for MyAuthenticationFilter.";
	}
	
	//Register user
	/*
	 * Return created User object or null(if duplicated userId)
	 */
	@RequestMapping(method=RequestMethod.POST ,value = "/register")
	@ResponseBody
	public User create(@RequestBody User user) throws Exception{
		
		User find_user = user_repository.findByUserId(user.getUserId());
		if(find_user!=null){
			//Duplicated user_id
			System.out.println("duplicated user_id");
			return null;
		}
		
		if(!Util.isAlphaNumeric(user.getUserId())){
			//Non alpha numeric user id 
			System.out.println("invalid user id");
			return null;
		}
		System.out.println("Register User");
		System.out.println("name: "+user.getName());
		System.out.println("user_id: "+user.getUserId());
		System.out.println("password: "+user.getPassword());
		System.out.println("email: "+user.getEmail());
		System.out.println("facebook: "+user.getFacebookProfile());
		System.out.println("invitation code: "+user.getInvitationCode());
		
		user.setFriends(new ArrayList<String>());
		user.setFriendsRequestReceived(new ArrayList<String>());
		user.setFriendsRequestSent(new ArrayList<String>());
		user.setBookmarks(new ArrayList<String>());
		user.setTrashcan(new ArrayList<String>());
		user.setInvitation(new ArrayList<String>());
		
		String decrypt = soget.security.Util.Decrypt(user.getInvitationCode(), Util.KEY);
		StringTokenizer st = new StringTokenizer(decrypt,"|");
		String hostUserId = st.nextToken();
		String invitationNumber = st.nextToken();
	
		//Make friendship between new user and host user 
		User friend = user_repository.findByUserId(hostUserId);
		User new_user = user_repository.save(user);
	
		friend.getFriends().add(new_user.getId());
		new_user.getFriends().add(friend.getId());
		
		//Delete invitation number from invitation list
		List<String> invitationList = friend.getInvitation();
		invitationList.remove(user.getInvitationCode());
		friend.setInvitation(invitationList);
		
		user_repository.save(friend);
		user_repository.save(new_user);
		
		return user;
	}
	
	//Check Duplicated userId
	/*
	 * return true if it is not duplicated userId or false
	 */
	@RequestMapping(method=RequestMethod.GET, value="/register/checkUserId/{user_id}")
	@ResponseBody
	public boolean checkUserId(@PathVariable String user_id){
		User find_user = user_repository.findByUserId(user_id);
		if(find_user!=null){
			//Duplicated user_id
			System.out.println("duplicated user_id");
			return false;
		} 
		return true;
	}
	
	//Check Invitation code
	/*
	 * Code :  {UserId}|{Invitation Code} 
	 */
	@RequestMapping(method=RequestMethod.GET, value="/register/checkInvitationCode/{code}")
	@ResponseBody
	public boolean checkInvitationCode(@PathVariable String code) throws Exception{
		 String decrypt = soget.security.Util.Decrypt(code, Util.KEY);
		 StringTokenizer st = new StringTokenizer(decrypt,"|");
		 String hostUserId = st.nextToken();
		 String invitationNumber = st.nextToken();
		 User host_user = user_repository.findByUserId(hostUserId);
		 if(host_user==null){
			 System.out.println("unvalidated invitation code");
			 return false;
		 } else {
			 if(host_user.getInvitation().contains(invitationNumber))
			 {
				 return true;
			 } else {
				 return false;
			 }
		 }
		 
	}
	
	//Password change
	@RequestMapping(method=RequestMethod.PUT, value="{user_id}")
	@ResponseBody
	public void update(@PathVariable String user_id, @RequestBody String password){
		System.out.println("Password Change");
		User mine = user_repository.findByUserId(user_id);
		mine.setPassword(password);
		user_repository.save(mine);
	}
	
	//Delete user
	@RequestMapping(method=RequestMethod.DELETE, value="{user_id}")
	@ResponseBody
	public void delete(@PathVariable String user_id){
		System.out.println("Delete User");
		User user = user_repository.findByUserId(user_id);
		user_repository.delete(user);
	}
	
	//Get all friend List
	@RequestMapping(value="/friends/{my_id}", method=RequestMethod.GET)
	@ResponseBody
	public List<User> getFriendList(@PathVariable String my_id){
		System.out.println("Show Friends");
		User mine = user_repository.findByUserId(my_id);
		List<String> frined_id_list = mine.getFriends();
		return (List<User>) user_repository.findAll(frined_id_list);
	}
	
	//Get all friend request sent list  
	@RequestMapping(value="/friends/sent/{my_id}", method=RequestMethod.GET)
	@ResponseBody
	public List<User> getFriendRequestSentList(@PathVariable String my_id){
		System.out.println("Show request sent Friends");
		User mine = user_repository.findByUserId(my_id);
		List<String> frined_id_list = mine.getFriendsRequestSent();
		List<User> friend_list = (List<User>) user_repository.findAll(frined_id_list);
	    return friend_list;
	}
	
	//Get all friend received list  
    @RequestMapping(value="/friends/receive/{my_id}", method=RequestMethod.GET)
	@ResponseBody
	public List<User> getFriendRequestReceivedList(@PathVariable String my_id){
			System.out.println("Show reqeust receive Friends");
			User mine = user_repository.findByUserId(my_id);
			List<String> frined_id_list = mine.getFriendsRequestReceived();
			List<User> friend_list = (List<User>) user_repository.findAll(frined_id_list);
		    return friend_list;
	}
	
	//find friend 
	@RequestMapping(value="/friends/find/{user_id}", method=RequestMethod.GET)
	@ResponseBody
	public User findFriend(@PathVariable String user_id){
			System.out.println("find Friend");
			return user_repository.findByUserId(user_id);
	}
	
	//Send friend request 
	@RequestMapping(value="/friends/{my_id}/{friend_id}", method = RequestMethod.POST)
	public void sendFriendRequest(@PathVariable String my_id, @PathVariable String friend_id){
		System.out.println("my_id:"+my_id);
		System.out.println("friend_id:"+friend_id);
		
		
		//Update send friend request list on my profile 
		User mine = user_repository.findByUserId(my_id);
		User friend = user_repository.findByUserId(friend_id);
		
		List<String> friendRequestSentList = mine.getFriendsRequestSent();
		if (friendRequestSentList ==null){
			friendRequestSentList = new ArrayList<String>();
		}
		friendRequestSentList.add(friend.getId());
		mine.setFriendsRequestSent(friendRequestSentList);
		
		//Update received friend request list on friend's profile 
		
		List<String> friendRequestReceivedList = friend.getFriendsRequestReceived();
		if (friendRequestReceivedList == null){
			friendRequestReceivedList = new ArrayList<String>();
		}
		friendRequestReceivedList.add(mine.getId());
		friend.setFriendsRequestReceived(friendRequestReceivedList);
		
		user_repository.save(mine);
		user_repository.save(friend);
	}
	
	//Accept friend request
	@RequestMapping(value="/friends/{my_id}/{friend_id}", method = RequestMethod.PUT)
	public void acceptFriendRequest(@PathVariable String my_id, @PathVariable String friend_id){
		User mine = user_repository.findByUserId(my_id);
		User friend = user_repository.findByUserId(friend_id);
		
		mine.getFriendsRequestReceived().remove(friend.getId());
		mine.getFriends().add(friend.getId());
				
		friend.getFriendsRequestSent().remove(mine.getId());
		friend.getFriends().add(mine.getId());
	    
	    user_repository.save(mine);
	    user_repository.save(friend);
	}
	
}
