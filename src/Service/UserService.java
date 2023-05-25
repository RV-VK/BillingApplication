package Service;

import DAO.ApplicationErrorException;
import DAO.PageCountOutOfBoundsException;
import DAO.UniqueConstraintException;
import Entity.User;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface UserService {

	/**
	 * This method invokes the DAO of the User entity and serves the create function
	 *
	 * @param user Input User
	 * @return User - Entered user
	 * @throws SQLException              Exception thrown based on SQL syntax.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 * @throws UniqueConstraintException Custom Exception to convey Unique constraint Violation in SQL
	 *                                   table
	 */
	User create(User user) throws SQLException, ApplicationErrorException, UniqueConstraintException, InvalidTemplateException;

	/**
	 * This method invokes the DAO of the User entity and serves the count function.
	 *
	 * @return count - Integer
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	Integer count() throws ApplicationErrorException;

	/**
	 * This method invokes the DAO of the User entity and serves the List function.
	 *
	 * @param listattributes Key Value pairs (map) of List function attributes.
	 * @return List -   Users
	 * @throws ApplicationErrorException     Exception thrown due to Persistence problems.
	 * @throws PageCountOutOfBoundsException Custom Exception thrown when a non-existing page is given as input in Pageable List.
	 */
	List<User> list(HashMap<String, String> listattributes) throws ApplicationErrorException, PageCountOutOfBoundsException;

	/**
	 * This method invokes the DAO of the User entity and serves the Edit function.
	 *
	 * @param user Edited user.
	 * @return User - Resulted User after Edit.
	 * @throws SQLException              Exception thrown based on SQL syntax.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 * @throws UniqueConstraintException Custom Exception to convey Unique constraint Violation in SQL
	 *                                   table.
	 */
	User edit(User user) throws SQLException, ApplicationErrorException, UniqueConstraintException, InvalidTemplateException;

	/**
	 * This method invokes the DAO of the User entity and serves the Delete function.
	 *
	 * @param username Input username perform to delete.
	 * @return resultCode - Integer.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	Integer delete(String username) throws ApplicationErrorException;
}
