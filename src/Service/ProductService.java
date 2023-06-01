package Service;

import DAO.*;
import Entity.Product;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface ProductService {

	/**
	 * This method invokes the ProductMapper object and serves the Product creation.
	 *
	 * @param product Input product
	 * @return Product
	 * @throws SQLException              Exception thrown based on SQL syntax.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 * @throws UniqueConstraintException Custom Exception to convey Unique constraint Violation in SQL
	 *                                   table.
	 */
	Product create(Product product) throws Exception;

	/**
	 * This method invokes the ProductMapper object and serves the Count function.
	 *
	 * @return count - Integer
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	Integer count(String attribute, String searchText) throws ApplicationErrorException;


	/**
	 * This method invokes the ProductMapper object and serves the List function.
	 *
	 * @param listattributes Key Value Pairs(Map) of List function attributes.
	 * @return List - Products
	 * @throws ApplicationErrorException     Exception thrown due to Persistence problems.
	 * @throws PageCountOutOfBoundsException Custom Exception thrown when a non-existing page is given as input in Pageable List.
	 */
	List<Product> list(HashMap<String, String> listattributes) throws ApplicationErrorException, PageCountOutOfBoundsException;

	/**
	 * This method invokes the Product DAO object and serves the edit function.
	 *
	 * @param product Edited Product
	 * @return Product - Resulted Product after Edit.
	 * @throws SQLException               Exception thrown based on SQL syntax.
	 * @throws ApplicationErrorException  Exception thrown due to Persistence problems.
	 * @throws UniqueConstraintException  Custom Exception to convey Unique constraint Violation in SQL
	 *                                    table.
	 * @throws UnitCodeViolationException Custom Exception to convey Foreign Key Violation in Product
	 *                                    table.
	 */
	Product edit(Product product) throws Exception;

	/**
	 * This method invokes the ProductMapper object and serves the delete function
	 *
	 * @param parameter Input parameter for delete function.(Code/Id)
	 * @return resultCode - Integer
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	Integer delete(String parameter) throws ApplicationErrorException;

}
