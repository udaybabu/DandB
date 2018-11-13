package in.otpl.dnb.report;


import java.io.Serializable;
import in.otpl.dnb.util.SessionConstants;


public class LoginDetailsForm implements Serializable{

	private static final long serialVersionUID = 1L;

	private long userId ;
	private String hourFrom;
	private String minFrom;
	private String hourTo;
	private String minTo;
	private String format = " ";
	private String key ="";
	private int offset = 0;
	private int rowcount = SessionConstants.DEFAULT_ROWCOUNT;
    private int typeData =0;
    private int notLogged;
	private int loggedUsers;
	private int htmlUsers;
	private int mobileUsers;
	private int androidUsers;
	private int iosUsers;
	private int userTypeId;
    
	public int getUserTypeId() {
		return userTypeId;
	}
	public void setUserTypeId(int userTypeId) {
		this.userTypeId = userTypeId;
	}
	public int getNotLogged() {
		return notLogged;
	}
	public void setNotLogged(int notLogged) {
		this.notLogged = notLogged;
	}
	public int getLoggedUsers() {
		return loggedUsers;
	}
	public void setLoggedUsers(int loggedUsers) {
		this.loggedUsers = loggedUsers;
	}
	public int getHtmlUsers() {
		return htmlUsers;
	}
	public void setHtmlUsers(int htmlUsers) {
		this.htmlUsers = htmlUsers;
	}
	public int getMobileUsers() {
		return mobileUsers;
	}
	public void setMobileUsers(int mobileUsers) {
		this.mobileUsers = mobileUsers;
	}
	public int getAndroidUsers() {
		return androidUsers;
	}
	public void setAndroidUsers(int androidUsers) {
		this.androidUsers = androidUsers;
	}
	public int getIosUsers() {
		return iosUsers;
	}
	public void setIosUsers(int iosUsers) {
		this.iosUsers = iosUsers;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getHourFrom() {
		return hourFrom;
	}
	public void setHourFrom(String hourFrom) {
		this.hourFrom = hourFrom;
	}
	public String getMinFrom() {
		return minFrom;
	}
	public void setMinFrom(String minFrom) {
		this.minFrom = minFrom;
	}
	public String getHourTo() {
		return hourTo;
	}
	public void setHourTo(String hourTo) {
		this.hourTo = hourTo;
	}
	public String getMinTo() {
		return minTo;
	}
	public void setMinTo(String minTo) {
		this.minTo = minTo;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
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
	public int getTypeData() {
		return typeData;
	}
	public void setTypeData(int typeData) {
		this.typeData = typeData;
	}
    
}
