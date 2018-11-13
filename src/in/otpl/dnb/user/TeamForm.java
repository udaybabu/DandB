package in.otpl.dnb.user;
import in.otpl.dnb.util.SessionConstants;
import java.io.Serializable;
import org.springframework.stereotype.Component;

@Component
public class TeamForm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private long teamId;
	private String teamName;
	private long teamLeadId;
	private long[] selected;
	private long[] available;
	private int offset = 0;
	private int rowcount = SessionConstants.DEFAULT_ROWCOUNT;
	private int status = SessionConstants.STATUS_ALL;
	private int customerId;
	private String name;
	private String leadName;
	private String memberName;
	
	public long getTeamId() {
		return teamId;
	}
	public void setTeamId(long teamId) {
		this.teamId = teamId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public long getTeamLeadId() {
		return teamLeadId;
	}
	public void setTeamLeadId(long teamLeadId) {
		this.teamLeadId = teamLeadId;
	}
	public long[] getSelected() {
		return selected;
	}
	public void setSelected(long[] selected) {
		this.selected = selected;
	}
	public long[] getAvailable() {
		return available;
	}
	public void setAvailable(long[] available) {
		this.available = available;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public int getRowcount() {
		return rowcount;
	}
	public void setRowcount(int rowcount) {
		this.rowcount = rowcount;
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
	public String getLeadName() {
		return leadName;
	}
	public void setLeadName(String leadName) {
		this.leadName = leadName;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
}