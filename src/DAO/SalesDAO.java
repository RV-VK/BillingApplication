package DAO;

import Entity.Sales;
import Entity.SalesItem;
import Mapper.SalesMapper;
import Mapper.SalesItemMapper;
import SQLSession.MyBatisSession;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalesDAO {
	private final SqlSessionFactory sqlSessionFactory = MyBatisSession.getSqlSessionFactory();
	private final ProductDAO productDAO = new ProductDAO();
	private final SqlSession sqlSession = sqlSessionFactory.openSession();
	private final SalesMapper salesMapper = sqlSession.getMapper(SalesMapper.class);
	private final SalesItemMapper salesItemMapper = sqlSession.getMapper(SalesItemMapper.class);
	private final List<SalesItem> salesItemList = new ArrayList<>();


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

	public Integer count(String attribute, Object searchText) throws ApplicationErrorException {
		try {
			if(attribute.equals("date")) return salesMapper.count(attribute, Date.valueOf(String.valueOf(searchText)));
			else return salesMapper.count(attribute, searchText);
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}


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

	private void checkPagination(int count, int offset, int pageLength) throws PageCountOutOfBoundsException {
		if(count <= offset && count != 0) {
			int pageCount;
			if(count % pageLength == 0) pageCount = count / pageLength;
			else pageCount = (count / pageLength) + 1;
			throw new PageCountOutOfBoundsException(">> Requested Page doesnt Exist!!\n>> Existing Pagecount with given pagination " + pageCount);
		}
	}

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
