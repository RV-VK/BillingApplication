package org.example.RestController;

import org.example.DAO.ApplicationErrorException;
import org.example.DAO.PageCountOutOfBoundsException;
import org.example.Entity.Sales;
import org.example.Service.InvalidTemplateException;
import org.example.Service.SalesService;
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
public class SalesControllerTest {
	@Autowired
	@InjectMocks
	private SalesController salesController;

	@Mock
	private SalesService salesService;

	@Mock
	private ListAttributeMapValidator validator;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void getSalesShouldCallAndReturnTest() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		List<Sales> salesList = new ArrayList<>();
		when(salesService.list(any())).thenReturn(salesList);
		assertNotNull(salesController.getAll());
		verify(salesService, times(1)).list(any());
	}

	@Test
	void getSalesShouldCatchAndRethrowPageCountException() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		when(salesService.list(any())).thenThrow(PageCountOutOfBoundsException.class);
		assertThrows(PageCountOutOfBoundsException.class, () -> salesController.getAll());
		verifyNoMoreInteractions(salesService);
	}

	@Test
	void getSalesShouldCatchAndRethrowApplicationErrorException() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		when(salesService.list(any())).thenThrow(ApplicationErrorException.class);
		assertThrows(ApplicationErrorException.class, () -> salesController.getAll());
		verifyNoMoreInteractions(salesService);
	}

	@Test
	void getSalesShouldCatchAndRethrowInvalidTemplateException() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		when(salesService.list(any())).thenThrow(InvalidTemplateException.class);
		assertThrows(InvalidTemplateException.class, () -> salesController.getAll());
		verifyNoMoreInteractions(salesService);
	}

	@Test
	void getSalesByPageLengthShouldCallAndReturn() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		when(salesService.list(any())).thenReturn(new ArrayList<>());
		doNothing().when(validator).validate(any(), any());
		assertNotNull(salesController.getByPageLength("10"));
		verify(salesService, times(1)).list(any());
	}

	@Test
	void getSalesByPageLengthShouldThrowExceptionOnInvalidPageLength() throws InvalidTemplateException {
		doThrow(InvalidTemplateException.class).when(validator).validate(any(), any());
		assertThrows(InvalidTemplateException.class, () -> salesController.getByPageLength("S"));
		verifyNoInteractions(salesService);
	}

	@Test
	void getSalesByPageLengthAndPageNumberShouldCallAndReturn() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		when(salesService.list(any())).thenReturn(new ArrayList<>());
		doNothing().when(validator).validate(any(), any());
		assertNotNull(salesController.getByPageLengthAndPageNumber("10", "2"));
		verify(salesService, times(1)).list(any());
	}

	@Test
	void getSalesByPageLengthAndPageNumberShouldThrowExceptionOnInvalidPageLengthOrPageNumber() throws InvalidTemplateException {
		doThrow(InvalidTemplateException.class).when(validator).validate(any(), any());
		assertThrows(InvalidTemplateException.class, () -> salesController.getByPageLengthAndPageNumber("1", "S"));
		verifyNoInteractions(salesService);
	}

	@Test
	void getSalesBySearchTextShouldCallAndReturn() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		when(salesService.list(any())).thenReturn(new ArrayList<>());
		assertNotNull(salesController.getBySearchText(any()));
		verify(salesService, times(1)).list(any());
	}

	@Test
	void getSalesByAttributeAndSearchTextShouldCallAndReturn() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		when(salesService.list(any())).thenReturn(new ArrayList<>());
		doNothing().when(validator).validate(any(), any());
		assertNotNull(salesController.getByAttributeAndSearchText("id", "198"));
		verify(salesService, times(1)).list(any());
	}

	@Test
	void getSalesByAttributeAndSearchTextShouldThrowExceptionOnInvalidAttribute() throws InvalidTemplateException {
		doThrow(InvalidTemplateException.class).when(validator).validate(any(), any());
		assertThrows(InvalidTemplateException.class, () -> salesController.getByAttributeAndSearchText("some", "search"));
		verifyNoInteractions(salesService);
	}

	@Test
	void getSalesByAttributeAndSearchTextWithPageLengthShouldCallAndReturn() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		when(salesService.list(any())).thenReturn(new ArrayList<>());
		doNothing().when(validator).validate(any(), any());
		assertNotNull(salesController.getByAttributeAndSearchTextWithPageLength("id", "20", "5"));
		verify(salesService, times(1)).list(any());
	}

	@Test
	void getSalesByAttributeAndSearchTextWithPageLengthShouldThrowExceptionOnInvalidAttributeOrPageLength() throws InvalidTemplateException {
		doThrow(InvalidTemplateException.class).when(validator).validate(any(), any());
		assertThrows(InvalidTemplateException.class, () -> salesController.getByAttributeAndSearchTextWithPageLength("some", "search", "2"));
		assertThrows(InvalidTemplateException.class, () -> salesController.getByAttributeAndSearchTextWithPageLength("id", "search", "S"));
		verifyNoInteractions(salesService);
	}

	@Test
	void getSalesByAttributeAndSearchTextWithPageLengthAndPageNumberShouldCallAndReturn() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		when(salesService.list(any())).thenReturn(new ArrayList<>());
		doNothing().when(validator).validate(any(), any());
		assertNotNull(salesController.getByAttributeAndSearchTextWithPageLengthAndPageNumber("id", "20", "5", "1"));
		verify(salesService, times(1)).list(any());
	}

	@Test
	void getSalesByAttributeAndSearchTextWithPageLengthAndPageNumberShouldThrowExceptionOnInvalidAttributeOrPageLengthOrPageNumber() throws InvalidTemplateException {
		doThrow(InvalidTemplateException.class).when(validator).validate(any(), any());
		assertThrows(InvalidTemplateException.class, () -> salesController.getByAttributeAndSearchTextWithPageLengthAndPageNumber("some", "search", "2", "1"));
		assertThrows(InvalidTemplateException.class, () -> salesController.getByAttributeAndSearchTextWithPageLengthAndPageNumber("id", "search", "S", "1"));
		assertThrows(InvalidTemplateException.class, () -> salesController.getByAttributeAndSearchTextWithPageLengthAndPageNumber("id", "search", "2", "S"));
		verifyNoInteractions(salesService);
	}

	@Test
	void addSalesShouldCallAndReturnTest() throws Exception {
		Sales sales = new Sales();
		when(salesService.create(sales)).thenReturn(sales);
		assertEquals(sales, salesController.add(sales));
		verify(salesService, times(1)).create(sales);
	}

	@Test
	void addSalesShouldCatchAndRethrowExceptionAndNoMoreInteractions() throws Exception {
		Sales sales = new Sales();
		when(salesService.create(sales)).thenThrow(ApplicationErrorException.class);
		assertThrows(Exception.class, () -> salesController.add(sales));
		verifyNoMoreInteractions(salesService);
	}

	@Test
	void countSalesShouldCallAndReturn() throws ApplicationErrorException, InvalidTemplateException {
		String attribute = "id";
		String searchText = null;
		when(salesService.count(attribute, searchText)).thenReturn(20);
		assertEquals(20, salesController.count());
		verify(salesService, times(1)).count(attribute, searchText);
	}

	@Test
	void countSalesShouldCatchAndRethrowAndNoMoreInteractions() throws ApplicationErrorException, InvalidTemplateException {
		String attribute = "id";
		String searchText = null;
		when(salesService.count(attribute, searchText)).thenThrow(ApplicationErrorException.class);
		assertThrows(ApplicationErrorException.class, () -> salesController.count());
		verifyNoMoreInteractions(salesService);
	}

	@Test
	void countSalesNyDateShouldCallAndReturn() throws ApplicationErrorException, InvalidTemplateException {
		String attribute = "date";
		String searchText = "2023-05-23";
		when(salesService.count(attribute, searchText)).thenReturn(20);
		assertEquals(20, salesController.countByDate(searchText));
		verify(salesService, times(1)).count(attribute, searchText);
	}

	@Test
	void countSalesByDateShouldCatchAndRethrowAndNoMoreInteractions() throws ApplicationErrorException, InvalidTemplateException {
		String attribute = "date";
		String searchText = "243";
		when(salesService.count(attribute, searchText)).thenThrow(ApplicationErrorException.class);
		assertThrows(ApplicationErrorException.class, () -> salesController.countByDate(searchText));
		verifyNoMoreInteractions(salesService);
	}

	@Test
	void countSalesByDateShouldThrowExceptionOnZeroReturn() throws ApplicationErrorException, InvalidTemplateException {
		String attribute = "date";
		String searchText = "2023-05-30";
		when(salesService.count(attribute, searchText)).thenReturn(0);
		assertThrows(InvalidTemplateException.class, () -> salesController.countByDate(searchText));
		verifyNoMoreInteractions(salesService);
	}

	@Test
	void deleteSalesShouldCallAndReturn() throws ApplicationErrorException, InvalidTemplateException {
		String parameter = "G01";
		when(salesService.delete(parameter)).thenReturn(1);
		assertEquals(1, salesController.delete(parameter));
		verify(salesService, times(1)).delete(parameter);
	}

	@Test
	void deleteSalesShouldCatchANdRethrowException() throws ApplicationErrorException {
		String parameter = "G01";
		when(salesService.delete(parameter)).thenThrow(ApplicationErrorException.class);
		assertThrows(ApplicationErrorException.class, () -> salesController.delete(parameter));
		verifyNoMoreInteractions(salesService);
	}

	@Test
	void deleteSalesShouldThrowExceptionWhenStatusCodeZero() throws ApplicationErrorException {
		String parameter = "G01";
		when(salesService.delete(parameter)).thenReturn(0);
		assertThrows(InvalidTemplateException.class, () -> salesController.delete(parameter));
		verifyNoMoreInteractions(salesService);
	}


}
