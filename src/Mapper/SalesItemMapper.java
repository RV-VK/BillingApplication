package Mapper;

import Entity.SalesItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface SalesItemMapper {

	/**
	 * This method maps the Insert Query with SalesItem entity attributes.
	 *
	 * @param salesItem SalesItem to be inserted.
	 * @param invoice   Invoice number Key.
	 * @return SalesItem - Created Sales Item.
	 */
	@Results( {@Result(property = "product.code", column = "productcode"), @Result(property = "quantity", column = "quantity"), @Result(property = "unitSalesPrice", column = "salesprice")})
	@Select("INSERT INTO salesItems (id, productcode, quantity, salesprice) VALUES (#{id},#{salesItem.product.code},#{salesItem.quantity},#{salesItem.product.price}) RETURNING *")
	SalesItem create(@Param("salesItem") SalesItem salesItem, @Param("id") int invoice);


	/**
	 * This Interface method maps the List Query with id attribute.
	 *
	 * @param id ID for List function.
	 * @return List of SalesItems.
	 */
	@Results( {@Result(property = "product.name", column = "name"), @Result(property = "product.code", column = "productcode"), @Result(property = "quantity", column = "quantity"), @Result(property = "unitSalesPrice", column = "salesprice")})
	@Select("SELECT P.name, S.productcode, S.quantity, S.salesprice FROM salesItems S INNER JOIN product P ON P.code = S.productcode WHERE S.id=#{id}")
	List<SalesItem> list(@Param("id") int id);


	/**
	 * This Interface method Maps the Delete query with id attribute.
	 *
	 * @param id ID to be deleted.
	 * @return Integer - ResultCode.
	 */
	@Delete("DELETE FROM salesItems WHERE id=#{id}")
	Integer delete(@Param("id") int id);

}
