package DAO;

import Entity.Sales;

import java.sql.SQLException;
import java.util.List;

public interface SalesDAO {

  /**
   * This method is a composite function that creates an entry in both Sales and Sales-items table.
   *
   * @param sales Input Sales.
   * @return sales - Created Sales.
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   * @throws SQLException Exception thrown based on SQL syntax.
   */
  Sales create(Sales sales) throws ApplicationErrorException, SQLException;


  /**
   * This method counts the number of entries from the sales table based on date parameter.
   *
   * @param parameter Date of Sale.
   * @return count - Integer.
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */
  int count(String parameter) throws ApplicationErrorException;

  /**
   * This method lists the Sales and SalesItem entries based on the given searchable attribute and
   * its corresponding search-text formatted in pageable manner.
   *
   * @param attribute The attribute to be looked upon.
   * @param searchText The search-text to be found.
   * @param pageLength The number of entries that must be listed.
   * @param offset The Page number to be listed.
   * @return List - Sales.
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */
  List<Sales> list(String attribute, String searchText, int pageLength, int offset)
      throws ApplicationErrorException;

  /**
   * This method lists the Entries from the Sales and SalesItem table based on the given search-text.
   *
   * @param searchText The search-text to be found.
   * @return List - Sales.
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */
  List<Sales> list(String searchText) throws ApplicationErrorException;

  /**
   * This method deletes an entry in the Sales table and the corresponding entries in the Sales items table.
   *
   * @param id Input id to perform delete.
   * @return resultCode - Integer.
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */
  int delete(int id) throws ApplicationErrorException;
}
