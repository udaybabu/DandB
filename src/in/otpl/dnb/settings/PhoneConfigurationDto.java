package in.otpl.dnb.settings;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class PhoneConfigurationDto  implements Serializable{

private static final long serialVersionUID = 1L;
	
	private long id;
	private int customerId;
	private boolean customerIdNull = true;
	private String paramName = "";
	private String stringParamValue = "";
	private int intParamValue = 0;
	private boolean intParamValueNull = true;
	private String creationTime;
	private String modificationTime;
	
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
	public boolean isCustomerIdNull() {
		return customerIdNull;
	}
	public void setCustomerIdNull(boolean customerIdNull) {
		this.customerIdNull = customerIdNull;
	}
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public String getStringParamValue() {
		return stringParamValue;
	}
	public void setStringParamValue(String stringParamValue) {
		this.stringParamValue = stringParamValue;
	}
	public int getIntParamValue() {
		return intParamValue;
	}
	public void setIntParamValue(int intParamValue) {
		this.intParamValue = intParamValue;
	}
	public boolean isIntParamValueNull() {
		return intParamValueNull;
	}
	public void setIntParamValueNull(boolean intParamValueNull) {
		this.intParamValueNull = intParamValueNull;
	}
	public String getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}
	public String getModificationTime() {
		return modificationTime;
	}
	public void setModificationTime(String modificationTime) {
		this.modificationTime = modificationTime;
	}
	
}
