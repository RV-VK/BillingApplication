package DAO;

import Entity.User;
import Mapper.UserMapper;
import SQLSession.MyBatisSession;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.sql.SQLException;
import java.util.List;

public class UserDAO {
	private final SqlSessionFactory sqlSessionFactory = MyBatisSession.getSqlSessionFactory();
	private SqlSession sqlSession;
	private UserMapper userMapper;

	/**
	 * This method Creates a User Entry in the User table
	 *
	 * @param user Input Object
	 * @return User Object - created
	 * @throws Exception Throws Variable Exceptions namely SQLException, UnitCodeViolationException, UniqueConstraintException.
	 */
	public User create(User user) throws Exception {
		try {
			sqlSession = sqlSessionFactory.openSession();
			userMapper = sqlSession.getMapper(UserMapper.class);
			User createdUser = userMapper.create(user);
			sqlSession.close();
			return createdUser;
		} catch(PersistenceException e) {
			Throwable cause = e.getCause();
			throw handleException((SQLException)cause);
		}
	}

	/**
	 * Private method to convert SQL Exception to user readable messages.
	 *
	 * @param exception Exception Object.
	 * @throws UniqueConstraintException Custom Exception to convey Unique constraint Violation in SQL table.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	private Exception handleException(SQLException exception) throws UniqueConstraintException, ApplicationErrorException {
		if(exception.getSQLState().equals("23505")) {
			throw new UniqueConstraintException(">> UserName must be unique!! The username you have entered already exists!!");
		}
		throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
	}

	/**
	 * This method counts the number od entries in the user table.
	 *
	 * @param attribute  Column to be counted.
	 * @param searchText Field to be counted.
	 * @return count
	 * @throws ApplicationErrorException Exception thrown due to persistence problems
	 */
	public Integer count(String attribute, Object searchText) throws ApplicationErrorException {
		try {
			sqlSession = sqlSessionFactory.openSession();
			userMapper = sqlSession.getMapper(UserMapper.class);
			Integer count = userMapper.count(attribute, searchText);
			sqlSession.close();
			return count;
		} catch(Exception e) {
			throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
		}
	}

	/**
	 * This method Lists the records in the user table based on a given Search-text.
	 *
	 * @param searchText The search-text that must be found.
	 * @return List of Users
	 * @throws ApplicationErrorException Exception thrown due to persistence problems.
	 */
	public List<User> searchList(String searchText) throws ApplicationErrorException {
		try {
			sqlSession = sqlSessionFactory.openSession();
			userMapper = sqlSession.getMapper(UserMapper.class);
			List<User> userList = userMapper.searchList(searchText);
			sqlSession.close();
			return userList;
		} catch(Exception e) {
			System.out.println(e.getMessage());
			throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
		}
	}

	/**
	 * This method lists the users in the user table based on the given searchable attribute
	 * and its corresponding search-text formatted in a pageable manner.
	 *
	 * @param attribute  The attribute to be looked upon
	 * @param searchText The search-text to be found.
	 * @param pageLength The number of entries that must be listed.
	 * @param offset     The Page number that has to be listed.
	 * @return List of Users
	 * @throws ApplicationErrorException Exception thrown due to persistence problems
	 */
	public List<User> list(String attribute, Object searchText, int pageLength, int offset) throws ApplicationErrorException {
		try {
			List<User> userList;
			sqlSession = sqlSessionFactory.openSession();
			userMapper = sqlSession.getMapper(UserMapper.class);
			if(searchText != null && String.valueOf(searchText).matches("^\\d+(\\.\\d+)?$")) {
				Double numericParameter = Double.parseDouble((String)searchText);
				Integer count = userMapper.count(attribute, numericParameter);
				checkPagination(count, offset, pageLength);
				userList = userMapper.list(attribute, numericParameter, pageLength, offset);
			} else {
				Integer count = userMapper.count(attribute, searchText);
				checkPagination(count, offset, pageLength);
				userList = userMapper.list(attribute, searchText, pageLength, offset);
			}
			sqlSession.close();
			return userList;
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}

	/**
	 * Private method to check whether the given Pagenumber is Valid or Not exists.
	 *
	 * @param count      Total Count of entries.
	 * @param offset     Index from which the Entries are requested.
	 * @param pageLength Length for Each page.
	 * @throws PageCountOutOfBoundsException Exception thrown in a pageable list function if a
	 *                                       non-existing page is prompted.
	 */
	private void checkPagination(int count, int offset, int pageLength) throws PageCountOutOfBoundsException {
		if(count <= offset && count != 0) {
			int pageCount;
			if(count % pageLength == 0) pageCount = count / pageLength;
			else pageCount = (count / pageLength) + 1;
			throw new PageCountOutOfBoundsException(">> Requested Page doesnt Exist!!\n>> Existing Pagecount with given pagination " + pageCount);
		}
	}

	/**
	 * This method updates the attributes of the User entry in the user table.
	 *
	 * @param user The updated User Entry.
	 * @return User - Resulted User Entity.
	 * @throws Exception Throws Variable Exceptions namely SQLException, UnitCodeViolationException, UniqueConstraintException.
	 */
	public User edit(User user) throws Exception {
		try {
			sqlSession = sqlSessionFactory.openSession();
			userMapper = sqlSession.getMapper(UserMapper.class);
			User editedUser = userMapper.edit(user);
			sqlSession.close();
			return editedUser;
		} catch(PersistenceException e) {
			Throwable cause = e.getCause();
			throw handleException((SQLException)cause);
		}
	}

	/**
	 * This method deleted an entry in the User table based on the given parameter.
	 *
	 * @param username Username to be deleted.
	 * @return Integer - resultCode
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	public Integer delete(String username) throws ApplicationErrorException {
		try {
			sqlSession = sqlSessionFactory.openSession();
			userMapper = sqlSession.getMapper(UserMapper.class);
			Integer rowsAffected = userMapper.delete(username);
			sqlSession.close();
			return rowsAffected;
		} catch(Exception e) {
			e.printStackTrace();
			throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
		}
	}

	/**
	 * This method verifies whether the input username and password matches in the user table to enable login for the users.
	 *
	 * @param userName Unique entry username of the user
	 * @param passWord Password string of the user
	 * @return String - Usertype or null
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	public User login(String userName, String passWord) throws ApplicationErrorException {
		try {
			sqlSession = sqlSessionFactory.openSession();
			userMapper = sqlSession.getMapper(UserMapper.class);
			User user = userMapper.login(userName, passWord);
			sqlSession.close();
			if(user != null && user.getPassWord().equals(passWord)) return user;
			else return null;
		} catch(SQLException e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}
}
