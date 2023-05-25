package Service;

import DAO.ApplicationErrorException;
import DAO.PageCountOutOfBoundsException;
import Entity.Sales;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface SalesService {
	/**
	 * This method invokes the DAO of the Sales entity and serves the Sales.
	 *
	 * @param sales Input Sales entity.
	 * @return Sales - Created Sales entity.
	 * @throws ApplicationErrorException  Exception thrown due to Persistence problems.
	 * @throws SQLException               Exception thrown based on SQL syntax.
	 * @throws UnDividableEntityException Exception thrown when a Non-dividable unit in a Sales transaction is asked for a decimal quantity.
	 */
	Sales create(Sales sales) throws ApplicationErrorException, SQLException, UnDividableEntityException;

	/**
	 * This method invokes the DAO of the Sales entity and serves the Count function.
	 *
	 * @param parameter Date of Sales.
	 * @return Count - Integer.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	Integer count(String parameter) throws ApplicationErrorException;

	/**
	 * This method invokes the DAO of the Sales entity and serves the List function.
	 *
	 * @param listAttributes Key Value pairs (Map) of List function attributes.
	 * @return List - Sales.
	 * @throws ApplicationErrorException     Exception thrown due to Persistence problems.
	 * @throws PageCountOutOfBoundsException Custom Exception thrown when a non-existing page is given as input in Pageable List.
	 */
	List<Sales> list(HashMap<String, String> listAttributes) throws ApplicationErrorException, PageCountOutOfBoundsException;


	/**
	 * This method invokes the DAO of the Sales entity and serves the Delete function.
	 *
	 * @param id Input id to perform delete.
	 * @return resultCode - Integer.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	Integer delete(String id) throws ApplicationErrorException;
}
