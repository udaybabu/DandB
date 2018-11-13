package in.otpl.dnb.util;

import org.springframework.stereotype.Component;

@Component
public class ConfigManager {

	public static String baseServerURL = "";
	public static String googleMapKey = "";
	public static String version = "";
	public static int userShortIdleTime = 1;
	public static int userMediumIdleTime = 2;
	public static int userLongIdleTime = 3;
	private String mediaFilePath = "";
	private String mediaImagePath = "";
	private String mediaPdfFilePath = "";
	
	private int dnbCustId;
	
	private boolean schedulerMasterDataFetch = false;
	private int schedulerMasterDataFetchInterval;//Hours
	private boolean schedulerEnquiryPoolFetch = false;
	private boolean schedulerPdfCreator = false;
	private boolean schedulerDataManipulate = false;
	private boolean schedulerEnquiryDataCompletion = false;
	private boolean schedulerReassignment = false;
	
	private String dnbDbUrl = "";
	private String dnbDbPort = "";
	private String dnbDbName = "";
	private String dnbDbUser = "";
	private String dnbDbPassword = "";
	private String dnbDbClass = "";
	
	public static String getBaseServerURL() {
		return baseServerURL;
	}
	public static void setBaseServerURL(String baseServerURL) {
		ConfigManager.baseServerURL = baseServerURL;
	}
	public static String getGoogleMapKey() {
		return googleMapKey;
	}
	public static void setGoogleMapKey(String googleMapKey) {
		ConfigManager.googleMapKey = googleMapKey;
	}
	public static String getVersion() {
		return version;
	}
	public static void setVersion(String version) {
		ConfigManager.version = version;
	}
	public static int getUserShortIdleTime() {
		return userShortIdleTime;
	}
	public static void setUserShortIdleTime(int userShortIdleTime) {
		ConfigManager.userShortIdleTime = userShortIdleTime;
	}
	public static int getUserMediumIdleTime() {
		return userMediumIdleTime;
	}
	public static void setUserMediumIdleTime(int userMediumIdleTime) {
		ConfigManager.userMediumIdleTime = userMediumIdleTime;
	}
	public static int getUserLongIdleTime() {
		return userLongIdleTime;
	}
	public static void setUserLongIdleTime(int userLongIdleTime) {
		ConfigManager.userLongIdleTime = userLongIdleTime;
	}
	public String getMediaFilePath() {
		return mediaFilePath;
	}
	public void setMediaFilePath(String mediaFilePath) {
		this.mediaFilePath = mediaFilePath;
	}
	public String getMediaImagePath() {
		return mediaImagePath;
	}
	public void setMediaImagePath(String mediaImagePath) {
		this.mediaImagePath = mediaImagePath;
	}
	public String getMediaPdfFilePath() {
		return mediaPdfFilePath;
	}
	public void setMediaPdfFilePath(String mediaPdfFilePath) {
		this.mediaPdfFilePath = mediaPdfFilePath;
	}
	public int getDnbCustId() {
		return dnbCustId;
	}
	public void setDnbCustId(int dnbCustId) {
		this.dnbCustId = dnbCustId;
	}
	public boolean isSchedulerMasterDataFetch() {
		return schedulerMasterDataFetch;
	}
	public void setSchedulerMasterDataFetch(boolean schedulerMasterDataFetch) {
		this.schedulerMasterDataFetch = schedulerMasterDataFetch;
	}
	public int getSchedulerMasterDataFetchInterval() {
		return schedulerMasterDataFetchInterval;
	}
	public void setSchedulerMasterDataFetchInterval(
			int schedulerMasterDataFetchInterval) {
		this.schedulerMasterDataFetchInterval = schedulerMasterDataFetchInterval;
	}
	public boolean isSchedulerEnquiryPoolFetch() {
		return schedulerEnquiryPoolFetch;
	}
	public void setSchedulerEnquiryPoolFetch(boolean schedulerEnquiryPoolFetch) {
		this.schedulerEnquiryPoolFetch = schedulerEnquiryPoolFetch;
	}
	public boolean isSchedulerPdfCreator() {
		return schedulerPdfCreator;
	}
	public void setSchedulerPdfCreator(boolean schedulerPdfCreator) {
		this.schedulerPdfCreator = schedulerPdfCreator;
	}
	public boolean isSchedulerDataManipulate() {
		return schedulerDataManipulate;
	}
	public void setSchedulerDataManipulate(boolean schedulerDataManipulate) {
		this.schedulerDataManipulate = schedulerDataManipulate;
	}
	public boolean isSchedulerEnquiryDataCompletion() {
		return schedulerEnquiryDataCompletion;
	}
	public void setSchedulerEnquiryDataCompletion(
			boolean schedulerEnquiryDataCompletion) {
		this.schedulerEnquiryDataCompletion = schedulerEnquiryDataCompletion;
	}
	public boolean isSchedulerReassignment() {
		return schedulerReassignment;
	}
	public void setSchedulerReassignment(boolean schedulerReassignment) {
		this.schedulerReassignment = schedulerReassignment;
	}
	public String getDnbDbUrl() {
		return dnbDbUrl;
	}
	public void setDnbDbUrl(String dnbDbUrl) {
		this.dnbDbUrl = dnbDbUrl;
	}
	public String getDnbDbPort() {
		return dnbDbPort;
	}
	public void setDnbDbPort(String dnbDbPort) {
		this.dnbDbPort = dnbDbPort;
	}
	public String getDnbDbName() {
		return dnbDbName;
	}
	public void setDnbDbName(String dnbDbName) {
		this.dnbDbName = dnbDbName;
	}
	public String getDnbDbUser() {
		return dnbDbUser;
	}
	public void setDnbDbUser(String dnbDbUser) {
		this.dnbDbUser = dnbDbUser;
	}
	public String getDnbDbPassword() {
		return dnbDbPassword;
	}
	public void setDnbDbPassword(String dnbDbPassword) {
		this.dnbDbPassword = dnbDbPassword;
	}
	public String getDnbDbClass() {
		return dnbDbClass;
	}
	public void setDnbDbClass(String dnbDbClass) {
		this.dnbDbClass = dnbDbClass;
	}

}