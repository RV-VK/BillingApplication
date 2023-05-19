package DAO;

import DBConnection.DBHelper;
import Entity.Store;
import java.sql.*;

public class StoreDAOImplementation implements StoreDAO {
  private Connection storeConnection = DBHelper.getConnection();


  /**
   * This method creates an Entry in the Store table.
   * @param store Input Store entity.
   * @return Store - Created store.
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   * @throws SQLException Exception thrown based on SQL syntax.
   */
  @Override
  public Store create(Store store) throws ApplicationErrorException, SQLException {
    try {
      storeConnection.setAutoCommit(false);
      PreparedStatement unitCreateStatement =
          storeConnection.prepareStatement(
              "INSERT INTO STORE(NAME,PHONENUMBER,ADDRESS,GSTNUMBER) VALUES (?,?,?,?) RETURNING *");
      setParameters(unitCreateStatement,store);
      ResultSet storeCreateResultSet = unitCreateStatement.executeQuery();
      storeCreateResultSet.next();
      Store createdStore =getStoreFromResultSet(storeCreateResultSet);
      storeConnection.commit();
      storeConnection.setAutoCommit(true);
      return createdStore;
    } catch (SQLException e) {
      storeConnection.rollback();
      if(e.getSQLState().equals("23514"))
        return null;
      else
        throw new ApplicationErrorException(
          e.getMessage());
    }
  }

  private PreparedStatement setParameters(PreparedStatement statement,Store store) throws SQLException {
    statement.setString(1, store.getName());
    statement.setLong(2, store.getPhoneNumber());
    statement.setString(3, store.getAddress());
    statement.setString(4, store.getGstCode());
    return statement;
  }
  private Store getStoreFromResultSet(ResultSet resultSet) throws SQLException {
    return  new Store(
            resultSet.getString(2),
            resultSet.getLong(3),
            resultSet.getString(4),
            resultSet.getString(5));
  }

  /**
   * This method updates the attributes of the Store entry in the Store table.
   * @param store Updated Store entity.
   * @return statusCode - Integer.
   * @throws SQLException Exception thrown based on SQL syntax.
   * @throws ApplicationErrorException  Exception thrown due to Persistence problems.
   */
  @Override
  public int edit(Store store) throws SQLException, ApplicationErrorException {
    storeConnection.setAutoCommit(false);
    try {
      String editQuery =
          "UPDATE STORE SET NAME= COALESCE(?,NAME),PHONENUMBER= COALESCE(?,PHONENUMBER),ADDRESS= COALESCE(?,ADDRESS),GSTNUMBER=COALESCE(?,GSTNUMBER)";
      PreparedStatement editStatement = storeConnection.prepareStatement(editQuery);
      setParameters(editStatement,store);
      if (store.getPhoneNumber() == 0) {
        editStatement.setNull(2, Types.BIGINT);
      } else {
        editStatement.setLong(2, store.getPhoneNumber());
      }
      if (editStatement.executeUpdate() > 0) {
        storeConnection.commit();
        storeConnection.setAutoCommit(true);
        return 1;
      } else {
        return -1;
      }
    } catch (Exception e) {
      storeConnection.rollback();
      System.out.println(e.getMessage());
      throw new ApplicationErrorException(
          "Application has went into an Error!!!\n Please Try again");
    }
  }

  /**
   * This method deleted the store Entry in the Store table.
   *
   * @param adminPassword Password String to allow to delete store.
   * @return statusCode - Integer.
   * @throws ApplicationErrorException  Exception thrown due to Persistence problems.
   */
  @Override
  public int delete(String adminPassword) throws ApplicationErrorException {
    try {
      Statement storeExistenceCheckStatement = storeConnection.createStatement();
      ResultSet storeResultSet = storeExistenceCheckStatement.executeQuery("SELECT * FROM STORE");
      if (storeResultSet.next()) {
        Statement passwordCheckStatement = storeConnection.createStatement();
        ResultSet adminPasswordResultSet =
            passwordCheckStatement.executeQuery(
                "SELECT PASSWORD FROM USERS WHERE USERTYPE='Admin'");
        if (adminPasswordResultSet.next()) {
          String originalPassword = adminPasswordResultSet.getString(1);
          if (originalPassword.equals(adminPassword)) {
            Statement deleteStatement = storeConnection.createStatement();
            deleteStatement.execute("DELETE FROM STORE");
            return 1;
          } else {
            return -1;
          }
        }
        else {
          return -1;
        }
      } else {
        return 0;
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new ApplicationErrorException(
          "Application has went into an Error!!!\n Please Try again");
    }
  }
}
