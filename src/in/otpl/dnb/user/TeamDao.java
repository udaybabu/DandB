package in.otpl.dnb.user;

import java.sql.Connection;
import java.util.Map;

public interface TeamDao{
	
	public long insert(TeamDto dto,Connection conn);

	public void update(TeamDto dto,Connection conn);
	
	public TeamDto[] findWhereCustomerIdEquals(int customerId,Connection conn);

	public TeamDto[] findWhereCustomerIdTeamIdsIn(int customerId, String teamIds, Connection conn);
		
	public TeamDto[] findWhereNameEquals(String name, int customerId,Connection conn);

	public TeamDto[] findWhereLeadIdEquals(long leadId,Connection conn);

	public TeamDto[] findByDynamicSelect(String sql, Object[] sqlParams,Connection conn);

	public TeamDto[] findByDynamicWhere(String sql, Object[] sqlParams,Connection conn);
	
	public TeamDto[] findWhereIdEquals(long id,int customerId,Connection conn);
	
	public Map<String, Object> teamList(int customerId, TeamForm form,Connection conn);
		
	public long getLeadIdByTeamId(int customerId, String teamId, Connection conn);
	
	public String getTeamIdByLeadId(int customerId, long leadId, Connection conn);
			
	public TeamDto[] findWhereCustomerId(int customerId, Connection conn);
	
	public TeamDto findByPrimaryKey(long id, Connection conn);
	
	public int getCount(String sql, Object[] sqlParams,Connection conn);
	
	public int changeStatus(int id, int status, Connection conn);
	
}
