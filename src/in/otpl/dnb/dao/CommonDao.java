package in.otpl.dnb.dao;

import java.sql.Connection;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public interface CommonDao {
	
	public boolean insertSVRData(Connection conn, long asId, String enquiryId, String uKey, int userId, JSONObject svrObject, int customerId, long masterId, long dnbMasterId);

	public boolean insertCCAMData(Connection conn, long asId, String enquiryId, String uKey, int userId, JSONObject ccamObject, int customerId, long masterId, long dnbMasterId);

	public boolean insertDocData(Connection conn, long asId,String enquiryId, String uKey, int userId, JSONArray docArray,int customerId, long masterId, long dnbMasterId);

	public boolean checkIfDataAlreadyExist(Connection conn, int id, String desc, String tableName,int docId);

	public int insertEmailDetails(int customerId, String emailTo, String emailCC, String emailBCC, String subject, String body);
	
	public void dnbReassignmentScheduler(Connection conn,int customerId);
	
	public void checkEnquiryCompleted(Connection conn,int customerId);
	
	public void getInitiatedEnquiryDetails(Connection conn,Connection dnbConn, int customerId);
	
	public JSONObject getDNBMediaDetails(Connection conn, String uKey,long masterId, long dnbMasterId, String docId, String mediaId);
	
}
