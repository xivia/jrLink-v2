package ch.ffhs.jee.util;

public class ErrorWs404Role extends ErrorWs {

	public ErrorWs404Role () {
		this.setCode("404");
		this.setMessage("role not found");
	}
	
}
