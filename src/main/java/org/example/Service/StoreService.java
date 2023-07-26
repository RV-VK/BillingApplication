package org.example.Service;

import org.example.DAO.ApplicationErrorException;
import org.example.Entity.Store;

import java.sql.SQLException;

public interface StoreService {

	/**
	 * This method invokes the DAO of the Store entity and serves the Create function.
	 *
	 * @param store Input Store.
	 * @return Store - Created store.
	 * @throws SQLException              Exception thrown based on SQL syntax.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	Store create(Store store) throws SQLException, ApplicationErrorException, InvalidTemplateException;

	/**
	 * This method invokes the DAO of the Store entity and serves the View Function
	 *
	 * @return Store Object
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	Store view() throws ApplicationErrorException;


	/**
	 * This method invokes the DAO of the Store entity and serves the Edit function.
	 *
	 * @param store Edited store.
	 * @return Store - Resulted Store after Edit.
	 * @throws SQLException              Exception thrown based on SQL syntax.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	Store edit(Store store) throws SQLException, ApplicationErrorException, InvalidTemplateException;

	/**
	 * This method invokes the DAO of the Store entity and serves the delete function.
	 *
	 * @param adminPassword Password string to allow deletion.
	 * @return resultCode - Integer.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	Integer delete(String userName, String adminPassword) throws ApplicationErrorException;
}
