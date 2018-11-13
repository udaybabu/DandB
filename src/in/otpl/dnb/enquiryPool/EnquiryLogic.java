package in.otpl.dnb.enquiryPool;

import java.sql.Connection;
import java.util.Map;

import in.otpl.dnb.enquiryPool.EnquiryForm;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public interface EnquiryLogic{	
	
	public Map<String, Object> getAssignmentPool(EnquiryForm enquiryForm,int customerId,String loginName,String userType,long userid);

	public JSONArray getCaseHistory(long dnbMasterId , int customerId);	

	public JSONArray getCaseAssignmentHistory(long dnbMasterId , int customerId);
	
	public long updateDNBAssignmentPoolHistory(int customerId,long userId,long asphId,long assignmentId);
	
	public JSONArray getDocumentList(int customerId, int caseId);
	
    public JSONObject getEnquiryDetails(long dnbMasterId, int customerId);

	public EnquiryDto[] getAllAssignmentStatus();
     
	public long saveUpdateEnquiry(long userId,String enquiryName,long dnbMasterId,long asphId,int isReassigned,long enquiryDetailsId,int customerId,int loggedInUserId,String enquiryStartTime);

	public Map<String, Object> getMediaInfoExtension(long dnbMasterDataId);
	
	public void resetOldDNBData(long dnbMasterId, int resetType, int customerId);

}
