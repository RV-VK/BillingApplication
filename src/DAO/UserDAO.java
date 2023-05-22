package DAO;

import Entity.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO {

  /**
   * This method Creates a User Entry in the User table
   * @param user Input Object
   * @return User Object - created
   * @throws SQLException Exception thrown based on SQL syntax.
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   * @throws UniqueConstraintException Custom Exception to convey Unique constraint Violation in SQL table.
   */
  User create(User user) throws SQLException, ApplicationErrorException, UniqueConstraintException;

  /**
   * This method counts the number od entries in the user table.
   *
   * @return count - Integer
   * @throws ApplicationErrorException Exception thrown due to persistence problems
   */

  int count() throws ApplicationErrorException;

  /**
   * This method Lists the records in the user table based on a given Search-text.
   *
   * @param searchText - The search-text that must be found.
   * @return List - Users
   * @throws ApplicationErrorException Exception thrown due to persistence problems
   */

  List list(String searchText) throws ApplicationErrorException;

  /**
   * This method lists the users in the user table based on the given searchable attribute
   * and its corresponding search-text formatted in a pageable manner.
   *
   * @param attribute  The attribute to be looked upon
   * @param searchText The search-text to be found.
   * @param pageLength The number of entries that must be listed.
   * @param offset The Page number that has to be listed.
   * @return List - Users
   * @throws ApplicationErrorException Exception thrown due to persistence problems
   */

  List list(String attribute, String searchText, int pageLength, int offset)
      throws ApplicationErrorException;

  /**
   * This method updates the attributes of the User entry in the user table.
   *
   * @param user  The updated User Entry.
   * @return status - Boolean
   * @throws SQLException Exception thrown based on SQL syntax.
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   * @throws UniqueConstraintException Custom Exception to convey Unique constraint Violation in SQL table
   */
  boolean edit(User user) throws SQLException, ApplicationErrorException, UniqueConstraintException;

  /**
   * This method deleted an entry in the User table based on the given parameter.
   *
   * @param parameter Input parameter based on which the row is selected to delete.
   * @return resultCode - Integer
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */

  int delete(String parameter) throws ApplicationErrorException;

  /**
   * This method acts as a helper method to check whether any entry is made on User table so that the control
   * of the program is directed as Initial setup or Login.
   *
   * @return status - Boolean
   * @throws SQLException Exception thrown due to Persistence problems.
   */

  boolean checkIfInitialSetup() throws SQLException;

  /**
   * This method verifies whether the input username and password matches in the user table to enable login for the users.
   *
   * @param userName Unique entry username of the user
   * @param passWord Password string of the user
   * @return String - Usertype or null
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */
  String login(String userName, String passWord) throws SQLException, ApplicationErrorException;
}
