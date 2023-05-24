package DAO;

import DBConnection.DBHelper;
import Entity.Store;

import java.sql.*;

public class StoreDAOImplementation implements StoreDAO {
	private final Connection storeConnection = DBHelper.getConnection();


	@Override
	public Store create(Store store) throws ApplicationErrorException, SQLException {
		try {
			PreparedStatement unitCreateStatement = storeConnection.prepareStatement("INSERT INTO STORE(NAME,PHONENUMBER,ADDRESS,GSTNUMBER) VALUES (?,?,?,?) RETURNING *");
			setParameters(unitCreateStatement, store);
			ResultSet storeCreateResultSet = unitCreateStatement.executeQuery();
			storeCreateResultSet.next();
			return getStoreFromResultSet(storeCreateResultSet);
		} catch(SQLException e) {
			storeConnection.rollback();
			if(e.getSQLState().equals("23514")) return null;
			else throw new ApplicationErrorException(e.getMessage());
		}
	}

	private void setParameters(PreparedStatement statement, Store store) throws SQLException {
		statement.setString(1, store.getName());
		statement.setLong(2, store.getPhoneNumber());
		statement.setString(3, store.getAddress());
		statement.setString(4, store.getGstCode());
	}

	private Store getStoreFromResultSet(ResultSet resultSet) throws SQLException {
		return new Store(resultSet.getString(2), resultSet.getLong(3), resultSet.getString(4), resultSet.getString(5));
	}


	@Override
	public Store edit(Store store) throws SQLException, ApplicationErrorException {
		try {
			String editQuery = "UPDATE STORE SET NAME= COALESCE(?,NAME),PHONENUMBER= COALESCE(?,PHONENUMBER),ADDRESS= COALESCE(?,ADDRESS),GSTNUMBER=COALESCE(?,GSTNUMBER) RETURNING *";
			PreparedStatement editStatement = storeConnection.prepareStatement(editQuery);
			setParameters(editStatement, store);
			if(store.getPhoneNumber() == 0) {
				editStatement.setNull(2, Types.BIGINT);
			} else {
				editStatement.setLong(2, store.getPhoneNumber());
			}
			ResultSet editStoreResultSet = editStatement.executeQuery();
			editStoreResultSet.next();
			return getStoreFromResultSet(editStoreResultSet);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
		}
	}


	@Override
	public Integer delete(String adminPassword) throws ApplicationErrorException {
		try {
			Statement storeExistenceCheckStatement = storeConnection.createStatement();
			ResultSet storeResultSet = storeExistenceCheckStatement.executeQuery("SELECT * FROM STORE");
			if(storeResultSet.next()) {
				Statement passwordCheckStatement = storeConnection.createStatement();
				ResultSet adminPasswordResultSet = passwordCheckStatement.executeQuery("SELECT PASSWORD FROM USERS WHERE USERTYPE='Admin'");
				if(adminPasswordResultSet.next()) {
					String originalPassword = adminPasswordResultSet.getString(1);
					if(originalPassword.equals(adminPassword)) {
						Statement deleteStatement = storeConnection.createStatement();
						deleteStatement.execute("DELETE FROM STORE");
						return 1;
					} else {
						return - 1;
					}
				} else {
					return - 1;
				}
			} else {
				return 0;
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
		}
	}
}
