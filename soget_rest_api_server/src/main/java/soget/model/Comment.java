package soget.model;

import java.util.Date;

import org.springframework.data.annotation.Id;

public class Comment implements Comparable<Comment>{
	
	private long date;
	private String userKeyId;        //Unique Object Id
	private String userName;      		//User name
	private String userId;  			//User id in Markin
	private String content;
	
	
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	@Override
	public int compareTo(Comment o) {
		if(o.date<=this.date){
			return -1;
		} else {
			return 1;
		}
	}
	
	
	
}
