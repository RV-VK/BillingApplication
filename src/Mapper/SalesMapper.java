package Mapper;

import DAO.ApplicationErrorException;
import Entity.Sales;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.sql.SQLException;
import java.util.List;

public interface SalesMapper {

	/**
	 * This Interface method maps the Insert Query with the Sales attributes.
	 *
	 * @param sales Input Sales.
	 * @return sales - Created Sales.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 * @throws SQLException              Exception thrown based on SQL syntax.
	 */
	@Select("INSERT INTO sales(date, grandtotal) VALUES(CAST(#{date} AS date),#{grandTotal}) RETURNING *")
	Sales create(Sales sales) throws ApplicationErrorException, SQLException;


	/**
	 * This Interface method maps the Count query with Column and Value attributes.
	 *
	 * @param attribute  Attribute for count.
	 * @param searchText SearchText to be counted.
	 * @return count - Integer.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	@Select("SELECT COUNT(ID) FROM sales WHERE ${attribute} = COALESCE(#{searchText},${attribute})")
	Integer count(@Param("attribute") String attribute, @Param("searchText") Object searchText) throws ApplicationErrorException;


	/**
	 * This Interface method maps the List query with List function Attributes.
	 *
	 * @param attribute  The attribute to be looked upon.
	 * @param searchText The search-text to be found.
	 * @param pageLength The number of entries that must be listed.
	 * @param offset     The Page number to be listed.
	 * @return List - Sales.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	@Select("SELECT * FROM sales WHERE ${attribute} = COALESCE(#{searchText} ,${attribute}) ORDER BY ID LIMIT #{pageLength}  OFFSET #{offset}")
	List<Sales> list(@Param("attribute") String attribute, @Param("searchText") Object searchText, @Param("pageLength") int pageLength, @Param("offset") int offset) throws ApplicationErrorException;


	/**
	 * This Interface method maps the List query with the searchText attribute.
	 *
	 * @param searchText The search-text to be found.
	 * @return List - Sales.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	@Select("SELECT * FROM sales WHERE CAST(id AS TEXT) ILIKE '%${searchText}%' OR CAST(date AS TEXT) ILIKE '%${searchText}%'")
	List<Sales> searchList(String searchText) throws ApplicationErrorException;


	/**
	 * This Interface method maps the Delete query with id attribute.
	 *
	 * @param id Input id to perform delete.
	 * @return resultCode - Integer.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	@Delete("DELETE FROM sales WHERE id=#{id}")
	Integer delete(int id) throws ApplicationErrorException;
}
