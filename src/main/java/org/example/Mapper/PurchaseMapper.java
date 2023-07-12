package org.example.Mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.DAO.ApplicationErrorException;
import org.example.Entity.Purchase;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PurchaseMapper {

	/**
	 * This Interface method maps the Insert Query with Purchase attributes.
	 *
	 * @param purchase Purchase to be entered.
	 * @return Purchase - Created Purchase Entry.
	 * @throws Exception This Exception is generalized and throws based on several conditions.
	 */
	@Select("INSERT INTO purchase(date, invoice, grandtotal) VALUES(CAST(#{date} AS date), #{invoice}, #{grandTotal}) RETURNING *")
	Purchase create(Purchase purchase) throws Exception;


	/**
	 * This Interface method maps the Count query with Column and Value attributes.
	 *
	 * @param searchText searchText to be counted.
	 * @return Count - Integer.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	@Select("SELECT COUNT(id) FROM purchase WHERE ${attribute} = COALESCE(#{searchText},${attribute})")
	Integer count(@Param("attribute") String attribute, @Param("searchText") Object searchText) throws ApplicationErrorException;


	/**
	 * This Interface method maps the List query with List function attributes.
	 *
	 * @param attribute  The attribute to be looked upon.
	 * @param searchText The searchtext to be found.
	 * @param pageLength The number of entries that must be listed.
	 * @param offset     The Page number to be listed.
	 * @return List - Purchase.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	@Select("SELECT * FROM purchase WHERE ${attribute} = COALESCE(#{searchText},${attribute}) ORDER BY ID LIMIT #{pageLength} OFFSET #{offset}")
	List<Purchase> list(@Param("attribute") String attribute, @Param("searchText") Object searchText, @Param("pageLength") int pageLength, @Param("offset") int offset) throws ApplicationErrorException;


	/**
	 * This Interface method maps the List query with searchText attribute.
	 *
	 * @param searchText The search-text to be found.
	 * @return List - Purchase
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	@Select("SELECT * FROM purchase WHERE CAST(id AS TEXT) ILIKE '%${searchText}%' OR CAST(date AS TEXT) ILIKE '%${searchText}%' OR CAST(invoice AS TEXT) ILIKE  '%${searchText}%'")
	List<Purchase> searchList(String searchText) throws ApplicationErrorException;


	/**
	 * This Interface method maps the Delete query with Invoice attribute.
	 *
	 * @param invoice Input invoice to perform delete.
	 * @return resultCode - Integer.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	@Delete("DELETE FROM purchase WHERE invoice= #{invoice}")
	Integer delete(int invoice) throws ApplicationErrorException;
}
