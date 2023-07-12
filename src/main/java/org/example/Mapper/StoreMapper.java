package org.example.Mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;
import org.example.DAO.ApplicationErrorException;
import org.example.Entity.Store;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

@Mapper
@Repository
public interface StoreMapper {

	/**
	 * This Interface method maps the Insert query with Store attributes.
	 *
	 * @param store Input Store entity.
	 * @return Store - Created store.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 * @throws SQLException              Exception thrown based on SQL syntax.
	 */
	@Select("INSERT INTO store (name, phonenumber, address, gstnumber) VALUES (#{name},#{phoneNumber},#{address},#{gstCode}) RETURNING *")
	Store create(Store store) throws ApplicationErrorException, SQLException;




	/**
	 * This Interface method maps the Update query with Store attributes.
	 *
	 * @param store Updated Store entity.
	 * @return Store - Resulted store.
	 * @throws SQLException              Exception thrown based on SQL syntax.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	@Select("UPDATE store SET name = COALESCE(#{name}, name), phonenumber = COALESCE(NULLIF(#{phoneNumber},0), phonenumber), address = COALESCE(#{address}, address), gstnumber = COALESCE(#{gstCode}, gstnumber) RETURNING *")
	Store edit(Store store) throws SQLException, ApplicationErrorException;





	/**
	 * This Interface method maps the Delete query with username and password attributes.
	 *
	 * @param password Password String to allow to delete store.
	 * @return statusCode - Integer.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	@Delete("TRUNCATE store ,product, users, unit, purchase, sales, purchaseitems, salesitems")
	Integer delete(String userName, String password) throws ApplicationErrorException;


	@Result(property = "gstCode", column = "gstnumber")
	@Select("SELECT * FROM STORE")
	Store view() throws ApplicationErrorException;

	/**
	 * This Interface method Executes the select query for Store table
	 *
	 * @return status - Boolean
	 * @throws SQLException Exception thrown based on SQLState.
	 */
	@Select("SELECT COUNT(ID) FROM store")
	Boolean checkIfStoreExists() throws SQLException, ApplicationErrorException;
}
