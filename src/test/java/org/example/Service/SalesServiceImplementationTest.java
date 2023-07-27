package org.example.Service;

import org.example.DAO.*;
import org.example.Entity.*;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class SalesServiceImplementationTest {

	@Autowired
	@InjectMocks
	private SalesServiceImplementation salesService;
	@Mock
	private SalesDAO salesDAO;
	@Mock
	private ProductDAO productDAO;
	@Mock
	private UnitDAO unitDAO;
	private Sales sales;

	public static Stream<Arguments> countArgumentsProvider() {
		return Stream.of(Arguments.of("id", null),
				Arguments.of("date", "2023-07-12"));
	}

	public static Stream<Arguments> mapProvider() {
		return Stream.of(Arguments.of(new HashMap<>() {{
			put("Pagelength", "10");
			put("Pagenumber", "1");
			put("Attribute", "id");
			put("Searchtext", "20");
		}}), Arguments.of(new HashMap<>() {{
			put("Pagelength", "10");
			put("Pagenumber", "1");
			put("Attribute", "id");
			put("Searchtext", null);
		}}), Arguments.of(new HashMap<>() {{
			put("Pagelength", "10");
			put("Pagenumber", "1");
			put("Attribute", "date");
			put("Searchtext", null);
		}}), Arguments.of(new HashMap<>() {{
			put("Pagelength", "10");
			put("Pagenumber", "1");
			put("Attribute", "date");
			put("Searchtext", "2023-05-12");
		}}));
	}

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		sales = new Sales();
		sales.setDate("2023-07-12");
		sales.setId(123);
		sales.setGrandTotal(50.0);
		List<SalesItem> salesItemList = List.of(new SalesItem("G01", 2.0F, 25.0), new SalesItem("G01", 2.5F, 25.0));
		sales.setSalesItemList(salesItemList);
	}

	@Test
	void createShouldCallAndReturnSales() throws Exception {
		when(productDAO.findByCode(anyString())).thenReturn(new Product("G01", "Garlic", "kg", "Grocery", 5F, 20.0));
		when(unitDAO.findByCode(anyString())).thenReturn(new Unit("Unit", "un", "desc", true));
		when(salesDAO.create(sales)).thenReturn(sales);
		assertNotNull(salesService.create(sales));
		verify(salesDAO, times(1)).create(sales);
	}

	@Test
	void createShouldThrowExceptionWhenProductNotFound() throws Exception {
		when(productDAO.findByCode(anyString())).thenReturn(null);
		assertThrows(ApplicationErrorException.class, () -> salesService.create(sales));
		verifyNoInteractions(salesDAO);
	}

	@Test
	void createShouldThrowExceptionWhenDividabilityViolated() throws Exception {
		when(productDAO.findByCode(anyString())).thenReturn(new Product("G01", "Garlic", "kg", "Grocery", 5F, 20.0));
		when(unitDAO.findByCode(anyString())).thenReturn(new Unit("Unit", "un", "desc", false));
		assertThrows(UnDividableEntityException.class, () -> salesService.create(sales));
		verifyNoInteractions(salesDAO);
	}

	@Test
	void createShouldThrowExceptionWhenStockIsOut() throws Exception {
		when(productDAO.findByCode(anyString())).thenReturn(new Product("G01", "Garlic", "kg", "Grocery", 1F, 20.0));
		assertThrows(ApplicationErrorException.class, () -> salesService.create(sales));
		verifyNoInteractions(salesDAO);
	}

	@Test
	void createShouldThrowExceptionWhenTemplateMismatch() {
		Sales sales = new Sales();
		sales.setDate("Malformed");
		assertThrows(InvalidTemplateException.class, () -> salesService.create(sales));
		verifyNoInteractions(salesDAO);
	}

	@Test
	void createSalesNullValidation() {
		assertThrows(NullPointerException.class, () -> salesService.create(null));
		assertThrows(NullPointerException.class, () -> salesService.create(new Sales(null, new ArrayList<>(), 250.0)));
		verifyNoInteractions(salesDAO);

	}

	@ParameterizedTest
	@MethodSource("countArgumentsProvider")
	void countShouldCallAndReturn(String attribute, String searchText) throws ApplicationErrorException, InvalidTemplateException {
		when(salesDAO.count(any(), any())).thenReturn(50);
		assertEquals(50, salesService.count(attribute, searchText));
		verify(salesDAO, times(1)).count(attribute, searchText);
	}

	@Test
	void countShouldThrowExceptionWhenDateMismatch() {
		String attribute = "date";
		String searchText = "Malformed";
		assertThrows(InvalidTemplateException.class, () -> salesService.count(attribute, searchText));
		verifyNoInteractions(salesDAO);
	}

	@Test
	void listWithSearchTextAloneCallsAndReturnsList() throws ApplicationErrorException, InvalidTemplateException, PageCountOutOfBoundsException {
		HashMap<String, String> listAttributes = new HashMap<>();
		listAttributes.put("Pagelength", null);
		listAttributes.put("Pagenumber", null);
		listAttributes.put("Attribute", null);
		listAttributes.put("Searchtext", "20");
		when(salesDAO.searchList("20")).thenReturn(Arrays.asList(new Sales(), new Sales()));
		List<Sales> salesList = salesService.list(listAttributes);
		assertNotNull(salesList);
		verify(salesDAO, times(1)).searchList("20");
		verifyNoMoreInteractions(salesDAO);
	}

	@ParameterizedTest
	@MethodSource("mapProvider")
	void listWithAllAttributesCallsAndReturnsList(HashMap<String, String> listAttributes) throws ApplicationErrorException, InvalidTemplateException, PageCountOutOfBoundsException {
		when(salesDAO.list(anyString(), any(), anyInt(), anyInt())).thenReturn(Arrays.asList(new Sales(), new Sales()));
		assertNotNull(salesService.list(listAttributes));
		verify(salesDAO, times(1)).list(anyString(), any(), anyInt(), anyInt());
	}

	@Test
	void listShouldThrowExceptionWhenDateMismatch() {
		HashMap<String, String> listAttributes = new HashMap<>();
		listAttributes.put("Pagelength", "10");
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", "date");
		listAttributes.put("Searchtext", "Malformed");
		assertThrows(InvalidTemplateException.class, () -> salesService.list(listAttributes));
		verifyNoInteractions(salesDAO);
	}

	@Test
	void deleteShouldCallAndReturnStatusCode() throws ApplicationErrorException {
		String id = "123";
		when(salesDAO.delete(anyInt())).thenReturn(1);
		assertEquals(1, salesService.delete(id));
		verify(salesDAO, times(1)).delete(anyInt());
	}

	@Test
	void deleteUserNullValidation() throws ApplicationErrorException {
		assertEquals(- 1, salesService.delete(null));
		verifyNoInteractions(salesDAO);
	}
}