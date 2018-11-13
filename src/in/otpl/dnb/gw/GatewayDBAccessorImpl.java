package in.otpl.dnb.gw;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import in.otpl.dnb.enquiryPool.EnquiryDao;
import in.otpl.dnb.enquiryPool.EnquiryDto;
import in.otpl.dnb.settings.PhoneConfigurationDao;
import in.otpl.dnb.settings.PhoneConfigurationDto;
import in.otpl.dnb.user.UserDao;
import in.otpl.dnb.user.UserDto;
import in.otpl.dnb.util.ClientRequestConstants;
import in.otpl.dnb.util.ErrorLogHandler;
import in.otpl.dnb.util.PlatformUtil;
import in.otpl.dnb.util.ResourceManager;
import in.otpl.dnb.util.ServerConstants;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Repository
public class GatewayDBAccessorImpl implements GatewayDBAccessor{

	private static final Logger LOG = Logger.getLogger(GatewayDBAccessorImpl.class);

	@Autowired
	private ResourceManager resourceManager;
	@Autowired
	private TrackingDao trackingDao;
	@Autowired
	private CurrentUserTrackingDao currentUserTrackingDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private AppSessionDao appSessionDao;
	@Autowired
	private PhoneConfigurationDao phoneConfigurationDao;
	@Autowired
	private EnquiryDao enquiryDao;

	@Override
	public int enquiryIsDeletedOrExist(Connection conn,long assignmentId,long userId,int customerId){
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		int isDeleted = 0;
		try{
			String SQL = "select is_deleted from dnb_enquiry_details where id = "+assignmentId+" and user_id = "+userId+" and customer_id = "+customerId+"";
			pstmt = conn.prepareStatement(SQL);
			rst = pstmt.executeQuery();
			if(rst.next()){
				isDeleted = rst.getInt("is_deleted");
			}
		}catch (Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(rst);
			resourceManager.close(pstmt);
		}
		return isDeleted;
	}

