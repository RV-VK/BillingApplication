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


  @Override
  public Unit create(Unit unit)
      throws SQLException, ApplicationErrorException, UniqueConstraintException, InvalidTemplateException {
    if (validate(unit)) return unitDAO.create(unit);
    else return null;
  }


  @Override
  public List<Unit> list() throws ApplicationErrorException {
    return unitDAO.list();
  }


  @Override
  public int edit(Unit unit)
      throws SQLException, ApplicationErrorException, UniqueConstraintException, InvalidTemplateException {
    if (!validate(unit)) {
      return 0;
    }
    return unitDAO.edit(unit);
  }


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
  private boolean validate(Unit unit) throws InvalidTemplateException {
    if (unit.getName()!=null&&!unit.getName().matches(NAME_REGEX))
      throw new InvalidTemplateException(">> Invalid Unit Name!!");
    if (unit.getCode()!=null&&!unit.getCode().matches(CODE_REGEX))
      throw new InvalidTemplateException(">> Invalid UnitCode!!");
    return true;
  }
}
