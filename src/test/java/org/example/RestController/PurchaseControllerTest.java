package org.example.RestController;

import org.example.DAO.ApplicationErrorException;
import org.example.DAO.PageCountOutOfBoundsException;
import org.example.Entity.Purchase;
import org.example.Service.InvalidTemplateException;
import org.example.Service.PurchaseService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PurchaseControllerTest {
	@Autowired
	@InjectMocks
	private PurchaseController purchaseController;
	@Mock
	private PurchaseService purchaseService;
	@Mock
	private ListAttributeMapValidator validator;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void getPurchaseShouldCallAndReturnTest() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		List<Purchase> salesList = new ArrayList<>();
		when(purchaseService.list(any())).thenReturn(salesList);
		assertNotNull(purchaseController.getAll());
		verify(purchaseService, times(1)).list(any());
	}

	@Test
	void getPurchaseShouldCatchAndRethrowPageCountException() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		when(purchaseService.list(any())).thenThrow(PageCountOutOfBoundsException.class);
		assertThrows(PageCountOutOfBoundsException.class, () -> purchaseController.getAll());
		verifyNoMoreInteractions(purchaseService);
	}

	@Test
	void getPurchaseShouldCatchAndRethrowApplicationErrorException() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		when(purchaseService.list(any())).thenThrow(ApplicationErrorException.class);
		assertThrows(ApplicationErrorException.class, () -> purchaseController.getAll());
		verifyNoMoreInteractions(purchaseService);
	}

	@Test
	void getPurchaseShouldCatchAndRethrowInvalidTemplateException() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		when(purchaseService.list(any())).thenThrow(InvalidTemplateException.class);
		assertThrows(InvalidTemplateException.class, () -> purchaseController.getAll());
		verifyNoMoreInteractions(purchaseService);
	}

	@Test
	void getPurchaseByPageLengthShouldCallAndReturn() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		when(purchaseService.list(any())).thenReturn(new ArrayList<>());
		doNothing().when(validator).validate(any(), any());
		assertNotNull(purchaseController.getByPageLength("10"));
		verify(purchaseService, times(1)).list(any());
	}

	@Test
	void getPurchaseByPageLengthShouldThrowExceptionOnInvalidPageLength() throws InvalidTemplateException {
		doThrow(InvalidTemplateException.class).when(validator).validate(any(), any());
		assertThrows(InvalidTemplateException.class, () -> purchaseController.getByPageLength("S"));
		verifyNoInteractions(purchaseService);
	}

	@Test
	void getPurchaseByPageLengthAndPageNumberShouldCallAndReturn() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		when(purchaseService.list(any())).thenReturn(new ArrayList<>());
		doNothing().when(validator).validate(any(), any());
		assertNotNull(purchaseController.getByPageLengthAndPageNumber("10", "2"));
		verify(purchaseService, times(1)).list(any());
	}

	@Test
	void getPurchaseByPageLengthAndPageNumberShouldThrowExceptionOnInvalidPageLengthOrPageNumber() throws InvalidTemplateException {
		doThrow(InvalidTemplateException.class).when(validator).validate(any(), any());
		assertThrows(InvalidTemplateException.class, () -> purchaseController.getByPageLengthAndPageNumber("1", "S"));
		verifyNoInteractions(purchaseService);
	}

	@Test
	void getPurchaseBySearchTextShouldCallAndReturn() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		when(purchaseService.list(any())).thenReturn(new ArrayList<>());
		assertNotNull(purchaseController.getBySearchText(any()));
		verify(purchaseService, times(1)).list(any());
	}

	@Test
	void getPurchaseByAttributeAndSearchTextShouldCallAndReturn() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		when(purchaseService.list(any())).thenReturn(new ArrayList<>());
		doNothing().when(validator).validate(any(), any());
		assertNotNull(purchaseController.getByAttributeAndSearchText("id", "198"));
		verify(purchaseService, times(1)).list(any());
	}

	@Test
	void getPurchaseByAttributeAndSearchTextShouldThrowExceptionOnInvalidAttribute() throws InvalidTemplateException {
		doThrow(InvalidTemplateException.class).when(validator).validate(any(), any());
		assertThrows(InvalidTemplateException.class, () -> purchaseController.getByAttributeAndSearchText("some", "search"));
		verifyNoInteractions(purchaseService);
	}

	@Test
	void getPurchaseByAttributeAndSearchTextWithPageLengthShouldCallAndReturn() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		when(purchaseService.list(any())).thenReturn(new ArrayList<>());
		doNothing().when(validator).validate(any(), any());
		assertNotNull(purchaseController.getByAttributeAndSearchTextWithPageLength("id", "20", "5"));
		verify(purchaseService, times(1)).list(any());
	}

	@Test
	void getPurchaseByAttributeAndSearchTextWithPageLengthShouldThrowExceptionOnInvalidAttributeOrPageLength() throws InvalidTemplateException {
		doThrow(InvalidTemplateException.class).when(validator).validate(any(), any());
		assertThrows(InvalidTemplateException.class, () -> purchaseController.getByAttributeAndSearchTextWithPageLength("some", "search", "2"));
		assertThrows(InvalidTemplateException.class, () -> purchaseController.getByAttributeAndSearchTextWithPageLength("id", "search", "S"));
		verifyNoInteractions(purchaseService);
	}

	@Test
	void getPurchaseByAttributeAndSearchTextWithPageLengthAndPageNumberShouldCallAndReturn() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		when(purchaseService.list(any())).thenReturn(new ArrayList<>());
		doNothing().when(validator).validate(any(), any());
		assertNotNull(purchaseController.getByAttributeAndSearchTextWithPageLengthAndPageNumber("id", "20", "5", "1"));
		verify(purchaseService, times(1)).list(any());
	}

	@Test
	void getPurchaseByAttributeAndSearchTextWithPageLengthAndPageNumberShouldThrowExceptionOnInvalidAttributeOrPageLengthOrPageNumber() throws InvalidTemplateException {
		doThrow(InvalidTemplateException.class).when(validator).validate(any(), any());
		assertThrows(InvalidTemplateException.class, () -> purchaseController.getByAttributeAndSearchTextWithPageLengthAndPageNumber("some", "search", "2", "1"));
		assertThrows(InvalidTemplateException.class, () -> purchaseController.getByAttributeAndSearchTextWithPageLengthAndPageNumber("id", "search", "S", "1"));
		assertThrows(InvalidTemplateException.class, () -> purchaseController.getByAttributeAndSearchTextWithPageLengthAndPageNumber("id", "search", "2", "S"));
		verifyNoInteractions(purchaseService);
	}

	@Test
	void addPurchaseShouldCallAndReturnTest() throws Exception {
		Purchase sales = new Purchase();
		when(purchaseService.create(sales)).thenReturn(sales);
		assertEquals(sales, purchaseController.add(sales));
		verify(purchaseService, times(1)).create(sales);
	}

	@Test
	void addPurchaseShouldCatchAndRethrowExceptionAndNoMoreInteractions() throws Exception {
		Purchase sales = new Purchase();
		when(purchaseService.create(sales)).thenThrow(ApplicationErrorException.class);
		assertThrows(Exception.class, () -> purchaseController.add(sales));
		verifyNoMoreInteractions(purchaseService);
	}

	@Test
	void countPurchaseShouldCallAndReturn() throws ApplicationErrorException, InvalidTemplateException {
		String attribute = "id";
		String searchText = null;
		when(purchaseService.count(attribute, searchText)).thenReturn(20);
		assertEquals(20, purchaseController.count());
		verify(purchaseService, times(1)).count(attribute, searchText);
	}

	@Test
	void countPurchaseShouldCatchAndRethrowAndNoMoreInteractions() throws ApplicationErrorException, InvalidTemplateException {
		String attribute = "id";
		String searchText = null;
		when(purchaseService.count(attribute, searchText)).thenThrow(ApplicationErrorException.class);
		assertThrows(ApplicationErrorException.class, () -> purchaseController.count());
		verifyNoMoreInteractions(purchaseService);
	}

	@Test
	void countPurchaseNyDateShouldCallAndReturn() throws ApplicationErrorException, InvalidTemplateException {
		String attribute = "date";
		String searchText = "2023-05-23";
		when(purchaseService.count(attribute, searchText)).thenReturn(20);
		assertEquals(20, purchaseController.countByDate(searchText));
		verify(purchaseService, times(1)).count(attribute, searchText);
	}

	@Test
	void countPurchaseByDateShouldCatchAndRethrowAndNoMoreInteractions() throws ApplicationErrorException, InvalidTemplateException {
		String attribute = "date";
		String searchText = "243";
		when(purchaseService.count(attribute, searchText)).thenThrow(ApplicationErrorException.class);
		assertThrows(ApplicationErrorException.class, () -> purchaseController.countByDate(searchText));
		verifyNoMoreInteractions(purchaseService);
	}

	@Test
	void countPurchaseByDateShouldThrowExceptionOnZeroReturn() throws ApplicationErrorException, InvalidTemplateException {
		String attribute = "date";
		String searchText = "2023-05-30";
		when(purchaseService.count(attribute, searchText)).thenReturn(0);
		assertThrows(InvalidTemplateException.class, () -> purchaseController.countByDate(searchText));
		verifyNoMoreInteractions(purchaseService);
	}

	@Test
	void deletePurchaseShouldCallAndReturn() throws ApplicationErrorException, InvalidTemplateException {
		String parameter = "G01";
		when(purchaseService.delete(parameter)).thenReturn(1);
		assertEquals(1, purchaseController.delete(parameter));
		verify(purchaseService, times(1)).delete(parameter);
	}

	@Test
	void deletePurchaseShouldCatchANdRethrowException() throws ApplicationErrorException {
		String parameter = "G01";
		when(purchaseService.delete(parameter)).thenThrow(ApplicationErrorException.class);
		assertThrows(ApplicationErrorException.class, () -> purchaseController.delete(parameter));
		verifyNoMoreInteractions(purchaseService);
	}

	@Test
	void deletePurchaseShouldThrowExceptionWhenStatusCodeZero() throws ApplicationErrorException {
		String parameter = "G01";
		when(purchaseService.delete(parameter)).thenReturn(0);
		assertThrows(InvalidTemplateException.class, () -> purchaseController.delete(parameter));
		verifyNoMoreInteractions(purchaseService);
	}

}
