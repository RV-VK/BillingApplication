package org.example.Service;

import org.example.DAO.ApplicationErrorException;
import org.example.DAO.PageCountOutOfBoundsException;
import org.example.DAO.ProductDAO;
import org.example.Entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
public class ProductServiceImplementation implements ProductService {

	private final String NAME_REGEX = "^[a-zA-Z\\s]{3,30}$";
	private final String CODE_REGEX = "^[a-zA-Z0-9]{2,6}$";
	private final String UNIT_CODE_REGEX = "^[a-zA-Z]{1,4}$";
	@Autowired
	private ProductDAO productDAO;

	public Product create(Product product) throws Exception {
		validate(product);
		return productDAO.create(product);
	}

	public Integer count(String attribute, String searchText) throws ApplicationErrorException {
		return productDAO.count(attribute, searchText);
	}

	public List<Product> list(HashMap<String, String> listattributes) throws ApplicationErrorException, PageCountOutOfBoundsException {
		List<Product> productList;
		if(Collections.frequency(listattributes.values(), null) == listattributes.size() - 1 && listattributes.get("Searchtext") != null) {
			productList = productDAO.searchList(listattributes.get("Searchtext"));
		} else {
			int pageLength = Integer.parseInt(listattributes.get("Pagelength"));
			int pageNumber = Integer.parseInt(listattributes.get("Pagenumber"));
			int offset = (pageLength * pageNumber) - pageLength;
			productList = productDAO.list(listattributes.get("Attribute"), listattributes.get("Searchtext"), pageLength, offset);
		}
		return productList;
	}

	public Product edit(Product product) throws Exception {
		validate(product);
		return productDAO.edit(product);
	}

	public Integer delete(String parameter) throws ApplicationErrorException {
		if(parameter != null)
			return productDAO.delete(parameter);
		else
			return - 1;
	}

	/**
	 * This method validates the Product attributes.
	 *
	 * @param product Product to be Validated
	 */
	private void validate(Product product) throws InvalidTemplateException {
		if(product == null)
			throw new NullPointerException("Product cannot be Null!!");
		if(product.getCode() != null && ! product.getCode().matches(CODE_REGEX))
			throw new InvalidTemplateException("Invalid Product Code!!");
		else if(product.getCode() == null)
			throw new NullPointerException("Product Code Cannot be null!");
		if(product.getName() != null && ! product.getName().matches(NAME_REGEX))
			throw new InvalidTemplateException("Invalid Product Name!");
		else if(product.getName() == null)
			throw new NullPointerException("Product Name cannot be null");
		if(product.getunitcode() != null && ! product.getunitcode().matches(UNIT_CODE_REGEX))
			throw new InvalidTemplateException("Invalid Unit Code!!");
		else if(product.getunitcode() == null)
			throw new NullPointerException("UnitCode cannot be null");
		if(product.getType() != null && ! product.getType().matches(NAME_REGEX))
			throw new InvalidTemplateException("Invalid Product Type!!");
		else if(product.getType() == null)
			throw new NullPointerException("Product Type Cannot be null!");
		if(product.getStock() == null)
			throw new NullPointerException("Product Stock cannot be null");
		if(product.getPrice() == null)
			throw new NullPointerException("Product Price Cannot be null!");
	}
}
