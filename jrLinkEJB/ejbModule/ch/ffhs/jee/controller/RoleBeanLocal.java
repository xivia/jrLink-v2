package ch.ffhs.jee.controller;

import java.util.Collection;

import javax.ejb.Local;

import ch.ffhs.jee.model.Role;

@Local
public interface RoleBeanLocal {

	/**
	 * get all records
	 */
	Collection<Role> getList();	
	
	/**
	 * get one record by id
	 */
	Role getById(Long id);

	/**
	 * add record
	 */
	void create(String name, String desciption);
	
	/**
	 * update record
	 */
	void update(Long id, String name, String desciption);
	
	/**
	 * delete record
	 */
	void delete(Long id);
	
}
