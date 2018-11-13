package in.otpl.dnb.gw;

import java.io.IOException;
import java.sql.Connection;

import in.otpl.dnb.user.UserDto;
import in.otpl.dnb.util.ProtocolMismatchException;
import net.sf.json.JSONObject;

public interface GatewayLogic {

	public boolean isValidSession(int customerId, long userId, String sessionId);
	
	public void rejectedJsonBackup(int customerId, long userId, int sessionId, long uniqueId, String jsonString, String remarks);
	
	public JSONObject updateAcknowledgement(int customerId, String ts, long uId, String paramName, String paramValues) throws Exception;
	
	public JSONObject updateForceSetupInactive(int customerId, String ts, long uId) throws NumberFormatException, Exception;
	
	public JSONObject dnbMmediaDataSync(int customerId, long userId, JSONObject dataObject);
	
	public JSONObject insertDNBFormData(int customerId, String ts, long uId, JSONObject jsonRequestObject) throws Exception;
	
	public int insertDocID(Connection con,String uniqueKey,long dnbMasterId,long assignmentEnquiryId,int docId,int cId,long uId,String enquiryNo);

	public JSONObject validateLogin(int customerId, String userLoginId, String password, String userAgent, boolean httpCall, String clientVer, String ts, String imei, String lat, String lon) ;

	public String generateSessionId(int customerId, long userId,String loginFrom);
	
	public JSONObject getSettingsForUser(int customerId, long userId, String ts);
	
	public UserDto getUserDetail(long userId, Connection con);
	
	public JSONObject updateCheckCall(long userId, int customerId);
	
	public JSONObject getDNBDocCheckList();
	
	public JSONObject getEnquiryForUser(int customerId, long userId, boolean isDownload) throws IOException;
	
	public JSONObject getRemovedEnquiryNos(int customerId, long userId) throws IOException;
	
	public void validateHeader(String protocolId, String clientVersion, String callType, String callName) throws ProtocolMismatchException;
	
	public void validateCallType(String callType) throws ProtocolMismatchException;
	
	public void validateCallName(String callName) throws ProtocolMismatchException;
	
	public void validateClientVersion(String clientVersion) throws ProtocolMismatchException;
	
	public void validateProtocolId(String protocolId) throws ProtocolMismatchException;
	
	public JSONObject insertEventData(int customerId, String ts, long uId, JSONObject jsonEventObject, JSONObject jsonLocObject, String userAgent, String clientVer, int updatedBy,String eventName) throws Exception ;
}
