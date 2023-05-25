package DAO;

import DBConnection.DBHelper;
import Entity.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalesDAOImplementation implements SalesDAO {
	private final Connection salesConnection = DBHelper.getConnection();
	private List<Sales> salesList = new ArrayList<>();


	@Override
	public Sales create(Sales sales) throws ApplicationErrorException, SQLException {
		try {
			salesConnection.setAutoCommit(false);
			PreparedStatement salesEntryStatement = salesConnection.prepareStatement("INSERT INTO SALES(DATE,GRANDTOTAL) VALUES(?,?) RETURNING *");
			PreparedStatement salesItemInsertStatement = salesConnection.prepareStatement("INSERT INTO SALESITEMS (ID, PRODUCTCODE, QUANTITY, SALESPRICE) VALUES (?,?,?,?) RETURNING *");
			setSales(salesEntryStatement, sales);
			ResultSet salesEntryResultSet = salesEntryStatement.executeQuery();
			Sales salesEntry = new Sales();
			while(salesEntryResultSet.next()) {
				getSalesFromResultSet(salesEntryResultSet, salesEntry);
			}
			List<SalesItem> salesItemList = new ArrayList<>();
			ResultSet salesItemInsertResultSet;
			for(SalesItem salesItem: sales.getSalesItemList()) {
				setSalesItems(salesItemInsertStatement, salesItem, salesEntry);
				salesItemInsertResultSet = salesItemInsertStatement.executeQuery();
				new ProductDAOImplementation().updateStock(salesItem.getProduct().getCode(), - salesItem.getQuantity());
				while(salesItemInsertResultSet.next()) {
					salesItemList.add(getSalesItemFromResultSet(salesItemInsertResultSet, salesItem.getProduct()));
				}
			}
			salesEntry.setSalesItemList(salesItemList);
			salesConnection.commit();
			salesConnection.setAutoCommit(true);
			return salesEntry;
		} catch(SQLException e) {
			salesConnection.rollback();
			e.printStackTrace();
			throw new ApplicationErrorException(e.getMessage());
		}
	}

	private void setSales(PreparedStatement statement, Sales sales) throws SQLException {
		statement.setDate(1, Date.valueOf(sales.getDate()));
		statement.setDouble(2, sales.getGrandTotal());
	}

	private void setSalesItems(PreparedStatement statement, SalesItem salesItem, Sales sales) throws SQLException {
		statement.setInt(1, sales.getId());
		statement.setString(2, salesItem.getProduct().getCode());
		statement.setFloat(3, salesItem.getQuantity());
		statement.setDouble(4, salesItem.getProduct().getPrice());
	}

	private void getSalesFromResultSet(ResultSet resultSet, Sales sales) throws SQLException {
		sales.setId(resultSet.getInt(1));
		sales.setDate(String.valueOf(resultSet.getDate(2)));
		sales.setGrandTotal(resultSet.getDouble(3));
	}

	private SalesItem getSalesItemFromResultSet(ResultSet resultSet, Product product) throws SQLException {
		return new SalesItem(product, resultSet.getFloat(3), resultSet.getDouble(4));
	}

	@Override
	public Integer count(String parameter) throws ApplicationErrorException {
		try {
			String countQuery = "SELECT COUNT(ID) FROM SALES";
			String countQueryByDate = "SELECT COUNT(*) FROM PURCHASE WHERE CAST(DATE AS TEXT) ILIKE'" + parameter + "'";
			ResultSet countResultSet;
			if(parameter == null) countResultSet = salesConnection.createStatement().executeQuery(countQuery);
			else countResultSet = salesConnection.createStatement().executeQuery(countQueryByDate);
			countResultSet.next();
			return countResultSet.getInt(1);
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}


	@Override
	public List<Sales> list(String attribute, String searchText, int pageLength, int offset) throws ApplicationErrorException {
		int count;
		try {
			String EntryCount = "SELECT COUNT(*) OVER() FROM SALES WHERE " + attribute + "= COALESCE(" + searchText + "," + attribute + ")" + " ORDER BY ID";
			String listQuery = "SELECT * FROM SALES WHERE " + attribute + "= COALESCE(" + searchText + "," + attribute + ")" + " ORDER BY ID LIMIT " + pageLength + "  OFFSET " + offset;
			PreparedStatement listStatement = salesConnection.prepareStatement(listQuery);
			PreparedStatement countStatement = salesConnection.prepareStatement(EntryCount);
			ResultSet countResultSet = countStatement.executeQuery();
			if(countResultSet.next()) count = countResultSet.getInt(1);
			else return null;
			checkPagination(count, offset, pageLength);
			ResultSet listResultSet = listStatement.executeQuery();
			return listHelper(listResultSet);
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}

	private void checkPagination(int count, int offset, int pageLength) throws PageCountOutOfBoundsException {
		if(count <= offset) {
			int pageCount;
			if(count % pageLength == 0) pageCount = count / pageLength;
			else pageCount = (count / pageLength) + 1;
			throw new PageCountOutOfBoundsException(">> Requested Page doesnt Exist!!\n>> Existing Pagecount with given pagination " + pageCount);
		}
	}


	@Override
	public List<Sales> list(String searchText) throws ApplicationErrorException {
		try {
			String listQuery = "SELECT * FROM SALES WHERE CAST(ID AS TEXT) '" + searchText + "' OR CAST(DATE AS TEXT) ILIKE '" + searchText + "' OR CAST (INVOICE AS TEXT) ILIKE '" + searchText + "'";
			ResultSet listResultSet = salesConnection.createStatement().executeQuery(listQuery);
			return listHelper(listResultSet);
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}

	/**
	 * This method serves the listDAO function.
	 *
	 * @param resultSet ListQuery results.
	 * @return List - Sales.
	 * @throws SQLException Exception thrown based on SQL syntax.
	 */
	private List<Sales> listHelper(ResultSet resultSet) throws SQLException, ApplicationErrorException {
		while(resultSet.next()) {
			Sales listedSale = new Sales();
			getSalesFromResultSet(resultSet, listedSale);
			salesList.add(listedSale);
		}
		PreparedStatement listSalesItemStatement = salesConnection.prepareStatement("SELECT P.NAME, S.PRODUCTCODE,S.QUANTITY,S.SALESPRICE FROM SALESITEMS S INNER JOIN PRODUCT P ON P.CODE=S.PRODUCTCODE WHERE S.ID=?");
		for(Sales sales: salesList) {
			List<SalesItem> salesItemList = new ArrayList<>();
			listSalesItemStatement.setInt(1, sales.getId());
			ResultSet listSalesResultSet = listSalesItemStatement.executeQuery();
			while(listSalesResultSet.next())
				salesItemList.add(getSalesItemFromResultSet(listSalesResultSet, new ProductDAOImplementation().findByCode(listSalesResultSet.getString(2))));
			sales.setSalesItemList(salesItemList);
		}
		return salesList;
	}

	@Override
	public Integer delete(int id) throws ApplicationErrorException {
		try {
			int salesItemUpdatedCount = salesConnection.createStatement().executeUpdate("DELETE FROM SALESITEMS WHERE ID='" + id + "'");
			int salesUpdatedCount = salesConnection.createStatement().executeUpdate("DELETE FROM SALES WHERE ID='" + id + "'");
			if(salesItemUpdatedCount > 0 && salesUpdatedCount > 0) {
				return 1;
			} else return - 1;
		} catch(Exception e) {
			throw new ApplicationErrorException(e.getMessage());
		}
	}
}
