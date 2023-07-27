package org.example.Service;

import org.example.DAO.ApplicationErrorException;
import org.example.DAO.PageCountOutOfBoundsException;
import org.example.DAO.ProductDAO;
import org.example.Entity.Product;
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
class ProductServiceImplementationTest {

	@Autowired
	@InjectMocks
	private ProductServiceImplementation productService;
	@Mock
	private ProductDAO productDAO;

	private static Stream<Arguments> productProvider() {
		return Stream.of(Arguments.of(new Product("G", "Avocado", "kg", "Grocery", 0F, 50.0)), Arguments.of(new Product("G41", "A", "kg", "Grocery", 0F, 50.0)), Arguments.of(new Product("G41", "Avocado", "kgDAsAS", "Grocery", 0F, 50.0)), Arguments.of(new Product("G41", "Avocado", "kg", "Grocery221", 0F, 50.0)));
	}

	public static Stream<Arguments> mapProvider() {
		return Stream.of(Arguments.of(new HashMap<>() {{
			put("Pagelength", "10");
			put("Pagenumber", "1");
			put("Attribute", "type");
			put("Searchtext", "Grocery");
		}}), Arguments.of(new HashMap<>() {{
			put("Pagelength", "10");
			put("Pagenumber", "1");
			put("Attribute", "id");
			put("Searchtext", null);
		}}));
	}

	public static Stream<Arguments> productNullProvider() {
		return Stream.of(Arguments.of((Object)null), Arguments.of(new Product(null, "A", "kg", "Grocery", 0F, 50.0)), Arguments.of(new Product("G41", null, "kgDAsAS", "Grocery", 0F, 50.0)), Arguments.of(new Product("G41", "Avocado", null, "Grocery221", 0F, 50.0)),
				Arguments.of(new Product("G41", "Avocado", "kg", null, 0F, 50.0)),
				Arguments.of(new Product("G41", "Avocado", "kg", "Grocery", null, 50.0)),
				Arguments.of(new Product("G41", "Avocado", "kg", "Grocery", 0F, null)));
	}

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void createShouldCallAndReturnProduct() throws Exception {
		Product product = new Product("G41", "Avocado", "kg", "Grocery", 0F, 50.0);
		when(productDAO.create(product)).thenReturn(product);
		Product createdProduct = productService.create(product);
		assertNotNull(createdProduct);
		assertEquals(product, createdProduct);
		verify(productDAO, times(1)).create(product);
	}

	@ParameterizedTest
	@MethodSource("productProvider")
	void createShouldThrowExceptionWhenFormatMismatch(Product product) {
		assertThrows(InvalidTemplateException.class, () -> productService.create(product));
		verifyNoInteractions(productDAO);
	}

	@ParameterizedTest
	@MethodSource("productNullProvider")
	void createProductNullValidation(Product product) {
		assertThrows(NullPointerException.class, () -> productService.create(product));
		verifyNoInteractions(productDAO);
	}

	@Test
	void countShouldReturnCallandReturn() throws ApplicationErrorException {
		String attribute = "id";
		String searchText = "40";
		when(productDAO.count(anyString(), anyString())).thenReturn(50);
		Integer count = productService.count(attribute, searchText);
		assertEquals(50, count);
		verify(productDAO, times(1)).count(attribute, searchText);
	}

	@Test
	void listWithSearchTextAloneCallsAndReturnsList() throws ApplicationErrorException, PageCountOutOfBoundsException {
		HashMap<String, String> listAttributes = new HashMap<>();
		listAttributes.put("Pagelength", null);
		listAttributes.put("Pagenumber", null);
		listAttributes.put("Attribute", null);
		listAttributes.put("Searchtext", "Grocery");
		when(productDAO.searchList("Grocery")).thenReturn(Arrays.asList(new Product("G01"), new Product("G02")));
		List<Product> productList = productService.list(listAttributes);
		assertNotNull(productList);
		verify(productDAO, times(1)).searchList("Grocery");
		verifyNoMoreInteractions(productDAO);
	}

	@ParameterizedTest
	@MethodSource("mapProvider")
	void listWithAllAttributesCallsAndReturnsList(HashMap<String, String> listAttributes) throws ApplicationErrorException, PageCountOutOfBoundsException {
		when(productDAO.list(anyString(), any(), anyInt(), anyInt())).thenReturn(Arrays.asList(new Product("G01"), new Product("G02")));
		assertNotNull(productService.list(listAttributes));
		verify(productDAO, times(1)).list(anyString(), any(), anyInt(), anyInt());
	}

	@Test
	void editShouldCallAndReturnProduct() throws Exception {
		Product product = new Product("G41", "Avocado", "kg", "Grocery", 0F, 60.0);
		when(productDAO.edit(product)).thenReturn(product);
		Product editedProduct = productService.edit(product);
		assertNotNull(editedProduct);
		assertEquals(product, editedProduct);
		verify(productDAO, times(1)).edit(product);
	}

	@ParameterizedTest
	@MethodSource("productProvider")
	void editShouldThrowExceptionWhenFormatMismatch(Product product) {
		assertThrows(InvalidTemplateException.class, () -> productService.edit(product));
		verifyNoInteractions(productDAO);
	}

	@ParameterizedTest
	@MethodSource("productNullProvider")
	void editProductNullValidation(Product product) {
		assertThrows(NullPointerException.class, () -> productService.edit(product));
		verifyNoInteractions(productDAO);
	}

	@Test
	void deleteShouldCallAndReturnStatusCode() throws ApplicationErrorException {
		String parameter = "40";
		when(productDAO.delete(anyString())).thenReturn(1);
		assertEquals(1, productService.delete(parameter));
		verify(productDAO, times(1)).delete(parameter);
	}

	@Test
	void deleteProductNullValidation() throws ApplicationErrorException {
		assertEquals(- 1, productService.delete(null));
		verifyNoInteractions(productDAO);
	}
}