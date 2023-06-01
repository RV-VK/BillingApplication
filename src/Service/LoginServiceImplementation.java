package Service;

import DAO.*;
import Entity.User;

import java.sql.SQLException;

public class LoginServiceImplementation implements LoginService {
	private final UserDAO userDAO = new UserDAO();


	@Override
	public Boolean checkIfInitialSetup() throws ApplicationErrorException {
		return new StoreDAO().checkIfStoreExists();
	}

	@Override
	public User createUser(User user) throws Exception {
		UserService userService = new UserServiceImplementation();
		return userService.create(user);
	}


	@Override
	public User login(String userName, String passWord) throws SQLException, ApplicationErrorException {
		return userDAO.login(userName, passWord);
	}


}
