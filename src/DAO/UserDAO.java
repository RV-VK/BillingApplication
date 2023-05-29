package DAO;

import Entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO {

	/**
	 * This method Creates a User Entry in the User table
	 *
	 * @param user Input Object
	 * @return User Object - created
	 * @throws SQLException              Exception thrown based on SQL syntax.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 * @throws UniqueConstraintException Custom Exception to convey Unique constraint Violation in SQL table.
	 */
	@Select("INSERT INTO USERS(USERNAME,USERTYPE,PASSWORD,FIRSTNAME,LASTNAME,PHONENUMBER) VALUES (#{userName},#{userType},#{passWord},#{firstName},#{lastName},#{phoneNumber}) RETURNING *")
	User create(User user) throws SQLException, ApplicationErrorException, UniqueConstraintException;

	/**
	 * This method counts the number od entries in the user table.
	 *
	 * @return count - Integer
	 * @throws ApplicationErrorException Exception thrown due to persistence problems
	 */

	@Select("SELECT COUNT(*) FROM USERS WHERE  #{attribute} = COALESCE(#{searchText},#{attribute})")
	Integer count(@Param("attribute") String attribute,@Param("searchText") String searchText) throws ApplicationErrorException;

	/**
	 * This method Lists the records in the user table based on a given Search-text.
	 *
	 * @param searchText - The search-text that must be found.
	 * @return List - Users
	 * @throws ApplicationErrorException Exception thrown due to persistence problems
	 */

	@Select("SELECT * FROM USERS WHERE ( USERNAME ILIKE '" + "%${searchText}%" + "' OR USERTYPE ILIKE '" + "%${searchText}%" + "' OR PASSWORD ILIKE '" + "%${searchText}%" + "' OR FIRSTNAME ILIKE '" + "%${searchText}%" + "' OR LASTNAME ILIKE '" + "%${searchText}%" + "' OR CAST(ID AS TEXT) ILIKE '" + "%${searchText}%" + "' OR CAST(PHONENUMBER AS TEXT) ILIKE '" + "%${searchText}%" + "')")
	List<User> searchList(String searchText) throws ApplicationErrorException;

	/**
	 * This method lists the users in the user table based on the given searchable attribute
	 * and its corresponding search-text formatted in a pageable manner.
	 *
	 * @param attribute  The attribute to be looked upon
	 * @param searchText The search-text to be found.
	 * @param pageLength The number of entries that must be listed.
	 * @param offset     The Page number that has to be listed.
	 * @return List - Users
	 * @throws ApplicationErrorException Exception thrown due to persistence problems
	 */

	@Select("SELECT *  FROM USERS WHERE ${attribute} = COALESCE(#{searchText},${attribute}) ORDER BY ID LIMIT #{pageLength} OFFSET #{offset}")
	List<User> list(@Param("attribute") String attribute,@Param("searchText") String searchText,@Param("pageLength") int pageLength,@Param("offset") int offset) throws ApplicationErrorException;

	/**
	 * This method updates the attributes of the User entry in the user table.
	 *
	 * @param user The updated User Entry.
	 * @return User - Resulted User Entity.
	 * @throws SQLException              Exception thrown based on SQL syntax.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 * @throws UniqueConstraintException Custom Exception to convey Unique constraint Violation in SQL table
	 */
	@Select("UPDATE USERS SET USERNAME= COALESCE(#{userName},USERNAME),USERTYPE= COALESCE(#{userType},USERTYPE),PASSWORD= COALESCE(#{passWord},PASSWORD),FIRSTNAME= COALESCE(#{firstName},FIRSTNAME),LASTNAME= COALESCE(#{lastName},LASTNAME),PHONENUMBER=COALESCE(NULLIF(#{phoneNumber},0),PHONENUMBER) WHERE ID=#{id} RETURNING *")
	User edit(User user) throws SQLException, ApplicationErrorException, UniqueConstraintException;

	/**
	 * This method deleted an entry in the User table based on the given parameter.
	 *
	 * @param parameter Input parameter based on which the row is selected to delete.
	 * @return resultCode - Integer
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	@Delete("DELETE FROM USERS WHERE USERNAME=#{parameter}")
	Integer delete(String parameter) throws ApplicationErrorException;


	/**
	 * This method verifies whether the input username and password matches in the user table to enable login for the users.
	 *
	 * @param userName Unique entry username of the user
	 * @param passWord Password string of the user
	 * @return String - Usertype or null
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	@Select("SELECT PASSWORD,USERTYPE FROM USERS WHERE USERNAME=#{username}")
	User login(@Param("username") String userName, String passWord) throws SQLException, ApplicationErrorException;
}
