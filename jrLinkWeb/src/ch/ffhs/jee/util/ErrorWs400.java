package ch.ffhs.jee.util;

public class ErrorWs400 extends ErrorWs {

	public ErrorWs400 () {
		this.setCode("400");
		this.setMessage("bad request");
	}
	
}
