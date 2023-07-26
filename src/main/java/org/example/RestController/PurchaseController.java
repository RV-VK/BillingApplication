package org.example.RestController;

import org.example.DAO.ApplicationErrorException;
import org.example.DAO.PageCountOutOfBoundsException;
import org.example.Entity.Purchase;
import org.example.Service.InvalidTemplateException;
import org.example.Service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
public class PurchaseController {
	@Autowired
	private PurchaseService purchaseService;
	@Autowired
	private ListAttributeMapValidator validator;
	private HashMap<String, String> listAttributes = new HashMap<>();

	@GetMapping(path = "/purchases", produces = "application/json")
	public List<Purchase> getAll() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		listAttributes.put("Pagelength", String.valueOf(Integer.MAX_VALUE));
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", "id");
		listAttributes.put("Searchtext", null);
		return purchaseService.list(listAttributes);
	}

	@GetMapping(path = "/purchases/{pageLength}", produces = "application/json")
	public List<Purchase> getByPageLength(@PathVariable String pageLength) throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		listAttributes.put("Pagelength", pageLength);
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", "id");
		listAttributes.put("Searchtext", null);
		validator.validate(listAttributes, PurchaseController.class);
		return purchaseService.list(listAttributes);
	}

	@GetMapping(path = "/purchases/{pageLength}/{pageNumber}", produces = "application/json")
	public List<Purchase> getByPageLengthAndPageNumber(@PathVariable String pageLength, @PathVariable String pageNumber) throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		listAttributes.put("Pagelength", pageLength);
		listAttributes.put("Pagenumber", pageNumber);
		listAttributes.put("Attribute", "id");
		listAttributes.put("Searchtext", null);
		validator.validate(listAttributes, PurchaseController.class);
		return purchaseService.list(listAttributes);
	}

	@GetMapping(path = "/purchases/find/{searchText}", produces = "application/json")
	public List<Purchase> getBySearchText(@PathVariable String searchText) throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		listAttributes.put("Pagelength", null);
		listAttributes.put("Pagenumber", null);
		listAttributes.put("Attribute", null);
		listAttributes.put("Searchtext", searchText);
		return purchaseService.list(listAttributes);
	}

	@GetMapping(path = "/purchases/find/{attribute}/{searchText}", produces = "application/json")
	public List<Purchase> getByAttributeAndSearchText(@PathVariable String attribute, @PathVariable String searchText) throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		listAttributes.put("Pagelength", String.valueOf(Integer.MAX_VALUE));
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", attribute);
		listAttributes.put("Searchtext", searchText);
		return purchaseService.list(listAttributes);
	}

	@GetMapping(path = "/purchases/find/{attribute}/{searchText}/{pageLength}", produces = "application/json")
	public List<Purchase> getByAttributeAndSearchTextWithPageLength(@PathVariable String attribute, @PathVariable String searchText, @PathVariable String pageLength) throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		listAttributes.put("Pagelength", pageLength);
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", attribute);
		listAttributes.put("Searchtext", searchText);
		return purchaseService.list(listAttributes);
	}

	@GetMapping(path = "/purchases/find/{attribute}/{searchText}/{pageLength}/{pageNumber}", produces = "application/json")
	public List<Purchase> getByAttributeAndSearchTextWithPageLengthAndPageNumber(@PathVariable String attribute, @PathVariable String searchText, @PathVariable String pageLength, @PathVariable String pageNumber) throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		listAttributes.put("Pagelength", pageLength);
		listAttributes.put("Pagenumber", pageNumber);
		listAttributes.put("Attribute", attribute);
		listAttributes.put("Searchtext", searchText);
		return purchaseService.list(listAttributes);
	}


}
