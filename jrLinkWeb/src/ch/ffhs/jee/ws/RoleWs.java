package ch.ffhs.jee.ws;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ch.ffhs.jee.controller.RoleBeanLocal;
import ch.ffhs.jee.controller.UserBeanLocal;
import ch.ffhs.jee.model.Role;
import ch.ffhs.jee.model.User;
import ch.ffhs.jee.util.ErrorWs401;

@Path("role")
@Stateless
public class RoleWs {

	@EJB
	private RoleBeanLocal roleBean;
	@EJB
	private UserBeanLocal userBean;
	
	public RoleWs() { }
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll( @QueryParam(value = "u") String user, 
							@QueryParam(value = "p") String password) {
		Response response;
		
		// check user, password
		User luser = userBean.getByCredentials(user, password);
		
		if ((luser != null) && (luser.isAdministrator())) {
			Collection<Role> roles = roleBean.getList();
			
			response = Response.status(200).entity(formatJSON(roles)).build();
		} else {
			response = Response.status(401).entity(new ErrorWs401()).build();
		}
		
		return response;
	}
	
	Collection<Map<String, String>> formatJSON(Collection<Role> roles) {
		Collection<Map<String, String>> myCollection = new ArrayList<Map<String,String>>();
		
		Iterator<Role> iter = roles.iterator();
		while(iter.hasNext()) {
			Role item = iter.next();
			
			Map<String, String> myMap = new LinkedHashMap<String, String>();
			myMap.put("name", item.getNameShort());
			myMap.put("description", item.getNameLong());	
			
			myCollection.add(myMap);
		}
		
		return myCollection;
	}
	
}
