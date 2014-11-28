package ch.ffhs.jee.util;

public class ErrorWs409 extends ErrorWs {

	public ErrorWs409 () {
		this.setCode("409");
		this.setMessage("conflict");
	}
	
}
