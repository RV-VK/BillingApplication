package DAO;

import Entity.Unit;
import Mapper.UnitMapper;
import SQLSession.MyBatisSession;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.sql.SQLException;
import java.util.List;

public class UnitDAO {
	private final SqlSessionFactory sqlSessionFactory = MyBatisSession.getSqlSessionFactory();
	private final SqlSession sqlSession = sqlSessionFactory.openSession();
	private final UnitMapper unitMapper = sqlSession.getMapper(UnitMapper.class);


	public Unit create(Unit unit) throws Exception {
		try {
			return unitMapper.create(unit);
		} catch(PersistenceException e) {
			Throwable cause = e.getCause();
			throw handleException((SQLException)cause);
		}
	}

	private Exception handleException(SQLException e) throws UniqueConstraintException, ApplicationErrorException, UnitCodeViolationException {
		if(e.getSQLState().equals("23505"))
			throw new UniqueConstraintException(">> Unit Code must be unique!!! the Unit code you have entered Already exists");
		else if(e.getSQLState().equals("23503"))
			throw new UnitCodeViolationException(">> Unit code in use!! Cannot edit or delete!!");
		throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
	}

	public List<Unit> list() throws ApplicationErrorException {
		try {
			return unitMapper.list();
		} catch(Exception e) {
			e.printStackTrace();
			throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
		}
	}


	public Unit edit(Unit unit) throws Exception {
		try {
			return unitMapper.edit(unit);
		} catch(PersistenceException e) {
			Throwable cause = e.getCause();
			throw handleException((SQLException)cause);
		}
	}


	public Integer delete(String code) throws Exception {
		try {
			return unitMapper.delete(code);
		} catch(PersistenceException e) {
			Throwable cause = e.getCause();
			throw handleException((SQLException)cause);
		}
	}


	public Unit findByCode(String code) throws ApplicationErrorException {
		try {
			return unitMapper.findByCode(code);
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}
}
