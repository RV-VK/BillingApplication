package Service;

import DAO.*;
import Entity.Product;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ProductServiceImplementation implements ProductService {

	private final ProductDAO productDAO = new ProductDAO();
	private final String NAME_REGEX = "^[a-zA-Z\\s]{3,30}$";
	private final String CODE_REGEX = "^[a-zA-Z0-9]{2,6}$";
	private final String UNIT_CODE_REGEX = "^[a-zA-Z]{1,4}$";

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
			return -1;
	}

	/**
	 * This method validates the Product attributes.
	 *
	 * @param product Product to be Validated
	 */
	private void validate(Product product) throws InvalidTemplateException {
		if(product == null)
			throw new NullPointerException(">> Product cannot be Null!!");
		if(product.getCode() != null && ! product.getCode().matches(CODE_REGEX))
			throw new InvalidTemplateException(">> Invalid Product Code!!");
		if(product.getName() != null && ! product.getName().matches(NAME_REGEX))
			throw new InvalidTemplateException(">> Invalid Product Name!");
		if(product.getunitcode() != null && ! product.getunitcode().matches(UNIT_CODE_REGEX))
			throw new InvalidTemplateException(">> Invalid Unit Code!!");
		if(product.getType() != null && ! product.getType().matches(NAME_REGEX))
			throw new InvalidTemplateException(">> Invalid Product Type!!");
	}
}
