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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PurchaseServiceImplementationTest {

	@Autowired
	@InjectMocks
	private PurchaseServiceImplementation purchaseService;
	@Mock
	private PurchaseDAO purchaseDAO;
	@Mock
	private ProductDAO productDAO;
	@Mock
	private UnitDAO unitDAO;
	private Purchase purchase;

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
		purchase = new Purchase();
		purchase.setDate("2023-07-12");
		purchase.setInvoice(123);
		purchase.setGrandTotal(50.0);
		List<PurchaseItem> purchaseItemList = List.of(new PurchaseItem("G01", 2.0F, 25.0), new PurchaseItem("G01", 2.5F, 25.0));
		purchase.setPurchaseItemList(purchaseItemList);
	}

	@Test
	void createShouldCallAndReturnPurchase() throws Exception {
		when(productDAO.findByCode(anyString())).thenReturn(new Product("G01", "Garlic", "kg", "Grocery", 2, 20));
		when(unitDAO.findByCode(anyString())).thenReturn(new Unit("Unit", "un", "desc", true));
		when(purchaseDAO.create(purchase)).thenReturn(purchase);
		assertNotNull(purchaseService.create(purchase));
		verify(purchaseDAO, times(1)).create(purchase);
	}

	@Test
	void createShouldThrowExceptionWhenProductNotFound() throws Exception {
		when(productDAO.findByCode(anyString())).thenReturn(null);
		assertThrows(ApplicationErrorException.class, () -> purchaseService.create(purchase));
		verifyNoInteractions(purchaseDAO);
	}

	@Test
	void createShouldThrowExceptionWhenDividabilityViolated() throws Exception {
		when(productDAO.findByCode(anyString())).thenReturn(new Product("G01", "Garlic", "kg", "Grocery", 2, 20));
		when(unitDAO.findByCode(anyString())).thenReturn(new Unit("Unit", "un", "desc", false));
		assertThrows(UnDividableEntityException.class, () -> purchaseService.create(purchase));
		verifyNoInteractions(purchaseDAO);
	}

	@Test
	void createShouldThrowExceptionWhenTemplateMismatch() {
		Purchase purchase = new Purchase();
		purchase.setDate("Malformed");
		assertThrows(InvalidTemplateException.class, () -> purchaseService.create(purchase));
		verifyNoInteractions(purchaseDAO);
	}

	@Test
	void createPurchaseNullValidation() {
		assertThrows(NullPointerException.class, () -> purchaseService.create(null));
		verifyNoInteractions(purchaseDAO);

	}

	@ParameterizedTest
	@MethodSource("countArgumentsProvider")
	void countShouldCallAndReturn(String attribute, String searchText) throws ApplicationErrorException, InvalidTemplateException {
		when(purchaseDAO.count(any(), any())).thenReturn(50);
		assertEquals(50, purchaseService.count(attribute, searchText));
		verify(purchaseDAO, times(1)).count(attribute, searchText);
	}

	@Test
	void countShouldThrowExceptionWhenDateMismatch() {
		String attribute = "date";
		String searchText = "bjkdsba";
		assertThrows(InvalidTemplateException.class, () -> purchaseService.count(attribute, searchText));
		verifyNoInteractions(purchaseDAO);
	}

	@Test
	void listWithSearchTextAloneCallsAndReturnsList() throws ApplicationErrorException, InvalidTemplateException {
		HashMap<String, String> listAttributes = new HashMap<>();
		listAttributes.put("Pagelength", null);
		listAttributes.put("Pagenumber", null);
		listAttributes.put("Attribute", null);
		listAttributes.put("Searchtext", "20");
		when(purchaseDAO.searchList("20")).thenReturn(Arrays.asList(new Purchase(), new Purchase()));
		List<Purchase> purchaseList = purchaseService.list(listAttributes);
		assertNotNull(purchaseList);
		verify(purchaseDAO, times(1)).searchList("20");
		verifyNoMoreInteractions(purchaseDAO);
	}

	@ParameterizedTest
	@MethodSource("mapProvider")
	void listWithAllAttributesCallsAndReturnsList(HashMap<String, String> listAttributes) throws ApplicationErrorException, InvalidTemplateException {
		when(purchaseDAO.list(anyString(), any(), anyInt(), anyInt())).thenReturn(Arrays.asList(new Purchase(), new Purchase()));
		assertNotNull(purchaseService.list(listAttributes));
		verify(purchaseDAO, times(1)).list(anyString(), any(), anyInt(), anyInt());
	}

	@Test
	void listShouldThrowExceptionWhenDateMismatch() {
		HashMap<String, String> listAttributes = new HashMap<>();
		listAttributes.put("Pagelength", "10");
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", "date");
		listAttributes.put("Searchtext", "Malformed");
		assertThrows(InvalidTemplateException.class, () -> purchaseService.list(listAttributes));
		verifyNoInteractions(purchaseDAO);
	}

	@Test
	void deleteShouldCallAndReturnStatusCode() throws ApplicationErrorException {
		String invoice = "123";
		when(purchaseDAO.delete(anyInt())).thenReturn(1);
		assertEquals(1, purchaseService.delete(invoice));
		verify(purchaseDAO, times(1)).delete(anyInt());
	}

	@Test
	void deleteUserNullValidation() throws ApplicationErrorException {
		assertEquals(- 1, purchaseService.delete(null));
		verifyNoInteractions(purchaseDAO);
	}
}