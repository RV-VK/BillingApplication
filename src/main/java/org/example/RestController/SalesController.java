package org.example.RestController;

import org.example.DAO.ApplicationErrorException;
import org.example.DAO.PageCountOutOfBoundsException;
import org.example.Entity.Sales;
import org.example.Service.InvalidTemplateException;
import org.example.Service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class SalesController {
	@Autowired
	private SalesService salesService;
	@Autowired
	private ListAttributeMapValidator validator;
	private HashMap<String, String> listAttributes = new HashMap<>();

	@GetMapping(path = "/sales", produces = "application/json")
	@ResponseBody
	public List<Sales> getAll() throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		listAttributes.put("Pagelength", String.valueOf(Integer.MAX_VALUE));
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", "id");
		listAttributes.put("Searchtext", null);
		return salesService.list(listAttributes);
	}

	@GetMapping(path = "/sales/{pageLength}", produces = "application/json")
	public List<Sales> getByPageLength(@PathVariable String pageLength) throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		listAttributes.put("Pagelength", pageLength);
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", "id");
		listAttributes.put("Searchtext", null);
		validator.validate(listAttributes, PurchaseController.class);
		return salesService.list(listAttributes);
	}

	@GetMapping(path = "/sales/{pageLength}/{pageNumber}", produces = "application/json")
	public List<Sales> getByPageLengthAndPageNumber(@PathVariable String pageLength, @PathVariable String pageNumber) throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		listAttributes.put("Pagelength", pageLength);
		listAttributes.put("Pagenumber", pageNumber);
		listAttributes.put("Attribute", "id");
		listAttributes.put("Searchtext", null);
		validator.validate(listAttributes, PurchaseController.class);
		return salesService.list(listAttributes);
	}

	@GetMapping(path = "/sales/find/{searchText}", produces = "application/json")
	public List<Sales> getBySearchText(@PathVariable String searchText) throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		listAttributes.put("Pagelength", null);
		listAttributes.put("Pagenumber", null);
		listAttributes.put("Attribute", null);
		listAttributes.put("Searchtext", searchText);
		return salesService.list(listAttributes);
	}

	@GetMapping(path = "/sales/find/{attribute}/{searchText}", produces = "application/json")
	public List<Sales> getByAttributeAndSearchText(@PathVariable String attribute, @PathVariable String searchText) throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		listAttributes.put("Pagelength", String.valueOf(Integer.MAX_VALUE));
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", attribute);
		listAttributes.put("Searchtext", searchText);
		validator.validate(listAttributes, SalesController.class);
		return salesService.list(listAttributes);
	}

	@GetMapping(path = "/sales/find/{attribute}/{searchText}/{pageLength}", produces = "application/json")
	public List<Sales> getByAttributeAndSearchTextWithPageLength(@PathVariable String attribute, @PathVariable String searchText, @PathVariable String pageLength) throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		listAttributes.put("Pagelength", pageLength);
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", attribute);
		listAttributes.put("Searchtext", searchText);
		validator.validate(listAttributes, SalesController.class);
		return salesService.list(listAttributes);
	}

	@GetMapping(path = "/sales/find/{attribute}/{searchText}/{pageLength}/{pageNumber}", produces = "application/json")
	public List<Sales> getByAttributeAndSearchTextWithPageLengthAndPageNumber(@PathVariable String attribute, @PathVariable String searchText, @PathVariable String pageLength, @PathVariable String pageNumber) throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		listAttributes.put("Pagelength", pageLength);
		listAttributes.put("Pagenumber", pageNumber);
		listAttributes.put("Attribute", attribute);
		listAttributes.put("Searchtext", searchText);
		validator.validate(listAttributes, SalesController.class);
		return salesService.list(listAttributes);
	}

	@GetMapping(path = "/countSales", produces = "application/json")
	public Integer count() throws ApplicationErrorException, InvalidTemplateException {
		String attribute = "id";
		String searchText = null;
		return salesService.count(attribute, searchText);
	}

	@GetMapping(path = "/countSales/{date}", produces = "application/json")
	public Integer countByDate(@PathVariable String date) throws ApplicationErrorException, InvalidTemplateException {
		Integer count = salesService.count("date", date);
		if(count <= 0)
			throw new InvalidTemplateException("No Purchases for the Given Date");
		else
			return count;
	}

	@PostMapping(path = "/sales", produces = "application/json")
	public Sales add(@RequestBody Sales sales) throws Exception {
		return salesService.create(sales);
	}

	@DeleteMapping(path = "/deleteSales/{id}", produces = "application/json")
	public Integer delete(@PathVariable String id) throws ApplicationErrorException, InvalidTemplateException {
		Integer statusCode = salesService.delete(id);
		if(statusCode == 1)
			return statusCode;
		else
			throw new InvalidTemplateException("Given Id doesnt exists to delete!");
	}

}
