package ch.ffhs.jee.ws;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.util.GenericType;

import ch.ffhs.jee.controller.LinkBeanLocal;
import ch.ffhs.jee.controller.UserBeanLocal;
import ch.ffhs.jee.model.Link;
import ch.ffhs.jee.model.User;
import ch.ffhs.jee.util.ErrorWs400;
import ch.ffhs.jee.util.ErrorWs401;

@Path("sync")
@Stateless
public class SyncWs {

	// JSON data
	public static class SyncRequest {
		private String url;
		private String login;
		private String password;
		
		public String getUrl() { return url; }
		public String getLogin() { return login; }
		public String getPassword() { return password; }
	}
	
	// JSON response
	public static class LinkResponse {
		private String name;
		private String url;
		
		public String getName() { return name; }
		public String getUrl() { return url; }
	}
	
	@EJB
	private LinkBeanLocal linkBean;	
	@EJB
	private UserBeanLocal userBean;
	
	public SyncWs() { }

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll( @QueryParam(value = "u") String user, 
							@QueryParam(value = "p") String password) {
		Response response;
		
		// check user, password
		User luser = userBean.getByCredentials(user, password);
		
		if ((luser != null) && (luser.isSynchronizer())) {
			Collection<Link> links = linkBean.getList();
			
			response = Response.status(200).entity(removeIds(links)).build();
		} else {
			response = Response.status(401).entity(new ErrorWs401()).build();
		}
		
		return response;
	}
	
	private Collection<Map<String, String>> removeIds(Collection<Link> links) {
		Collection<Map<String, String>> linksWithoutIds = new ArrayList<Map<String,String>>();
		
		Iterator<Link> iter = links.iterator();
		while(iter.hasNext()) {
			Link item = iter.next();
			
			Map<String, String> myMap = new LinkedHashMap<String, String>();
			myMap.put("name", item.getName());
			myMap.put("url", item.getUrl());
			
			linksWithoutIds.add(myMap);
		}
		
		return linksWithoutIds;
	}

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
			// check required fields
			if ((syncRequest.getUrl() != null) &&
				 (syncRequest.getLogin() != null) &&
				 (syncRequest.getPassword() != null)) {
				
				String url = syncRequest.getUrl();
				url = url.replace("%login%", syncRequest.getLogin());
				url = url.replace("%password%", syncRequest.getPassword());
				
				// prepare call
				ClientRequest clientRequest = new ClientRequest(url);
				try {
					// run call
					ClientResponse<Object> clientResponse = clientRequest.get(Object.class);
					List<LinkResponse> responseLinkList = clientResponse.getEntity(new GenericType<List<LinkResponse>>() {});
					
					// synchronize data
					syncData(responseLinkList);
					
					response = Response.status(200).entity("{}").build();
				} catch (Exception e) {
					response = Response.status(400).entity(new ErrorWs400()).build();
					// e.printStackTrace();
				}  
			} else {
				response = Response.status(400).entity(new ErrorWs400()).build();
			}
		} else {
			response = Response.status(401).entity(new ErrorWs401()).build();
		}
		
		return response;
	}
	
	// temporary container
	public class LinkContainer {
		private Long id;
		private String name;
		private String url;
		private Boolean toDelete;
		
		LinkContainer(Long id, String name, String url) {
			this.setId(id);
			this.setName(name);
			this.setUrl(url);
			this.setToDelete(true);
		}

		public Long getId() { return id; }
		public void setId(Long id) { this.id = id; }

		public String getName() { return name; }
		public void setName(String name) { this.name = name; }

		public String getUrl() { return url; }
		public void setUrl(String url) { this.url = url; }

		public Boolean isToDelete() { return toDelete; }
		public void setToDelete(Boolean toDelete) { this.toDelete = toDelete; }
		
	}
	
	void syncData(List<LinkResponse> responseLinkList) {
		
		// get local links (marked as delete)
		Collection<LinkContainer> linkContainer = new ArrayList<LinkContainer>();
		Collection<Link> localLinkList = linkBean.getList();
		Iterator<Link> iter = localLinkList.iterator();
		while(iter.hasNext()) {
			Link item = iter.next();
			linkContainer.add(new LinkContainer(item.getId(), item.getName(), item.getUrl()));
		}	
		
		// get response links and merge
		Iterator<LinkResponse> respIter = responseLinkList.iterator();
		while(respIter.hasNext()) {
			LinkResponse respItem = respIter.next();
			
			// check link exits 
			Boolean linkExist = false;
			Iterator<LinkContainer> contIter = linkContainer.iterator();
			while (!linkExist && contIter.hasNext()) {
				LinkContainer item = contIter.next();
				if (item.getUrl().equalsIgnoreCase(respItem.getUrl())) {
					linkExist = true;
					// unmark delete
					item.setToDelete(false);
				}
			}
			// add new link
			if (!linkExist) {
				linkBean.create(respItem.getName(), respItem.getUrl());
			}
		}	
		
		// delete marked links
		Iterator<LinkContainer> contIterDel = linkContainer.iterator();
		while (contIterDel.hasNext()) {
			LinkContainer delItem = contIterDel.next();
			if (delItem.isToDelete()) {
				linkBean.delete(delItem.getId());
			}
		}
	}
}
