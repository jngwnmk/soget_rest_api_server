package soget.controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
import soget.model.Follower;
import soget.model.Invitation;
import soget.model.MarkIn;
import soget.model.User;
import soget.repository.BookmarkRepository;
import soget.repository.InvitationRepository;
import soget.repository.MarkInRepository;
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
	@Autowired
	private InvitationRepository invitation_repository;
	@Autowired
	private MarkInRepository markin_repository;
	
	//GET
	//1. getMyBookmarkList
	//2. getFriendBookmarkList
	//3. getFriendsBookmarkListWithDate
	//4. getAllBookmarkList
	//5. getComment
	
	
	//Get My bookmark list
	@RequestMapping(method=RequestMethod.GET, value="/{my_id}/{page_num}")
	@ResponseBody
	//public Page<Bookmark> getMyBookmarkList(@PathVariable String my_id, @PathVariable int page_num){
	public List<Bookmark> getMyBookmarkList(@PathVariable String my_id, @PathVariable int page_num){
		System.out.println("getMyBookmarkList");
		//get bookmark id list from my profile
		User mine = user_repository.findByUserId(my_id);
		//List<String> bookmark_ids = mine.getBookmarks();
		//System.out.println("bookmark ids: "+bookmark_ids.toString());
		//return bookmark_repository.findByIdIn(bookmark_ids, new PageRequest(page_num,PAGE_SIZE, new Sort(new Order(Direction.DESC,"date"))));
		
		Page<MarkIn> markin = markin_repository.findByUserKeyId(mine.getId(), new PageRequest(page_num,PAGE_SIZE, new Sort(new Order(Direction.DESC,"date"))));
		List<String> bookmark_ids = new ArrayList<String>();
		for(int i = 0 ; i < markin.getContent().size() ; ++i){
			bookmark_ids.add(markin.getContent().get(i).getBookmarkId());
		}
		return bookmark_repository.findByIdIn(bookmark_ids);
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
	//TODO: discover에 보이는 방법 변경 필요
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
			Bookmark temp = bookmark_repository.findOne(bookmark);
			if(temp!=null){
				exclusivelist.add(temp.getUrl());
			}
		}
		
		for(String trash : my_trashcan){
			Bookmark temp = bookmark_repository.findOne(trash);
			if(temp!=null){
				exclusivelist.add(temp.getUrl());
			}
		}
		//return bookmark_repository.findByInitUserIdInAndUrlNotInAndDateLessThan(friends, exclusivelist, date ,new PageRequest(page_num,PAGE_SIZE, new Sort(new Order(Direction.DESC,"date"))));
		return bookmark_repository.findByInitUserIdInAndUrlNotInAndDateLessThanAndPrivacyIsFalse(friends, exclusivelist, date ,new PageRequest(page_num,PAGE_SIZE, new Sort(new Order(Direction.DESC,"date"))));
		
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
	
	//get comments of {bookmark_id}
	@RequestMapping(method=RequestMethod.GET, value="/comment/{bookmark_id}")
	@ResponseBody
	public ArrayList<Comment> getComment(@PathVariable String bookmark_id){
		System.out.println("add comment");
		Bookmark bookmark = bookmark_repository.findOne(bookmark_id);
		ArrayList<Comment> comments = (ArrayList<Comment>)bookmark.getComments();
		return comments;
	}
	
	//PUT
	//1. updateBookmarkList
	//2. updateTrashcanList
	
	//Insert {bookmark_id} to my bookmark list
	//{bookmark_id}의 followers 에 {user_id}를 추가
	//bookmark정보를 가지고 새롭게 bookmark 생성이 아니라.....
	//following 하는 개념으로 변경 되었음.
	//이미 Markin한 URL이면 return null, otherwise return new Bookmark 
	@RequestMapping(method=RequestMethod.PUT, value="{user_id}/{bookmark_id}")
	@ResponseBody
	public Bookmark updateBookmarkList(@PathVariable String user_id, @PathVariable String bookmark_id, @RequestBody Bookmark bookmark){
		System.out.println("add bookmark from discover");
		User me = user_repository.findByUserId(user_id);
		
		//////////1.Check whether the URL have already Markin////////////
		if(hasAlreadyMarkin(me.getBookmarks(), bookmark)){
			 return null;
		}
		/////////////////////////////////////////////////////////////////
				
		///////////2. 만약 첫번째 MarkIn 이면, Invitation code생성////////////
		int bookmark_cnt = 0;
		if(me.getBookmarks()==null){
			bookmark_cnt = 0;
		} else {
			bookmark_cnt = me.getBookmarks().size();
		}
		
		if(bookmark_cnt==0){
			me.setInvitation(makeInvitationList(me.getUserId()));
		}
		///////////////////////////////////////////////////////////////////
				
		////////////3. 기존 Ref Bookmark 에 Follower에 추가///////////////////
		Bookmark ref_bookmark = bookmark_repository.findOne(bookmark_id);
		Follower follower = new Follower();
		follower.setPrivacy(bookmark.isPrivacy());
		if(bookmark.getTags()==null){
			follower.setTags(new ArrayList<String>());
		} else {
			follower.setTags((ArrayList<String>)bookmark.getTags());
		}
		follower.setUserKeyId(me.getId());
		follower.setUserId(me.getUserId());
		follower.setUserName(me.getName());
		ref_bookmark.getFollowers().add(follower);
		bookmark_repository.save(ref_bookmark);
		///////////////////////////////////////////////////////////////////
		
		/////////////4. 나의 bookmark리스트에 해당 bookmark_id 추가//////////////
		me.getBookmarks().add(bookmark_id);
		user_repository.save(me);
		///////////////////////////////////////////////////////////////////
		
		return ref_bookmark;
		
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
	
	//POST
	//1. createBookmark
	//2. addComment
	
	//Insert new bookmark into my list
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public Bookmark createBookmark(@RequestBody Bookmark bookmark){
		System.out.println("add bookmark manually");
		User me = user_repository.findByUserId(bookmark.getInitUserId());
		//////////1.Check whether the URL have already Markin////////////
		if(hasAlreadyMarkin(me.getBookmarks(), bookmark)){
			 return null;
		}
		/////////////////////////////////////////////////////////////////
				
		///////////2. 만약 첫번째 MarkIn 이면, Invitation code생성////////////
		int bookmark_cnt = 0;
		if(me.getBookmarks()==null){
			bookmark_cnt = 0;
		} else {
			bookmark_cnt = me.getBookmarks().size();
		}
		
		if(bookmark_cnt==0){
			me.setInvitation(makeInvitationList(me.getUserId()));
		}
		///////////////////////////////////////////////////////////////////
		
		/////////////3.  새로운 bookmark생성//////////////////////////////////
		ArrayList<String> webInfo = getBookmarkWebInfo(bookmark.getUrl());
		
		Bookmark new_bookmark = new Bookmark();
		new_bookmark.setTitle(webInfo.get(0));
		new_bookmark.setUrl(bookmark.getUrl());
		new_bookmark.setImg_url(webInfo.get(1));
		new_bookmark.setDescription(webInfo.get(2));
		new_bookmark.setInitUserId(me.getId());
		new_bookmark.setInitUserName(me.getName());
		new_bookmark.setInitUserNickName(me.getUserId());
		new_bookmark.setFollowers(new ArrayList<Follower>());
		new_bookmark.setDate(System.currentTimeMillis());
		new_bookmark.setPrivacy(bookmark.isPrivacy());
		new_bookmark.setComments(new ArrayList<Comment>());
		if(bookmark.getTags()==null){
			new_bookmark.setTags(new ArrayList<String>());
		} else {
			new_bookmark.setTags(bookmark.getTags());
		}
		new_bookmark.setCategory(new ArrayList<String>());
		
		Bookmark saved_bookmark = bookmark_repository.save(new_bookmark);
		/////////////4. 나의 bookmark리스트에 해당 bookmark_id 추가//////////////
		me.getBookmarks().add(saved_bookmark.getId());
		user_repository.save(me);		
		///////////////////////////////////////////////////////////////////
		
		/////////////5. markin repository에 추가/////////////////////////////
		MarkIn markin = new MarkIn();
		markin.setBookmarkId(saved_bookmark.getId());
		markin.setDate(saved_bookmark.getDate());
		markin.setPrivacy(saved_bookmark.isPrivacy());
		markin.setTags(saved_bookmark.getTags());
		markin.setUserKeyId(saved_bookmark.getInitUserId());
		markin.setUserId(saved_bookmark.getInitUserName());
		markin.setUserName(saved_bookmark.getInitUserNickName());
		markin_repository.save(markin);
		///////////////////////////////////////////////////////////////////
	
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
	
	private ArrayList<String> getBookmarkWebInfo(String url){

		ArrayList<String> webInfo = new ArrayList<String>();
		Document document;
		try {
			//document = Jsoup.connect(url).get();
			//Set the userAgent to Mozilla to get the information of "waitbutwhy.com"
			//Please check other urls that have been working well. 
			document = Jsoup.connect(url).userAgent("Mozilla").get();
			String title = "";
		    Element img_url_element = null;
		    Element description_element = null;
		    String img_url = "";
		    String description = "";
		    try{
			    title = document.title();
			    img_url_element = getMetaTag(document, "og:image");
			    if(img_url_element==null){
			    	img_url_element = document.select("img").first();
			    	if(img_url_element!=null){
			    		img_url =  img_url_element.absUrl("src");
			    	}
				} else {
					img_url =  img_url_element.absUrl("content");
				}
			    
			    description_element = getMetaTag(document, "og:description");
			    if(description_element==null){
			    	description_element = getMetaTag(document, "description");
			    	if(description_element!=null){
			    		description = description_element.attr("content");
					}
			    } else {
			       description = description_element.attr("content");
				    
			    }
		    } catch(Exception e){
		    	e.printStackTrace();
		    }
		    
		    System.out.println("title: "+title);
		    webInfo.add(title);
		    System.out.println("Img_URL: "+img_url);
		    webInfo.add(img_url);
			System.out.println("Description: "+description);
		    webInfo.add(description);
		    
		    System.out.println(webInfo.toString());
		    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    return webInfo;

	}
	
	private Element getMetaTag(Document document, String attr) {
			Elements elements = document.select("meta[name=" + attr + "]");
			for (Element element : elements) {
				final String s = element.attr("content");
				if (s != null) return element;
			}
			elements = document.select("meta[property=" + attr + "]");
			for (Element element : elements) {
				final String s = element.attr("content");
				if (s != null) return element;
			}
			elements = document.select("meta[itemprop=" + attr + "]");
			for (Element element : elements) {
				final String s = element.attr("content");
				if (s != null) return element;
			}
			
			return null;
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
		System.out.println("updateInvitationCode()");
		User me = user_repository.findByUserId(user_id);
		ArrayList<String> invitations = new ArrayList<String>();
		BigInteger bigInteger = new BigInteger("999999");
        BigInteger bigInteger1 = bigInteger.subtract(new BigInteger("100000"));
        int count = 10;
        while(count>0){
        	String invitationNum = soget.security.Util.randomBigInteger(bigInteger1).toString();
        	System.out.println("InvitationNum:"+invitationNum);
        	if(invitation_repository.findByInvitationNum(invitationNum)==null){
        		Invitation invitation = new Invitation();
            	invitation.setInvitationNum(invitationNum);
            	invitation.setOwnerUserId(user_id);
            	invitation_repository.save(invitation);
            	invitations.add(invitationNum);
            	count--;
            }
        }
        System.out.println(me.getInvitation());
        if(me.getInvitation()!=null){
        	me.getInvitation().clear();
        	me.getInvitation().addAll(invitations);
        }
        user_repository.save(me);
		return invitations;
	}
	


}
