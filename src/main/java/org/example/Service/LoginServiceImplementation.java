package org.example.Service;

import org.example.DAO.ApplicationErrorException;
import org.example.DAO.StoreDAO;
import org.example.DAO.UserDAO;
import org.example.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImplementation implements LoginService {
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private StoreDAO storeDAO;
	@Autowired
	private UserService userService;

	@Override
	public Boolean checkIfInitialSetup() throws ApplicationErrorException {
		return storeDAO.checkIfStoreExists();
	}

	@Override
	public User createUser(User user) throws Exception {
		return userService.create(user);
	}


	@Override
	public User login(String userName, String passWord) throws ApplicationErrorException {
		return userDAO.login(userName, passWord);
	}


}