	@Override
	public void signatureDataAlreadyExist(Connection con, int docId,int seqId,long dnbMasterId,String uKey)  {
		PreparedStatement pstmt = null, dPstmt = null;
		ResultSet rst = null;
		try{
			String sql = "select id from dnb_workflow_media where dnb_master_id = "+dnbMasterId+" and unique_key = '"+uKey+"' and seq_id = "+seqId+" and doc_id = "+docId+" ";
			pstmt = con.prepareStatement(sql);
			rst = pstmt.executeQuery();
			if(rst.next()){
				String dSQL = "delete from dnb_workflow_media where dnb_master_id = "+dnbMasterId+" and unique_key = '"+uKey+"' and doc_id = "+docId+" and seq_id = "+seqId+" ";
				dPstmt = con.prepareStatement(dSQL);
				dPstmt.executeUpdate();
			}
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(dPstmt);
			resourceManager.close(rst);
			resourceManager.close(pstmt);
		}
	}
	@Override
	public long createMediaFileIdDNB(Connection con, int customerId, long userId, String uniqueKey, String mediaType, int seqId,int docId,long asId,String enquiryNo,long dnbMasterId,int isLastImage)  {
		long mediaId = 0;
		String insertSql = "insert into dnb_workflow_media (customer_id,user_id,doc_id,as_id,enquiry_id,unique_key,media_type,seq_id,created_time,modified_time,action_id,dnb_master_id,is_last_image)"
				+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?) ";
		String sql = "select max(id) as max_id from dnb_workflow_media ";
		PreparedStatement pstmt = null, pstmt2 = null;
		ResultSet rs2 = null;
		try{
			String currentTime = PlatformUtil.getRealDateToSQLDateTime(new Date());
			pstmt = con.prepareStatement(insertSql);
			pstmt.setInt(1, customerId);
			pstmt.setLong(2, userId);
			pstmt.setInt(3, docId);
			pstmt.setLong(4, asId);
			pstmt.setString(5, enquiryNo);
			pstmt.setString(6, uniqueKey);
			pstmt.setString(7, mediaType);
			pstmt.setInt(8, seqId);
			pstmt.setString(9, currentTime);
			pstmt.setString(10, currentTime);
			pstmt.setInt(11, 1);
			pstmt.setLong(12, dnbMasterId);
			pstmt.setInt(13, isLastImage);
			int rows = pstmt.executeUpdate();
			if(rows > 0){
				pstmt2 = con.prepareStatement(sql);
				rs2 = pstmt2.executeQuery();
				if(rs2.next()) mediaId = rs2.getLong("max_id");
			}
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(rs2);
			resourceManager.close(pstmt2);
			resourceManager.close(pstmt);
		}
		return mediaId;
	}

	@Override
	public int dnbMediaFilePathUpdate(Connection con, long mediaId, String dataKey, String dataPath,String mediaLink){
		int row = 0;
		PreparedStatement pstmt = null;
		String sql = "update dnb_workflow_media set media_key = ?, media_path = ? where id = ? ";
		try{
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, dataKey);
			pstmt.setString(2, dataPath);
			pstmt.setLong(3, mediaId);
			row = pstmt.executeUpdate();
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(pstmt);
		}
		return row;
	}

	@Override
	public int updateSVRSigImgCount(Connection con, int docId,long dnbMasterId){
		int row = 0,svrImgCount = 0;
		PreparedStatement pstmt = null,pstmt1=null;
		ResultSet rst = null;
		try{
			String getSQL = "select svr_img_count from dnb_master_data where dnb_master_id = "+dnbMasterId+"";
			pstmt = con.prepareStatement(getSQL);
			rst = pstmt.executeQuery();
			if(rst.next()){
				svrImgCount = rst.getInt("svr_img_count");
				if(svrImgCount < 2){
					if(svrImgCount > 0) svrImgCount++;
					else svrImgCount =1;

					String sql = "update dnb_master_data set svr_img_count = "+svrImgCount+" where dnb_master_id = "+dnbMasterId+"";
					pstmt1 = con.prepareStatement(sql);
					row = pstmt1.executeUpdate();
				}
			}
		}catch (Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(pstmt1);
			resourceManager.close(rst);
			resourceManager.close(pstmt);
		}
		return row;
	}

	@Override
	public void updateSVRDataDetails(Connection con, int docId,long dnbMasterId,int seqId,String mediaLink,int customerId){
		PreparedStatement pstmt = null;
		try{
			String SQL = "";
			if(seqId == 1)
				SQL = "update dnb_svr_details set p_sig = ? where dnb_master_id = "+dnbMasterId+" and customer_id = "+customerId+"";
			else if(seqId == 2)
				SQL = "update dnb_svr_details set c_sig = ? where dnb_master_id = "+dnbMasterId+" and customer_id = "+customerId+"";
			pstmt = con.prepareStatement(SQL);
			pstmt.setString(1, mediaLink);
			pstmt.executeUpdate();
		}catch (Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(pstmt);
		}
	}

	@Override
	public boolean checkEventExist(Connection conn, String uniqueKey,long dnbMasterId){
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		boolean dataExist = false;
		try{
			String sql = "select unique_key from dnb_master_data where unique_key = '"+uniqueKey+"' and dnb_master_id = "+dnbMasterId+"";
			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();
			if(rst.next()){
				dataExist = true;
			}
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(rst);
			resourceManager.close(pstmt);
		}
		return dataExist;
	}

	@Override
	public  Map<String, Integer> insertOrUpdateDNBData(Connection conn,int customerId,long assignmentId,String enquiryId,long uId,String svrData,String ccamData,String docData,
			int imgCount,String uniqueKey,String docIds,long dnbMasterId,String ccamComments,String isInsert,String clientTime){
		PreparedStatement pstmt = null;
		int rowId = 0;
		String sql = "";
		Map<String, Integer> map = new HashMap<String,Integer>();
		try{
			int isDeleted = enquiryIsDeletedOrExist(conn,assignmentId,uId,customerId); // check if the case is assigned to same user or not,if Yes allow insert/update or else not allow 
			if(isDeleted == 1 || isDeleted == 2){ // Already Reassigned to someone else , ignore this data and send success to mobile application for direct remove this enquiry from pool
				LOG.info("Enquiry has been assigned to some other user ");
				map.put("is_deleted", isDeleted);
				map.put("rowId", 0);
			}else if(isDeleted == 0){
				if(isInsert.equalsIgnoreCase("insert")){
					LOG.info("Insert data to dnb_master_data table ");
					sql = "insert into dnb_master_data (customer_id,created_time,modified_time,assignment_id,enquiry_id,"
							+"user_id,svr_data,ccam_data,doc_data,img_count,unique_key,doc_ids,dnb_master_id,ccam_comments) "
							+"values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, customerId);
					pstmt.setString(2, PlatformUtil.getRealDateToSQLDateTime(new Date()));
					pstmt.setString(3, PlatformUtil.getRealDateToSQLDateTime(new Date()));
					pstmt.setLong(4, assignmentId);
					pstmt.setString(5, enquiryId);
					pstmt.setLong(6, uId);
					pstmt.setString(7, svrData);
					pstmt.setString(8, ccamData);
					pstmt.setString(9, docData);
					pstmt.setInt(10, imgCount);
					pstmt.setString(11, uniqueKey);
					pstmt.setString(12, docIds);
					pstmt.setLong(13, dnbMasterId);
					pstmt.setString(14, ccamComments);
					rowId = pstmt.executeUpdate();
				}else{ // update
					LOG.info("Update data to dnb_master_data table ");
					sql = "update dnb_master_data set svr_data = ? , ccam_data = ? , doc_data = ?, user_id=?,ccam_comments=?,"
							+"created_time =?, modified_time =?, assignment_id= ? , pdf_link='' , is_data_distributed = 0 , img_count = ? , is_image_linked = 0 "
							+"where unique_key = ? and dnb_master_id = ?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, svrData);
					pstmt.setString(2, ccamData);
					pstmt.setString(3, docData);
					pstmt.setLong(4, uId);
					pstmt.setString(5, ccamComments);
					pstmt.setString(6, PlatformUtil.getRealDateToSQLDateTime(new Date()));
					pstmt.setString(7, PlatformUtil.getRealDateToSQLDateTime(new Date()));
					pstmt.setLong(8, assignmentId);
					pstmt.setInt(9, imgCount);
					pstmt.setString(10, uniqueKey);
					pstmt.setLong(11, dnbMasterId);
					rowId = pstmt.executeUpdate();
				}
				map.put("is_deleted", isDeleted);
				map.put("rowId", rowId);
			}
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			map.put("is_deleted", 2);
			map.put("rowId", rowId);
		}finally{
			resourceManager.close(pstmt);
		}
		LOG.info("MAP data from insertUpdate Method : "+map);
		return map;
	}

	@Override
	public int updateEnquiryStatus(Connection con,long timeDiff,long assignmentId){
		int rowId = 0;
		PreparedStatement pstmt = null;
		try{
			String updateSQL = "update dnb_enquiry_details set status = ?, modified_time = ? , submission_time_diff = ? where id = ? ";
			pstmt = con.prepareStatement(updateSQL);
			pstmt.setInt(1, 2);
			pstmt.setString(2, PlatformUtil.getRealDateToSQLDateTime(new Date()));
			pstmt.setLong(3, timeDiff);
			pstmt.setLong(4, assignmentId);
			rowId = pstmt.executeUpdate();
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(pstmt);
		}
		return rowId;
	}

	@Override
	public int updateAssignmentPoolStatus(Connection con,long dnbMasterId){
		int rowId = 0;
		PreparedStatement pstmt = null;
		try{
			String updateASPSQL = "update dnb_assignment_pool set case_status = 0, modified_time = ? where dnb_master_id = ?";
			pstmt = con.prepareStatement(updateASPSQL);
			pstmt.setString(1, PlatformUtil.getRealDateToSQLDateTime(new Date()));
			pstmt.setLong(2, dnbMasterId);
			rowId = pstmt.executeUpdate();
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(pstmt);
		}
		return rowId;
	}
	@Override
	public int updateDnbAssignmentPoolHistory(Connection con,long asphId,long dnbMasterId){
		int rowId = 0;
		PreparedStatement pstmt = null;
		try{
			String updateSQL = "update dnb_assignment_pool_history set is_processed = ?, modified_time = ? where id = ? and dnb_master_id = ? ";
			pstmt = con.prepareStatement(updateSQL);
			pstmt.setInt(1, 2); // 2 means completed and received from mobile
			pstmt.setString(2, PlatformUtil.getRealDateToSQLDateTime(new Date()));
			pstmt.setLong(3, asphId);
			pstmt.setLong(4,dnbMasterId);
			rowId = pstmt.executeUpdate();
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(pstmt);
		}
		return rowId;
	}

	@Override
	public void deleteOldSVRAndCCAMData(Connection conn,long dnbMasterId){
		PreparedStatement svrpstmt = null,gpstmt = null,buspstmt = null,emppstmt = null,ppstmt = null,spstmt=null,
				cpstmt=null,sepstmt=null,bipstmt=null,suppstmt=null,tpstmt=null,opstmt=null,fpstmt=null;
		try{
			String svrSQL = "delete from dnb_svr_details where dnb_master_id = "+dnbMasterId+"";
			String ccamGSQL = "delete from dnb_ccam_general_data where dnb_master_id = "+dnbMasterId+"";
			String ccamBusSQL = "delete from dnb_ccam_buisness_data where dnb_master_id = "+dnbMasterId+"";
			String ccamEmpSQL = "delete from dnb_ccam_emp_data where dnb_master_id = "+dnbMasterId+"";
			String ccamPurSQL = "delete from dnb_ccam_purchase_data where dnb_master_id = "+dnbMasterId+"";
			String ccamSalesSQL = "delete from dnb_ccam_sales_data where dnb_master_id = "+dnbMasterId+"";
			String ccamCustSQL = "delete from dnb_customer_data where dnb_master_id = "+dnbMasterId+"";
			String ccamSeaSQL = "delete from dnb_seasonal_data where dnb_master_id = "+dnbMasterId+"";
			String ccamBiSQL = "delete from dnb_ccam_bi_data where dnb_master_id = "+dnbMasterId+"";
			String ccamSupSQL = "delete from dnb_ccam_supplier_data where dnb_master_id = "+dnbMasterId+"";
			String ccamTaxSQL = "delete from dnb_ccam_tax_data where dnb_master_id = "+dnbMasterId+"";
			String ccamOrgSQL = "delete from dnb_ccam_org_data where dnb_master_id = "+dnbMasterId+"";
			String ccamFinSQL = "delete from dnb_ccam_financial_data where dnb_master_id = "+dnbMasterId+"";

			svrpstmt =conn.prepareStatement(svrSQL);
			gpstmt =conn.prepareStatement(ccamGSQL);
			buspstmt =conn.prepareStatement(ccamBusSQL);
			emppstmt =conn.prepareStatement(ccamEmpSQL);
			ppstmt =conn.prepareStatement(ccamPurSQL);
			spstmt =conn.prepareStatement(ccamSalesSQL);
			cpstmt =conn.prepareStatement(ccamCustSQL);
			sepstmt =conn.prepareStatement(ccamSeaSQL);
			bipstmt =conn.prepareStatement(ccamBiSQL);
			suppstmt =conn.prepareStatement(ccamSupSQL);
			tpstmt =conn.prepareStatement(ccamTaxSQL);
			opstmt =conn.prepareStatement(ccamOrgSQL);
			fpstmt =conn.prepareStatement(ccamFinSQL);

			svrpstmt.executeUpdate();
			gpstmt.executeUpdate();
			buspstmt.executeUpdate();
			emppstmt.executeUpdate();
			ppstmt.executeUpdate();
			spstmt.executeUpdate();
			cpstmt.executeUpdate();
			sepstmt.executeUpdate();
			bipstmt.executeUpdate();
			suppstmt.executeUpdate();
			tpstmt.executeUpdate();
			opstmt.executeUpdate();
			fpstmt.executeUpdate();
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(fpstmt);
			resourceManager.close(opstmt);
			resourceManager.close(tpstmt);
			resourceManager.close(suppstmt);
			resourceManager.close(bipstmt);
			resourceManager.close(sepstmt);
			resourceManager.close(cpstmt);
			resourceManager.close(spstmt);
			resourceManager.close(ppstmt);
			resourceManager.close(emppstmt);
			resourceManager.close(buspstmt);
			resourceManager.close(gpstmt);
			resourceManager.close(svrpstmt);
		}
	}

	@Override
	public long insertLocData(String clientTime, String lat, String lon, String alt, String speed, String direction,
			String accuracy, String eventName, int cId, long uId, int isValid, int isCellsite,
			String cellSiteLocAddress, Connection con, String status)  {
		long trackingId = 0;
		TrackingDto t = new TrackingDto();
		t.setAccuracy(accuracy);
		t.setSpeed(speed);
		t.setAltitude(alt);
		t.setCustomerId(cId);
		t.setUserId(uId);
		t.setEventName(eventName);
		t.setClientTime(clientTime);
		t.setDirection(direction);
		t.setLattitude(lat);
		t.setLongitude(lon);
		t.setClientTime(clientTime);
		t.setIsValid(isValid);
		t.setIsCellsite((short)isCellsite);
		t.setCellsiteLocAddres(cellSiteLocAddress);
		t.setStatus(status);
		trackingId  = trackingDao.insert(t, con);
		t.setId(trackingId);
		CurrentUserTracking cut = new CurrentUserTracking();
		cut.setAccuracy(accuracy);
		cut.setAltitude(alt);
		cut.setCustomerId(new Integer(cId));
		cut.setUserId(uId);
		cut.setLattitude(lat);
		cut.setLongitude(lon);
		cut.setModificationTime(clientTime);
		cut.setIsValid(isValid);
		cut.setIsCellsite((short)isCellsite);
		CurrentUserTracking ctpk = new CurrentUserTracking();
		if(uId != 0 && cId != 0){
			CurrentUserTracking customerTrackingUser = validateTrackingUserId(uId, cId, con);
			if(customerTrackingUser != null){
				if((cut.getUserId() == customerTrackingUser.getUserId()) && (cut.getCustomerId() == customerTrackingUser.getCustomerId())){
					ctpk.setId(customerTrackingUser.getId());
					cut.setCreationTime(customerTrackingUser.getCreationTime());
					currentUserTrackingDao.update(customerTrackingUser.getId(), cut, con);
				}
			}else{
				currentUserTrackingDao.insert(cut, con);
			}
		}

		/* update last_tracking table */
		try{
			updateLastTrackingInfo(trackingId, uId, isValid, isCellsite, cId, con);
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		return trackingId;
	}

	@Override
	public CurrentUserTracking validateTrackingUserId(long userid, int customerId, Connection con)  {
		CurrentUserTracking user = null;
		try {
			CurrentUserTracking[] users = currentUserTrackingDao.findByDynamicWhere("t.user_id = ? and t.customer_id = ? ", new Object[] { userid, customerId }, con);
			if(users != null && users.length > 0){
				user = users[0];
			}
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			throw e;
		}
		return user;
	}

	@Override
	public void updateLastTrackingInfo(long trackingId, long uId, int isValid, int isCellsite,int cId, Connection con){
		LastTrackingDto lastTracking = new LastTrackingDto();
		lastTracking.setUserId(uId);
		lastTracking.setTrackingId(trackingId);
		lastTracking.setCustomerId(cId);
		if(isValid == 1){
			lastTracking.setValidTrackingId(trackingId);
		}
		if(isCellsite == 0){
			lastTracking.setNonCellsiteTrackingId(trackingId);
		}
		currentUserTrackingDao.lastTracking(lastTracking, con);
	}

	@Override
	public UserDto getUserIdFromDB(int customerId, String userLoginId, String password, Connection con) {
		UserDto user = null;
		try {
			String query = "WHERE u.customer_id = ? and u.status = 1 and u.user_type_id = 3 " +
					" and u.login_name = ? and u.password = ? " ;

			final int ARRAY_SIZE = 3;
			Object[] params = new Object[ARRAY_SIZE];
			params[0] = customerId;
			params[1] = userLoginId;
			params[2] = PlatformUtil.sha(password);
			UserDto[] users = userDao.findByDynamicCondition(query, params, con);
			if(users != null && users.length >0){
				user = users[0];
			}
		} catch(Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		return user;
	}

	@Override
	public int updateHandsetDetailsUser(long userId, String handsetDetails, String clientVer, Connection conn){
		int rows = 0;
		try{
			rows = userDao.updateHandsetDetailUser(userId, handsetDetails, clientVer, conn);
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		return rows;
	}

	@Override
	public String generateSessionId(int customerId, long userId,String loginFrom) {
		Connection conn = null;
		final int DIGITS = 6;
		String key = String.valueOf(PlatformUtil.generateRandomNumber(DIGITS));
		try{
			conn = resourceManager.getConnection();
			int rowId = appSessionDao.getSessionKey(customerId, userId, conn);
			appSessionDao.insertUpdateSessionKey(conn, rowId, key, customerId, userId,loginFrom);
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			key = null;
		}finally{
			resourceManager.close(conn);
		}
		return key;
	}

	@Override
	public int getSettingsFromDB(Connection con, int customerId, long userId, HashMap<String, String> settingsMap) {
		try {
			Object[] params = new Object[1];
			String query = " customer_id = ?";
			params[0] = customerId;
			PhoneConfigurationDto[] phoneConfigurationArray = phoneConfigurationDao.findByDynamicWhere(query, params, con);
			if(phoneConfigurationArray.length < 1) {
				return -1;
			}

			for(int i=0; i<phoneConfigurationArray.length; i++) {
				PhoneConfigurationDto p = phoneConfigurationArray[i];
				String key = p.getParamName();
				String value = String.valueOf(p.getIntParamValue());
				if(key.equals(ServerConstants.PHONE_CONFIGURATION_SUPPORT_NUMBER))
					value = p.getStringParamValue();
				settingsMap.put(key, value);
			}

		} catch(Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			return -1;
		}
		return 0;
	}

	@Override
	public boolean getDNBDocCheckList(Connection conn,HashMap<String, Object> resultMap) {
		List<HashMap<String, Object>> dnbCheckList = new ArrayList<HashMap<String, Object>>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean check = false;
		try{
			String sql = "select doc_id,doc_description from dnb_doc_checklist";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("docId", rs.getString("doc_id"));
				map.put("docDes", rs.getString("doc_description"));
				dnbCheckList.add(map);
			}
			if(dnbCheckList.size() > 0){
				check = true;
			}
		}catch(Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(rs);
			resourceManager.close(pstmt);
		}
		resultMap.put(ClientRequestConstants.DNB_DOC_CHECK_DETAILS, dnbCheckList);
		return check;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getEnquiriessForUser(Connection conn, int customerId, long userId, boolean isDownload){
		ArrayList<HashMap<String, Object>> enquiryList = new ArrayList<HashMap<String, Object>>();
		try{
			String sql = " ded.customer_id= ? and ded.user_id = ? and ded.status = 1 ";
			if(isDownload) 
				sql += " and ded.is_downloaded = 0 ";
			final int ARRAY_SIZE = 2;
			Object[] sqlParams = new Object[ARRAY_SIZE];
			sqlParams[0] = customerId;
			sqlParams[1] = userId;
			EnquiryDto[] assignments = enquiryDao.getEnquiriesForUser(sql, sqlParams, conn);
			for(EnquiryDto assignment: assignments){
				HashMap<String, Object> assignmentDefn = new HashMap<String, Object>();
				assignmentDefn.put("asId", assignment.getId()); // dnb_enquiry_details id column
				assignmentDefn.put("assgnName",assignment.getAssignmentName());
				assignmentDefn.put("text", assignment.getText());
				assignmentDefn.put("scheduleStartTime", assignment.getScheduledStartTime());
				assignmentDefn.put("scheduleEndTime", assignment.getScheduledStartTime());
				assignmentDefn.put("status", assignment.getStatus());
				assignmentDefn.put("typeId", assignment.getTypeId());
				assignmentDefn.put("date_of_site_visit", assignment.getDateOfSiteVisit());
				assignmentDefn.put("entity_company_name", assignment.getEntityCompanyName());
				assignmentDefn.put("entity_address", assignment.getEntityAddress());
				assignmentDefn.put("contact_person_name", assignment.getContactPersonName());
				assignmentDefn.put("contact_number", assignment.getContactNumber());
				assignmentDefn.put("case_type", assignment.getCaseType());
				if(assignment.getCaseType() != null && assignment.getCaseType().equalsIgnoreCase("Non Buy in")){
					assignment.setCustomerCRNNumber("");
					assignment.setCorporateName("");
				}
				assignmentDefn.put("customer_reference_number", assignment.getCustomerCRNNumber());
				assignmentDefn.put("corporate_name", assignment.getCorporateName());
				assignmentDefn.put("case_id", assignment.getCaseId());
				assignmentDefn.put("enquiry_id", assignment.getEnquiryNo());
				assignmentDefn.put("case_type", assignment.getCaseType());
				assignmentDefn.put("assignment_pool_id", assignment.getAspId());
				assignmentDefn.put("asph_id", assignment.getAsphId());
				assignmentDefn.put("dnbMasterId", assignment.getDnbMasterId());
				long asId = assignment.getId();
				long dnbMasterId = assignment.getDnbMasterId(); 
				String enquiryNo = assignment.getEnquiryNo();

				HashMap<String, String> map = getDocumentIds(conn,assignment.getCaseId(),dnbMasterId,enquiryNo,asId);
				assignmentDefn.put("doc_id", map.get("docIds"));
				assignmentDefn.put("uKey", map.get("uKey"));
				if(assignment.getDateOfSiteVisit() != null && !assignment.getDateOfSiteVisit().equals("")){
					enquiryList.add(assignmentDefn);
				}
				/*if(assignmentIds != ""){
					assignmentIds += ",";
				}
				assignmentIds += assignment.getId();*/
			}
			/*if(assignmentIds != ""){
				enquiryDao.updateEnquiryDownloadStatus(conn, assignmentIds);
			}*/
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		return enquiryList;
	}
	@Override
	public JSONArray getRemovedEnquiryNos(Connection conn, long userId){
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		JSONArray jArray = new JSONArray();
		try{
			String sql = "select id asId, dnb_master_id from dnb_enquiry_details where is_deleted = 1 and user_id = "+userId+"";
			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();
			while(rst.next()){
				JSONObject jObj = new JSONObject();
				jObj.put("asId", rst.getLong("asId"));
				jObj.put("dnb_master_id", rst.getLong("dnb_master_id"));
				jArray.add(jObj);
			}
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(rst);
			resourceManager.close(pstmt);
		}
		return jArray;
	}
	
	@Override
	public void resetSessionKeyToNull(long userId){
		Connection conn = null;
		try{
			conn = resourceManager.getConnection();
			appSessionDao.resetSessionKeyToNull(userId, conn);
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(conn);
		}
	}

	@Override
	public HashMap<String, String> getDocumentIds(Connection conn,int caseId,long dnbMasterId,String enquiryNo,long asId){
		String docIds="",uKey="";
		HashMap<String,String> map = new HashMap<String, String>();
		PreparedStatement pstmt=null,pstmt1=null,pstmt2=null ,wpstmt=null,mpstmt=null,ccampstmt=null,svrpstmt=null,svrCpstmt=null,upstmt=null;
		ResultSet rst=null,rst1=null,rst2=null;
		int isRejectedAlready = 0;
		String sql = "", wmSQL = "",dmSQL="";
		try{
			sql = "select case_status from dnb_assignment_pool where dnb_master_id = "+dnbMasterId+"" ;
			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();
			if(rst.next()){
				isRejectedAlready = rst.getInt("case_status"); 
			}

			if(isRejectedAlready == 2){
				wmSQL = "select doc_id,unique_key from dnb_workflow_media where enquiry_id = '"+enquiryNo+"' and dnb_master_id = "+dnbMasterId+" and app_rej = 2 and is_deleted = 0";
				pstmt1 = conn.prepareStatement(wmSQL);
				rst1 = pstmt1.executeQuery();

				while(rst1.next()){
					if(PlatformUtil.isContain(docIds, rst1.getString("doc_id"))){

					}else{
						if(docIds != ""){
							docIds+=",";
						}
						docIds += rst1.getString("doc_id");
					}
					if(uKey.equals("")) uKey = rst1.getString("unique_key");
				}
				map.put("docIds",docIds);
				map.put("uKey",uKey);
				if(!docIds.equals("")){
					String temp[] = docIds.split(",");
					String otherIds = "";
					for(int i = 0 ; i < temp.length ; i++){
						if(!temp[i].equals("1") && !temp[i].equals("2")){
							if(otherIds!="") otherIds+=",";
							otherIds += temp[i];
						}
					}
					if(!otherIds.equals("")){
						String workFlowSQL = "update dnb_workflow_media set is_deleted = 1 , as_id = ? where unique_key = ? and dnb_master_id = ? and doc_id not in (1,2)";
						wpstmt = conn.prepareStatement(workFlowSQL);
						wpstmt.setLong(1, asId);
						wpstmt.setString(2, uKey);
						wpstmt.setLong(3, dnbMasterId);
						int rowId = wpstmt.executeUpdate();
						if(rowId > 0 ) 
						LOG.info("All Old document marked deleted successfully");
						String masterSQL= "update dnb_master_data set img_count = 0 where unique_key = ? and dnb_master_id = ?";
						mpstmt = conn.prepareStatement(masterSQL);
						mpstmt.setString(1, uKey);
						mpstmt.setLong(2, dnbMasterId);
						int msRowId = mpstmt.executeUpdate();
						if(msRowId > 0 ) LOG.info("Old Main Image Count updated to 0 for dnbMasterId "+dnbMasterId);
					}

					String uSQL = "update dnb_workflow_media set as_id = ?,is_last_image = 0 where unique_key = ? and dnb_master_id = ?";
					upstmt = conn.prepareStatement(uSQL);
					upstmt.setLong(1, asId);
					upstmt.setString(2, uKey);
					upstmt.setLong(3, dnbMasterId);
					upstmt.executeUpdate();
				}

				if(PlatformUtil.isContain(docIds, "1")){
					String ccamSQL = "update dnb_workflow_media set is_deleted = 1, as_id = ? where doc_id = 1 and unique_key = ? and dnb_master_id = ? ";
					ccampstmt = conn.prepareStatement(ccamSQL);
					ccampstmt.setLong(1, asId);
					ccampstmt.setString(2, uKey);
					ccampstmt.setLong(3, dnbMasterId);
					ccampstmt.executeUpdate();
					LOG.info("CCAM Document also marked deleted successfully");
				}

				if(PlatformUtil.isContain(docIds, "2")){
					String svrSQL = "update dnb_workflow_media set is_deleted = 1, as_id =? where doc_id = 2 and unique_key = ? and dnb_master_id = ?";
					svrpstmt = conn.prepareStatement(svrSQL);
					svrpstmt.setLong(1, asId);
					svrpstmt.setString(2, uKey);
					svrpstmt.setLong(3, dnbMasterId);
					svrpstmt.executeUpdate();
					LOG.info("SVR Document also marked deleted successfully");

					String svrCSQL = "update dnb_master_data set svr_img_count = 0, img_count = 0 where dnb_master_id = "+dnbMasterId+"";
					svrCpstmt = conn.prepareStatement(svrCSQL);
					int svRowId = svrCpstmt.executeUpdate();;
					if(svRowId > 0 ) LOG.info("Old SVR Image count updated to 0 for dnbMasterId "+dnbMasterId);
				}
			}else{
				uKey="";
				dmSQL = "select group_concat(doc_id) as doc_ids from dnb_doccase_master where case_id = "+caseId+" group by case_id";
				pstmt2 = conn.prepareStatement(dmSQL);
				rst2 = pstmt2.executeQuery();
				while(rst2.next()){
					if(docIds!="") docIds+=",";
					docIds += rst2.getString("doc_ids");
				}
				map.put("docIds",docIds);
				map.put("uKey", uKey);
			}
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(mpstmt);
			resourceManager.close(svrCpstmt);
			resourceManager.close(svrpstmt);
			resourceManager.close(ccampstmt);
			resourceManager.close(wpstmt);
			resourceManager.close(rst1);
			resourceManager.close(pstmt1);
			resourceManager.close(rst2);
			resourceManager.close(pstmt2);
			resourceManager.close(rst);
			resourceManager.close(pstmt);
		}
		return map;
	}
}
