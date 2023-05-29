package DAO;

import Entity.Product;
import Entity.Purchase;
import Entity.PurchaseItem;
import SQLSession.DBHelper;
import SQLSession.MyBatisSession;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PurchaseDAOImplementation implements PurchaseDAO {
	private final Connection purchaseConnection = DBHelper.getConnection();
	private final SqlSessionFactory sqlSessionFactory = MyBatisSession.getSqlSessionFactory();
	private final SqlSession sqlSession = sqlSessionFactory.openSession();
	private final PurchaseDAO purchaseMapper = sqlSession.getMapper(PurchaseDAO.class);
	private final PurchaseItemMapper purchaseItemMapper = sqlSession.getMapper(PurchaseItemMapper.class);
	private final ProductDAOImplementation productDAO = new ProductDAOImplementation();
	private List<Purchase> purchaseList = new ArrayList<>();
	private List<PurchaseItem> purchaseItemList = new ArrayList<>();

	@Override
	public Purchase create(Purchase purchase) throws ApplicationErrorException, SQLException, UniqueConstraintException {
		try {
			Purchase createdPurchase = purchaseMapper.create(purchase);
			for(PurchaseItem purchaseItem: purchase.getPurchaseItemList()) {
				PurchaseItem createPurchaseItem = purchaseItemMapper.create(purchaseItem, purchase.getInvoice());
				createPurchaseItem.setProduct(purchaseItem.getProduct());
				purchaseItemList.add(createPurchaseItem);
				productDAO.updateStock(purchaseItem.getProduct().getCode(), purchaseItem.getQuantity());
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
	public Integer count(String attribute, String searchText) throws ApplicationErrorException {
		try {
			return purchaseMapper.count(attribute, searchText);
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}

	@Override
	public List<Purchase> list(String attribute, String searchText, int pageLength, int offset) throws ApplicationErrorException {
		try {
			Integer count = purchaseMapper.count(attribute, searchText);
			checkPagination(count, offset, pageLength);
			List<Purchase> listedPurchase = purchaseMapper.list(attribute, searchText, pageLength, offset);
			List<PurchaseItem> listedPurchaseItems;
			for(Purchase purchase: listedPurchase)
			{
				listedPurchaseItems = purchaseItemMapper.list(purchase.getInvoice());
				purchase.setPurchaseItemList(listedPurchaseItems);
			}
			return listedPurchase;
		} catch(Exception e) {
			e.printStackTrace();
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
	public List<Purchase> searchList(String searchText) throws ApplicationErrorException {
		try {
			List<Purchase> listedPurchase = purchaseMapper.searchList( searchText);
			List<PurchaseItem> listedPurchaseItems;
			for(Purchase purchase: listedPurchase)
			{
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
			if(purchaseItemMapper.delete(invoice) > 0 && purchaseMapper.delete(invoice) > 0 )
				return 1;
			else
				return -1;
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}
}
