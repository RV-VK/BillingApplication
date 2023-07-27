package org.example.Service;

import org.example.DAO.ApplicationErrorException;
import org.example.DAO.UnitDAO;
import org.example.Entity.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UnitServiceImplementation implements UnitService {
	private final String NAME_REGEX = "^[a-zA-Z\\s]{3,30}$";
	private final String CODE_REGEX = "^[a-zA-Z]{1,4}$";
	@Autowired
	private UnitDAO unitDAO;

	@Override
	public Unit create(Unit unit) throws Exception {
		validate(unit);
		return unitDAO.create(unit);
	}


	@Override
	public List<Unit> list() throws ApplicationErrorException {
		return unitDAO.list();
	}


	@Override
	public Unit edit(Unit unit) throws Exception {
		validate(unit);
		return unitDAO.edit(unit);
	}


	@Override
	public Integer delete(String code) throws Exception {
		if(code != null)
			return unitDAO.delete(code);
		else
			return - 1;
	}


	/**
	 * This method Validates the unit attributes.
	 *
	 * @param unit Unit to be validated
	 */
	private void validate(Unit unit) throws InvalidTemplateException {
		if(unit == null)
			throw new NullPointerException("Unit Cannot be Null!!");
		if(unit.getName() != null && ! unit.getName().matches(NAME_REGEX))
			throw new InvalidTemplateException("Invalid Unit Name!!");
		else if(unit.getName() == null)
			throw new NullPointerException("Unit Name cannot be null!");
		if(unit.getCode() != null && ! unit.getCode().matches(CODE_REGEX))
			throw new InvalidTemplateException("Invalid UnitCode!!");
		else if(unit.getCode() == null)
			throw new NullPointerException("Unit Code Cannot be null!");
		if(unit.getIsDividable() == null)
			throw new NullPointerException("Isdividable field cannot be null!");
		if(unit.getDescription() == null)
			throw new NullPointerException("Description Cannot be null!");
	}
}
