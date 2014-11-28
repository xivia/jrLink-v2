package ch.ffhs.jee.controller;

import java.util.Collection;
import java.util.Iterator;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import ch.ffhs.jee.model.Role;
import ch.ffhs.jee.model.User;

/**
 * Session Bean implementation class UserBean
 */
@Stateless
public class UserBean implements UserBeanLocal {

	@PersistenceContext
	private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public UserBean() { }

	@Override
	public Collection<User> getList() {
		return em.createNamedQuery("User.findAll",User.class).getResultList();
	}

	@Override
	public User getById(Long id) {
		return em.find(User.class, new Long(id));
	}

    // only for private use
	private Collection<User> getByName(String name) {
		TypedQuery<User> q = em.createNamedQuery("User.findByName", User.class);
		q.setParameter("name", name);

		return q.getResultList();
    }
    
	
	@Override
	public User getByCredentials(String user, String password) {
		Query q = em.createNamedQuery("User.findByCredentials", User.class);
		q.setParameter("name", user);
		q.setParameter("password", password);

		if (!q.getResultList().isEmpty()) {
			return (User) q.getSingleResult();
		} else {
			return null;
		}
	}

	@Override
	public Long create(Role role, String name, String password, Boolean isActive) {
		// initial return
		Long result = new Long(-1);
		
		// check user name already exists
		if (getByName(name).size() == 0) {
			User user = new User();
			
			user.setRole(role);
			user.setName(name);
			user.setPassword(password);
			user.setActive(isActive);
			
			em.persist(user);
			em.flush();
			
			result = user.getId();
		} else {
			// conflict
			result = new Long(0);
		}
		
		return result;
	}

	@Override
	public Long update(Long id, Role role, String name, String password, Boolean isActive) {
		// initial return
		Long result = new Long(-1);
		
		// check user name already exists
		Boolean userExists = false;
		Iterator<User> iter = getByName(name).iterator();
		while(!userExists && iter.hasNext()) {
			User item = iter.next();
			if (item.getId() == id) userExists = true;
		}
		
		if (!userExists) {
			User user = getById(id);
			
			user.setRole(role);
			user.setName(name);
			user.setPassword(password);
			user.setActive(isActive);
			
			em.merge(user);
			em.flush();
			
			result = id;
		} else {
			// conflict
			result = new Long(0);
		}
		
		return result;
	}

	@Override
	public void delete(Long id) {
		User user = getById(id);
		
		if (user != null) em.remove(user);
		em.flush();
	}

}
