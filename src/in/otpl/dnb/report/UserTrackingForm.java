package in.otpl.dnb.report;

import in.otpl.dnb.util.SessionConstants;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class UserTrackingForm implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int userId ;
	private String dateFrom;
	private String dateTo;
    public String format = "";
    public String key ="";
	public int offset = 0;
	public int rowcount = SessionConstants.DEFAULT_ROWCOUNT;
    private int includeCellSite;
    private int customerId;
    private String userList;
	private String appVersion;
	private String bccStatus = "";
	
	public String getBccStatus() {
		return bccStatus;
	}
	public void setBccStatus(String bccStatus) {
		this.bccStatus = bccStatus;
	}
	public String getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	public String getUserList() {
		return userList;
	}
	public void setUserList(String userList) {
		this.userList = userList;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getDateFrom() {
		return dateFrom;
	}
	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}
	public String getDateTo() {
		return dateTo;
	}
	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
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
	public int getIncludeCellSite() {
		return includeCellSite;
	}
	public void setIncludeCellSite(int includeCellSite) {
		this.includeCellSite = includeCellSite;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
}