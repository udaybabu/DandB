package in.otpl.dnb.util;

public class ClientRequestConstants {

	public static final String ENCRYPTION_KEY="7c5D59bF";
	public static final String DECRYPT_RESPONSE="dec";

	//Request names
	public static final String PROTOCOL_ID = "pId";
	public static final String CLIENT_VERSION = "cVer";
	public static final String CALL_TYPE = "callType";
	public static final String CALL_NAME = "callName";
	public static final String PARAM_VALUES = "paramValues";

	//callType
	public static final String GET_CALL_TYPE = "get";
	public static final String SEND_CALL_TYPE = "send";

	// callName
	public static final String LOGIN_GET = "login";
	public static final String SETTINGS_GET_STRING = "setup";
	public static final String LOGO_GET = "logo";
	public static final String UPDATE_GET = "update";
	public static final String EVENT_SEND = "es";
	public static final String ACKNOWLEDGEMENT = "ack";
	public static final String MEDIA_SYNC = "mediaSync";



	public static final String SETTINGS_GET = "setup";
	public static final String CHANGE_PASSWORD = "changePass";
	public static final String CALL1 = "call_1";
	public static final String CALL2 = "call_2";
	public static final String CALL15 = "call_15";
	public static final String CALL25 = "call_25";
	public static final String CALL26 = "call_26";
	public static final String CALL31 = "call_31";
	public static final String CALL32 = "call_32";

	public static String returnCall(String callName){
		String call = "";
		switch (callName) {
		case CALL1:
			call = LOGIN_GET;
			break;
		case CALL32:
			call = LOGO_GET;
			break;
		case CALL2:
			call = SETTINGS_GET;
			break;
		case CALL26:
			call = ACKNOWLEDGEMENT;
			break;
		case CALL15:
			call = UPDATE_GET;
			break;
			// Data Sync
		case CALL25:
			call = EVENT_SEND;
			break;
		case CALL31:
			call = MEDIA_SYNC;
			break;
		default:
			break;
		}
		return call;
	}

	//Login call values
	public static final String CUSTOMER_ID = "cId";
	public static final String USER_LOGIN_ID = "uli";
	public static final String PASSWORD = "passwd";

	//Login call response values
	public static final String USER_ID = "uId";
	public static final String USER_NAME = "uName";
	public static final String EMP_CODE = "empCode";
	public static final String TEAM_ID = "teamId";

	//Response parameter names
	public static final String RESPONSE_CODE_STRING = "respCode";
	public static final String MESSAGE_STRING = "message";
	public static final String CALLTOMAKE_STRING = "callToDo";
	public static final String REASSIGNED_MSG_STRING = "reassigned-message";

	//Response values
	public static final String SUCCESS_RESPONSE_CODE_GENERIC = "success";
	public static final String ERROR_RESPONSE_CODE_GENERIC = "failure";
	public static final String SUCCESS_LOGIN = "suc-001";
	public static final String SUCCESS_LOGIN_MSG = "Login Success!";
	public static final String ERROR_IMEI = "err-001";
	public static final String ERROR_IMEI_MSG = "Login Failure! IMEI not matched.";
	public static final String ERROR_UNAUTHORIZED_ACCESS = "err-002";
	public static final String ERROR_UNAUTHORIZED_ACCESS_MSG = "Login Failure! Unauthorized Access.";
	public static final String ERROR_INVALID_SESSION = "err-003";
	public static final String ERROR_INVALID_SESSION_MSG = "Invalid Session";
	public static final String ERROR_USER_INACTIVE = "err-004";
	public static final String ERROR_USER_INACTIVE_MSG = "User Inactive!";
	public static final String SUCCESS_URL_VERIFYING = "suc-002";
	public static final String SUCCESS_URL_VERIFYING_MSG = "Event sent successfully!";
	public static final String SUCCESS_EVENT_INSERT = "suc-003";
	public static final String SUCCESS_EVENT_INSERT_MSG = "Event Inserted Successfully!";
	public static final String ERROR_INVALID_CREDENTIALS = "err-005";
	public static final String ERROR_INVALID_CREDENTIALS_MSG = "Invalid input please try again with valid credentials";
	public static final String ERROR_URL_VERIFYING = "err-006";
	public static final String ERROR_URL_VERIFYING_MSG = "Please verify once again";
	public static final String ERROR_TECHNICAL_PROBLEM = "err-007";
	public static final String ERROR_TECHNICAL_PROBLEM_MSG = "There seems to be some technical problem. Please try again.";
	public static final String ERROR_INVALID_CALL = "err-008";
	public static final String ERROR_INVALID_CLIENT_VER_MSG = "Invalid App Version";
	public static final String ERROR_INVALID_CALLTYPE_MSG = "Invalid callType";
	public static final String ERROR_INVALID_CALLNAME_MSG = "Invalid callName";
	public static final String SUCCESS_SETTINGS = "suc-004";
	public static final String SUCCESS_SETTINGS_MSG = "Settings call Success!";
	public static final String ERROR_SETTINGS = "err-009";
	public static final String ERROR_SETTINGS_MSG = "Settings call Failure!";
	public static final String SUCCESS_DETAILS = "suc-005";
	public static final String SUCCESS_TASKS = "suc-009";
	public static final String SUCCESS_TASKS_MSG = "Enquiry call Success!";
	public static final String ERROR_TASKS = "err-014";
	public static final String ERROR_TASKS_MSG = "Enquiry call Failure!";
	public static final String ERROR_JSON = "err-026";
	public static final String ERROR_JSON_MSG = "Not Valid JSON";
	public static final String SUCCESS_ACK_USER_UPDATE = "suc-027";
	public static final String SUCCESS_ACK_USER_UPDATE_MSG = "Application Updated Successfully!";
	public static final String ERROR_ACK_USER_UPDATE = "err-033";
	public static final String ERROR_ACK_USER_UPDATE_MSG = "Application Updation Failed!";
	public static final String SUCCESS_ACK_TASK = "suc-028";
	public static final String SUCCESS_ACK_TASK_MSG = "Enquiry recieved Successfully!";
	public static final String ERROR_ACK_TASK = "err-034";
	public static final String ERROR_ACK_TASK_MSG = "Enquiry recieved but updation failed!";
	public static final String SUCCESS_ACK_TASK_NOTHING = "suc-029";
	public static final String SUCCESS_ACK_TASK_NOTHING_MSG = "Nothing to update : Enquiry!";
	public static final String ERROR_ACK = "err-036";
	public static final String ERROR_ACK_MSG = "Acknowledgement Failed!";
	public static final String ERROR_EVENT_LOC = "err-039";
	public static final String ERROR_EVENT_LOC_MSG = "Failure! Event and Location both do not exist.";
	public static final String SUCCESS_APP_EXIT = "suc-034";
	public static final String SUCCESS_APP_EXIT_MSG = "Application successfully Logged out!";
	public static final String SUCCESS_MEDIA_SYNC = "suc-035";
	public static final String SUCCESS_MEDIA_SYNC_MSG = "Media synced successfully";
	public static final String ERROR_MEDIA_SYNC = "err-040";
	public static final String ERROR_MEDIA_SYNC_MSG = "Media unable to sync";
	public static final String SETTINGS_RESPONSE_STRING = "settings";
	public static final String WORKFLOWS_RESPONSE_STRING = "workflows";
	public static final String IS_EDITED = "isEdited";

