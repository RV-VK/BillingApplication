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


	@GetMapping(path = "/products", produces = "application/json")
	public List<Product> getAll() throws PageCountOutOfBoundsException, ApplicationErrorException {
		listAttributes.put("Pagelength", String.valueOf(Integer.MAX_VALUE));
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", "id");
		listAttributes.put("Searchtext", null);
		return productService.list(listAttributes);
	}

	@GetMapping(path = "/products/{pageLength}", produces = "application/json")
	public List<Product> getByPageLength(@PathVariable String pageLength) throws InvalidTemplateException, PageCountOutOfBoundsException, ApplicationErrorException {
		listAttributes.put("Pagelength", pageLength);
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", "id");
		listAttributes.put("Searchtext", null);
		validator.validate(listAttributes, ProductController.class);
		return productService.list(listAttributes);
	}

	@GetMapping(path = "/products/{pageLength}/{pageNumber}", produces = "application/json")
	public List<Product> getByPageLengthAndPageNumber(@PathVariable String pageLength, @PathVariable String pageNumber) throws InvalidTemplateException, PageCountOutOfBoundsException, ApplicationErrorException {
		listAttributes.put("Pagelength", pageLength);
		listAttributes.put("Pagenumber", pageNumber);
		listAttributes.put("Attribute", "id");
		listAttributes.put("Searchtext", null);
		validator.validate(listAttributes, ProductController.class);
		return productService.list(listAttributes);
	}

	@GetMapping(path = "/products/find/{searchText}", produces = "application/json")
	public List<Product> getBySearchText(@PathVariable String searchText) throws PageCountOutOfBoundsException, ApplicationErrorException {
		listAttributes.put("Pagelength", null);
		listAttributes.put("Pagenumber", null);
		listAttributes.put("Attribute", null);
		listAttributes.put("Searchtext", searchText);
		return productService.list(listAttributes);
	}

	@GetMapping(path = "/products/find/{attribute}/{searchText}", produces = "application/json")
	public List<Product> getByAttributeAndSearchText(@PathVariable String attribute, @PathVariable String searchText) throws InvalidTemplateException, PageCountOutOfBoundsException, ApplicationErrorException {
		listAttributes.put("Pagelength", String.valueOf(Integer.MAX_VALUE));
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", attribute);
		listAttributes.put("Searchtext", searchText);
		validator.validate(listAttributes, ProductController.class);
		return productService.list(listAttributes);
	}

	@GetMapping(path = "/products/find/{attribute}/{searchText}/{PageLength}", produces = "application/json")
	public List<Product> getByAttributeAndSearchTextWithPageLength(@PathVariable String attribute, @PathVariable String searchText, @PathVariable String PageLength) throws InvalidTemplateException, PageCountOutOfBoundsException, ApplicationErrorException {
		listAttributes.put("Pagelength", PageLength);
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", attribute);
		listAttributes.put("Searchtext", searchText);
		validator.validate(listAttributes, ProductController.class);
		return productService.list(listAttributes);
	}

	@GetMapping(path = "/products/find/{attribute}/{searchText}/{PageLength}/{PageNumber}", produces = "application/json")
	public List<Product> getByAttributeAndSearchTextWithPageLengthAndPageNumber(@PathVariable String attribute, @PathVariable String searchText, @PathVariable String PageLength, @PathVariable String PageNumber) throws InvalidTemplateException, PageCountOutOfBoundsException, ApplicationErrorException {
		listAttributes.put("Pagelength", PageLength);
		listAttributes.put("Pagenumber", PageNumber);
		listAttributes.put("Attribute", attribute);
		listAttributes.put("Searchtext", searchText);
		validator.validate(listAttributes, ProductController.class);
		return productService.list(listAttributes);
	}

	@PostMapping(path = "/product", produces = "application/json")
	public Product add(@RequestBody Product product) throws Exception {
		return productService.create(product);
	}

	@GetMapping(path = "/countProducts", produces = "application/json")
	public Integer count() throws ApplicationErrorException {
		String attribute = "id";
		String searchText = null;
		return productService.count(attribute, searchText);
	}

	@PutMapping(path = "/product", produces = "application/json")
	public Product edit(@RequestBody Product product) throws Exception {
		Product editedProduct = productService.edit(product);
		if(editedProduct == null)
			throw new InvalidTemplateException("The Id doesnt exist to edit! Please Give An Existing Id");
		else
			return editedProduct;

	}

	@DeleteMapping(path = "/deleteProduct/{parameter}", produces = "application/json")
	public Integer delete(@PathVariable String parameter) throws ApplicationErrorException, InvalidTemplateException {
		Integer statusCode;
		statusCode = productService.delete(parameter);
		if(statusCode == 1)
			return statusCode;
		else
			throw new InvalidTemplateException("Please check the Id (or) Code you have entered whether it exists or have any stock left!!");
	}

}
