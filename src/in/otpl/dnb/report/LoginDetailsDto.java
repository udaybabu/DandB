package in.otpl.dnb.report;

import java.io.Serializable;
import org.springframework.stereotype.Component;


@Component
public class LoginDetailsDto implements Serializable{
	private static final long serialVersionUID = 1L;

	private long id;
	private String userName;
	private String teamName;
    private int typeData =0;
	private int customerId;
	private String creationTime;
	private String modificationTime;
	private long userId;
    private long noDays = 0;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public int getTypeData() {
		return typeData;
	}
	public void setTypeData(int typeData) {
		this.typeData = typeData;
	}
		public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
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
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getNoDays() {
		return noDays;
	}
	public void setNoDays(long noDays) {
		this.noDays = noDays;
	}
	
}
