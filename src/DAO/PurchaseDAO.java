package DAO;

import Entity.Purchase;

import java.sql.SQLException;
import java.util.List;

public interface PurchaseDAO {
	/**
	 * This method is a composite function that creates an entry in both Purchase and PurchaseItems table.
	 *
	 * @param purchase Purchase to be entered.
	 * @return Purchase - Created Purchase Entry.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 * @throws SQLException              Exception thrown based on SQL syntax.
	 */
	Purchase create(Purchase purchase) throws ApplicationErrorException, SQLException;

	/**
	 * This method counts the number of entries in the Purchase table based on date parameter.
	 *
	 * @param parameter Date of Purchase
	 * @return Count - Integer.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	Integer count(String parameter) throws ApplicationErrorException;

	/**
	 * This method Lists the Purchase and PurchaseItem entries based on the given searchable attribute
	 * and its corresponding search-text formatted in a pageable manner.
	 *
	 * @param attribute  The attribute to be looked upon.
	 * @param searchText The searchtext to be found.
	 * @param pageLength The number of entries that must be listed.
	 * @param offset     The Page number to be listed.
	 * @return List - Purchase.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	List<Purchase> list(String attribute, String searchText, int pageLength, int offset) throws ApplicationErrorException;


	/**
	 * This method lists the entries in the Purchase and PurchaseItems table based on the given search-text.
	 *
	 * @param searchText The search-text to be found.
	 * @return List - Purchase
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	List<Purchase> list(String searchText) throws ApplicationErrorException;


	/**
	 * This method deletes an entry in the Purchase table and the corresponding entries in the purchase-items table
	 *
	 * @param invoice Input invoice to perform delete.
	 * @return resultCode - Integer.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	Integer delete(int invoice) throws ApplicationErrorException;
}
