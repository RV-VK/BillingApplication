package Mapper;

import DAO.ApplicationErrorException;
import DAO.PageCountOutOfBoundsException;
import Entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ProductMapper {

	/**
	 * This Interface method Maps the create query with Product Entity.
	 *
	 * @param product Product Entry to be Created.
	 * @return Product - Created Product
	 * @throws Exception This Exception is generalized and throws based on several conditions.
	 */
	@Select("INSERT INTO product(code, name, unitcode, type, price, stock) VALUES (#{code},#{name},#{unitcode},#{type},#{price},#{stock}) RETURNING *")
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
	@Select("SELECT * FROM product WHERE ${attribute} = coalesce(#{searchText},${attribute}) AND isdeleted=false ORDER BY ID LIMIT #{pageLength} OFFSET #{offset}")
	List<Product> list(@Param("attribute") String attribute, @Param("searchText") Object searchText, @Param("pageLength") int pageLength, @Param("offset") int offset) throws ApplicationErrorException, PageCountOutOfBoundsException;



	/**
	 * This Interface method maps the List query with necessary Searchtext Parameter.
	 *
	 * @param searchText The search-text that must be found.
	 * @return List - Products
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	@Select("SELECT * FROM product WHERE ( name ILIKE '" + "%${searchText}%" + "' OR code ILIKE '" + "%${searchText}%" + "' OR unitcode ILIKE '" + "%${searchText}%" + "' OR type ILIKE '" + "%${searchText}%" + "' OR CAST(id AS TEXT) ILIKE '" + "%${searchText}%" + "' OR CAST(stock AS TEXT) ILIKE '" + "%${searchText}%" + "' OR CAST(price AS TEXT) ILIKE '" + "%${searchText}%" + "' )" + " AND isdeleted = false")
	List<Product> searchList(String searchText) throws ApplicationErrorException;



	/**
	 * This Interface method Maps the Update Query with the Product Attributes.
	 *
	 * @param product The Updated Product entry
	 * @return Product - Result Product
	 * @throws Exception This Exception is generalized and throws based on several conditions.
	 *
	 */
	@Select("UPDATE product SET code = COALESCE(#{code}, code), name= COALESCE(#{name}, name), unitcode= COALESCE(#{unitcode}, unitcode), type = COALESCE(#{type}, type), price = COALESCE(NULLIF(#{price},0),price), stock = COALESCE(#{stock},stock) WHERE id=#{id} RETURNING *")
	Product edit(Product product) throws Exception;



	/**
	 * This Interface method maps the Delete Query with Column Value attribute.
	 *
	 * @param parameter Input parameter based on which the row is selected to delete.
	 * @return resultCode - Integer
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */

	@Update("UPDATE product SET isdeleted='true' WHERE (CAST(id AS TEXT) ILIKE '${parameter}' OR code='${parameter}') AND stock = 0")
	Integer delete(String parameter) throws ApplicationErrorException;



	/**
	 * This Interface method maps the Select Query to find a product by its code.
	 *
	 * @param code Input product code.
	 * @return Product
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	@Select("SELECT * FROM product WHERE code = #{code} AND isdeleted = false")

	Product findByCode(String code) throws ApplicationErrorException;


}
