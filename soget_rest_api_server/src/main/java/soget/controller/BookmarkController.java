package soget.controller;

import java.math.BigInteger;
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
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import soget.model.Bookmark;
import soget.model.Comment;
import soget.model.User;
import soget.repository.BookmarkRepository;
import soget.repository.UserRepository;
import soget.security.Util;

@EnableAutoConfiguration
@RestController
@RequestMapping("/bookmark")
public class BookmarkController {
	
	private static int PAGE_SIZE = 10;
	
	@Autowired
	private BookmarkRepository bookmark_repository;
	
	@Autowired
	private UserRepository user_repository;
	
	@Autowired private MongoOperations mongoOps;
	
	//Get My bookmark list
	@RequestMapping(method=RequestMethod.GET, value="/{my_id}/{page_num}")
	@ResponseBody
	public Page<Bookmark> getMyBookmarkList(@PathVariable String my_id, @PathVariable int page_num){
		System.out.println("getMyBookmarkList");
		//get bookmark id list from my profile
		User mine = user_repository.findByUserId(my_id);
		List<String> bookmark_ids = mine.getBookmarks();
		System.out.println("bookmark ids: "+bookmark_ids.toString());
		return bookmark_repository.findByIdIn(bookmark_ids, new PageRequest(page_num,PAGE_SIZE, new Sort(new Order(Direction.DESC,"date"))));
	}
	
	//Get {user_id}'s bookmark list
	//Same as getMyBookmarkList(), but not shows the privacy is true
	@RequestMapping(method=RequestMethod.GET, value="/friend/{friend_id}/{page_num}")
	@ResponseBody
	public Page<Bookmark> getFriendBookmarkList(@PathVariable String friend_id, @PathVariable int page_num){
		System.out.println("getFriendBookmarkList");
		User friend = user_repository.findByUserId(friend_id);
		List<String> bookmark_ids = friend.getBookmarks();
		System.out.println("bookmark ids: "+bookmark_ids.toString());
		return bookmark_repository.findByIdIn(bookmark_ids, new PageRequest(page_num,PAGE_SIZE, new Sort(new Order(Direction.DESC,"date"))));
	}
	
