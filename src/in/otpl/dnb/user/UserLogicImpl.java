package in.otpl.dnb.user;

import in.otpl.dnb.util.ErrorLogHandler;
import in.otpl.dnb.util.PlatformUtil;
import in.otpl.dnb.util.ResourceManager;
import in.otpl.dnb.util.SessionConstants;
import in.otpl.dnb.user.UserDto;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserLogicImpl implements UserLogic {

	private static final Logger LOG = Logger.getLogger(UserLogicImpl.class);

	@Autowired
	private ResourceManager resourceManager;
	@Autowired
	private UserDao userDao;
	@Autowired
	private TeamDao teamDao;
	@Autowired
	private TeamLogic teamLogic;

	@Override
	public long createUser(UserForm userForm) {
		Connection conn = null;
		long id = 0;
		try {
			conn = resourceManager.getConnection();
			id = userDao.insert(userForm, conn);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return id;
	}

	@Override
	public long updateUser(UserForm userForm) {
		long cId = 0;
		Connection conn = null;
		try {
			conn = resourceManager.getConnection();
			cId = userDao.update(userForm, conn);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return cId;
	}

	public UserTypeDto[] getAllUserTypes(boolean isAdmin) {
		Connection conn = null;
		UserTypeDto[] userType = null;
		try {
			conn = resourceManager.getConnection();
			if (isAdmin)
				userType = userDao.findWhereIsAdminEqualsUserType(Short.valueOf("0"), conn);
			else
				userType = userDao.findAllUserType(conn);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return userType;
	}

	@Override
	public UserDto getUserDetails(long userId) {
		Connection conn = null;
		UserDto userObj = null;
		try {
			conn = resourceManager.getConnection();
			userObj = userDao.findByPrimaryKey(userId, conn);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return userObj;
	}

	/* Fetch UserType by Description */
	@Override
	public UserTypeDto getUserTypes(String description) {
		Connection conn = null;
		UserTypeDto[] userType = null;
		try {
			conn = resourceManager.getConnection();
			userType = userDao.findWhereDescriptionEqualsUserType(description, conn);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		if (userType != null)
			return userType[0];
		else
			return null;
	}

	/* Fetch All UserTypes for Lead */
	@Override
	public UserTypeDto[] getUserTypesForLead(boolean isLead) {
		Connection conn = null;
		UserTypeDto[] userType = null;
		try {
			conn = resourceManager.getConnection();
			if (isLead)
				userType = userDao.findWhereIsUserEqualsUserType(Short.valueOf("1"), conn);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return userType;
	}

	/* Fetch users where user as lead & active */
	@Override
	public UserDto[] getTeamLeads(int customerId) {
		Connection con = null;
		UserDto[] users = null;
		try {
			con = resourceManager.getConnection();
			int status = 1;
			int userTypeId = 2;
			users = userDao.findWhereUserTypeIdStatusEquals(customerId, status, userTypeId, con);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(con);
		}
		return users;
	}
	@Override
	public UserDto[] getAllUsers(int customerId) {
		Connection con = null;
		UserDto[] users = null;
		try {
			con = resourceManager.getConnection();
			int status = 1;
			int userTypeId = 3;
			users = userDao.findWhereUserTypeIdStatusEquals(customerId, status, userTypeId, con);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(con);
		}
		return users;
	}

	/* Fetch users those are not allocated in any Team */
	@Override
	public UserDto[] getAvailableUsersTeams(int customerId) {
		Connection con = null;
		UserDto[] users = null;
		try {
			con = resourceManager.getConnection();
			String query = " u.customer_id = ? and u.user_type_id = ? and (u.team_id is null OR u.team_id = 0) and u.status = 1 ORDER BY u.first_name, u.last_name ASC";
			Object[] params = new Object[2];
			params[0] = customerId;
			params[1] = 3;
			users = userDao.findByDynamicWhere(query, params, con);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(con);
		}
		return users;
	}

	/* Get Users by Team */
	@Override
	public UserDto[] getSelectedUsersByTeamId(int customerId, int teamId) {
		Connection con = null;
		UserDto[] users = null;
		try {
			con = resourceManager.getConnection();
			String query = " u.customer_id = ? and u.team_id = ? ORDER BY u.first_name, u.last_name ASC";
			Object[] params = new Object[2];
			params[0] = customerId;
			params[1] = teamId;
			users = userDao.findByDynamicWhere(query, params, con);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(con);
		}
		return users;
	}

	/* Count of total users for a particular Customer by userType */
	@Override
	public int getTotalUsers(int customerId, int usertypeId) {
		final int ARRAY_VALUE = 3;
		Connection conn = null;
		int count = 0;
		String query = " u.customer_id = ? and u.user_type_id = ? and u.status = ?";
		Object[] params = new Object[ARRAY_VALUE];
		params[0] = customerId;
		params[1] = usertypeId;
		params[2] = 1;
		try {
			conn = resourceManager.getConnection();
			count = userDao.getCount(query, params, conn);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return count;
	}

	/* Count of total users for a particular Customer */
	@Override
	public int getTotalUsers(int customerId) {
		final int ARRAY_VALUE = 1;
		Connection conn = null;
		int count = 0;
		String query = " u.customer_id = ? ";
		Object[] params = new Object[ARRAY_VALUE];
		params[0] = customerId;
		try {
			conn = resourceManager.getConnection();
			count = userDao.getCount(query, params, conn);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return count;
	}

	/* User list based on search criteria */
	@Override
	public Map<String, Object> userList(int customerId, UserForm form, long LoginUserId) {
		Connection conn = null;
		Map<String, Object> map = null;
		try {
			conn = resourceManager.getConnection();
			map = userDao.userList(customerId, form, conn, LoginUserId);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return map;
	}

	/* Fetch the single user detail */
	@Override
	public UserDto getUserDetails(int userId) {
		Connection conn = null;
		UserDto userObj = null;
		try {
			conn = resourceManager.getConnection();
			userObj = userDao.findByPrimaryKey(userId, conn);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return userObj;
	}

	/* User details by login name & customer id */
	@Override
	public UserDto getUserByName(String loginName, int customerId) {
		Connection conn = null;
		UserDto userObj = null;
		try {
			conn = resourceManager.getConnection();
			userObj = userDao.findWhereLoginNameEquals(loginName, customerId, conn);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return userObj;
	}

	/* Validate Column data for upload excel */
	@SuppressWarnings("resource")
	@Override
	public boolean checkUserColumnHeader(byte[] file) {
		String[] header = { "First Name", "Last Name", "User Type", "Email Id", "Employee Number", "Login Id",
				"Password", "Mobile Number", "IMEI", "Designation" };
		List<String> columnHeaders = new ArrayList<String>();
		boolean check = true;
		try {
			XSSFWorkbook wb = null;
			try {
				InputStream is = new ByteArrayInputStream(file);
				wb = new XSSFWorkbook(is);
			} catch (IOException e) {
				LOG.error(ErrorLogHandler.getStackTraceAsString(e));
				return false;
			}

			int numOfSheets = wb.getNumberOfSheets();

			for (int i = 0; i < numOfSheets; i++) {
				XSSFSheet sheet = wb.getSheetAt(i);

				Iterator<?> rows = sheet.rowIterator();
				if (rows.hasNext()) {
					XSSFRow columnHeaderRow = (XSSFRow) rows.next();
					short c1 = columnHeaderRow.getFirstCellNum();
					short c2 = columnHeaderRow.getLastCellNum();
					for (short c = c1; c < c2; c++) {
						XSSFCell cell = columnHeaderRow.getCell(c);
						if (cell != null) {
							String cellValue = getCellValue(cell);
							if (cellValue != null && cellValue.trim().length() > 0) {
								columnHeaders.add(cellValue);
							}
						}
					}
				}
			}

			for (String a : header) {
				if (!columnHeaders.contains(a)) {
					check = false;
					break;
				}
			}
		} catch (Exception ex) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(ex));
			check = false;
		}
		return check;
	}

	private String getCellValue(XSSFCell cell) {
		if (cell == null)
			return null;

		String result = null;

		int cellType = cell.getCellType();
		switch (cellType) {
		case XSSFCell.CELL_TYPE_BLANK:
			result = "";
			break;
		case XSSFCell.CELL_TYPE_BOOLEAN:
			result = cell.getBooleanCellValue() ? "true" : "false";
			break;
		case XSSFCell.CELL_TYPE_ERROR:
			result = "ERROR: " + cell.getErrorCellValue();
			break;
		case XSSFCell.CELL_TYPE_FORMULA:
			result = cell.getCellFormula();
			break;
		case XSSFCell.CELL_TYPE_NUMERIC:
			XSSFCellStyle cellStyle = cell.getCellStyle();
			short dataFormat = cellStyle.getDataFormat();

			// assumption is made that dataFormat = 15,
			// when cellType is HSSFCell.CELL_TYPE_NUMERIC
			// is equal to a DATE format.
			final int DATA_FORMAT_VALUE = 15;
			if (dataFormat == DATA_FORMAT_VALUE) {
				result = cell.getDateCellValue().toString();
			} else {
				BigDecimal decimal = new BigDecimal(cell.getNumericCellValue());
				if (decimal.scale() > 0) {
					result = decimal.setScale(2, RoundingMode.CEILING).toPlainString();
				} else {
					result = decimal.toPlainString();
				}
				// result = String.valueOf(cell.getNumericCellValue());
			}

			break;
		case XSSFCell.CELL_TYPE_STRING:
			result = cell.getStringCellValue();
			break;
		default:
			break;
		}

		return result;
	}

	/* User detail by UserId & customer id */
	@Override
	public UserDto validateUserId(int userid, int customerId) {
		Connection con = null;
		UserDto user = null;
		final int USERTYPE_USER = 3;
		try {
			con = resourceManager.getConnection();
			UserDto[] users = userDao.findByDynamicWhere(" u.id = ? and u.customer_id = ? and u.user_type_id = ? ",
					new Object[] { userid, customerId, USERTYPE_USER }, con);
			if (users != null && users.length > 0)
				user = users[0];
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(con);
		}
		return user;
	}

	/* To check a unique employee number for a particular customer */
	@Override
	public boolean validateEmployeeNumber(String empNo, int customerId) {
		Connection conn = null;
		boolean bool = false;
		try {
			conn = resourceManager.getConnection();
			UserDto[] users = userDao.findByDynamicWhere(" u.employee_number = ? and u.customer_id = ? ",
					new Object[] { empNo, customerId }, conn);

			if (users != null && users.length > 0)
				bool = true;
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return bool;
	}

	/* To check a unique employee number edit for a particular customer */
	@Override
	public boolean validateEmployeeNumberEdit(String empNo, int customerId, long userId) {
		Connection conn = null;
		boolean bool = false;
		try {
			conn = resourceManager.getConnection();
			UserDto[] users = userDao.findByDynamicWhere(" u.employee_number = ? and u.customer_id = ? and u.id != ? ",
					new Object[] { empNo, customerId, userId }, conn);

			if (users != null && users.length > 0)
				bool = true;

		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return bool;
	}

	/* Validate email by customer id & user type */
	@Override
	public boolean validateEmailIdAddress(String emailId, int customerId) {
		Connection conn = null;
		boolean status = false;
		int count = 0;
		String queryy = " u.email_address = ? and u.customer_id = ? and u.user_type_id > 1 ";
		Object[] paramss = new Object[2];
		paramss[0] = emailId;
		paramss[1] = customerId;
		try {
			conn = resourceManager.getConnection();
			count = userDao.getCount(queryy, paramss, conn);
			if (count != 0)
				status = true;
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return status;
	}

	/* Validate edit email by customer id & user type */
	@Override
	public boolean validateEmailIdEdit(String emailId, int customerId, long userId) {
		Connection conn = null;
		boolean bool = false;
		try {
			conn = resourceManager.getConnection();
			String sql = " u.email_address = ? and u.customer_id = ? and u.id != ? and u.user_type_id > 1 ";
			Object[] paramss = new Object[3];
			paramss[0] = emailId;
			paramss[1] = customerId;
			paramss[2] = userId;
			UserDto[] users = userDao.findByDynamicWhere(sql, paramss, conn);

			if (users != null && users.length > 0)
				bool = true;

		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return bool;
	}

	/* Validate user phone no. by Customer Id */
	@Override
	public boolean validatePhoneNumber(String phoneNumber, int customerId) {
		Connection conn = null;
		boolean status = false;
		String query = " u.ptn = ? and u.customer_id = ? ";
		Object[] params = new Object[2];
		params[0] = phoneNumber;
		params[1] = customerId;
		try {
			conn = resourceManager.getConnection();
			int count = userDao.getCount(query, params, conn);
			if (count > 0)
				status = true;
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return status;
	}

	/* Validate user edit phone no. by Customer Id */
	@Override
	public boolean validatePhoneNumberEdit(String phoneNumber, int customerId, long userId) {
		Connection conn = null;
		boolean bool = false;
		String query = " u.ptn = ? and u.customer_id = ? and u.id != ? ";
		Object[] params = new Object[3];
		params[0] = phoneNumber;
		params[1] = customerId;
		params[2] = userId;
		try {
			conn = resourceManager.getConnection();
			UserDto[] users = userDao.findByDynamicWhere(query, params, conn);
			if (users != null && users.length > 0)
				bool = true;
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return bool;
	}

	/* Validate user phone no. by login name */
	@Override
	public boolean validatePhoneNumber(String phoneNumber, String loginName) {
		Connection conn = null;
		boolean status = false;
		String query = " u.ptn = ? and u.login_name != ?";
		Object[] params = new Object[2];
		params[0] = phoneNumber;
		params[1] = loginName;
		try {
			conn = resourceManager.getConnection();
			int count = userDao.getCount(query, params, conn);
			if (count > 0)
				status = true;
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return status;
	}

	/*
	 * Checks for the LoginName for a particular customer in database if contains
	 * returns true
	 */
	@Override
	public boolean validateLoginName(int customerId, String loginName) {
		Connection conn = null;
		boolean status = false;
		int count = 0;
		String query = " u.customer_id = ? and u.login_name = ? ";
		Object[] params = new Object[2];
		params[0] = customerId;
		params[1] = loginName;
		try {
			conn = resourceManager.getConnection();
			count = userDao.getCount(query, params, conn);
			if (count != 0)
				status = true;
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return status;
	}

	/* Validate IMEI no. by customer id */
	@Override
	public boolean validateIMEINumber(String IMEI, int customerId) {
		Connection conn = null;
		boolean status = false;
		int count = 0;
		String query = " u.imei = ? and u.customer_id = ? ";
		Object[] params = new Object[2];
		params[0] = IMEI;
		params[1] = customerId;
		try {
			conn = resourceManager.getConnection();
			count = userDao.getCount(query, params, conn);
			if (count != 0)
				status = true;
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return status;
	}

	/* Validate edit IMEI no. by customer id */
	@Override
	public boolean validateIMEINumberEdit(String IMEI, int customerId, long userId) {
		Connection conn = null;
		boolean bool = false;
		try {
			conn = resourceManager.getConnection();
			String query = " u.imei = ? and u.customer_id = ? and u.id != ? ";
			Object[] params = new Object[3];
			params[0] = IMEI;
			params[1] = customerId;
			params[2] = userId;
			UserDto[] users = userDao.findByDynamicWhere(query, params, conn);
			if (users != null && users.length > 0)
				bool = true;
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return bool;
	}

	/* Validate IMEI no. by login Name */
	@Override
	public boolean validateIMEINumber(String IMEI, String loginName) {
		Connection conn = null;
		boolean status = false;
		int count = 0;
		String query = " u.imei = ? and u.login_name != ?";
		Object[] params = new Object[2];
		params[0] = IMEI;
		params[1] = loginName;
		try {
			conn = resourceManager.getConnection();
			count = userDao.getCount(query, params, conn);
			if (count != 0)
				status = true;
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return status;
	}

	/* User detail by First Name, Last Name & customer id */
	@Override
	public UserDto getUserByName(String firstName, String lastName, int customerId) {
		Connection conn = null;
		UserDto[] userPresent = null;
		try {
			conn = resourceManager.getConnection();
			Object[] sqlParams = new Object[] { firstName, lastName, customerId };
			userPresent = userDao.findByDynamicWhere(" u.first_name = ? and u.last_name = ? and u.customer_id = ? ",
					sqlParams, conn);
			if (userPresent != null && userPresent.length > 0) {
				return userPresent[0];
			}
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return null;
	}

	/* User detail by First Name, Last Name, Login Name & customer id */
	@Override
	public UserDto getUserByNameWithLoginId(String firstName, String lastName, int customerId, String loginName) {
		Connection conn = null;
		try {
			conn = resourceManager.getConnection();
			Object[] sqlParams = new Object[] { firstName, lastName, customerId, loginName };
			UserDto[] userPresent = userDao.findByDynamicWhere(
					" u.first_name = ? and u.last_name = ? and u.customer_id = ? and u.login_name = ?", sqlParams,
					conn);
			if (userPresent != null && userPresent.length > 0) {
				return userPresent[0];
			}
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return null;
	}

	/* Update user for ForceSetupUpdate */
	@Override
	public int updateUserColumnForceSetUpUpdate(Connection con, int customerId, String call) {
		if (customerId > 0)
			return userDao.updateUserColumnForceSetUpUpdate(con, customerId, call);
		return 0;
	}

	/* Update user for ForceSetupUpdate by customer id */
	@Override
	public boolean updateForceSetupActive(int customerId, String call) {
		Connection conn = null;
		try {
			conn = resourceManager.getConnection();
			int rows = updateUserColumnForceSetUpUpdate(conn, customerId, call);
			if (rows > 0)
				return true;
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return false;
	}
	@Override
	public boolean updateForceSetupActiveByTeam(int loginUserId, int teamId, int customerId, String calls) {
		Connection conn = null;
		try {
			conn = resourceManager.getConnection();
			int updated = userDao.updateForceSetupActiveByTeam(conn, loginUserId, teamId, customerId, calls);
			TeamDto team = teamDao.findByPrimaryKey(teamId, conn);
			if (updated > 0) {
				// String logInfo ="Form group association of '"+team.getName()+"' is updated";
				// AdministrationLogic.auditLog(customerId, loginUserId, logInfo);
				return true;
			}
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return false;
	}

	@Override
	public UserDto[] getAllUserListByCusidStatType(int customerId, int userTypeId, long userId, int status){
		int type = SessionConstants.USER_TYPE_ID_USER;
		UserDto[] users = null;
		Connection con = null;
		try{
			con = resourceManager.getConnection();
			if(SessionConstants.USER_TYPE_ID_ADMIN == userTypeId || SessionConstants.USER_TYPE_ID_HR == userTypeId) {
				users = userDao.findWhereUserTypeIdStatusEquals(customerId, status, type, con);}
			else if (SessionConstants.USER_TYPE_ID_LEAD == userTypeId){
				String teamIds = teamLogic.teamIdsByLeadId(customerId, userId);
				if("".equals(teamIds)) 
					teamIds="-1";
				users = userDao.findWhereTeamIdInEquals(customerId, status, type, teamIds, con);
				UserDto userDto = userDao.findByPrimaryKey(userId, con);
				if(userDto != null){
					if(users == null){
						users = new UserDto[1];
						users[0] = userDto;
					}else{
						List<UserDto> newUserList = new ArrayList<UserDto>();
						//newUserList.add(userDto); //CB Lead name should not come in user list
						List<UserDto> userList = Arrays.asList(users);
						newUserList.addAll(userList);
						users = newUserList.toArray(new UserDto[newUserList.size()]);
					}
				}
			}else{
				UserDto userDto = userDao.findByPrimaryKey(userId, con);
				if(userDto != null){
					users = new UserDto[1];
					users[0] = userDto;
				}
			}
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(con);
		}
		return users;
	}


	/* Check Available Users for team */
	@Override
	public boolean checkAvailableUsers(int customerId, String[] selected) {
		UserLogicImpl userLogic = new UserLogicImpl();
		UserDto user[] = userLogic.getAvailableUsersTeams(customerId);
		int count = 0;
		for (int i = 0; i < user.length; i++) {
			for (int j = 0; j < selected.length; j++) {
				if (user[i].getId() == Integer.parseInt(selected[j]))
					count++;
			}
		}
		return (count == selected.length) ? true : false;
	}

	@Override
	public int userChangePassword(long userId, String password) {
		Connection con = null;
		int rows = 0;
		try {
			con = resourceManager.getConnection();
			rows = userDao.resetPassword(userId, password, con);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(con);
		}
		return rows;
	}

	/* Admin Profile by Customer Id */
	@Override
	public UserDto adminProfile(int customerId) {
		Connection conn = null;
		UserDto user = null;
		int userTypeId = 1;
		try {
			conn = resourceManager.getConnection();
			Object[] sqlParams = new Object[] { customerId, userTypeId };
			UserDto[] users = userDao.findByDynamicWhere(" u.customer_id = ? and u.user_type_id = ? ", sqlParams, conn);
			user = users[0];
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return user;
	}
	@Override
	public String getTeamName(int teamId, int customerId) {
		Connection conn = null;
		String teamName = "";
		try {
			conn = resourceManager.getConnection();
			TeamDto team = teamDao.findByPrimaryKey(teamId, conn);
			if (team != null) {
				teamName = team.getName();
			}
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return teamName;
	}
	@Override
	public UserDto[] getChildUser(String userIds) {
		UserDto[] users = null;
		Connection con = null;
		try {
			con = resourceManager.getConnection();
			users = userDao.findWhereIdIn(userIds, con);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(con);
		}
		return users;
	}

	@Override
	public List<Long> getUserIdsFromCommaSeperatedUsersTeamIds(int customerId, String commaSeperatedTeamIds, String commaSeperatedUsers) {
		List<Long> list = new ArrayList<Long>();
		Connection conn = null;
		try {
			conn = resourceManager.getConnection();
			// Users
			if (commaSeperatedUsers != null && !("").equals(commaSeperatedUsers)) {
				String[] userIds = PlatformUtil.getArrayFromCommaSeperatedString(commaSeperatedUsers);
				for (String userId : userIds) {
					list.add(Long.parseLong(userId));
				}
			}
			// Teams
			if (commaSeperatedTeamIds != null && !("").equals(commaSeperatedTeamIds)) {
				UserDto[] users = userDao.findWhereCustIdTeamIdIN(customerId, commaSeperatedTeamIds, conn);
				if (users != null && users.length > 0) {
					for (UserDto userDto : users) {
						list.add(userDto.getId());
					}
				}
			}
			// Sort
			Set<Long> set = new HashSet<Long>();
			set.addAll(list);
			list.clear();
			list.addAll(set);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return list;
	}

	@Override
	public long changeStatus(long id, int customerId, int status) {
		Connection con = null;
		long rows = 0;
		try {
			con = resourceManager.getConnection();
			rows = userDao.changeStatus(id, customerId, status, con);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(con);
		}
		return rows;
	}

	@Override
	public UserDto[] getSelectedUsersByTeamId(int customerId, long teamId) {
		Connection con = null;
		UserDto[] users = new UserDto[0];
		try {
			con = resourceManager.getConnection();
			String query = " u.customer_id = ? and u.team_id = ? ORDER BY u.first_name, u.last_name ASC";

			Object[] params = new Object[2];
			params[0] = customerId;
			params[1] = teamId;
			users = userDao.findByDynamicWhere(query, params, con);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(con);
		}
		return users;

	}

	@Override
	public UserDto[] getAvailableUsersTeams(int customerId, String userIds) {
		Connection con = null;
		UserDto[] users = new UserDto[0];
		try {
			con = resourceManager.getConnection();
			String query = " u.customer_id = ? and u.user_type_id = ? and (u.team_id is null OR u.team_id = 0) and u.id NOT IN ("
					+ userIds + ") and u.status = 1 ORDER BY u.first_name, u.last_name ASC";
			Object[] params = new Object[2];
			params[0] = customerId;
			params[1] = SessionConstants.USER_TYPE_ID_USER;
			users = userDao.findByDynamicWhere(query, params, con);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(con);
		}
		return users;
	}

	@Override
	public UserDto[] getSelectedUsersByTeamIds(int customerId, String selectedUsers) {
		Connection con = null;
		UserDto[] users = new UserDto[0];
		try {
			con = resourceManager.getConnection();
			String query = " u.customer_id = ? and u.user_type_id = ? and (u.team_id is null OR u.team_id = 0) and u.status = 1 ORDER BY u.first_name, u.last_name ASC";
			Object[] params = new Object[2];
			params[0] = customerId;
			params[1] = SessionConstants.USER_TYPE_ID_USER;
			users = userDao.findByDynamicWhere(query, params, con);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(con);
		}
		return users;
	}

	@Override
	public UserDto[] getAllAvailableUsers(int customerId) {
		Connection con = null;
		UserDto[] users  = null;
		try {
			con = resourceManager.getConnection();
			String query = " u.customer_id = ? and u.user_type_id = ? and u.status = ? ORDER BY u.first_name, u.last_name ASC";
			Object[] params = new Object[3];
			params[0] = customerId;
			params[1] = SessionConstants.USER_TYPE_ID_USER;
			params[2] = SessionConstants.STATUS_ACTIVE;
			users = userDao.findByDynamicWhere(query, params,con); 

		} catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} 
		finally {
			resourceManager.close(con);
		}
		return users;
	}

	@Override
	public UserDto[] getAllActiveInactiveUsers(int customerId) {
		Connection con = null;
		UserDto[] users  = null;
		try {
			con = resourceManager.getConnection();
			String query = " u.customer_id = ? and u.user_type_id = ? ORDER BY u.first_name, u.last_name ASC";
			Object[] params = new Object[2];
			params[0] = customerId;
			params[1] = SessionConstants.USER_TYPE_ID_USER;
			users = userDao.findByDynamicWhere(query, params,con); 

		} catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} 
		finally {
			resourceManager.close(con);
		}
		return users;
	}

}
