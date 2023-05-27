package DAO;

import Entity.Product;
import java.sql.SQLException;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface ProductDAO {

	/**
	 * This method creates an entry in the Product table
	 *
	 * @param product Input product
	 * @return Product - Created product.
	 * @throws SQLException               Exception thrown based on SQL syntax.
	 * @throws ApplicationErrorException  Exception thrown due to Persistence problems.
	 * @throws UniqueConstraintException  Custom Exception to convey Unique constraint Violation in SQL
	 *                                    table
	 * @throws UnitCodeViolationException Custom Exception to convey Foreign Key Violation in Product
	 *                                    table.
	 */

	@Select("INSERT into product(CODE,NAME,UNITCODE,TYPE,PRICE,STOCK) VALUES (#{code},#{name},#{unitcode},#{type},#{price},#{stock}) RETURNING *")
	Product create(Product product) throws SQLException, ApplicationErrorException, UniqueConstraintException, UnitCodeViolationException;

	/**
	 * This Method returns the number of entries in the Product table.
	 *
	 * @return count
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */

	@Select("select count(id) from product")
	Integer count() throws ApplicationErrorException;

	/**
	 * This method lists the products in the product table based on the given searchable attribute and
	 * its corresponding search-text formatted in a pageable manner.
	 *
	 * @param attribute  The attribute to be looked upon
	 * @param searchText The search-text to be found.
	 * @param pageLength The number of entries that must be listed.
	 * @param offset     The Page number that has to be listed.
	 * @return List - Products
	 * @throws ApplicationErrorException     Exception thrown due to persistence problems
	 * @throws PageCountOutOfBoundsException Exception thrown in a pageable list function if a
	 *                                       non-existing page is prompted.
	 */

	@Select("Select * from product where ${attribute} = coalesce(#{searchText},${attribute}) AND isdeleted=false order by id limit #{pageLength} offset #{offset}")
	List<Product> list(@Param("attribute") String attribute,@Param("searchText") String searchText,@Param("pageLength") int pageLength,@Param("offset") int offset) throws ApplicationErrorException, PageCountOutOfBoundsException;

	/**
	 * This method Lists the products in the product table based on the given search-text.
	 *
	 * @param searchText The search-text that must be found.
	 * @return List - Products
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */

	@Select("SELECT * FROM PRODUCT WHERE ( NAME ILIKE '" + "%${searchText}%" + "' OR CODE ILIKE '" + "%${searchText}%" + "' OR UNITCODE ILIKE '" + "%${searchText}%" + "' OR TYPE ILIKE '" + "%${searchText}%" + "' OR CAST(ID AS TEXT) ILIKE '" + "%${searchText}%" + "' OR CAST(STOCK AS TEXT) ILIKE '" + "%${searchText}%" + "' OR CAST(PRICE AS TEXT) ILIKE '" + "%${searchText}%" + "' )" + " AND ISDELETED=FALSE")
	List<Product> searchList(String searchText) throws ApplicationErrorException;

	/**
	 * This method updates the attributes of the product entry in the Product table
	 *
	 * @param product The Updated Product entry
	 * @return Product - Result Product
	 * @throws SQLException               Exception thrown based on SQL syntax.
	 * @throws ApplicationErrorException  Exception thrown due to Persistence problems.
	 * @throws UniqueConstraintException  Custom Exception to convey Unique constraint Violation in SQL
	 *                                    table
	 * @throws UnitCodeViolationException Custom Exception to convey Foreign Key Violation in Product
	 *                                    table.
	 */

	@Select("UPDATE PRODUCT SET CODE= COALESCE(#{code},CODE),NAME= COALESCE(#{name},NAME),UNITCODE= COALESCE(#{unitcode},UNITCODE),TYPE= COALESCE(#{type},TYPE),PRICE= COALESCE(NULLIF(#{price},0),PRICE) WHERE ID=#{id} RETURNING *")
	Product edit(Product product) throws SQLException, ApplicationErrorException, UniqueConstraintException, UnitCodeViolationException;

	/**
	 * This method deletes an entry in the Product table based on the given parameter.
	 *
	 * @param parameter Input parameter based on which the row is selected to delete.
	 * @return resultCode - Integer
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */

	@Update("UPDATE PRODUCT SET ISDELETED='TRUE' WHERE (CAST(ID AS TEXT) ILIKE '${parameter}' OR CODE='${parameter}') AND STOCK=0")
	Integer delete(String parameter) throws ApplicationErrorException;

	/**
	 * This method finds the Product by its product code attribute.
	 *
	 * @param code Input product code.
	 * @return Product
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */

	@Select("SELECT * FROM PRODUCT WHERE CODE=#{code}")
	Product findByCode(String code) throws ApplicationErrorException;


}
