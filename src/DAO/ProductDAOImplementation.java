package DAO;

import Entity.Product;
import SQLSession.DBHelper;
import SQLSession.MyBatisSession;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImplementation implements ProductDAO {
	private final Connection productConnection = DBHelper.getConnection();
	private SqlSessionFactory sqlSessionFactory;
	private SqlSession sqlSession;
	private ProductDAO productMapper;
	private List<Product> productList = new ArrayList<>();


	@Override
	public Product create(Product product) throws ApplicationErrorException, SQLException, UniqueConstraintException, UnitCodeViolationException {
		try {
			sqlSessionFactory = MyBatisSession.getSqlSessionFactory();
			sqlSession = sqlSessionFactory.openSession();
			productMapper = sqlSession.getMapper(ProductDAO.class);
			Product createdProduct = productMapper.create(product);
			sqlSession.commit();
			sqlSession.close();
			return createdProduct;
		} catch(SQLException e) {
			handleException(e);
			return null;
		}
	}

	/**
	 * Private method to handle SQL Exception and convert it to user readable messages.
	 *
	 * @param e Exception Object
	 * @throws UnitCodeViolationException Custom Exception to convey Foreign Key Violation in Product table.
	 * @throws UniqueConstraintException  Custom Exception to convey Unique constraint Violation in SQL table
	 * @throws SQLException               Exception thrown based on SQL syntax.
	 * @throws ApplicationErrorException  Exception thrown due to Persistence problems.
	 */
	private void handleException(SQLException e) throws UnitCodeViolationException, UniqueConstraintException, SQLException, ApplicationErrorException {
		if(e.getSQLState().equals("23503")) {
			throw new UnitCodeViolationException(">> The unit Code you have entered  does not Exists!!");
		} else if(e.getSQLState().equals("23505")) {
			if(e.getMessage().contains("product_name"))
				throw new UniqueConstraintException(">> Name must be unique!!!\n>> The Name you have entered already exists!!!");
			else
				throw new UniqueConstraintException(">> Code must be unique!!!\n>> The code you have entered already exists!!!");
		} else {
			throw new ApplicationErrorException(">> Application has went into an Error!!!\n>>Please Try again");
		}
	}

	@Override
	public Integer count() throws ApplicationErrorException {
		try {
			sqlSessionFactory = MyBatisSession.getSqlSessionFactory();
			sqlSession = sqlSessionFactory.openSession();
			productMapper = sqlSession.getMapper(ProductDAO.class);
			return productMapper.count();
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}

	}


	public List<Product> searchList(String searchText) throws ApplicationErrorException {
		try {
			sqlSessionFactory = MyBatisSession.getSqlSessionFactory();
			sqlSession = sqlSessionFactory.openSession();
			productMapper = sqlSession.getMapper(ProductDAO.class);
			return productMapper.searchList(searchText);
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}


	@Override
	public List<Product> list(String attribute, String searchText, int pageLength, int offset) throws ApplicationErrorException, PageCountOutOfBoundsException {
		try {
			sqlSessionFactory = MyBatisSession.getSqlSessionFactory();
			sqlSession = sqlSessionFactory.openSession();
			productMapper = sqlSession.getMapper(ProductDAO.class);
			return productMapper.list(attribute, searchText, pageLength, offset);
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}

	private void checkPagination(int count, int offset, int pageLength) throws PageCountOutOfBoundsException {
		if(count <= offset) {
			int pageCount;
			if(count % pageLength == 0) pageCount = count / pageLength;
			else pageCount = (count / pageLength) + 1;
			throw new PageCountOutOfBoundsException(">> Requested Page doesnt Exist!!\n>> Existing Pagecount with given pagination " + pageCount);
		}
	}


	@Override
	public Product edit(Product product) throws SQLException, ApplicationErrorException, UniqueConstraintException, UnitCodeViolationException {
		try {
			sqlSessionFactory = MyBatisSession.getSqlSessionFactory();
			sqlSession = sqlSessionFactory.openSession();
			productMapper = sqlSession.getMapper(ProductDAO.class);
			Product editedProduct = productMapper.edit(product);
			sqlSession.commit();
			sqlSession.close();
			return editedProduct;
		} catch(SQLException e) {
			handleException(e);
			return null;
		}
	}


	@Override
	public Integer delete(String parameter) throws ApplicationErrorException {
		try {
			sqlSessionFactory = MyBatisSession.getSqlSessionFactory();
			sqlSession = sqlSessionFactory.openSession();
			productMapper = sqlSession.getMapper(ProductDAO.class);
			int rowsAffected = productMapper.delete(parameter);
			sqlSession.commit();
			sqlSession.close();
			return rowsAffected;
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}

	@Override
	public Product findByCode(String code) throws ApplicationErrorException {
		try {
			sqlSessionFactory = MyBatisSession.getSqlSessionFactory();
			sqlSession = sqlSessionFactory.openSession();
			productMapper = sqlSession.getMapper(ProductDAO.class);
			return productMapper.findByCode(code);
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}

	public void updateStock(String code, float stock) throws ApplicationErrorException {
		try {
			productConnection.createStatement().executeUpdate("UPDATE PRODUCT SET STOCK=" + stock + " WHERE CODE='" + code + "'");
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}
}
