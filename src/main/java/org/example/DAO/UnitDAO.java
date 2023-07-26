package org.example.DAO;

import org.apache.ibatis.exceptions.PersistenceException;
import org.example.Entity.Unit;
import org.example.Mapper.UnitMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

@Component
public class UnitDAO {
	@Autowired
	private UnitMapper unitMapper;

	/**
	 * This method creates an Entry in the Unit table.
	 *
	 * @param unit Input Unit
	 * @return Unit - Created Unit.
	 * @throws Exception Throws Variable Exceptions namely SQLException, UnitCodeViolationException, UniqueConstraintException.
	 */
	public Unit create(Unit unit) throws Exception {
		try {
			return unitMapper.create(unit);
		} catch(Exception e) {
			Throwable cause = e.getCause();
			throw handleException((SQLException)cause);
		}
	}

	/**
	 * Private method to convert SQL Exception to user readable messages.
	 *
	 * @param e Exception Object.
	 * @throws UniqueConstraintException Custom Exception to convey Unique constraint Violation in SQL table.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	private Exception handleException(SQLException e) throws UniqueConstraintException, ApplicationErrorException, UnitCodeViolationException {
		if(e.getSQLState().equals("23505"))
			throw new UniqueConstraintException("Unit Code must be unique!!! the Unit code you have entered Already exists");
		else if(e.getSQLState().equals("23503"))
			throw new UnitCodeViolationException("Unit code in use!! Cannot edit or delete!!");
		throw new ApplicationErrorException("Application has went into an Error!!! Please Try again");
	}

	/**
	 * This method Lists all the entries of the Unit table.
	 *
	 * @return List of Units.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	public List<Unit> list() throws ApplicationErrorException {
		try {
			return unitMapper.list();
		} catch(Exception e) {
			e.printStackTrace();
			throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
		}
	}

	/**
	 * This method updates the attributes of the Unit entry in the Unit table.
	 *
	 * @param unit Updated Unit.
	 * @return Unit - Resulted Unit.
	 * @throws Exception Throws Variable Exceptions namely SQLException, UnitCodeViolationException, UniqueConstraintException.
	 */
	public Unit edit(Unit unit) throws Exception {
		try {
			return unitMapper.edit(unit);
		} catch(Exception e) {
			Throwable cause = e.getCause();
			throw handleException((SQLException)cause);
		}
	}

	/**
	 * This method deletes an entry in the Unit table.
	 *
	 * @param code Input attribute to perform delete.
	 * @return Integer - resultCode.
	 * @throws Exception Throws Variable Exceptions namely SQLException, UnitCodeViolationException, UniqueConstraintException.
	 */
	public Integer delete(String code) throws Exception {
		try {
			return unitMapper.delete(code);
		} catch(Exception e) {
			Throwable cause = e.getCause();
			throw handleException((SQLException)cause);
		}
	}

	/**
	 * This method returns a Unit entry based on its attribute code.
	 *
	 * @param code Input code
	 * @return Unit
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	public Unit findByCode(String code) throws ApplicationErrorException {
		try {
			return unitMapper.findByCode(code);
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}
}
