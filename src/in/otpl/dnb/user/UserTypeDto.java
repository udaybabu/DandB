package in.otpl.dnb.user;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class UserTypeDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String description;
	private String creationTime;
	private String modificationTime;
	private int isAdmin;
	private boolean isAdminNull = true;
	private int isTeamLead;
	private boolean isTeamLeadNull = true;
	private int isFieldUser;
	private boolean isFieldUserNull = true;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}
	public String getModificationTime() {
		return modificationTime;
	}
	public void setModificationTime(String modificationTime) {
		this.modificationTime = modificationTime;
	}
	public int getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(int isAdmin) {
		this.isAdmin = isAdmin;
	}
	public boolean isAdminNull() {
		return isAdminNull;
	}
	public void setAdminNull(boolean isAdminNull) {
		this.isAdminNull = isAdminNull;
	}
	public int getIsTeamLead() {
		return isTeamLead;
	}
	public void setIsTeamLead(int isTeamLead) {
		this.isTeamLead = isTeamLead;
	}
	public boolean isTeamLeadNull() {
		return isTeamLeadNull;
	}
	public void setTeamLeadNull(boolean isTeamLeadNull) {
		this.isTeamLeadNull = isTeamLeadNull;
	}
	public int getIsFieldUser() {
		return isFieldUser;
	}
	public void setIsFieldUser(int isFieldUser) {
		this.isFieldUser = isFieldUser;
	}
	public boolean isFieldUserNull() {
		return isFieldUserNull;
	}
	public void setFieldUserNull(boolean isFieldUserNull) {
		this.isFieldUserNull = isFieldUserNull;
	}

}
