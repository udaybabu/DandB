package in.otpl.dnb.logic.impl;

import in.otpl.dnb.logic.AdministrationLogic;
import in.otpl.dnb.user.UserDao;
import in.otpl.dnb.user.UserDto;
import in.otpl.dnb.util.ErrorLogHandler;
import in.otpl.dnb.util.PlatformUtil;
import in.otpl.dnb.util.ResourceManager;
import in.otpl.dnb.util.SessionConstants;

import java.sql.Connection;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdministrationLogicImpl implements AdministrationLogic{

	private static final Logger LOG = Logger.getLogger(AdministrationLogic.class);

	@Autowired
	private ResourceManager resourceManager;
	@Autowired
	private UserDao userDao;

	@Override
	public UserDto authorize(int customerId, String userName, String password){
		Connection con = null;
		UserDto user = null;
		try{
			con = resourceManager.getConnection();			
			String query = " u.customer_id = ? and u.login_name = ? and u.password = ? and u.status = "+SessionConstants.STATUS_ACTIVE;
			final int arraySize = 3;
			Object[] params = new Object[arraySize];
			params[0] = new Integer(customerId);
			params[1] = userName;
			params[2] = PlatformUtil.sha(password);
			UserDto[] users = userDao.findByDynamicWhere(query, params,con);
			if(users != null && users.length > 0){
				user = users[0];
			}
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(con);
		}
		return user;
	}

	@Override
	public int authorizeUser(int customerId, long userId, String password){
		Connection conn = null;
		int status=0;
		try{
			conn = resourceManager.getConnection();			
			String query = " u.customer_id = ? and u.id = ? and u.password = ? ";
			Object[] params = new Object[3];
			params[0] = customerId;
			params[1] = userId;
			params[2] = PlatformUtil.sha(password);
			int count = userDao.getCount(query, params, conn);
			if(count!=0){
				status=1;
			}
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(conn);
		}
		return status;
	}

	@Override
	public boolean auditLog(int customerId, long modifiedBy, String logInfo){
		Connection conn = null;
		boolean status = false;
		try{
			conn = resourceManager.getConnection();
		//	status = commonDao.insertAuditLog(conn, customerId, modifiedBy, logInfo);
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(conn);
		}
		return status;
	}

}