package in.otpl.dnb.user;


import in.otpl.dnb.util.ErrorLogHandler;
import in.otpl.dnb.util.PlatformUtil;
import in.otpl.dnb.util.ResourceManager;
import in.otpl.dnb.util.SessionConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mysql.jdbc.Statement;

@Repository
public class UserDaoImpl implements UserDao{ 

	private  static final Logger LOG = Logger.getLogger( UserDaoImpl.class );

	@Autowired
	private  ResourceManager  resourceManager;
	@Autowired
	private TeamLogic teamLogic;

	private final String SQL_SELECT = "SELECT u.id, u.customer_id, u.user_type_id, u.ptn, u.first_name, u.last_name, u.creation_time, " +
			" u.modification_time, ut.description as usertype, u.handset_detail, u.password, u.login_name, u.email_address, u.employee_number, u.team_id, u.force_setup_update, " +
			" u.phone_model_id, u.phone_app_version, u.status, u.need_download_assignment, u.timezone_id, u.designation, u.is_first_login, " +
			" u.pincode, u.imei ,u.country_id" +
			" FROM " + getTableName() + " as u " +
			" JOIN user_type  ut ON u.user_type_id = ut.id " ;

	private final String SQL_COUNT = "SELECT count(u.id) FROM " + getTableName() + " as u ";

	private final String SQL_INSERT = "INSERT INTO " + getTableName() + " (customer_id, user_type_id, ptn, first_name, " +
			" last_name, creation_time, modification_time, password, login_name, email_address, employee_number, status , " +
			" designation,userCreatedBy, imei) " +
			"VALUES (?,?, ?, ?, ?, ?, ?, ?, ?,  ?, ?, ?, ?, ?, ?)";

	private String SQL_UPDATE_USER = "UPDATE " + getTableName() + " SET  modification_time = ?, email_address = ? ";

	private final String SQL_UPDATE_FIRST_LOGIN = "UPDATE  " + getTableName() + " SET is_first_login = ? WHERE id = ?";

	private final String SQL_UPDATESTATUS = "UPDATE  " + getTableName() + " SET status = ? , modification_time = ? WHERE id = ?";

	private final String SQL_UPDATE_MYINFO = "UPDATE " + getTableName() + " SET customer_id = ?, user_type_id = ?, first_name = ?, last_name = ?, " +
			" modification_time = ?, email_address = ? " ;

	private final String SQL_SELECT_USERTYPE = "SELECT id, description, creation_time, modification_time, is_admin, is_team_lead, is_field_user FROM user_type ";

	public String getTableName(){
		return "user";
	}
	
