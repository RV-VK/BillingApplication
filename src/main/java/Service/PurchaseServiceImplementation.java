package Service;

import CLIController.AppDependencyConfig;
import DAO.ApplicationErrorException;
import DAO.ProductDAO;
import DAO.PurchaseDAO;
import DAO.UnitDAO;
import Entity.Product;
import Entity.Purchase;
import Entity.PurchaseItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Component
public class PurchaseServiceImplementation implements PurchaseService {

	private final String dateRegex = "([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))";
	@Autowired
	private PurchaseDAO purchaseDAO;

	@Override
	public Purchase create(Purchase purchase) throws Exception {
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppDependencyConfig.class);
		ProductDAO productDAO = applicationContext.getBean(ProductDAO.class);
		UnitDAO unitDAO = applicationContext.getBean(UnitDAO.class);
		boolean isDividable;
		validate(purchase);
		for(PurchaseItem purchaseItem: purchase.getPurchaseItemList()) {
			try {
				Product product = productDAO.findByCode(purchaseItem.getProduct().getCode());
				if(product != null) purchaseItem.setProduct(product);
				isDividable = unitDAO.findByCode(product.getunitcode()).getIsDividable();
			} catch(NullPointerException e) {
				throw new ApplicationErrorException("Product code '" + purchaseItem.getProduct().getCode() + "' does not exists!");
			}
			if(! isDividable && purchaseItem.getQuantity() % 1 != 0) {
				throw new UnDividableEntityException("Product code '" + purchaseItem.getProduct().getCode() + "' is not of dividable unit!");
			}
		}
		return purchaseDAO.create(purchase);
	}


	@Override
	public Integer count(String attribute, String searchText) throws ApplicationErrorException, InvalidTemplateException {
		if(searchText != null) {
			if(! searchText.matches(dateRegex))
				throw new InvalidTemplateException("Invalid Date format!! Must be in YYYY-MM-DD format!");
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
				if(listattributes.get("Searchtext") != null && ! (listattributes.get("Searchtext").replace("'", "").matches(dateRegex)))
					throw new InvalidTemplateException("Invalid Format for Attribute date!! Must be in format YYYY-MM-DD");
			purchaseList = purchaseDAO.list(listattributes.get("Attribute"), listattributes.get("Searchtext"), pageLength, offset);
		}
		return purchaseList;
	}


	@Override
	public Integer delete(String invoice) throws ApplicationErrorException {
		if(invoice != null)
			return purchaseDAO.delete(Integer.parseInt(invoice));
		else
			return - 1;
	}

	private void validate(Purchase purchase) throws InvalidTemplateException {
		if(purchase == null)
			throw new NullPointerException("Purchase cannot be Null");
		if(purchase.getDate() != null && ! purchase.getDate().matches(dateRegex))
			throw new InvalidTemplateException("Date Format is Invalid!! Must be YYYY-MM-DD!!");
	}
}
