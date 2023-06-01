package DAO;

import Entity.Purchase;
import Entity.PurchaseItem;
import Mapper.PurchaseMapper;
import Mapper.PurchaseItemMapper;
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
	private final SqlSession sqlSession = sqlSessionFactory.openSession();
	private final PurchaseMapper purchaseMapper = sqlSession.getMapper(PurchaseMapper.class);
	private final PurchaseItemMapper purchaseItemMapper = sqlSession.getMapper(PurchaseItemMapper.class);
	private List<PurchaseItem> purchaseItemList = new ArrayList<>();

	public Purchase create(Purchase purchase) throws Exception {
		try {
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
			throw handleException((SQLException)cause);
		}
	}

	private Exception handleException(SQLException sqlException) throws UniqueConstraintException, ApplicationErrorException {
		if(sqlException.getSQLState().equals("23505"))
			throw new UniqueConstraintException(">> Invoice Id already Exists!!");
		throw new ApplicationErrorException(sqlException.getMessage());
	}


	public Integer count(String attribute, Object searchText) throws ApplicationErrorException {
		try {
			if(attribute.equals("date"))
				return purchaseMapper.count(attribute, Date.valueOf(String.valueOf(searchText)));
			else return purchaseMapper.count(attribute, searchText);
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}

	public List<Purchase> list(String attribute, Object searchText, int pageLength, int offset) throws ApplicationErrorException {
		List<Purchase> listedPurchase;
		Date dateParameter = null;
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

	public List<Purchase> searchList(String searchText) throws ApplicationErrorException {
		try {
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

	public Integer delete(int invoice) throws ApplicationErrorException {
		try {
			int purchaseItemDeleted = purchaseItemMapper.delete(invoice);
			int purchaseDeleted = purchaseMapper.delete(invoice);
			if(purchaseItemDeleted > 0 && purchaseDeleted > 0) return 1;
			else return - 1;
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}
}
