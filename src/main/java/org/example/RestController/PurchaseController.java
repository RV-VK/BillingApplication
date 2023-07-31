package org.example.RestController;

import org.example.DAO.ApplicationErrorException;
import org.example.DAO.PageCountOutOfBoundsException;
import org.example.Entity.Purchase;
import org.example.Service.InvalidTemplateException;
import org.example.Service.PurchaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class PurchaseController {
	private final Logger logger = LoggerFactory.getLogger(PurchaseController.class);
	@Autowired
	private PurchaseService purchaseService;
	@Autowired
	private ListAttributeMapValidator validator;
	private HashMap<String, String> listAttributes = new HashMap<>();
	private List<Purchase> purchaseList;

	@GetMapping(path = "/purchases", produces = "application/json")
	@PreAuthorize("hasRole('Admin') or hasRole('Purchase')")
	public List<Purchase> getAll() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		listAttributes.put("Pagelength", String.valueOf(Integer.MAX_VALUE));
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", "id");
		listAttributes.put("Searchtext", null);
		return listHelperFunction(listAttributes);
	}

	@GetMapping(path = "/purchases/{pageLength}", produces = "application/json")
	@PreAuthorize("hasRole('Admin') or hasRole('Purchase')")
	public List<Purchase> getByPageLength(@PathVariable String pageLength) throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		listAttributes.put("Pagelength", pageLength);
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", "id");
		listAttributes.put("Searchtext", null);
		validator.validate(listAttributes, PurchaseController.class);
		return listHelperFunction(listAttributes);
	}

	@GetMapping(path = "/purchases/{pageLength}/{pageNumber}", produces = "application/json")
	@PreAuthorize("hasRole('Admin') or hasRole('Purchase')")
	public List<Purchase> getByPageLengthAndPageNumber(@PathVariable String pageLength, @PathVariable String pageNumber) throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		listAttributes.put("Pagelength", pageLength);
		listAttributes.put("Pagenumber", pageNumber);
		listAttributes.put("Attribute", "id");
		listAttributes.put("Searchtext", null);
		validator.validate(listAttributes, PurchaseController.class);
		return listHelperFunction(listAttributes);
	}

	@GetMapping(path = "/purchases/find/{searchText}", produces = "application/json")
	@PreAuthorize("hasRole('Admin') or hasRole('Purchase')")
	public List<Purchase> getBySearchText(@PathVariable String searchText) throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		listAttributes.put("Pagelength", null);
		listAttributes.put("Pagenumber", null);
		listAttributes.put("Attribute", null);
		listAttributes.put("Searchtext", searchText);
		return listHelperFunction(listAttributes);
	}

	@GetMapping(path = "/purchases/find/{attribute}/{searchText}", produces = "application/json")
	@PreAuthorize("hasRole('Admin') or hasRole('Purchase')")
	public List<Purchase> getByAttributeAndSearchText(@PathVariable String attribute, @PathVariable String searchText) throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		listAttributes.put("Pagelength", String.valueOf(Integer.MAX_VALUE));
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", attribute);
		listAttributes.put("Searchtext", searchText);
		validator.validate(listAttributes, PurchaseController.class);
		return listHelperFunction(listAttributes);
	}

	@GetMapping(path = "/purchases/find/{attribute}/{searchText}/{pageLength}", produces = "application/json")
	@PreAuthorize("hasRole('Admin') or hasRole('Purchase')")
	public List<Purchase> getByAttributeAndSearchTextWithPageLength(@PathVariable String attribute, @PathVariable String searchText, @PathVariable String pageLength) throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		listAttributes.put("Pagelength", pageLength);
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", attribute);
		listAttributes.put("Searchtext", searchText);
		validator.validate(listAttributes, PurchaseController.class);
		return listHelperFunction(listAttributes);
	}

	@GetMapping(path = "/purchases/find/{attribute}/{searchText}/{pageLength}/{pageNumber}", produces = "application/json")
	@PreAuthorize("hasRole('Admin') or hasRole('Purchase')")
	public List<Purchase> getByAttributeAndSearchTextWithPageLengthAndPageNumber(@PathVariable String attribute, @PathVariable String searchText, @PathVariable String pageLength, @PathVariable String pageNumber) throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		listAttributes.put("Pagelength", pageLength);
		listAttributes.put("Pagenumber", pageNumber);
		listAttributes.put("Attribute", attribute);
		listAttributes.put("Searchtext", searchText);
		validator.validate(listAttributes, PurchaseController.class);
		return listHelperFunction(listAttributes);
	}

	@GetMapping(path = "/countPurchase", produces = "application/json")
	@PreAuthorize("hasRole('Admin') or hasRole('Purchase')")
	public Integer count() throws ApplicationErrorException, InvalidTemplateException {
		Integer count;
		String attribute = "id";
		String searchText = null;
		try {
			count = purchaseService.count(attribute, searchText);
		} catch(Exception exception) {
			logger.error("Error retrieving data from the database, {} ", exception.getMessage());
			throw exception;
		}
		logger.info("Returned Successfully! Purchase Count : {} ", count);
		return count;
	}

	@GetMapping(path = "/countPurchase/{date}", produces = "application/json")
	@PreAuthorize("hasRole('Admin') or hasRole('Purchase')")
	public Integer countByDate(@PathVariable String date) throws ApplicationErrorException, InvalidTemplateException {
		Integer count;
		try {
			count = purchaseService.count("date", date);
		} catch(Exception exception) {
			logger.error("Error while retrieving data from the database, {} ", exception.getMessage());
			throw exception;
		}
		if(count <= 0) {
			logger.warn("No Purchases for the Given date : {} ", date);
			throw new InvalidTemplateException("No Purchases for the Given Date");
		} else {
			logger.info("Returned Successfully! Purchase Count : {} ", count);
			return count;
		}
	}

	@PostMapping(path = "/purchase", produces = "application/json")
	@PreAuthorize("hasRole('Admin') or hasRole('Purchase')")
	public Purchase add(@RequestBody Purchase purchase) throws Exception {
		Purchase createdPurchase;
		try {
			createdPurchase = purchaseService.create(purchase);
		} catch(Exception exception) {
			logger.error("Purchase Creation failed!, {} ", exception.getMessage());
			throw exception;
		}
		logger.info("Purchase Created Successfully! Created Purchase : {} ", createdPurchase);
		return createdPurchase;
	}

	@DeleteMapping(path = "/deletePurchase/{invoice}", produces = "application/json")
	@PreAuthorize("hasRole('Admin') or hasRole('Purchase')")
	public Integer delete(@PathVariable String invoice) throws ApplicationErrorException, InvalidTemplateException {
		Integer statusCode;
		try {
			statusCode = purchaseService.delete(invoice);
		} catch(Exception exception) {
			logger.error("Purchase deletion failed!, {} ", exception.getMessage());
			throw exception;
		}
		if(statusCode == 1) {
			logger.info("Purchase deleted Successfully!");
			return statusCode;
		} else {
			logger.error("Purchase deletion failed!, Given Invoice id doesnt exists : {} ", invoice);
			throw new InvalidTemplateException("Given Invoice doesnt exists to delete!");
		}
	}

	private List<Purchase> listHelperFunction(HashMap<String, String> listAttributes) throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		try {
			purchaseList = purchaseService.list(listAttributes);
		} catch(PageCountOutOfBoundsException exception) {
			logger.warn("Error while returning the list. {} ", exception.getMessage());
			throw exception;
		} catch(ApplicationErrorException exception) {
			logger.error("Error while retrieving data from the Database, {} ", exception.getMessage());
			throw exception;
		} catch(InvalidTemplateException exception) {
			logger.warn("Error while returning list, {} ", exception.getMessage());
			throw exception;
		}
		logger.info("List returned Successfully!");
		return purchaseList;
	}

}
