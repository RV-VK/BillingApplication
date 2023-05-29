package DAO;

import Entity.Purchase;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.sql.Date;
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
	@Select("INSERT INTO PURCHASE(DATE,INVOICE,GRANDTOTAL) VALUES(CAST(#{date} AS DATE),#{invoice},#{grandTotal}) RETURNING *")
	Purchase create(Purchase purchase) throws ApplicationErrorException, SQLException, UniqueConstraintException;

	/**
	 * This method counts the number of entries in the Purchase table based on date parameter.
	 *
	 * @param searchText searchText to be counted.
	 * @return Count - Integer.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	@Select("SELECT COUNT(ID) FROM PURCHASE WHERE ${attribute} = COALESCE(#{searchText}, ${attribute})")
	Integer count(@Param("attribute") String attribute,@Param("searchText") String  searchText) throws ApplicationErrorException;

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
	@Select("SELECT * FROM PURCHASE WHERE #{attribute} = COALESCE(#{searchText} ,#{attribute}) ORDER BY ID LIMIT #{pageLength} OFFSET #{offset}")
	List<Purchase> list(@Param("attribute") String attribute,@Param("searchText") String searchText,@Param("pageLength") int pageLength,@Param("offset") int offset) throws ApplicationErrorException;


	/**
	 * This method lists the entries in the Purchase and PurchaseItems table based on the given search-text.
	 *
	 * @param searchText The search-text to be found.
	 * @return List - Purchase
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	@Select("SELECT * FROM PURCHASE WHERE CAST(ID AS TEXT) ILIKE '%${searchText}%' OR CAST(DATE AS TEXT) ILIKE '%${searchText}%' OR CAST(INVOICE AS TEXT) ILIKE  '%${searchText}%'")
	List<Purchase> searchList(String searchText) throws ApplicationErrorException;


	/**
	 * This method deletes an entry in the Purchase table and the corresponding entries in the purchase-items table
	 *
	 * @param invoice Input invoice to perform delete.
	 * @return resultCode - Integer.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	@Delete("DELETE FROM PURCHASE WHERE INVOICE= #{invoice}")
	Integer delete(int invoice) throws ApplicationErrorException;
}
