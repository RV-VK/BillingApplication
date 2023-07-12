package org.example.Service;

import org.example.DAO.ApplicationErrorException;
import org.example.DAO.UniqueConstraintException;
import org.example.Entity.User;

import java.sql.SQLException;

public interface LoginService {
	/**
	 * This method invokes the DAO of the Product entity and serves the Initial setup function.
	 *
	 * @return status - Boolean.
	 * @throws SQLException Exception thrown based on SQL syntax.
	 */
	Boolean checkIfInitialSetup() throws SQLException, ApplicationErrorException;

	/**
	 * This method invokes the DAO of the User entity and serves the create user function.
	 *
	 * @param user User to be created.
	 * @return User - Created user.
	 * @throws UniqueConstraintException Custom Exception to convey Unique constraint Violation in SQL
	 *                                   table
	 * @throws SQLException              Exception thrown based on SQL syntax.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	User createUser(User user) throws Exception;

	/**
	 * This method invokes the DAO of the User entity and serves the Login function.
	 *
	 * @param userName Username to be Logged in as.
	 * @param passWord Corresponding password.
	 * @return usertype - String.
	 * @throws SQLException              Exception thrown based on SQL syntax.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	User login(String userName, String passWord) throws SQLException, ApplicationErrorException;
}
