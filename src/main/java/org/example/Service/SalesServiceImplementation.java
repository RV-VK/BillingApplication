package org.example.Service;

import org.example.DAO.*;
import org.example.Entity.Product;
import org.example.Entity.Sales;
import org.example.Entity.SalesItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


@Service
public class SalesServiceImplementation implements SalesService {
	private final String dateRegex = "([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))";
	@Autowired
	private SalesDAO salesDAO;
	@Autowired
	private ProductDAO getProductByCode;
	@Autowired
	private UnitDAO getUnitByCode;

	@Override
	public Sales create(Sales sales) throws ApplicationErrorException, SQLException, UnDividableEntityException, InvalidTemplateException {
		boolean isDividable;
		double grandtotal = 0.0;
		validate(sales);
		for(SalesItem salesItem: sales.getSalesItemList()) {
			try {
				Product product = getProductByCode.findByCode(salesItem.getProduct().getCode());
				if(product != null) salesItem.setProduct(product);
				if(salesItem.getProduct().getAvailableQuantity() < salesItem.getQuantity())
					throw new ApplicationErrorException("Product '" + salesItem.getProduct().getCode() + "' is out of Stock");
				isDividable = getUnitByCode.findByCode(product.getunitcode()).getIsDividable();
				grandtotal += salesItem.getProduct().getPrice() * salesItem.getQuantity();
			} catch(NullPointerException e) {
				throw new ApplicationErrorException("Product code '" + salesItem.getProduct().getCode() + "' does not exist!");
			}
			if((! isDividable && salesItem.getQuantity() % 1 != 0)) {
				throw new UnDividableEntityException("Product code '" + salesItem.getProduct().getCode() + "' is not a dividable product");
			}
		}
		sales.setGrandTotal(grandtotal);
		return salesDAO.create(sales);
	}


	@Override
	public Integer count(String attribute, String searchText) throws ApplicationErrorException, InvalidTemplateException {
		if(searchText != null) {
			if(! searchText.matches(dateRegex)) {
				throw new InvalidTemplateException("Invalid Date format!! Must be in YYYY-MM-DD format!");
			}
		}
		return salesDAO.count(attribute, searchText);
	}


	@Override
	public List<Sales> list(HashMap<String, String> listAttributes) throws ApplicationErrorException, PageCountOutOfBoundsException, InvalidTemplateException {
		List<Sales> salesList;
		if(Collections.frequency(listAttributes.values(), null) == listAttributes.size() - 1 && listAttributes.get("Searchtext") != null) {
			salesList = salesDAO.searchList(listAttributes.get("Searchtext"));
		} else {
			int pageLength = Integer.parseInt(listAttributes.get("Pagelength"));
			int pageNumber = Integer.parseInt(listAttributes.get("Pagenumber"));
			int offset = (pageLength * pageNumber) - pageLength;
			if(listAttributes.get("Attribute").equals("date")) {
				if(listAttributes.get("Searchtext") != null && ! (listAttributes.get("Searchtext").replace("'", "").matches(dateRegex)))
					throw new InvalidTemplateException("Invalid Format for Attribute date!! Must be in format YYYY-MM-DD");
			}
			salesList = salesDAO.list(listAttributes.get("Attribute"), listAttributes.get("Searchtext"), pageLength, offset);
		}
		return salesList;
	}

	@Override
	public Integer delete(String id) throws ApplicationErrorException {
		if(id != null)
			return salesDAO.delete(Integer.parseInt(id));
		else
			return - 1;
	}

	private void validate(Sales sales) throws InvalidTemplateException {
		if(sales == null)
			throw new NullPointerException("Sales cannot be Null");
		if(sales.getDate() != null && ! sales.getDate().matches(dateRegex))
			throw new InvalidTemplateException("Date Format is Invalid!! Must be YYYY-MM-DD!!");
	}
}
