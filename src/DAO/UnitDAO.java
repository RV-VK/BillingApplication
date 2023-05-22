package DAO;

import Entity.Unit;
import java.sql.SQLException;
import java.util.List;

public interface UnitDAO {

  /**
   * This method creates an Entry in the Unit table.
   *
   * @param unit Input Unit
   * @return Unit - Created Unit.
   * @throws SQLException Exception thrown based on SQL syntax.
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   * @throws UniqueConstraintException  Custom Exception to convey Unique constraint Violation in SQL table
   */
  Unit create(Unit unit) throws SQLException, ApplicationErrorException, UniqueConstraintException;

  /**
   * This method Lists all the entries of the Unit table.
   *
   * @return List - Units.
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */

  List<Unit> list() throws ApplicationErrorException;

  /**
   * This method updates the attributes of the Unit entry in the Unit table.
   *
   * @param unit Updated Unit.
   * @return resultCode - Integer.
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   * @throws SQLException Exception thrown based on SQL syntax.
   * @throws UniqueConstraintException Custom Exception to convey Unique constraint Violation in SQL table
   */

  int edit(Unit unit) throws ApplicationErrorException, SQLException, UniqueConstraintException;

  /**
   * This method deletes an entry in the Unit table.
   *
   * @param code Input attribute to perform delete.
   * @return resultCode - Integer
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */

  int delete(String code) throws ApplicationErrorException;

  /**
   * This method returns a Unit entry based on its attribute code.
   *
   * @param code Input code
   * @return Unit
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */
  Unit findByCode(String code) throws ApplicationErrorException;
}
