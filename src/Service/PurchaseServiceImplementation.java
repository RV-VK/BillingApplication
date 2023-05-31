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
	private final String dateRegex = "([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))";


	@Override
	public Purchase create(Purchase purchase) throws ApplicationErrorException, SQLException, UnDividableEntityException, UniqueConstraintException, UnitCodeViolationException {
		ProductDAO productDAO = new ProductDAOImplementation();
		UnitDAO unitDAO = new UnitDAOImplementation();
		boolean isDividable;
		for(PurchaseItem purchaseItem: purchase.getPurchaseItemList()) {
			try {
				Product product = productDAO.findByCode(purchaseItem.getProduct().getCode());
				if(product != null) purchaseItem.setProduct(product);
				isDividable = unitDAO.findByCode(product.getunitcode()).getIsDividable();
			} catch(NullPointerException e) {
				throw new ApplicationErrorException(">> Product code '" + purchaseItem.getProduct().getCode() + "' does not exists!");
			}
			if(! isDividable && purchaseItem.getQuantity() % 1 != 0) {
				throw new UnDividableEntityException(">> Product code '" + purchaseItem.getProduct().getCode() + "' is not of dividable unit!");
			}
		}
		return purchaseDAO.create(purchase);
	}


	@Override
	public Integer count(String attribute, String searchText) throws ApplicationErrorException, InvalidTemplateException {
		if(searchText != null) {
			if(!searchText.matches(dateRegex))
				throw new InvalidTemplateException(">> Invalid Date format!! Must be in YYYY-MM-DD format!");
		}
		return purchaseDAO.count(attribute, searchText);
	}


	@Override
	public List<Purchase> list(HashMap<String, String> listattributes) throws ApplicationErrorException, InvalidTemplateException {
		List<Purchase> purchaseList;
		if(Collections.frequency(listattributes.values(), null) == listattributes.size() - 1 && listattributes.get("Searchtext") != null) {
			purchaseList = purchaseDAO.searchList(listattributes.get("Searchtext"));
		} else {
			int pageLength = Integer.parseInt(listattributes.get("Pagelength"));
			int pageNumber = Integer.parseInt(listattributes.get("Pagenumber"));
			int offset = (pageLength * pageNumber) - pageLength;
			if(listattributes.get("Attribute").equals("date"))
				if(! (listattributes.get("Searchtext").replace("'", "").matches(dateRegex)))
					throw new InvalidTemplateException(">> Invalid Format for Attribute date!! Must be in format YYYY-MM-DD");
			purchaseList = purchaseDAO.list(listattributes.get("Attribute"), listattributes.get("Searchtext"), pageLength, offset);
		}
		return purchaseList;
	}


	@Override
	public Integer delete(String invoice) throws ApplicationErrorException {
		return purchaseDAO.delete(Integer.parseInt(invoice));
	}
}
