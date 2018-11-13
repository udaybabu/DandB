package in.otpl.dnb.report;

import java.sql.Connection;
import java.util.Map;

import in.otpl.dnb.user.UserDto;

public interface ReportDao {

	public Map<String, Object> getUserTrackingReport(UserTrackingForm form, int customerId,Connection conn);

	public Map<String, Object> getLoginDetailList(LoginDetailsForm loginDetailsForm, int customerId, Connection conn, UserDto user);
}
