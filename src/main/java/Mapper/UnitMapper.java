package Mapper;

import DAO.ApplicationErrorException;
import Entity.Unit;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

@Component
public interface UnitMapper {

	/**
	 * This Interface method maps the Insert Query with Unit attributes.
	 *
	 * @param unit Input Unit
	 * @return Unit - Created Unit.
	 * @throws SQLException This Exception is generalized and throws based on several conditions.
	 */
	@Select("INSERT INTO unit(name ,code , description, isdividable) VALUES (#{name},#{code},#{description},#{isDividable}) RETURNING *")
	Unit create(Unit unit) throws Exception;


	/**
	 * This Interface method Executes the List query.
	 *
	 * @return List - Units.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	@Select("SELECT * FROM unit ORDER BY code")
	List<Unit> list() throws ApplicationErrorException;


	/**
	 * This Interface method maps the Update query with Unit attributes.
	 *
	 * @param unit Updated Unit.
	 * @return Unit - Resulted Unit.
	 * @throws Exception This Exception is generalized and throws based on several conditions.
	 */
	@Select("UPDATE unit SET name= COALESCE(#{name}, name), code = COALESCE(#{code}, code), description = COALESCE(#{description}, description), isdividable = COALESCE(#{isDividable}, isdividable) WHERE id=#{id} RETURNING *")
	Unit edit(Unit unit) throws Exception;


	/**
	 * This Interface method maps the Delete query with product code attribute.
	 *
	 * @param code Input attribute to perform delete.
	 * @return resultCode - Integer
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	@Delete("DELETE FROM unit WHERE code=#{code}")
	Integer delete(String code) throws Exception;


	/**
	 * This Interface method maps the Select query to find the Unit by its code.
	 *
	 * @param code Input code
	 * @return Unit
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	@Select("SELECT * FROM unit WHERE code=#{code}")
	Unit findByCode(String code) throws ApplicationErrorException;
}
