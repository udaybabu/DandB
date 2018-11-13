package in.otpl.dnb.report;

import in.otpl.dnb.gw.CurrentUserTrackingDao;

import in.otpl.dnb.util.ErrorLogHandler;
import in.otpl.dnb.util.ResourceManager;
import in.otpl.dnb.user.UserDao;
import in.otpl.dnb.report.LoginDetailsForm;
import in.otpl.dnb.user.UserDto;

import java.sql.Connection;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportLogicImpl implements ReportLogic{

	private static final Logger LOG = Logger.getLogger(ReportLogicImpl.class);
	
	@Autowired
	private CurrentUserTrackingDao currentUserTrackingDao;
	@Autowired
	private ResourceManager resourceManager;
	@Autowired
	private ReportDao reportDao;
	@Autowired
	UserDao userDao;
	@Autowired
	private CurrentUserTrackingDao currentuserTrackingDao;

	@Override
	public Map<String, Object> getAllUserTrackingReport(int customerId, long userId, int userTypeId){
		Connection conn = null;
		Map<String, Object> userReport = null;
		try {
			conn = resourceManager.getConnection();
			userReport = currentUserTrackingDao.getAllUserTrackingReport(customerId, userId, userTypeId, conn);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return userReport;
	}
	
	@Override
	public Map<String, Object> getUserTrackingList(UserTrackingForm userForm, int customerId){
		Connection conn = null;
		Map<String, Object> userReport = null;
		try {
			conn = resourceManager.getConnection();
			userReport = reportDao.getUserTrackingReport(userForm, customerId, conn);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return userReport;
	}

	@Override
	public Map<String, Object> getAllUserTrackingReport(AllUserMapForm userMapForm) {
		Connection conn = null;
		Map<String, Object> userReport = null;
		try {
			conn = resourceManager.getConnection();
			userReport = currentuserTrackingDao.getdnbMapReport(userMapForm, conn);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return userReport;

	}

	@Override
	public Map<String, Object> getLoginDetailList(LoginDetailsForm loginDetailsForm, int customerId) {
		Connection conn = null;
		Map<String, Object> map = null;
		try{
			conn = resourceManager.getConnection();
			UserDto user = userDao.findByPrimaryKey(loginDetailsForm.getUserId(),conn);
			map = reportDao.getLoginDetailList(loginDetailsForm,customerId,conn,user);
		}catch (Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(conn);
		}
		return map;
	}
}