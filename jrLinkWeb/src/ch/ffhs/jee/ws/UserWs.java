package ch.ffhs.jee.ws;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ch.ffhs.jee.controller.RoleBeanLocal;
import ch.ffhs.jee.controller.UserBeanLocal;
import ch.ffhs.jee.model.User;
import ch.ffhs.jee.util.ErrorWs400;
import ch.ffhs.jee.util.ErrorWs401;
import ch.ffhs.jee.util.ErrorWs404Role;
import ch.ffhs.jee.util.ErrorWs404User;
import ch.ffhs.jee.util.ErrorWs409;

@Path("user")
@Stateless
public class UserWs {

	// JSON data
	public static class UserData {
		private String name;
		private String password;
		private String active;
		private String role;
		
		public String getName() { return name; }
		public String getPassword() { return password; }
		public Boolean isActive() { if (active != null) return new Boolean(active); else return null; }
		public String getRole() { return role; }
	}
	
	@EJB
	private UserBeanLocal userBean;
	@EJB
	private RoleBeanLocal roleBean;
	
	public UserWs() { }
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll( @QueryParam(value = "u") String user, 
							@QueryParam(value = "p") String password) {
		Response response;
		
		// check user, password
		User luser = userBean.getByCredentials(user, password);
		
		if ((luser != null) && (luser.isAdministrator())) {
			Collection<User> rusers = userBean.getList();
			
			response = Response.status(200).entity(formatJSON(rusers)).build();
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
		
		if ((luser != null) && (luser.isAdministrator())) {
			Long lid;
			try {
				lid = Long.parseLong(id); 
			} 
			catch (NumberFormatException e) {
				lid = new Long(0);
			}
			
			// check id (above catch) 
			if (lid > 0) {
				User ruser = userBean.getById(new Long(lid));
				// check id (in database)
				if (ruser != null) {
					Collection<User> rusers = new ArrayList<User>();
					rusers.add(ruser);
					
					response = Response.status(200).entity(formatJSON(rusers).iterator().next()).build();
				} else {
					response = Response.status(404).entity(new ErrorWs404User()).build();
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
	public Response update(UserData uuser, @PathParam("id") String id, 
						   @QueryParam(value = "u") String user, 
						   @QueryParam(value = "p") String password) {				
		Response response;
		
		// check user, password
		User luser = userBean.getByCredentials(user, password);
		
		if ((luser != null) && (luser.isAdministrator())) {
			Long lid;
			try {
				lid = Long.parseLong(id); 
			} 
			catch (NumberFormatException e) {
				lid = new Long(0);
			}
			
			// check id (above catch) 
			if (lid > 0) {
				User cuser = userBean.getById(new Long(lid));
				// check id (in database)
				if (cuser != null) {
					// check role (in database)
					if ((uuser.getRole() == null) || 
						(roleBean.getByName(uuser.getRole()) != null)) {
						
						Long result = userBean.update(lid, roleBean.getByName(uuser.getRole()), 
														uuser.getName(), uuser.getPassword(), 
														uuser.isActive());
						
						if (result > 0) {
							User updUser = userBean.getById(new Long(lid));
							
							Collection<User> rusers = new ArrayList<User>();
							rusers.add(updUser);
							
							response = Response.status(200).entity(formatJSON(rusers).iterator().next()).build();						
						} else {
							response = Response.status(409).entity(new ErrorWs409()).build();						
						}
					} else {
						response = Response.status(404).entity(new ErrorWs404Role()).build();
					}
				} else {
					response = Response.status(404).entity(new ErrorWs404User()).build();
				}
			} else {
				response = Response.status(400).entity(new ErrorWs400()).build();
			}
		} else {
			response = Response.status(401).entity(new ErrorWs401()).build();
		}
	
		return response;
	}
	
	// TODO post
	// -> role not found?
	
	// TODO delete
	
	
	Collection<Map<String, String>> formatJSON(Collection<User> users) {
		Collection<Map<String, String>> myCollection = new ArrayList<Map<String,String>>();
		
		Iterator<User> iter = users.iterator();
		while(iter.hasNext()) {
			User item = iter.next();
			
			Map<String, String> myMap = new LinkedHashMap<String, String>();
			myMap.put("id", item.getId().toString());
			myMap.put("name", item.getName());
			myMap.put("password", "*****");
			myMap.put("active", item.isActive().toString());	
			myMap.put("role", item.getRole().getNameShort());
			
			myCollection.add(myMap);
		}
		
		return myCollection;
	}
	
}
