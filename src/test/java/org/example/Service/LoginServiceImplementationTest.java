package org.example.Service;

import org.example.DAO.ApplicationErrorException;
import org.example.DAO.StoreDAO;
import org.example.DAO.UserDAO;
import org.example.Entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceImplementationTest {

	@InjectMocks
	private LoginServiceImplementation loginService;
	@Mock
	private UserDAO userDAO;
	@Mock
	private StoreDAO storeDAO;
	@Mock
	private UserService userService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void checkIfInitialSetupShouldCallAndReturnBoolean() throws ApplicationErrorException {
		when(storeDAO.checkIfStoreExists()).thenReturn(true);
		assertTrue(loginService.checkIfInitialSetup());
		verify(storeDAO, times(1)).checkIfStoreExists();
	}

	@Test
	void createUserShouldCallAndReturnUser() throws Exception {
		User user = new User("Admin", "Thomas", "Thomas22112", "Thomas", "Shelby", 8695685968L);
		when(userService.create(user)).thenReturn(user);
		User createdUser = loginService.createUser(user);
		assertNotNull(createdUser);
		assertEquals(user, createdUser);
		verify(userService, times(1)).create(user);
	}

	@Test
	void loginShouldReturnUser() throws ApplicationErrorException {
		String username = "Thomas";
		String passWord = "ThomasShelby";
		when(userDAO.login(username, passWord)).thenReturn(new User("Admin", "Thomas", "ThomasShelby", "Thomas", "Hitler", 9854838448L));
		User loggedInUser = loginService.login(username, passWord);
		assertNotNull(loggedInUser);
		assertEquals(username, loggedInUser.getUserName());
		assertEquals(passWord, loggedInUser.getPassWord());
		assertNotNull(loggedInUser.getUserType());
		verify(userDAO, times(1)).login(username, passWord);
	}
}