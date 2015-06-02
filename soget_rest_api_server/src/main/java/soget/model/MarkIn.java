package soget.model;

import java.util.List;

import org.springframework.data.annotation.Id;

public class MarkIn {
	@Id
	private String id;
	private String bookmarkId;
	private String userKeyId;
	private String userName;
	private String userId;
	private List<String> tags;
	private long date;
	private boolean privacy;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBookmarkId() {
		return bookmarkId;
	}
	public void setBookmarkId(String bookmarkId) {
		this.bookmarkId = bookmarkId;
	}
	public String getUserKeyId() {
		return userKeyId;
	}
	public void setUserKeyId(String userKeyId) {
		this.userKeyId = userKeyId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	public boolean isPrivacy() {
		return privacy;
	}
	public void setPrivacy(boolean privacy) {
		this.privacy = privacy;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	@Override
	public String toString() {
		return "MarkIn [id=" + id + ", bookmarkId=" + bookmarkId
				+ ", userKeyId=" + userKeyId + ", userName=" + userName
				+ ", userId=" + userId + ", tags=" + tags + ", date=" + date
				+ ", privacy=" + privacy + "]";
	}
	
	
	
}
