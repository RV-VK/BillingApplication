package Mapper;

import Entity.PurchaseItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PurchaseItemMapper {

	/**
	 * This Interface method maps the Insert query with PurchaseItem attributes.
	 *
	 * @param purchaseItem Purchase Item entity to be Inserted.
	 * @param invoice Invoice number Key.
	 * @return PurchaseItem - Created Purchase Item entity
	 */
	@Results({
			@Result(property = "product.code", column = "productcode"),
			@Result(property = "quantity", column = "quantity"),
			@Result(property = "unitPurchasePrice", column = "costprice")
	})
	@Select("INSERT INTO PURCHASEITEMS(INVOICE,PRODUCTCODE,QUANTITY,COSTPRICE) VALUES(#{invoice},#{purchaseItem.product.code, jdbcType=VARCHAR },#{purchaseItem.quantity},#{purchaseItem.unitPurchasePrice}) RETURNING PRODUCTCODE, QUANTITY, COSTPRICE")
	PurchaseItem create(@Param("purchaseItem") PurchaseItem purchaseItem, @Param("invoice") int invoice);




	/**
	 * This Interface method maps the List Query with Invoice attribute.
	 * @param invoice Invoice for list function.
	 * @return List of PurchaseItems.
	 */
	@Results({
			@Result(property = "product.name", column = "name"),
			@Result(property = "product.code", column = "productcode"),
			@Result(property = "quantity", column = "quantity"),
			@Result(property = "unitPurchasePrice", column = "costprice")
	})
	@Select("SELECT P.NAME,PU.PRODUCTCODE,PU.QUANTITY,PU.COSTPRICE FROM PURCHASEITEMS PU INNER JOIN PRODUCT P ON P.CODE=PU.PRODUCTCODE WHERE PU.INVOICE=#{invoice}")
	List<PurchaseItem> list(@Param("invoice")int invoice);





	/**
	 * This Interface method maps the Delete Query with invoice attribute.
	 *
	 * @param invoice Invoice to be deleted.
	 * @return Integer - ResultCode.
	 */
	@Delete("DELETE FROM PURCHASEITEMS WHERE INVOICE=#{invoice}")
	Integer delete(@Param("invoice") int invoice);
}