	@Override
	public long insert(UserForm form, Connection conn){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		long userId = 0;
		try {
			pstmt = conn.prepareStatement( SQL_INSERT, Statement.RETURN_GENERATED_KEYS );
			int index = 1;
			pstmt.setInt(index++, form.getCustomerId());
			pstmt.setInt(index++, form.getUserTypeId());
			pstmt.setString(index++, form.getPtn());
			pstmt.setString(index++, form.getFirstName());
			pstmt.setString(index++, form.getLastName());
			pstmt.setString(index++, PlatformUtil.getRealDateToSQLDateTime(new Date()));
			pstmt.setString(index++, PlatformUtil.getRealDateToSQLDateTime(new Date()));
			pstmt.setString(index++, PlatformUtil.sha(form.getPassword().trim()));
			pstmt.setString(index++, form.getLoginName());
			pstmt.setString(index++, form.getEmailAddress());
			pstmt.setString(index++, form.getEmployeeNumber());
			pstmt.setInt(index++, SessionConstants.STATUS_ACTIVE);
			pstmt.setString(index++, form.getDesignation());
			pstmt.setLong(index++, form.getUserCreatedBy());
			pstmt.setString(index++, form.getImei());
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			if (rs != null && rs.next()) {
				userId = rs.getLong(1);
			}
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally {
			resourceManager.close(rs);
			resourceManager.close(pstmt);
		}
		return userId;
	}

	@Override
	public long update(UserForm form, Connection conn) {
		PreparedStatement pstmt = null;
		int rows = 0;
		String updateSql = "UPDATE " + getTableName() + " SET ptn = ?, first_name = ?, last_name = ?, modification_time = ?, "
				+ "login_name = ?, email_address = ?, employee_number = ?, designation = ?, imei = ? ";
		try {
			if(PlatformUtil.isNotEmpty(form.getPassword())){
				updateSql += " , password = ? ";
			}
			updateSql += " WHERE id = ? ";
			pstmt = conn.prepareStatement( updateSql );
			int index = 1;
			pstmt.setString(index++, form.getPtn());
			pstmt.setString(index++, form.getFirstName());
			pstmt.setString(index++, form.getLastName());
			pstmt.setString(index++, PlatformUtil.getRealDateToSQLDateTime(new Date()));
			pstmt.setString(index++, form.getLoginName());
			pstmt.setString(index++, form.getEmailAddress());
			pstmt.setString(index++, form.getEmployeeNumber());
			pstmt.setString(index++, form.getDesignation());
			pstmt.setString(index++, form.getImei());
			if(form.getPassword() != null && !form.getPassword().trim().equals("")){
				pstmt.setString(index++, PlatformUtil.sha(form.getPassword()));
			}
			pstmt.setLong(index++, form.getId());
			rows = pstmt.executeUpdate();
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(pstmt);
		}
		return rows;
	}

	@Override
	public UserDto findByPrimaryKey(long id, Connection conn){
		UserDto ret[] = findByDynamicSelect( SQL_SELECT + " WHERE u.id = ?", new Object[] {  new Long(id) },conn );
		return ret.length==0 ? null : ret[0];
	}

	@Override
	public UserDto[] findWhereIdIn(String commaSeperatedId, Connection conn){
		return findByDynamicSelect( SQL_SELECT + " WHERE u.id IN ("+ commaSeperatedId +") ORDER BY u.first_name, u.last_name ASC",null,conn);
	}

	@Override
	public UserDto[] findWhereUserTypeIdStatusEquals(int customerId, int status, int type, Connection conn){
		return findByDynamicSelect( SQL_SELECT + " WHERE u.customer_id = ? and u.status = ? and u.user_type_id = ? ORDER BY u.first_name, u.last_name ASC", new Object[] {  new Integer(customerId),new Integer(status),new Integer(type)  },conn );
	}

	@Override
	public UserDto[] findWhereUserTypeIdEquals(int customerId, int type, Connection conn){
		return findByDynamicSelect( SQL_SELECT + " WHERE u.customer_id = ? and u.user_type_id = ? ORDER BY u.first_name, u.last_name ASC", new Object[] {  new Integer(customerId),new Integer(type)  },conn );
	}

	@Override
	public UserDto findWhereLoginNameEquals(String loginName, int customerId, Connection conn){
		UserDto[] ret =  findByDynamicSelect( SQL_SELECT + " WHERE u.login_name = ? and u.customer_id = ? " , new Object[] { loginName,customerId },conn );
		return ret.length==0 ? null : ret[0];
	}

	@Override
	public UserDto[] findWhereTeamIdEquals(int teamId,Connection conn){
		return findByDynamicSelect( SQL_SELECT + " WHERE u.team_id = ? ORDER BY u.first_name, u.last_name ASC", new Object[] {  new Integer(teamId) },conn );
	}

	@Override
	public UserDto[] findWhereCustIdTeamIdIN(int customerId, String commaSeperatedTeamIds, Connection conn){
		return findByDynamicSelect( SQL_SELECT + " WHERE u.customer_id = ? and u.team_id IN ("+commaSeperatedTeamIds+") ORDER BY u.first_name, u.last_name ASC", new Object[] { new Integer(customerId) },conn );
	}

	@Override
	public UserDto[] findWhereTeamIdInEquals(int customerId, int status, int type,	String commaSeperatedTeamIds,Connection conn) {
		return findByDynamicSelect ( SQL_SELECT + " WHERE u.customer_id = ? and u.status = ? and u.user_type_id = ? and u.team_id IN ("+commaSeperatedTeamIds+") ORDER BY u.first_name, u.last_name ASC", new Object[] { new Integer(customerId), new Integer(status), new Integer(type) },conn);
	}

	@Override
	public UserDto[] findWhereTeamIdEquals(int customerId, int type, String teamId, Connection conn) {
		return findByDynamicSelect ( SQL_SELECT + " WHERE u.customer_id = ? and u.user_type_id = ? and u.team_id in ("+teamId+") ORDER BY u.first_name, u.last_name ASC", new Object[] { new Integer(customerId), new Integer(type) },conn);
	}

	@Override
	public UserDto[] findByDynamicWhere(String sql, Object[] sqlParams, Connection conn){
		final String SQL = SQL_SELECT + " WHERE " + sql;
		return findByDynamicSelect(SQL, sqlParams, conn);
	}

	@Override
	public UserDto[] getUsersCreatedBy(int customerId, String created_by_id, int user_type_id, int status, Connection conn) {
		if(created_by_id.equals("0"))
			return findByDynamicSelect( SQL_SELECT + " where u.customer_id = ? and u.user_type_id = ? and u.status = ? ORDER BY u.first_name, u.last_name ASC", new Object[] {  new Integer(customerId), new Integer(user_type_id), new Integer(status) } ,conn);
		else
			return findByDynamicSelect( SQL_SELECT + " where u.customer_id = ? and u.user_type_id = ? and u.status = ? and u.userCreatedBy = ? ORDER BY u.first_name, u.last_name ASC", new Object[] {  new Integer(customerId), new Integer(user_type_id), new Integer(status), created_by_id } ,conn);
	}

	@Override
	public UserDto[] getUsersByLeadId(int customerId, String lead_id, int status, Connection conn){
		return findByDynamicSelect( SQL_SELECT + " inner join team as t on u.team_id = t.id where t.customer_id = ? and t.lead_id in (?) and u.status = ? ORDER BY u.first_name, u.last_name ASC", new Object[] {  new Integer(customerId), lead_id, new Integer(status) } ,conn);
	}
	@Override
	public UserDto[] findByDynamicCondition(String sql, Object[] sqlParams, Connection conn) {
		final String SQL = SQL_SELECT + sql;
		return findByDynamicSelect(SQL, sqlParams, conn);
	}

	@Override
	public UserDto leadProfileBasedOnOwnTeamUserId(Connection conn, int customerId, int userId){
		UserDto ret[] = findByDynamicSelect( SQL_SELECT + " where u.id = (select lead_id from team where id = (select team_id from UserDto where customer_id = ? and id = ? )) ", new Object[] { new Integer(customerId), new Integer(userId) }, conn );
		return ret.length==0 ? null : ret[0];
	}

	private UserDto[] fetchMultiResults(ResultSet rs) throws SQLException{
		Collection<UserDto> resultList = new ArrayList<UserDto>();
		while (rs.next()) {
			UserDto dto = new UserDto();
			populateDto( dto, rs);
			resultList.add( dto );
		}

		UserDto ret[] = new UserDto[ resultList.size() ];
		resultList.toArray( ret );
		return ret;
	}

	private void populateDto(UserDto dto, ResultSet rs) throws SQLException{
		dto.setId( rs.getInt( "id" ) );
		dto.setCustomerId( rs.getInt( "customer_id" ) );
		dto.setUserTypeId( rs.getInt( "user_type_id" ) );
		dto.setPtn( rs.getString( "ptn" ) );
		dto.setFirstName( rs.getString( "first_name" ) );
		dto.setLastName( rs.getString( "last_name" ) );
		dto.setCreationTime( rs.getString("creation_time" ) );
		dto.setModificationTime( rs.getString("modification_time" ) );
		dto.setUserType(rs.getString("userType"));
		dto.setHandsetDetails( rs.getString( "handset_detail" ) );
		dto.setPassword( rs.getString( "password" ) );
		dto.setLoginName( rs.getString( "login_name" ) );
		dto.setEmailAddress( rs.getString( "email_address" ) );
		dto.setEmployeeNumber( rs.getString( "employee_number" ) );
		dto.setTeamId( rs.getInt( "team_id" ) );
		dto.setForceSetupUpdate( rs.getInt("force_setup_update") );
		dto.setPhoneModelId( rs.getInt( "phone_model_id" ) );
		dto.setPhoneAppVersion(rs.getString("phone_app_version"));
		dto.setStatus( rs.getInt( "status" ) );
		dto.setNeedDownloadAssignment(rs.getInt("need_download_assignment"));
		dto.setTimezoneId(rs.getInt("timezone_id"));
		dto.setDesignation(rs.getString("designation"));
		dto.setIsFirstLogin(rs.getInt("is_first_login"));
		dto.setAssociatedPincode(rs.getString("pincode")); 
		dto.setImei(rs.getString("imei"));
		dto.setCountry(rs.getString("country_id"));
		
	}

	@Override
	public UserDto[] findByDynamicSelect(String sql, Object[] sqlParams, Connection conn){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		UserDto[] userDtos = null;
		try {
			final String SQL = sql;
			stmt = conn.prepareStatement( SQL );
			for (int i=0; sqlParams!=null && i<sqlParams.length; i++ ) {
				stmt.setObject( i+1, sqlParams[i] );
			}
			rs = stmt.executeQuery();
			userDtos= fetchMultiResults(rs);
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(rs);
			resourceManager.close(stmt);
		}
		return userDtos;
	}

	@Override
	public int getCount(String sql, Object[] sqlParams, Connection conn){
		final String SQL = SQL_COUNT + " WHERE " + sql;
		return getCountCondition(SQL, sqlParams, conn);
	}

	@Override
	public int getCountCondition(String sql, Object[] sqlParams, Connection conn){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			stmt = conn.prepareStatement( sql );
			for (int i=0; sqlParams!=null && i<sqlParams.length; i++ ) {
				stmt.setObject( i+1, sqlParams[i] );
			}
			rs = stmt.executeQuery();
			if (rs.next()) count = rs.getInt(1);
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally {
			resourceManager.close(rs);
			resourceManager.close(stmt);
		}
		return count;
	}

	@Override
	public void updateAvailableUsers(int teamId,Connection conn){
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("update user set team_id = ?, modification_time = ? where team_id = ? ");
			stmt.setInt( 1, 0 );
			stmt.setString(2, PlatformUtil.getRealDateToSQLDateTime(new Date()));
			stmt.setInt( 3, teamId);

			stmt.executeUpdate();
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally {
			resourceManager.close(stmt);
		}
	}

	@Override
	public Map<String, Object> userList(int customerId, UserForm form,Connection conn,long LoginUserId) {
		PreparedStatement stmt = null,cstmt=null;
		ResultSet rs = null,crs=null;
		int count = 0;
		List<UserDto> list = new ArrayList<UserDto>();
		int userTypeId = 0;
		UserDto userDto = findByPrimaryKey(form.getUserCreatedBy(), conn);
		if(userDto != null) 
			userTypeId = userDto.getUserTypeId();
		try {
			String SELECT_LIST = "SELECT u.id, u.user_type_id, u.phone_model_id, u.team_id, u.ptn, u.first_name, u.last_name, u.employee_number, "
					+"u.email_address, u.modification_time, ut.description as usertype, t.name as team,  u.status, "
					+"t.name as team,  u.status, u.login_name, u.password, u.phone_app_version, u.force_setup_UPDATE, u.app_version_update_time, "
					+"u.customer_id, u.designation, u.pincode,u.userCreatedBy,u.imei,u.handset_detail from "
					+"user u ";
			String join = "JOIN user_type  ut ON u.user_type_id = ut.id " +
					"LEFT JOIN team t ON u.team_id = t.id ";

			String orderBy = "ORDER BY u.first_name, u.last_name ASC ";
			String limit = "";
			Vector<Object> paramList = new Vector<Object>();
			String where = " WHERE u.customer_id = ? and ut.is_admin!=1 ";
			paramList.add(customerId);
			if(PlatformUtil.isNotEmpty(form.getFirstName())){
				where += " AND u.first_name like ? ";
				paramList.add("%"+form.getFirstName().trim() + "%");
			}
			if(PlatformUtil.isNotEmpty(form.getLastName())){
				where += "AND u.last_name like ? ";
				paramList.add(form.getLastName().trim() + "%");
			}
			if(form.getUserTypeId() != 0){
				where += "AND u.user_type_id = ? ";
				paramList.add(form.getUserTypeId());
			}
			if(PlatformUtil.isNotEmpty(form.getEmployeeNumber())){
				where += "AND u.employee_number like ? ";
				paramList.add("%"+form.getEmployeeNumber().trim() + "%");
			}
			if(PlatformUtil.isNotEmpty(form.getPhoneAppVersion())){
				where += "AND u.phone_app_version like ? ";
				paramList.add("%"+form.getPhoneAppVersion().trim() + "%");
			}
			if(PlatformUtil.isNotEmpty(form.getEmailAddress())){
				where += "AND u.email_address like ? ";
				paramList.add("%"+form.getEmailAddress().trim() + "%");
			}
			if(PlatformUtil.isNotEmpty(form.getLoginName())){
				where += "AND u.login_name like ? ";
				paramList.add("%"+form.getLoginName().trim() + "%");
			}
			if(PlatformUtil.isNotEmpty(form.getPtn())){
				where += "AND u.ptn like ? ";
				paramList.add(form.getPtn().trim() + "%");
			}
			if(PlatformUtil.isNotEmpty(form.getDesignation()) ){
				where += " AND u.designation like ? ";
				paramList.add(form.getDesignation().trim() + "%");
			}
			if(PlatformUtil.isNotEmpty(form.getImei())){     //Check pincode
				where += "AND u.imei like ? ";
				paramList.add(form.getImei().trim() + "%");
			}
			if(form.getStatus()!=SessionConstants.STATUS_ALL){//All Check
				where +=" AND u.status = ? ";
				paramList.add(form.getStatus());
			}
			if(form.getTeamId() > 0){
				where += "AND find_in_set(u.team_id, '"+form.getTeamId()+"')";
			}else if(form.getTeamId() == 0){
				if(userTypeId == (SessionConstants.USER_TYPE_ID_LEAD)){ 
					String defaultTeamIds ="";  
					defaultTeamIds=teamLogic.teamIdsByLeadId(customerId, LoginUserId);
					where += " AND  u.id in (select id from user where team_id in ("+defaultTeamIds+"))";
				}
			}else if(form.getTeamId() < 0){
				where += "AND find_in_set(u.team_id, '0')";
			}

			// Count SQL
			String countSql = SQL_COUNT + join + "\n" + where;
			cstmt = conn.prepareStatement(countSql);
			for (int i = 0; i<paramList.size(); i++ ) {
				cstmt.setObject( i+1, paramList.get(i) );
			}
			crs = cstmt.executeQuery();
			if (crs.next()) {
				count = crs.getInt(1);
			}

			// Main SQL
			if(form.getRowcount() > 0){
				limit = "LIMIT ? , ?";
				paramList.add(form.getOffset());
				paramList.add(form.getRowcount());
			}
			String sql = SELECT_LIST + join + "\n" + where + "\n" + orderBy + "\n" + limit;

			stmt = conn.prepareStatement(sql);

			for (int i = 0; i<paramList.size(); i++ ) {
				stmt.setObject( i+1, paramList.get(i) );
			}
			rs = stmt.executeQuery();

			while (rs.next()) {
				UserDto uDto = new UserDto();
				uDto.setId(rs.getLong("id"));
				uDto.setTeamId(rs.getInt("team_id"));
				uDto.setPtn(rs.getString("ptn"));
				uDto.setTeamName(rs.getString("team"));
				uDto.setFirstName(rs.getString("first_name"));
				uDto.setLastName(rs.getString("last_name"));
				uDto.setEmployeeNumber(rs.getString("employee_number"));
				uDto.setEmailAddress(rs.getString("email_address"));
				uDto.setStatus(rs.getInt("status"));
				uDto.setUserType(rs.getString("userType"));
				uDto.setUserTypeId(rs.getInt("user_type_id"));
				uDto.setImei(rs.getString("imei"));
				uDto.setLoginName(rs.getString("login_name"));
				uDto.setPhoneAppVersion(rs.getString("phone_app_version"));
				uDto.setDesignation(rs.getString("designation"));
				uDto.setPhoneModelId(rs.getInt("phone_model_id"));
				uDto.setHandsetDetails(rs.getString("handset_detail"));
				uDto.setForceSetupUpdate(rs.getInt("force_setup_UPDATE"));
				try{
					uDto.setAppVersionUpdateTime(PlatformUtil.getDateTimeInddMMMYYYY(rs.getString("app_version_update_time")));
				}catch(Exception e){
				}
				list.add(uDto);
			}

		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally {
			resourceManager.close(rs);
			resourceManager.close(stmt);
			resourceManager.close(crs);
			resourceManager.close(cstmt);
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalRows", count);
		map.put("data", list);
		return map;
	}

	@Override
	public int updateUser(int id, UserDto dto,Connection conn){
		PreparedStatement stmt = null;
		int rows = 0;
		try {
			boolean passwdModified = PlatformUtil.isNotEmpty(dto.getPassword());
			if (dto.getIsFirstLogin() == 0) {
				stmt = conn.prepareStatement(SQL_UPDATE_FIRST_LOGIN);
				stmt.setInt(1, 1);
				stmt.setInt(2, id);
				rows = stmt.executeUpdate();
				return rows;
			}
			if(passwdModified){
				SQL_UPDATE_USER+= ", password = sha(?) ";
			}

			SQL_UPDATE_USER+= " WHERE customer_id = ? AND user_type_id = ?";

			stmt = conn.prepareStatement( SQL_UPDATE_USER );
			int index=1;
			stmt.setString( index++, dto.getEmailAddress() );

			if(passwdModified){
				stmt.setString( index++, dto.getPassword() );
			}
			stmt.setInt( index++, id );
			stmt.setInt(index++, dto.getUserTypeId());
			rows = stmt.executeUpdate();
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		finally {
			resourceManager.close(stmt);
		}
		return rows;
	}

	@Override
	public int updateUserColumnForceSetUpUpdate(Connection con, int customerId, String call){
		PreparedStatement pstmt = null;
		int noOfRowsUpdated = 0;
		String sql = "UPDATE user set force_setup_update = 1, modification_time = ? WHERE customer_id = ? and user_type_id = 3 and status = 1";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, PlatformUtil.getRealDateToSQLDateTime(new Date()));
			pstmt.setInt(2, customerId);
			noOfRowsUpdated = pstmt.executeUpdate();
			int teamId = 0;
			userUpdateCall(con, customerId, call, teamId);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(pstmt);
		}

		return noOfRowsUpdated;
	}

	@Override
	public void userUpdate(Connection conn, int customerId, String call, int teamId, long userId){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select id from user where customer_id = ? and user_type_id = 3 and status = 1";
		if(teamId > 0) 
			sql += " and team_id = "+teamId;
		if(userId > 0)
			sql += " and id = "+userId;
		try{
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, customerId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				userId = rs.getInt("id");
				userUpdateCall(conn, customerId, call,teamId);
			}
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(rs);
			resourceManager.close(pstmt);
		}
	}

	public void userUpdateCall(Connection conn, int customerId, String call, int teamId){
		PreparedStatement pstmt = null, pstmt2 = null, pstmt3 = null, pstmt4 = null;
		ResultSet rs = null, rs2 = null;
		String sql = "select id from user where customer_id = ? and user_type_id = 3 and status = 1";
		if(teamId > 0) sql += " and team_id = "+teamId;
		String sql2 = "select calls from user_update_call where customer_id = ? and user_id = ?";
		String sql3 = "update user_update_call set calls = ?, modified_time = ? where customer_id = ? and user_id = ?";
		String sql4 = "insert into user_update_call (customer_id, user_id, calls, created_time, modified_time) values (?, ?, ?, ?, ?)";
		try{
			String currentTime = PlatformUtil.getRealDateToSQLDateTime(new Date());
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, customerId);
			rs = pstmt.executeQuery();
			pstmt2 = conn.prepareStatement(sql2);
			pstmt3 = conn.prepareStatement(sql3);
			pstmt4 = conn.prepareStatement(sql4);
			while(rs.next()){
				int userId = rs.getInt("id");
				pstmt2.setInt(1, customerId);
				pstmt2.setInt(2, userId);
				rs2 = pstmt2.executeQuery();
				if(rs2.next()){
					String calls = rs2.getString("calls");
					boolean check = true;
					if(!calls.equals("")){
						String[] tmps = calls.split(",");
						for(String tmp: tmps){
							if(tmp.equals(call)){
								check = false;
								break;
							}
						}
						if(check) calls += ",";
					}
					if(check){
						calls += call;
						pstmt3.setString(1, calls);
						pstmt3.setString(2, currentTime);
						pstmt3.setInt(3, customerId);
						pstmt3.setInt(4, userId);
						pstmt3.execute();
					}
				}else{
					pstmt4.setInt(1, customerId);
					pstmt4.setInt(2, userId);
					pstmt4.setString(3, call);
					pstmt4.setString(4, currentTime);
					pstmt4.setString(5, currentTime);
					pstmt4.execute();
				}
			}
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(pstmt4);
			resourceManager.close(pstmt3);
			resourceManager.close(rs2);
			resourceManager.close(pstmt2);
			resourceManager.close(rs);
			resourceManager.close(pstmt);
		}
	}

