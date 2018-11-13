package in.otpl.dnb.enquiryPool;

import java.io.Serializable;

import in.otpl.dnb.util.SessionConstants;

import org.springframework.stereotype.Component;

@Component
public class EnquiryForm implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private long id;
	private	 String dateFrom;
	private	 String dateTo;
	private String assignmentId="";
	private int customerId;
	private String[]assignmentName;
	private String[]workflowId;
	private String[]landmarkId;
	private int[] timeZone;
	private String[] teamId;
	private String[] userId ;
	private int status;
	private int key;
	private int offset = 0;
	private int rowcount = SessionConstants.DEFAULT_ROWCOUNT;
	private String teamName;
	private String teamLeadId;
	private String[] selected;
	private String[] available;
	private int leadId;
	private String firstName;
	private String data;
	private String lastName;
	private String workflowName;
	private int totalCount;
	private int selectStatus;
	private String mediaId;
	private String mediaType;
	private int loggedInUserId=0;
	private String assignmentPoolDateFrom ="";
	private String assignmentPoolDateTo ="";
	private long assinmentPoolId=0;
	private long asphId=0;
	private String enquiryNo="";
    private long dnbMasterId;
    private long uId=0;
    private int caseStatus=0;
    private int wfmStatus=0;
    private int isReassigned = 0;
    private int assignedUserId = 0;
    private long enquiryDetailsId = 0;
    private int selUser = 0;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public int getSelectStatus() {
		return selectStatus;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
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

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public int getLeadId() {
		return leadId;
	}

	public void setLeadId(int leadId) {
		this.leadId = leadId;
	}

	public int getSelUser() {
		return selUser;
	}

	public void setSelUser(int selUser) {
		this.selUser = selUser;
	}

	public long getEnquiryDetailsId() {
		return enquiryDetailsId;
	}

	public void setEnquiryDetailsId(long enquiryDetailsId) {
		this.enquiryDetailsId = enquiryDetailsId;
	}

	public int getAssignedUserId() {
		return assignedUserId;
	}

	public void setAssignedUserId(int assignedUserId) {
		this.assignedUserId = assignedUserId;
	}
    
	public int getIsReassigned() {
		return isReassigned;
	}

	public void setIsReassigned(int isReassigned) {
		this.isReassigned = isReassigned;
	}

	public int getCaseStatus() {
		return caseStatus;
	}

	public void setCaseStatus(int caseStatus) {
		this.caseStatus = caseStatus;
	}

	public int getWfmStatus() {
		return wfmStatus;
	}

	public void setWfmStatus(int wfmStatus) {
		this.wfmStatus = wfmStatus;
	}

	public long getAsphId() {
		return asphId;
	}

	public void setAsphId(long asphId) {
		this.asphId = asphId;
	}

	public long getDnbMasterId() {
		return dnbMasterId;
	}

	public void setDnbMasterId(long dnbMasterId) {
		this.dnbMasterId = dnbMasterId;
	}

	public long getuId() {
		return uId;
	}

	public void setuId(long uId) {
		this.uId = uId;
	}

	public int getLoggedInUserId() {
		return loggedInUserId;
	}

	public void setLoggedInUserId(int loggedInUserId) {
		this.loggedInUserId = loggedInUserId;
	}

	public String getAssignmentPoolDateFrom() {
		return assignmentPoolDateFrom;
	}

	public void setAssignmentPoolDateFrom(String assignmentPoolDateFrom) {
		this.assignmentPoolDateFrom = assignmentPoolDateFrom;
	}

	public String getAssignmentPoolDateTo() {
		return assignmentPoolDateTo;
	}

	public void setAssignmentPoolDateTo(String assignmentPoolDateTo) {
		this.assignmentPoolDateTo = assignmentPoolDateTo;
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

	public int[] getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(int[] timeZone) {
		this.timeZone = timeZone;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getTeamLeadId() {
		return teamLeadId;
	}

	public void setTeamLeadId(String teamLeadId) {
		this.teamLeadId = teamLeadId;
	}

	public String[] getSelected() {
		return selected;
	}

	public void setSelected(String[] selected) {
		this.selected = selected;
	}

	public String[] getAvailable() {
		return available;
	}

	public void setAvailable(String[] available) {
		this.available = available;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(String assignmentId) {
		this.assignmentId = assignmentId;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String[] getAssignmentName() {
		return assignmentName;
	}

	public void setAssignmentName(String[] assignmentName) {
		this.assignmentName = assignmentName;
	}

	public String[] getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(String[] workflowId) {
		this.workflowId = workflowId;
	}

	public String[] getTeamId() {
		return teamId;
	}

	public void setTeamId(String[] teamId) {
		this.teamId = teamId;
	}

	public String[] getUserId() {
		return userId;
	}

	public void setUserId(String[] userId) {
		this.userId = userId;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String[] getLandmarkId() {
		return landmarkId;
	}

	public void setLandmarkId(String[] landmarkId) {
		this.landmarkId = landmarkId;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

}
