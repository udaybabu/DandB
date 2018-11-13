package in.otpl.dnb.logic;

import in.otpl.dnb.user.UserDto;

public interface AdministrationLogic {

	public UserDto authorize(int customerId, String userName, String password);
	
	public int authorizeUser(int customerId, long userId, String password);
	
	public boolean auditLog(int customerId, long modifiedBy, String logInfo);
	
}
