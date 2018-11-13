package in.otpl.dnb.enquiryPool;

import java.sql.Connection;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public interface EnquiryDao {

	public Map<String, Object> getAssignmentPool(Connection conn, EnquiryForm enquiryForm, int customerId,String loginName, String userType, long userId);

	public long saveUpdateEnquiry(long userId, String enquiryName, long dnbMasterId, long asphId, int isReassigned,
			
	long enquiryDetailsId, Connection conn, int customerId, int loggedInUserId, String enquiryStartTime);

	public EnquiryDto[] getEnquiriesForUser(String sql, Object[] sqlParams, Connection conn);

	public int updateEnquiryDownloadStatus(Connection conn, String assignmentIds);
	
	public int updateRemovedEnquiryNos(Connection conn, String ids);

	public JSONObject getEnquiryDetails(Connection conn, long dnbMasterId, int customerId);

	public JSONArray getCaseHistory(Connection conn, long dnbMasterId);

	public JSONArray getCaseAssignmentHistory(Connection conn, long dnbMasterId, int customerId);

	public JSONArray getDocumentList(Connection conn, int customerId, int caseId);

	public int updateAssignmentDownloadStatus(Connection conn, String assignmentIds);
	
	public Map<String, Object> getMediaInfoExtension(Connection conn,long dnbMasterDataId);
	
	public void resetOldDNBData(Connection conn, long dnbMasterId,	int resetType, int customerId);
	
}
