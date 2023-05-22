package DAO;

import DBConnection.DBHelper;
import Entity.Product;
import Entity.Purchase;
import Entity.PurchaseItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseDAOImplementation implements PurchaseDAO {
  private final Connection purchaseConnection = DBHelper.getConnection();
  private List<Purchase> purchaseList = new ArrayList<>();



  @Override
  public Purchase create(Purchase purchase) throws ApplicationErrorException, SQLException {
    try {
      purchaseConnection.setAutoCommit(false);
      PreparedStatement stockUpdateStatement = purchaseConnection.prepareStatement("UPDATE PRODUCT SET STOCK=STOCK+? WHERE CODE=?");
      PreparedStatement purchaseEntryStatement = purchaseConnection.prepareStatement("INSERT INTO PURCHASE(DATE,INVOICE,GRANDTOTAL) VALUES(?,?,?) RETURNING *");
      PreparedStatement productNameStatement = purchaseConnection.prepareStatement("SELECT NAME FROM PRODUCT WHERE CODE=?");
      setPurchase(purchaseEntryStatement,purchase);
      ResultSet purchaseEntryResultSet = purchaseEntryStatement.executeQuery();
      Purchase purchaseEntry = new Purchase();
      while (purchaseEntryResultSet.next()) {
            purchaseEntry=getPurchaseFromResultSet(purchaseEntryResultSet,purchaseEntry);
      }
      List<PurchaseItem> purchaseItemList = new ArrayList<>();
      PreparedStatement purchaseItemInsertStatement =
          purchaseConnection.prepareStatement("INSERT INTO PURCHASEITEMS(INVOICE,PRODUCTCODE,QUANTITY,COSTPRICE) VALUES(?,?,?,?) RETURNING *");
      ResultSet purchaseItemInsertResultSet;
      for (PurchaseItem purchaseItem : purchase.getPurchaseItemList()) {
        setPurchaseItems(purchaseItemInsertStatement,purchaseItem,purchaseEntry);
        purchaseItemInsertResultSet = purchaseItemInsertStatement.executeQuery();
        stockUpdateStatement.setFloat(1, purchaseItem.getQuantity());
        stockUpdateStatement.setString(2, purchaseItem.getProduct().getCode());
        if (!(stockUpdateStatement.executeUpdate() > 0)) {
          return null;
        }
        while (purchaseItemInsertResultSet.next()) {
          productNameStatement.setString(1, purchaseItemInsertResultSet.getString(2));
          ResultSet productNameResultSet = productNameStatement.executeQuery();
          productNameResultSet.next();
          purchaseItemList.add(getPurchaseItemFromResultSet(purchaseItemInsertResultSet,productNameResultSet.getString(1)));
        }
      }
      purchaseEntry.setPurchaseItemList(purchaseItemList);
      purchaseConnection.commit();
      purchaseConnection.setAutoCommit(true);
      return purchaseEntry;
    } catch (SQLException e) {
      purchaseConnection.rollback();
      if(e.getSQLState().equals("23503"))
        throw new ApplicationErrorException(">> The Product code you have entered doesnt exists!");
      throw new ApplicationErrorException(e.getMessage());
    }
  }

  private PreparedStatement setPurchase(PreparedStatement statement,Purchase purchase) throws SQLException {
    statement.setDate(1, Date.valueOf(purchase.getDate()));
    statement.setInt(2, purchase.getInvoice());
    statement.setDouble(3, purchase.getGrandTotal());
    return statement;
  }
  private PreparedStatement setPurchaseItems(PreparedStatement statement,PurchaseItem purchaseItem,Purchase purchase) throws SQLException {
    statement.setInt(1, purchase.getInvoice());
    statement.setString(2, purchaseItem.getProduct().getCode());
    statement.setFloat(3, purchaseItem.getQuantity());
    statement.setDouble(4, purchaseItem.getUnitPurchasePrice());
    return statement;

  }
  private Purchase getPurchaseFromResultSet(ResultSet resultSet,Purchase purchase) throws SQLException {
    purchase.setId(resultSet.getInt(1));
    purchase.setDate(String.valueOf(resultSet.getDate(2)));
    purchase.setInvoice(resultSet.getInt(3));
    purchase.setGrandTotal(resultSet.getDouble(4));
    return purchase;
  }
  private PurchaseItem getPurchaseItemFromResultSet(ResultSet resultSet,String name) throws SQLException {
    return new PurchaseItem(
            new Product(
                    resultSet.getString(2), name),
            resultSet.getFloat(3),
            resultSet.getDouble(4));
  }


  @Override
  public int count(String parameter) throws ApplicationErrorException {
    int count;
    try {
      ResultSet countResultSet;
      if (parameter == null) {
        countResultSet = purchaseConnection.createStatement().executeQuery("SELECT COUNT(ID) FROM PURCHASE");
      } else {
        countResultSet = purchaseConnection.createStatement().executeQuery(
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
      String EntryCount="SELECT COUNT(*) OVER() FROM PURCHASE WHERE " + attribute + "= COALESCE("+searchText+"," + attribute + ")" + " ORDER BY ID";
      String listQuery ="SELECT * FROM PURCHASE WHERE " + attribute + "= COALESCE("+searchText+"," + attribute + ")" + " ORDER BY ID LIMIT " + pageLength + "  OFFSET " + offset;
      PreparedStatement countStatement=purchaseConnection.prepareStatement(EntryCount);
      PreparedStatement listStatement = purchaseConnection.prepareStatement(listQuery);
      ResultSet countResultSet=countStatement.executeQuery();
      if (countResultSet.next()) {
        count = countResultSet.getInt(1);
      }
      else return null;
      if (count <= offset) {
        int pageCount;
        if (count % pageLength == 0)
          pageCount=count/pageLength;
        else
          pageCount=(count/pageLength)+1;
        throw new PageCountOutOfBoundsException(
            ">> Requested Page doesnt Exist!!\n>> Existing Pagecount with given pagination "
                + pageCount);
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
          "SELECT * FROM PURCHASE WHERE CAST(ID AS TEXT) ILIKE '"
              + searchText
              + "' OR CAST(DATE AS TEXT) ILIKE '"
              + searchText
              + "' OR CAST(INVOICE AS TEXT) ILIKE '"
              + searchText
              + "'";
      ResultSet listResultSet = purchaseConnection.createStatement().executeQuery(listQuery);
      return listHelper(listResultSet);
    } catch (Exception e) {
      throw new ApplicationErrorException(e.getMessage());
    }
  }

  /**
   * This method serves the listDAO function.
   *
   * @param resultSet ListQuery results.
   * @return List - Purchase.
   * @throws SQLException Exception thrown based on SQL syntax.
   */
  private List<Purchase> listHelper(ResultSet resultSet) throws SQLException {
    while (resultSet.next()) {
      Purchase listedPurchase = new Purchase();
      getPurchaseFromResultSet(resultSet,listedPurchase);
      purchaseList.add(listedPurchase);
    }
    PreparedStatement listPurchaseItemsStatement =
            purchaseConnection.prepareStatement(
                    "SELECT P.NAME,PU.PRODUCTCODE,PU.QUANTITY,PU.COSTPRICE FROM PURCHASEITEMS PU INNER JOIN PRODUCT P ON P.CODE=PU.PRODUCTCODE WHERE PU.INVOICE=?");
    for (Purchase purchase : purchaseList) {
      List<PurchaseItem> purchaseItemList = new ArrayList<>();
      listPurchaseItemsStatement.setInt(1, purchase.getInvoice());
      ResultSet listPurchaseResultSet = listPurchaseItemsStatement.executeQuery();
      while (listPurchaseResultSet.next()) {
        purchaseItemList.add(getPurchaseItemFromResultSet(listPurchaseResultSet,listPurchaseResultSet.getString(1)));
      }
      purchase.setPurchaseItemList(purchaseItemList);
    }
    return purchaseList;
  }


  @Override
  public int delete(int invoice) throws ApplicationErrorException {
    try {
      if ( purchaseConnection.createStatement().executeUpdate("DELETE FROM PURCHASEITEMS WHERE INVOICE='" + invoice + "'")
              > 0
          &&  purchaseConnection.createStatement().executeUpdate("DELETE FROM PURCHASE WHERE INVOICE='" + invoice + "'")
              > 0) {
        return 1;
      } else return -1;
    } catch (Exception e) {
      throw new ApplicationErrorException(e.getMessage());
    }
  }
}
