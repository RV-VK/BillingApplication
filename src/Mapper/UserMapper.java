package Mapper;

import DAO.ApplicationErrorException;
import Entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.sql.SQLException;
import java.util.List;

public interface UserMapper {

	/**
	 * This Interface method maps the Insert Query with User attributes.
	 *
	 * @param user Input Object
	 * @return User Object - created
	 * @throws Exception This Exception is generalized and throws based on several conditions.
	 */
	@Select("INSERT INTO USERS(USERNAME,USERTYPE,PASSWORD,FIRSTNAME,LASTNAME,PHONENUMBER) VALUES (#{userName},#{userType},#{passWord},#{firstName},#{lastName},#{phoneNumber}) RETURNING *")
	User create(User user) throws Exception;




	/**
	 * This Interface method maps the Count Query with Column and Value Attributes.
	 *
	 * @return count - Integer
	 * @throws ApplicationErrorException Exception thrown due to persistence problems
	 */
	@Select("SELECT COUNT(*) FROM USERS WHERE  ${attribute} = COALESCE(#{searchText},${attribute})")
	Integer count(@Param("attribute") String attribute,@Param("searchText") Object searchText) throws ApplicationErrorException;




	/**
	 * This Interface method maps the List Query with necessary searchText attribute.
	 *
	 * @param searchText - The search-text that must be found.
	 * @return List - Users
	 * @throws ApplicationErrorException Exception thrown due to persistence problems
	 */
	@Select("SELECT * FROM USERS WHERE ( USERNAME ILIKE '" + "%${searchText}%" + "' OR USERTYPE ILIKE '" + "%${searchText}%" + "' OR PASSWORD ILIKE '" + "%${searchText}%" + "' OR FIRSTNAME ILIKE '" + "%${searchText}%" + "' OR LASTNAME ILIKE '" + "%${searchText}%" + "' OR CAST(ID AS TEXT) ILIKE '" + "%${searchText}%" + "' OR CAST(PHONENUMBER AS TEXT) ILIKE '" + "%${searchText}%" + "')")
	List<User> searchList(String searchText) throws ApplicationErrorException;




	/**
	 * This Interface method maps the List Query with List function attributes.
	 *
	 * @param attribute  The attribute to be looked upon
	 * @param searchText The search-text to be found.
	 * @param pageLength The number of entries that must be listed.
	 * @param offset     The Page number that has to be listed.
	 * @return List - Users
	 * @throws ApplicationErrorException Exception thrown due to persistence problems
	 */
	@Select("SELECT *  FROM USERS WHERE ${attribute} = COALESCE(#{searchText},${attribute}) ORDER BY ID LIMIT #{pageLength} OFFSET #{offset}")
	List<User> list(@Param("attribute") String attribute, @Param("searchText") Object searchText, @Param("pageLength") int pageLength, @Param("offset") int offset) throws ApplicationErrorException;




	/**
	 * This Interface method maps the Update Query with the User attributes
	 *
	 * @param user The updated User Entry.
	 * @return User - Resulted User Entity.
	 * @throws Exception This Exception is generalized and throws based on several conditions.
	 */
	@Select("UPDATE USERS SET USERNAME= COALESCE(#{userName},USERNAME),USERTYPE= COALESCE(#{userType},USERTYPE),PASSWORD= COALESCE(#{passWord},PASSWORD),FIRSTNAME= COALESCE(#{firstName},FIRSTNAME),LASTNAME= COALESCE(#{lastName},LASTNAME),PHONENUMBER=COALESCE(NULLIF(#{phoneNumber},0),PHONENUMBER) WHERE ID=#{id} RETURNING *")
	User edit(User user) throws Exception;




	/**
	 * This Interface method maps the Delete query with necessary column value to be deleted.
	 *
	 * @param parameter Input parameter based on which the row is selected to delete.
	 * @return resultCode - Integer
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	@Delete("DELETE FROM USERS WHERE USERNAME=#{parameter}")
	Integer delete(String parameter) throws ApplicationErrorException;




	/**
	 * This Interface method maps the Login Authentication query with Username and Password attributes.
	 *
	 * @param userName Unique entry username of the user
	 * @param passWord Password string of the user
	 * @return String - Usertype or null
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	@Select("SELECT PASSWORD,USERTYPE FROM USERS WHERE USERNAME=#{username}")
	User login(@Param("username") String userName, String passWord) throws SQLException, ApplicationErrorException;
}
