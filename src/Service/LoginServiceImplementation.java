package Service;

import DAO.*;
import Entity.User;

import java.sql.SQLException;

public class LoginServiceImplementation implements LoginService {
	private final UserDAO userDAO = new UserDAOImplementation();


	@Override
	public Boolean checkIfInitialSetup() throws SQLException {
		return new StoreDAOImplementation().checkIfStoreExists();
	}

	@Override
	public User createUser(User user) throws UniqueConstraintException, SQLException, ApplicationErrorException, InvalidTemplateException {
		UserService userService = new UserServiceImplementation();
		return userService.create(user);
	}


	@Override
	public String login(String userName, String passWord) throws SQLException, ApplicationErrorException {
		return userDAO.login(userName, passWord);
	}


}
