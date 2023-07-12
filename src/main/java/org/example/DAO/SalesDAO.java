package org.example.DAO;

import org.example.Entity.Sales;
import org.example.Entity.SalesItem;
import org.example.Mapper.SalesItemMapper;
import org.example.Mapper.SalesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SalesDAO {
	@Autowired
	private ProductDAO productDAO;
	private final List<SalesItem> salesItemList = new ArrayList<>();
	@Autowired
	private SalesMapper salesMapper;

	@Autowired
	private SalesItemMapper salesItemMapper;

	/**
	 * This method is a composite function that creates an entry in both Sales and Sales-items table.
	 *
	 * @param sales sales Input Sales.
	 * @return sales - Created Sales.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 * @throws SQLException              Exception thrown based on SQL syntax.
	 */
	public Sales create(Sales sales) throws ApplicationErrorException, SQLException {
		try {
			salesItemList.clear();
			Sales createdSales = salesMapper.create(sales);
			for(SalesItem salesItem: sales.getSalesItemList()) {
				SalesItem createdSalesItem = salesItemMapper.create(salesItem, createdSales.getId());
				createdSalesItem.setProduct(salesItem.getProduct());
				salesItemList.add(createdSalesItem);
				System.out.println(salesItem.getProduct().getAvailableQuantity() + " " + salesItem.getQuantity());
				salesItem.getProduct().setAvailableQuantity(salesItem.getProduct().getAvailableQuantity() - salesItem.getQuantity());
				System.out.println(salesItem.getProduct().getAvailableQuantity());
				productDAO.edit(salesItem.getProduct());
			}
			createdSales.setSalesItemList(salesItemList);
			return createdSales;
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}

	/**
	 * This method counts the number of entries from the sales table based on Given attribute and searchText.
	 *
	 * @param attribute  Column to be counted.
	 * @param searchText Field to be counted.
	 * @return Integer - Count.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	public Integer count(String attribute, Object searchText) throws ApplicationErrorException {
		try {
			Integer count;
			if(attribute.equals("date")) count = salesMapper.count(attribute, Date.valueOf(String.valueOf(searchText)));
			else count = salesMapper.count(attribute, searchText);
			return count;
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}

	/**
	 * This method lists the Sales and SalesItem entries based on the given searchable attribute and
	 * its corresponding search-text formatted in pageable manner.
	 *
	 * @param attribute  The attribute to be looked upon.
	 * @param searchText The search-text to be found.
	 * @param pageLength The number of entries that must be listed.
	 * @param offset     The Page number to be listed.
	 * @return List - Sales.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	public List<Sales> list(String attribute, Object searchText, int pageLength, int offset) throws ApplicationErrorException {
		List<Sales> listedSales;
		Date dateParameter = null;
		Integer count, numericParameter;
		try {
			if(searchText != null && String.valueOf(searchText).matches("^\\d+(\\.\\d+)?$")) {
				numericParameter = Integer.parseInt(String.valueOf(searchText));
				count = salesMapper.count(attribute, numericParameter);
				checkPagination(count, offset, pageLength);
				listedSales = salesMapper.list(attribute, numericParameter, pageLength, offset);
			} else {
				if(searchText != null) dateParameter = Date.valueOf(String.valueOf(searchText));
				count = salesMapper.count(attribute, dateParameter);
				checkPagination(count, offset, pageLength);
				listedSales = salesMapper.list(attribute, dateParameter, pageLength, offset);
			}
			List<SalesItem> listedSalesItems;
			for(Sales sales: listedSales) {
				listedSalesItems = salesItemMapper.list(sales.getId());
				sales.setSalesItemList(listedSalesItems);
			}
			return listedSales;
		} catch(Exception e) {
			e.printStackTrace();
			throw new ApplicationErrorException(e.getMessage());
		}
	}

	/**
	 * Private method to check whether the given Pagenumber is Valid or Not exists.
	 *
	 * @param count      Total Count of entries.
	 * @param offset     Index from which the Entries are requested.
	 * @param pageLength Length for Each page.
	 * @throws PageCountOutOfBoundsException Exception thrown in a pageable list function if a
	 *                                       non-existing page is prompted.
	 */
	private void checkPagination(int count, int offset, int pageLength) throws PageCountOutOfBoundsException {
		if(count <= offset && count != 0) {
			int pageCount;
			if(count % pageLength == 0) pageCount = count / pageLength;
			else pageCount = (count / pageLength) + 1;
			throw new PageCountOutOfBoundsException(">> Requested Page doesnt Exist!!\n>> Existing Pagecount with given pagination " + pageCount);
		}
	}

	/**
	 * This method lists the Entries from the Sales and SalesItem table based on the given search-text.
	 *
	 * @param searchText The search-text to be found.
	 * @return List - Sales.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	public List<Sales> searchList(String searchText) throws ApplicationErrorException {
		try {
			List<Sales> listedSales = salesMapper.searchList(searchText);
			List<SalesItem> listedSalesItems;
			for(Sales sales: listedSales) {
				listedSalesItems = salesItemMapper.list(sales.getId());
				sales.setSalesItemList(listedSalesItems);
			}
			return listedSales;
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}

	/**
	 * This method deletes an entry in the Sales table and the corresponding entries in the Sales items table.
	 *
	 * @param id Input id to perform delete.
	 * @return resultCode - Integer.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	public Integer delete(int id) throws ApplicationErrorException {
		try {
			Integer salesItemDeleted = salesItemMapper.delete(id);
			Integer salesDeleted = salesMapper.delete(id);
			if(salesItemDeleted > 0 && salesDeleted > 0) return 1;
			else return - 1;
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}
}
