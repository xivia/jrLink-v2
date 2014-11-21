package ch.ffhs.jee.controller;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

}
