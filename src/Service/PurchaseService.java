package Service;

import DAO.ApplicationErrorException;
import DAO.PageCountOutOfBoundsException;
import Entity.Purchase;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface PurchaseService {

	/**
	 * This method invokes the DAO of the Purchase Entity and serves the Purchase function.
	 *
	 * @param purchase Input Purchase.
	 * @return Purchase - Created Purchase
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 * @throws SQLException              Exception thrown based on SQL syntax.
	 */
	Purchase create(Purchase purchase) throws ApplicationErrorException, SQLException;

	/**
	 * This method invokes the DAO of the Purchase entity and serves the Count function.
	 *
	 * @param parameter Date of Purchase.
	 * @return Count - Integer.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	int count(String parameter) throws ApplicationErrorException;


	/**
	 * This method invokes the DAO of the Purchase entity and serves the List function.
	 *
	 * @param listattributes Key Value pairs (Map) of List function attributes.
	 * @return List - Purchase.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	List<Purchase> list(HashMap<String, String> listattributes)
			throws PageCountOutOfBoundsException, ApplicationErrorException;

	/**
	 * This method invokes the DAO of the Purchase entity and serves the Delete function.
	 *
	 * @param invoice Input invoice.
	 * @return resultCode - Integer.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	int delete(String invoice) throws ApplicationErrorException;
}
