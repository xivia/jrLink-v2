package ch.ffhs.jee.controller;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
		// TODO Auto-generated method stub
		// TODO check unique name - unique login/pw
		return new Long(0);
	}

	@Override
	public Long update(Long id, Role role, String name, String password, Boolean isActive) {
		// TODO Auto-generated method stub
		// TODO check unique name - unique login/pw
		return new Long(0);
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

}
