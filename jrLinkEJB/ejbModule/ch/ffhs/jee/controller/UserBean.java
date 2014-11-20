package ch.ffhs.jee.controller;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

	@Override
	public void create(Role role, String name, String password, Boolean isActive) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Long id, Role role, String name, String password, Boolean isActive) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

}
