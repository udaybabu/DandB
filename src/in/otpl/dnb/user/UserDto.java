package in.otpl.dnb.user;

import in.otpl.dnb.util.ServerConstants;
import in.otpl.dnb.util.SessionConstants;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class UserDto implements Serializable{

	private static final long serialVersionUID = 1L;

	private long Id;
	private long userId;
	private int customerId;
	private int userTypeId;
	private String userType;
	private long teamId;
	private int forceSetupUpdate;
	private int phoneModelId;
	private String ptn = null ;
	private String firstName;
	private String lastName;
	private String creationTime;
	private String modificationTime;
	private String password;
	private String confirmPassword;
	private String loginName;
	private String emailAddress;
	private String employeeNumber;
	private String phoneAppVersion;
	public int offset = 0;
	public int rowcount = SessionConstants.DEFAULT_ROWCOUNT;
	private int status;
	private String designation;
	private long userCreatedBy;
	private String imei;
	private String country;
	private String name;
	private String teamName;
	private String appVersionUpdateTime;
	private String strStatus = "Inactive";
	private boolean forceSetupUpdateNull = true;
	private boolean phoneModelIdNull = true;
	private String externalId;
	private int needDownloadAssignment;
	private int timezoneId;
	private String externalUerId;
	private String leadFullName;
	private String handsetDetails ="";
	private int deleteCheckId;
	private int checkStatus;
	private String checkDeleteStatus;
	private boolean teamIdNull = true;
	private int isFirstLogin;
	private String associatedPincode;
	
	public long getId() {
		return Id;
	}
	public void setId(long id) {
		Id = id;
	}
	public long getUserId() {
		return userId;
	}
	public boolean isTeamIdNull() {
		return teamIdNull;
	}
	public void setTeamIdNull(boolean teamIdNull) {
		this.teamIdNull = teamIdNull;
	}
	public boolean isForceSetupUpdateNull() {
		return forceSetupUpdateNull;
	}
	public void setForceSetupUpdateNull(boolean forceSetupUpdateNull) {
		this.forceSetupUpdateNull = forceSetupUpdateNull;
	}
	public boolean isPhoneModelIdNull() {
		return phoneModelIdNull;
	}
	public void setPhoneModelIdNull(boolean phoneModelIdNull) {
		this.phoneModelIdNull = phoneModelIdNull;
	}
	public String getExternalId() {
		return externalId;
	}
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
	public int getNeedDownloadAssignment() {
		return needDownloadAssignment;
	}
	public void setNeedDownloadAssignment(int needDownloadAssignment) {
		this.needDownloadAssignment = needDownloadAssignment;
	}
	public int getTimezoneId() {
		return timezoneId;
	}
	public void setTimezoneId(int timezoneId) {
		this.timezoneId = timezoneId;
	}
	public String getExternalUerId() {
		return externalUerId;
	}
	public void setExternalUerId(String externalUerId) {
		this.externalUerId = externalUerId;
	}
	public String getLeadFullName() {
		return leadFullName;
	}
	public void setLeadFullName(String leadFullName) {
		this.leadFullName = leadFullName;
	}
	public String getHandsetDetails() {
		return handsetDetails;
	}
	public void setHandsetDetails(String handsetDetails) {
		this.handsetDetails = handsetDetails;
	}
	public int getDeleteCheckId() {
		return deleteCheckId;
	}
	public void setDeleteCheckId(int deleteCheckId) {
		this.deleteCheckId = deleteCheckId;
	}
	public int getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(int checkStatus) {
		this.checkStatus = checkStatus;
	}
	public String getCheckDeleteStatus() {
		return checkDeleteStatus;
	}
	public void setCheckDeleteStatus(String checkDeleteStatus) {
		this.checkDeleteStatus = checkDeleteStatus;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setStrStatus(String strStatus) {
		this.strStatus = strStatus;
	}
	public String getStrStatus() {
		return (this.status==1)?ServerConstants.STATUS_ACTIVE:ServerConstants.STATUS_INACTIVE;
	}
	public String getAppVersionUpdateTime() {
		return appVersionUpdateTime;
	}
	public void setAppVersionUpdateTime(String appVersionUpdateTime) {
		this.appVersionUpdateTime = appVersionUpdateTime;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public String getName() {
		return (this.firstName+" "+this.lastName);
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public int getUserTypeId() {
		return userTypeId;
	}
	public void setUserTypeId(int userTypeId) {
		this.userTypeId = userTypeId;
	}
	public String getUserType() {
		if(this.userTypeId == 1)
			return "Admin";
		else if(this.userTypeId == 2)
			return "Lead";
		else
			return "User";
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public long getTeamId() {
		return teamId;
	}
	public void setTeamId(long teamId) {
		this.teamId = teamId;
	}
	public int getForceSetupUpdate() {
		return forceSetupUpdate;
	}
	public void setForceSetupUpdate(int forceSetupUpdate) {
		this.forceSetupUpdate = forceSetupUpdate;
	}
	public int getPhoneModelId() {
		return phoneModelId;
	}
	public void setPhoneModelId(int phoneModelId) {
		this.phoneModelId = phoneModelId;
	}
	public String getPtn() {
		return ptn;
	}
	public void setPtn(String ptn) {
		this.ptn = ptn;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getEmployeeNumber() {
		return employeeNumber;
	}
	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}
	public String getPhoneAppVersion() {
		return phoneAppVersion;
	}
	public void setPhoneAppVersion(String phoneAppVersion) {
		this.phoneAppVersion = phoneAppVersion;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public int getIsFirstLogin() {
		return isFirstLogin;
	}
	public void setIsFirstLogin(int isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}
	public String getAssociatedPincode() {
		return associatedPincode;
	}
	public void setAssociatedPincode(String associatedPincode) {
		this.associatedPincode = associatedPincode;
	}
	public long getUserCreatedBy() {
		return userCreatedBy;
	}
	public void setUserCreatedBy(long userCreatedBy) {
		this.userCreatedBy = userCreatedBy;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
}
