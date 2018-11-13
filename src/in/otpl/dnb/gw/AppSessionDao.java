package in.otpl.dnb.gw;

import java.sql.Connection;

public interface AppSessionDao {

	public boolean isValidSession(Connection conn, int customerId, long userId, String sessionId);
	
	public boolean checkUniqueKey(Connection conn, int customerId, long userId, long uniqueId);
	
	public int getSessionKey(int customerId, long userId, Connection conn);
	
	public boolean rejectedDataBackup(Connection conn, int customerId, long userId, int sessionIdOld, int sessionIdCurrent, long uniqueId, String json, String remarks);

	public int insertUpdateSessionKey(Connection conn, int rowId, String key, int customerId, long userId,String loginFrom);
	
	public void resetSessionKeyToNull(long userId, Connection conn) throws Exception;

}
