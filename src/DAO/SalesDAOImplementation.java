package DAO;

import DBConnection.DBHelper;
import Entity.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalesDAOImplementation implements SalesDAO {
  private Connection salesConnection = DBHelper.getConnection();
  private List<Sales> salesList = new ArrayList<>();


  @Override
  public Sales create(Sales sales) throws ApplicationErrorException, SQLException {
    try {
      salesConnection.setAutoCommit(false);
      PreparedStatement salesEntryStatement = salesConnection.prepareStatement("INSERT INTO SALES(DATE,GRANDTOTAL) VALUES(?,?) RETURNING *");
      PreparedStatement salesItemInsertStatement = salesConnection.prepareStatement( "INSERT INTO SALESITEMS (ID, PRODUCTCODE, QUANTITY, SALESPRICE) VALUES (?,?,?,?) RETURNING *");
      PreparedStatement salesPriceStatement = salesConnection.prepareStatement("SELECT PRICE,STOCK,NAME FROM PRODUCT WHERE CODE=?");
      PreparedStatement stockUpdateStatement = salesConnection.prepareStatement("UPDATE PRODUCT SET STOCK=STOCK-? WHERE CODE=?");
      PreparedStatement grandTotalUpdateStatement = salesConnection.prepareStatement("UPDATE SALES SET GRANDTOTAL=? WHERE ID=?");
      setSales(salesEntryStatement,sales);
      ResultSet salesEntryResultSet = salesEntryStatement.executeQuery();
      Sales salesEntry = new Sales();
      while (salesEntryResultSet.next()) {
          salesEntry=getSalesFromResultSet(salesEntryResultSet,salesEntry);
      }
      List<SalesItem> salesItemList = new ArrayList<>();
      ResultSet salesItemInsertResultSet;
      double price;
      float stock;
      String productName;
      double grandTotal = 0.0;
      for (SalesItem salesItem : sales.getSalesItemList()) {
        salesPriceStatement.setString(1, salesItem.getProduct().getCode());
        ResultSet salesPriceResultSet = salesPriceStatement.executeQuery();
        salesPriceResultSet.next();
        price = salesPriceResultSet.getDouble(1);
        stock = salesPriceResultSet.getFloat(2);
        productName = salesPriceResultSet.getString(3);
        grandTotal += price * salesItem.getQuantity();
        if (stock < salesItem.getQuantity()) {
          salesConnection.rollback();
          return null;
        }
        setSalesItems(salesItemInsertStatement,salesItem,salesEntry,price);
        salesItemInsertResultSet = salesItemInsertStatement.executeQuery();
        stockUpdateStatement.setFloat(1, salesItem.getQuantity());
        stockUpdateStatement.setString(2, salesItem.getProduct().getCode());
        stockUpdateStatement.executeUpdate();
        while (salesItemInsertResultSet.next()) {
          salesItemList.add(getSalesItemFromResultSet(salesItemInsertResultSet,productName));
        }
      }
      grandTotalUpdateStatement.setDouble(1, grandTotal);
      grandTotalUpdateStatement.setInt(2, salesEntry.getId());
      grandTotalUpdateStatement.executeUpdate();
      salesEntry.setGrandTotal(grandTotal);
      salesEntry.setSalesItemList(salesItemList);
      salesConnection.commit();
      salesConnection.setAutoCommit(true);
      return salesEntry;
    } catch (SQLException e) {
      salesConnection.rollback();
      e.printStackTrace();
      throw new ApplicationErrorException(e.getMessage());
    }
  }

  private PreparedStatement setSales(PreparedStatement statement, Sales sales) throws SQLException {
    statement.setDate(1, Date.valueOf(sales.getDate()));
    statement.setDouble(2, sales.getGrandTotal());
    return statement;
  }

  private PreparedStatement setSalesItems(PreparedStatement statement,SalesItem salesItem, Sales sales,double price) throws SQLException {
    statement.setInt(1, sales.getId());
    statement.setString(2, salesItem.getProduct().getCode());
    statement.setFloat(3, salesItem.getQuantity());
    statement.setDouble(4, price);
    return statement;
  }

  private Sales getSalesFromResultSet(ResultSet resultSet,Sales sales) throws SQLException {
    sales.setId(resultSet.getInt(1));
    sales.setDate(String.valueOf(resultSet.getDate(2)));
    sales.setGrandTotal(resultSet.getDouble(3));
    return sales;
  }

  private SalesItem getSalesItemFromResultSet(ResultSet resultSet,String name) throws SQLException {
    return new SalesItem(
            new Product(resultSet.getString(2), name),
            resultSet.getFloat(3),
            resultSet.getDouble(4));
  }

  @Override
  public int count(String parameter) throws ApplicationErrorException {
    int count;
    try {
      ResultSet countResultSet;
      if (parameter == null) {
        countResultSet = salesConnection.createStatement().executeQuery("SELECT COUNT(ID) FROM SALES");
      } else {
        countResultSet = salesConnection.createStatement().executeQuery(
                "SELECT COUNT(*) FROM PURCHASE WHERE CAST(DATE AS TEXT) ILIKE'" + parameter + "'");
      }
      countResultSet.next();
      count = countResultSet.getInt(1);
      return count;
    } catch (Exception e) {
      throw new ApplicationErrorException(e.getMessage());
    }
  }


  @Override
  public List list(String attribute, String searchText, int pageLength, int offset)
      throws ApplicationErrorException {
    int count;
    try {
      String EntryCount="SELECT COUNT(*) OVER() FROM SALES WHERE " + attribute + "= COALESCE('"+searchText+"'," + attribute + ")" + " ORDER BY ID";
      String listQuery = "SELECT * FROM SALES WHERE " + attribute + "= COALESCE('"+searchText+"'," + attribute + ")" + " ORDER BY ID LIMIT " + pageLength + "  OFFSET " + offset;
      PreparedStatement listStatement = salesConnection.prepareStatement(listQuery);
      PreparedStatement countStatement=salesConnection.prepareStatement(EntryCount);
      ResultSet countResultSet=countStatement.executeQuery();
      if(countResultSet.next())
        count=countResultSet.getInt(1);
      else return null;
      if(count<=offset){
        int pageCount;
        if (count % pageLength == 0)
          pageCount=count/pageLength;
        else
          pageCount=(count/pageLength)+1;
        throw new PageCountOutOfBoundsException(">> Requested Page doesnt Exist!!\n>> Existing Pagecount with given pagination "+pageCount);
    }
      ResultSet listResultSet = listStatement.executeQuery();
      return listHelper(listResultSet);
    } catch (Exception e) {
      throw new ApplicationErrorException(e.getMessage());
    }
  }



  @Override
  public List list(String searchText) throws ApplicationErrorException {
    try {
      String listQuery =
          "SELECT * FROM SALES WHERE CAST(ID AS TEXT) '"
              + searchText
              + "' OR CAST(DATE AS TEXT) ILIKE '"
              + searchText
              + "' OR CAST (INVOICE AS TEXT) ILIKE '"
              + searchText
              + "'";
      ResultSet listResultSet = salesConnection.createStatement().executeQuery(listQuery);
      return listHelper(listResultSet);
    } catch (Exception e) {
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
  private List<Sales> listHelper(ResultSet resultSet) throws SQLException {
    while (resultSet.next()) {
      Sales listedSale = new Sales();
      getSalesFromResultSet(resultSet,listedSale);
      salesList.add(listedSale);
    }
    PreparedStatement listSalesItemStatement = salesConnection.prepareStatement("SELECT P.NAME, S.PRODUCTCODE,S.QUANTITY,S.SALESPRICE FROM SALESITEMS S INNER JOIN PRODUCT P ON P.CODE=S.PRODUCTCODE WHERE S.ID=?");
    for (Sales sales : salesList) {
      List<SalesItem> salesItemList = new ArrayList<>();
      listSalesItemStatement.setInt(1, sales.getId());
      ResultSet listSalesResultSet = listSalesItemStatement.executeQuery();
      while (listSalesResultSet.next())
        salesItemList.add(getSalesItemFromResultSet(listSalesResultSet,listSalesResultSet.getString(1)));
      sales.setSalesItemList(salesItemList);
    }
    return salesList;
  }


  @Override
  public int delete(int id) throws ApplicationErrorException {
    try {
      int salesItemUpdatedCount =
              salesConnection.createStatement().executeUpdate("DELETE FROM SALESITEMS WHERE ID='" + id + "'");
      int salesUpdatedCount =
              salesConnection.createStatement().executeUpdate("DELETE FROM SALES WHERE ID='" + id + "'");
      if (salesItemUpdatedCount > 0 && salesUpdatedCount > 0) {
        return 1;
      } else return -1;
    } catch (Exception e) {
      throw new ApplicationErrorException(e.getMessage());
    }
  }
}
