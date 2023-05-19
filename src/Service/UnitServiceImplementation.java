package Service;

import DAO.ApplicationErrorException;
import DAO.UniqueConstraintException;
import DAO.UnitDAO;
import DAO.UnitDAOImplementation;
import Entity.Unit;

import java.sql.SQLException;
import java.util.List;

public class UnitServiceImplementation implements UnitService {
  private UnitDAO unitDAO = new UnitDAOImplementation();
  private final String NAME_REGEX = "^[a-zA-Z\\s]{1,30}$";
  private final String CODE_REGEX = "^[a-zA-Z]{1,4}$";

  /**
   * This method invokes the DAO of the Unit entity and serves the Create function.
   * @param unit Input Unit.
   * @return Unit - Created unit.
   * @throws SQLException Exception thrown based on SQL syntax.
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   * @throws UniqueConstraintException Custom Exception to convey Unique constraint Violation in SQL table.
   */
  @Override
  public Unit create(Unit unit)
      throws SQLException, ApplicationErrorException, UniqueConstraintException {
    if (validate(unit)) return unitDAO.create(unit);
    else return null;
  }

  /**
   * This method invokes the DAO of the Unit entity and serves the List function.
   *
   * @return List - Units.
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */
  @Override
  public List<Unit> list() throws ApplicationErrorException {
    return unitDAO.list();
  }

  /**
   *This method invokes the DAO of the Unit entity and serves the Edit function.
   *
   * @param unit Edited Unit.
   * @return resultCode - Integer.
   * @throws SQLException Exception thrown based on SQL syntax.
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   * @throws UniqueConstraintException Custom Exception to convey Unique constraint Violation in SQL table.
   */
  @Override
  public int edit(Unit unit)
      throws SQLException, ApplicationErrorException, UniqueConstraintException {
    if (!validate(unit)) {
      return 0;
    }
    return unitDAO.edit(unit);
  }

  /**
   * This method invokes the DAO of the Unit entity and serves the Delete function.
   *
   * @param code Input code to perform delete.
   * @return resultCode - Integer.
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */
  @Override
  public int delete(String code) throws ApplicationErrorException {
    UnitDAO unitDeleteDAO = new UnitDAOImplementation();
    return unitDeleteDAO.delete(code);
  }


  /**
   * This method Validates the unit attributes.
   * @param unit Unit to be validated
   * @return status - Boolean.
   */
  private boolean validate(Unit unit) {
    if ((unit.getName()!=null&&!unit.getName().matches(NAME_REGEX)) || (unit.getCode()!=null&&!unit.getCode().matches(CODE_REGEX))) return false;
    else return true;
  }
}
