package org.example.Service;

import org.example.CLIController.AppDependencyConfig;
import org.example.DAO.ApplicationErrorException;
import org.example.DAO.StoreDAO;
import org.example.DAO.UserDAO;
import org.example.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImplementation implements LoginService {
	@Autowired
	private UserDAO userDAO;
	private ApplicationContext context;

	@Override
	public Boolean checkIfInitialSetup() throws ApplicationErrorException {
		context = new AnnotationConfigApplicationContext(AppDependencyConfig.class);
		StoreDAO storeDAO = context.getBean(StoreDAO.class);
		return storeDAO.checkIfStoreExists();
	}

	@Override
	public User createUser(User user) throws Exception {
		context = new AnnotationConfigApplicationContext(AppDependencyConfig.class);
		UserService userService = context.getBean(UserService.class);
		return userService.create(user);
	}


	@Override
	public User login(String userName, String passWord) throws ApplicationErrorException {
		return userDAO.login(userName, passWord);
	}


}
