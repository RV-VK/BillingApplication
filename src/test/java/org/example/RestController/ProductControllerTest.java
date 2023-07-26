package org.example.RestController;

import org.example.DAO.ApplicationErrorException;
import org.example.DAO.PageCountOutOfBoundsException;
import org.example.Entity.Product;
import org.example.Service.InvalidTemplateException;
import org.example.Service.ProductService;
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
public class ProductControllerTest {
	@Autowired
	@InjectMocks
	private ProductController productController;
	@Mock
	private ProductService productService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void getProductsShouldCallAndReturnTest() throws PageCountOutOfBoundsException, ApplicationErrorException {
		List<Product> productList = new ArrayList<>();
		when(productService.list(any())).thenReturn(productList);
		assertNotNull(productController.getAll());
		verify(productService, times(1)).list(any());
	}

	@Test
	void getProductsByPageLengthShouldCallAndReturn() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		when(productService.list(any())).thenReturn(new ArrayList<>());
		assertNotNull(productController.getByPageLength("10"));
		verify(productService, times(1)).list(any());
	}

	@Test
	void getProductsByPageLengthShouldThrowExceptionOnInvalidPageLength() {
		assertThrows(InvalidTemplateException.class, () -> productController.getByPageLength("S"));
		verifyNoInteractions(productService);
	}

	@Test
	void getProductsByPageLengthAndPageNumberShouldCallAndReturn() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		when(productService.list(any())).thenReturn(new ArrayList<>());
		assertNotNull(productController.getByPageLengthAndPageNumber("10", "2"));
		verify(productService, times(1)).list(any());
	}

	@Test
	void getProductsByPageLengthAndPageNumberShouldThrowExceptionOnInvalidPageLengthOrPageNumber() {
		assertThrows(InvalidTemplateException.class, () -> productController.getByPageLengthAndPageNumber("1", "S"));
		verifyNoInteractions(productService);
	}

	@Test
	void getProductsBySearchTextShouldCallAndReturn() throws PageCountOutOfBoundsException, ApplicationErrorException {
		when(productService.list(any())).thenReturn(new ArrayList<>());
		assertNotNull(productController.getBySearchText(any()));
		verify(productService, times(1)).list(any());
	}

	@Test
	void getProductsByAttributeAndSearchTextShouldCallAndReturn() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		when(productService.list(any())).thenReturn(new ArrayList<>());
		assertNotNull(productController.getByAttributeAndSearchText("id", "198"));
		verify(productService, times(1)).list(any());
	}

	@Test
	void getProductsByAttributeAndSearchTextShouldThrowExceptionOnInvalidAttribute() {
		assertThrows(InvalidTemplateException.class, () -> productController.getByAttributeAndSearchText("some", "search"));
		verifyNoInteractions(productService);
	}

	@Test
	void getProductsByAttributeAndSearchTextWithPageLengthShouldCallAndReturn() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		when(productService.list(any())).thenReturn(new ArrayList<>());
		assertNotNull(productController.getByAttributeAndSearchTextWithPageLength("id", "20", "5"));
		verify(productService, times(1)).list(any());
	}

	@Test
	void getProductsByAttributeAndSearchTextWithPageLengthShouldThrowExceptionOnInvalidAttributeOrPageLength() {
		assertThrows(InvalidTemplateException.class, () -> productController.getByAttributeAndSearchTextWithPageLength("some", "search", "2"));
		assertThrows(InvalidTemplateException.class, () -> productController.getByAttributeAndSearchTextWithPageLength("id", "search", "S"));
		verifyNoInteractions(productService);
	}

	@Test
	void getProductsByAttributeAndSearchTextWithPageLengthAndPageNumberShouldCallAndReturn() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		when(productService.list(any())).thenReturn(new ArrayList<>());
		assertNotNull(productController.getByAttributeAndSearchTextWithPageLengthAndPageNumber("id", "20", "5", "1"));
		verify(productService, times(1)).list(any());
	}

	@Test
	void getProductsByAttributeAndSearchTextWithPageLengthAndPageNumberShouldThrowExceptionOnInvalidAttributeOrPageLengthOrPageNumber() {
		assertThrows(InvalidTemplateException.class, () -> productController.getByAttributeAndSearchTextWithPageLengthAndPageNumber("some", "search", "2", "1"));
		assertThrows(InvalidTemplateException.class, () -> productController.getByAttributeAndSearchTextWithPageLengthAndPageNumber("id", "search", "S", "1"));
		assertThrows(InvalidTemplateException.class, () -> productController.getByAttributeAndSearchTextWithPageLengthAndPageNumber("id", "search", "2", "S"));
		verifyNoInteractions(productService);
	}

	@Test
	void addProductShouldCallAndReturnTest() throws Exception {
		Product product = new Product("G01", "Garlic", "kg", "Grocery", 2, 50);
		when(productService.create(product)).thenReturn(product);
		assertEquals(product, productController.add(product));
		verify(productService, times(1)).create(product);
	}

	@Test
	void countProductShouldCallAndReturn() throws ApplicationErrorException {
		String attribute = "id";
		String searchText = null;
		when(productService.count(attribute, searchText)).thenReturn(20);
		assertEquals(20, productController.count());
		verify(productService, times(1)).count(attribute, searchText);
	}


	@Test
	void editProductShouldCallAndReturn() throws Exception {
		Product product = new Product(101, "G11", "Ginger", "kg", "Grocery", 5, 55);
		when(productService.edit(product)).thenReturn(product);
		assertEquals(product, productController.edit(product));
		verify(productService, times(1)).edit(product);
	}

	@Test
	void deleteProductShouldCallAndReturn() throws ApplicationErrorException {
		String parameter = "G01";
		when(productService.delete(parameter)).thenReturn(1);
		assertEquals(1, productController.delete(parameter));
		verify(productService, times(1)).delete(parameter);
	}

}
