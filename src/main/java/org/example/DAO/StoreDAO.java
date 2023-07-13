package org.example.DAO;

import org.apache.ibatis.exceptions.PersistenceException;
import org.example.Entity.Store;
import org.example.Entity.User;
import org.example.Mapper.StoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class StoreDAO {
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private StoreMapper storeMapper;

	/**
	 * This method creates an Entry in the Store table.
	 *
	 * @param store Input Store entity.
	 * @return Store - Created store.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 * @throws SQLException              Exception thrown based on SQL syntax.
	 */
	public Store create(Store store) throws ApplicationErrorException, SQLException {
		try {
			return storeMapper.create(store);
		} catch(Exception e) {
			Throwable cause = e.getCause();
			SQLException sqlException = (SQLException)cause;
			if(sqlException.getSQLState().equals("23514")) return null;
			throw new ApplicationErrorException(e.getMessage());
		}
	}

	/**
	 * This method updates the attributes of the Store entry in the Store table.
	 *
	 * @param store Updated Store entity.
	 * @return Store - Resulted store.
	 * @throws SQLException              Exception thrown based on SQL syntax.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	public Store edit(Store store) throws SQLException, ApplicationErrorException {
		try {
			return storeMapper.edit(store);
		} catch(Exception e) {
			throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
		}
	}

	public Store view() throws ApplicationErrorException {
		try{
			return storeMapper.view();
		}catch(Exception e) {
			e.printStackTrace();
			throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
		}
	}

	/**
	 * This is a helper method that verifies whether a store Entry exists in the Store table to differentiate initial setup and Login.
	 *
	 * @return Boolean - status
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	public Boolean checkIfStoreExists() throws ApplicationErrorException {
		try {
			return storeMapper.checkIfStoreExists();
		} catch(Exception e) {
			throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
		}
	}

	/**
	 * This method deleted the store Entry in the Store table.
	 *
	 * @param userName      Username of the Current User.
	 * @param adminPassword Password String to allow to delete store.
	 * @return statusCode - Integer.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	public Integer delete(String userName, String adminPassword) throws ApplicationErrorException {
		try {
			Integer rowsAffected;
			User user = userDAO.login(userName, adminPassword);
			if(user == null) rowsAffected = - 1;
			else rowsAffected = storeMapper.delete(userName, adminPassword);
			return rowsAffected;
		} catch(Exception e) {
			throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
		}
	}
}
