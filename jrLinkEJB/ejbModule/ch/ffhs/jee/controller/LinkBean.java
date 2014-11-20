package ch.ffhs.jee.controller;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ch.ffhs.jee.model.Link;

/**
 * Session Bean implementation class LinkBean
 */
@Stateless
public class LinkBean implements LinkBeanLocal {

	@PersistenceContext
	private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public LinkBean() { }

	@Override
	public Collection<Link> getList() {
		return em.createNamedQuery("Link.findAll",Link.class).getResultList();
	}

	@Override
	public Link getById(Long id) {
		return em.find(Link.class, new Long(id));
	}

	@Override
	public void create(String name, String url) {
		Link link = new Link();
		
		link.setName(name);
		link.setUrl(url);
		
		em.persist(link);
		em.flush();
	}

	@Override
	public void update(Long id, String name, String url) {
		Link link = getById(id);
		
		link.setName(name);
		link.setUrl(url);
		
		em.merge(link);
		em.flush();
	}

	@Override
	public void delete(Long id) {
		Link link = getById(id);
		
		if (link != null) em.remove(link);
		em.flush();
	}

}
