package DAO;

import Entity.Purchase;
import Entity.PurchaseItem;
import Mapper.PurchaseItemMapper;
import Mapper.PurchaseMapper;
import SQLSession.MyBatisSession;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PurchaseDAO {
	private final SqlSessionFactory sqlSessionFactory = MyBatisSession.getSqlSessionFactory();
	private final ProductDAO productDAO = new ProductDAO();
	private SqlSession sqlSession;
	private PurchaseMapper purchaseMapper;
	private PurchaseItemMapper purchaseItemMapper;
	private final List<PurchaseItem> purchaseItemList = new ArrayList<>();

	/**
	 * This method is a composite function that creates an entry in both Purchase and PurchaseItems table.
	 *
	 * @param purchase Purchase to be entered.
	 * @return Purchase - Created Purchase Entry.
	 * @throws Exception Throws Variable Exceptions namely SQLException, UnitCodeViolationException, UniqueConstraintException.
	 */
	public Purchase create(Purchase purchase) throws Exception {
		try {
			sqlSession = sqlSessionFactory.openSession();
			purchaseMapper = sqlSession.getMapper(PurchaseMapper.class);
			purchaseItemMapper = sqlSession.getMapper(PurchaseItemMapper.class);
			purchaseItemList.clear();
			Purchase createdPurchase = purchaseMapper.create(purchase);
			for(PurchaseItem purchaseItem: purchase.getPurchaseItemList()) {
				PurchaseItem createPurchaseItem = purchaseItemMapper.create(purchaseItem, createdPurchase.getInvoice());
				createPurchaseItem.setProduct(purchaseItem.getProduct());
				purchaseItemList.add(createPurchaseItem);
				purchaseItem.getProduct().setAvailableQuantity(purchaseItem.getProduct().getAvailableQuantity() + purchaseItem.getQuantity());
				productDAO.edit(purchaseItem.getProduct());
			}
			createdPurchase.setPurchaseItemList(purchaseItemList);
			sqlSession.close();
			return createdPurchase;
		} catch(PersistenceException e) {
			Throwable cause = e.getCause();
			throw handleException((SQLException)cause);
		}
	}

	/**
	 * Private method to convert SQL Exception to user readable messages.
	 *
	 * @param sqlException Exception Object.
	 * @throws UniqueConstraintException Custom Exception to convey Unique constraint Violation in SQL table.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	private Exception handleException(SQLException sqlException) throws UniqueConstraintException, ApplicationErrorException {
		if(sqlException.getSQLState().equals("23505"))
			throw new UniqueConstraintException(">> Invoice Id already Exists!!");
		throw new ApplicationErrorException(sqlException.getMessage());
	}

	/**
	 * This method counts the number of entries in the Purchase table based on date parameter.
	 *
	 * @param attribute  Column to be counted.
	 * @param searchText Field to be counted.
	 * @return Integer - count.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	public Integer count(String attribute, Object searchText) throws ApplicationErrorException {
		try {
			Integer count;
			sqlSession = sqlSessionFactory.openSession();
			purchaseMapper = sqlSession.getMapper(PurchaseMapper.class);
			if(attribute.equals("date"))
				count = purchaseMapper.count(attribute, Date.valueOf(String.valueOf(searchText)));
			else count = purchaseMapper.count(attribute, searchText);
			sqlSession.close();
			return count;
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}

	/**
	 * This method Lists the Purchase and PurchaseItem entries based on the given searchable attribute
	 * and its corresponding search-text formatted in a pageable manner.
	 *
	 * @param attribute  The attribute to be looked upon.
	 * @param searchText The searchtext to be found.
	 * @param pageLength The number of entries that must be listed.
	 * @param offset     The Page number to be listed.
	 * @return List - Purchase.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	public List<Purchase> list(String attribute, Object searchText, int pageLength, int offset) throws ApplicationErrorException {
		List<Purchase> listedPurchase;
		Date dateParameter = null;
		sqlSession = sqlSessionFactory.openSession();
		purchaseMapper = sqlSession.getMapper(PurchaseMapper.class);
		purchaseItemMapper = sqlSession.getMapper(PurchaseItemMapper.class);
		try {
			if(searchText != null && String.valueOf(searchText).matches("^\\d+(\\.\\d+)?$")) {
				Integer numericParameter = Integer.parseInt(String.valueOf(searchText));
				Integer count = purchaseMapper.count(attribute, numericParameter);
				checkPagination(count, offset, pageLength);
				listedPurchase = purchaseMapper.list(attribute, numericParameter, pageLength, offset);
			} else {
				if(searchText != null) dateParameter = Date.valueOf(String.valueOf(searchText));
				Integer count = purchaseMapper.count(attribute, dateParameter);
				checkPagination(count, offset, pageLength);
				listedPurchase = purchaseMapper.list(attribute, dateParameter, pageLength, offset);
			}
			List<PurchaseItem> listedPurchaseItems;
			for(Purchase purchase: listedPurchase) {
				listedPurchaseItems = purchaseItemMapper.list(purchase.getInvoice());
				purchase.setPurchaseItemList(listedPurchaseItems);
			}
			sqlSession.close();
			return listedPurchase;
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
			throw new PageCountOutOfBoundsException(">> Requested Page doesnt Exist!!\n>> Existing Pagecount with given pagination " + pageCount);
		}
	}

	/**
	 * This method lists the entries in the Purchase and PurchaseItems table based on the given search-text.
	 *
	 * @param searchText The search-text to be found.
	 * @return List - Purchase
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	public List<Purchase> searchList(String searchText) throws ApplicationErrorException {
		try {
			sqlSession = sqlSessionFactory.openSession();
			purchaseMapper = sqlSession.getMapper(PurchaseMapper.class);
			purchaseItemMapper = sqlSession.getMapper(PurchaseItemMapper.class);
			List<Purchase> listedPurchase = purchaseMapper.searchList(searchText);
			List<PurchaseItem> listedPurchaseItems;
			for(Purchase purchase: listedPurchase) {
				listedPurchaseItems = purchaseItemMapper.list(purchase.getInvoice());
				purchase.setPurchaseItemList(listedPurchaseItems);
			}
			sqlSession.close();
			return listedPurchase;
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}

	/**
	 * This method deletes an entry in the Purchase table and the corresponding entries in the purchase-items table
	 *
	 * @param invoice Input invoice to perform delete.
	 * @return resultCode - Integer.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	public Integer delete(int invoice) throws ApplicationErrorException {
		try {
			sqlSession = sqlSessionFactory.openSession();
			purchaseMapper = sqlSession.getMapper(PurchaseMapper.class);
			purchaseItemMapper = sqlSession.getMapper(PurchaseItemMapper.class);
			int purchaseItemDeleted = purchaseItemMapper.delete(invoice);
			int purchaseDeleted = purchaseMapper.delete(invoice);
			sqlSession.close();
			if(purchaseItemDeleted > 0 && purchaseDeleted > 0) return 1;
			else return - 1;
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}
}
