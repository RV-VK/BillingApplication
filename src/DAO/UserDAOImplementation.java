package DAO;

import Entity.User;
import SQLSession.MyBatisSession;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.sql.SQLException;
import java.util.List;

public class UserDAOImplementation implements UserDAO {
	private final SqlSessionFactory sqlSessionFactory = MyBatisSession.getSqlSessionFactory();
	private final SqlSession sqlSession = sqlSessionFactory.openSession();
	private final UserDAO userMapper = sqlSession.getMapper(UserDAO.class);

	@Override
	public User create(User user) throws SQLException, ApplicationErrorException, UniqueConstraintException {
		try {
			return userMapper.create(user);
		} catch(PersistenceException e) {
			Throwable cause = e.getCause();
			handleException((SQLException) cause);
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
	public Integer count(String attribute, Object searchText) throws ApplicationErrorException {
		try {
			return userMapper.count(attribute, searchText);
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
	public List<User> list(String attribute, Object searchText, int pageLength, int offset) throws ApplicationErrorException {
		try {
			if(searchText!= null && String.valueOf(searchText).matches("^\\d+(\\.\\d+)?$")) {
				Double numericParameter = Double.parseDouble((String)searchText);
				Integer count = userMapper.count(attribute,numericParameter);
				checkPagination(count, offset, pageLength);
				return userMapper.list(attribute, numericParameter, pageLength, offset);
			} else {
				Integer count = userMapper.count(attribute, searchText);
				checkPagination(count, offset, pageLength);
				return userMapper.list(attribute, searchText, pageLength, offset);
			}
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}

	private void checkPagination(int count, int offset, int pageLength) throws PageCountOutOfBoundsException {
		if(count <= offset && count != 0) {
			int pageCount;
			if(count % pageLength == 0) pageCount = count / pageLength;
			else pageCount = (count / pageLength) + 1;
			throw new PageCountOutOfBoundsException(">> Requested Page doesnt Exist!!\n>> Existing Pagecount with given pagination " + pageCount);
		}
	}


	@Override
	public User edit(User user) throws SQLException, ApplicationErrorException, UniqueConstraintException {
		try {
			return userMapper.edit(user);
		} catch(PersistenceException e) {
			Throwable cause = e.getCause();
			handleException((SQLException) cause);
			return null;
		}
	}


	@Override
	public Integer delete(String username) throws ApplicationErrorException {
		try {
			return userMapper.delete(username);
		} catch(Exception e) {
			e.printStackTrace();
			throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
		}
	}


	@Override
	public User login(String userName, String passWord) throws ApplicationErrorException {
		try {
			User user = userMapper.login(userName, passWord);
			if(user != null && user.getPassWord().equals(passWord)) return user;
			else return null;
		} catch(SQLException e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}
}
