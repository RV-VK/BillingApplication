package DAO;

import SQLSession.DBHelper;
import Entity.Unit;
import SQLSession.MyBatisSession;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UnitDAOImplementation implements UnitDAO {
	private final SqlSessionFactory sqlSessionFactory = MyBatisSession.getSqlSessionFactory();
	private final SqlSession sqlSession = sqlSessionFactory.openSession();
	private final UnitDAO unitMapper = sqlSession.getMapper(UnitDAO.class);
	private List<Unit> unitList = new ArrayList<>();


	@Override
	public Unit create(Unit unit) throws SQLException, ApplicationErrorException, UniqueConstraintException, UnitCodeViolationException {
		try {
			return unitMapper.create(unit);
		} catch(PersistenceException e) {
			Throwable cause = e.getCause();
			handleException((SQLException) cause);
			return null;
		}
	}

	private void handleException(SQLException e) throws UniqueConstraintException, ApplicationErrorException, UnitCodeViolationException {
		if(e.getSQLState().equals("23505"))
			throw new UniqueConstraintException(">> Unit Code must be unique!!! the Unit code you have entered Already exists");
		else if(e.getSQLState().equals("23503"))
			throw new UnitCodeViolationException(">> Unit code in use!! Cannot edit or delete!!");
		throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
	}

	@Override
	public List<Unit> list() throws ApplicationErrorException {
		try {
			return unitMapper.list();
		} catch(Exception e) {
			e.printStackTrace();
			throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
		}
	}


	@Override
	public Unit edit(Unit unit) throws ApplicationErrorException, SQLException, UniqueConstraintException, UnitCodeViolationException {
		try {
			return unitMapper.edit(unit);
		} catch(PersistenceException e) {
			Throwable cause = e.getCause();
			handleException((SQLException) cause);
			return null;
		}
	}


	@Override
	public Integer delete(String code) throws ApplicationErrorException, UniqueConstraintException, UnitCodeViolationException {
		try {
			return unitMapper.delete(code);
		} catch(PersistenceException e) {
			Throwable cause = e.getCause();
			handleException((SQLException) cause);
			return null;
		}
	}


	@Override
	public Unit findByCode(String code) throws ApplicationErrorException {
		try {
			return unitMapper.findByCode(code);
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}
}
