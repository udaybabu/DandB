package in.otpl.dnb.user;

import java.sql.Connection;
import java.util.Map;
import java.util.Set;

public interface UserDao{

	public long insert(UserForm form, Connection conn);
	
	public long update(UserForm form, Connection conn);
	
	public UserDto findByPrimaryKey(long id,Connection conn);

	public UserDto findWhereLoginNameEquals(String loginName, int customerId,Connection conn);

	public UserDto[] findWhereTeamIdEquals(int teamId,Connection conn);
	
	public UserDto[] findByDynamicSelect(String sql, Object[] sqlParams,Connection conn);

	public UserDto[] findByDynamicWhere(String sql, Object[] sqlParams,Connection conn);
	
	public int getCount(String sql, Object[] sqlParams,Connection conn);

	public int getCountCondition(String sql, Object[] sqlParams, Connection conn);
	
	public void updateAvailableUsers(int userId,Connection conn);

	public Map<String, Object> userList(int customerId, UserForm form,Connection conn,long LoginUserId);
	
	public int updateUser(int id, UserDto dto,Connection conn);
	
	public int updateUserColumnForceSetUpUpdate(Connection con, int customerId, String call);
	
	public boolean updateForceSetupInactive(Connection conn, long userId, int customerId, String ts);
	
	public int updateForceSetupActiveByTeam(Connection conn, int loginUserId, int teamId, int customerId, String calls);
	
	public UserDto[] findWhereIdIn(String commaSeperatedId,Connection conn);
	
	public UserDto[] findWhereCustIdTeamIdIN(int customerId, String commaSeperatedTeamIds,Connection conn);
	
	public int updateHandsetDetailUser(long id, String details, String clientVer, Connection conn);
	
	public int resetPassword(long userId, String pwd, Connection conn);

	public UserDto[] findWhereUserTypeIdStatusEquals(int customerId, int status,int type,Connection conn);
	
	public UserDto[] findWhereUserTypeIdEquals(int customerId,int type,Connection conn);
	
	public UserDto[] findWhereTeamIdInEquals(int customerId, int status, int type, String commaSeperatedTeamIds,Connection conn);
	
	public UserDto[] findWhereTeamIdEquals(int customerId, int type, String teamId,Connection conn);
	
	public UserDto[] getUsersCreatedBy(int customerId, String created_by_id, int user_type_id, int status, Connection conn);
	
	public UserDto[] getUsersByLeadId(int customerId, String lead_id, int status, Connection conn);
	
	public UserDto[] findByDynamicCondition(String sql, Object[] sqlParams,Connection conn);
	
	public int updateCustDetail(UserDto dto,Connection conn);
	
	public int updateMyInfoUser(UserDto dto,Connection conn);
	
	public UserDto leadProfileBasedOnOwnTeamUserId(Connection conn, int CustomerId, int userId);

	public Set<String> getUserUpdateCalls(Connection conn, int customerId, long userId);
	
	public int getTotalUsers(Connection con,int customerId);
	
	public UserTypeDto findByPrimaryKeyUserType(int id,Connection conn);

	public UserTypeDto[] findAllUserType(Connection conn);

	public UserTypeDto[] findWhereDescriptionEqualsUserType(String description,Connection conn);

	public UserTypeDto[] findWhereIsAdminEqualsUserType(short isAdmin,Connection conn);

	public UserTypeDto[] findByDynamicSelectUserType(String sql, Object[] sqlParams,Connection conn);

	public UserTypeDto[] findByDynamicWhereUserType(String sql, Object[] sqlParams,Connection conn);
	
	public UserTypeDto[] findWhereIsUserEqualsUserType(short isAdmin,Connection conn);
	
	public long changeStatus(long id, int customerId, int status, Connection conn);
	
	public void updateAvailableUsers(long teamId, String userId, Connection conn);
	
	public int updateAvailableUsers(long teamId, Connection conn);

	public int updateAvailableUser(long teamId, Connection conn);
	
	public void userUpdate(Connection conn, int customerId, String call, int teamId, long userId);
	
}
