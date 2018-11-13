package in.otpl.dnb.util;

public class ServerConstants {

	public static final int ACCOUNT_TYPE_REVENUE = 1;
	public static final int ACCOUNT_TYPE_DEMO = 2;
	
	/* Phone Configuration */
	public static final String PHONE_CONFIGURATION_AUTO_TRACKING = "AUTO_TRACKING_ENABLED";
	public static final String PHONE_CONFIGURATION_TRACKING_LEVEL = "TRACKING_LEVEL";
	public static final String PHONE_CONFIGURATION_TRACKING_START_TIME = "TRACKING_START_TIME";
	public static final String PHONE_CONFIGURATION_TRACKING_END_TIME = "TRACKING_END_TIME";
	public static final String PHONE_CONFIGURATION_TRACKING_FREQUENCY = "TRACKING_FREQUENCY";
	public static final String PHONE_CONFIGURATION_SENDING_FREQUENCY = "SENDING_FREQUENCY";
	public static final String PHONE_CONFIGURATION_INCLUDE_CELLSITE = "INCLUDE_CELLSITE";
	public static final String PHONE_CONFIGURATION_CONNECT_BLUETOOTH = "CONNECT_BLUETOOTH";
	public static final String PHONE_CONFIGURATION_LOG_OUT = "LOG_OUT";
	public static final String PHONE_CONFIGURATION_CLEAR_DATA_ON_LOGOUT = "CLEAR_DATA_ON_LOGOUT";
	public static final String PHONE_CONFIGURATION_CHECK_FOR_UPDATE = "CHECK_FOR_UPDATE";
	public static final String PHONE_CONFIGURATION_IMEI_CHECK = "IMEI_CHECK";
	public static final String PHONE_CONFIGURATION_WORKFLOW_ATTENDANCE_SHIFTIN = "WORKFLOW_ATTENDANCE_SHIFTIN";
	public static final String PHONE_CONFIGURATION_ATTENDANCE_LANDMARK= "ATTENDANCE_LANDMARK";
	public static final String PHONE_CONFIGURATION_SUPPORT_NUMBER = "SUPPORT_NUMBER";

	/* Web Configuration */
	public static final String WEB_CONFIGURATION_GEOFENCE = "GEOFENCE";
	public static final String WEB_CONFIGURATION_SMART_JOB_ZONE = "SMART_JOB_ZONE";
	public static final String WEB_CONFIGURATION_ALERT = "ALERT";
	public static final String WEB_CONFIGURATION_ATTENDANCE = "ATTENDANCE";
	public static final String WEB_CONFIGURATION_CONTENT_MANAGEMENT = "CONTENT_MANAGEMENT";
	public static final String WEB_CONFIGURATION_EVENT_DATA_AUTH = "EVENT_DATA_AUTH";
	public static final String WEB_CONFIGURATION_EVENT_DATA_EDIT = "EVENT_DATA_EDIT";
	public static final String WEB_CONFIGURATION_CLIENT = "CUSTOMER";
	public static final String WEB_CONFIGURATION_TASK = "TASK";
	public static final String WEB_CONFIGURATION_WORKFLOWS = "WORKFLOWS";
	public static final String WEB_CONFIGURATION_ROUTE = "ROUTE";
	
	/*public static final String WEB_CONFIGURATION_EDIT_FORM = "ENABLE_EDIT_FORM";
	public static final String WEB_CONFIGURATION_ROUTE = "ENABLE_ROUTE";
	public static final String WEB_SALES = "ENABLE_SALES";
	public static final String WEB_EXPENSE = "ENABLE_EXPENSE";
	public static final String URL_ENABLED = "URL_ENABLED";*/
	
