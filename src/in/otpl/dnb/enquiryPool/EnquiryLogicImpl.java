package in.otpl.dnb.enquiryPool;
import in.otpl.dnb.util.ErrorLogHandler;
import in.otpl.dnb.util.PlatformUtil;
import in.otpl.dnb.util.ResourceManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnquiryLogicImpl implements EnquiryLogic {

	private static final Logger LOG = Logger.getLogger(EnquiryLogicImpl.class);

	@Autowired
	private ResourceManager resourceManager;
	@Autowired
	private EnquiryDao enquiryDao;

	@Override
	public Map<String, Object> getAssignmentPool(EnquiryForm enquiryForm, int customerId, String loginName, String userType,long userId) {
		Connection conn = null;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			conn = resourceManager.getConnection();
			map = enquiryDao.getAssignmentPool(conn, enquiryForm, customerId, loginName, userType,userId);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return map;
	}
	@Override
	public JSONArray getCaseHistory(long dnbMasterId, int customerId) {
		JSONArray caseHistoryArray = null;
		Connection conn = null;
		try {
			conn = resourceManager.getConnection();
			caseHistoryArray = enquiryDao.getCaseHistory(conn, dnbMasterId);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return caseHistoryArray;
	}
	@Override
	public JSONArray getCaseAssignmentHistory(long dnbMasterId, int customerId) {
		JSONArray caseAssignmentHistoryArray = null;
		Connection conn = null;
		try {
			conn = resourceManager.getConnection();
			caseAssignmentHistoryArray = enquiryDao.getCaseAssignmentHistory(conn, dnbMasterId, customerId);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return caseAssignmentHistoryArray;
	}

	@Override
	public JSONObject getEnquiryDetails(long dnbMasterId, int customerId) {
		JSONObject enquiryDetails = null;
		Connection conn = null;
		try {
			conn = resourceManager.getConnection();
			enquiryDetails = enquiryDao.getEnquiryDetails(conn, dnbMasterId, customerId);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return enquiryDetails;
	}
	@Override
	public long updateDNBAssignmentPoolHistory(int customerId, long userId, long asphId, long assignmentId) {
		long rows = 0;
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = resourceManager.getConnection();
			String sql = "update dnb_assignment_pool_history set assignment_id = ? , is_processed = ? , processed_time = ?,assigned_to = ? where id = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, assignmentId);
			pstmt.setInt(2, 1);
			pstmt.setString(3, PlatformUtil.getRealDateToSQLDateTime(new Date()));
			pstmt.setLong(4, userId);
			pstmt.setLong(5, asphId);
			rows = pstmt.executeUpdate();
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(pstmt);
			resourceManager.close(conn);
		}
		return rows;
	}
	@Override
	public JSONArray getDocumentList(int customerId, int caseId) {
		JSONArray documentList = new JSONArray();
		Connection conn = null;
		try {
			conn = resourceManager.getConnection();
			documentList = enquiryDao.getDocumentList(conn, customerId, caseId);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return documentList;
	}
	@Override
	public void resetOldDNBData(long dnbMasterId, int resetType, int customerId) {
		Connection conn = null;
		try {
			conn = resourceManager.getConnection();
			enquiryDao.resetOldDNBData(conn, dnbMasterId, resetType, customerId);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
	}

	@Override
	public EnquiryDto[] getAllAssignmentStatus() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		EnquiryDto[] statusArray = null;
		ArrayList<EnquiryDto> statusList = new ArrayList<EnquiryDto>();

		try {
			String sql = " SELECT id,status_name FROM assignment_status ";
			con = resourceManager.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				EnquiryDto status = new EnquiryDto();
				status.setId(rs.getInt(1));
				status.setStatusName(rs.getString(2));
				statusList.add(status);
			}
			statusArray = new EnquiryDto[statusList.size()];
			statusArray = statusList.toArray(statusArray);

		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(rs);
			resourceManager.close(pstmt);
			resourceManager.close(con);
		}
		return statusArray;

	}

	@Override
	public long saveUpdateEnquiry(long userId, String enquiryName, long dnbMasterId, long asphId, int isReassigned, long enquiryDetailsId, int customerId, int loggedInUserId, String enquiryStartTime) {
		Connection conn = null;
		long rowId = 0;
		try {
			conn = resourceManager.getConnection();
			rowId = enquiryDao.saveUpdateEnquiry(userId, enquiryName, dnbMasterId, asphId, isReassigned,
					enquiryDetailsId, conn, customerId, loggedInUserId, enquiryStartTime);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return rowId;
	}

	@Override
	public Map<String, Object> getMediaInfoExtension(long dnbMasterDataId) {
		Connection conn = null;
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			conn = resourceManager.getConnection();
			map = enquiryDao.getMediaInfoExtension(conn,dnbMasterDataId);
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(conn);
		}
		return map;
	}
	
}