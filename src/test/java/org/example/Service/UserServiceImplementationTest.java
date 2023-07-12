package org.example.Service;

import org.example.DAO.ApplicationErrorException;
import org.example.DAO.PageCountOutOfBoundsException;
import org.example.DAO.UserDAO;
import org.example.Entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplementationTest {

	@Autowired
	@InjectMocks
	private UserServiceImplementation userService;
	@Mock
	private UserDAO userDAO;

	public static Stream<Arguments> userProvider() {
		return Stream.of(Arguments.of(new User("Some", "Thomas", "Thomas2121", "Thomas", "Shelby", 8954709985L)),
				Arguments.of(new User("Thomas", "Thom12as", "Thomas2121", "Thomas", "Shelby", 8954709985L)),
				Arguments.of(new User("Thomas", "Thomas", "T@21", "Thomas", "Shelby", 8954709985L)),
				Arguments.of(new User("Thomas", "Thomas", "Thomas2121", "Thom243as", "Shelby", 8954709985L)),
				Arguments.of(new User("Thomas", "Thomas", "Thomas2121", "Thomas", "Sh31elby", 8954709985L)),
				Arguments.of(new User("Thomas", "Thomas", "Thomas2121", "Thomas", "Shelby", 231)));
	}

	public static Stream<Arguments> mapProvider() {
		return Stream.of(Arguments.of(new HashMap<>() {{
			put("Pagelength", "10");
			put("Pagenumber", "1");
			put("Attribute", "usertype");
			put("Searchtext", "Sales");
		}}), Arguments.of(new HashMap<>() {{
			put("Pagelength", "10");
			put("Pagenumber", "1");
			put("Attribute", "id");
			put("Searchtext", null);
		}}));
	}

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void createShouldCallAndReturnUser() throws Exception {
		User user = new User("Admin", "Thomas", "Thomas2121", "Thomas", "Shelby", 8954709985L);
		when(userDAO.create(user)).thenReturn(user);
		User createdUser = userService.create(user);
		assertNotNull(createdUser);
		assertEquals(user, createdUser);
		verify(userDAO, times(1)).create(user);
	}

	@ParameterizedTest
	@MethodSource("userProvider")
	void createShouldThrowExceptionWhenFormatMismatch(User user) {
		assertThrows(InvalidTemplateException.class, () -> userService.create(user));
		verifyNoInteractions(userDAO);
	}

	@Test
	void createUserNullValidation() {
		assertThrows(NullPointerException.class, () -> userService.create(null));
		verifyNoInteractions(userDAO);
	}

	@Test
	void countShouldCallAndReturn() throws ApplicationErrorException {
		String attribute = "id";
		String searchText = "40";
		when(userDAO.count(anyString(), anyString())).thenReturn(50);
		Integer count = userService.count(attribute, searchText);
		assertEquals(50, count);
		verify(userDAO, times(1)).count(attribute, searchText);
	}

	@Test
	void listWithSearchTextAloneCallsAndReturnsList() throws ApplicationErrorException, PageCountOutOfBoundsException {
		HashMap<String, String> listAttributes = new HashMap<>();
		listAttributes.put("Pagelength", null);
		listAttributes.put("Pagenumber", null);
		listAttributes.put("Attribute", null);
		listAttributes.put("Searchtext", "Sales");
		when(userDAO.searchList("Sales")).thenReturn(Arrays.asList(new User(), new User()));
		List<User> userList = userService.list(listAttributes);
		assertNotNull(userList);
		verify(userDAO, times(1)).searchList("Sales");
		verifyNoMoreInteractions(userDAO);
	}

	@ParameterizedTest
	@MethodSource("mapProvider")
	void listWithAllAttributesCallsAndReturnsList(HashMap<String, String> listAttributes) throws ApplicationErrorException, PageCountOutOfBoundsException {
		when(userDAO.list(anyString(), any(), anyInt(), anyInt())).thenReturn(Arrays.asList(new User(), new User()));
		assertNotNull(userService.list(listAttributes));
		verify(userDAO, times(1)).list(anyString(), any(), anyInt(), anyInt());
	}

	@Test
	void editShouldCallAndReturnUser() throws Exception {
		User user = new User("Admin", "Thomas", "Thomas2122", "Thomas", "Shelby", 8954709985L);
		when(userDAO.edit(user)).thenReturn(user);
		User editedUser = userService.edit(user);
		assertNotNull(editedUser);
		assertEquals(user, editedUser);
		verify(userDAO, times(1)).edit(user);
	}

	@ParameterizedTest
	@MethodSource("userProvider")
	void editShouldThrowExceptionWhenFormatMismatch(User user) {
		assertThrows(InvalidTemplateException.class, () -> userService.edit(user));
		verifyNoInteractions(userDAO);
	}

	@Test
	void editProductNullValidation() {
		assertThrows(NullPointerException.class, () -> userService.edit(null));
		verifyNoInteractions(userDAO);
	}

	@Test
	void deleteShouldCallAndReturnStatusCode() throws ApplicationErrorException {
		String username = "Thomas";
		when(userDAO.delete(anyString())).thenReturn(1);
		assertEquals(1, userService.delete(username));
		verify(userDAO, times(1)).delete(username);
	}

	@Test
	void deleteUserNullValidation() throws ApplicationErrorException {
		assertEquals(- 1, userService.delete(null));
		verifyNoInteractions(userDAO);
	}
}