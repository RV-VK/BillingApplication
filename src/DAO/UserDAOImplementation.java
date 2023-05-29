package DAO;

import SQLSession.DBHelper;
import Entity.User;
import SQLSession.MyBatisSession;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImplementation implements UserDAO {
	private final Connection userConnection = DBHelper.getConnection();
	private final SqlSessionFactory sqlSessionFactory= MyBatisSession.getSqlSessionFactory();
	private final SqlSession sqlSession =sqlSessionFactory.openSession();
	private final UserDAO userMapper  = sqlSession.getMapper(UserDAO.class);
	private List<User> userList = new ArrayList<>();

	@Override
	public User create(User user) throws SQLException, ApplicationErrorException, UniqueConstraintException {
		try{
		User createdUser = userMapper.create(user);
		sqlSession.commit();
		sqlSession.close();
		return createdUser;
		} catch( SQLException e) {
			handleException(e);
			return null;
		}
	}

	/**
	 * Private method to convert SQL Exception to user readable messages.
	 *
	 * @param exception Exception Object.
	 * @throws UniqueConstraintException Custom Exception to convey Unique constraint Violation in SQL table.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	private void handleException(SQLException exception) throws UniqueConstraintException, ApplicationErrorException {
		if(exception.getSQLState().equals("23505")) {
			throw new UniqueConstraintException(">> UserName must be unique!! The username you have entered already exists!!");
		}
		throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
	}

	@Override
	public Integer count() throws ApplicationErrorException {
		try {
			return userMapper.count();
		} catch(Exception e) {
			throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
		}
	}


	public List<User> searchList(String searchText) throws ApplicationErrorException {
		try {
			return userMapper.searchList(searchText);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
		}
	}


	@Override
	public List<User> list(String attribute, String searchText, int pageLength, int offset) throws ApplicationErrorException {
		try {
			return userMapper.list(attribute, searchText, pageLength, offset);
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}

	private void checkPagination(int count, int offset, int pageLength) throws PageCountOutOfBoundsException {
		if(count <= offset) {
			int pageCount;
			if(count % pageLength == 0) pageCount = count / pageLength;
			else pageCount = (count / pageLength) + 1;
			throw new PageCountOutOfBoundsException(">> Requested Page doesnt Exist!!\n>> Existing Pagecount with given pagination " + pageCount);
		}
	}


	@Override
	public User edit(User user) throws SQLException, ApplicationErrorException, UniqueConstraintException {
		try {
			User editedUser = userMapper.edit(user);
			sqlSession.commit();
			sqlSession.close();
			return editedUser;
		} catch(SQLException e) {
			handleException(e);
			return null;
		}
	}


	@Override
	public Integer delete(String username) throws ApplicationErrorException {
		try {
			int rowsAffected = userMapper.delete(username);
			sqlSession.commit();
			sqlSession.close();
			return rowsAffected;
		} catch(Exception e) {
			e.printStackTrace();
			throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
		}
	}



	@Override
	public User login(String userName, String passWord) throws ApplicationErrorException {
		try {
			User user=userMapper.login(userName,passWord);
			if(user!=null && user.getPassWord().equals(passWord))
				return user;
			else
				return null;
		} catch(SQLException e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}
}
