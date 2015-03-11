package soget.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import soget.model.Bookmark;
import soget.model.Comment;
import soget.model.User;
import soget.repository.BookmarkRepository;
import soget.repository.UserRepository;

@EnableAutoConfiguration
@RestController
@RequestMapping("/bookmark")
public class BookmarkController {
	
	private static int PAGE_SIZE = 10;
	
	@Autowired
	private BookmarkRepository bookmark_repository;
	
	@Autowired
	private UserRepository user_repository;
	
	
	//Get My bookmark list
	@RequestMapping(method=RequestMethod.GET, value="{my_id}")
	@ResponseBody
	public List<Bookmark> getMyBookmarkList(@PathVariable String my_id){
		System.out.println("getMyBookmarkList");
		//get bookmark id list from my profile
		User mine = user_repository.findByUserId(my_id);
		List<String> bookmark_ids = mine.getBookmarks();
		List<Bookmark> bookmark_list = (List<Bookmark>) bookmark_repository.findAll(bookmark_ids);
		return bookmark_list;
	}
	
	//Get friend's recent bookmark list
	@RequestMapping(method=RequestMethod.GET, value="/home/friends/{my_id}/{page_num}")
	@ResponseBody
	public Page<Bookmark> getFriendsBookmarkList(@PathVariable String my_id, @PathVariable int page_num){
			System.out.println("getFriendsBookmarkList");
			User mine = user_repository.findByUserId(my_id);
			List<String> friends = mine.getFriends();
			System.out.println(friends.toString());
			return bookmark_repository.findByInitUserIdIn(friends, new PageRequest(page_num,PAGE_SIZE, new Sort(new Order(Direction.DESC,"date"))));
	}
	
	//Get all user's recent bookmark list
	@RequestMapping(method=RequestMethod.GET, value="/home/{page_num}")
	@ResponseBody
	public Page<Bookmark> getAllBookmarkList(@PathVariable int page_num){
		System.out.println("getAllBookmarkList");
		return bookmark_repository.findAll(new PageRequest(page_num,PAGE_SIZE, new Sort(new Order(Direction.DESC,"date"))));
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/home")
	@ResponseBody
	public Page<Bookmark> getAllBookmarkList(){
		System.out.println("getAllBookmarkList");
		return bookmark_repository.findAll(new PageRequest(0,PAGE_SIZE, new Sort(new Order(Direction.DESC,"date"))));
	}
	
	//Get {user_id}'s bookmark list
	//Same as getMyBookmarkList()
	
	//Insert {bookmark_id} to my bookmark list
	@RequestMapping(method=RequestMethod.PUT, value="{user_id}/{bookmark_id}")
	@ResponseBody
	public void updateBookmarkList(@PathVariable String user_id, @PathVariable String bookmark_id){
		System.out.println("get bookmark");
		User mine = user_repository.findByUserId(user_id);
		mine.getBookmarks().add(bookmark_id);
		
		Bookmark bookmark = bookmark_repository.findOne(bookmark_id);
		bookmark.getFollowers().add(mine.getId());
		
		user_repository.save(mine);
		bookmark_repository.save(bookmark);
		
	}
	
	//Insert new bookmark into my list
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public Bookmark createBookmark(@RequestBody Bookmark bookmark){
		System.out.println("Insert new bookmark");
		System.out.println("title: "+bookmark.getTitle());
		System.out.println("url: "+bookmark.getUrl());
		User user_id = user_repository.findByUserId(bookmark.getInitUserId());
		System.out.println("init_user_id: "+user_id.getId());
		bookmark.setInitUserId(user_id.getId());
		System.out.println("privacy: "+bookmark.isPrivacy());
		
		//Create new bookmark
		bookmark.setFollowers(new ArrayList<String>());
		bookmark.setDate(new Date());
		bookmark.setComments(new ArrayList<Comment>());
		bookmark.setTags(new ArrayList<String>());
		bookmark.setCategory(new ArrayList<String>());
		
		Bookmark saved_bookmark = bookmark_repository.save(bookmark);
		
		//update user bookmark list
		User mine = user_repository.findOne(bookmark.getInitUserId());
		mine.getBookmarks().add(saved_bookmark.getId());
		user_repository.save(mine);		
				
		return saved_bookmark;

	}

}
