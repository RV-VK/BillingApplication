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
	 * This method is a composite function that creates an entry in both Sales and Sales-items table.
	 *
	 * @param sales Input Sales.
	 * @return sales - Created Sales.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 * @throws SQLException              Exception thrown based on SQL syntax.
	 */
	@Select("INSERT INTO SALES(DATE,GRANDTOTAL) VALUES(CAST(#{date} AS DATE),#{grandTotal}) RETURNING *")
	Sales create(Sales sales) throws ApplicationErrorException, SQLException;



	/**
	 * This method counts the number of entries from the sales table based on date parameter.
	 *
	 * @param attribute  Attribute for count.
	 * @param searchText SearchText to be counted.
	 * @return count - Integer.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	@Select("SELECT COUNT(ID) FROM SALES WHERE ${attribute} = COALESCE(#{searchText},${attribute})")
	Integer count(@Param("attribute") String attribute,@Param("searchText") Object searchText) throws ApplicationErrorException;



	/**
	 * This method lists the Sales and SalesItem entries based on the given searchable attribute and
	 * its corresponding search-text formatted in pageable manner.
	 *
	 * @param attribute  The attribute to be looked upon.
	 * @param searchText The search-text to be found.
	 * @param pageLength The number of entries that must be listed.
	 * @param offset     The Page number to be listed.
	 * @return List - Sales.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	@Select("SELECT * FROM SALES WHERE ${attribute} = COALESCE(#{searchText} ,${attribute}) ORDER BY ID LIMIT #{pageLength}  OFFSET #{offset}")
	List<Sales> list(@Param("attribute") String attribute, @Param("searchText") Object searchText, @Param("pageLength") int pageLength, @Param("offset") int offset) throws ApplicationErrorException;



	/**
	 * This method lists the Entries from the Sales and SalesItem table based on the given search-text.
	 *
	 * @param searchText The search-text to be found.
	 * @return List - Sales.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	@Select("SELECT * FROM SALES WHERE CAST(ID AS TEXT) ILIKE '%${searchText}%' OR CAST(DATE AS TEXT) ILIKE '%${searchText}%'")
	List<Sales> searchList(String searchText) throws ApplicationErrorException;



	/**
	 * This method deletes an entry in the Sales table and the corresponding entries in the Sales items table.
	 *
	 * @param id Input id to perform delete.
	 * @return resultCode - Integer.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	@Delete("DELETE FROM SALES WHERE ID=#{id}")
	Integer delete(int id) throws ApplicationErrorException;
}
