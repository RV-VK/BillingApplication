package Service;

import DAO.ApplicationErrorException;
import Entity.Store;
import java.sql.SQLException;

public interface StoreService {
  Store create(Store store) throws SQLException, ApplicationErrorException;

  int edit(Store store) throws SQLException, ApplicationErrorException;

  int delete(String adminPassword) throws ApplicationErrorException;
}
