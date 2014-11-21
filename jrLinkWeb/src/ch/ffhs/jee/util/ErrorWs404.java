package ch.ffhs.jee.util;

public class ErrorWs404 extends ErrorWs {

	public ErrorWs404 () {
		this.setCode("404");
		this.setMessage("not found");
	}
	
}
