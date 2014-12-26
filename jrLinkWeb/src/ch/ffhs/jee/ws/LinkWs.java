package ch.ffhs.jee.ws;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ch.ffhs.jee.util.ErrorWs400;
import ch.ffhs.jee.util.ErrorWs401;
import ch.ffhs.jee.util.ErrorWs404;
import ch.ffhs.jee.controller.LinkBeanLocal;
import ch.ffhs.jee.controller.UserBeanLocal;
import ch.ffhs.jee.model.Link;
import ch.ffhs.jee.model.User;

@Path("link")
@Stateless
public class LinkWs {
	
	@EJB
	private LinkBeanLocal linkBean;
	@EJB
	private UserBeanLocal userBean;
	
	public LinkWs() { }
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll( @QueryParam(value = "u") String user, 
							@QueryParam(value = "p") String password) {
		Response response;
		
		// check user, password
		User luser = userBean.getByCredentials(user, password);
		
		if (luser != null) {
			Collection<Link> links = linkBean.getList();
			response = Response.status(200).entity(links).build();
		} else {
			response = Response.status(401).entity(new ErrorWs401()).build();
		}
		
		return response;
	}
	
	@GET
	@Path("{id}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getById(@PathParam("id") String id, 
							@QueryParam(value = "u") String user, 
							@QueryParam(value = "p") String password) {
		Response response;
		
		// check user, password
		User luser = userBean.getByCredentials(user, password);
		
		if (luser != null) {
			Long lid;
			try {
				lid = Long.parseLong(id); 
			} 
			catch (NumberFormatException e) {
				lid = new Long(0);
			}
			
			// check id (above catch) 
			if (lid > 0) {
				Link link = linkBean.getById(new Long(lid));
				// check id (in database)
				if (link != null) {
					response = Response.status(200).entity(link).build();
				} else {
					response = Response.status(404).entity(new ErrorWs404()).build();
				}
			} else {
				response = Response.status(400).entity(new ErrorWs400()).build();
			}
		} else {
			response = Response.status(401).entity(new ErrorWs401()).build();
		}
		
		return response;
	}
	
	@PUT
	@Path("{id}/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(Link link, @PathParam("id") String id, 
						   @QueryParam(value = "u") String user, 
						   @QueryParam(value = "p") String password) {				
		Response response;
		
		// check user, password
		User luser = userBean.getByCredentials(user, password);
		
		if (luser != null) {
			Long lid;
			try {
				lid = Long.parseLong(id); 
			} 
			catch (NumberFormatException e) {
				lid = new Long(0);
			}
			
			// check id (above catch) 
			if (lid > 0) {
				Link clink = linkBean.getById(new Long(lid));
				// check id (in database)
				if (clink != null) {
					linkBean.update(lid, link.getName(), link.getUrl());
					Link updLink = linkBean.getById(new Long(lid));
					
					response = Response.status(200).entity(updLink).build();
				} else {
					response = Response.status(404).entity(new ErrorWs404()).build();
				}
			} else {
				response = Response.status(400).entity(new ErrorWs400()).build();
			}			
		} else {
			response = Response.status(401).entity(new ErrorWs401()).build();
		}

		return response;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(Link link,
						   @QueryParam(value = "u") String user, 
						   @QueryParam(value = "p") String password) {
		Response response;
		
		// check user, password
		User luser = userBean.getByCredentials(user, password);
		
		if (luser != null) {
			Long lid = linkBean.create(link.getName(), link.getUrl());
			Link newLink = linkBean.getById(new Long(lid));
					
			response = Response.status(200).entity(newLink).build();
		} else {
			response = Response.status(401).entity(new ErrorWs401()).build();
		}

		return response;
	}
	
	@DELETE
	@Path("{id}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") String id, 
						   @QueryParam(value = "u") String user, 
						   @QueryParam(value = "p") String password) {			
		Response response;
		
		// check user, password
		User luser = userBean.getByCredentials(user, password);
		
		if (luser != null) {
			Long lid;
			try {
				lid = Long.parseLong(id); 
			} 
			catch (NumberFormatException e) {
				lid = new Long(0);
			}
			
			// check id (above catch) 
			if (lid > 0) {
				Link clink = linkBean.getById(new Long(lid));
				// check id (in database)
				if (clink != null) {
					linkBean.delete(lid);
					
					response = Response.status(200).entity("{}").build();
				} else {
					response = Response.status(404).entity(new ErrorWs404()).build();
				}
			} else {
				response = Response.status(400).entity(new ErrorWs400()).build();
			}			
		} else {
			response = Response.status(401).entity(new ErrorWs401()).build();
		}

		return response;
	}	
	
}