	//Get friend's recent bookmark list
	@RequestMapping(method=RequestMethod.GET, value="/home/friends/{my_id}/{date}/{page_num}")
	@ResponseBody
	public Page<Bookmark> getFriendsBookmarkListWithDate(@PathVariable String my_id, @PathVariable long date,@PathVariable int page_num){
		System.out.println("getFriendsBookmarkList");
		User mine = user_repository.findByUserId(my_id);
		List<String> friends = mine.getFriends();
		System.out.println(friends.toString());
		List<String> my_bookmark = mine.getBookmarks();
		List<String> my_trashcan = mine.getTrashcan();
		List<String> exclusivelist = new ArrayList<String>();
		for(String bookmark : my_bookmark){
			exclusivelist.add(bookmark);
		}
		
		for(String trash : my_trashcan){
			exclusivelist.add(trash);
		}
		
		return bookmark_repository.findByInitUserIdInAndIdNotInAndDateLessThan(friends, exclusivelist, date ,new PageRequest(page_num,PAGE_SIZE, new Sort(new Order(Direction.DESC,"date"))));
		//return bookmark_repository.findByInitUserIdInAndDateLessThan(friends, date ,new PageRequest(page_num,PAGE_SIZE, new Sort(new Order(Direction.DESC,"date"))));
		//return bookmark_repository.findByDateLessThan(new Date(date*1000) ,new PageRequest(page_num,PAGE_SIZE, new Sort(new Order(Direction.DESC,"date"))));
		//return bookmark_repository.findByDateLessThanAndInitUserIdIn(date,friends ,new PageRequest(page_num,PAGE_SIZE, new Sort(new Order(Direction.DESC,"date"))));
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
	
	
	private boolean hasAlreadyMarkin(List<String> bookmark_ids, Bookmark bookmark){
		List<Bookmark> bookmark_list = (List<Bookmark>) bookmark_repository.findAll(bookmark_ids);
		for(int i = 0 ; i < bookmark_list.size() ; ++i){
			if(bookmark_list.get(i).getUrl().equals(bookmark.getUrl())){
				return true;
			}
		}	
		return false;
	}
	
	private ArrayList<String> makeInvitationList(String user_id){
		ArrayList<String> invitations = new ArrayList<String>();
		for(int i = 0 ; i < 10 ; ++i){
			BigInteger randomInteger = soget.security.Util.nextRandomInteger();
			String encrypt;
			try {
				encrypt = soget.security.Util.Encrypt(user_id+"|"+randomInteger, Util.KEY);
				invitations.add(encrypt);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return invitations;
	}
	
	//Insert {bookmark_id} to my bookmark list
	@RequestMapping(method=RequestMethod.PUT, value="{user_id}/{bookmark_id}")
	@ResponseBody
	public Bookmark updateBookmarkList(@PathVariable String user_id, @PathVariable String bookmark_id){
		System.out.println("add bookmark");
		User me = user_repository.findByUserId(user_id);
		Bookmark bookmark = bookmark_repository.findOne(bookmark_id);
		
		
		int bookmark_cnt = 0;
		if(me.getBookmarks()==null){
			bookmark_cnt = 0;
		} else {
			bookmark_cnt = me.getBookmarks().size();
		}
		
		//Check whether have already markin
		if(hasAlreadyMarkin(me.getBookmarks(), bookmark)){
			return null;
		}
		
		//TODO
		//해당 bookmark_id를 보고 정보를 긁어온 다음, 새로운 Bookmark를 생성하는 방식으로 변경해야 함.
		//Follower에도 추가하자.
		
		//1. 기존 Bookmark 의 Follower에 추가 
		bookmark.getFollowers().add(me.getId());
		bookmark_repository.save(bookmark);
		
		//2. 새로운 bookmark생성
		Bookmark new_bookmark = new Bookmark();
		new_bookmark.setTitle(bookmark.getTitle());
		new_bookmark.setUrl(bookmark.getUrl());
		new_bookmark.setInitUserId(me.getId());
		new_bookmark.setInitUserName(me.getName());
		new_bookmark.setInitUserNickName(me.getUserId());
		new_bookmark.setFollowers(new ArrayList<String>());
		new_bookmark.setDate(System.currentTimeMillis());
		new_bookmark.setComments(new ArrayList<Comment>());
		new_bookmark.setTags(new ArrayList<String>());
		new_bookmark.setCategory(new ArrayList<String>());
		
		//3. bookmark 추가 
		Bookmark saved_bookmark = bookmark_repository.save(new_bookmark);
		
		//4. update user info list
		me.getBookmarks().add(saved_bookmark.getId());
		
		//5. 만약 첫번째 Get 이면, Invitation code생성
		if(bookmark_cnt==0){
			me.setInvitation(makeInvitationList(me.getUserId()));
		}
		
		user_repository.save(me);
		
		return saved_bookmark;
		
	}
	
	//Insert {bookmark_id} to my trashcan list
	@RequestMapping(method=RequestMethod.PUT, value="/trashcan/{user_id}/{bookmark_id}")
	@ResponseBody
	public void updateTrashcanList(@PathVariable String user_id, @PathVariable String bookmark_id){
		System.out.println("add trash");
		User me = user_repository.findByUserId(user_id);
		me.getTrashcan().add(bookmark_id);
		user_repository.save(me);
	}
	
	//Insert new bookmark into my list
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public Bookmark createBookmark(@RequestBody Bookmark bookmark){
		
		User me = user_repository.findByUserId(bookmark.getInitUserId());
		
		int bookmark_cnt = 0;
		if(me.getBookmarks()==null){
			bookmark_cnt = 0;
		} else {
			bookmark_cnt = me.getBookmarks().size();
		}
		
		if(hasAlreadyMarkin(me.getBookmarks(), bookmark)){
			return null;
		}
		
		bookmark.setInitUserId(me.getId());
		bookmark.setInitUserName(me.getName());
		bookmark.setInitUserNickName(me.getUserId());
		
		System.out.println("Insert new bookmark");
		System.out.println("title: "+bookmark.getTitle());
		System.out.println("url: "+bookmark.getUrl());
		System.out.println("init_user_id: "+me.getId());
		System.out.println("init_user_nick: "+me.getUserId());
		System.out.println("init_user_name: "+me.getName());
		System.out.println("privacy: "+bookmark.isPrivacy());
		
		//Create new bookmark
		bookmark.setFollowers(new ArrayList<String>());
		bookmark.setDate(System.currentTimeMillis());
		bookmark.setComments(new ArrayList<Comment>());
		
		if(bookmark.getTags()==null){
			bookmark.setTags(new ArrayList<String>());
		} else {
			bookmark.setTags(bookmark.getTags());
		}
		
		bookmark.setCategory(new ArrayList<String>());
		
		Bookmark saved_bookmark = bookmark_repository.save(bookmark);
		
		//update user info
		if(bookmark_cnt==0){
			me.setInvitation(makeInvitationList(me.getUserId()));
		}
		me.getBookmarks().add(saved_bookmark.getId());
		
		user_repository.save(me);		
		return saved_bookmark;

	}
	
	//comment.userId comments on {bookmark_id} 
	@RequestMapping(method=RequestMethod.POST, value="/comment/{bookmark_id}")
	@ResponseBody
	public Comment addComment(@PathVariable String bookmark_id, @RequestBody Comment comment){
		System.out.println("add comment");
		Bookmark bookmark = bookmark_repository.findOne(bookmark_id);
		User me = user_repository.findByUserId(comment.getUserId());
		comment.setDate(System.currentTimeMillis());
		comment.setUserKeyId(me.getId());
		comment.setUserName(me.getName());
		
		bookmark.getComments().add(comment);
		Bookmark bookmark_w_new_comment = bookmark_repository.save(bookmark);
		int comment_size = bookmark_w_new_comment.getComments().size();
		if(comment_size==0){
			return null;
		}
		return bookmark_w_new_comment.getComments().get(comment_size-1);
	}
	
	//get comments of {bookmark_id}
	@RequestMapping(method=RequestMethod.GET, value="/comment/{bookmark_id}")
	@ResponseBody
	public ArrayList<Comment> getComment(@PathVariable String bookmark_id){
		System.out.println("add comment");
		Bookmark bookmark = bookmark_repository.findOne(bookmark_id);
		ArrayList<Comment> comments = (ArrayList<Comment>)bookmark.getComments();
		return comments;
		
	}

}
