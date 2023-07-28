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
	@Mock
	private ListAttributeMapValidator validator;

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
	void getProductsShouldCatchAndRethrowPageCountException() throws PageCountOutOfBoundsException, ApplicationErrorException {
		when(productService.list(any())).thenThrow(PageCountOutOfBoundsException.class);
		assertThrows(PageCountOutOfBoundsException.class, () -> productController.getAll());
		verifyNoMoreInteractions(productService);
	}

	@Test
	void getProductsShouldCatchAndRethrowApplicationErrorException() throws PageCountOutOfBoundsException, ApplicationErrorException {
		when(productService.list(any())).thenThrow(ApplicationErrorException.class);
		assertThrows(ApplicationErrorException.class, () -> productController.getAll());
		verifyNoMoreInteractions(productService);
	}

	@Test
	void getProductsByPageLengthShouldCallAndReturn() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		when(productService.list(any())).thenReturn(new ArrayList<>());
		doNothing().when(validator).validate(any(), any());
		assertNotNull(productController.getByPageLength("10"));
		verify(productService, times(1)).list(any());
	}

	@Test
	void getProductsByPageLengthShouldThrowExceptionOnInvalidPageLength() throws InvalidTemplateException {
		doThrow(InvalidTemplateException.class).when(validator).validate(any(), any());
		assertThrows(InvalidTemplateException.class, () -> productController.getByPageLength("S"));
		verifyNoInteractions(productService);
	}

	@Test
	void getProductsByPageLengthAndPageNumberShouldCallAndReturn() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		when(productService.list(any())).thenReturn(new ArrayList<>());
		doNothing().when(validator).validate(any(), any());
		assertNotNull(productController.getByPageLengthAndPageNumber("10", "2"));
		verify(productService, times(1)).list(any());
	}

	@Test
	void getProductsByPageLengthAndPageNumberShouldThrowExceptionOnInvalidPageLengthOrPageNumber() throws InvalidTemplateException {
		doThrow(InvalidTemplateException.class).when(validator).validate(any(), any());
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
		doNothing().when(validator).validate(any(), any());
		assertNotNull(productController.getByAttributeAndSearchText("id", "198"));
		verify(productService, times(1)).list(any());
	}

	@Test
	void getProductsByAttributeAndSearchTextShouldThrowExceptionOnInvalidAttribute() throws InvalidTemplateException {
		doThrow(InvalidTemplateException.class).when(validator).validate(any(), any());
		assertThrows(InvalidTemplateException.class, () -> productController.getByAttributeAndSearchText("some", "search"));
		verifyNoInteractions(productService);
	}

	@Test
	void getProductsByAttributeAndSearchTextWithPageLengthShouldCallAndReturn() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		when(productService.list(any())).thenReturn(new ArrayList<>());
		doNothing().when(validator).validate(any(), any());
		assertNotNull(productController.getByAttributeAndSearchTextWithPageLength("id", "20", "5"));
		verify(productService, times(1)).list(any());
	}

	@Test
	void getProductsByAttributeAndSearchTextWithPageLengthShouldThrowExceptionOnInvalidAttributeOrPageLength() throws InvalidTemplateException {
		doThrow(InvalidTemplateException.class).when(validator).validate(any(), any());
		assertThrows(InvalidTemplateException.class, () -> productController.getByAttributeAndSearchTextWithPageLength("some", "search", "2"));
		assertThrows(InvalidTemplateException.class, () -> productController.getByAttributeAndSearchTextWithPageLength("id", "search", "S"));
		verifyNoInteractions(productService);
	}

	@Test
	void getProductsByAttributeAndSearchTextWithPageLengthAndPageNumberShouldCallAndReturn() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		when(productService.list(any())).thenReturn(new ArrayList<>());
		doNothing().when(validator).validate(any(), any());
		assertNotNull(productController.getByAttributeAndSearchTextWithPageLengthAndPageNumber("id", "20", "5", "1"));
		verify(productService, times(1)).list(any());
	}

	@Test
	void getProductsByAttributeAndSearchTextWithPageLengthAndPageNumberShouldThrowExceptionOnInvalidAttributeOrPageLengthOrPageNumber() throws InvalidTemplateException {
		doThrow(InvalidTemplateException.class).when(validator).validate(any(), any());
		assertThrows(InvalidTemplateException.class, () -> productController.getByAttributeAndSearchTextWithPageLengthAndPageNumber("some", "search", "2", "1"));
		assertThrows(InvalidTemplateException.class, () -> productController.getByAttributeAndSearchTextWithPageLengthAndPageNumber("id", "search", "S", "1"));
		assertThrows(InvalidTemplateException.class, () -> productController.getByAttributeAndSearchTextWithPageLengthAndPageNumber("id", "search", "2", "S"));
		verifyNoInteractions(productService);
	}

	@Test
	void addProductShouldCallAndReturnTest() throws Exception {
		Product product = new Product("G01", "Garlic", "kg", "Grocery", 2F, 50.0);
		when(productService.create(product)).thenReturn(product);
		assertEquals(product, productController.add(product));
		verify(productService, times(1)).create(product);
	}

	@Test
	void addProductShouldCatchAndRethrowExceptionAndNoMoreInteractions() throws Exception {
		Product product = new Product("G01", "Garlic", "lg", "Grocery", 2F, 50.0);
		when(productService.create(product)).thenThrow(Exception.class);
		assertThrows(Exception.class, () -> productController.add(product));
		verifyNoMoreInteractions(productService);
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
	void countProductShouldCatchAndRethrowAndNoMoreInteractions() throws ApplicationErrorException {
		String attribute = "id";
		String searchText = null;
		when(productService.count(attribute, searchText)).thenThrow(ApplicationErrorException.class);
		assertThrows(ApplicationErrorException.class, () -> productController.count());
		verifyNoMoreInteractions(productService);
	}


	@Test
	void editProductShouldCallAndReturn() throws Exception {
		Product product = new Product(101, "G11", "Ginger", "kg", "Grocery", 5F, 55);
		when(productService.edit(product)).thenReturn(product);
		assertEquals(product, productController.edit(product));
		verify(productService, times(1)).edit(product);
	}

	@Test
	void editProductShouldCatchAndRethrowException() throws Exception {
		Product product = new Product(101, "G11", "Ginger", "kgl", "Grocery", 5F, 55);
		when(productService.edit(product)).thenThrow(Exception.class);
		assertThrows(Exception.class, () -> productController.edit(product));
		verifyNoMoreInteractions(productService);
	}

	@Test
	void editProductShouldThrowExceptionWhenNullReturned() throws Exception {
		Product product = new Product(101, "G11", "Ginger", "kgl", "Grocery", 5F, 55);
		when(productService.edit(product)).thenReturn(null);
		assertThrows(InvalidTemplateException.class, () -> productController.edit(product));
		verifyNoMoreInteractions(productService);
	}

	@Test
	void deleteProductShouldCallAndReturn() throws ApplicationErrorException, InvalidTemplateException {
		String parameter = "G01";
		when(productService.delete(parameter)).thenReturn(1);
		assertEquals(1, productController.delete(parameter));
		verify(productService, times(1)).delete(parameter);
	}

	@Test
	void deleteProductShouldCatchANdRethrowException() throws ApplicationErrorException {
		String parameter = "G01";
		when(productService.delete(parameter)).thenThrow(ApplicationErrorException.class);
		assertThrows(ApplicationErrorException.class, () -> productController.delete(parameter));
		verifyNoMoreInteractions(productService);
	}

	@Test
	void deleteProductShouldThrowExceptionWhenStatusCodeZero() throws ApplicationErrorException {
		String parameter = "G01";
		when(productService.delete(parameter)).thenReturn(0);
		assertThrows(InvalidTemplateException.class, () -> productController.delete(parameter));
		verifyNoMoreInteractions(productService);
	}

}
