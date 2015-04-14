package soget.controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import soget.model.User;
import soget.repository.BookmarkRepository;
import soget.repository.UserRepository;
import soget.security.Util;

@EnableAutoConfiguration
@RestController
@RequestMapping("/manage")
public class ManageController {
	@Autowired
	private BookmarkRepository bookmark_repository;
	
	@Autowired
	private UserRepository user_repository;
	
	//Delete All user
	@RequestMapping(method=RequestMethod.DELETE, value="/user")
	@ResponseBody
	public void deleteUser(){
			System.out.println("Delete All User");
			user_repository.deleteAll();
	}
	
	//Delete All bookmark
	@RequestMapping(method=RequestMethod.DELETE, value="/bookmark")
	@ResponseBody
	public void deleteBookmark(){
			System.out.println("Delete All Bookmark");
			bookmark_repository.deleteAll();
	}
	
	//Make Admin User
	@RequestMapping(method=RequestMethod.POST , value="/admin")
	@ResponseBody
	public User makeAdmin(@RequestBody User user) throws Exception{
		
		User find_user = user_repository.findByUserId(user.getUserId());
		if(find_user!=null){
			//Duplicated user_id
			System.out.println("duplicated user_id");
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
		
		ArrayList<String> invitations = new ArrayList<String>();
		for(int i = 0 ; i < 10 ; ++i){
			BigInteger randomInteger = soget.security.Util.nextRandomInteger();
			String encrypt = soget.security.Util.Encrypt(user.getUserId()+"|"+randomInteger, Util.KEY);
			invitations.add(encrypt);
		}
		user.setInvitation(invitations);
			
		user_repository.save(user);
		
		return user;
	}
	
	
}
