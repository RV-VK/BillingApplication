package Service;

import DAO.*;
import Entity.User;
import java.sql.SQLException;
import java.util.*;

public class UserServiceImplementation implements UserService {

  private UserDAO userDAO = new UserDAOImplementation();
  private final String NAME_REGEX = "^[a-zA-Z\\s]{1,30}$";
  private final String PASSWORD_REGEX = "^[a-zA-Z0-9]{8,30}$";
  private final List<String> userTypeList = Arrays.asList("Sales", "Purchase", "Admin");
  private final String PHONE_NUMBER_REGEX = "^[6789]\\d{9}$";

  /**
   * This method invokes the DAO of the User entity and serves the create function
   *
   * @param user Input User
   * @return User - Entered user
   * @throws SQLException Exception thrown based on SQL syntax.
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   * @throws UniqueConstraintException Custom Exception to convey Unique constraint Violation in SQL table
   */
  @Override
  public User create(User user)
      throws SQLException, ApplicationErrorException, UniqueConstraintException {
    if (validate(user)) return userDAO.create(user);
    else return null;
  }

  /**
   * This method invokes the DAO of the User entity and serves the count function.
   *
   * @return count - Integer
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */
  @Override
  public int count() throws ApplicationErrorException {
    return userDAO.count();
  }

  /**
   * This method invokes the DAO of the User entity and serves the List function.
   *
   * @param listattributes Key Value pairs (map) of List function attributes.
   * @return List -   Users
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   * @throws PageCountOutOfBoundsException Custom Exception thrown when a non-existing page is given as input in Pageable List.
   */
  @Override
  public List list(HashMap<String, String> listattributes)
      throws ApplicationErrorException, PageCountOutOfBoundsException {
    List<User> userList;
    if (Collections.frequency(listattributes.values(), null) == 0
        || Collections.frequency(listattributes.values(), null) == 1) {
      int pageLength = Integer.parseInt(listattributes.get("Pagelength"));
      int pageNumber = Integer.parseInt(listattributes.get("Pagenumber"));
      int offset = (pageLength * pageNumber) - pageLength;
      userList =
          userDAO.list(
              listattributes.get("Attribute"),
              listattributes.get("Searchtext"),
              pageLength,
              offset);
      return userList;
    } else if (Collections.frequency(listattributes.values(), null) == listattributes.size() - 1
        && listattributes.get("Searchtext") != null) {
      userList = userDAO.list(listattributes.get("Searchtext"));
      return userList;
    }
    return null;
  }

  /**
   * This method invokes the DAO of the User entity and serves the Edit function.
   * @param user Edited user.
   * @return resultCode - Integer.
   * @throws SQLException Exception thrown based on SQL syntax.
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   * @throws UniqueConstraintException Custom Exception to convey Unique constraint Violation in SQL table.
   */
  @Override
  public int edit(User user)
      throws SQLException, ApplicationErrorException, UniqueConstraintException {
    if (!validate(user)) return 0;
    boolean status = userDAO.edit(user);
    if (status) {
      return 1;
    } else {
      return -1;
    }
  }

  /**
   * This method invokes the DAO of the User entity and serves the Delete function.
   *
   * @param username Input username perform to delete.
   * @return resultCode - Integer.
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */
  @Override
  public int delete(String username) throws ApplicationErrorException {
    return userDAO.delete(username);
  }

  /**
   * This method validates the user attributes.
   *
   * @param user user to be validated.
   * @return status - Boolean.
   */
  private boolean validate(User user) {
    if ((user.getUserName()!=null&&!user.getUserName().matches(NAME_REGEX))
        || (user.getFirstName()!=null&&!user.getFirstName().matches(NAME_REGEX))
        || (user.getLastName()!=null&&!user.getLastName().matches(NAME_REGEX))
        || (user.getPassWord()!=null&&!user.getPassWord().matches(PASSWORD_REGEX))
        || (user.getPhoneNumber()!=0&&!String.valueOf(user.getPhoneNumber()).matches(PHONE_NUMBER_REGEX))
        || (user.getUserType()!=null&&!userTypeList.contains(user.getUserType()))) return false;
    else return true;
  }
}
