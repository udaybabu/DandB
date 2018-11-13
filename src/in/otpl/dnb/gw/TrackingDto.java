package in.otpl.dnb.gw;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class TrackingDto implements Serializable{

private static final long serialVersionUID = 1L;
	
	private String COMMA = ",";
	private long id;
	private int customerId;
	private String lattitude;
	private String longitude;
	private String altitude;
	private String accuracy;
	private String speed;
	private String direction;
	private String clientTime;
	private String dbTime;
	private long userId;
	private int landmarkId;
	private boolean landmarkIdNull = true;
	private String eventName;
	private int isValid;
	private short isCellsite;
	private String nearestLandmarkName;
	private String nearestLandmarkLatitude;
	private String nearestLandmarkLongitude;
	private String cellsiteLocAddres;// added for india-server
	private String status = "";
	public String getCOMMA() {
		return COMMA;
	}
	public void setCOMMA(String cOMMA) {
		COMMA = cOMMA;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public String getLattitude() {
		return lattitude;
	}
	public void setLattitude(String lattitude) {
		this.lattitude = lattitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getAltitude() {
		return altitude;
	}
	public void setAltitude(String altitude) {
		this.altitude = altitude;
	}
	public String getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(String accuracy) {
		this.accuracy = accuracy;
	}
	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getClientTime() {
		return clientTime;
	}
	public void setClientTime(String clientTime) {
		this.clientTime = clientTime;
	}
	public String getDbTime() {
		return dbTime;
	}
	public void setDbTime(String dbTime) {
		this.dbTime = dbTime;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public int getLandmarkId() {
		return landmarkId;
	}
	public void setLandmarkId(int landmarkId) {
		this.landmarkId = landmarkId;
	}
	public boolean isLandmarkIdNull() {
		return landmarkIdNull;
	}
	public void setLandmarkIdNull(boolean landmarkIdNull) {
		this.landmarkIdNull = landmarkIdNull;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public int getIsValid() {
		return isValid;
	}
	public void setIsValid(int isValid) {
		this.isValid = isValid;
	}
	public short getIsCellsite() {
		return isCellsite;
	}
	public void setIsCellsite(short isCellsite) {
		this.isCellsite = isCellsite;
	}
	public String getNearestLandmarkName() {
		return nearestLandmarkName;
	}
	public void setNearestLandmarkName(String nearestLandmarkName) {
		this.nearestLandmarkName = nearestLandmarkName;
	}
	public String getNearestLandmarkLatitude() {
		return nearestLandmarkLatitude;
	}
	public void setNearestLandmarkLatitude(String nearestLandmarkLatitude) {
		this.nearestLandmarkLatitude = nearestLandmarkLatitude;
	}
	public String getNearestLandmarkLongitude() {
		return nearestLandmarkLongitude;
	}
	public void setNearestLandmarkLongitude(String nearestLandmarkLongitude) {
		this.nearestLandmarkLongitude = nearestLandmarkLongitude;
	}
	public String getCellsiteLocAddres() {
		return cellsiteLocAddres;
	}
	public void setCellsiteLocAddres(String cellsiteLocAddres) {
		this.cellsiteLocAddres = cellsiteLocAddres;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
