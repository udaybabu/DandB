package in.otpl.dnb.gw;

import in.otpl.dnb.user.UserDto;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;

public interface GatewayDBAccessor {

	public int enquiryIsDeletedOrExist(Connection conn,long assignmentId,long userId,int customerId);
	
	public void signatureDataAlreadyExist(Connection con, int docId,int seqId,long dnbMasterId,String uKey) ;
	
	public long createMediaFileIdDNB(Connection con, int customerId, long userId, String uniqueKey, String mediaType, int seqId,int docId,
			long asId,String enquiryNo,long dnbMasterId,int isLastImage) ;
	
	public int dnbMediaFilePathUpdate(Connection con, long mediaId, String dataKey, String dataPath,String mediaLink);
	
	public int updateSVRSigImgCount(Connection con, int docId,long dnbMasterId);
	
	public void updateSVRDataDetails(Connection con, int docId,long dnbMasterId,int seqId,String mediaLink,int customerId);
	
	public boolean checkEventExist(Connection conn, String uniqueKey,long dnbMasterId);
	
	public  Map<String, Integer> insertOrUpdateDNBData(Connection conn,int customerId,long assignmentId,String enquiryId,long uId,String svrData,String ccamData,String docData,
			int imgCount,String uniqueKey,String docIds,long dnbMasterId,String ccamComments,String isInsert,String clientTime);
	
	public int updateEnquiryStatus(Connection con,long timeDiff,long assignmentId);
	
	public int updateAssignmentPoolStatus(Connection con,long dnbMasterId);
	
	public int updateDnbAssignmentPoolHistory(Connection con,long asphId,long dnbMasterId);
	
	public void deleteOldSVRAndCCAMData(Connection conn,long dnbMasterId);
	
	public long insertLocData(String clientTime, String lat, String lon, String alt, String speed, String direction, String accuracy,
			String eventName, int cId, long uId, int isValid, int isCellsite, String cellSiteLocAddress, Connection con, String status) ;
	
	public CurrentUserTracking validateTrackingUserId(long userid, int customerId, Connection con) ;
	
	public void updateLastTrackingInfo(long trackingId, long uId, int isValid, int isCellsite,int cId, Connection con);
	
	public UserDto getUserIdFromDB(int customerId, String userLoginId, String password, Connection con) ;
	
	public int updateHandsetDetailsUser(long userId, String handsetDetails, String clientVer, Connection conn);
	
	public String generateSessionId(int customerId, long userId,String loginFrom) ;
	
	public int getSettingsFromDB(Connection con, int customerId, long userId, HashMap<String, String> settingsMap);
	
	public boolean getDNBDocCheckList(Connection conn,HashMap<String, Object> resultMap);
	
	public ArrayList<HashMap<String, Object>> getEnquiriessForUser(Connection conn, int customerId, long userId, boolean isDownload);
	
	public JSONArray getRemovedEnquiryNos(Connection conn, long userId);
	
	public void resetSessionKeyToNull(long userId);
	
	public HashMap<String, String> getDocumentIds(Connection conn,int caseId,long dnbMasterId,String enquiryNo,long asId);
}