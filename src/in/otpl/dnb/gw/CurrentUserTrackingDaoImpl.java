package in.otpl.dnb.gw;

import in.otpl.dnb.gw.CurrentUserTrackingForm;
import in.otpl.dnb.report.AllUserMapForm;
import in.otpl.dnb.user.TeamLogic;
import in.otpl.dnb.user.UserLogic;
import in.otpl.dnb.util.ErrorLogHandler;
import in.otpl.dnb.util.PlatformUtil;
import in.otpl.dnb.util.ResourceManager;
import in.otpl.dnb.util.SessionConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CurrentUserTrackingDaoImpl implements CurrentUserTrackingDao{
	
	private  static final Logger LOG = Logger.getLogger(CurrentUserTrackingDaoImpl.class);
	
	@Autowired
	private ResourceManager resourceManager;
	@Autowired
	private TeamLogic teamLogic;
	@Autowired
	private UserLogic userLogic;
	
	private final String SQLSELECT = "SELECT u.id, u.customer_id, t.user_id, t.creation_time, t.altitude, u.first_name, u.last_name, t.lattitude,"
			+ "t.longitude, t.is_valid, t.accuracy, t.is_cellsite, t.modification_time, u.team_id "
			+ "FROM current_user_tracking t "
			+ "join user u on t.user_id = u.id ";
	
	private final String SQLINSERT = "INSERT INTO " + getTableName() + " ( customer_id, lattitude, longitude, altitude, accuracy, creation_time, modification_time, user_id, landmark_id, is_valid, is_cellsite ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private final String SQLUPDATE = "UPDATE " + getTableName() + " SET lattitude = ?, longitude = ?, altitude = ?, accuracy = ?, modification_time = ?, landmark_id = ?, is_valid = ?, is_cellsite = ? WHERE id = ? ";
	
	public String getTableName(){
		return "current_user_tracking";
	}
	
	@Override
	public int insert(CurrentUserTracking dto, Connection conn){
		PreparedStatement stmt = null;
		int rows = 0;
		try {
			stmt = conn.prepareStatement( SQLINSERT );
			int index = 1;
			stmt.setInt( index++, dto.getCustomerId() );
			stmt.setString( index++, dto.getLattitude() );
			stmt.setString( index++, dto.getLongitude() );
			stmt.setString( index++, dto.getAltitude() );
			stmt.setString( index++, dto.getAccuracy() );
			stmt.setString(index++, PlatformUtil.getRealDateToSQLDateTime(new Date()));
			stmt.setString(index++, PlatformUtil.getRealDateToSQLDateTime(new Date()));
			stmt.setLong( index++, dto.getUserId() );
			if (dto.isLandmarkIdNull()) {
				stmt.setNull( index++, java.sql.Types.INTEGER );
			} else {
				stmt.setLong( index++, dto.getLandmarkId() );
			}
			stmt.setInt( index++, dto.getIsValid() );
			stmt.setInt( index++, dto.getIsCellsite() );
			rows = stmt.executeUpdate();
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(stmt);
		}
		return rows;
	}

	@Override
	public void update(long customerTrackingUserId, CurrentUserTracking dto, Connection conn){
		PreparedStatement stmt = null;
		try {
			synchronized (this) {
				stmt = conn.prepareStatement( SQLUPDATE );
				int index=1;
				stmt.setString( index++, dto.getLattitude() );
				stmt.setString( index++, dto.getLongitude() );
				stmt.setString( index++, dto.getAltitude() );
				stmt.setString( index++, dto.getAccuracy() );
				stmt.setString(index++, PlatformUtil.getRealDateToSQLDateTime(new Date()));
				if (dto.isLandmarkIdNull()) {
					stmt.setNull( index++, java.sql.Types.INTEGER );
				} else {
					stmt.setLong( index++, dto.getLandmarkId() );
				}
				stmt.setInt( index++, dto.getIsValid() );
				stmt.setInt( index++, dto.getIsCellsite() );
				stmt.setLong( index++, customerTrackingUserId );
				stmt.executeUpdate();
			}
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(stmt);
		}
	}

	@Override
	public CurrentUserTracking[] findByDynamicWhere(String sql, Object[] sqlParams, Connection conn){
		final String query = SQLSELECT + " WHERE " + sql;
		return findByDynamicSelect(query, sqlParams, conn);
	}

	private CurrentUserTracking[] findByDynamicSelect(String sql, Object[] sqlParams, Connection conn){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		CurrentUserTracking[] fetchMultiResults = null;
		try {
			stmt = conn.prepareStatement( sql );
			for (int i=0; sqlParams!=null && i<sqlParams.length; i++ ) {
				stmt.setObject( i+1, sqlParams[i] );
			}
			rs = stmt.executeQuery();
			fetchMultiResults = fetchMultiResults(rs);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(rs);
			resourceManager.close(stmt);
		}
		return fetchMultiResults;
	}

	private CurrentUserTracking[] fetchMultiResults(ResultSet rs) throws SQLException{
		List<CurrentUserTracking> resultList = fetchMultiResultsList(rs);
		CurrentUserTracking[] ret = new CurrentUserTracking[ resultList.size() ];
		resultList.toArray( ret );
		return ret;
	}

	@Override
	public Map<String, Object> getAllUserTrackingReport(int customerId, long userId, int userTypeId, Connection conn) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String where = " where u.customer_id = ? and t.lattitude != 'NA' and t.longitude != 'NA' and t.is_valid= 1 ";
			String orderBy =" order by t.creation_time desc ";
			if(userTypeId == SessionConstants.USER_TYPE_ID_LEAD) {
				String teamIds = teamLogic.teamIdsByLeadId(customerId, userId);
				List<Long> userIdList = userLogic.getUserIdsFromCommaSeperatedUsersTeamIds(customerId, teamIds, String.valueOf(userId));
				long[] userIds = ArrayUtils.toPrimitive(userIdList.toArray(new Long[userIdList.size()]));
				if(userIds != null && userIds.length > 0)
					where += " and u.id in ("+PlatformUtil.getCommaSeperatedString(userIds)+") ";
			}else if(userTypeId==SessionConstants.USER_TYPE_ID_USER){
				where += " AND u.id = "+userId;
			}
			String sql = SQLSELECT + where + orderBy;
			
			CurrentUserTracking[] currentUserTrackings = findByDynamicSelect(sql, new Object[] { customerId }, conn);
			List<CurrentUserTracking> data = Arrays.asList(currentUserTrackings);
			map.put("data", data);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(rs);
			resourceManager.close(stmt);
		}
		return map;
	}
	
	private List<CurrentUserTracking> fetchMultiResultsList(ResultSet rs) throws SQLException{
		List<CurrentUserTracking> resultList = new ArrayList<CurrentUserTracking>();
		while (rs.next()) {
			CurrentUserTracking userTrack = new CurrentUserTracking();
			populateDto(userTrack, rs);
			resultList.add(userTrack);
		}
		return resultList;
	}

	private void populateDto(CurrentUserTracking userTrack, ResultSet rs) throws SQLException{
		userTrack.setId(rs.getInt("id"));
		userTrack.setCustomerId(rs.getInt("customer_id"));
		userTrack.setUserId(rs.getInt("user_id"));
		userTrack.setFirstName(rs.getString("first_name"));
		userTrack.setLastName(rs.getString("last_name"));
		userTrack.setLattitude(rs.getString("lattitude"));
		userTrack.setLongitude(rs.getString("longitude"));
		int cellSite = rs.getInt("is_cellsite");
		userTrack.setIsCellsite((short) (cellSite));
		userTrack.setCreationTime(rs.getString("creation_time"));
		userTrack.setModificationTime(rs.getString("modification_time"));
		userTrack.setTeamId(rs.getInt("team_id"));
		userTrack.setAltitude( rs.getString("altitude") );
		userTrack.setAccuracy( rs.getString("accuracy") );
		userTrack.setIsValid(rs.getInt("is_valid"));
	}
	
	//All user map
	private List<CurrentUserTrackingForm> fetchMultiList(ResultSet rs) throws SQLException{
		List<CurrentUserTrackingForm> resultList = new ArrayList<CurrentUserTrackingForm>();
		while (rs.next()) {
			CurrentUserTrackingForm userTrack = new CurrentUserTrackingForm();
			populateData(userTrack, rs);
			resultList.add(userTrack);
		}
		return resultList;
	}

	private void populateData(CurrentUserTrackingForm userTrack, ResultSet rs) throws SQLException{
		userTrack.setId(rs.getInt("id"));
		userTrack.setCustomerId(rs.getInt("customer_id"));
		userTrack.setUserId(rs.getInt("user_id"));
		userTrack.setFirstName(rs.getString("first_name"));
		userTrack.setLastName(rs.getString("last_name"));
		userTrack.setLattitude(rs.getString("lattitude"));
		userTrack.setLongitude(rs.getString("longitude"));
		int cellSite = rs.getInt("is_cellsite");
		userTrack.setIsCellsite((short) (cellSite));
		userTrack.setCreationTime(rs.getString("creation_time"));
		userTrack.setModificationTime(rs.getString("modification_time"));
		userTrack.setTeamId(rs.getInt("team_id"));
		userTrack.setAltitude( rs.getString("altitude") );
		userTrack.setAccuracy( rs.getString("accuracy") );
		userTrack.setIsValid(rs.getInt("is_valid"));
	}
	@Override
	public void lastTracking(LastTrackingDto dto, Connection conn){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sqlForInsertOrUpdate = "insert into last_tracking (user_id, tracking_id, valid_tracking_id, non_cellsite_tracking_id, customer_id) " +
				" values(?,?,?,?,?) ON DUPLICATE KEY UPDATE tracking_id=? ";
		if(dto.getValidTrackingId() > 0){
			sqlForInsertOrUpdate += " ,valid_tracking_id=? ";
		}
		if(dto.getNonCellsiteTrackingId() > 0){
			sqlForInsertOrUpdate += " ,non_cellsite_tracking_id=? ";
		}
		try {
			stmt = conn.prepareStatement(sqlForInsertOrUpdate , Statement.RETURN_GENERATED_KEYS );
			int index = 1;	
			stmt.setLong( index++, dto.getUserId() );
			stmt.setLong( index++, dto.getTrackingId() );
			stmt.setLong( index++, dto.getValidTrackingId());
			stmt.setLong( index++, dto.getNonCellsiteTrackingId() );
			stmt.setInt( index++, dto.getCustomerId());
			//for update
			stmt.setLong( index++, dto.getTrackingId() );
			if(dto.getValidTrackingId() > 0){
				stmt.setLong( index++, dto.getValidTrackingId() );
			}
			if(dto.getNonCellsiteTrackingId() > 0){
				stmt.setLong( index++, dto.getNonCellsiteTrackingId() );
			}

			stmt.executeUpdate();

			rs = stmt.getGeneratedKeys();
			if (rs != null && rs.next()) {
				dto.setId( rs.getLong( 1 ) );
			}
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally {
			resourceManager.close(rs);
			resourceManager.close(stmt);
		}
	}

	/** All User Map **/
	@Override
	public Map<String, Object> getdnbMapReport(AllUserMapForm userMapForm, Connection conn){
		PreparedStatement stmt = null, stmt2 = null;
		ResultSet rs = null, rs2 = null;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String where = " where u.customer_id = ? and t.is_valid= 1 and t.modification_time >= CURRENT_DATE ";
			String orderBy =" order by t.creation_time desc";
			String sql = SQLSELECT + where + orderBy;
			CurrentUserTrackingForm[] currentUserTrackings = findBySelectDynamic(sql, new Object[] { userMapForm.getCustomerId() }, conn);
			List<CurrentUserTrackingForm> data = Arrays.asList(currentUserTrackings);
			int count = countRecord(conn, where, userMapForm.getCustomerId());
			map.put("data", data);
			map.put("totalRows", count);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(rs2);
			resourceManager.close(stmt2);
			resourceManager.close(rs);
			resourceManager.close(stmt);
		}
		return map;
	}

	private int countRecord(Connection conn, String where, int customerId){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int count = 0;
		String countQuery = "SELECT count(t.id) FROM " + getTableName() + " t " +
				"join user u on t.user_id = u.id " + where;
		try{
			stmt = conn.prepareStatement(countQuery);
			int index = 1;
			stmt.setInt(index++, customerId);
			rs = stmt.executeQuery();
			if(rs.next())
				count = rs.getInt(1);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(rs);
			resourceManager.close(stmt);
		}
		return count;
	}
	@Override
	public CurrentUserTrackingForm[] findByDynamic(String sql, Object[] sqlParams, Connection conn){
		final String query = SQLSELECT + " WHERE " + sql;
		return findBySelectDynamic(query, sqlParams, conn);
	}

	private CurrentUserTrackingForm[] findBySelectDynamic(String sql, Object[] sqlParams, Connection conn){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		CurrentUserTrackingForm[] fetchResults = null;
		try {
			stmt = conn.prepareStatement( sql );
			for (int i=0; sqlParams!=null && i<sqlParams.length; i++ ) {
				stmt.setObject( i+1, sqlParams[i] );
			}
			rs = stmt.executeQuery();
			fetchResults = fetchResults(rs);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(rs);
			resourceManager.close(stmt);
		}
		return fetchResults;
	}

	private CurrentUserTrackingForm[] fetchResults(ResultSet rs) throws SQLException{
		List<CurrentUserTrackingForm> resultList = fetchMultiList(rs);
		CurrentUserTrackingForm[] ret = new CurrentUserTrackingForm[ resultList.size() ];
		resultList.toArray( ret );
		return ret;
	}
}