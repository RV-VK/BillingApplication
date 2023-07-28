package org.example.RestController;


import org.example.DAO.ApplicationErrorException;
import org.example.DAO.PageCountOutOfBoundsException;
import org.example.Entity.User;
import org.example.Service.InvalidTemplateException;
import org.example.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
	
	@Autowired
	@InjectMocks
	private UserController userController;
	@Mock
	private UserService userService;
	
	@Mock
	private ListAttributeMapValidator validator;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void getUsersShouldCallAndReturnTest() throws PageCountOutOfBoundsException, ApplicationErrorException {
		List<User> userList = new ArrayList<>();
		when(userService.list(any())).thenReturn(userList);
		assertNotNull(userController.getAll());
		verify(userService, times(1)).list(any());
	}

	@Test
	void getUsersShouldCatchAndRethrowPageCountException() throws PageCountOutOfBoundsException, ApplicationErrorException {
		when(userService.list(any())).thenThrow(PageCountOutOfBoundsException.class);
		assertThrows(PageCountOutOfBoundsException.class, () -> userController.getAll());
		verifyNoMoreInteractions(userService);
	}

	@Test
	void getUsersShouldCatchAndRethrowApplicationErrorException() throws PageCountOutOfBoundsException, ApplicationErrorException {
		when(userService.list(any())).thenThrow(ApplicationErrorException.class);
		assertThrows(ApplicationErrorException.class, () -> userController.getAll());
		verifyNoMoreInteractions(userService);
	}

	@Test
	void getUsersByPageLengthShouldCallAndReturn() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		when(userService.list(any())).thenReturn(new ArrayList<>());
		doNothing().when(validator).validate(any(), any());
		assertNotNull(userController.getByPageLength("10"));
		verify(userService, times(1)).list(any());
	}

	@Test
	void getUsersByPageLengthShouldThrowExceptionOnInvalidPageLength() throws InvalidTemplateException {
		doThrow(InvalidTemplateException.class).when(validator).validate(any(), any());
		assertThrows(InvalidTemplateException.class, () -> userController.getByPageLength("S"));
		verifyNoInteractions(userService);
	}

	@Test
	void getUsersByPageLengthAndPageNumberShouldCallAndReturn() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		when(userService.list(any())).thenReturn(new ArrayList<>());
		doNothing().when(validator).validate(any(), any());
		assertNotNull(userController.getByPageLengthAndPageNumber("10", "2"));
		verify(userService, times(1)).list(any());
	}

	@Test
	void getUsersByPageLengthAndPageNumberShouldThrowExceptionOnInvalidPageLengthOrPageNumber() throws InvalidTemplateException {
		doThrow(InvalidTemplateException.class).when(validator).validate(any(), any());
		assertThrows(InvalidTemplateException.class, () -> userController.getByPageLengthAndPageNumber("1", "S"));
		verifyNoInteractions(userService);
	}

	@Test
	void getUsersBySearchTextShouldCallAndReturn() throws PageCountOutOfBoundsException, ApplicationErrorException {
		when(userService.list(any())).thenReturn(new ArrayList<>());
		assertNotNull(userController.getBySearchText(any()));
		verify(userService, times(1)).list(any());
	}

	@Test
	void getUsersByAttributeAndSearchTextShouldCallAndReturn() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		when(userService.list(any())).thenReturn(new ArrayList<>());
		doNothing().when(validator).validate(any(), any());
		assertNotNull(userController.getByAttributeAndSearchText("id", "198"));
		verify(userService, times(1)).list(any());
	}

	@Test
	void getUsersByAttributeAndSearchTextShouldThrowExceptionOnInvalidAttribute() throws InvalidTemplateException {
		doThrow(InvalidTemplateException.class).when(validator).validate(any(), any());
		assertThrows(InvalidTemplateException.class, () -> userController.getByAttributeAndSearchText("some", "search"));
		verifyNoInteractions(userService);
	}

	@Test
	void getUsersByAttributeAndSearchTextWithPageLengthShouldCallAndReturn() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		when(userService.list(any())).thenReturn(new ArrayList<>());
		doNothing().when(validator).validate(any(), any());
		assertNotNull(userController.getByAttributeAndSearchTextWithPageLength("id", "20", "5"));
		verify(userService, times(1)).list(any());
	}

	@Test
	void getUsersByAttributeAndSearchTextWithPageLengthShouldThrowExceptionOnInvalidAttributeOrPageLength() throws InvalidTemplateException {
		doThrow(InvalidTemplateException.class).when(validator).validate(any(), any());
		assertThrows(InvalidTemplateException.class, () -> userController.getByAttributeAndSearchTextWithPageLength("some", "search", "2"));
		assertThrows(InvalidTemplateException.class, () -> userController.getByAttributeAndSearchTextWithPageLength("id", "search", "S"));
		verifyNoInteractions(userService);
	}

	@Test
	void getUsersByAttributeAndSearchTextWithPageLengthAndPageNumberShouldCallAndReturn() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		when(userService.list(any())).thenReturn(new ArrayList<>());
		doNothing().when(validator).validate(any(), any());
		assertNotNull(userController.getByAttributeAndSearchTextWithPageLengthAndPageNumber("id", "20", "5", "1"));
		verify(userService, times(1)).list(any());
	}

	@Test
	void getUsersByAttributeAndSearchTextWithPageLengthAndPageNumberShouldThrowExceptionOnInvalidAttributeOrPageLengthOrPageNumber() throws InvalidTemplateException {
		doThrow(InvalidTemplateException.class).when(validator).validate(any(), any());
		assertThrows(InvalidTemplateException.class, () -> userController.getByAttributeAndSearchTextWithPageLengthAndPageNumber("some", "search", "2", "1"));
		assertThrows(InvalidTemplateException.class, () -> userController.getByAttributeAndSearchTextWithPageLengthAndPageNumber("id", "search", "S", "1"));
		assertThrows(InvalidTemplateException.class, () -> userController.getByAttributeAndSearchTextWithPageLengthAndPageNumber("id", "search", "2", "S"));
		verifyNoInteractions(userService);
	}

	@Test
	void addUserShouldCallAndReturnTest() throws Exception {
		User user = new User("Admin", "manikantan", "manikantan", "Manikantan", "RV", 6393974789L);
		when(userService.create(user)).thenReturn(user);
		assertEquals(user, userController.add(user));
		verify(userService, times(1)).create(user);
	}

	@Test
	void addUserShouldCatchAndRethrowExceptionAndNoMoreInteractions() throws Exception {
		User user = new User("Admin", "manikantan", "manikantan", "Manikantan", "RV", 6393974789L);
		when(userService.create(user)).thenThrow(Exception.class);
		assertThrows(Exception.class, () -> userController.add(user));
		verifyNoMoreInteractions(userService);
	}

	@Test
	void countUserShouldCallAndReturn() throws ApplicationErrorException {
		String attribute = "id";
		String searchText = null;
		when(userService.count(attribute, searchText)).thenReturn(20);
		assertEquals(20, userController.count());
		verify(userService, times(1)).count(attribute, searchText);
	}

	@Test
	void countUserShouldCatchAndRethrowAndNoMoreInteractions() throws ApplicationErrorException {
		String attribute = "id";
		String searchText = null;
		when(userService.count(attribute, searchText)).thenThrow(ApplicationErrorException.class);
		assertThrows(ApplicationErrorException.class, () -> userController.count());
		verifyNoMoreInteractions(userService);
	}


	@Test
	void editUserShouldCallAndReturn() throws Exception {
		User user = new User("Admin", "manikantan", "manikantan", "Manikantan", "RV", 6393974789L);
		when(userService.edit(user)).thenReturn(user);
		assertEquals(user, userController.edit(user));
		verify(userService, times(1)).edit(user);
	}

	@Test
	void editUserShouldCatchAndRethrowException() throws Exception {
		User user = new User("Admin", "manikantan", "manikantan", "Manikantan", "RV", 6393974789L);
		when(userService.edit(user)).thenThrow(Exception.class);
		assertThrows(Exception.class, () -> userController.edit(user));
		verifyNoMoreInteractions(userService);
	}

	@Test
	void editUserShouldThrowExceptionWhenNullReturned() throws Exception {
		User user = new User("Admin", "manikantan", "manikantan", "Manikantan", "RV", 6393974789L);
		when(userService.edit(user)).thenReturn(null);
		assertThrows(InvalidTemplateException.class, () -> userController.edit(user));
		verifyNoMoreInteractions(userService);
	}

	@Test
	void deleteUserShouldCallAndReturn() throws ApplicationErrorException, InvalidTemplateException {
		String parameter = "G01";
		when(userService.delete(parameter)).thenReturn(1);
		assertEquals(1, userController.delete(parameter));
		verify(userService, times(1)).delete(parameter);
	}

	@Test
	void deleteUserShouldCatchANdRethrowException() throws ApplicationErrorException {
		String parameter = "G01";
		when(userService.delete(parameter)).thenThrow(ApplicationErrorException.class);
		assertThrows(ApplicationErrorException.class, () -> userController.delete(parameter));
		verifyNoMoreInteractions(userService);
	}

	@Test
	void deleteUserShouldThrowExceptionWhenStatusCodeZero() throws ApplicationErrorException {
		String parameter = "G01";
		when(userService.delete(parameter)).thenReturn(0);
		assertThrows(InvalidTemplateException.class, () -> userController.delete(parameter));
		verifyNoMoreInteractions(userService);
	}
	
}
