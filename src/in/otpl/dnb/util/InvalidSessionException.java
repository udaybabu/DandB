package in.otpl.dnb.util;

public class InvalidSessionException extends Exception{

	private static final long serialVersionUID = 1L;
	private String errorMessage;
	
	public InvalidSessionException(String errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
}
