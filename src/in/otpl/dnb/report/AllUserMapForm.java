package in.otpl.dnb.report;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class AllUserMapForm implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int customerId;
	private long userId ;
    private int includeCellSite;

    public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public int getIncludeCellSite() {
		return includeCellSite;
	}
	public void setIncludeCellSite(int includeCellSite) {
		this.includeCellSite = includeCellSite;
	}
    
}