package Service;

import DAO.*;
import Entity.Product;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ProductServiceImplementation implements ProductService {

	private final ProductDAO productDAO = new ProductDAOImplementation();
	private final String NAME_REGEX = "^[a-zA-Z\\s]{1,30}$";
	private final String CODE_REGEX = "^[a-zA-Z0-9]{2,6}$";
	private final String UNIT_CODE_REGEX = "^[a-zA-Z]{1,4}$";

	public Product create(Product product)
			throws SQLException,
			ApplicationErrorException,
			UniqueConstraintException,
			UnitCodeViolationException,
			InvalidTemplateException  {
		validate(product);
		return productDAO.create(product);
	}

	public int count() throws ApplicationErrorException {
		return productDAO.count();
	}

	public List<Product> list(HashMap<String, String> listattributes)
			throws ApplicationErrorException, PageCountOutOfBoundsException {
		List<Product> productList;
		if(Collections.frequency(listattributes.values(), null) == 0 || Collections.frequency(listattributes.values(), null) == 1) {
			int pageLength = Integer.parseInt(listattributes.get("Pagelength"));
			int pageNumber = Integer.parseInt(listattributes.get("Pagenumber"));
			int offset = (pageLength * pageNumber) - pageLength;
			productList =
					productDAO.list(
							listattributes.get("Attribute"),
							listattributes.get("Searchtext"),
							pageLength,
							offset);
			return productList;
		} else if(Collections.frequency(listattributes.values(), null) == listattributes.size() - 1
				&& listattributes.get("Searchtext") != null) {
			productList = productDAO.list(listattributes.get("Searchtext"));
			return productList;
		}
		return null;
	}

	public Product edit(Product product)
			throws SQLException,
			ApplicationErrorException,
			UniqueConstraintException,
			UnitCodeViolationException,
			InvalidTemplateException {
		validate(product);
		return productDAO.edit(product);
	}

	public int delete(String parameter) throws ApplicationErrorException {
		return productDAO.delete(parameter);
	}

	/**
	 * This method validates the Product attributes.
	 *
	 * @param product Product to be Validated
	 * @return status - Boolean.
	 */
	private boolean validate(Product product) throws InvalidTemplateException {
		if(product.getCode() != null && ! product.getCode().matches(CODE_REGEX))
			throw new InvalidTemplateException(">> Invalid Product Code!!");
		if(product.getName() != null && ! product.getName().matches(NAME_REGEX))
			throw new InvalidTemplateException(">> Invalid Product Name!");
		if(product.getunitcode() != null && ! product.getunitcode().matches(UNIT_CODE_REGEX))
			throw new InvalidTemplateException(">> Invalid Unit Code!!");
		if(product.getType() != null && ! product.getType().matches(NAME_REGEX))
			throw new InvalidTemplateException(">> Invalid Product Type!!");
		return true;
	}
}
