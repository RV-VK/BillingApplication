package DAO;

import DBConnection.DBHelper;
import Entity.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImplementation implements UserDAO {
  private Connection userConnection = DBHelper.getConnection();
  private List<User> userList = new ArrayList<>();


  /**
   * This method Creates a User Entry in the User table
   * @param user Input Object
   * @return User Object - created
   * @throws SQLException Exception thrown based on SQL syntax.
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   * @throws UniqueConstraintException Custom Exception to convey Unique constraint Violation in SQL table.
   */
  @Override
  public User create(User user)
      throws SQLException, ApplicationErrorException, UniqueConstraintException {
    try {
      userConnection.setAutoCommit(false);
      PreparedStatement userCreateStatement =
          userConnection.prepareStatement(
              "INSERT INTO USERS(USERNAME,USERTYPE,PASSWORD,FIRSTNAME,LASTNAME,PHONENUMBER) VALUES (?,?,?,?,?,?) RETURNING *");
      userCreateStatement.setString(1, user.getUserName());
      userCreateStatement.setString(2, user.getUserType());
      userCreateStatement.setString(3, user.getPassWord());
      userCreateStatement.setString(4, user.getFirstName());
      userCreateStatement.setString(5, user.getLastName());
      userCreateStatement.setLong(6, user.getPhoneNumber());
      ResultSet userCreateResultSet = userCreateStatement.executeQuery();
      userCreateResultSet.next();
      User createdUser =
          new User(
              userCreateResultSet.getInt(1),
              userCreateResultSet.getString(3),
              userCreateResultSet.getString(2),
              userCreateResultSet.getString(4),
              userCreateResultSet.getString(5),
              userCreateResultSet.getString(6),
              userCreateResultSet.getLong(7));
      userConnection.commit();
      userConnection.setAutoCommit(true);
      return createdUser;
    } catch (SQLException e) {
      userConnection.rollback();
      if (e.getSQLState().equals("23505")) {
        throw new UniqueConstraintException(
            ">> UserName must be unique!! The username you have entered already exists!!");
      }
      throw new ApplicationErrorException(
          "Application has went into an Error!!!\n Please Try again");
    }
  }

  /**
   * This method counts the number od entries in the user table.
   *
   * @return count - Integer
   * @throws ApplicationErrorException Exception thrown due to persistence problems
   */
  @Override
  public int count() throws ApplicationErrorException {
    try {
      Statement countStatement = userConnection.createStatement();
      ResultSet countResultSet = countStatement.executeQuery("SELECT COUNT(ID) FROM USERS");
      int count = 0;
      while (countResultSet.next()) {
        count = countResultSet.getInt(1);
      }
      return count;
    } catch (Exception e) {
      throw new ApplicationErrorException(
          "Application has went into an Error!!!\n Please Try again");
    }
  }

  /**
   * This method Lists the records in the user table based on a given Search-text.
   *
   * @param searchText - The search-text that must be found.
   * @return List - Users
   * @throws ApplicationErrorException Exception thrown due to persistence problems
   */
  @Override
  public List<User> list(String searchText) throws ApplicationErrorException {
    try {
      Statement listStatement = userConnection.createStatement();
      String listQuery =
          "SELECT * FROM USERS WHERE USERTYPE ILIKE '"
              + searchText
              + "' OR USERNAME ILIKE '"
              + searchText
              + "' OR FIRSTNAME ILIKE '"
              + searchText
              + "' OR LASTNAME ILIKE '"
              + searchText
              + "' OR PASSWORD ILIKE '"
              + searchText
              + "' OR CAST(PHONENUMBER AS TEXT) ILIKE '"
              + searchText
              + "' OR CAST(ID AS TEXT) ILIKE '"
              + searchText
              + "'";
      ResultSet listresultSet = listStatement.executeQuery(listQuery);
      return listHelper(listresultSet);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      throw new ApplicationErrorException(
          "Application has went into an Error!!!\n Please Try again");
    }
  }

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
  @Override
  public List list(String attribute, String searchText, int pageLength, int offset)
      throws ApplicationErrorException {
    int count;
    try {
      String EntryCount =
          "SELECT COUNT(*) OVER() FROM USERS WHERE "
              + attribute
              + "= COALESCE(?,"
              + attribute
              + ")"
              + " ORDER BY ID";
      String listQuery =
          "SELECT * FROM USERS WHERE "
              + attribute
              + "= COALESCE(?,"
              + attribute
              + ")"
              + " ORDER BY ID LIMIT "
              + pageLength
              + "  OFFSET "
              + offset;
      PreparedStatement countStatement = userConnection.prepareStatement(EntryCount);
      PreparedStatement listStatement =
          userConnection.prepareStatement(
              listQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
      if (attribute.equals("id") && searchText == null) {
        listStatement.setNull(1, Types.INTEGER);
        countStatement.setNull(1, Types.INTEGER);
      } else if (attribute.equals("id") || attribute.equals("phonenumber")) {
        listStatement.setLong(1, Long.parseLong(searchText));
        countStatement.setLong(1, Long.parseLong(searchText));
      } else {
        listStatement.setString(1, searchText);
        countStatement.setString(1, searchText);
      }
      ResultSet countResultSet = countStatement.executeQuery();
      countResultSet.next();
      count = countResultSet.getInt(1);
      if (count <= offset) {
        int pageCount;
        if (count % pageLength == 0)
          pageCount=count/pageLength;
        else
          pageCount=(count/pageLength)+1;
        throw new PageCountOutOfBoundsException(
            ">> Requested Page doesnt Exist!!\n>> Existing Pagecount with given pagination "
                + pageCount);
        }
      ResultSet listResultSet = listStatement.executeQuery();
      return listHelper(listResultSet);
    } catch (Exception e) {
      throw new ApplicationErrorException(e.getMessage());
    }
  }

  /**
   * This method serves the ListDAO function.
   * @param resultSet ListQuery results.
   * @return List - Users
   * @throws SQLException Exception thrown based on SQL syntax.
   */
  private List<User> listHelper(ResultSet resultSet) throws SQLException {
    while (resultSet.next()) {
      User listedUser =
          new User(
              resultSet.getInt(1),
              resultSet.getString(3),
              resultSet.getString(2),
              resultSet.getString(4),
              resultSet.getString(5),
              resultSet.getString(6),
              resultSet.getLong(7));
      userList.add(listedUser);
    }
    return userList;
  }

  /**
   * This method updates the attributes of the User entry in the user table.
   *
   * @param user  The updated User Entry.
   * @return status - Boolean
   * @throws SQLException Exception thrown based on SQL syntax.
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   * @throws UniqueConstraintException Custom Exception to convey Unique constraint Violation in SQL table
   */
  @Override
  public boolean edit(User user)
      throws SQLException, ApplicationErrorException, UniqueConstraintException {
    Connection editConnection = DBHelper.getConnection();
    try {
      editConnection.setAutoCommit(false);
      String editQuery =
          "UPDATE USERS SET USERNAME= COALESCE(?,USERNAME),USERTYPE= COALESCE(?,USERTYPE),PASSWORD= COALESCE(?,PASSWORD),FIRSTNAME= COALESCE(?,FIRSTNAME),LASTNAME= COALESCE(?,LASTNAME),PHONENUMBER=COALESCE(?,PHONENUMBER) WHERE ID=?";
      PreparedStatement editStatement = editConnection.prepareStatement(editQuery);
      editStatement.setString(1, user.getUserName());
      editStatement.setString(2, user.getUserType());
      editStatement.setString(3, user.getPassWord());
      editStatement.setString(4, user.getFirstName());
      editStatement.setString(5, user.getLastName());
      if (user.getPhoneNumber() == 0) {
        editStatement.setNull(6, Types.BIGINT);
      } else {
        editStatement.setLong(6, user.getPhoneNumber());
      }
      editStatement.setInt(7, user.getId());
      if (editStatement.executeUpdate() > 0) {
        editConnection.commit();
        editConnection.setAutoCommit(true);
        return true;
      }
      return false;
    } catch (SQLException e) {
      if (e.getSQLState().equals("23505")) {
        throw new UniqueConstraintException(
            ">> Username must be unique!!!\n>>The username you have entered already exists!!!");
      }
      editConnection.rollback();
      throw new ApplicationErrorException(
          "Application has went into an Error!!!\n Please Try again");
    }
  }

  /**
   * This method deleted an entry in the User table based on the given parameter.
   *
   * @param username Input parameter based on which the row is selected to delete.
   * @return resultCode - Integer
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */
  @Override
  public int delete(String username) throws ApplicationErrorException {
    try {
      Statement deleteStatement = userConnection.createStatement();
      if (deleteStatement.executeUpdate("DELETE FROM USERS WHERE USERNAME='" + username + "'")
          > 0) {
        return 1;
      } else {
        return -1;
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new ApplicationErrorException(
          "Application has went into an Error!!!\n Please Try again");
    }
  }

  /**
   * This method acts as a helper method to check whether any entry is made on User table so that the control
   * of the program is directed as Initial setup or Login.
   *
   * @return status - Boolean
   * @throws SQLException Exception thrown due to Persistence problems.
   */
  public boolean checkIfInitialSetup() throws SQLException {
    ResultSet resultSet =
        userConnection.createStatement().executeQuery("SELECT COUNT(ID) FROM USERS WHERE USERTYPE='Admin'");
    resultSet.next();
    return resultSet.getInt(1) == 0;
  }

  /**
   * This method verifies whether the input username and password matches in the user table to enable login for the users.
   *
   * @param userName Unique entry username of the user
   * @param passWord Password string of the user
   * @return String - Usertype or null
   * @throws SQLException Exception thrown based on SQL syntax.
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */
  @Override
  public String login(String userName, String passWord)
      throws SQLException, ApplicationErrorException {
    try{
    ResultSet resultSet = userConnection
            .createStatement()
            .executeQuery("SELECT PASSWORD,USERTYPE FROM USERS WHERE USERNAME='"+userName+"'");
    if (resultSet.next()) {
      if (resultSet.getString(1).equals(passWord)) return resultSet.getString(2);
      else return null;
    } else return null;
    }
    catch(SQLException e)
    {
      throw new ApplicationErrorException(e.getMessage());
    }
  }
}
