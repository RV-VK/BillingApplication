package Mapper;

import DAO.ApplicationErrorException;
import DAO.PageCountOutOfBoundsException;
import Entity.Product;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ProductMapper {

	/**
	 * This Interface method Maps the create query with Product Entity.
	 *
	 * @param product Product Entry to be Created.
	 * @return Product - Created Product
	 * @throws Exception This Exception is generalized and throws based on several conditions.
	 */
	@Select("INSERT into product(CODE,NAME,UNITCODE,TYPE,PRICE,STOCK) VALUES (#{code},#{name},#{unitcode},#{type},#{price},#{stock}) RETURNING *")
	Product create(Product product) throws Exception;



	/**
	 * This Interface method Maps the count Query with necessary column and value Parameters.
	 *
	 * @param attribute The Column to be counted.
	 * @param searchText The SearchText to be counted.
	 * @return Integer - Count.
	 * @throws ApplicationErrorException Exception thrown due to persistence problems.
	 */
	@Select("SELECT count(*) FROM product WHERE ${attribute} = COALESCE(#{searchText},${attribute}) AND isdeleted = false")
	Integer count(@Param("attribute") String attribute, @Param("searchText") Object searchText) throws ApplicationErrorException;



	/**
	 * This Interface method Maps the List query with necessary List function Parameters.
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
	List<Product> list(@Param("attribute") String attribute, @Param("searchText") Object searchText, @Param("pageLength") int pageLength, @Param("offset") int offset) throws ApplicationErrorException, PageCountOutOfBoundsException;



	/**
	 * This Interface method maps the List query with necessary Searchtext Parameter.
	 *
	 * @param searchText The search-text that must be found.
	 * @return List - Products
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	@Select("SELECT * FROM PRODUCT WHERE ( NAME ILIKE '" + "%${searchText}%" + "' OR CODE ILIKE '" + "%${searchText}%" + "' OR UNITCODE ILIKE '" + "%${searchText}%" + "' OR TYPE ILIKE '" + "%${searchText}%" + "' OR CAST(ID AS TEXT) ILIKE '" + "%${searchText}%" + "' OR CAST(STOCK AS TEXT) ILIKE '" + "%${searchText}%" + "' OR CAST(PRICE AS TEXT) ILIKE '" + "%${searchText}%" + "' )" + " AND ISDELETED=FALSE")
	List<Product> searchList(String searchText) throws ApplicationErrorException;



	/**
	 * This Interface method Maps the Update Query with the Product Attributes.
	 *
	 * @param product The Updated Product entry
	 * @return Product - Result Product
	 * @throws Exception This Exception is generalized and throws based on several conditions.
	 *
	 */
	@Select("UPDATE PRODUCT SET CODE= COALESCE(#{code},CODE),NAME= COALESCE(#{name},NAME),UNITCODE= COALESCE(#{unitcode},UNITCODE),TYPE= COALESCE(#{type},TYPE),PRICE= COALESCE(NULLIF(#{price},0),PRICE),STOCK= COALESCE(#{stock},STOCK) WHERE ID=#{id} RETURNING *")
	Product edit(Product product) throws Exception;



	/**
	 * This Interface method maps the Delete Query with Column Value attribute.
	 *
	 * @param parameter Input parameter based on which the row is selected to delete.
	 * @return resultCode - Integer
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */

	@Update("UPDATE PRODUCT SET ISDELETED='TRUE' WHERE (CAST(ID AS TEXT) ILIKE '${parameter}' OR CODE='${parameter}') AND STOCK=0")
	Integer delete(String parameter) throws ApplicationErrorException;



	/**
	 * This Interface method maps the Select Query to find a product by its code.
	 *
	 * @param code Input product code.
	 * @return Product
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	@Select("SELECT * FROM PRODUCT WHERE CODE=#{code} AND ISDELETED=FALSE")

	Product findByCode(String code) throws ApplicationErrorException;


}
