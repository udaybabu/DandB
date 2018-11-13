package in.otpl.dnb.user;

import java.sql.Connection;
import java.util.List;
import java.util.Map;


public interface UserLogic{

	public long createUser(UserForm userForm);
	
	public long updateUser(UserForm userForm);
	
	public UserTypeDto [] getAllUserTypes(boolean isAdmin);
	
	public UserTypeDto getUserTypes(String description);
	
	public UserTypeDto [] getUserTypesForLead(boolean isLead);

	public UserDto[] getTeamLeads(int customerId);
	
	public UserDto[] getAllUsers(int customerId);
	
	public UserDto[] getAvailableUsersTeams(int customerId);
	
	public UserDto[] getSelectedUsersByTeamId(int customerId, int teamId);
	
	public int getTotalUsers(int customerId, int usertypeId);
	
	public int getTotalUsers(int customerId);

	public Map<String, Object> userList(int customerId, UserForm form, long LoginUserId);

	public UserDto getUserDetails(int userId);

	public UserDto getUserByName(String loginName, int customerId);

	public boolean checkUserColumnHeader(byte[] file);
	
	public UserDto validateUserId(int userid, int customerId);
	
	public boolean validateEmployeeNumber(String empNo, int customerId);

	public boolean validateEmployeeNumberEdit(String empNo, int customerId, long userId);

	public boolean validateEmailIdAddress(String emailId,int customerId);
	
	public boolean validateEmailIdEdit(String emailId, int customerId, long userId);

	public boolean validatePhoneNumber(String phoneNumber, int customerId);
	
	public boolean validatePhoneNumberEdit(String phoneNumber, int customerId, long userId);
	
	public boolean validatePhoneNumber(String phoneNumber, String loginName);

	public boolean validateLoginName(int customerId, String  loginName);
	
	public boolean validateIMEINumber(String IMEI, int customerId);
	
	public boolean validateIMEINumberEdit(String IMEI, int customerId, long userId);
	
	public boolean validateIMEINumber(String IMEI, String loginName);

	public UserDto getUserByName(String firstName, String lastName, int customerId);
	
	public UserDto getUserByNameWithLoginId(String firstName, String lastName, int customerId, String loginName);
	
	public int updateUserColumnForceSetUpUpdate(Connection con, int customerId, String call);
	
	public boolean updateForceSetupActiveByTeam(int loginUserId, int teamId, int customerId, String calls);
	
	public UserDto[] getAllUserListByCusidStatType(int customerId, int userTypeId, long userId, int status);
	
	public boolean checkAvailableUsers(int customerId, String[] selected);
	
	public int userChangePassword(long userId, String password);
	
	public UserDto adminProfile(int customerId);
	
	public String getTeamName(int teamId,int customerId);
	
	public UserDto[] getChildUser(String userIds);
	
	public List<Long> getUserIdsFromCommaSeperatedUsersTeamIds(int customerId, String commaSeperatedTeamIds, String commaSeperatedUsers);
	
	public UserDto getUserDetails(long userId);
	
	public long changeStatus(long id, int customerId, int status);
	
    public UserDto[] getAllAvailableUsers(int customerId);
	
	public UserDto[] getAllActiveInactiveUsers(int customerId);
	
	public UserDto[] getAvailableUsersTeams(int customerId, String userIds);
	
	public UserDto[] getSelectedUsersByTeamId(int customerId, long teamId);
	
	public UserDto[] getSelectedUsersByTeamIds(int customerId, String selectedUsers);
	
	public boolean updateForceSetupActive(int customerId, String call);


}
