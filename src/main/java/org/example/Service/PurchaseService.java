package org.example.Service;

import org.example.DAO.ApplicationErrorException;
import org.example.DAO.PageCountOutOfBoundsException;
import org.example.Entity.Purchase;

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
	Purchase create(Purchase purchase) throws Exception;

	/**
	 * This method invokes the DAO of the Purchase entity and serves the Count function.
	 *
	 * @param attribute  Attribute to be counted.
	 * @param searchText SearchText to be counted.
	 * @return Count - Integer.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	Integer count(String attribute, String searchText) throws ApplicationErrorException, InvalidTemplateException;


	/**
	 * This method invokes the DAO of the Purchase entity and serves the List function.
	 *
	 * @param listattributes Key Value pairs (Map) of List function attributes.
	 * @return List - Purchase.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	List<Purchase> list(HashMap<String, String> listattributes) throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException;

	/**
	 * This method invokes the DAO of the Purchase entity and serves the Delete function.
	 *
	 * @param invoice Input invoice.
	 * @return resultCode - Integer.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	Integer delete(String invoice) throws ApplicationErrorException;
}