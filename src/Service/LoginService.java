package Service;

import DAO.ApplicationErrorException;
import DAO.UniqueConstraintException;
import Entity.User;

import java.sql.SQLException;

public interface LoginService {
	/**
	 * This method invokes the DAO of the Product entity and serves the Initial setup function.
	 *
	 * @return status - Boolean.
	 * @throws SQLException Exception thrown based on SQL syntax.
	 */
	Boolean checkIfInitialSetup() throws SQLException;

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
	User createUser(User user)
			throws UniqueConstraintException,
			SQLException,
			ApplicationErrorException,
			InvalidTemplateException;

	/**
	 * This method invokes the DAO of the User entity and serves the Login function.
	 *
	 * @param userName Username to be Logged in as.
	 * @param passWord Corresponding password.
	 * @return usertype - String.
	 * @throws SQLException              Exception thrown based on SQL syntax.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	String login(String userName, String passWord) throws SQLException, ApplicationErrorException;
}
