package DAO;

import Entity.Store;
import Entity.User;
import Mapper.StoreMapper;
import SQLSession.MyBatisSession;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.sql.SQLException;

public class StoreDAO {
	private final SqlSessionFactory sqlSessionFactory = MyBatisSession.getSqlSessionFactory();
	private final SqlSession sqlSession = sqlSessionFactory.openSession();
	private final StoreMapper storeMapper = sqlSession.getMapper(StoreMapper.class);
	private final UserDAO userDAO = new UserDAO();


	public Store create(Store store) throws ApplicationErrorException, SQLException {
		try {
			return storeMapper.create(store);
		} catch(PersistenceException e) {
			Throwable cause = e.getCause();
			SQLException sqlException = (SQLException)cause;
			if(sqlException.getSQLState().equals("23514"))
				return null;
			throw new ApplicationErrorException(e.getMessage());
		}
	}

	public Store edit(Store store) throws SQLException, ApplicationErrorException {
		try {
			return storeMapper.edit(store);
		} catch(Exception e) {
			throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
		}
	}

	public Boolean checkIfStoreExists() throws ApplicationErrorException {
		try {
			return storeMapper.checkIfStoreExists();
		} catch(Exception e) {
			throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
		}
	}

	public Integer delete(String userName, String adminPassword) throws ApplicationErrorException {
		try {
			User user = userDAO.login(userName, adminPassword);
			if(user == null)
				return - 1;
			else
				return storeMapper.delete(userName, adminPassword);
		} catch(Exception e) {
			throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
		}
	}
}
