package ch.ffhs.jee.ws;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ch.ffhs.jee.controller.LinkBeanLocal;
import ch.ffhs.jee.model.Link;

@Path("link")
@Stateless
public class LinkWs {
	
	@EJB
	private LinkBeanLocal linkBean;
	
	public LinkWs() { }
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll() {
		Collection<Link> links = linkBean.getList();
		
		return Response.status(200).entity(links).build();
	}
	
	@GET
	@Path("{id}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getById(@PathParam("id") String id) {
		Response response;
		
		Long lid;
		try {
			lid = Long.parseLong(id); 
		} 
		catch (NumberFormatException e) {
			lid = new Long(-1);
		}
		
		if (lid > 0) {
			Link links = linkBean.getById(new Long(lid));
			if (links != null) {
				response = Response.status(200).entity(links).build();
			} else {
				response = Response.status(404).entity("not found").build();
			}
		} else {
			response = Response.status(400).entity("bad request").build();
		}
		
		return response;
	}
	
	
	
	
}
