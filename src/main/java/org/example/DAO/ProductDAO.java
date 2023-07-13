package org.example.DAO;

import org.apache.ibatis.exceptions.PersistenceException;
import org.example.Entity.Product;
import org.example.Mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

@Component
public class ProductDAO {
	@Autowired
	private ProductMapper productMapper;

	/**
	 * This method creates an entry in the Product table
	 *
	 * @param product Input product
	 * @return Product - Created product.
	 * @throws Exception Throws Variable Exceptions namely SQLException, UnitCodeViolationException,  UniqueConstraintException.
	 */
	public Product create(Product product) throws Exception {
		try {
			return productMapper.create(product);
		} catch(Exception e) {
			Throwable cause = e.getCause();
			throw handleException((SQLException)cause);
		}
	}

	/**
	 * Private method to handle SQL Exception and convert it to user readable messages.
	 *
	 * @param e Exception Object
	 * @throws UnitCodeViolationException Custom Exception to convey Foreign Key Violation in Product table.
	 * @throws UniqueConstraintException  Custom Exception to convey Unique constraint Violation in SQL table
	 * @throws ApplicationErrorException  Exception thrown due to Persistence problems.
	 */
	private Exception handleException(SQLException e) throws UnitCodeViolationException, UniqueConstraintException, ApplicationErrorException {
		if(e.getSQLState().equals("23503")) {
			throw new UnitCodeViolationException("The unit Code you have entered  does not Exists!");
		} else if(e.getSQLState().equals("23505")) {
			if(e.getMessage().contains("product_name"))
				throw new UniqueConstraintException("Name must be unique!\nThe Name you have entered already exists");
			else
				throw new UniqueConstraintException("Code must be unique! \nThe code you have entered already exists");
		} else {
			throw new ApplicationErrorException(e.getMessage());
		}
	}

	/**
	 * This Method returns the number of entries in the Product table.
	 *
	 * @param attribute  Column to be counted.
	 * @param searchText Field to be counted.
	 * @return count
	 * @throws ApplicationErrorException Exception thrown due to persistence problems
	 */
	public Integer count(String attribute, Object searchText) throws ApplicationErrorException {
		try {
			Integer count;
			if(searchText != null && String.valueOf(searchText).matches("^\\d+(\\.\\d+)?$")) {
				Double numericParameter = Double.parseDouble((String)searchText);
				count = productMapper.count(attribute, numericParameter);
			} else {
				count = productMapper.count(attribute, searchText);
			}
			return count;
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}

	}

	/**
	 * This method Lists the products in the product table based on the given search-text.
	 *
	 * @param searchText The search-text that must be found.
	 * @return List of Products
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	public List<Product> searchList(String searchText) throws ApplicationErrorException {
		try {
			return productMapper.searchList(searchText);
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}

	/**
	 * This method lists the products in the product table based on the given searchable attribute and
	 * its corresponding search-text formatted in a pageable manner.
	 *
	 * @param attribute  The attribute to be looked upon
	 * @param searchText The search-text to be found.
	 * @param pageLength The number of entries that must be listed.
	 * @param offset     The Page number that has to be listed.
	 * @return List of Products
	 * @throws ApplicationErrorException Exception thrown due to persistence problems
	 */
	public List<Product> list(String attribute, Object searchText, int pageLength, int offset) throws ApplicationErrorException {
		try {
			Integer count;
			List<Product> productList;
			if(searchText != null && String.valueOf(searchText).matches("^\\d+(\\.\\d+)?$")) {
				Double numericParameter = Double.parseDouble((String)searchText);
				count = productMapper.count(attribute, numericParameter);
				checkPagination(count, offset, pageLength);
				productList = productMapper.list(attribute, numericParameter, pageLength, offset);
			} else {
				count = productMapper.count(attribute, searchText);
				checkPagination(count, offset, pageLength);
				productList = productMapper.list(attribute, searchText, pageLength, offset);
			}
			return productList;
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}

	/**
	 * Private method to check whether the given Pagenumber is Valid or Not exists.
	 *
	 * @param count      Total Count of entries.
	 * @param offset     Index from which the Entries are requested.
	 * @param pageLength Length for Each page.
	 * @throws PageCountOutOfBoundsException Exception thrown in a pageable list function if a
	 *                                       non-existing page is prompted.
	 */
	private void checkPagination(int count, int offset, int pageLength) throws PageCountOutOfBoundsException {
		if(count <= offset && count != 0) {
			int pageCount;
			if(count % pageLength == 0) pageCount = count / pageLength;
			else pageCount = (count / pageLength) + 1;
			throw new PageCountOutOfBoundsException("Requested Page doesnt Exist!!\nExisting Pagecount with given pagination " + pageCount);
		}
	}

	/**
	 * This method updates the attributes of the product entry in the Product table
	 *
	 * @param product The Updated Product entry
	 * @return Product - Result Product
	 * @throws Exception Throws Variable Exceptions namely SQLException, UnitCodeViolationException, UniqueConstraintException.
	 */
	public Product edit(Product product) throws Exception {
		try {
			return productMapper.edit(product);
		} catch(Exception e) {
			Throwable cause = e.getCause();
			throw handleException((SQLException)cause);
		}
	}

	/**
	 * This method deletes an entry in the Product table based on the given parameter.
	 *
	 * @param parameter Input parameter based on which the row is selected to delete.v
	 * @return Integer - resultCode
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	public Integer delete(String parameter) throws ApplicationErrorException {
		try {
			return productMapper.delete(parameter);
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}

	/**
	 * This method finds the Product by its product code attribute.
	 *
	 * @param code Input product code.
	 * @return Product
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	public Product findByCode(String code) throws ApplicationErrorException {
		try {
			return productMapper.findByCode(code);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			throw new ApplicationErrorException(e.getMessage());
		}
	}
}
