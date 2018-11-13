package in.otpl.dnb.enquiryPool;

import java.io.Serializable;

public class EnquiryDto implements Serializable{

	private static final long serialVersionUID = 1L;

	private long id;
	private long assignmentGroupId;
	private int customerId;
	private long workflowId;
	private String text;
	private String externalId;
	private int timezoneId;
	private String scheduledStartTime;
	private String scheduledEndTime;
	private String modificationTime;
	private int status;
	private String data;
	private long workflowInstanceId;
	private String downloadTime;
	private int assignedByUserId;
	private Long userId;
	private String assignmentName;
	private String userName;
	private int downloaded;
	private String statusName;
	private int typeId;
	private int expiryTime;
	private int landmarkId = 0;
	private String landmarkName = "";
	private String dbTime;
	private String lattitude;
	private String longitude;

	/*For Assignment Pool changes for D&B*/
	private long assinmentPoolId=0;
	private String enquiryNo="";
	private int caseId=0;
	private String caseType="";
	private String leadEmpName="";
	private String createdDate="";
	private String dateOfSiteVisit="";
	private String dateOfSubmission="";
	private String entityCompanyName="";
	private String entityAddress="";
	private String contactPersonName="";
	private String contactNumber="";
	private String customerEmailAddress="";
	private String customerCRNNumber="";
	private String corporateName="";
	private int isProcessed=0;
	private int caseStatus = 0;
	private String assignmentStartTime="";
	private String dateFrom="";
	private String userType="";
	private long dnbMasterId=0;
	private long dnbMasterDataId = 0;
	private int isDataDistributed = 0;
	private String submissionTime="";
	private String enquiryDetails="";
	private String caseStatusVal="";
	private String city="";
	private String state="";
	private String pincode="";
	private String webAddress="";
	private String worflowRemarks="";
	private long asphId = 0;
	private long aspId = 0;
	private String eqnuiryReceivedTime = "";
	private long enquiryDetailsId = 0;
	private String qfStatus = "";
	private int isDeleted = 0;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getAssignmentGroupId() {
		return assignmentGroupId;
	}
	public void setAssignmentGroupId(long assignmentGroupId) {
		this.assignmentGroupId = assignmentGroupId;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public long getWorkflowId() {
		return workflowId;
	}
	public void setWorkflowId(long workflowId) {
		this.workflowId = workflowId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getExternalId() {
		return externalId;
	}
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
	public int getTimezoneId() {
		return timezoneId;
	}
	public void setTimezoneId(int timezoneId) {
		this.timezoneId = timezoneId;
	}
	public String getScheduledStartTime() {
		return scheduledStartTime;
	}
	public void setScheduledStartTime(String scheduledStartTime) {
		this.scheduledStartTime = scheduledStartTime;
	}
	public String getScheduledEndTime() {
		return scheduledEndTime;
	}
	public void setScheduledEndTime(String scheduledEndTime) {
		this.scheduledEndTime = scheduledEndTime;
	}
	public String getModificationTime() {
		return modificationTime;
	}
	public void setModificationTime(String modificationTime) {
		this.modificationTime = modificationTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public long getWorkflowInstanceId() {
		return workflowInstanceId;
	}
	public void setWorkflowInstanceId(long workflowInstanceId) {
		this.workflowInstanceId = workflowInstanceId;
	}
	public String getDownloadTime() {
		return downloadTime;
	}
	public void setDownloadTime(String downloadTime) {
		this.downloadTime = downloadTime;
	}
	public int getAssignedByUserId() {
		return assignedByUserId;
	}
	public void setAssignedByUserId(int assignedByUserId) {
		this.assignedByUserId = assignedByUserId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getAssignmentName() {
		return assignmentName;
	}
	public void setAssignmentName(String assignmentName) {
		this.assignmentName = assignmentName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getDownloaded() {
		return downloaded;
	}
	public void setDownloaded(int downloaded) {
		this.downloaded = downloaded;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public int getExpiryTime() {
		return expiryTime;
	}
	public void setExpiryTime(int expiryTime) {
		this.expiryTime = expiryTime;
	}
	public int getLandmarkId() {
		return landmarkId;
	}
	public void setLandmarkId(int landmarkId) {
		this.landmarkId = landmarkId;
	}
	public String getLandmarkName() {
		return landmarkName;
	}
	public void setLandmarkName(String landmarkName) {
		this.landmarkName = landmarkName;
	}
	public String getDbTime() {
		return dbTime;
	}
	public void setDbTime(String dbTime) {
		this.dbTime = dbTime;
	}
	public String getLattitude() {
		return lattitude;
	}
	public void setLattitude(String lattitude) {
		this.lattitude = lattitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public long getAssinmentPoolId() {
		return assinmentPoolId;
	}
	public void setAssinmentPoolId(long assinmentPoolId) {
		this.assinmentPoolId = assinmentPoolId;
	}
	public String getEnquiryNo() {
		return enquiryNo;
	}
	public void setEnquiryNo(String enquiryNo) {
		this.enquiryNo = enquiryNo;
	}
	public int getCaseId() {
		return caseId;
	}
	public void setCaseId(int caseId) {
		this.caseId = caseId;
	}
	public String getCaseType() {
		return caseType;
	}
	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}
	public String getLeadEmpName() {
		return leadEmpName;
	}
	public void setLeadEmpName(String leadEmpName) {
		this.leadEmpName = leadEmpName;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getDateOfSiteVisit() {
		return dateOfSiteVisit;
	}
	public void setDateOfSiteVisit(String dateOfSiteVisit) {
		this.dateOfSiteVisit = dateOfSiteVisit;
	}
	public String getDateOfSubmission() {
		return dateOfSubmission;
	}
	public void setDateOfSubmission(String dateOfSubmission) {
		this.dateOfSubmission = dateOfSubmission;
	}
	public String getEntityCompanyName() {
		return entityCompanyName;
	}
	public void setEntityCompanyName(String entityCompanyName) {
		this.entityCompanyName = entityCompanyName;
	}
	public String getEntityAddress() {
		return entityAddress;
	}
	public void setEntityAddress(String entityAddress) {
		this.entityAddress = entityAddress;
	}
	public String getContactPersonName() {
		return contactPersonName;
	}
	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getCustomerEmailAddress() {
		return customerEmailAddress;
	}
	public void setCustomerEmailAddress(String customerEmailAddress) {
		this.customerEmailAddress = customerEmailAddress;
	}
	public String getCustomerCRNNumber() {
		return customerCRNNumber;
	}
	public void setCustomerCRNNumber(String customerCRNNumber) {
		this.customerCRNNumber = customerCRNNumber;
	}
	public String getCorporateName() {
		return corporateName;
	}
	public void setCorporateName(String corporateName) {
		this.corporateName = corporateName;
	}
	public int getIsProcessed() {
		return isProcessed;
	}
	public void setIsProcessed(int isProcessed) {
		this.isProcessed = isProcessed;
	}
	public int getCaseStatus() {
		return caseStatus;
	}
	public void setCaseStatus(int caseStatus) {
		this.caseStatus = caseStatus;
	}
	public String getAssignmentStartTime() {
		return assignmentStartTime;
	}
	public void setAssignmentStartTime(String assignmentStartTime) {
		this.assignmentStartTime = assignmentStartTime;
	}
	public String getDateFrom() {
		return dateFrom;
	}
	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public long getDnbMasterId() {
		return dnbMasterId;
	}
	public void setDnbMasterId(long dnbMasterId) {
		this.dnbMasterId = dnbMasterId;
	}
	public long getDnbMasterDataId() {
		return dnbMasterDataId;
	}
	public void setDnbMasterDataId(long dnbMasterDataId) {
		this.dnbMasterDataId = dnbMasterDataId;
	}
	public int getIsDataDistributed() {
		return isDataDistributed;
	}
	public void setIsDataDistributed(int isDataDistributed) {
		this.isDataDistributed = isDataDistributed;
	}
	public String getSubmissionTime() {
		return submissionTime;
	}
	public void setSubmissionTime(String submissionTime) {
		this.submissionTime = submissionTime;
	}
	public String getEnquiryDetails() {
		return enquiryDetails;
	}
	public void setEnquiryDetails(String enquiryDetails) {
		this.enquiryDetails = enquiryDetails;
	}
	public String getCaseStatusVal() {
		return caseStatusVal;
	}
	public void setCaseStatusVal(String caseStatusVal) {
		this.caseStatusVal = caseStatusVal;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getWebAddress() {
		return webAddress;
	}
	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
	}
	public String getWorflowRemarks() {
		return worflowRemarks;
	}
	public void setWorflowRemarks(String worflowRemarks) {
		this.worflowRemarks = worflowRemarks;
	}
	public long getAsphId() {
		return asphId;
	}
	public void setAsphId(long asphId) {
		this.asphId = asphId;
	}
	public long getAspId() {
		return aspId;
	}
	public void setAspId(long aspId) {
		this.aspId = aspId;
	}
	public String getEqnuiryReceivedTime() {
		return eqnuiryReceivedTime;
	}
	public void setEqnuiryReceivedTime(String eqnuiryReceivedTime) {
		this.eqnuiryReceivedTime = eqnuiryReceivedTime;
	}
	public long getEnquiryDetailsId() {
		return enquiryDetailsId;
	}
	public void setEnquiryDetailsId(long enquiryDetailsId) {
		this.enquiryDetailsId = enquiryDetailsId;
	}
	public String getQfStatus() {
		return qfStatus;
	}
	public void setQfStatus(String qfStatus) {
		this.qfStatus = qfStatus;
	}
	public int getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
}