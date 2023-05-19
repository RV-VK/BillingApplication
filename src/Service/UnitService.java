package Service;

import DAO.ApplicationErrorException;
import DAO.UniqueConstraintException;
import Entity.Unit;
import java.sql.SQLException;
import java.util.List;

public interface UnitService {
  Unit create(Unit unit)
      throws SQLException, ApplicationErrorException, UniqueConstraintException;

  List<Unit> list() throws ApplicationErrorException;

  int edit(Unit unit)
      throws SQLException, ApplicationErrorException, UniqueConstraintException;

  int delete(String code) throws ApplicationErrorException;
}
