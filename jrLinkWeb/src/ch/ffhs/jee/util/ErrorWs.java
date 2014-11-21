package ch.ffhs.jee.util;

public class ErrorWs {
	
	private String code;
	private String message;
	
	public ErrorWs () {
		this.setCode("500");
		this.setMessage("internal server error");
	}
	
	public ErrorWs (String code, String message) {
		this.setCode(code);;
		this.setMessage(message);
	}	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
