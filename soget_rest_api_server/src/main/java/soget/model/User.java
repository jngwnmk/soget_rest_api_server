package soget.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.data.annotation.Id;

public class User {
	
	@Id
	private String id;
	private String name;
	private String userId;
	private String email;
	private String password;
	private String facebookProfile;
	private List<String> friends;
	private List<String> friendsRequestRecieved;
	private List<String> friendsRequestSent;
	private List<String> bookmarks;
	private String[] roles;
	
	public User(String userId, String name, String password, String... roles){
		this.userId = userId;
		this.name = name;
		this.password = password;
		this.roles = roles;
	}
	
	
	public String[] getRoles() {
		return roles;
	}


	public void setRoles(String[] roles) {
		this.roles = roles;
	}


	public List<String> getBookmarks() {
		return bookmarks;
	}
	public void setBookmarks(List<String> bookmarks) {
		this.bookmarks = bookmarks;
	}
	public List<String> getFriends() {
		return friends;
	}
	public void setFriends(List<String> friends) {
		this.friends = friends;
	}
	public List<String> getFriendsRequestRecieved() {
		return friendsRequestRecieved;
	}
	public void setFriendsRequestRecieved(List<String> friendsRequestRecieved) {
		this.friendsRequestRecieved = friendsRequestRecieved;
	}
	public List<String> getFriendsRequestSent() {
		return friendsRequestSent;
	}
	public void setFriendsRequestSent(List<String> friendsRequestSent) {
		this.friendsRequestSent = friendsRequestSent;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFacebookProfile() {
		return facebookProfile;
	}
	public void setFacebookProfile(String facebookProfile) {
		this.facebookProfile = facebookProfile;
	}
	
	@Override
	public boolean equals(Object o){
		return this==o || o!=null & o instanceof User && Objects.equals(userId, ((User)o).userId);
	}
	
	@Override
	public int hashCode(){
		return Objects.hashCode(userId);
	}
	
	@Override
	public String toString(){
		return "User{"+ "userId="+userId+","+"name="+name+",password="+password+",roles="+Arrays.toString(roles)+"}";
	}
	
	
	
}
