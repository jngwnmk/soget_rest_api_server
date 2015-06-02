package soget.model;

import org.springframework.data.annotation.Id;

public class Invitation {
	@Id
	private String id;
	private String invitationNum;
	private String ownerUserId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInvitationNum() {
		return invitationNum;
	}
	public void setInvitationNum(String invitationNum) {
		this.invitationNum = invitationNum;
	}
	public String getOwnerUserId() {
		return ownerUserId;
	}
	public void setOwnerUserId(String ownerUserId) {
		this.ownerUserId = ownerUserId;
	}
	
	
	
}
