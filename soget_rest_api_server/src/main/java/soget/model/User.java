package soget.model;

import java.util.List;

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
	
	
}
