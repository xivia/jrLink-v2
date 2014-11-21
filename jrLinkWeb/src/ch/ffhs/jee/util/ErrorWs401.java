package ch.ffhs.jee.util;

public class ErrorWs401 extends ErrorWs {

	public ErrorWs401 () {
		this.setCode("401");
		this.setMessage("unauthorized");
	}
	
}
