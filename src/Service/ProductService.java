package Service;

import DAO.*;
import Entity.Product;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface ProductService {

  /**
   * This method invokes the ProductDAO object and serves the Product creation.
   *
   * @param product Input product
   * @return Product
   * @throws SQLException Exception thrown based on SQL syntax.
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   * @throws UniqueConstraintException Custom Exception to convey Unique constraint Violation in SQL table.
   */
  Product create(Product product)
      throws SQLException,
          ApplicationErrorException,
          UniqueConstraintException,
          UnitCodeViolationException;

  /**
   * This method invokes the ProductDAO object and serves the Count function.
   * @return count - Integer
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */
  int count() throws ApplicationErrorException;


  /**
   * This method invokes the ProductDAO object and serves the List function.
   * @param listattributes Key Value Pairs(Map) of List function attributes.
   * @return List - Products
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   * @throws PageCountOutOfBoundsException Custom Exception thrown when a non-existing page is given as input in Pageable List.
   */
  List<Product> list(HashMap<String, String> listattributes)
      throws ApplicationErrorException, PageCountOutOfBoundsException;

  /**
   * This method invokes the Product DAO object and serves the edit function.
   * @param product Edited Product
   * @return resultCode - Integer
   * @throws SQLException Exception thrown based on SQL syntax.
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   * @throws UniqueConstraintException Custom Exception to convey Unique constraint Violation in SQL table.
   * @throws UnitCodeViolationException Custom Exception to convey Foreign Key Violation in Product table.
   */
  int edit(Product product)
      throws SQLException,
          ApplicationErrorException,
          UniqueConstraintException,
          UnitCodeViolationException;

  /**
   *This method invokes the ProductDAO object and serves the delete function
   *
   * @param parameter Input parameter for delete function.(Code/Id)
   * @return resultCode - Integer
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */
  int delete(String parameter) throws ApplicationErrorException;

  /**This method invokes the ProductDAO and serves the stock Update function.
   *
   * @param code Product Code
   * @param stock updated Stock
   * @return statusCode - Integer
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */
  int updateStock(String code,String stock) throws ApplicationErrorException;

  /**This method invokes the ProductDAO and serves the Price Update function.
   *
   * @param code Product code
   * @param price updated Price
   * @return statusCode - Integer
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */
  int updatePrice(String code,String price) throws ApplicationErrorException;
}
