package DAO;

import Entity.Product;
import java.sql.SQLException;
import java.util.List;

public interface ProductDAO {

  /**
   * This method creates an entry in the Product table
   *
   * @param product Input product
   * @return Product - Created product.
   * @throws SQLException Exception thrown based on SQL syntax.
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   * @throws UniqueConstraintException Custom Exception to convey Unique constraint Violation in SQL
   *     table
   * @throws UnitCodeViolationException Custom Exception to convey Foreign Key Violation in Product
   *     table.
   */
  Product create(Product product)
      throws SQLException,
          ApplicationErrorException,
          UniqueConstraintException,
          UnitCodeViolationException;

  /**
   * This Method returns the number of entries in the Product table.
   *
   * @return count
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */
  int count() throws ApplicationErrorException;

  /**
   * This method lists the products in the product table based on the given searchable attribute and
   * its corresponding search-text formatted in a pageable manner.
   *
   * @param attribute The attribute to be looked upon
   * @param searchText The search-text to be found.
   * @param pageLength The number of entries that must be listed.
   * @param offset The Page number that has to be listed.
   * @return List - Products
   * @throws ApplicationErrorException Exception thrown due to persistence problems
   * @throws PageCountOutOfBoundsException Exception thrown in a pageable list function if a
   *     non-existing page is prompted.
   */
  List list(String attribute, String searchText, int pageLength, int offset)
      throws ApplicationErrorException, PageCountOutOfBoundsException;

  /**
   * This method Lists the products in the product table based on the given search-text.
   *
   * @param searchText The search-text that must be found.
   * @return List - Products
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */
  List list(String searchText) throws ApplicationErrorException;

  /**
   * This method updates the attributes of the product entry in the Product table
   *
   * @param product The Updated Product entry
   * @return status - Boolean
   * @throws SQLException Exception thrown based on SQL syntax.
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   * @throws UniqueConstraintException Custom Exception to convey Unique constraint Violation in SQL
   *     table
   * @throws UnitCodeViolationException Custom Exception to convey Foreign Key Violation in Product
   *     table.
   */
  boolean edit(Product product)
      throws SQLException,
          ApplicationErrorException,
          UniqueConstraintException,
          UnitCodeViolationException;

  /**
   * This method deletes an entry in the Product table based on the given parameter.
   *
   * @param parameter Input parameter based on which the row is selected to delete.
   * @return resultCode - Integer
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */
  int delete(String parameter) throws ApplicationErrorException;

  /**
   * This method finds the Product by its product code attribute.
   *
   * @param code Input product code.
   * @return Product
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */
  Product findByCode(String code) throws ApplicationErrorException;

  /**
   * This method updates the Stock column of Product based on given product code.
   *
   * @param code Product code
   * @param stock Stock to be updated
   * @return updated Stock - Float
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */
  int updateStock(String code, float stock) throws ApplicationErrorException;

  /**
   * This method updates the Price column of Product based on given product code.
   *
   * @param code Product code
   * @param price Price to be updated
   * @return updated Price - Double
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */
  int updatePrice(String code, double price) throws ApplicationErrorException;
}
