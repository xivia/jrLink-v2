package ch.ffhs.jee.controller;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ch.ffhs.jee.model.Role;

/**
 * Session Bean implementation class RoleBean
 */
@Stateless
public class RoleBean implements RoleBeanLocal {

	@PersistenceContext
	private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public RoleBean() { }

	@Override
	public Collection<Role> getList() {
		return em.createNamedQuery("Role.findAll",Role.class).getResultList();
	}

	@Override
	public Role getById(Long id) {
		return em.find(Role.class, new Long(id));
	}

	@Override
	public Role getByName(String name) {
		Query q = em.createNamedQuery("Role.findByName", Role.class);
		q.setParameter("name", name);
		
		if (!q.getResultList().isEmpty()) {
			return (Role) q.getSingleResult();
		} else {
			return null;
		}
	}

}
