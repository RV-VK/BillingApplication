package DAO;

import Entity.SalesItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SalesItemMapper {

	@Results({
			@Result(property = "product.code", column = "productcode"),
			@Result(property = "quantity", column = "quantity"),
			@Result(property = "unitSalesPrice", column = "salesprice")
	})
	@Select("INSERT INTO SALESITEMS (ID, PRODUCTCODE, QUANTITY, SALESPRICE) VALUES (#{id},#{salesItem.product.code},#{salesItem.quantity},#{salesItem.product.price}) RETURNING *")
	SalesItem create(@Param("salesItem")SalesItem salesItem, @Param("id") int invoice);


	@Results({
			@Result(property = "product.name", column = "name"),
			@Result(property = "product.code", column = "productcode"),
			@Result(property = "quantity", column = "quantity"),
			@Result(property = "unitSalesPrice", column = "salesprice")
	})
	@Select("SELECT P.NAME, S.PRODUCTCODE,S.QUANTITY,S.SALESPRICE FROM SALESITEMS S INNER JOIN PRODUCT P ON P.CODE=S.PRODUCTCODE WHERE S.ID=#{id}")
	List<SalesItem> list(@Param("id") int id);

	@Select("DELETE FROM SALESITEMS WHERE ID=#{id}")
	Integer delete(@Param("id") int id);

}