	@Override
	public boolean updateForceSetupInactive(Connection conn, long userId, int customerId, String ts){
		Date date = new Date(Long.parseLong(ts));
		String clientTime = PlatformUtil.getRealDateToSQLDateTime(date);
		String currentTime = PlatformUtil.getRealDateToSQLDateTime(new Date());
		PreparedStatement pstmt = null, pstmt2 = null;
		String sql = "update user set force_setup_UPDATE = 0, app_version_update_time = ?, modification_time = ? where id = ? and customer_id = ? ";
		String sql2 = "update user_update_call set calls = ?, modified_time = ? where customer_id = ? and user_id = ?";
		try{
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, clientTime);
			pstmt.setString(2, currentTime);
			pstmt.setLong(3, userId);
			pstmt.setInt(4, customerId);
			int updated = pstmt.executeUpdate();
			if(updated > 0){
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, "");
				pstmt2.setString(2, currentTime);
				pstmt2.setInt(3, customerId);
				pstmt2.setLong(4, userId);
				pstmt2.execute();
				return true;
			}
		} catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally{
			resourceManager.close(pstmt);
		}
		return false;
	}

	@Override
	public int updateForceSetupActiveByTeam(Connection conn, int loginUserId, int teamId, int customerId, String calls){
		int updated = 0;
		PreparedStatement pstmt = null;
		String sql = "update user set force_setup_UPDATE = 1, modification_time = ? where team_id = ? and customer_id = ? ";
		try{
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, PlatformUtil.getRealDateToSQLDateTime(new Date()));
			pstmt.setInt(2, teamId);
			pstmt.setInt(3, customerId);
			updated = pstmt.executeUpdate();
			userUpdateCall(conn, customerId, calls, teamId);
		} catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally{
			resourceManager.close(pstmt);
		}
		return updated;
	}

	@Override
	public int updateHandsetDetailUser(long id, String details, String clientVer, Connection conn){
		PreparedStatement pstmt = null;
		int rows = 0;
		String query = "update user set handset_detail = ?, phone_app_version = ?, modification_time = ? where id = ? ";
		try {
			pstmt = conn.prepareStatement(query);
			int index=1;
			pstmt.setString(index++, details);
			pstmt.setString(index++, clientVer);
			pstmt.setString(index++, PlatformUtil.getRealDateToSQLDateTime(new Date()));
			pstmt.setLong( index++, id);
			rows = pstmt.executeUpdate();
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally {
			resourceManager.close(pstmt);
		}
		return rows;
	}

	@Override
	public int resetPassword(long userId, String pwd, Connection conn){
		PreparedStatement pstmt = null;
		int rows = 0;
		try{
			String sql = "UPDATE user SET password = ?, modification_time = ? ";
			sql+="WHERE id = ? ";
			pstmt = conn.prepareStatement( sql );
			int index=1;
			pstmt.setString( index++, PlatformUtil.sha(pwd));
			pstmt.setString(index++, PlatformUtil.getRealDateToSQLDateTime(new Date()));
			pstmt.setLong( index++, userId );
			rows = pstmt.executeUpdate();
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(pstmt);
		}
		return rows;
	}

	@Override
	public int updateCustDetail(UserDto dto, Connection conn){
		PreparedStatement stmt = null;
		int rows = 0;
		try {
			boolean passwordModified = PlatformUtil.isNotEmpty(dto.getPassword());
			String updateSql = "UPDATE " + getTableName() + " SET customer_id = ?, user_type_id = ?, ptn = ?, " +
					" modification_time = ?, email_address = ?, employee_number = ?, team_id = ?, " +
					" designation=?, is_first_login = ?,pincode = ?,imei = ?";
			if(passwordModified){
				updateSql += " , password = sha(?) ";
			}
			updateSql += " WHERE id = ? ";

			stmt = conn.prepareStatement( updateSql );
			int index=1;
			stmt.setInt( index++, dto.getCustomerId() );
			stmt.setInt( index++, dto.getUserTypeId());
			stmt.setString( index++, dto.getPtn() );
			stmt.setString(index++, PlatformUtil.getRealDateToSQLDateTime(new Date()));
			stmt.setString( index++, dto.getEmailAddress() );
			stmt.setString( index++, dto.getEmployeeNumber() );
			stmt.setLong( index++, dto.getTeamId() );
			stmt.setString(index++, dto.getDesignation());
			stmt.setInt(index++, 0);
			stmt.setString(index++, dto.getAssociatedPincode());
			stmt.setString(index++, dto.getImei());
			// do not add any set statement bellow
			if(passwordModified){
				stmt.setString( index++, dto.getPassword() );
			}
			stmt.setLong(index++, dto.getId());
			rows = stmt.executeUpdate();
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally {
			resourceManager.close(stmt);
		}
		return rows;
	}

	@Override
	public int updateMyInfoUser(UserDto dto,Connection conn){
		PreparedStatement stmt = null;
		int rows = 0;
		try {
			String updateSql = SQL_UPDATE_MYINFO + " WHERE id = ? ";
			stmt = conn.prepareStatement( updateSql );
			int index=1;
			stmt.setInt( index++, dto.getCustomerId() );
			stmt.setInt( index++, dto.getUserTypeId() );
			stmt.setString( index++, dto.getFirstName() );
			stmt.setString( index++, dto.getLastName() );
			stmt.setString( index++, dto.getEmailAddress() );
			stmt.setLong( index++, dto.getId() );
			rows = stmt.executeUpdate();
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally {
			resourceManager.close(stmt);
		}
		return rows;
	}

	@Override
	public Set<String> getUserUpdateCalls(Connection conn, int customerId, long userId){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select calls from user_update_call where customer_id = ? and user_id = ?";
		Set<String> set = new HashSet<String>();
		try{
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, customerId);
			pstmt.setLong(2, userId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				String calls = rs.getString("calls");
				if(!calls.equals("")){
					String[] call = calls.split(",");
					for (String callName : call) {
						set.add(callName);
					}
				}
			}
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(pstmt);
			resourceManager.close(rs);
		}

		return set;
	}

	@Override
	public int getTotalUsers(Connection con, int customerId) {
		PreparedStatement stmt=null;
		ResultSet rs=null;
		int count=0;
		String sql="select count(*) as count from user where customer_id="+customerId+" and status = 1";
		try {
			stmt=con.prepareStatement(sql);
			rs=stmt.executeQuery();
			if(rs.next())
				count=rs.getInt("count");
		}catch (SQLException e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		finally{
			resourceManager.close(stmt);
			resourceManager.close(rs);
		}
		return count;
	}

	@Override
	public UserTypeDto findByPrimaryKeyUserType(int id, Connection conn) {
		UserTypeDto ret[] = findByDynamicWhereUserType( " id = ?", new Object[] {  new Integer(id) },conn );
		return ret.length==0 ? null : ret[0];
	}

	@Override
	public UserTypeDto[] findAllUserType(Connection conn) {
		return findByDynamicSelectUserType( SQL_SELECT + " where u.user_type_id != 4 ORDER BY u.id", null,conn );
	}

	@Override
	public UserTypeDto[] findWhereDescriptionEqualsUserType(String description, Connection conn) {
		return findByDynamicWhereUserType( " description = ? ORDER BY description", new Object[] { description },conn );
	}

	@Override
	public UserTypeDto[] findWhereIsAdminEqualsUserType(short isAdmin, Connection conn) {
		return findByDynamicWhereUserType( " is_admin = ? and id != 4 ORDER BY is_field_user desc", new Object[] {  new Short(isAdmin) },conn );
	}

	@Override
	public UserTypeDto[] findByDynamicSelectUserType(String sql, Object[] sqlParams, Connection conn) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		UserTypeDto[] userDtos = null;
		try {
			final String SQL = sql;
			stmt = conn.prepareStatement( SQL );
			for (int i=0; sqlParams!=null && i<sqlParams.length; i++ ) {
				stmt.setObject( i+1, sqlParams[i] );
			}
			rs = stmt.executeQuery();
			userDtos= fetchMultiResultsUserType(rs);
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally {
			resourceManager.close(rs);
			resourceManager.close(stmt);
		}
		return userDtos;
	}

	@Override
	public UserTypeDto[] findByDynamicWhereUserType(String sql, Object[] sqlParams, Connection conn) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		UserTypeDto[] userDtos = null;
		try {
			final String SQL = SQL_SELECT_USERTYPE + " WHERE " + sql;
			stmt = conn.prepareStatement( SQL );
			for (int i=0; sqlParams!=null && i<sqlParams.length; i++ ) {
				stmt.setObject( i+1, sqlParams[i] );
			}
			rs = stmt.executeQuery();
			userDtos= fetchMultiResultsUserType(rs);
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally {
			resourceManager.close(rs);
			resourceManager.close(stmt);
		}
		return userDtos;
	}

	@Override
	public UserTypeDto[] findWhereIsUserEqualsUserType(short isAdmin,Connection conn) {
		return findByDynamicWhereUserType( " is_field_user = ? and id != 4 ORDER BY is_field_user desc", new Object[] {  new Short(isAdmin) },conn );
	}

	private UserTypeDto[] fetchMultiResultsUserType(ResultSet rs) throws SQLException{
		Collection<UserTypeDto> resultList = new ArrayList<UserTypeDto>();
		while (rs.next()) {
			UserTypeDto dto = new UserTypeDto();
			populateDtoUserType( dto, rs);
			resultList.add( dto );
		}
		UserTypeDto ret[] = new UserTypeDto[ resultList.size() ];
		resultList.toArray( ret );
		return ret;
	}
	private void populateDtoUserType(UserTypeDto dto, ResultSet rs) throws SQLException{
		dto.setId( rs.getLong( "id" ) );
		dto.setDescription( rs.getString( "description" ) );
		dto.setCreationTime( rs.getString("creation_time" ) );
		dto.setModificationTime( rs.getString("modification_time" ) );
		dto.setIsAdmin( rs.getShort( "is_admin" ) );
		if (rs.wasNull()) dto.setAdminNull( true );
		dto.setIsTeamLead( rs.getShort( "is_team_lead" ) );
		if (rs.wasNull()) dto.setTeamLeadNull( true );
		dto.setIsFieldUser( rs.getShort( "is_field_user" ) );
		if (rs.wasNull()) dto.setFieldUserNull( true );
	}

	@Override
	public long changeStatus(long id, int customerId, int status, Connection conn) {
		PreparedStatement pstmt = null;
		int rows = 0;
		try {
			status=(status == 1)?0:1;
			pstmt = conn.prepareStatement( SQL_UPDATESTATUS );
			pstmt.setInt(1, status);
			pstmt.setString(2, PlatformUtil.getRealDateToSQLDateTime(new Date()));
			pstmt.setLong(3, id);
			rows = pstmt.executeUpdate();
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(pstmt);
		}
		return rows;
	}
	@Override
	public void updateAvailableUsers(long teamId, String userId, Connection conn) {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("update user set team_id = ?, modification_time = ? where FIND_IN_SET(id,?) ");
			int index = 1;
			stmt.setLong( index++, teamId);
			stmt.setString(index++, PlatformUtil.getRealDateToSQLDateTime(new Date()));
			stmt.setString( index++, userId);
			stmt.executeUpdate();
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally {
			resourceManager.close(stmt);
		}		
	}
	@Override
	public int updateAvailableUsers(long teamId, Connection conn) {
		PreparedStatement stmt = null;
		int row=0;
		try {
			stmt = conn.prepareStatement("update user set team_id = 0, modification_time = ? where team_id = ? ");
			int index = 1;
			stmt.setString(index++, PlatformUtil.getRealDateToSQLDateTime(new Date()));
			stmt.setLong(index++, teamId);
			row = stmt.executeUpdate();
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally {
			resourceManager.close(stmt);
		}
		return row;
	}

	@Override
	public int updateAvailableUser(long teamId, Connection conn) {
		PreparedStatement stmt = null;
		int row=0;
		try {
			stmt = conn.prepareStatement("update user set team_id = 0, modification_time = ? where id = ? ");
			int index = 1;
			stmt.setString(index++, PlatformUtil.getRealDateToSQLDateTime(new Date()));
			stmt.setLong(index++, teamId);
			row = stmt.executeUpdate();
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally {
			resourceManager.close(stmt);
		}
		return row;

	}
}