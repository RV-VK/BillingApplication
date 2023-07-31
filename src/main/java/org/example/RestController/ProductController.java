package org.example.RestController;

import org.example.DAO.ApplicationErrorException;
import org.example.DAO.PageCountOutOfBoundsException;
import org.example.Entity.Product;
import org.example.Service.InvalidTemplateException;
import org.example.Service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class ProductController {
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
	@Autowired
	private ProductService productService;
	@Autowired
	private ListAttributeMapValidator validator;
	private HashMap<String, String> listAttributes = new HashMap<>();
	private List<Product> productList;

	@GetMapping(path = "/products", produces = "application/json")
	@PreAuthorize("hasRole('Admin')")
	public List<Product> getAll() throws PageCountOutOfBoundsException, ApplicationErrorException {
		listAttributes.put("Pagelength", String.valueOf(Integer.MAX_VALUE));
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", "id");
		listAttributes.put("Searchtext", null);
		return listHelperFunction(listAttributes);
	}

	@GetMapping(path = "/products/{pageLength}", produces = "application/json")
	@PreAuthorize("hasRole('Admin')")
	public List<Product> getByPageLength(@PathVariable String pageLength) throws InvalidTemplateException, PageCountOutOfBoundsException, ApplicationErrorException {
		listAttributes.put("Pagelength", pageLength);
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", "id");
		listAttributes.put("Searchtext", null);
		validator.validate(listAttributes, ProductController.class);
		return listHelperFunction(listAttributes);
	}

	@GetMapping(path = "/products/{pageLength}/{pageNumber}", produces = "application/json")
	@PreAuthorize("hasRole('Admin')")
	public List<Product> getByPageLengthAndPageNumber(@PathVariable String pageLength, @PathVariable String pageNumber) throws InvalidTemplateException, PageCountOutOfBoundsException, ApplicationErrorException {
		listAttributes.put("Pagelength", pageLength);
		listAttributes.put("Pagenumber", pageNumber);
		listAttributes.put("Attribute", "id");
		listAttributes.put("Searchtext", null);
		validator.validate(listAttributes, ProductController.class);
		return listHelperFunction(listAttributes);
	}

	@GetMapping(path = "/products/find/{searchText}", produces = "application/json")
	@PreAuthorize("hasRole('Admin')")
	public List<Product> getBySearchText(@PathVariable String searchText) throws PageCountOutOfBoundsException, ApplicationErrorException {
		listAttributes.put("Pagelength", null);
		listAttributes.put("Pagenumber", null);
		listAttributes.put("Attribute", null);
		listAttributes.put("Searchtext", searchText);
		return listHelperFunction(listAttributes);
	}

	@GetMapping(path = "/products/find/{attribute}/{searchText}", produces = "application/json")
	@PreAuthorize("hasRole('Admin')")
	public List<Product> getByAttributeAndSearchText(@PathVariable String attribute, @PathVariable String searchText) throws InvalidTemplateException, PageCountOutOfBoundsException, ApplicationErrorException {
		listAttributes.put("Pagelength", String.valueOf(Integer.MAX_VALUE));
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", attribute);
		listAttributes.put("Searchtext", searchText);
		validator.validate(listAttributes, ProductController.class);
		return listHelperFunction(listAttributes);
	}

	@GetMapping(path = "/products/find/{attribute}/{searchText}/{PageLength}", produces = "application/json")
	@PreAuthorize("hasRole('Admin')")
	public List<Product> getByAttributeAndSearchTextWithPageLength(@PathVariable String attribute, @PathVariable String searchText, @PathVariable String PageLength) throws InvalidTemplateException, PageCountOutOfBoundsException, ApplicationErrorException {
		listAttributes.put("Pagelength", PageLength);
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", attribute);
		listAttributes.put("Searchtext", searchText);
		validator.validate(listAttributes, ProductController.class);
		return listHelperFunction(listAttributes);
	}

	@GetMapping(path = "/products/find/{attribute}/{searchText}/{PageLength}/{PageNumber}", produces = "application/json")
	@PreAuthorize("hasRole('Admin')")
	public List<Product> getByAttributeAndSearchTextWithPageLengthAndPageNumber(@PathVariable String attribute, @PathVariable String searchText, @PathVariable String PageLength, @PathVariable String PageNumber) throws InvalidTemplateException, PageCountOutOfBoundsException, ApplicationErrorException {
		listAttributes.put("Pagelength", PageLength);
		listAttributes.put("Pagenumber", PageNumber);
		listAttributes.put("Attribute", attribute);
		listAttributes.put("Searchtext", searchText);
		validator.validate(listAttributes, ProductController.class);
		return listHelperFunction(listAttributes);
	}

	@PostMapping(path = "/product", produces = "application/json")
	@PreAuthorize("hasRole('Admin')")
	public Product add(@RequestBody Product product) throws Exception {
		Product createdProduct;
		try {
			createdProduct = productService.create(product);
		} catch(Exception exception) {
			logger.error("Product Creation Failed!, {} ", exception.getMessage());
			throw exception;
		}
		logger.info("Product Created Successfully! : {} ", createdProduct);
		return createdProduct;
	}

	@GetMapping(path = "/countProducts", produces = "application/json")
	@PreAuthorize("hasRole('Admin')")
	public Integer count() throws ApplicationErrorException {
		String attribute = "id";
		String searchText = null;
		Integer count;
		try {
			count = productService.count(attribute, searchText);
		} catch(ApplicationErrorException exception) {
			logger.error("Error while retrieving data from database, {} ", exception.getMessage());
			throw exception;
		}
		logger.info("Returned Successfully!, Product Count : {} ", count);
		return count;
	}

	@PutMapping(path = "/product", produces = "application/json")
	@PreAuthorize("hasRole('Admin')")
	public Product edit(@RequestBody Product product) throws Exception {
		Product editedProduct;
		try {
			editedProduct = productService.edit(product);
		} catch(Exception exception) {
			logger.error("Product Edit Failed!, {} ", exception.getMessage());
			throw exception;
		}
		if(editedProduct == null) {
			logger.error("Product Edit Failed!, Given Id doesnt exists, {} ", product.getId());
			throw new InvalidTemplateException("The Id doesnt exist to edit! Please Give An Existing Id");
		} else {
			logger.info("Product Edited Successfully!, Edited Product : {} ", editedProduct);
			return editedProduct;
		}
	}

	@DeleteMapping(path = "/deleteProduct/{parameter}", produces = "application/json")
	@PreAuthorize("hasRole('Admin')")
	public Integer delete(@PathVariable String parameter) throws ApplicationErrorException, InvalidTemplateException {
		Integer statusCode;
		try {
			statusCode = productService.delete(parameter);
		} catch(ApplicationErrorException exception) {
			logger.error("Product deletion failed, {} ", exception.getMessage());
			throw exception;
		}
		if(statusCode == 1) {
			logger.info("Product deleted successfully!");
			return statusCode;
		} else {
			logger.error("Product Delete Failed! Either the Product doesnt exists or doesnt have any stock left! Product-Parameter: {} ", parameter);
			throw new InvalidTemplateException("Please check the Id (or) Code you have entered whether it exists or have any stock left!!");
		}
	}

	private List<Product> listHelperFunction(HashMap<String, String> listAttributes) throws PageCountOutOfBoundsException, ApplicationErrorException {
		try {
			productList = productService.list(listAttributes);
		} catch(PageCountOutOfBoundsException exception) {
			logger.warn("Error while returning the list. {} ", exception.getMessage());
			throw exception;
		} catch(ApplicationErrorException exception) {
			logger.error("Error while retrieving data from the Database, {} ", exception.getMessage());
			throw exception;
		}
		logger.info("List returned Successfully!");
		return productList;
	}
}
