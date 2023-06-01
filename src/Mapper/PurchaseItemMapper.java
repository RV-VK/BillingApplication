package Mapper;

import Entity.PurchaseItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PurchaseItemMapper {

	/**
	 * This Interface method maps the Insert query with PurchaseItem attributes.
	 *
	 * @param purchaseItem Purchase Item entity to be Inserted.
	 * @param invoice      Invoice number Key.
	 * @return PurchaseItem - Created Purchase Item entity
	 */
	@Results( {
			@Result(property = "product.code", column = "productcode"),
			@Result(property = "quantity", column = "quantity"),
			@Result(property = "unitPurchasePrice", column = "costprice")
	})
	@Select("INSERT INTO purchaseItems (invoice, productCode, quantity, costPrice) VALUES(#{invoice},#{purchaseItem.product.code, jdbcType=VARCHAR },#{purchaseItem.quantity},#{purchaseItem.unitPurchasePrice}) RETURNING productCode, quantity, costPrice")
	PurchaseItem create(@Param("purchaseItem") PurchaseItem purchaseItem, @Param("invoice") int invoice);


	/**
	 * This Interface method maps the List Query with Invoice attribute.
	 *
	 * @param invoice Invoice for list function.
	 * @return List of PurchaseItems.
	 */
	@Results( {
			@Result(property = "product.name", column = "name"),
			@Result(property = "product.code", column = "productcode"),
			@Result(property = "quantity", column = "quantity"),
			@Result(property = "unitPurchasePrice", column = "costprice")
	})
	@Select("SELECT P.name,PU.productCode,PU.quantity,PU.costPrice FROM purchaseItems PU INNER JOIN product P ON P.code = PU.productCode WHERE PU.invoice=#{invoice}")
	List<PurchaseItem> list(@Param("invoice") int invoice);


	/**
	 * This Interface method maps the Delete Query with invoice attribute.
	 *
	 * @param invoice Invoice to be deleted.
	 * @return Integer - ResultCode.
	 */
	@Delete("DELETE FROM purchaseItems WHERE invoice=#{invoice}")
	Integer delete(@Param("invoice") int invoice);
}
