package DAO;

import Entity.Store;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.sql.SQLException;

public interface StoreDAO {

	/**
	 * This method creates an Entry in the Store table.
	 *
	 * @param store Input Store entity.
	 * @return Store - Created store.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 * @throws SQLException              Exception thrown based on SQL syntax.
	 */
	@Select("INSERT INTO STORE (NAME,PHONENUMBER,ADDRESS,GSTNUMBER) VALUES (#{name},#{phoneNumber},#{address},#{gstCode}) RETURNING *")
	Store create(Store store) throws ApplicationErrorException, SQLException;

	/**
	 * This method updates the attributes of the Store entry in the Store table.
	 *
	 * @param store Updated Store entity.
	 * @return Store - Resulted store.
	 * @throws SQLException              Exception thrown based on SQL syntax.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */

	@Select("UPDATE STORE SET NAME=COALESCE(#{name},NAME), PHONENUMBER=COALESCE(NULLIF(#{phoneNumber},0),PHONENUMBER), ADDRESS= COALESCE(#{address},ADDRESS), GSTNUMBER=COALESCE(#{gstCode},GSTNUMBER) RETURNING *")
	Store edit(Store store) throws SQLException, ApplicationErrorException;

	/**
	 * This method deleted the store Entry in the Store table.
	 *
	 * @param password Password String to allow to delete store.
	 * @return statusCode - Integer.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	@Delete("TRUNCATE STORE ,PRODUCT, USERS, UNIT, PURCHASE, SALES, PURCHASEITEMS, SALESITEMS")
	Integer delete(String userName, String password) throws ApplicationErrorException;


	/**
	 * This method Checks whether an entry is created in Store table or not
	 *
	 * @return status - Boolean
	 * @throws SQLException Exception thrown based on SQLState.
	 */
	@Select("SELECT COUNT(ID) FROM STORE")
	Boolean checkIfStoreExists() throws SQLException, ApplicationErrorException;
}
