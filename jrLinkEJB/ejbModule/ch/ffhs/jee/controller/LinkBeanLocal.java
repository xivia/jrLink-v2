package ch.ffhs.jee.controller;

import java.util.Collection;

import javax.ejb.Local;

import ch.ffhs.jee.model.Link;

@Local
public interface LinkBeanLocal {

	/**
	 * get all records
	 */
	Collection<Link> getList();	
	
	/**
	 * get one record by id
	 */
	Link getById(Long id);

	/**
	 * add record
	 */
	Long create(String name, String url);
	
	/**
	 * update record
	 */
	void update(Long id, String name, String url);
	
	/**
	 * delete record
	 */
	void delete(Long id);
	
}
