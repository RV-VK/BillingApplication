package DAO;

import Entity.Purchase;
import Entity.PurchaseItem;
import SQLSession.MyBatisSession;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PurchaseDAOImplementation implements PurchaseDAO {
	private final SqlSessionFactory sqlSessionFactory = MyBatisSession.getSqlSessionFactory();
	private final ProductDAOImplementation productDAO = new ProductDAOImplementation();
	private SqlSession sqlSession;
	private PurchaseDAO purchaseMapper;
	private PurchaseItemMapper purchaseItemMapper;
	private List<PurchaseItem> purchaseItemList = new ArrayList<>();

	@Override
	public Purchase create(Purchase purchase) throws ApplicationErrorException, SQLException, UniqueConstraintException, UnitCodeViolationException {
		try {
			sqlSession = sqlSessionFactory.openSession();
			purchaseMapper = sqlSession.getMapper(PurchaseDAO.class);
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
			return createdPurchase;
		} catch(PersistenceException e) {
			Throwable cause = e.getCause();
			SQLException sqlException = (SQLException)cause;
			if(sqlException.getSQLState().equals("23505"))
				throw new UniqueConstraintException(">> Invoice Id already Exists!!");
			throw new ApplicationErrorException(e.getMessage());
		}
	}

	@Override
	public Integer count(String attribute, Object searchText) throws ApplicationErrorException {
		try {
			sqlSession = sqlSessionFactory.openSession();
			purchaseMapper = sqlSession.getMapper(PurchaseDAO.class);
			if(attribute.equals("date"))
				return purchaseMapper.count(attribute, Date.valueOf(String.valueOf(searchText)));
			else
				return purchaseMapper.count(attribute, searchText);
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}

	@Override
	public List<Purchase> list(String attribute, Object searchText, int pageLength, int offset) throws ApplicationErrorException {
		List<Purchase> listedPurchase;
		Date dateParameter = null;
		sqlSession = sqlSessionFactory.openSession();
		purchaseMapper = sqlSession.getMapper(PurchaseDAO.class);
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
			return listedPurchase;
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
	public List<Purchase> searchList(String searchText) throws ApplicationErrorException {
		try {
			sqlSession = sqlSessionFactory.openSession();
			purchaseMapper = sqlSession.getMapper(PurchaseDAO.class);
			purchaseItemMapper = sqlSession.getMapper(PurchaseItemMapper.class);
			List<Purchase> listedPurchase = purchaseMapper.searchList(searchText);
			List<PurchaseItem> listedPurchaseItems;
			for(Purchase purchase: listedPurchase) {
				listedPurchaseItems = purchaseItemMapper.list(purchase.getInvoice());
				purchase.setPurchaseItemList(listedPurchaseItems);
			}
			return listedPurchase;
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}

	@Override
	public Integer delete(int invoice) throws ApplicationErrorException {
		try {
			sqlSession = sqlSessionFactory.openSession();
			purchaseMapper = sqlSession.getMapper(PurchaseDAO.class);
			purchaseItemMapper = sqlSession.getMapper(PurchaseItemMapper.class);
			int purchaseItemDeleted = purchaseItemMapper.delete(invoice);
			int purchaseDeleted = purchaseMapper.delete(invoice);
			if(purchaseItemDeleted > 0 && purchaseDeleted > 0) return 1;
			else return - 1;
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}
}
