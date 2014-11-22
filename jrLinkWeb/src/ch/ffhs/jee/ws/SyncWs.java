package ch.ffhs.jee.ws;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ch.ffhs.jee.controller.UserBeanLocal;
import ch.ffhs.jee.model.User;
import ch.ffhs.jee.util.ErrorWs401;

@Path("sync")
@Stateless
public class SyncWs {

	private class SyncRequest {
		private String url;
		private String login;
		private String password;
		
		//public SyncRequest() { }
		
		public String getUrl() { return url; }
		//public void setUrl(String url) { this.url = url; }
		
		public String getLogin() { return login; }
		//public void setLogin(String login) { this.login = login; }
		
		public String getPassword() { return password; }
		//public void setPassword(String password) { this.password = password;}
	}
	
	@EJB
	private UserBeanLocal userBean;
	
	public SyncWs() { }
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response start(SyncRequest syncRequest,
						  @QueryParam(value = "u") String user, 
						  @QueryParam(value = "p") String password) {
		Response response;
		
		// check user, password
				User luser = userBean.getByCredentials(user, password);
				
				if ((luser != null) && (luser.isSynchronizer())) {
					
					String url = syncRequest.getUrl();
					url.replace("%login%", syncRequest.getLogin());
					url.replace("%password%", syncRequest.getPassword());
					
					// TODO call link-get and synchronize 
							
					response = Response.status(200).entity("").build();
				} else {
					response = Response.status(401).entity(new ErrorWs401()).build();
				}
		
		return response;
	}
	
}