	/* Dynafield Types */
	public static final int DYNAFIELD_TYPE_ALPHANUMERIC = 1;
	public static final int DYNAFIELD_TYPE_NUMERIC = 2;
	public static final int DYNAFIELD_TYPE_TIME = 3;
	public static final int DYNAFIELD_TYPE_DATE = 4;
	public static final int DYNAFIELD_TYPE_DATE_TIME = 5;
	public static final int DYNAFIELD_TYPE_PICTURE = 6;
	public static final int DYNAFIELD_TYPE_AUDIO = 7;
	public static final int DYNAFIELD_TYPE_VIDEO = 8;
	public static final int DYNAFIELD_TYPE_BARCODE = 9;
	public static final int DYNAFIELD_TYPE_EMAIL_ID = 10;
	public static final int DYNAFIELD_TYPE_SMART_CARD_ID = 11;
	public static final int DYNAFIELD_TYPE_PHONE_NUMBER = 12;
	public static final int DYNAFIELD_TYPE_PASSWORD_NUMERIC = 13;
	public static final int DYNAFIELD_TYPE_PASSWORD_ALPHANUMERIC = 14;
	public static final int DYNAFIELD_TYPE_LOV = 15;
	public static final int DYNAFIELD_TYPE_SIGNATURE = 16;
	public static final int DYNAFIELD_TYPE_REGION = 17;
	public static final int DYNAFIELD_TYPE_ALPHABET_ONLY = 18;
	public static final int DYNAFIELD_TYPE_HIERARCHY = 19;
	public static final int DYNAFIELD_TYPE_TABLE = 20;
	public static final int DYNAFIELD_TYPE_SUBSET = 21;
	public static final int DYNAFIELD_TYPE_CONTACT = 22;
	public static final int DYNAFIELD_TYPE_MEETING = 23;
	public static final int DYNAFIELD_TYPE_CUSTOMER = 24;
	
	public static final String DYNAFIELD_FIELD_TYPE_1 = "Alphanumeric";
	public static final String DYNAFIELD_FIELD_TYPE_2 = "Numeric";
	public static final String DYNAFIELD_FIELD_TYPE_3 = "Time";
	public static final String DYNAFIELD_FIELD_TYPE_4 = "Date";
	public static final String DYNAFIELD_FIELD_TYPE_5 = "Date & Time";
	public static final String DYNAFIELD_FIELD_TYPE_6 = "Picture";
	public static final String DYNAFIELD_FIELD_TYPE_7 = "Audio";
	public static final String DYNAFIELD_FIELD_TYPE_8 = "Video";
	public static final String DYNAFIELD_FIELD_TYPE_9 = "Barcode";
	public static final String DYNAFIELD_FIELD_TYPE_10 = "Email ID";
	public static final String DYNAFIELD_FIELD_TYPE_11 = "Smart Card ID";
	public static final String DYNAFIELD_FIELD_TYPE_12 = "Phone Number";
	public static final String DYNAFIELD_FIELD_TYPE_13 = "Password Numeric";
	public static final String DYNAFIELD_FIELD_TYPE_14 = "Password AlphaNumeric";
	public static final String DYNAFIELD_FIELD_TYPE_15 = "LOV";
	public static final String DYNAFIELD_FIELD_TYPE_16 = "Signature";
	public static final String DYNAFIELD_FIELD_TYPE_17 = "Region";
	public static final String DYNAFIELD_FIELD_TYPE_18 = "Alphabet only";
	public static final String DYNAFIELD_FIELD_TYPE_19 = "Hierarchy";
	public static final String DYNAFIELD_FIELD_TYPE_20 = "Table";
	public static final String DYNAFIELD_FIELD_TYPE_21 = "SubSet";
	public static final String DYNAFIELD_FIELD_TYPE_22 = "Contact";
	public static final String DYNAFIELD_FIELD_TYPE_23 = "Meeting";
	public static final String DYNAFIELD_FIELD_TYPE_24 = "Customer";
	
	public static final String DYNAFIELD_MULTISELECT_ENABLED = "Yes";
	public static final String DYNAFIELD_MULTISELECT_DISABLED = "No";
	
	public static final String STATUS_ALL = "All";
	public static final String STATUS_ACTIVE = "Active";
	public static final String STATUS_INACTIVE = "Inactive";
	public static final String STATUS_APPROVED = "Approved";
	public static final String STATUS_REJECTED = "Rejected";
	public static final String STATUS_PENDING = "Pending";
	
	public static final int ALERT_TYPE_NO_DATA= 1;
	public static final int ALERT_TYPE_ENTER_GEOFENCE= 2;
	public static final int ALERT_TYPE_LEAVE_GEOFENCE= 3;
	
	public static int DEVICE_TYPE_IOS = 1;
	public static int DEVICE_TYPE_ANDROID = 2;
	public static int DEVICE_TYPE_HTML5 = 3;
	
	public static final int TASK_ASSIGNED = 1;
	public static final int TASK_REASSIGNED = 2;
	public static final int TASK_CANCELED = 3;
	public static final int TASK_COMPLETED = 4;
	public static final int TASK_NOACTION = 5;
	public static final int TASK_INPROGRESS = 6;
	public static final int TASK_ACCEPTED = 7;
	public static final int TASK_REJECTED = 8;
	
	public static final String AUTO_REFERESH_MAP_INTERVAL = "AUTO_REFERESH_MAP_INTERVAL";
}
