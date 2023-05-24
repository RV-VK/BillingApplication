package Service;

import DAO.ApplicationErrorException;
import DAO.UniqueConstraintException;
import DAO.UnitDAO;
import DAO.UnitDAOImplementation;
import Entity.Unit;

import java.sql.SQLException;
import java.util.List;

public class UnitServiceImplementation implements UnitService {
	private final UnitDAO unitDAO = new UnitDAOImplementation();
	private final String NAME_REGEX = "^[a-zA-Z\\s]{1,30}$";
	private final String CODE_REGEX = "^[a-zA-Z]{1,4}$";


	@Override
	public Unit create(Unit unit)
			throws SQLException, ApplicationErrorException, UniqueConstraintException, InvalidTemplateException {
		validate(unit);
		return unitDAO.create(unit);
	}


	@Override
	public List<Unit> list() throws ApplicationErrorException {
		return unitDAO.list();
	}


	@Override
	public Unit edit(Unit unit)
			throws SQLException, ApplicationErrorException, UniqueConstraintException, InvalidTemplateException {
		validate(unit);
		return unitDAO.edit(unit);
	}


	@Override
	public Integer delete(String code) throws ApplicationErrorException {
		return unitDAO.delete(code);
	}


	/**
	 * This method Validates the unit attributes.
	 *
	 * @param unit Unit to be validated
	 */
	private void validate(Unit unit) throws InvalidTemplateException {
		if(unit.getName() != null && ! unit.getName().matches(NAME_REGEX))
			throw new InvalidTemplateException(">> Invalid Unit Name!!");
		if(unit.getCode() != null && ! unit.getCode().matches(CODE_REGEX))
			throw new InvalidTemplateException(">> Invalid UnitCode!!");
	}
}
