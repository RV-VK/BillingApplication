package DAO;

import Entity.Store;

import java.sql.SQLException;

public interface StoreDAO {

	/**
	 * This method creates an Entry in the Store table.
	 *
	 * @param store Input Store entity.
	 * @return Store - Created store.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 * @throws SQLException              Exception thrown based on SQL syntax.
	 */
	Store create(Store store) throws ApplicationErrorException, SQLException;

	/**
	 * This method updates the attributes of the Store entry in the Store table.
	 *
	 * @param store Updated Store entity.
	 * @return statusCode - Integer.
	 * @throws SQLException              Exception thrown based on SQL syntax.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */

	Store edit(Store store) throws SQLException, ApplicationErrorException;

	/**
	 * This method deleted the store Entry in the Store table.
	 *
	 * @param adminPassword Password String to allow to delete store.
	 * @return statusCode - Integer.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */

	Integer delete(String adminPassword) throws ApplicationErrorException;
}
