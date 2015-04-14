package soget.model;

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
	private String invitationCode;
	private List<String> friends;
	private List<String> friendsRequestReceived;
	private List<String> friendsRequestSent;
	private List<String> bookmarks;
	private List<String> invitation;
	
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
	
	public List<String> getFriendsRequestReceived() {
		return friendsRequestReceived;
	}
	public void setFriendsRequestReceived(List<String> friendsRequestReceived) {
		this.friendsRequestReceived = friendsRequestReceived;
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
	
	public List<String> getInvitation() {
		return invitation;
	}
	public void setInvitation(List<String> invitation) {
		this.invitation = invitation;
	}
	public String getInvitationCode() {
		return invitationCode;
	}
	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
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
		return "User{"+ "userId="+userId+","+"name="+name+",password="+password+"}";
	}
	
	
	
}
