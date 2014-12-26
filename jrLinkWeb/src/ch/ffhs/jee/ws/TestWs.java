package ch.ffhs.jee.ws;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("test")
@Stateless
public class TestWs {

	public TestWs() { }
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response test() {
		Response response;
		
		response = Response.status(200).entity("{\"test\": \"ok\"}").build();
		
		return response;
	}
}
