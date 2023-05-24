package Service;

import DAO.ApplicationErrorException;
import DAO.UniqueConstraintException;
import Entity.Unit;

import java.sql.SQLException;
import java.util.List;

public interface UnitService {
	/**
	 * This method invokes the DAO of the Unit entity and serves the Create function.
	 *
	 * @param unit Input Unit.
	 * @return Unit - Created unit.
	 * @throws SQLException              Exception thrown based on SQL syntax.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 * @throws UniqueConstraintException Custom Exception to convey Unique constraint Violation in SQL
	 *                                   table.
	 */
	Unit create(Unit unit)
			throws SQLException,
			ApplicationErrorException,
			UniqueConstraintException,
			InvalidTemplateException;

	/**
	 * This method invokes the DAO of the Unit entity and serves the List function.
	 *
	 * @return List - Units.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	List<Unit> list() throws ApplicationErrorException;

	/**
	 * This method invokes the DAO of the Unit entity and serves the Edit function.
	 *
	 * @param unit Edited Unit.
	 * @return resultCode - Integer.
	 * @throws SQLException              Exception thrown based on SQL syntax.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 * @throws UniqueConstraintException Custom Exception to convey Unique constraint Violation in SQL
	 *                                   table.
	 */
	Unit edit(Unit unit)
			throws SQLException,
			ApplicationErrorException,
			UniqueConstraintException,
			InvalidTemplateException;

	/**
	 * This method invokes the DAO of the Unit entity and serves the Delete function.
	 *
	 * @param code Input code to perform delete.
	 * @return resultCode - Integer.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	Integer delete(String code) throws ApplicationErrorException;
}
