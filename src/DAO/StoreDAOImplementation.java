package DAO;

import Entity.User;
import SQLSession.DBHelper;
import Entity.Store;
import SQLSession.MyBatisSession;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.sql.*;

public class StoreDAOImplementation implements StoreDAO {
	private final SqlSessionFactory sqlSessionFactory = MyBatisSession.getSqlSessionFactory();
	private final SqlSession sqlSession = sqlSessionFactory.openSession();
	private final StoreDAO storeMapper = sqlSession.getMapper(StoreDAO.class);
	private final UserDAO userDAO = new UserDAOImplementation();


	@Override
	public Store create(Store store) throws ApplicationErrorException, SQLException {
		try {
			return storeMapper.create(store);
		} catch(SQLException e) {
//			if(e.getSQLState().equals("23514")) return null;
			throw new ApplicationErrorException(e.getMessage());
		}
	}

	@Override
	public Store edit(Store store) throws SQLException, ApplicationErrorException {
		try {
			return storeMapper.edit(store);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
		}
	}

	public Boolean checkIfStoreExists() throws ApplicationErrorException {
		try {
			return storeMapper.checkIfStoreExists();
		} catch(Exception e) {
			System.out.println(e.getMessage());
			throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
		}
	}

	@Override
	public Integer delete(String userName, String adminPassword) throws ApplicationErrorException {
		try {
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
