package org.example.RestController;

import org.example.DAO.ApplicationErrorException;
import org.example.Entity.Store;
import org.example.Service.InvalidTemplateException;
import org.example.Service.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StoreControllerTest {
	@Autowired
	@InjectMocks
	private StoreController storeController;
	@Mock
	private StoreService storeService;
	@Mock
	private ListAttributeMapValidator validator;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void storeViewShouldCallAndReturn() throws ApplicationErrorException {
		when(storeService.view()).thenReturn(new Store());
		assertNotNull(storeController.view());
		verify(storeService, times(1)).view();
	}

	@Test
	public void storeViewShouldCatchAndRethrow() throws ApplicationErrorException {
		when(storeService.view()).thenThrow(ApplicationErrorException.class);
		assertThrows(ApplicationErrorException.class, () -> storeController.view());
		verifyNoMoreInteractions(storeService);
	}

	@Test
	public void addStoreShouldCallAndReturn() throws SQLException, ApplicationErrorException, InvalidTemplateException {
		Store store = new Store("ABCStores", 8594354848L, "123/B North Street", "1234567890ABCDE");
		when(storeService.create(store)).thenReturn(store);
		assertEquals(store, storeController.add(store));
		verify(storeService, times(1)).create(store);
	}

	@Test
	public void addStoreShouldCatchAndRethrowException() throws SQLException, ApplicationErrorException, InvalidTemplateException {
		Store store = new Store("ABCStores", 8594354848L, "123/B North Street", "1234567890ABCDE");
		when(storeService.create(store)).thenThrow(ApplicationErrorException.class);
		assertThrows(ApplicationErrorException.class, () -> storeController.add(store));
		verifyNoMoreInteractions(storeService);
	}

	@Test
	public void addStoreShouldCallThrowIfNullReturned() throws SQLException, ApplicationErrorException, InvalidTemplateException {
		Store store = new Store("ABCStores", 8594354848L, "123/B North Street", "1234567890ABCDE");
		when(storeService.create(store)).thenReturn(null);
		assertThrows(InvalidTemplateException.class, () -> storeController.add(store));
		verifyNoMoreInteractions(storeService);
	}

	@Test
	public void editStoreShouldCallAndReturn() throws SQLException, ApplicationErrorException, InvalidTemplateException {
		Store store = new Store("ABCStores", 8594354848L, "123/B North Street", "1234567890ABCDE");
		when(storeService.edit(store)).thenReturn(store);
		assertEquals(store, storeController.edit(store));
		verify(storeService, times(1)).edit(store);
	}

	@Test
	public void editStoreShouldCatchAndRethrowException() throws SQLException, ApplicationErrorException, InvalidTemplateException {
		Store store = new Store("ABCStores", 8594354848L, "123/B North Street", "1234567890ABCDE");
		when(storeService.edit(store)).thenThrow(ApplicationErrorException.class);
		assertThrows(ApplicationErrorException.class, () -> storeController.edit(store));
		verifyNoMoreInteractions(storeService);
	}

	@Test
	public void deleteStoreShouldCallAndReturn() throws ApplicationErrorException, InvalidTemplateException {
		String userName = "manikantan";
		String passWord = "manikantan";
		when(storeService.delete(userName, passWord)).thenReturn(1);
		assertEquals(1, storeController.delete(userName, passWord));
		verify(storeService, times(1)).delete(userName, passWord);
	}

	@Test
	public void deleteStoreShouldCatchAndRethrow() throws ApplicationErrorException {
		String userName = "manikantan";
		String passWord = "manikantan";
		when(storeService.delete(userName, passWord)).thenThrow(ApplicationErrorException.class);
		assertThrows(ApplicationErrorException.class, () -> storeController.delete(userName, passWord));
		verifyNoMoreInteractions(storeService);
	}

	@Test
	public void deleteStoreShouldThrowIfStatusCodeZero() throws ApplicationErrorException {
		String userName = "manikantan";
		String passWord = "manikantan";
		when(storeService.delete(userName, passWord)).thenReturn(0);
		assertThrows(InvalidTemplateException.class, () -> storeController.delete(userName, passWord));
		verifyNoMoreInteractions(storeService);
	}

	@Test
	public void deleteStoreShouldThrowIfStatusNegative() throws ApplicationErrorException {
		String userName = "manikantan";
		String passWord = "manikantan";
		when(storeService.delete(userName, passWord)).thenReturn(- 1);
		assertThrows(InvalidTemplateException.class, () -> storeController.delete(userName, passWord));
		verifyNoMoreInteractions(storeService);
	}

}
