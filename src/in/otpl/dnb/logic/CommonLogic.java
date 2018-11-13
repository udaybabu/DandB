package in.otpl.dnb.logic;

import net.sf.json.JSONObject;

public interface CommonLogic {

	public void dataManipulator(int customerId, int dnbMId);
	
	public void masterDataFetch(int customerId);
	
	public void enquiryDataCompletion(int customerId);
	
	public void reassignment(int customerId);
	
	public void enquiryPoolFetch(int customerId);
	
	public int insertEmailDetails(int customerId, String emailTo, String emailCC, String emailBCC, String subject, String body);
	
	public JSONObject getDNBMediaDetails( String uKey,long masterId, long dnbMasterId, String docId, String mediaId);

}
