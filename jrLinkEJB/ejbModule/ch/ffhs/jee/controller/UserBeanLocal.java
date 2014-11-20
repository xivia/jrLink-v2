package ch.ffhs.jee.controller;

import java.util.Collection;

import javax.ejb.Local;

import ch.ffhs.jee.model.Role;
import ch.ffhs.jee.model.User;

@Local
public interface UserBeanLocal {

	/**
	 * get all records
	 */
	Collection<User> getList();	
	
	/**
	 * get one record by id
	 */
	User getById(Long id);
	
	/**
	 * get user by credentials (check user/password)
	 */
	User getByCredentials(String user, String password);

	/**
	 * add record
	 */
	void create(Role role, String name, String password, Boolean isActive);
	
	/**
	 * update record
	 */
	void update(Long id, Role role, String name, String password, Boolean isActive);
	
	/**
	 * delete record
	 */
	void delete(Long id);

}
