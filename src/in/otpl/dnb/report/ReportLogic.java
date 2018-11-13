package in.otpl.dnb.report;

import java.util.Map;

import in.otpl.dnb.report.LoginDetailsForm;

public interface ReportLogic {
	
	public Map<String, Object> getAllUserTrackingReport(int customerId, long userId, int userTypeId);
	
	public Map<String, Object> getUserTrackingList(UserTrackingForm userForm, int customerId);

	public Map<String, Object> getAllUserTrackingReport(AllUserMapForm userMapForm);
	
	public Map<String, Object> getLoginDetailList(LoginDetailsForm loginDetailsForm,int customerId);
}
