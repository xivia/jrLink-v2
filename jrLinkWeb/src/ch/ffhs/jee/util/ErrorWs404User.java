package ch.ffhs.jee.util;

public class ErrorWs404User extends ErrorWs {

	public ErrorWs404User () {
		this.setCode("404");
		this.setMessage("user not found");
	}
	
}
