package in.otpl.dnb.gw;

import in.otpl.dnb.enquiryPool.EnquiryDao;
import in.otpl.dnb.user.UserDao;
import in.otpl.dnb.user.UserDto;
import in.otpl.dnb.util.ClientRequestConstants;
import in.otpl.dnb.util.ConfigManager;
import in.otpl.dnb.util.ErrorLogHandler;
import in.otpl.dnb.util.PlatformUtil;
import in.otpl.dnb.util.ProtocolMismatchException;
import in.otpl.dnb.util.ResourceManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GatewayLogicImpl implements GatewayLogic{

	private static final Logger LOG = Logger.getLogger(GatewayLogicImpl.class);
	private static final SimpleDateFormat mediaDirectoryDateFormat = new SimpleDateFormat("dd.MM.yyyy");
	private final static String seperator = "/";

	@Autowired
	private ResourceManager resourceManager;
	@Autowired
	private AppSessionDao appSessionDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private GatewayDBAccessor gatewayDBAccessor;
	@Autowired
	private ConfigManager configManager;
	@Autowired
	private EnquiryDao enquiryDao;

	/* Session Key Validation */
	@Override
	public boolean isValidSession(int customerId, long userId, String sessionId) {
		Connection conn = null;
		boolean isSessionValid = false;
		try{
			conn = resourceManager.getConnection();
			isSessionValid = appSessionDao.isValidSession(conn, customerId, userId, sessionId);
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(conn);
		}
		LOG.info("Is valid Session : "+isSessionValid);
		return isSessionValid;
	}

	/* Backup for Rejected JSON */
	@Override
	public void rejectedJsonBackup(int customerId, long userId, int sessionId, long uniqueId, String jsonString, String remarks) {
		Connection conn = null;
		try{
			conn = resourceManager.getConnection();
			boolean check = appSessionDao.checkUniqueKey(conn, customerId, userId, uniqueId);
			if(!check){
				int sessionIdCurrent = appSessionDao.getSessionKey(customerId, userId, conn);
				appSessionDao.rejectedDataBackup(conn, customerId, userId, sessionId, sessionIdCurrent, uniqueId, jsonString, remarks);
			}
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(conn);
		}		
	}

	/* Update Call Success */
	@Override
	public JSONObject updateAcknowledgement(int customerId, String ts, long uId, String paramName, String paramValues) throws Exception {
		Map<String, String> resultMap = new HashMap<String, String>();
		Connection conn = null;
		try{
			conn = resourceManager.getConnection();
			// Login & Client Update
			if(paramName.equals(ClientRequestConstants.LOGIN_GET)){
				//return updateForceSetupInactive(customerId, ts, uId);
			}
			// Enquiry
			else if(paramName.equals(ClientRequestConstants.DNB_ENQUIRY_GET) || paramName.equals(ClientRequestConstants.DNB_ENQUIRY_REGET)){
				if(paramValues != null && !paramValues.isEmpty()){
					int count = enquiryDao.updateEnquiryDownloadStatus(conn, paramValues);
					if(count > 0){
						resultMap.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.SUCCESS_ACK_TASK);
						resultMap.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.SUCCESS_ACK_TASK_MSG);
					}else{
						resultMap.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.ERROR_ACK_TASK);
						resultMap.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.ERROR_ACK_TASK_MSG);
					}
				}else{
					resultMap.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.SUCCESS_ACK_TASK_NOTHING);
					resultMap.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.SUCCESS_ACK_TASK_NOTHING_MSG);
				}
			}
			// Enquiry Removed
			else if(paramName.equals(ClientRequestConstants.DNB_REMOVED_ENQUIRY_LIST)){
				if(paramValues != null && !paramValues.isEmpty()){
					int count = enquiryDao.updateRemovedEnquiryNos(conn, paramValues);
					if(count > 0){
						resultMap.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.SUCCESS_ACK_TASK);
						resultMap.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.SUCCESS_ACK_TASK_MSG);
					}else{
						resultMap.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.ERROR_ACK_TASK);
						resultMap.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.ERROR_ACK_TASK_MSG);
					}
				}else{
					resultMap.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.SUCCESS_ACK_TASK_NOTHING);
					resultMap.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.SUCCESS_ACK_TASK_NOTHING_MSG);
				}
			}
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			resultMap.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.ERROR_ACK);
			resultMap.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.ERROR_ACK_MSG);
		}finally{
			resourceManager.close(conn);
		}
		return JSONObject.fromObject(resultMap);
	}

	/* Force Setup Update Inactive for User*/
	@Override
	public JSONObject updateForceSetupInactive(int customerId, String ts, long uId) throws NumberFormatException, Exception{
		Connection conn = null;
		boolean check = false;
		try{
			conn = resourceManager.getConnection();
			check = userDao.updateForceSetupInactive(conn,uId, customerId, ts);
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(conn);
		}
		Map<String, String> resultMap = new HashMap<String, String>();
		if(check){
			resultMap.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.SUCCESS_ACK_USER_UPDATE);
			resultMap.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.SUCCESS_ACK_USER_UPDATE_MSG);
		}else{
			resultMap.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.ERROR_ACK_USER_UPDATE);
			resultMap.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.ERROR_ACK_USER_UPDATE_MSG);
		}
		return JSONObject.fromObject(resultMap);
	}

	@Override
	public JSONObject dnbMmediaDataSync(int customerId, long userId, JSONObject dataObject) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		Connection conn = null;
		int isUpdated = 0;
		long dnbMasterId = 0;
		String mediaLink="";
		try{
			String uniqueKey = dataObject.getString("uniqueKey"); 
			String mediaType = dataObject.getString("ext");
			int seqId = dataObject.getInt("picIndex");
			int docId = dataObject.getInt("docId");
			long asId = dataObject.getLong("asId");
			String enquiryNo = dataObject.getString("enquiry_id");
			dnbMasterId = dataObject.getLong("dnbMasterId");
			int isLastImage = dataObject.getInt("isLastImage");
			long uId = userId;
			int cId = customerId;
			conn = resourceManager.getConnection();
			conn.setAutoCommit(false);
			int isDeleted = gatewayDBAccessor.enquiryIsDeletedOrExist(conn,asId,uId,cId);
			if(isDeleted == 1){
				conn.commit();
				conn.setAutoCommit(true);
				resultMap.put(ClientRequestConstants.REASSIGNED_MSG_STRING, ClientRequestConstants.DNB_REASSIGNED_TO_OTHER_USER_MSG);
				resultMap.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.SUCCESS_MEDIA_SYNC);
				resultMap.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.SUCCESS_MEDIA_SYNC_MSG);
				resultMap.put(ClientRequestConstants.CUSTOMER_ID, customerId);
				resultMap.put(ClientRequestConstants.USER_ID, userId);
			}else{
				if(docId == 2) 
					gatewayDBAccessor.signatureDataAlreadyExist(conn, docId, seqId, dnbMasterId,uniqueKey);
				long mediaId = gatewayDBAccessor.createMediaFileIdDNB(conn, cId, uId,  uniqueKey, mediaType, seqId,docId,asId,enquiryNo,dnbMasterId,isLastImage);
				if(mediaId > 0){
					String dataString = dataObject.getString("data");
					String fileName = userId+"_"+enquiryNo+"_"+docId+"_"+mediaId+"."+mediaType;
					String dateDirectory = mediaDirectoryDateFormat.format(new Date());
					String dirPath = configManager.getMediaFilePath() + seperator + customerId ;
					new File(dirPath, dateDirectory).mkdirs();
					String imgPath = dirPath + seperator + dateDirectory + seperator+ fileName;
					FileOutputStream fos = new FileOutputStream(new File(imgPath));
					byte[] decodedBytes = Base64.decodeBase64(dataString.getBytes());
					fos.write(decodedBytes);
					fos.close();
					
					if(new File(imgPath).length() > 0){
						String dataPath = seperator + customerId + seperator + dateDirectory + seperator + fileName;
						String key = mediaId+"_"+customerId+"_"+userId;
						byte[] encoded = Base64.encodeBase64(key.getBytes());
						String dataKey = new String(encoded);
						//mediaLink = configManager.getBaseServerURL()+URLConstants.ENQUIRY_REPORT_MEDIA+mediaType+"/"+mediaId;
						isUpdated = gatewayDBAccessor.dnbMediaFilePathUpdate(conn, mediaId, dataKey, dataPath,mediaLink);
						if(docId == 2){
							gatewayDBAccessor.updateSVRSigImgCount(conn, docId,dnbMasterId);
							//gatewayDBAccessor.updateSVRDataDetails(conn, docId,dnbMasterId,seqId,mediaLink,cId);
						}
						conn.commit();
						conn.setAutoCommit(true);
					}else{
						LOG.info("Image not uploaded properly");
						if(conn != null) {
							try{
								conn.rollback();
							}catch(Exception rollBackExcepion) {
								LOG.error(ErrorLogHandler.getStackTraceAsString(rollBackExcepion));
							}
						}
					}
				}
			}
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			if(conn != null) {
				try{
					conn.rollback();
				}catch(Exception rollBackExcepion) {
					LOG.error(ErrorLogHandler.getStackTraceAsString(rollBackExcepion));
				}
			}
		}finally{
			resourceManager.close(conn);
		}
		
		if(isUpdated > 0){
			resultMap.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.SUCCESS_MEDIA_SYNC);
			resultMap.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.SUCCESS_MEDIA_SYNC_MSG);
		}else{
			resultMap.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.ERROR_MEDIA_SYNC);
			resultMap.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.ERROR_MEDIA_SYNC_MSG);
		}
		resultMap.put(ClientRequestConstants.CUSTOMER_ID, customerId);
		resultMap.put(ClientRequestConstants.USER_ID, userId);
		return JSONObject.fromObject(resultMap);
	}

	@Override
	public JSONObject insertDNBFormData(int customerId, String ts, long uId, JSONObject jsonRequestObject) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		String clientTime = "";
		Connection con = null;
		long trackId =0;
		PreparedStatement  mpstmt = null;
		ResultSet mrst = null;
		String ccamComments="",eventName="";
		int imgCount = 0, signCount = 0 ; long startTime = 0, endTime = 0 , timeDiff = 0;
		String docData = "",svrData="",ccamData="", formName="";
		boolean dataExists = false;
		try{
			con = resourceManager.getConnection();
			con.setAutoCommit(false);
			JSONArray jsonObjectArray = jsonRequestObject.getJSONArray("formData");
			JSONObject jsonLocObject = jsonRequestObject.getJSONObject("locData");
			if(jsonRequestObject.containsKey("formImageCount") && !jsonRequestObject.getString("formImageCount").trim().equals("")) 
				imgCount = jsonRequestObject.getInt("formImageCount");
			long assignmentId = jsonRequestObject.getLong("asId");
			String enquiryId = jsonRequestObject.getString("enquiry_id");
			String uniqueKey = jsonRequestObject.getString("uniqueKey");
			String docIds = jsonRequestObject.getString("doc_id");
			long assignmentEnquiryId = jsonRequestObject.getLong("assignment_pool_id");
			long asphId = jsonRequestObject.getLong("asph_id");
			long dnbMasterId = jsonRequestObject.getLong("dnbMasterId");
			if(jsonRequestObject.containsKey("formFillingStartTym"))  startTime = jsonRequestObject.getLong("formFillingStartTym");
			if(jsonRequestObject.containsKey("formFillingEndTym"))  endTime = jsonRequestObject.getLong("formFillingEndTym");
			/* Tracking Record */
			String lat = "NA", lon = "NA", alt = "NA", speed = "0", direction = "NA", accuracy = "NA", cellSiteLocAddress = "", BCCState = "";
			int isValidLatLon = 0, isCellsite = 0;
			if(jsonLocObject.containsKey("landmark")) cellSiteLocAddress = jsonLocObject.getString("landmark");
			lat = jsonLocObject.getString(ClientRequestConstants.CLIENT_LAT);
			lon = jsonLocObject.getString(ClientRequestConstants.CLIENT_LON);
			if(!lat.equalsIgnoreCase("NA")) 
				lat = latValidate(lat);
			if(lat.equalsIgnoreCase("NA")) 
				lon = "NA";
			else{ if(!lon.equalsIgnoreCase("NA")){
					lon = lonValidate(lon);
					isValidLatLon = 1;
				}
				if(lon.equalsIgnoreCase("NA")) lat = "NA";
			}
			alt = jsonLocObject.getString("alt");
			if(jsonLocObject.getString("alt").length()>=10){
				alt=jsonLocObject.getString("alt").substring(0,10);
			}
			speed = jsonLocObject.getString("speed");
			if(jsonLocObject.getString("speed").length()>=10){
				speed=jsonLocObject.getString("speed").substring(0,10);
			}
			direction = jsonLocObject.getString("direction");
			if(jsonLocObject.getString("direction").length()>=10){
				direction=jsonLocObject.getString("direction").substring(0,10);
			}
			accuracy = jsonLocObject.getString("acc");
			if(jsonLocObject.getString("acc").length()>=10){
				accuracy=jsonLocObject.getString("acc").substring(0,10);
			}
			if(accuracy.trim().equals("")) accuracy = "NA";
			if(!accuracy.equals("NA")){
				if(Math.round(Double.parseDouble(accuracy)) > 150) isCellsite = 1; // Cell site location 
			}
			if(jsonLocObject.getInt("type") == 3){
				isCellsite = 3;
			}
			timeDiff = endTime - startTime;
			for(int i = 0 ; i < jsonObjectArray.size() ; i++){
				JSONObject jobj = jsonObjectArray.getJSONObject(i);
				if(jobj.containsKey("name")){
					formName = jobj.getString("name");
					if(formName.equalsIgnoreCase("svr")){
						svrData = jobj.getString("data");
						signCount = jobj.getInt("signCount");
					}else if(formName.equalsIgnoreCase("ccam")){
						ccamData = jobj.getString("data");
						ccamComments = jobj.getString("comments");
					}else if(formName.equalsIgnoreCase("docData")){
						docData = jobj.getString("data");
					}
				}
			}
			dataExists = gatewayDBAccessor.checkEventExist(con,uniqueKey,dnbMasterId);
			if(dataExists){ //Update Old Data 
				long masterRowId = 0; int enquiryRowId = 0, aspRowId = 0,oldImgCount = 0, newImgCount =0,svrImgCount=0;
				String newDocString = "",newSVRString = "",newCCAMString="",oldDocString = "",oldSVRString = "",oldCCAMString="";
				String sql = "select doc_data as old_doc_data,svr_data as old_svr_data,ccam_data as old_ccam_data,img_count,svr_img_count "
						+ "from dnb_master_data where unique_key = '"+uniqueKey+"' and dnb_master_id = "+dnbMasterId+"";
				mpstmt = con.prepareStatement(sql);
				mrst = mpstmt.executeQuery();
				if(mrst.next()){
					oldDocString = mrst.getString("old_doc_data");
					oldSVRString = mrst.getString("old_svr_data");
					oldCCAMString = mrst.getString("old_ccam_data");
					oldImgCount = mrst.getInt("img_count");
					svrImgCount = mrst.getInt("svr_img_count");
				}
				if(!docData.equals("")){
					JSONArray newlyCreatedArray = new JSONArray();
					JSONArray newDocArray = JSONArray.fromObject(docData);
					JSONArray oldDocArray = null;
					if(newDocArray.size() > 0){
						oldDocArray = JSONArray.fromObject(oldDocString);
						if(oldDocArray.size() > 0){
							for(int i = 0 ; i < newDocArray.size();i++){
								JSONObject newObj = newDocArray.getJSONObject(i);
								int newDocId = newObj.getInt("docId");
								for(int j = 0 ; j < oldDocArray.size() ; j++){
									JSONObject oldObj = oldDocArray.getJSONObject(j);
									int oldDocId = oldObj.getInt("docId");
									if(newDocId == oldDocId){
										oldDocArray.remove(oldObj);
									}
								}
							}
							for(int l = 0 ; l < oldDocArray.size() ; l++){
								newlyCreatedArray.add(oldDocArray.getJSONObject(l));
							}
							for(int l = 0 ; l < newDocArray.size() ; l++){
								newlyCreatedArray.add(newDocArray.getJSONObject(l));
							}
						}
						newDocString = newlyCreatedArray.toString();
						for(int k =0 ; k < newlyCreatedArray.size(); k++){
							JSONObject obj = newlyCreatedArray.getJSONObject(k);
							String count = obj.getString("mediaCount");
							if(!count.equals("")){
								newImgCount = newImgCount+Integer.parseInt(count);
							}
						}
					}else{
						newDocString = oldDocString;
						newImgCount = oldImgCount;
					}
				}else{
					newDocString = oldDocString;
					newImgCount = oldImgCount;
				}
				if(PlatformUtil.isContain(docIds, "1")){ // ccam 
					if(!ccamData.equals("")) newCCAMString = ccamData;
					else newCCAMString = oldCCAMString;
					int docId = 1;
					insertDocID(con,uniqueKey,dnbMasterId,assignmentId,docId,customerId,uId,enquiryId);
				}else{
					newCCAMString = oldCCAMString;
				}
				if(PlatformUtil.isContain(docIds, "2")){ // svr
					if(!svrData.equals("")) newSVRString = svrData;
					else newSVRString = oldSVRString;
					int docId = 2;
					insertDocID(con,uniqueKey,dnbMasterId,assignmentId,docId,customerId,uId,enquiryId);
					svrImgCount = signCount;
				}else{
					newSVRString = oldSVRString;
				}
				newImgCount = newImgCount + svrImgCount;
				Map<String, Integer> map = gatewayDBAccessor.insertOrUpdateDNBData(con, customerId, assignmentId, enquiryId, uId, newSVRString, newCCAMString, 
						newDocString, newImgCount, uniqueKey, docIds, dnbMasterId, ccamComments, "update",clientTime);
				int isDeleted = map.get("is_deleted");
				masterRowId = map.get("rowId");
				if(isDeleted == 1 || isDeleted == 2){ // Reassigned to someone else // Send success msg to client for deletion internally
					con.commit();
					con.setAutoCommit(true);
					resultMap.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.DNB_DATA_SUCCESS);
					resultMap.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.DNB_DATA_SUCCESS_INSERT_MSG);
					resultMap.put(ClientRequestConstants.REASSIGNED_MSG_STRING, ClientRequestConstants.DNB_REASSIGNED_TO_OTHER_USER_MSG);
					resultMap.put(ClientRequestConstants.CUSTOMER_ID, customerId);
				}else if(isDeleted == 0){ // Allow to update
					enquiryRowId = gatewayDBAccessor.updateEnquiryStatus(con, timeDiff, assignmentId);
					aspRowId = gatewayDBAccessor.updateAssignmentPoolStatus(con, dnbMasterId);
					int poolHisId = gatewayDBAccessor.updateDnbAssignmentPoolHistory(con,asphId,dnbMasterId);
					gatewayDBAccessor.deleteOldSVRAndCCAMData(con,dnbMasterId);
					if(masterRowId > 0 && enquiryRowId > 0 && poolHisId > 0 && isDeleted == 0){
						con.commit(); 
						con.setAutoCommit(true);
						resultMap.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.DNB_DATA_SUCCESS);
						resultMap.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.DNB_DATA_SUCCESS_UPDATE_MSG);
						resultMap.put(ClientRequestConstants.CUSTOMER_ID, customerId);
					}
				}else{
					try{
						con.rollback();
					}catch(Exception e){
						LOG.error(ErrorLogHandler.getStackTraceAsString(e));
					}
					resultMap.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.DNB_DATA_FAILURE);
					resultMap.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.DNB_DATA_FAILURE_UPDATED_MSG);
					resultMap.put(ClientRequestConstants.CUSTOMER_ID, customerId);
				}
			}else{ // Insert DNB Form Data
				int insertRowId = 0;
				Map<String, Integer> map = gatewayDBAccessor.insertOrUpdateDNBData(con,customerId,assignmentId,enquiryId,uId,svrData,ccamData,docData,
						imgCount,uniqueKey,docIds,dnbMasterId,ccamComments,"insert",clientTime);
				int isDeleted = map.get("is_deleted");
				int rowId = map.get("rowId");
				if(rowId > 0 && isDeleted == 0){
					eventName="Enquiry Submission - "+enquiryId+" ";
					trackId = gatewayDBAccessor.insertLocData(clientTime, lat, lon, alt, speed, direction, accuracy, eventName, customerId, uId, isValidLatLon, isCellsite, cellSiteLocAddress, con, BCCState);
					insertRowId = gatewayDBAccessor.updateEnquiryStatus(con,timeDiff,assignmentId);
					int poolHisId = gatewayDBAccessor.updateDnbAssignmentPoolHistory(con,asphId,dnbMasterId);
					if(insertRowId > 0 && poolHisId > 0){
						// Insert SVR & CCAM DOC-ID into dnb_workflow_media for accept / reject when required
						int docId = 0;
						if(PlatformUtil.isContain(docIds, "1")){ // CCAM 
							docId = 1;
							insertDocID(con,uniqueKey,dnbMasterId,assignmentId,docId,customerId,uId,enquiryId);
						}
						if(PlatformUtil.isContain(docIds, "2")){ // SVR
							docId = 2;
							insertDocID(con,uniqueKey,dnbMasterId,assignmentId,docId,customerId,uId,enquiryId);
						}
						con.commit();
						con.setAutoCommit(true);
						resultMap.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.DNB_DATA_SUCCESS);
						resultMap.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.DNB_DATA_SUCCESS_INSERT_MSG);
						resultMap.put(ClientRequestConstants.CUSTOMER_ID, customerId);
					}else{
						try{
							con.rollback();
						}catch(Exception e){
							LOG.error(ErrorLogHandler.getStackTraceAsString(e));
						}
						resultMap.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.DNB_DATA_FAILURE);
						resultMap.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.DNB_DATA_FAILURE_MSG);
						resultMap.put(ClientRequestConstants.CUSTOMER_ID, customerId);
					}
				}else if(isDeleted == 1 ||isDeleted == 2){ // Reassigned to someone else // Send success msg to client for deletion internally
					con.commit();
					con.setAutoCommit(true);
					resultMap.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.DNB_DATA_SUCCESS);
					resultMap.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.DNB_DATA_SUCCESS_INSERT_MSG);
					resultMap.put(ClientRequestConstants.REASSIGNED_MSG_STRING, ClientRequestConstants.DNB_REASSIGNED_TO_OTHER_USER_MSG);
					resultMap.put(ClientRequestConstants.CUSTOMER_ID, customerId);
				}else{
					try{
						con.rollback();
					}catch(Exception e){
						LOG.error(ErrorLogHandler.getStackTraceAsString(e));
					}
					resultMap.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.DNB_DATA_FAILURE);
					resultMap.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.DNB_DATA_FAILURE_MSG);
					resultMap.put(ClientRequestConstants.CUSTOMER_ID, customerId);
				}
			}
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			if(con != null) {
				try{
					con.rollback();
				}catch(Exception rollBackExcepion) {
					LOG.error(ErrorLogHandler.getStackTraceAsString(rollBackExcepion));
				}
			}
			resultMap.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.DNB_DATA_FAILURE);
			resultMap.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.DNB_DATA_FAILURE_MSG);
			resultMap.put(ClientRequestConstants.CUSTOMER_ID, customerId);
		}finally{
			resourceManager.close(mrst);
			resourceManager.close(mpstmt);
			resourceManager.close(con);
		}
		return JSONObject.fromObject(resultMap);
	}

	@Override
	public int insertDocID(Connection con,String uniqueKey,long dnbMasterId,long assignmentEnquiryId,int docId,int cId,long uId,String enquiryNo){
		PreparedStatement pstmt = null;
		int index = 1;
		int rowId = 0;
		try{
			String sql = "insert into dnb_workflow_media (customer_id,user_id,doc_id,as_id,enquiry_id,unique_key,created_time,modified_time,dnb_master_id,action_id,seq_id) "
					+ "values (?,?,?,?,?,?,?,?,?,?,?) ";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(index++, cId);
			pstmt.setLong(index++, uId);
			pstmt.setInt(index++, docId);
			pstmt.setLong(index++, assignmentEnquiryId);
			pstmt.setString(index++, enquiryNo);
			pstmt.setString(index++, uniqueKey);
			pstmt.setString(index++, PlatformUtil.getRealDateToSQLDateTime(new Date()));
			pstmt.setString(index++, PlatformUtil.getRealDateToSQLDateTime(new Date()));
			pstmt.setLong(index++, dnbMasterId);
			pstmt.setInt(index++, 1);
			pstmt.setInt(index++, 0);
			rowId = pstmt.executeUpdate();
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(pstmt);
		}
		return rowId;
	}

	/* Client Data Insertion , Single Form*/
	public JSONObject insertEventData(int customerId, String ts, long uId, JSONObject jsonEventObject, JSONObject jsonLocObject, String userAgent, String clientVer,int updatedBy,String eventName) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		boolean eventExists = false;
		Map<String, Object> eventJsonMap = new HashMap<String, Object>();
		Map<String, String> locJsonMap = new HashMap<String, String>();
		String clientTime = ts;
		String eventId = null;
		String uniqueId = null;
		long trackId = 0;int eventUpdatedBy=0,isEdited=0;
		long formStartTime = 0,formSubmissionTime=0,submisstionTimeDiff=0;
		Connection con = null;
		try {
			con = resourceManager.getConnection();
			con.setAutoCommit(false);
			UserDto user = getUserDetail(uId, con);
			if(user != null && user.getStatus()==1){
				if(jsonEventObject.containsKey(ClientRequestConstants.EVENT_ID)){
					try {
						eventId = jsonEventObject.getString(ClientRequestConstants.EVENT_ID);
						if(jsonEventObject.containsKey(ClientRequestConstants.UNIQUE_ID)){
							uniqueId = jsonEventObject.getString(ClientRequestConstants.UNIQUE_ID);
							eventJsonMap.put("eventId", eventId);
							eventJsonMap.put("clientTime", ts);
							if(uniqueId != "") eventExists = true;
						}
						if(jsonEventObject.containsKey(ClientRequestConstants.FORM_START_TIME)) formStartTime = jsonEventObject.getLong(ClientRequestConstants.FORM_START_TIME);
						if(jsonEventObject.containsKey(ClientRequestConstants.IS_EDITED)) isEdited = jsonEventObject.getInt(ClientRequestConstants.IS_EDITED);
						if(jsonEventObject.has(ClientRequestConstants.FORM_END_TIME)) formSubmissionTime = jsonEventObject.getLong(ClientRequestConstants.FORM_END_TIME);
						if(formStartTime > 0){
							Date d1 = new Date(formStartTime);
							Date d2 = new Date(formSubmissionTime);
							submisstionTimeDiff = d2.getTime() - d1.getTime();
						}
					}catch(Exception e){
						LOG.error(ErrorLogHandler.getStackTraceAsString(e));
					}
				}
				if(jsonEventObject.containsKey(ClientRequestConstants.UPDATED_BY)) eventUpdatedBy = jsonEventObject.getInt("updatedBy");
				if(eventUpdatedBy == 0) eventUpdatedBy = updatedBy;
				String BCCState = "";
				/* Tracking Record */
				boolean locationExists = false, status = false;
				if(jsonLocObject.containsKey(ClientRequestConstants.CLIENT_LAT)) locationExists = true;
				if(jsonLocObject.containsKey(ClientRequestConstants.STATUS)){
					if(jsonLocObject.getString("status").equalsIgnoreCase("OK")) status = true;
				}
				String lat = "NA", lon = "NA", alt = "NA", speed = "0", direction = "NA", accuracy = "NA", cellSiteLocAddress = "";
				int isValidLatLon = 0, isCellsite = 0;
				if(jsonLocObject.containsKey("landmark")) cellSiteLocAddress = jsonLocObject.getString("landmark");
				if(locationExists && status) {
					lat = jsonLocObject.getString(ClientRequestConstants.CLIENT_LAT);
					lon = jsonLocObject.getString(ClientRequestConstants.CLIENT_LON);
					if(!lat.equalsIgnoreCase("NA")) lat = latValidate(lat);
					if(lat.equalsIgnoreCase("NA")) lon = "NA";
					else{
						if(!lon.equalsIgnoreCase("NA")){
							lon = lonValidate(lon);
							isValidLatLon = 1;
						}
						if(lon.equalsIgnoreCase("NA")) lat = "NA";
					}
					alt = jsonLocObject.getString("alt");
					if(jsonLocObject.getString("alt").length()>=10){
						alt=jsonLocObject.getString("alt").substring(0,10);
					}
					speed = jsonLocObject.getString("speed");
					if(jsonLocObject.getString("speed").length()>=10){
						speed=jsonLocObject.getString("speed").substring(0,10);
					}
					direction = jsonLocObject.getString("direction");
					if(jsonLocObject.getString("direction").length()>=10){
						direction=jsonLocObject.getString("direction").substring(0,10);
					}
					accuracy = jsonLocObject.getString("acc");
					if(jsonLocObject.getString("acc").length()>=10){
						accuracy=jsonLocObject.getString("acc").substring(0,10);
					}
					if(accuracy.trim().equals("")) accuracy = "NA";
					if(!accuracy.equals("NA")){
						if(Math.round(Double.parseDouble(accuracy)) > 150) isCellsite = 1; // Cell site location 
					}
					if(jsonLocObject.getInt("type") == 3){
						isCellsite = 3;
					}
					locJsonMap.put("lat", lat);
					locJsonMap.put("lon", lon);
					locJsonMap.put("speed", speed);
					locJsonMap.put("direction", direction);
					locJsonMap.put("accuracy", accuracy);
					locJsonMap.put("cellSite", Integer.toString(isCellsite));
					locJsonMap.put("cellSiteLoc", cellSiteLocAddress);
				} else {
					locJsonMap.put("lat", "NA");
					locJsonMap.put("lon", "NA");
					locJsonMap.put("speed", "NA");
					locJsonMap.put("direction", "NA");
					locJsonMap.put("accuracy", "NA");
					locJsonMap.put("cellSite", "0");
					locJsonMap.put("cellSiteLoc", cellSiteLocAddress);
				}
				/* Check workflow exist or not for Tracking */
				trackId = gatewayDBAccessor.insertLocData(clientTime, lat, lon, alt, speed, direction, accuracy, eventName, customerId, uId, isValidLatLon, isCellsite, cellSiteLocAddress, con, BCCState);
				locJsonMap.put("trackId", Long.toString(trackId));
				/* Update Handset details */
				updateHandsetDetailsUser(uId, userAgent, clientVer, con);
				/* Application Exit */
				if(eventName.equals(ClientRequestConstants.EVENT_APP_EXIT) || eventName.equals(ClientRequestConstants.PERTIAL_EXIT)){
					if(eventName.equals(ClientRequestConstants.EVENT_APP_EXIT)){
						gatewayDBAccessor.resetSessionKeyToNull(uId);
					}
					con.commit();
					con.setAutoCommit(true);
					resultMap.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.SUCCESS_APP_EXIT);
					resultMap.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.SUCCESS_APP_EXIT_MSG);
					resultMap.put(ClientRequestConstants.CUSTOMER_ID, customerId);
					return JSONObject.fromObject(resultMap);
				}
				/* insert the event */
				if(eventExists) {
					if(eventExists || locationExists) {
						con.commit();
						con.setAutoCommit(true);
						JSONArray jsonArray = new JSONArray();
						return getRequestStatus(customerId, uId, ClientRequestConstants.SUCCESS_EVENT_INSERT, ClientRequestConstants.SUCCESS_EVENT_INSERT_MSG, con, jsonArray);
					} else {
						con.setAutoCommit(true);
						resultMap.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.ERROR_EVENT_LOC);
						resultMap.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.ERROR_EVENT_LOC_MSG);
						resultMap.put(ClientRequestConstants.CUSTOMER_ID, customerId);
					}
				}else{
					resultMap.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.ERROR_USER_INACTIVE);
					resultMap.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.ERROR_USER_INACTIVE_MSG);
				}
			}
		} catch(Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			if(con != null) {
				try {
					con.rollback();
				} catch(Exception rollBackExcepion) {

				}
			}
			resultMap.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.ERROR_TECHNICAL_PROBLEM);
			resultMap.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.ERROR_TECHNICAL_PROBLEM_MSG);
			resultMap.put(ClientRequestConstants.CUSTOMER_ID, customerId);
		} finally {
			resourceManager.close(con);
		}
		return JSONObject.fromObject(resultMap);
	}


	/* Client Login */
	@Override
	public JSONObject validateLogin(int customerId, String userLoginId, String password, String userAgent, boolean httpCall, String clientVer, String ts, String imei, String lat, String lon) {
		Connection con = null;
		UserDto user= null;
		HashMap<String, Object> result = new HashMap<String, Object>();
		try{
			con = resourceManager.getConnection();
			user = gatewayDBAccessor.getUserIdFromDB(customerId, userLoginId, password, con);
			long userId = 0;
			if(user != null ) {
				userId = user.getId();
			}
			if(userId > 0){
				String userName = user.getFirstName()+" "+user.getLastName();
				String empCode = user.getEmployeeNumber();
				long teamId = user.getTeamId();
				if(!imei.equals(user.getImei())) {
					result.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.ERROR_IMEI);
					result.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.ERROR_IMEI_MSG);
					result.put(ClientRequestConstants.USER_ID, String.valueOf(userId));
					result.put(ClientRequestConstants.CUSTOMER_ID, customerId);
					return JSONObject.fromObject(result);
				}
				result.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.SUCCESS_LOGIN);
				result.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.SUCCESS_LOGIN_MSG);
				result.put(ClientRequestConstants.USER_ID, String.valueOf(userId));
				result.put(ClientRequestConstants.USER_NAME,userName);
				result.put(ClientRequestConstants.EMP_CODE,empCode);
				result.put(ClientRequestConstants.TEAM_ID, String.valueOf(teamId));
				result.put(ClientRequestConstants.CUSTOMER_ID, customerId);
				String[] callsToDo = getCallsToMake(con, customerId, user.getUserTypeId());
				result.put(ClientRequestConstants.CALLTOMAKE_STRING, callsToDo);
				updateHandsetDetailsUser(userId, userAgent, clientVer, con);
				updateForceSetupInactive(customerId, ts,userId);
				try{
					if(ts != null){
						String clientTime = "";
						int isValidLatLon = 0;
						if(lat.equalsIgnoreCase("NA")) lon = "NA";
						else{
							lat = latValidate(lat);
							if(!lon.equalsIgnoreCase("NA")){
								lon = lonValidate(lon);
								isValidLatLon = 1;
							}else lat = "NA";
						}
						gatewayDBAccessor.insertLocData(clientTime, lat, lon, "NA", "0", "NA", "NA", ClientRequestConstants.EVENT_LOGIN, customerId, userId, isValidLatLon, 0, "", con, "");
					}else{
						LOG.info("Client time is null. Can't insert LI event");
					}
				}catch(Exception e){
					LOG.error(ErrorLogHandler.getStackTraceAsString(e));
				}
				LOG.info("Client login successful");
			} else{
				LOG.info("Client login Failure due to unauthorized access !");
				result.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.ERROR_UNAUTHORIZED_ACCESS);
				result.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.ERROR_UNAUTHORIZED_ACCESS_MSG);
				result.put(ClientRequestConstants.USER_ID, String.valueOf(userId));
				result.put(ClientRequestConstants.CUSTOMER_ID, customerId);
			}
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(con);
		}
		return JSONObject.fromObject(result);
	}

	/* Calls To Do */
	private String[] getCallsToMake(Connection conn, int customerId, int userType) {
		List<String> list = new ArrayList<String>();
		list.add(ClientRequestConstants.SETTINGS_GET_STRING);
		if(userType == 3){ // Only Field Users
			list.add(ClientRequestConstants.DNB_DOC_LIST);
			list.add(ClientRequestConstants.DNB_ENQUIRY_GET);
		}
		String[] callsToDo = new String[list.size()];
		for(int i = 0; i < list.size(); i++){
			callsToDo[i] = list.get(i);
		}

		return callsToDo;
	}

	/* Update Handset details for User */
	private void updateHandsetDetailsUser(long userId, String userAgent, String clientVer, Connection con){
		gatewayDBAccessor.updateHandsetDetailsUser(userId, userAgent, clientVer, con);
	}

	/* Latitude validation & Conversion */
	private  String latValidate(String lat) {
		try {
			double LAT = Double.parseDouble(lat);
			final double MIN_LAT = -90;
			final double MAX_LAT = 90;
			final double SIX = 6.0;
			final double HALF = 0.5;
			final double RADIUS = 1000000.0;
			if(LAT >= MIN_LAT && LAT <= MAX_LAT && LAT != 0) {
				return lat;
			} else {
				int x = (int) (LAT / SIX + HALF);
				double r = ((double) x) / RADIUS;
				if(r == 0.0) {
					return "NA";
				} else {
					return String.valueOf(r);
				}
			}
		} catch(Exception e) {
			return "NA";
		}
	}

	/* Longitude validation & Conversion */
	private String lonValidate(String lon) {
		try {
			double LON = Double.parseDouble(lon);
			final int MIN_LON = -180;
			final int MAX_LON = 180;
			final double SIX = 6.0;
			final double HALF = 0.5;
			final double RADIUS = 1000000.0;
			if(LON >= MIN_LON && LON <= MAX_LON && LON != 0) {
				return lon;
			} else {
				int x = (int) (LON / SIX + HALF);
				double r = ((double) x) / RADIUS;
				if(r == 0.0) {
					return "NA";
				} else {
					return String.valueOf(r);
				}
			}
		} catch(Exception e) {
			return "NA";
		}
	}

	/* Generating Session Key */
	public String generateSessionId(int customerId, long userId,String loginFrom) {
		String sessionId = null;
		try {
			sessionId = gatewayDBAccessor.generateSessionId(customerId, userId,loginFrom);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		return sessionId;
	}

	/* User Settings */
	@Override
	public JSONObject getSettingsForUser(int customerId, long userId,String ts) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, String> settingsMap = new HashMap<String, String>();
		Connection conn = null;
		int errorCode = -1;
		try{
			conn = resourceManager.getConnection();
			errorCode = gatewayDBAccessor.getSettingsFromDB(conn, customerId, userId, settingsMap);
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(conn);
		}

		if(errorCode != -1) {
			resultMap.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.SUCCESS_SETTINGS);
			resultMap.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.SUCCESS_SETTINGS_MSG);
			resultMap.put(ClientRequestConstants.USER_ID, userId);
			resultMap.put(ClientRequestConstants.CUSTOMER_ID, customerId);
			resultMap.put(ClientRequestConstants.SETTINGS_RESPONSE_STRING, settingsMap);
		} else {
			resultMap.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.ERROR_SETTINGS);
			resultMap.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.ERROR_SETTINGS_MSG);
			resultMap.put(ClientRequestConstants.USER_ID, userId);
			resultMap.put(ClientRequestConstants.CUSTOMER_ID, customerId);
		}

		return JSONObject.fromObject(resultMap);
	}

	/* Fetch User Details */
	@Override
	public UserDto getUserDetail(long userId, Connection con){
		UserDto user = null;
		try{
			user = userDao.findByPrimaryKey(userId, con);
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		return user;
	}

	@Override
	public JSONObject updateCheckCall(long userId, int customerId){
		Connection conn = null;
		HashMap<String, Object> result = new HashMap<String, Object>();
		try{
			conn = resourceManager.getConnection();
			UserDto user = getUserDetail(userId, conn);
			boolean check = false;
			if(user != null && user.getForceSetupUpdate() == 1) check = true;

			if(check){
				List<String> callsToDo = new ArrayList<String>();
				Set<String> calls = userDao.getUserUpdateCalls(conn, customerId, userId);
				Iterator<String> iterator = calls.iterator(); 
				while (iterator.hasNext()){
					String call = ClientRequestConstants.returnCall(iterator.next());
					if(!call.equals("")) callsToDo.add(call);
				}

				result.put(ClientRequestConstants.CALLTOMAKE_STRING, callsToDo);
				result.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.SUCCESS_RESPONSE_CODE_GENERIC);
			}else{
				String[] callsToDo = {};
				result.put(ClientRequestConstants.CALLTOMAKE_STRING, callsToDo);
				result.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.SUCCESS_RESPONSE_CODE_GENERIC);
			}
		} catch (SQLException e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(conn);
		}
		return JSONObject.fromObject(result);
	}

	@Override
	public JSONObject getDNBDocCheckList(){
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		Connection conn = null;
		boolean check = false;
		try{
			conn=resourceManager.getConnection();
			check = gatewayDBAccessor.getDNBDocCheckList(conn,resultMap);
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(conn);
		}
		if(check){
			resultMap.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.SUCCESS_DNB_DOC_DET);
			resultMap.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.SUCCESS_DNB_DOC_MSG);
		}else{
			resultMap.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.ERROR_DNB_DOC_DET);
			resultMap.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.ERROR_DNB_DOC_MSG);
		}
		return JSONObject.fromObject(resultMap);
	}

	/* Get DNB Enquiries */
	@Override
	public JSONObject getEnquiryForUser(int customerId, long userId, boolean isDownload) throws IOException{
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		Connection conn = null;
		try{
			conn = resourceManager.getConnection();
			ArrayList<HashMap<String, Object>> enquiryList = gatewayDBAccessor.getEnquiriessForUser(conn, customerId, userId, isDownload);
			resultMap.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.SUCCESS_TASKS);
			resultMap.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.SUCCESS_TASKS_MSG);
			resultMap.put(ClientRequestConstants.USER_ID, userId);
			resultMap.put(ClientRequestConstants.CUSTOMER_ID, customerId);
			resultMap.put(ClientRequestConstants.WORKFLOWS_RESPONSE_STRING, enquiryList);
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			resultMap.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.ERROR_TASKS);
			resultMap.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.ERROR_TASKS_MSG);
			resultMap.put(ClientRequestConstants.USER_ID, userId);
			resultMap.put(ClientRequestConstants.CUSTOMER_ID, customerId);
		}finally{
			resourceManager.close(conn);
		}
		return JSONObject.fromObject(resultMap);
	}

	/* Get Removed Enquiries List */
	@Override
	public JSONObject getRemovedEnquiryNos(int customerId, long userId) throws IOException{
		PreparedStatement pstmt = null;
		JSONObject jObj = new JSONObject();
		Connection conn = null;
		JSONArray removedEnquiryArray = new JSONArray();
		//String ids = "";
		try{
			conn = resourceManager.getConnection();
			removedEnquiryArray = gatewayDBAccessor.getRemovedEnquiryNos(conn, userId);
			jObj.put(ClientRequestConstants.DNB_REMOVED_ENQUIRY_DATA, removedEnquiryArray);
			jObj.put(ClientRequestConstants.USER_ID, userId);
			jObj.put(ClientRequestConstants.CUSTOMER_ID, customerId);
			/*if(removedEnquiryArray != null && removedEnquiryArray.size() > 0){
				for(int i = 0 ; i < removedEnquiryArray.size();i++){
					JSONObject tempObj = removedEnquiryArray.getJSONObject(i);
					if(ids != "") ids += ",";
					ids += tempObj.getLong("asId");
				}
			}
			if(ids != ""){
				String sql = "update dnb_enquiry_details set is_deleted = 2 where find_in_set (id,'"+ids+"')";
				pstmt = conn.prepareStatement(sql);
				pstmt.executeUpdate();
			}*/
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(pstmt);
			resourceManager.close(conn);
		}
		return jObj;
	}

	/* Header Validation */
	@Override
	public void validateHeader(String protocolId, String clientVersion, String callType, String callName) throws ProtocolMismatchException {
		if(protocolId == null || clientVersion == null || callType == null || callName == null) {
			throw new ProtocolMismatchException("Invalid protocolId or clientVersion or callType or callName");
		}
		validateProtocolId(protocolId);
		validateClientVersion(clientVersion);
		validateCallType(callType);
		validateCallName(callName);
	}

	/* CallType Validation */
	@Override
	public void validateCallType(String callType) throws ProtocolMismatchException {
		List<String> callTypeList = new ArrayList<String>();
		callTypeList.add(ClientRequestConstants.GET_CALL_TYPE);
		callTypeList.add(ClientRequestConstants.SEND_CALL_TYPE);
		if(!callTypeList.contains(callType)) throw new ProtocolMismatchException(ClientRequestConstants.ERROR_INVALID_CALLTYPE_MSG);
	}

	/* CallName Validation */
	@Override
	public void validateCallName(String callName) throws ProtocolMismatchException {
		List<String> callNameList = new ArrayList<String>();
		callNameList.add(ClientRequestConstants.LOGIN_GET);
		callNameList.add(ClientRequestConstants.SETTINGS_GET_STRING);
		callNameList.add(ClientRequestConstants.UPDATE_GET);
		callNameList.add(ClientRequestConstants.EVENT_SEND);
		callNameList.add(ClientRequestConstants.ACKNOWLEDGEMENT);
		callNameList.add(ClientRequestConstants.MEDIA_SYNC);
		callNameList.add(ClientRequestConstants.DNB_DOC_LIST);
		callNameList.add(ClientRequestConstants.DNB_ENQUIRY_GET);
		callNameList.add(ClientRequestConstants.DNB_ENQUIRY_REGET);
		callNameList.add(ClientRequestConstants.DNB_EVENT_DETAILS);
		callNameList.add(ClientRequestConstants.DNB_DOC_MEDIA_SYNC);
		callNameList.add(ClientRequestConstants.EVENT_TRACKING);
		callNameList.add(ClientRequestConstants.EVENT_APP_EXIT);
		callNameList.add(ClientRequestConstants.DNB_REMOVED_ENQUIRY_LIST);
		if(!callNameList.contains(callName)) throw new ProtocolMismatchException(ClientRequestConstants.ERROR_INVALID_CALLNAME_MSG);
	}

	/* Client Version Validation */
	@Override
	public void validateClientVersion(String clientVersion) throws ProtocolMismatchException {
		//If clientVersion is not in a current set of clientVersions then
		//throw new ProtocolMismatchException("Invalid clientVersion");
	}

	/* Protocol Validation */
	@Override
	public void validateProtocolId(String protocolId) throws ProtocolMismatchException {
		//If protocol Id is not in a current set of protocol identifiers then
		//throw new ProtocolMismatchException("Invalid protocolId");
	}

	/* Update Check */
	private JSONObject getRequestStatus(int customerId, long userId, String codeString, String message, Connection con, Object ivj){
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put(ClientRequestConstants.RESPONSE_CODE_STRING, codeString);
		resultMap.put(ClientRequestConstants.MESSAGE_STRING, message);
		resultMap.put(ClientRequestConstants.CUSTOMER_ID, customerId);
		UserDto user = getUserDetail(userId, con);
		if(user != null && user.getStatus()==1){
			if(ivj != null && ivj instanceof JSONArray) {
				resultMap.put("ivj", (JSONArray)ivj);
			}
			boolean isSetupUpdateRequired = false;
			if(user.getForceSetupUpdate() == 1) isSetupUpdateRequired = true;
			if(isSetupUpdateRequired){
				List<String> callsToDo = new ArrayList<String>();
				if(user.getForceSetupUpdate() == 1) {
					Set<String> calls = userDao.getUserUpdateCalls(con, customerId, userId);
					Iterator<String> iterator = calls.iterator(); 
					while (iterator.hasNext()){
						String call = ClientRequestConstants.returnCall(iterator.next());
						if(!call.equals("")) callsToDo.add(call);
					}
				}
				resultMap.put(ClientRequestConstants.CALLTOMAKE_STRING, callsToDo);
			}
		}else{
			resultMap.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.ERROR_USER_INACTIVE_MSG);
		}
		return JSONObject.fromObject(resultMap);
	}
}