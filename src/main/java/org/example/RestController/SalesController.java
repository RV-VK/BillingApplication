package org.example.RestController;

import org.example.DAO.ApplicationErrorException;
import org.example.DAO.PageCountOutOfBoundsException;
import org.example.Entity.Sales;
import org.example.Service.InvalidTemplateException;
import org.example.Service.SalesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class SalesController {
	private final Logger logger = LoggerFactory.getLogger(SalesController.class);
	@Autowired
	private SalesService salesService;
	@Autowired
	private ListAttributeMapValidator validator;
	private HashMap<String, String> listAttributes = new HashMap<>();
	private List<Sales> salesList;

	@GetMapping(path = "/sales", produces = "application/json")
	@ResponseBody
	public List<Sales> getAll() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		listAttributes.put("Pagelength", String.valueOf(Integer.MAX_VALUE));
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", "id");
		listAttributes.put("Searchtext", null);
		return listHelperFunction(listAttributes);
	}

	@GetMapping(path = "/sales/{pageLength}", produces = "application/json")
	public List<Sales> getByPageLength(@PathVariable String pageLength) throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		listAttributes.put("Pagelength", pageLength);
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", "id");
		listAttributes.put("Searchtext", null);
		validator.validate(listAttributes, PurchaseController.class);
		return listHelperFunction(listAttributes);
	}

	@GetMapping(path = "/sales/{pageLength}/{pageNumber}", produces = "application/json")
	public List<Sales> getByPageLengthAndPageNumber(@PathVariable String pageLength, @PathVariable String pageNumber) throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		listAttributes.put("Pagelength", pageLength);
		listAttributes.put("Pagenumber", pageNumber);
		listAttributes.put("Attribute", "id");
		listAttributes.put("Searchtext", null);
		validator.validate(listAttributes, PurchaseController.class);
		return listHelperFunction(listAttributes);
	}

	@GetMapping(path = "/sales/find/{searchText}", produces = "application/json")
	public List<Sales> getBySearchText(@PathVariable String searchText) throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		listAttributes.put("Pagelength", null);
		listAttributes.put("Pagenumber", null);
		listAttributes.put("Attribute", null);
		listAttributes.put("Searchtext", searchText);
		return listHelperFunction(listAttributes);
	}

	@GetMapping(path = "/sales/find/{attribute}/{searchText}", produces = "application/json")
	public List<Sales> getByAttributeAndSearchText(@PathVariable String attribute, @PathVariable String searchText) throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		listAttributes.put("Pagelength", String.valueOf(Integer.MAX_VALUE));
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", attribute);
		listAttributes.put("Searchtext", searchText);
		validator.validate(listAttributes, SalesController.class);
		return listHelperFunction(listAttributes);
	}

	@GetMapping(path = "/sales/find/{attribute}/{searchText}/{pageLength}", produces = "application/json")
	public List<Sales> getByAttributeAndSearchTextWithPageLength(@PathVariable String attribute, @PathVariable String searchText, @PathVariable String pageLength) throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		listAttributes.put("Pagelength", pageLength);
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", attribute);
		listAttributes.put("Searchtext", searchText);
		validator.validate(listAttributes, SalesController.class);
		return listHelperFunction(listAttributes);
	}

	@GetMapping(path = "/sales/find/{attribute}/{searchText}/{pageLength}/{pageNumber}", produces = "application/json")
	public List<Sales> getByAttributeAndSearchTextWithPageLengthAndPageNumber(@PathVariable String attribute, @PathVariable String searchText, @PathVariable String pageLength, @PathVariable String pageNumber) throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		listAttributes.put("Pagelength", pageLength);
		listAttributes.put("Pagenumber", pageNumber);
		listAttributes.put("Attribute", attribute);
		listAttributes.put("Searchtext", searchText);
		validator.validate(listAttributes, SalesController.class);
		return listHelperFunction(listAttributes);
	}

	@GetMapping(path = "/countSales", produces = "application/json")
	public Integer count() throws ApplicationErrorException, InvalidTemplateException {
		Integer count;
		String attribute = "id";
		String searchText = null;
		try {
			count = salesService.count(attribute, searchText);
		} catch(Exception exception) {
			logger.error("Error retrieving data from the database, {} ", exception.getMessage());
			throw exception;
		}
		logger.info("Returned Successfully! Sales Count : {} ", count);
		return count;
	}

	@GetMapping(path = "/countSales/{date}", produces = "application/json")
	public Integer countByDate(@PathVariable String date) throws ApplicationErrorException, InvalidTemplateException {
		Integer count;
		try {
			count = salesService.count("date", date);
		} catch(Exception exception) {
			logger.error("Error retrieving data from the database, {} ", exception.getMessage());
			throw exception;
		}
		if(count <= 0) {
			logger.warn("No Purchases for the given date : {} ", date);
			throw new InvalidTemplateException("No Purchases for the Given Date");
		} else {
			logger.info("Returned Successfully! Sales Count : {} ", count);
			return count;
		}
	}

	@PostMapping(path = "/sales", produces = "application/json")
	public Sales add(@RequestBody Sales sales) throws Exception {
		Sales createdSale;
		try {
			createdSale = salesService.create(sales);
		} catch(Exception exception) {
			logger.error("Sales creation failed!, {} ", exception.getMessage());
			throw exception;
		}
		logger.info("Sales created Successfully! Created SalesEntry : {} ", sales);
		return createdSale;
	}

	@DeleteMapping(path = "/deleteSales/{id}", produces = "application/json")
	public Integer delete(@PathVariable String id) throws ApplicationErrorException, InvalidTemplateException {
		Integer statusCode;
		try {
			statusCode = salesService.delete(id);
		} catch(Exception exception) {
			logger.error("Sales deletion failed, {} ", exception.getMessage());
			throw exception;
		}
		if(statusCode == 1) {
			logger.info("Sales deleted Successfully!");
			return statusCode;
		} else {
			logger.error("Sales deletion failed!, Given Id doesnt exists: {} ", id);
			throw new InvalidTemplateException("Given Id doesnt exists to delete!");
		}
	}

	private List<Sales> listHelperFunction(HashMap<String, String> listAttributes) throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		try {
			salesList = salesService.list(listAttributes);
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
		return salesList;
	}
}
