package in.otpl.dnb.gw;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import in.otpl.dnb.util.ErrorLogHandler;
import in.otpl.dnb.util.PlatformUtil;
import in.otpl.dnb.util.ResourceManager;


@Repository
public class AppSessionDaoImpl implements AppSessionDao{
	
	private static final Logger LOG = Logger.getLogger(AppSessionDaoImpl.class);

	@Autowired
	private ResourceManager resourceManager;
	
	@Override
	public boolean isValidSession(Connection conn, int customerId, long userId, String sessionId) {
		String sql = " SELECT COUNT(*) FROM app_session a inner join customer c on  a.customer_id = c.id " +
				"inner join user u on a.user_id = u.id WHERE a.customer_id = ? and c.status = 1 and " +
				"u.status = 1 and a.user_id = ? and a.session_key  = ?" ;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean isSessionValid = false;
		try{
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, customerId);
			pstmt.setLong(2, userId);
			pstmt.setString(3, sessionId);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				if(rs.getInt(1) > 0 ) {
					isSessionValid = true;
				}
			}
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(rs);
			resourceManager.close(pstmt);
		}
		return isSessionValid;
	}

	@Override
	public boolean checkUniqueKey(Connection conn, int customerId, long userId, long uniqueId) {
		String sql = "select count(*) as count from data_reject_backup where customer_id = ? and user_id = ? and unique_id = ? ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean check = false;
		try{
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, customerId);
			pstmt.setLong(2, userId);
			pstmt.setLong(3, uniqueId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				if(rs.getInt("count") > 0) check = true;
			}
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(rs);
			resourceManager.close(pstmt);
		}
		return check;
	}

	@Override
	public int getSessionKey(int customerId, long userId, Connection conn) {
		String sql = "select id, session_key from app_session where customer_id = ? and user_id = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int rowId = 0;
		try{
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, customerId);
			pstmt.setLong(2, userId);
			rs = pstmt.executeQuery();
			if(rs.next()) rowId = rs.getInt("id");
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(rs);
			resourceManager.close(pstmt);
		}
		return rowId;
	}

	@Override
	public boolean rejectedDataBackup(Connection conn, int customerId, long userId, int sessionIdOld, int sessionIdCurrent, long uniqueId, String json, String remarks) {
		PreparedStatement pstmt = null;
		String sql = "insert into data_reject_backup (customer_id, user_id, session_id_old, session_id_current, unique_id, json, remarks) values (?, ?, ?, ?, ?, ?, ?)";
		try{
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, customerId);
			pstmt.setLong(2, userId);
			pstmt.setInt(3, sessionIdOld);
			pstmt.setInt(4, sessionIdCurrent);
			pstmt.setLong(5, uniqueId);
			pstmt.setString(6, json);
			pstmt.setString(7, remarks);
			return pstmt.execute();
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			return false;
		}finally{
			resourceManager.close(pstmt);
		}
	}

	@Override
	public int insertUpdateSessionKey(Connection conn, int rowId, String key, int customerId, long userId,String loginFrom){
		PreparedStatement stmt = null;
		String sql = "";
		int check = 0;
		try{
			if(rowId > 0)
				sql = "UPDATE app_session SET session_key = ?, customer_id = ?, modification_time = ? , login_from = ? WHERE user_id = ? ";
			else
				sql = "INSERT INTO app_session(session_key, customer_id, modification_time, login_from,user_id) VALUES(?, ?, ?, ?,?)";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, key);
			stmt.setInt(2, customerId);
			stmt.setString(3, PlatformUtil.getRealDateToSQLDateTime(new Date()));
			stmt.setString(4, loginFrom);
			stmt.setLong(5, userId);
			check = stmt.executeUpdate();
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(stmt);
		}
		return check;
	}
	
	@Override
	public void resetSessionKeyToNull(long userId, Connection conn) throws Exception{
		PreparedStatement stmt = null;
		try {
			String sql = "Update app_session set session_key = null where user_id = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setLong(1, userId);
			stmt.executeUpdate();
		} catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			throw e;
		}finally {
			resourceManager.close(stmt);
		}
		
	}
}
