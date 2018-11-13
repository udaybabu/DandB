package in.otpl.dnb.user;


import java.sql.Connection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface TeamLogic {
	
	public int checkAvailabilityTeam(String teamName, int customerId);
	
	public int checkTeamId(long id, int customerId);
	
	public TeamDto[] getTeamByName(String name, int customerId);

	public int checkAvailabilityTeamEdit(long id, int customerId, String teamName);
	
	public TeamDto getTeamById(long id, int customerId);

	public TeamDto[] getTeamByIds(int customerId, String teamIds);

	public TeamDto[] getTeamsLead(long userId);
	
	public TeamDto[] getTeams(int customerId);
	
	public Map<String, Object> getTeamList(int customerId, TeamForm form);
	
	public TeamDto createTeam(int customerId, TeamForm teamForm);
	
	public void updateUserTeamId(Connection conn, long[] userIds, long teamId);
	
	public TeamDto updateTeam(int customerId, TeamForm teamForm);
	
	public int deleteTeam(int customerId, String[] teams);
	
	public String teamIdsByLeadId(int customerId, long leadId);

	public void setDataForError(HttpServletRequest request, long[] selectedL, long[] availableL);

	public int changeStatus(int id, int status,String users);
}
