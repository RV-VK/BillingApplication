package DAO;

import DBConnection.DBHelper;
import Entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImplementation implements UserDAO {
	private final Connection userConnection = DBHelper.getConnection();
	private List<User> userList = new ArrayList<>();


	@Override
	public User create(User user)
			throws SQLException, ApplicationErrorException, UniqueConstraintException {
		try {
			PreparedStatement userCreateStatement =
					userConnection.prepareStatement(
							"INSERT INTO USERS(USERNAME,USERTYPE,PASSWORD,FIRSTNAME,LASTNAME,PHONENUMBER) VALUES (?,?,?,?,?,?) RETURNING *");
			setParameters(userCreateStatement, user);
			ResultSet userCreateResultSet = userCreateStatement.executeQuery();
			userCreateResultSet.next();
			return getUserFromResultSet(userCreateResultSet);
		} catch(SQLException e) {
			handleException(e);
			return null;
		}
	}

	/**
	 * Private method to set User parameters for Prepared Statement.
	 *
	 * @param statement Statement to be Set.
	 * @param user      User Entity
	 * @throws SQLException Exception thrown based on SQL syntax.
	 */
	private void setParameters(PreparedStatement statement, User user) throws SQLException {
		statement.setString(1, user.getUserName());
		statement.setString(2, user.getUserType());
		statement.setString(3, user.getPassWord());
		statement.setString(4, user.getFirstName());
		statement.setString(5, user.getLastName());
		statement.setLong(6, user.getPhoneNumber());
	}

	/**
	 * Private method to assists user construction from resultset.
	 *
	 * @param resultSet User resultset.
	 * @return User entity.
	 * @throws SQLException Exception thrown based on SQL syntax.
	 */
	private User getUserFromResultSet(ResultSet resultSet) throws SQLException {
		return new User(
				resultSet.getInt(1),
				resultSet.getString(3),
				resultSet.getString(2),
				resultSet.getString(4),
				resultSet.getString(5),
				resultSet.getString(6),
				resultSet.getLong(7));
	}

	/**
	 * Private method to convert SQL Exception to user readable messages.
	 *
	 * @param exception Exception Object.
	 * @throws UniqueConstraintException Custom Exception to convey Unique constraint Violation in SQL table.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	private void handleException(SQLException exception)
			throws UniqueConstraintException, ApplicationErrorException {
		if(exception.getSQLState().equals("23505")) {
			throw new UniqueConstraintException(
					">> UserName must be unique!! The username you have entered already exists!!");
		}
		throw new ApplicationErrorException(
				"Application has went into an Error!!!\n Please Try again");
	}


	@Override
	public Integer count() throws ApplicationErrorException {
		try {
			Statement countStatement = userConnection.createStatement();
			ResultSet countResultSet = countStatement.executeQuery("SELECT COUNT(ID) FROM USERS");
			int count = 0;
			while(countResultSet.next()) {
				count = countResultSet.getInt(1);
			}
			return count;
		} catch(Exception e) {
			throw new ApplicationErrorException(
					"Application has went into an Error!!!\n Please Try again");
		}
	}


	public List<User> list(String searchText) throws ApplicationErrorException {
		try {
			Statement listStatement = userConnection.createStatement();
			String listQuery =
					"SELECT * FROM USERS WHERE USERTYPE ILIKE '"
							+ searchText
							+ "' OR USERNAME ILIKE '"
							+ searchText
							+ "' OR FIRSTNAME ILIKE '"
							+ searchText
							+ "' OR LASTNAME ILIKE '"
							+ searchText
							+ "' OR PASSWORD ILIKE '"
							+ searchText
							+ "' OR CAST(PHONENUMBER AS TEXT) ILIKE '"
							+ searchText
							+ "' OR CAST(ID AS TEXT) ILIKE '"
							+ searchText
							+ "'";
			ResultSet listresultSet = listStatement.executeQuery(listQuery);
			return listHelper(listresultSet);
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			throw new ApplicationErrorException(
					"Application has went into an Error!!!\n Please Try again");
		}
	}


	@Override
	public List<User> list(String attribute, String searchText, int pageLength, int offset)
			throws ApplicationErrorException {
		try {
			String EntryCount = "SELECT COUNT(*) OVER() FROM USERS WHERE " + attribute + "= COALESCE(" + searchText + "," + attribute + ")" + " ORDER BY ID";
			String listQuery = "SELECT * FROM USERS WHERE " + attribute + "= COALESCE(" + searchText + "," + attribute + ")" + " ORDER BY ID LIMIT " + pageLength + "  OFFSET " + offset;
			PreparedStatement countStatement = userConnection.prepareStatement(EntryCount);
			PreparedStatement listStatement = userConnection.prepareStatement(listQuery);
			ResultSet countResultSet = countStatement.executeQuery();
			if(countResultSet.next() && countResultSet.getInt(1) <= offset)
				checkPagination(countResultSet.getInt(1), offset, pageLength);
			ResultSet listResultSet = listStatement.executeQuery();
			return listHelper(listResultSet);
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}

	private void checkPagination(int count, int offset, int pageLength) throws PageCountOutOfBoundsException {
		if(count <= offset) {
			int pageCount;
			if(count % pageLength == 0)
				pageCount = count / pageLength;
			else
				pageCount = (count / pageLength) + 1;
			throw new PageCountOutOfBoundsException(
					">> Requested Page doesnt Exist!!\n>> Existing Pagecount with given pagination "
							+ pageCount);
		}
	}

	/**
	 * This method serves the ListDAO function.
	 *
	 * @param resultSet ListQuery results.
	 * @return List - Users
	 * @throws SQLException Exception thrown based on SQL syntax.
	 */
	private List<User> listHelper(ResultSet resultSet) throws SQLException {
		while(resultSet.next()) {
			userList.add(getUserFromResultSet(resultSet));
		}
		return userList;
	}


	@Override
	public User edit(User user)
			throws SQLException, ApplicationErrorException, UniqueConstraintException {
		try {
			String editQuery =
					"UPDATE USERS SET USERNAME= COALESCE(?,USERNAME),USERTYPE= COALESCE(?,USERTYPE),PASSWORD= COALESCE(?,PASSWORD),FIRSTNAME= COALESCE(?,FIRSTNAME),LASTNAME= COALESCE(?,LASTNAME),PHONENUMBER=COALESCE(?,PHONENUMBER) WHERE ID=? RETURNING *";
			PreparedStatement editStatement = userConnection.prepareStatement(editQuery);
			setParameters(editStatement, user);
			if(user.getPhoneNumber() == 0) {
				editStatement.setNull(6, Types.BIGINT);
			} else {
				editStatement.setLong(6, user.getPhoneNumber());
			}
			editStatement.setInt(7, user.getId());
			ResultSet editUserResultSet = editStatement.executeQuery();
			editUserResultSet.next();
			return getUserFromResultSet(editUserResultSet);
		} catch(SQLException e) {
			handleException(e);
			return null;
		}
	}


	@Override
	public Integer delete(String username) throws ApplicationErrorException {
		try {
			Statement deleteStatement = userConnection.createStatement();
			if(deleteStatement.executeUpdate("DELETE FROM USERS WHERE USERNAME='" + username + "'")
					> 0) {
				return 1;
			} else {
				return - 1;
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw new ApplicationErrorException(
					"Application has went into an Error!!!\n Please Try again");
		}
	}


	public Boolean checkIfInitialSetup() throws SQLException {
		ResultSet resultSet =
				userConnection.createStatement().executeQuery("SELECT COUNT(ID) FROM USERS WHERE USERTYPE='Admin'");
		resultSet.next();
		return resultSet.getInt(1) == 0;
	}


	@Override
	public String login(String userName, String passWord)
			throws ApplicationErrorException {
		try {
			ResultSet resultSet = userConnection
					.createStatement()
					.executeQuery("SELECT PASSWORD,USERTYPE FROM USERS WHERE USERNAME='" + userName + "'");
			if(resultSet.next()) {
				if(resultSet.getString(1).equals(passWord)) return resultSet.getString(2);
				else return null;
			} else return null;
		} catch(SQLException e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}
}
