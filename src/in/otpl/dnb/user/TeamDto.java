package in.otpl.dnb.user;
import in.otpl.dnb.util.ServerConstants;
import in.otpl.dnb.util.SessionConstants;
import java.io.Serializable;
import org.springframework.stereotype.Component;

@Component
public class TeamDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private long id;
	private int customerId;
	private String name;
	private long leadId;
	private String creationTime;
	private String modificationTime;
	private String leadName;
	private String members;
	private String fromTime;
	private String toTime;
	private String leadFirstName;
	private int status = SessionConstants.STATUS_ALL;
	private String strStatus = "Inactive";
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getLeadFirstName() {
		return leadFirstName;
	}
	public String getStrStatus() {
		return (this.status==1)?ServerConstants.STATUS_ACTIVE:ServerConstants.STATUS_INACTIVE;
	}
	public void setLeadFirstName(String leadFirstName) {
		this.leadFirstName = leadFirstName;
	}
	public String getLeadLastName() {
		return leadLastName;
	}
	public void setLeadLastName(String leadLastName) {
		this.leadLastName = leadLastName;
	}
	private String leadLastName;

	public String getFromTime() {
		return fromTime;
	}
	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}
	public String getToTime() {
		return toTime;
	}
	public void setToTime(String toTime) {
		this.toTime = toTime;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getLeadId() {
		return leadId;
	}
	public void setLeadId(long leadId) {
		this.leadId = leadId;
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
	public String getLeadName() {
		return leadName;
	}
	public void setLeadName(String leadName) {
		this.leadName = leadName;
	}
	public String getMembers() {
		return members;
	}
	public void setMembers(String members) {
		this.members = members;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}