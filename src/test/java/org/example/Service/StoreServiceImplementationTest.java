package org.example.Service;

import org.example.DAO.ApplicationErrorException;
import org.example.DAO.StoreDAO;
import org.example.Entity.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StoreServiceImplementationTest {

	@Autowired
	@InjectMocks
	private StoreServiceImplementation storeService;
	@Mock
	private StoreDAO storeDAO;

	public static Stream<Arguments> storeProvider() {
		return Stream.of(Arguments.of(new Store("A", 8595449589L, "123-First Street Extension, CBE.", "1234567890ABCDE")),
				Arguments.of(new Store("ABC Stores", 8599589L, "123-First Street Extension, CBE.", "1234567890ABCDE")),
				Arguments.of(new Store("ABC Stores", 8595449589L, "123-First Street Extension, CBE.", "123890ABCDE")));
	}

	public static Stream<Arguments> storeNullProvider() {
		return Stream.of(Arguments.of((Store)null), Arguments.of(new Store(null, 8595449589L, "123-First Street Extension, CBE.", "1234567890ABCDE")),
				Arguments.of(new Store("ABC Stores", null, "123-First Street Extension, CBE.", "1234567890ABCDE")),
				Arguments.of(new Store("ABC Stores", 8595449589L, null, "123890ABCDE")),
				Arguments.of(new Store("ABC Stores", 8595449589L, "123-First Street Extension, CBE.", null)));
	}

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void createShouldCallAndReturnStore() throws SQLException, ApplicationErrorException, InvalidTemplateException {
		Store store = new Store("ABC Stores", 8595449589L, "123-First Street Extension, CBE.", "1234567890ABCDE");
		when(storeDAO.create(store)).thenReturn(store);
		Store createdStore = storeService.create(store);
		assertNotNull(createdStore);
		assertEquals(store, createdStore);
		verify(storeDAO, times(1)).create(store);
	}

	@ParameterizedTest
	@MethodSource("storeProvider")
	void createShouldThrowExceptionWhenFormatMismatch(Store store) {
		assertThrows(InvalidTemplateException.class, () -> storeService.create(store));
		verifyNoInteractions(storeDAO);
	}

	@ParameterizedTest
	@MethodSource("storeNullProvider")
	void createStoreNullValidation(Store store) {
		assertThrows(NullPointerException.class, () -> storeService.create(store));
		verifyNoInteractions(storeDAO);
	}

	@Test
	void viewShouldCallAndReturn() throws ApplicationErrorException {
		when(storeDAO.view()).thenReturn(new Store());
		assertNotNull(storeService.view());
		verify(storeDAO, times(1)).view();
	}

	@Test
	void editShouldCallAndReturnStore() throws SQLException, ApplicationErrorException, InvalidTemplateException {
		Store store = new Store("ABCD Stores", 8595449589L, "123-First Street Extension, CBE.", "1234567890ABCDE");
		when(storeDAO.edit(store)).thenReturn(store);
		Store editedStore = storeService.edit(store);
		assertNotNull(editedStore);
		assertEquals(store, editedStore);
		verify(storeDAO, times(1)).edit(store);
	}

	@ParameterizedTest
	@MethodSource("storeProvider")
	void editShouldThrowExceptionWhenFormatMismatch(Store store) {
		assertThrows(InvalidTemplateException.class, () -> storeService.edit(store));
		verifyNoInteractions(storeDAO);
	}

	@ParameterizedTest
	@MethodSource("storeNullProvider")
	void editStoreNullValidation(Store store) {
		assertThrows(NullPointerException.class, () -> storeService.edit(store));
		verifyNoInteractions(storeDAO);
	}

	@Test
	void deleteShouldCallAndReturnStatusCode() throws ApplicationErrorException {
		String userName = "Thomas";
		String adminPassWord = "ThomasShelby";
		when(storeDAO.delete(anyString(), anyString())).thenReturn(1);
		assertEquals(1, storeService.delete(userName, adminPassWord));
		verify(storeDAO, times(1)).delete(userName, adminPassWord);
	}

	@Test
	void deleteUserNullValidation() {
		assertThrows(NullPointerException.class, () -> storeService.delete(null, null));
		assertThrows(NullPointerException.class, () -> storeService.delete("username", null));
		assertThrows(NullPointerException.class, () -> storeService.delete(null, "password"));
		verifyNoInteractions(storeDAO);
	}
}