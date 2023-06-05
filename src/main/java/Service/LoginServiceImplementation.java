package Service;

import DAO.ApplicationErrorException;
import DAO.StoreDAO;
import DAO.UserDAO;
import Entity.User;

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
	public User login(String userName, String passWord) throws ApplicationErrorException {
		return userDAO.login(userName, passWord);
	}


}