	public static final String EVENT_ID = "eventId";
	public static final String EVENT_NAME = "eventName";

	public static final String JSON_STRING = "jsonString";
	public static final String CLIENT_TIME_STAMP= "ts";
	public static final String CLIENT_PHONE_MODEL= "phoneModel";
	public static final String CLIENT_LAT= "lat";
	public static final String CLIENT_LON= "lon";
	public static final String SESSION_ID = "sessionId";
	public static final String IMEI = "imei";
	public static final String UID= "uId";
	public static final String DATA="data";

	// Tracking
	public static final String EVENT_LOGIN = "LI";
	public static final String EVENT_LOGIN_WEB = "LI-WEB";
	public static final String EVENT_TRACKING = "TR";
	public static final String EVENT_LOW_BATTERY = "LB";
	public static final String EVENT_APP_EXIT = "EX";
	public static final String EVENT_APP_EXIT_WEB = "EX-WEB";
	public static final String PERTIAL_EXIT = "PE";
	public static final String CONNECTION_CHECK = "CC";
	public static final String DATA_SERVICE = "DP";
	public static final String STATE = "ST";
	public static final String UNIQUE_ID = "uniqueId";
	public static final String FORM_START_TIME = "formStartTime";
	public static final String FORM_END_TIME = "formSubmissionTime";
	public static final String UPDATED_BY = "updatedBy";

	public static final String STATUS = "status";

	//Message
	public static final String MESSAGE = "Message";
	public static final String CM_MEDIA_DATA = "mediaData";

	/*DNB*/
	public static final String DNB_DOC_MEDIA_SYNC = "docMediaSync";
	public static final String DNB_DOC_LIST = "dnbDocList";
	public static final String DNB_ENQUIRY_GET = "enquiryGet";
	public static final String DNB_ENQUIRY_REGET = "enquiryReGet";
	public static final String DNB_REMOVED_ENQUIRY_LIST = "enquiryRemovalList";
	public static final String DNB_EVENT_DETAILS = "dnbes";
	public static final String DNB_FORM_DATA = "formData";
	public static final String DNB_CASE_DETAILS = "dnbCaseDetails";
	public static final String SUCCESS_DNB_DET = "suc-dnb";
	public static final String SUCCESS_DNB_MSG = "DNB Case Details call Success!";
	public static final String ERROR_DNB_DET = "err-dnb";
	public static final String ERROR_DNB_MSG = "DNB Case Details call Failure!";
	public static final String DNB_DOC_CHECK_DETAILS = "dnbDocDetails";
	public static final String SUCCESS_DNB_DOC_DET = "suc-dnbDoc";
	public static final String SUCCESS_DNB_DOC_MSG = "DNB document check list call Success!";
	public static final String ERROR_DNB_DOC_DET = "err-dnbDoc";
	public static final String ERROR_DNB_DOC_MSG = "DNB document check list call Failure!";

	public static final String DNB_DATA_SUCCESS = "suc-dnbData";
	public static final String DNB_DATA_SUCCESS_INSERT_MSG = "Data synced / inserted  successfully";
	public static final String DNB_DATA_SUCCESS_UPDATE_MSG = "Data synced / updated successfully";
	public static final String DNB_DATA_FAILURE = "err-dnbData";
	public static final String DNB_DATA_FAILURE_MSG = "Unable to sync data !!! Please try after sometime";
	public static final String DNB_DATA_FAILURE_UPDATED_MSG = "Unable to sync updated data !!! Please try after sometime";
	public static final String DNB_REASSIGNED_TO_OTHER_USER_MSG = "Enquiry has been reassigned to some other user";
	public static final String DNB_REMOVED_ENQUIRY_DATA = "removedEnquiryData";

}
