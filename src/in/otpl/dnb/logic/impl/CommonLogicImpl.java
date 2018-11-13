package in.otpl.dnb.logic.impl;

import in.otpl.dnb.dao.CommonDao;
import in.otpl.dnb.logic.CommonLogic;
import in.otpl.dnb.user.UserDao;
import in.otpl.dnb.user.UserLogic;
import in.otpl.dnb.util.ConfigManager;
import in.otpl.dnb.util.ErrorLogHandler;
import in.otpl.dnb.util.PlatformUtil;
import in.otpl.dnb.util.ResourceManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonLogicImpl implements CommonLogic {

	private static final Logger LOG = Logger.getLogger(CommonLogicImpl.class);

	@Autowired
	private ResourceManager resourceManager;
	@Autowired
	private CommonDao commonDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ConfigManager configManager;
	@Autowired
	private UserLogic userLogic;

	public static boolean enquiryPoolService = false;
	public static boolean pdfCreatorService = false;
	public static boolean dataManipulatorService = false;
	public static boolean enquiryDataCompletionService = false;
	public static boolean reassignmentService = false;

	@Override
	public void dataManipulator(int customerId,int dnbMId) {
		if(!dataManipulatorService){
			dataManipulatorService = true;
			Connection conn = null;
			PreparedStatement pstmt = null, upstmt = null;
			ResultSet rst = null;
			JSONObject svrObject = new JSONObject();
			JSONObject ccamObject = new JSONObject();
			JSONArray docArray = new JSONArray();

			boolean isSVRDone = false, isCCAMDone = false, isDocDone = false;
			try {
				conn = resourceManager.getConnection();
				String sql = "select id,assignment_id,dnb_master_id, enquiry_id,unique_key,user_id,svr_data,ccam_data,doc_data "
						+ "from dnb_master_data where customer_id = "+customerId+" and is_data_distributed = 0 ";

				if(dnbMId > 0 ) sql+= " and dnb_master_id = "+dnbMId+"";
				pstmt = conn.prepareStatement(sql);
				rst = pstmt.executeQuery();
				while(rst.next()){
					long masterId = rst.getLong("id");
					long dnbMasterId = rst.getLong("dnb_master_id");
					long asId = rst.getLong("assignment_id");
					String enquiryId = rst.getString("enquiry_id");
					String uKey = rst.getString("unique_key");
					int userId = rst.getInt("user_id");
					String svrData = rst.getString("svr_data");
					String ccamData = rst.getString("ccam_data");
					String docData = rst.getString("doc_data");
					if (svrData != null && !svrData.equals("")) svrObject = JSONObject.fromObject(svrData);
					if (ccamData != null && !ccamData.equals("")) ccamObject = JSONObject.fromObject(ccamData);
					if (docData != null && !docData.equals("")) docArray = JSONArray.fromObject(docData);

					if (svrObject.size() > 0) 
						isSVRDone = commonDao.insertSVRData(conn, asId, enquiryId, uKey,userId, svrObject, customerId, masterId,dnbMasterId);
					else 
						isSVRDone = true;

					if (ccamObject.size() > 0) 
						isCCAMDone = commonDao.insertCCAMData(conn, asId, enquiryId, uKey,userId, ccamObject, customerId, masterId,dnbMasterId);
					else
						isCCAMDone = true;

					if (docArray.size() > 0)
						isDocDone = commonDao.insertDocData(conn, asId, enquiryId, uKey,userId, docArray, customerId, masterId, dnbMasterId);
					else 
						isDocDone = true;

					if (isSVRDone && isCCAMDone && isDocDone) {
						String uSql = "update dnb_master_data set is_data_distributed = 1 , modified_time = ? where id = ? and dnb_master_id = ?";
						upstmt = conn.prepareStatement(uSql);
						upstmt.setString(1,PlatformUtil.getRealDateToSQLDateTime(new Date()));
						upstmt.setLong(2, masterId);
						upstmt.setLong(3, dnbMasterId);
						int row = upstmt.executeUpdate();
						if (row > 0) LOG.info("Data successfully distributed for dnbMasterId " + dnbMasterId);
					} else {
						LOG.info("Unable to distributed data for dnbMasterId " + dnbMasterId);
					}
				}
			} catch (Exception e) {
				LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			} finally {
				resourceManager.close(upstmt);
				resourceManager.close(rst);
				resourceManager.close(pstmt);
				resourceManager.close(conn);
				dataManipulatorService = false;
			}
		}
	}

	@Override
	public void masterDataFetch(int customerId){
		Connection dnbConn = null, conn = null;
		try {
			dnbConn = resourceManager.getDnbConnection();
			if (dnbConn != null){
				conn = resourceManager.getConnection();
				getMasterDataFromDNBServer(conn, dnbConn, customerId);
			}
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(dnbConn);
			resourceManager.close(conn);
			LOG.info("Connection Closed with DNB Server");
		}
	}

	private void getMasterDataFromDNBServer(Connection conn,Connection dnbConn,int customerId){
		String updatedNextRunTime = PlatformUtil.getRealDateToSQLDateTime(new Date());
		String nextRunDate = getNextRunDate(conn, updatedNextRunTime);
		LOG.info("Data fetch from time : "+nextRunDate);
		boolean check1 = false, check2 = false, check3 = false, check4 = false, check5 = false, check6 = false;
		if(nextRunDate != null && !nextRunDate.isEmpty()){
			check1 = getDocumentMaster(conn,dnbConn,customerId,nextRunDate);
			check2 = getCaseTypeMaster(conn,dnbConn,customerId,nextRunDate);
			check3 = getStatemaster(conn,dnbConn,customerId,nextRunDate);
			check4 = getCorporateMaster(conn,dnbConn,customerId,nextRunDate);
			check5 = getCityMaster(conn,dnbConn,customerId,nextRunDate);
			check6 = getCaseWiseDocMaster(conn,dnbConn,customerId,nextRunDate);
		}
		if(check1 && check2 && check3 && check4 && check5 && check6)
			updateNextRunDate(conn, updatedNextRunTime);
	}

	private String getNextRunDate(Connection conn, String updatedNextRunTime){
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		String nextRunDateTime = updatedNextRunTime;
		try {
			String mSQL = "select next_run_time from dnb_master_data_fetch order by id desc limit 1 ";
			pstmt = conn.prepareStatement(mSQL);
			rst = pstmt.executeQuery();
			if(rst.next()){
				nextRunDateTime = rst.getString("next_run_time");
			}
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(rst);
			resourceManager.close(pstmt);
		}
		return nextRunDateTime;
	}

	private int updateNextRunDate(Connection conn, String updatedNextRunTime){
		PreparedStatement pstmt = null;
		int count = 0;
		try {
			String iSQL = "insert into dnb_master_data_fetch (next_run_time) values (?) ";
			pstmt = conn.prepareStatement(iSQL);
			pstmt.setString(1, updatedNextRunTime);
			count = pstmt.executeUpdate();
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(pstmt);
		}
		return count;
	}

	private boolean getDocumentMaster(Connection conn,Connection dnbConn,int customerId,String nextRunDate){
		PreparedStatement dnbpstmt = null,pstmt = null;
		ResultSet dnbrst = null;
		boolean isRecordAlreadyAvailable = false;
		String tableName = "dnb_doc_checklist";
		boolean check = false;
		try {
			String date = PlatformUtil.getRealDateToSQLDateTime(new Date());
			String sql = "insert into dnb_doc_checklist (doc_id,doc_description,created_time,modified_time) values (?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			String docSQL = "select DOC_ID,DOC_Desc from dbo.view_mst_document where InsertDate >= ? or ModifiedDate >= ? ";
			dnbpstmt = dnbConn.prepareStatement(docSQL);
			dnbpstmt.setString(1, nextRunDate);
			dnbpstmt.setString(2, nextRunDate);
			dnbrst = dnbpstmt.executeQuery();
			while(dnbrst.next()){
				int index = 1;
				int docId = dnbrst.getInt("DOC_ID");
				String docDesc = dnbrst.getString("DOC_Desc");
				isRecordAlreadyAvailable = commonDao.checkIfDataAlreadyExist(conn,docId,docDesc,tableName,0);
				if(!isRecordAlreadyAvailable){
					pstmt.setInt(index++, docId);
					pstmt.setString(index++, docDesc);
					pstmt.setString(index++, date);
					pstmt.setString(index++, date);
					pstmt.addBatch();
				}
			}
			pstmt.executeBatch();
			check = true;
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(dnbrst);
			resourceManager.close(dnbpstmt);
			resourceManager.close(pstmt);
		}
		return check;
	}

	private boolean getCaseTypeMaster(Connection conn,Connection dnbConn,int customerId,String nextRunDate){
		PreparedStatement dnbpstmt = null,pstmt = null;
		ResultSet dnbrst = null;
		boolean isRecordAlreadyAvailable = false;
		String tableName = "dnb_casetype_master";
		boolean check = false;
		try {
			String date = PlatformUtil.getRealDateToSQLDateTime(new Date());
			String sql = "insert into dnb_casetype_master (case_id,case_description,created_time,modified_time) values (?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			String caseMasterSQL = "select CaseType_id,CaseType_Desc from dbo.view_trn_Casetype_Desc where InsertedDate >= ?  or ModifiedDate >= ? ";
			dnbpstmt = dnbConn.prepareStatement(caseMasterSQL);
			dnbpstmt.setString(1, nextRunDate);
			dnbpstmt.setString(2, nextRunDate);
			dnbrst = dnbpstmt.executeQuery();
			while (dnbrst.next()) {
				int index = 1;
				int caseId = dnbrst.getInt("CaseType_id");
				String caseDesc = dnbrst.getString("CaseType_Desc");
				isRecordAlreadyAvailable = commonDao.checkIfDataAlreadyExist(conn,caseId,caseDesc,tableName,0);
				if(!isRecordAlreadyAvailable){
					pstmt.setInt(index++, caseId);
					pstmt.setString(index++, caseDesc);
					pstmt.setString(index++, date);
					pstmt.setString(index++, date);
					pstmt.addBatch();
				}
			}
			pstmt.executeBatch();
			check = true;
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(dnbrst);
			resourceManager.close(dnbpstmt);
			resourceManager.close(pstmt);
		}
		return check;
	}

	private boolean getStatemaster(Connection conn,Connection dnbConn,int customerId,String nextRunDate){
		PreparedStatement dnbpstmt = null,pstmt = null;
		ResultSet dnbrst = null;
		boolean isRecordAlreadyAvailable = false;
		String tableName = "dnb_state_master";
		boolean check = false;
		try {
			String date = PlatformUtil.getRealDateToSQLDateTime(new Date());
			String sql = "insert into dnb_state_master (state_id,state_name,created_time,modified_time) values (?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			String stateMasterSQL = "select StateId,State from dbo.view_mst_state where Insertdate >= ? or modifieddate >= ? ";
			dnbpstmt = dnbConn.prepareStatement(stateMasterSQL);
			dnbpstmt.setString(1, nextRunDate);
			dnbpstmt.setString(2, nextRunDate);
			dnbrst = dnbpstmt.executeQuery();
			while (dnbrst.next()) {
				int index = 1;
				int stateId = dnbrst.getInt("StateId");
				String stateName = dnbrst.getString("State");
				isRecordAlreadyAvailable = commonDao.checkIfDataAlreadyExist(conn,stateId,stateName,tableName,0);
				if(!isRecordAlreadyAvailable){
					pstmt.setInt(index++, stateId);
					pstmt.setString(index++, stateName);
					pstmt.setString(index++, date);
					pstmt.setString(index++, date);
					pstmt.addBatch();
				}
			}
			pstmt.executeBatch();
			check = true;
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(dnbrst);
			resourceManager.close(dnbpstmt);
			resourceManager.close(pstmt);
		}
		return check;
	}

	private boolean getCaseWiseDocMaster(Connection conn,Connection dnbConn,int customerId,String nextRunDate){
		PreparedStatement dnbpstmt = null,pstmt = null;
		ResultSet dnbrst = null;
		boolean isRecordAlreadyAvailable = false;
		String tableName = "dnb_doccase_master";
		boolean check = false;
		try {
			String date = PlatformUtil.getRealDateToSQLDateTime(new Date());
			String sql = "insert into dnb_doccase_master (case_id,doc_id,created_time,modified_time) values (?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			String caseDocMasterSQL = "select Doc_id,CaseType_id from dbo.view_trn_documentchecklist where InsertDate >= ? or ModifiedDate >= ? ";
			dnbpstmt = dnbConn.prepareStatement(caseDocMasterSQL);
			dnbpstmt.setString(1, nextRunDate);
			dnbpstmt.setString(2, nextRunDate);
			dnbrst = dnbpstmt.executeQuery();
			while (dnbrst.next()) {
				int index = 1;
				int caseId = dnbrst.getInt("CaseType_id");
				int docId = dnbrst.getInt("Doc_id");
				isRecordAlreadyAvailable = commonDao.checkIfDataAlreadyExist(conn,caseId,"",tableName,docId);
				if(!isRecordAlreadyAvailable){
					pstmt.setInt(index++, caseId);
					pstmt.setInt(index++, docId);
					pstmt.setString(index++, date);
					pstmt.setString(index++, date);
					pstmt.addBatch();
				}
			}
			pstmt.executeBatch();
			check = true;
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(dnbrst);
			resourceManager.close(dnbpstmt);
			resourceManager.close(pstmt);
		}
		return check;
	}

	private boolean getCorporateMaster(Connection conn,Connection dnbConn,int customerId,String nextRunDate){
		PreparedStatement dnbpstmt = null,pstmt = null;
		ResultSet dnbrst = null;
		boolean isRecordAlreadyAvailable = false;
		String tableName = "dnb_corporate_master";
		boolean check = false;
		try {
			String date = PlatformUtil.getRealDateToSQLDateTime(new Date());
			String sql = "insert into dnb_corporate_master (corporate_id,corporate_name,created_time,modified_time) values (?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			String corporateMasterSQL = "select corporate_id,corporate_name from dbo.view_mst_corporate where InsertDate >= ? or ModifiedDate >= ? ";
			dnbpstmt = dnbConn.prepareStatement(corporateMasterSQL);
			dnbpstmt.setString(1, nextRunDate);
			dnbpstmt.setString(2, nextRunDate);
			dnbrst = dnbpstmt.executeQuery();
			while (dnbrst.next()) {
				int index = 1;
				int corporateId = dnbrst.getInt("corporate_id");
				String corporateName = dnbrst.getString("corporate_name");
				isRecordAlreadyAvailable = commonDao.checkIfDataAlreadyExist(conn,corporateId,corporateName,tableName,0);
				if(!isRecordAlreadyAvailable){
					pstmt.setInt(index++, corporateId);
					pstmt.setString(index++, corporateName);
					pstmt.setString(index++, date);
					pstmt.setString(index++, date);
					pstmt.addBatch();
				}
			}
			pstmt.executeBatch();
			check = true;
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(dnbrst);
			resourceManager.close(dnbpstmt);
			resourceManager.close(pstmt);
		}
		return check;
	}

	private boolean getCityMaster(Connection conn,Connection dnbConn,int customerId,String nextRunDate){
		PreparedStatement dnbpstmt = null,pstmt = null;
		ResultSet dnbrst = null;
		boolean isRecordAlreadyAvailable = false;
		String tableName = "dnb_city_master";
		boolean check = false;
		try {
			String date = PlatformUtil.getRealDateToSQLDateTime(new Date());
			String sql = "insert into dnb_city_master (city_id,city_name,created_time,modified_time) values (?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			String cityMasterSQL = "select CityId,CityName from dbo.view_mst_city where InsertDate >= ? or ModifiedDate >= ? ";
			dnbpstmt = dnbConn.prepareStatement(cityMasterSQL);
			dnbpstmt.setString(1, nextRunDate);
			dnbpstmt.setString(2, nextRunDate);
			dnbrst = dnbpstmt.executeQuery();
			while (dnbrst.next()) {
				int index = 1;
				int cityId = dnbrst.getInt("CityId");
				String cityName = dnbrst.getString("CityName");
				isRecordAlreadyAvailable = commonDao.checkIfDataAlreadyExist(conn,cityId,cityName,tableName,0);
				if(!isRecordAlreadyAvailable){
					pstmt.setInt(index++, cityId);
					pstmt.setString(index++, cityName);
					pstmt.setString(index++, date);
					pstmt.setString(index++, date);
					pstmt.addBatch();
				}
			}
			pstmt.executeBatch();
			check = true;
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(dnbrst);
			resourceManager.close(dnbpstmt);
			resourceManager.close(pstmt);
		}
		return check;
	}

	@Override
	public void enquiryDataCompletion(int customerId){
		if(!enquiryDataCompletionService){
			enquiryDataCompletionService = true;
			Connection conn = null;
			try{
				conn = resourceManager.getConnection();
				commonDao.checkEnquiryCompleted(conn, customerId);
			}catch (Exception e) {
				LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			}finally{
				resourceManager.close(conn);
				enquiryDataCompletionService = false;
			}
		}
	}

	@Override
	public void reassignment(int customerId){
		if(!reassignmentService){
			reassignmentService = true;
			Connection conn = null;
			try{
				conn = resourceManager.getConnection();
				commonDao.dnbReassignmentScheduler(conn, customerId);
			}catch (Exception e) {
				LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			}finally{
				resourceManager.close(conn);
				reassignmentService = false;
			}
		}
	}

	@Override
	public void enquiryPoolFetch(int customerId) {
		Connection dnbConn = null, conn = null;
		if(!enquiryPoolService){
			enquiryPoolService = true;
			try {
				dnbConn = resourceManager.getDnbConnection();
				if(dnbConn != null){
					LOG.info("Connection initiated with DNB sql server");
					conn = resourceManager.getConnection();
					commonDao.getInitiatedEnquiryDetails(conn, dnbConn, customerId);
				}
			} catch (Exception e) {
				LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			} finally {
				resourceManager.close(dnbConn);
				resourceManager.close(conn);
				LOG.info("Connection Closed with DNB Server");
				enquiryPoolService = false;
			}
		}
	}

	/*@Override
	public void pdfCreator(int customerId){
		Connection conn = null;
		if(!pdfCreatorService){
			pdfCreatorService = true;
			try{
				conn = resourceManager.getConnection();
				commonDao.createPDF(conn, customerId);
			}catch (Exception e) {
				LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			}finally{
				resourceManager.close(conn);
				pdfCreatorService = false;
			}
		}
	}*/

	@Override
	public int insertEmailDetails(int customerId, String emailTo, String emailCC, String emailBCC, String subject,String body) {
		Connection conn = null;
		try{
			conn = resourceManager.getConnection();
			customerId=commonDao.insertEmailDetails(customerId, emailTo, emailCC, emailBCC, subject, body);
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(conn);
		}
		return customerId;
	}

	@Override
	public JSONObject getDNBMediaDetails( String uKey, long masterId, long dnbMasterId, String docId, String mediaId) {
		JSONObject jsonObject = new JSONObject();
		Connection conn = null;
		try{
			conn = resourceManager.getConnection();
			jsonObject = commonDao.getDNBMediaDetails(conn, uKey, masterId, dnbMasterId, docId, mediaId);
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(conn);
		}
		return jsonObject;
	}

}