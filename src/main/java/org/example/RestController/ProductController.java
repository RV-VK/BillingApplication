package org.example.RestController;

import org.example.DAO.ApplicationErrorException;
import org.example.DAO.PageCountOutOfBoundsException;
import org.example.Entity.Product;
import org.example.Service.InvalidTemplateException;
import org.example.Service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@RestController
public class ProductController {
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
	@Autowired
	private ProductService productService;
	private List<Product> productList;
	private HashMap<String, String> listAttributes = new HashMap<>();


	@GetMapping(path = "/products", produces = "application/json")
	public List<Product> getAll() throws PageCountOutOfBoundsException, ApplicationErrorException {
		listAttributes.put("Pagelength", String.valueOf(Integer.MAX_VALUE));
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", "id");
		listAttributes.put("Searchtext", null);
		try {
			productList = productService.list(listAttributes);
		} catch(ApplicationErrorException | PageCountOutOfBoundsException e) {
			logger.error(e.getMessage());
			throw e;
		}
		return productList;
	}

	@GetMapping(path = "/products/{pageLength}", produces = "application/json")
	public List<Product> getByPageLength(@PathVariable String pageLength) throws InvalidTemplateException, PageCountOutOfBoundsException, ApplicationErrorException {
		listAttributes.put("Pagelength", pageLength);
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", "id");
		listAttributes.put("Searchtext", null);
		validate(listAttributes);
		try {
			productList = productService.list(listAttributes);
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return productList;
	}

	@GetMapping(path = "/products/{pageLength}/{pageNumber}", produces = "application/json")
	public List<Product> getByPageLengthAndPageNumber(@PathVariable String pageLength, @PathVariable String pageNumber) throws InvalidTemplateException, PageCountOutOfBoundsException, ApplicationErrorException {
		listAttributes.put("Pagelength", pageLength);
		listAttributes.put("Pagenumber", pageNumber);
		listAttributes.put("Attribute", "id");
		listAttributes.put("Searchtext", null);
		validate(listAttributes);
		try {
			productList = productService.list(listAttributes);
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return productList;
	}

	@GetMapping(path = "/products/find/{searchText}", produces = "application/json")
	public List<Product> getBySearchText(@PathVariable String searchText) throws PageCountOutOfBoundsException, ApplicationErrorException {
		listAttributes.put("Pagelength", null);
		listAttributes.put("Pagenumber", null);
		listAttributes.put("Attribute", null);
		listAttributes.put("Searchtext", searchText);
		try {
			productList = productService.list(listAttributes);
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return productList;
	}

	@GetMapping(path = "/products/find/{attribute}/{searchText}", produces = "application/json")
	public List<Product> getByAttributeAndSearchText(@PathVariable String attribute, @PathVariable String searchText) throws InvalidTemplateException, PageCountOutOfBoundsException, ApplicationErrorException {
		listAttributes.put("Pagelength", "20");
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", attribute);
		listAttributes.put("Searchtext", searchText);
		validate(listAttributes);
		try {
			productList = productService.list(listAttributes);
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return productList;
	}

	@GetMapping(path = "/products/find/{attribute}/{searchText}/{PageLength}", produces = "application/json")
	public List<Product> getByAttributeAndSearchTextWithPageLength(@PathVariable String attribute, @PathVariable String searchText, @PathVariable String PageLength) throws InvalidTemplateException, PageCountOutOfBoundsException, ApplicationErrorException {
		listAttributes.put("Pagelength", PageLength);
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", attribute);
		listAttributes.put("Searchtext", searchText);
		validate(listAttributes);
		try {
			productList = productService.list(listAttributes);
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return productList;
	}

	@GetMapping(path = "/products/find/{attribute}/{searchText}/{PageLength}/{PageNumber}", produces = "application/json")
	public List<Product> getByAttributeAndSearchTextWithPageLengthAndPageNumber(@PathVariable String attribute, @PathVariable String searchText, @PathVariable String PageLength, @PathVariable String PageNumber) throws InvalidTemplateException, PageCountOutOfBoundsException, ApplicationErrorException {
		listAttributes.put("Pagelength", PageLength);
		listAttributes.put("Pagenumber", PageNumber);
		listAttributes.put("Attribute", attribute);
		listAttributes.put("Searchtext", searchText);
		validate(listAttributes);
		try {
			productList = productService.list(listAttributes);
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return productList;
	}

	@PostMapping(path = "/product", produces = "application/json")
	public Product add(@RequestBody Product product) throws Exception {
		Product createdProduct;
		try {
			createdProduct = productService.create(product);
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return createdProduct;
	}

	@GetMapping(path = "/countProducts", produces = "application/json")
	public Integer countProducts() throws ApplicationErrorException {
		String attribute = "id";
		String searchText = null;
		Integer count;
		try {
			count = productService.count(attribute, searchText);
		} catch(ApplicationErrorException e) {
			logger.error(e.getMessage());
			throw e;
		}
		return count;
	}

	@PutMapping(path = "/product", produces = "application/json")
	public Product edit(@RequestBody Product product) throws Exception {
		Product editedProduct;
		try {
			editedProduct = productService.edit(product);
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return editedProduct;
	}

	@DeleteMapping(path = "/deleteProduct/{parameter}", produces = "application/json")
	public Integer deleteProduct(@PathVariable String parameter) throws ApplicationErrorException {
		try {
			return productService.delete(parameter);
		} catch(ApplicationErrorException e) {
			logger.error(e.getMessage());
			throw e;
		}
	}

	private void validate(HashMap<String, String> listAttributes) throws InvalidTemplateException {
		String NUMBER_REGEX = "^\\d+$";
		List<String> productAttributes = Arrays.asList("id", "code", "name", "unitcode", "type", "price", "stock");
		if(! listAttributes.get("Pagelength").matches(NUMBER_REGEX)) {
			System.out.println(listAttributes.get("Pagelength"));
			throw new InvalidTemplateException("Pagelength Invalid! It must be number");
		}
		if(! listAttributes.get("Pagenumber").matches(NUMBER_REGEX))
			throw new InvalidTemplateException("Pagenumber Invalid! It mus be a number");
		if(listAttributes.get("Attribute") != null && ! productAttributes.contains(listAttributes.get("Attribute")))
			throw new InvalidTemplateException("Given Attribute is not A Searchable Attribute in Product");
	}
}
