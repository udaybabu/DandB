package in.otpl.dnb.gw;

import java.io.Serializable;
import java.util.Date;

import org.springframework.stereotype.Component;
	@Component
	public class CurrentUserTrackingForm implements Serializable{

		private static final long serialVersionUID = 1L;

		private long id;
		private String firstName;
		private String lastName; 
		private long teamId;
		private String assignmentStatus;
		private int customerId;
		private String lattitude;
		private String longitude;
		private String altitude;
		private String accuracy;
		private String creationTime;
		private String modificationTime;
		private long userId;
		private long landmarkId;
		private boolean landmarkIdNull = true;
		private int isValid;
		private short isCellsite;
		private String landmarkLatitude;
		private String landmarkLongitude;
		private String landmarkName;
		private String status = "";
		
		
		public String getModificationTime() {
			return modificationTime;
		}
		public void setModificationTime(String modificationTime) {
			this.modificationTime = modificationTime;
		}
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
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
		public String getName() {
			return this.firstName+" "+this.lastName;
		}
		public long getTeamId() {
			return teamId;
		}
		public void setTeamId(long teamId) {
			this.teamId = teamId;
		}
		public String getAssignmentStatus() {
			return assignmentStatus;
		}
		public void setAssignmentStatus(String assignmentStatus) {
			this.assignmentStatus = assignmentStatus;
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
		public String getCreationTime() {
			return creationTime;
		}
		public void setCreationTime(String creationTime) {
			this.creationTime = creationTime;
		}
		public long getUserId() {
			return userId;
		}
		public void setUserId(long userId) {
			this.userId = userId;
		}
		public long getLandmarkId() {
			return landmarkId;
		}
		public void setLandmarkId(long landmarkId) {
			this.landmarkId = landmarkId;
		}
		public boolean isLandmarkIdNull() {
			return landmarkIdNull;
		}
		public void setLandmarkIdNull(boolean landmarkIdNull) {
			this.landmarkIdNull = landmarkIdNull;
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
		public String getLandmarkLatitude() {
			return landmarkLatitude;
		}
		public void setLandmarkLatitude(String landmarkLatitude) {
			this.landmarkLatitude = landmarkLatitude;
		}
		public String getLandmarkLongitude() {
			return landmarkLongitude;
		}
		public void setLandmarkLongitude(String landmarkLongitude) {
			this.landmarkLongitude = landmarkLongitude;
		}
		public String getLandmarkName() {
			return landmarkName;
		}
		public void setLandmarkName(String landmarkName) {
			this.landmarkName = landmarkName;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
}
