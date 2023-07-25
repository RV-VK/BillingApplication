package org.example.RestController;

import org.example.DAO.ApplicationErrorException;
import org.example.DAO.PageCountOutOfBoundsException;
import org.example.Entity.Product;
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
		assertNotNull(productController.getProducts());
		verify(productService, times(1)).list(any());
	}

	@Test
	void getProductsShouldCatchExceptionAndNoMoreInteractions() throws PageCountOutOfBoundsException, ApplicationErrorException {
		when(productService.list(any())).thenThrow(ApplicationErrorException.class);
		assertNull(productController.getProducts());
		verifyNoMoreInteractions(productService);
	}

	@Test
	void addProductShouldCallAndReturnTest() throws Exception {
		Product product = new Product("G01", "Garlic", "kg", "Grocery", 2, 50);
		when(productService.create(product)).thenReturn(product);
		assertEquals(product, productController.add(product));
		verify(productService, times(1)).create(product);
	}

	@Test
	void addProductShouldCatchExceptionAndNoInteractions() throws Exception {
		Product product = new Product("G01", "Garlic", "kg", "Grocery", 2, 50);
		when(productService.create(product)).thenThrow(Exception.class);
		assertNull(productController.add(product));
		verifyNoMoreInteractions(productService);
	}

	@Test
	void countProductShouldCallAndReturn() throws ApplicationErrorException {
		String attribute = "id";
		String searchText = null;
		when(productService.count(attribute, searchText)).thenReturn(20);
		assertEquals(20, productController.countProducts());
		verify(productService, times(1)).count(attribute, searchText);
	}

	@Test
	void countProductShouldCatchExceptionAndNoInteractions() throws ApplicationErrorException {
		String attribute = "id";
		String searchText = null;
		when(productService.count(attribute, searchText)).thenThrow(ApplicationErrorException.class);
		assertNull(productController.countProducts());
		verifyNoMoreInteractions(productService);
	}

	@Test
	void editProductShouldCallAndReturn() throws Exception {
		Product product = new Product(101, "G11", "Ginger", "kg", "Grocery", 5, 55);
		when(productService.edit(product)).thenReturn(product);
		assertEquals(product, productController.edit(product));
		verify(productService, times(1)).edit(product);
	}

	@Test
	void editProductShouldCatchExceptionAndNoInteractions() throws Exception {
		Product product = new Product("G11", "Ginger", "kg", "Grocery", 5, 55);
		when(productService.edit(product)).thenThrow(Exception.class);
		assertNull(productController.edit(product));
		verifyNoMoreInteractions(productService);
	}

	@Test
	void deleteProductShouldCallAndReturn() throws ApplicationErrorException {
		String parameter = "G01";
		when(productService.delete(parameter)).thenReturn(1);
		assertEquals(1, productController.deleteProduct(parameter));
		verify(productService, times(1)).delete(parameter);
	}

	@Test
	void deleteShouldCatchExceptionAndNoMoreInteractions() throws ApplicationErrorException {
		String parameter = "G0001";
		when(productService.delete(parameter)).thenThrow(ApplicationErrorException.class);
		assertNull(productController.deleteProduct(parameter));
		verifyNoMoreInteractions(productService);
	}
}
