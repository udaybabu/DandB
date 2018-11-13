package in.otpl.dnb.user;

import in.otpl.dnb.util.ErrorLogHandler;

import in.otpl.dnb.util.PlatformUtil;
import in.otpl.dnb.util.ResourceManager;
import in.otpl.dnb.util.SessionConstants;
import java.sql.Connection;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamLogicImpl implements TeamLogic{

	private  static final Logger LOG = Logger.getLogger(TeamLogic.class);

	@Autowired
	private ResourceManager resourceManager;
	@Autowired
	private TeamDao teamDao;
	@Autowired
	private UserDao userDao;
	
	/* Check Team Name Availability */
	@Override
	public int checkAvailabilityTeam(String name, int customerId){
		int available = 0;
		TeamDto[] teams = getTeamByName(name, customerId);
		if(teams != null){
			available = teams.length;
		}
		return available;
	}

	/* Get Team details by Team Name*/ 
	@Override
	public TeamDto[] getTeamByName(String name, int customerId){
		Connection conn=null;
		TeamDto[] teams = null;
		try{
			conn=resourceManager.getConnection();
			teams = teamDao.findWhereNameEquals(name, customerId, conn);
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(conn);
		}
		return teams;
	}

	@Override
	public int checkAvailabilityTeamEdit(long id, int customerId, String teamName){
		int available = 0;
		Connection conn=null;
		try{
			conn=resourceManager.getConnection();
			String sql = " id != ? and customer_id = ? and name = ? ORDER BY name ASC ";
			Object[] objParams= { new Long(id), new Long(customerId), teamName };
			TeamDto[] teams = teamDao.findByDynamicWhere(sql, objParams, conn);
			if (teams != null && teams.length > 0) 
				available = teams.length;
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(conn);
		}
		return available;
	}

	/* Get Team details by Team Id */
	@Override
	public TeamDto getTeamById(long id, int customerId){
		Connection con = null;
		TeamDto teamDto = null;
		try {
			con = resourceManager.getConnection();
			String sql = " id = ? and customer_id = ? ORDER BY name ASC ";
			Object[] objParams= { new Long(id), new Integer(customerId) };
			TeamDto[] teams = teamDao.findByDynamicWhere(sql, objParams, con);
			if (teams.length > 0) 
				teamDto = teams[0];
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(con);
		}
		return teamDto;
	}

	@Override
	public int checkTeamId(long id, int customerId){
		Connection conn=null;
		TeamDto[] teams = null;
		int available=0;
		try{
			conn=resourceManager.getConnection();
			teams = teamDao.findWhereIdEquals(id,customerId, conn);
			if (teams != null && teams.length > 0) 
				available = teams.length;
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(conn);
		}
		return available;
	}	

	@Override
	public TeamDto[] getTeamByIds(int customerId, String teamIds){
		Connection con = null;
		TeamDto[] teams = null;
		try {
			con = resourceManager.getConnection();
			teams = teamDao.findWhereCustomerIdTeamIdsIn(customerId, teamIds, con);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(con);
		}
		return teams;
	}

	@Override
	public TeamDto[] getTeamsLead(long userId){
		Connection conn = null;
		TeamDto[] teams = null;
		try{
			conn = resourceManager.getConnection();
			teams = teamDao.findWhereLeadIdEquals(userId, conn);
		} catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally{
			resourceManager.close(conn);
		}
		return teams;
	}
	
	/* Get Teams by Customer */
	@Override
	public TeamDto[] getTeams(int customerId){
		Connection conn = null;
		TeamDto[] teams = null;
		try{
			conn = resourceManager.getConnection();
			teams = teamDao.findWhereCustomerIdEquals(customerId,conn);
		} catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally{
			resourceManager.close(conn);
		}
		return teams;
	}

	/* Get Team List by conditions */
	@Override
	public Map<String, Object> getTeamList(int customerId, TeamForm form){
		Connection con = null;
		Map<String, Object> map = null;
		try {
			con = resourceManager.getConnection();
			map = teamDao.teamList(customerId, form, con);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(con);
		}
		return map;
	}

	/* Create Team */
	@Override
	public TeamDto createTeam(int customerId, TeamForm teamForm){
		Connection con = null;
		TeamDto teamDto = new TeamDto();
		try {
			con = resourceManager.getConnection();
			teamDto.setCustomerId(customerId);
			teamDto.setName(teamForm.getTeamName());
			teamDto.setLeadId(teamForm.getTeamLeadId());
			long teamId = teamDao.insert(teamDto,con);
			teamDto.setId(teamId);
			long[] userIds = teamForm.getSelected();
			if (userIds != null) {
				updateUserTeamId(con, userIds, teamDto.getId());
			}
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(con);
		}
		return teamDto;
	}

	@Override
	public void updateUserTeamId(Connection conn, long[] userIds, long teamId){
		String users = "";
		for (int i = 0; i < userIds.length; i++) {
			if(!"".equals(users)){
				users += ",";
			}
			users += userIds[i];
		}
		if(!"".equals(users)){
			userDao.updateAvailableUsers(teamId, users, conn);
		}
	}

	/* Update Team */
	@Override
	public TeamDto updateTeam(int customerId, TeamForm teamForm){
		Connection con = null;
		TeamDto teamDto = new TeamDto();
		try {
			con = resourceManager.getConnection();

			teamDto.setId(teamForm.getTeamId());
			teamDto.setCustomerId(customerId);
			teamDto.setName(teamForm.getTeamName());
			teamDto.setLeadId(teamForm.getTeamLeadId());
			teamDao.update(teamDto,con);

			userDao.updateAvailableUsers(teamForm.getTeamId(), con);
			long[] userIds = teamForm.getSelected();
			if (userIds != null) {
				updateUserTeamId(con, userIds, teamDto.getId());
			}
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(con);
		}
		return teamDto;
	}

	/* Delete Team */
	@Override
	public int deleteTeam(int customerId, String[] teams){
		int rows = 0;
		Connection con = null;
		try {
			con = resourceManager.getConnection();
			UserDto[] users = null;
			for (int i = 0; i < teams.length; i++) {
				int teamId = Integer.parseInt(teams[i]);
				users = userDao.findWhereTeamIdEquals(teamId,con);
				for (int j = 0; j < users.length; j++) {
					rows +=	userDao.updateAvailableUser(users[j].getId(), con);
				}
			}
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(con);
		}

		return rows;
	}

	/* Team Already Existed or error then reset */
	@Override
	public void setDataForError(HttpServletRequest request, long[] selectedL, long[] availableL){
		Connection con = null;
		UserDto[] availableList = null;
		UserDto[] selectedList = null;
		try{
			con = resourceManager.getConnection();
			availableList = userDao.findWhereIdIn(PlatformUtil.getCommaSeperatedString(availableL),con);
			selectedList = userDao.findWhereIdIn(PlatformUtil.getCommaSeperatedString(selectedL),con);
			request.removeAttribute(SessionConstants.AVAILABLE_USERS);
			request.removeAttribute(SessionConstants.SELECTED_USERS);

			request.setAttribute(SessionConstants.AVAILABLE_USERS, availableList);
			request.setAttribute(SessionConstants.SELECTED_USERS, selectedList);
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally {
			resourceManager.close(con);
		}
	}
	
	/* Team ids based on Lead id */
	@Override
	public String teamIdsByLeadId(int customerId, long leadId) {
		Connection con = null;
		String teamIds = "0";
		try {
			con = resourceManager.getConnection();
			teamIds = teamDao.getTeamIdByLeadId(customerId, leadId, con);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(con);
		}
		return teamIds;
	}


	public TeamDto[] getTeam(int customerId) {
		Connection conn = null;
		TeamDto[] team = null;
		try {
			conn = resourceManager.getConnection();
			team = teamDao.findWhereCustomerId(customerId, conn);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(conn);
		}
		return team;
	}

	@Override
	public int changeStatus(int id, int status,String users) {
		Connection con = null;
		int rows = 0;
		try {
			con = resourceManager.getConnection();
			rows =teamDao.changeStatus(id, status, con);
			if(status == 1 && !"".equals(users) ){
				userDao.updateAvailableUsers(id, con);
			}
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(con);
		}
		return rows;
	}
}