package Service;

import DAO.ApplicationErrorException;
import DAO.PageCountOutOfBoundsException;
import Entity.Sales;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface SalesService {
  Sales create(Sales sales)
      throws ApplicationErrorException, SQLException, UnDividableEntityException;

  int count(String parameter) throws ApplicationErrorException;

  List<Sales> list(HashMap<String, String> listAttributes)
      throws ApplicationErrorException, PageCountOutOfBoundsException;

  int delete(String id) throws ApplicationErrorException;
}
