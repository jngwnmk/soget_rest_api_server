package soget.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import soget.repository.BookmarkRepository;
import soget.repository.UserRepository;

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
	
	
}
