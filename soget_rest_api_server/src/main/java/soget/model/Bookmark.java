package soget.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;

public class Bookmark {
	@Id
	private String id;
	private String title;
	private String url;
	private String img_url;
	private String description;
	private String initUserId;
	private String initUserName;
	private String initUserNickName;
	private List<Follower> followers;
	private long date;
	private boolean privacy;
	private List<Comment> comments;
	private List<String> tags;
	private List<String> category;
	
	
	public String getImg_url() {
		return img_url;
	}
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getInitUserId() {
		return initUserId;
	}
	public void setInitUserId(String initUserId) {
		this.initUserId = initUserId;
	}
	
	public List<Follower> getFollowers() {
		return followers;
	}
	public void setFollowers(List<Follower> followers) {
		this.followers = followers;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public boolean isPrivacy() {
		return privacy;
	}
	public void setPrivacy(boolean privacy) {
		this.privacy = privacy;
	}
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	public List<String> getCategory() {
		return category;
	}
	public void setCategory(List<String> category) {
		this.category = category;
	}
	public String getInitUserName() {
		return initUserName;
	}
	public void setInitUserName(String initUserName) {
		this.initUserName = initUserName;
	}
	public String getInitUserNickName() {
		return initUserNickName;
	}
	public void setInitUserNickName(String initUserNickName) {
		this.initUserNickName = initUserNickName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "Bookmark [id=" + id + ", title=" + title + ", url=" + url
				+ ", img_url=" + img_url + ", description=" + description
				+ ", initUserId=" + initUserId + ", initUserName="
				+ initUserName + ", initUserNickName=" + initUserNickName
				+ ", followers=" + followers + ", date=" + date + ", privacy="
				+ privacy + ", comments=" + comments + ", tags=" + tags
				+ ", category=" + category + "]";
	}
	
	
	
	
	
	
}
