package in.otpl.dnb.gw;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class LastTrackingDto implements Serializable{

	private static final long serialVersionUID = 1L;

	private long id;
	private long userId;
	private long trackingId;
	private long validTrackingId;
	private long nonCellsiteTrackingId;
	private int customerId;
	private String modificationTime;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getTrackingId() {
		return trackingId;
	}
	public void setTrackingId(long trackingId) {
		this.trackingId = trackingId;
	}
	public long getValidTrackingId() {
		return validTrackingId;
	}
	public void setValidTrackingId(long validTrackingId) {
		this.validTrackingId = validTrackingId;
	}
	public long getNonCellsiteTrackingId() {
		return nonCellsiteTrackingId;
	}
	public void setNonCellsiteTrackingId(long nonCellsiteTrackingId) {
		this.nonCellsiteTrackingId = nonCellsiteTrackingId;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public String getModificationTime() {
		return modificationTime;
	}
	public void setModificationTime(String modificationTime) {
		this.modificationTime = modificationTime;
	}

}