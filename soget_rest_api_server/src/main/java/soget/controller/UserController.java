package soget.controller;

import java.util.ArrayList;
import java.util.List;

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

@EnableAutoConfiguration
@RestController
@RequestMapping("/user")
public class UserController {
    
	@Autowired
	private UserRepository user_repository;
	
	
	//Register user
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public User create(@RequestBody User user){
		System.out.println("Register User");
		System.out.println("name: "+user.getName());
		System.out.println("user_id: "+user.getUserId());
		System.out.println("password: "+user.getPassword());
		System.out.println("email: "+user.getEmail());
		System.out.println("facebook: "+user.getFacebookProfile());
		user.setFriends(new ArrayList<String>());
		user.setFriendsRequestRecieved(new ArrayList<String>());
		user.setFriendsRequestSent(new ArrayList<String>());
		user_repository.save(user);
		return user;
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
	public List<String> getFriendList(@PathVariable String my_id){
		System.out.println("Show Friends");
		User mine = user_repository.findByUserId(my_id);
	    return mine.getFriends();
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
		List<String> friendRequestSentList = mine.getFriendsRequestSent();
		if (friendRequestSentList ==null){
			friendRequestSentList = new ArrayList<String>();
		}
		friendRequestSentList.add(friend_id);
		mine.setFriendsRequestSent(friendRequestSentList);
		
		//Update received friend request list on friend's profile 
		User friend = user_repository.findByUserId(friend_id);
		List<String> friendRequestReceivedList = friend.getFriendsRequestRecieved();
		if (friendRequestReceivedList == null){
			friendRequestReceivedList = new ArrayList<String>();
		}
		friendRequestReceivedList.add(my_id);
		friend.setFriendsRequestRecieved(friendRequestReceivedList);
		
		user_repository.save(mine);
		user_repository.save(friend);
	}
	
	//Accept friend request
	@RequestMapping(value="/friends/{my_id}/{friend_id}", method = RequestMethod.PUT)
	public void acceptFriendRequest(@PathVariable String my_id, @PathVariable String friend_id){
		User mine = user_repository.findByUserId(my_id);
		mine.getFriendsRequestRecieved().remove(friend_id);
		mine.getFriends().add(friend_id);
		
		User friend = user_repository.findByUserId(friend_id);
		friend.getFriendsRequestSent().remove(my_id);
		friend.getFriends().add(my_id);
	    
	    user_repository.save(mine);
	    user_repository.save(friend);
	}
	
}
