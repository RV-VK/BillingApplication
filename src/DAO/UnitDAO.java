package DAO;

import Entity.Unit;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.sql.SQLException;
import java.util.List;

public interface UnitDAO {

	/**
	 * This method creates an Entry in the Unit table.
	 *
	 * @param unit Input Unit
	 * @return Unit - Created Unit.
	 * @throws SQLException              Exception thrown based on SQL syntax.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 * @throws UniqueConstraintException Custom Exception to convey Unique constraint Violation in SQL table
	 */
	@Select("INSERT INTO UNIT(NAME,CODE,DESCRIPTION,ISDIVIDABLE) VALUES (#{name},#{code},#{description},#{isDividable}) RETURNING *")
	Unit create(Unit unit) throws SQLException, ApplicationErrorException, UniqueConstraintException, UnitCodeViolationException;


	/**
	 * This method Lists all the entries of the Unit table.
	 *
	 * @return List - Units.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	@Select("SELECT * FROM UNIT ORDER BY CODE")
	List<Unit> list() throws ApplicationErrorException;


	/**
	 * This method updates the attributes of the Unit entry in the Unit table.
	 *
	 * @param unit Updated Unit.
	 * @return Unit - Resulted Unit.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 * @throws SQLException              Exception thrown based on SQL syntax.
	 * @throws UniqueConstraintException Custom Exception to convey Unique constraint Violation in SQL table
	 */
	@Select("UPDATE UNIT SET NAME= COALESCE(#{name},NAME), CODE=COALESCE(#{code},CODE), DESCRIPTION=COALESCE(#{description},DESCRIPTION), ISDIVIDABLE=COALESCE(#{isDividable},ISDIVIDABLE) WHERE ID=#{id} RETURNING *")
	Unit edit(Unit unit) throws ApplicationErrorException, SQLException, UniqueConstraintException, UnitCodeViolationException;


	/**
	 * This method deletes an entry in the Unit table.
	 *
	 * @param code Input attribute to perform delete.
	 * @return resultCode - Integer
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	@Delete("DELETE FROM UNIT WHERE CODE=#{code}")
	Integer delete(String code) throws ApplicationErrorException, UniqueConstraintException, UnitCodeViolationException;


	/**
	 * This method returns a Unit entry based on its attribute code.
	 *
	 * @param code Input code
	 * @return Unit
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	@Select("SELECT * FROM UNIT WHERE CODE=#{code}")
	Unit findByCode(String code) throws ApplicationErrorException;
}
