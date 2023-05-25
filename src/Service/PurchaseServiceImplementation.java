package Service;

import DAO.*;
import Entity.Product;
import Entity.Purchase;
import Entity.PurchaseItem;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class PurchaseServiceImplementation implements PurchaseService {

	private final PurchaseDAO purchaseDAO = new PurchaseDAOImplementation();


	@Override
	public Purchase create(Purchase purchase) throws ApplicationErrorException, SQLException, UnDividableEntityException {
		ProductDAO productDAO = new ProductDAOImplementation();
		UnitDAO unitDAO = new UnitDAOImplementation();
		boolean isDividable;
		for(PurchaseItem purchaseItem: purchase.getPurchaseItemList()) {
			try {
				Product product = productDAO.findByCode(purchaseItem.getProduct().getCode());
				purchaseItem.setProduct(product);
				isDividable = unitDAO.findByCode(product.getunitcode()).getIsDividable();
			} catch(NullPointerException e) {
				throw new ApplicationErrorException(">> Product '"+purchaseItem.getProduct().getCode() + " does not exists!");
			}
			if(! isDividable && purchaseItem.getQuantity() % 1 != 0) {
				throw new UnDividableEntityException(">> Product '" + purchaseItem.getProduct().getCode() + " is not of dividable unit!");
			}
		}
		return purchaseDAO.create(purchase);
	}


	@Override
	public Integer count(String parameter) throws ApplicationErrorException {
		String dateRegex = "([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))";
		if(parameter != null) {
			if(! parameter.matches(dateRegex)) return - 1;
		}
		return purchaseDAO.count(parameter);
	}


	@Override
	public List<Purchase> list(HashMap<String, String> listattributes) throws ApplicationErrorException {
		List<Purchase> purchaseList;
		if(Collections.frequency(listattributes.values(), null) == listattributes.size() - 1 && listattributes.get("Searchtext") != null) {
			purchaseList = purchaseDAO.list(listattributes.get("Searchtext"));
		} else {
			int pageLength = Integer.parseInt(listattributes.get("Pagelength"));
			int pageNumber = Integer.parseInt(listattributes.get("Pagenumber"));
			int offset = (pageLength * pageNumber) - pageLength;
			purchaseList = purchaseDAO.list(listattributes.get("Attribute"), listattributes.get("Searchtext"), pageLength, offset);
		}
		return purchaseList;
	}


	@Override
	public Integer delete(String invoice) throws ApplicationErrorException {
		return purchaseDAO.delete(Integer.parseInt(invoice));
	}
}
