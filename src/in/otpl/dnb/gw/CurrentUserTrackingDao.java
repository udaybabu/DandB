package in.otpl.dnb.gw;
import in.otpl.dnb.gw.CurrentUserTrackingForm;
import in.otpl.dnb.report.AllUserMapForm;

import java.sql.Connection;
import java.util.Map;

public interface CurrentUserTrackingDao{

	public int insert(CurrentUserTracking dto, Connection conn);
	
	public void update(long customerTrackingUserId, CurrentUserTracking dto, Connection conn);
	
	public CurrentUserTracking[] findByDynamicWhere(String sql, Object[] sqlParams, Connection conn);
	
	public CurrentUserTrackingForm[] findByDynamic(String sql, Object[] sqlParams, Connection conn);
	
	public Map<String, Object> getAllUserTrackingReport( int customerId,long userId,int userTypeId, Connection conn);
	
	public Map<String, Object> getdnbMapReport(AllUserMapForm userMapForm, Connection conn);
	
	public void lastTracking(LastTrackingDto dto, Connection conn);

}