package DAO;

import Entity.Store;
import Entity.User;
import SQLSession.MyBatisSession;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.sql.SQLException;

public class StoreDAOImplementation implements StoreDAO {
	private final SqlSessionFactory sqlSessionFactory = MyBatisSession.getSqlSessionFactory();
	private  SqlSession sqlSession;
	private  StoreDAO storeMapper;
	private final UserDAO userDAO = new UserDAOImplementation();


	@Override
	public Store create(Store store) throws ApplicationErrorException, SQLException {
		try {
			sqlSession = sqlSessionFactory.openSession();
			storeMapper = sqlSession.getMapper(StoreDAO.class);
			return storeMapper.create(store);
		} catch(PersistenceException e) {
			Throwable cause = e.getCause();
			SQLException sqlException = (SQLException) cause;
			if(sqlException.getSQLState().equals("23514"))
				return null;
			throw new ApplicationErrorException(e.getMessage());
		}
	}

	@Override
	public Store edit(Store store) throws SQLException, ApplicationErrorException {
		try {
			sqlSession = sqlSessionFactory.openSession();
			storeMapper = sqlSession.getMapper(StoreDAO.class);
			return storeMapper.edit(store);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
		}
	}

	public Boolean checkIfStoreExists() throws ApplicationErrorException {
		try {
			sqlSession = sqlSessionFactory.openSession();
			storeMapper = sqlSession.getMapper(StoreDAO.class);
			return storeMapper.checkIfStoreExists();
		} catch(Exception e) {
			System.out.println(e.getMessage());
			throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
		}
	}

	@Override
	public Integer delete(String userName, String adminPassword) throws ApplicationErrorException {
		try {
			sqlSession = sqlSessionFactory.openSession();
			storeMapper = sqlSession.getMapper(StoreDAO.class);
			User user = userDAO.login(userName, adminPassword);
			if(user==null)
				return -1;
			else
				return storeMapper.delete(userName, adminPassword);
		} catch(Exception e) {
			e.printStackTrace();
			throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
		}
	}
}
