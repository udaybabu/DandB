package in.otpl.dnb.report;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class UserTrackingDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private String clientTime;
	private String firstName;
	private String lastName;
	private String eventName;
	private String latitude;
	private String longitude;
	private int cellSite;
	private String cellSiteLocation;
	private String dbTime;
	private String speed;
	private String accuracy;
	private String bccStatus = "";
	private String fullName;
	private String appVersion;
	
	public String getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getClientTime() {
		return clientTime;
	}
	public void setClientTime(String clientTime) {
		this.clientTime = clientTime;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public int getCellSite() {
		return cellSite;
	}
	public void setCellSite(int cellSite) {
		this.cellSite = cellSite;
	}
	public String getCellSiteLocation() {
		return cellSiteLocation;
	}
	public void setCellSiteLocation(String cellSiteLocation) {
		this.cellSiteLocation = cellSiteLocation;
	}
	public String getDbTime() {
		return dbTime;
	}
	public void setDbTime(String dbTime) {
		this.dbTime = dbTime;
	}
	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	public String getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(String accuracy) {
		this.accuracy = accuracy;
	}
	public String getBccStatus() {
		return bccStatus;
	}
	public void setBccStatus(String bccStatus) {
		this.bccStatus = bccStatus;
	}
	
}