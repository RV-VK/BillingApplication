package DAO;

import Entity.Product;
import SQLSession.MyBatisSession;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.sql.SQLException;
import java.util.List;

public class ProductDAOImplementation implements ProductDAO {
	private final SqlSessionFactory sqlSessionFactory = MyBatisSession.getSqlSessionFactory();
	private final SqlSession sqlSession = sqlSessionFactory.openSession();
	private final ProductDAO productMapper = sqlSession.getMapper(ProductDAO.class);

	@Override
	public Product create(Product product) throws ApplicationErrorException, SQLException, UniqueConstraintException, UnitCodeViolationException {
		try {
			return productMapper.create(product);
		} catch(PersistenceException e) {
			Throwable cause = e.getCause();
			handleException((SQLException)cause);
			return null;
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
	private void handleException(SQLException e) throws UnitCodeViolationException, UniqueConstraintException, ApplicationErrorException {
		if(e.getSQLState().equals("23503")) {
			throw new UnitCodeViolationException(">> The unit Code you have entered  does not Exists!!");
		} else if(e.getSQLState().equals("23505")) {
			if(e.getMessage().contains("product_name"))
				throw new UniqueConstraintException(">> Name must be unique!!!\n>> The Name you have entered already exists!!!");
			else
				throw new UniqueConstraintException(">> Code must be unique!!!\n>> The code you have entered already exists!!!");
		} else {
			throw new ApplicationErrorException(e.getMessage());
		}
	}

	@Override
	public Integer count(String attribute, Object searchText) throws ApplicationErrorException {
		try {
			return productMapper.count(attribute, searchText);
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}

	}

	public List<Product> searchList(String searchText) throws ApplicationErrorException {
		try {
			return productMapper.searchList(searchText);
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}


	@Override
	public List<Product> list(String attribute, Object searchText, int pageLength, int offset) throws ApplicationErrorException, PageCountOutOfBoundsException {
		try {
			if(searchText != null && String.valueOf(searchText).matches("^\\d+(\\.\\d+)?$")) {
				Double numericParameter = Double.parseDouble((String)searchText);
				Integer count = productMapper.count(attribute, numericParameter);
				checkPagination(count, offset, pageLength);
				return productMapper.list(attribute, numericParameter, pageLength, offset);
			} else {
				Integer count = productMapper.count(attribute, searchText);
				checkPagination(count, offset, pageLength);
				return productMapper.list(attribute, searchText, pageLength, offset);
			}
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}

	private void checkPagination(int count, int offset, int pageLength) throws PageCountOutOfBoundsException {
		if(count <= offset && count != 0) {
			int pageCount;
			if(count % pageLength == 0) pageCount = count / pageLength;
			else pageCount = (count / pageLength) + 1;
			throw new PageCountOutOfBoundsException(">> Requested Page doesnt Exist!!\n>> Existing Pagecount with given pagination " + pageCount);
		}
	}

	@Override
	public Product edit(Product product) throws SQLException, ApplicationErrorException, UniqueConstraintException, UnitCodeViolationException {
		try {
			return productMapper.edit(product);
		} catch(PersistenceException e) {
			Throwable cause = e.getCause();
			handleException((SQLException)cause);
			return null;
		}
	}

	@Override
	public Integer delete(String parameter) throws ApplicationErrorException {
		try {
			return productMapper.delete(parameter);
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}

	@Override
	public Product findByCode(String code) throws ApplicationErrorException {
		try {
			return productMapper.findByCode(code);
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}

}
