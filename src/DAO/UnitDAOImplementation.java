package DAO;

import DBConnection.DBHelper;
import Entity.Unit;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UnitDAOImplementation implements UnitDAO {
  private Connection unitConnection = DBHelper.getConnection();


  @Override
  public Unit create(Unit unit)
      throws SQLException, ApplicationErrorException, UniqueConstraintException {
    try {
      unitConnection.setAutoCommit(false);
      PreparedStatement unitCreateStatement =
          unitConnection.prepareStatement(
              "INSERT INTO UNIT(NAME,CODE,DESCRIPTION,ISDIVIDABLE) VALUES (?,?,?,?) RETURNING *");
      setParameters(unitCreateStatement,unit);
      ResultSet unitCreateResultSet = unitCreateStatement.executeQuery();
      unitCreateResultSet.next();
      Unit createdUnit =getUnitFromResultSet(unitCreateResultSet);
      unitConnection.commit();
      unitConnection.setAutoCommit(true);
      return createdUnit;
    } catch (SQLException e) {
      unitConnection.rollback();
      if (e.getSQLState().equals("23505"))
        throw new UniqueConstraintException(
            ">> Unit Code must be unique!!! the Unit code you have entered Already exists");
      throw new ApplicationErrorException(
          "Application has went into an Error!!!\n Please Try again");
    }
  }

  private PreparedStatement setParameters(PreparedStatement statement, Unit unit) throws SQLException {
    statement.setString(1, unit.getName());
    statement.setString(2, unit.getCode());
    statement.setString(3, unit.getDescription());
    statement.setBoolean(4, unit.getIsDividable());
    return statement;
  }
  
  private Unit getUnitFromResultSet(ResultSet resultSet) throws SQLException {
    boolean isDividable;
    if (resultSet.getString(5).equals("t")) {
      isDividable = true;
    } else {
      isDividable = false;
    }
    return new Unit(
            resultSet.getInt(1),
            resultSet.getString(2),
            resultSet.getString(3),
            resultSet.getString(4),
            isDividable);
  }


  @Override
  public List<Unit> list() throws ApplicationErrorException {
    List<Unit> unitList = new ArrayList<>();
    try {
      Statement listStatement = unitConnection.createStatement();
      ResultSet listResultSet = listStatement.executeQuery("SELECT * FROM UNIT ORDER BY CODE");
      while (listResultSet.next()) {
        unitList.add(getUnitFromResultSet(listResultSet));
      }
      return unitList;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ApplicationErrorException(
          "Application has went into an Error!!!\n Please Try again");
    }
  }


  @Override
  public int edit(Unit unit)
      throws ApplicationErrorException, SQLException, UniqueConstraintException {
    try {
      unitConnection.setAutoCommit(false);
      String editQuery =
          "UPDATE UNIT SET NAME= COALESCE(?,NAME),CODE= COALESCE(?,CODE), DESCRIPTION= COALESCE(?,DESCRIPTION),ISDIVIDABLE= COALESCE(?,ISDIVIDABLE) WHERE ID=?";
      PreparedStatement editStatement = unitConnection.prepareStatement(editQuery);
      setParameters(editStatement,unit);
      try {
        editStatement.setBoolean(4, unit.getIsDividable());
      } catch (Exception e) {
        editStatement.setNull(4, Types.BOOLEAN);
      }
      editStatement.setInt(5, unit.getId());
      if (editStatement.executeUpdate() > 0) {
        unitConnection.commit();
        unitConnection.setAutoCommit(true);
        return 1;
      } else return -1;
    } catch (SQLException e) {
      unitConnection.rollback();
      if (e.getSQLState().equals("23505")) {
        throw new UniqueConstraintException(
            ">>Unit Code must be unique!!!\n The unit code you have entered already exists!!!");
      } else {
        e.printStackTrace();
        throw new ApplicationErrorException(
            "Application has went into an Error!!!\n Please Try again");
      }
    }
  }


  @Override
  public int delete(String code) throws ApplicationErrorException {
    try {
      Statement deleteStatement = unitConnection.createStatement();
      if (!(deleteStatement.executeUpdate("DELETE FROM UNIT WHERE CODE='" + code + "'") > 0)) {
        return -1;
      } else {
        return 1;
      }
    } catch (Exception e) {
      throw new ApplicationErrorException(
          "Application has went into an Error!!!\n Please Try again");
    }
  }


  @Override
  public Unit findByCode(String code) throws ApplicationErrorException {
    try {
      Statement getUnitStatement = unitConnection.createStatement();
      ResultSet getUnitResultSet =
          getUnitStatement.executeQuery("SELECT * FROM UNIT WHERE CODE='" + code + "'");
      Unit unit = null;
      while (getUnitResultSet.next()) {
        unit =getUnitFromResultSet(getUnitResultSet);
      }
      return unit;
    } catch (Exception e) {
      throw new ApplicationErrorException(e.getMessage());
    }
  }
}
