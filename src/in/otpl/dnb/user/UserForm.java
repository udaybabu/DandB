package in.otpl.dnb.user;

import in.otpl.dnb.util.SessionConstants;

import java.io.Serializable;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class UserForm implements Serializable{

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
	private int status = SessionConstants.STATUS_ALL;
	private String designation;
	private int isFirstLogin;
	private String associatedPincode;
	private long userCreatedBy;
	private String imei;
	private String country;
	private String keyValue = "";
	private int validateType;
	private String oldPassword;
	private String flag;
	private MultipartFile file;
	private String name = "";
	
	public long getId() {
		return Id;
	}
	public void setId(long id) {
		Id = id;
	}
	public long getUserId() {
		return userId;
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
		return userType;
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
	public String getName() {
		return this.firstName+" "+this.lastName;
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public String getKeyValue() {
		return keyValue;
	}
	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}
	public int getValidateType() {
		return validateType;
	}
	public void setValidateType(int validateType) {
		this.validateType = validateType;
	}
	
}
