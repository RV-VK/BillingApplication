package Service;

import DAO.ApplicationErrorException;
import DAO.PageCountOutOfBoundsException;
import Entity.Purchase;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface PurchaseService {
  Purchase create(Purchase purchase) throws ApplicationErrorException, SQLException;

  int count(String parameter) throws ApplicationErrorException;

  List<Purchase> list(HashMap<String, String> listattributes)
      throws PageCountOutOfBoundsException, ApplicationErrorException;

  int delete(String invoice) throws ApplicationErrorException;
}
