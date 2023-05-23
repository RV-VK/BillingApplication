package DAO;

import DBConnection.DBHelper;
import Entity.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImplementation implements ProductDAO {
  private final Connection productConnection = DBHelper.getConnection();
  private List<Product> productList = new ArrayList<>();


  @Override
  public Product create(Product product)
      throws ApplicationErrorException,
          SQLException,
          UniqueConstraintException,
          UnitCodeViolationException {
    try {
      PreparedStatement productCreateStatement =
          productConnection.prepareStatement(
              "INSERT INTO PRODUCT(CODE,NAME,unitcode,TYPE,PRICE,STOCK) VALUES (?,?,?,?,?,?) RETURNING *");
      setParameters(productCreateStatement,product);
      ResultSet productCreateResultSet = productCreateStatement.executeQuery();
      productCreateResultSet.next();
      return getProductFromResultSet(productCreateResultSet);
    } catch (SQLException e) {
      handleException(e);
      return null;
    }
  }

 /**
   * Private method to handle SQL Exception and convert it to user readable messages.
   *
   * @param e Exception Object
   * @throws UnitCodeViolationException Custom Exception to convey Foreign Key Violation in Product table.
   * @throws UniqueConstraintException Custom Exception to convey Unique constraint Violation in SQL table
   * @throws SQLException Exception thrown based on SQL syntax.
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */
  private void handleException(SQLException e) throws UnitCodeViolationException, UniqueConstraintException, SQLException, ApplicationErrorException {
    if (e.getSQLState().equals("23503")) {
      throw new UnitCodeViolationException(">> The unit Code you have entered  does not Exists!!");
    }
    else if (e.getSQLState().equals("23505")) {
      if (e.getMessage().contains("product_name"))
        throw new UniqueConstraintException(
                ">> Name must be unique!!!\n>> The Name you have entered already exists!!!");
      else
        throw new UniqueConstraintException(
                ">> Code must be unique!!!\n>> The code you have entered already exists!!!");
    }
    else {
      throw new ApplicationErrorException(
              ">> Application has went into an Error!!!\n>>Please Try again");
    }
  }
  /**
   * Private method to Set parameters on PreparedStatement for Product.
   *
   * @param statement Statement to be set.
   * @param product Product entity.
   * @throws SQLException Exception thrown based on SQL syntax.
   */
  private void setParameters(PreparedStatement statement, Product product) throws SQLException {
    statement.setString(1, product.getCode());
    statement.setString(2, product.getName());
    statement.setString(3, product.getunitcode());
    statement.setString(4, product.getType());
    statement.setDouble(5, product.getPrice());
    statement.setFloat(6, product.getAvailableQuantity());
  }
  /**
   * Private Method to assist Product Construction from ResultSet.
   * @param resultSet Product ResultSet.
   * @return Product
   * @throws SQLException Exception thrown based on SQL syntax.
   */
  private Product getProductFromResultSet(ResultSet resultSet) throws SQLException {
    return new Product(resultSet.getInt(1),
            resultSet.getString(2),
            resultSet.getString(3),
            resultSet.getString(4),
            resultSet.getString(5),
            resultSet.getFloat(6),
            resultSet.getDouble(7));
  }


  @Override
  public int count() throws ApplicationErrorException {
    try {
      ResultSet countResultSet = productConnection.createStatement().executeQuery("SELECT COUNT(ID) FROM PRODUCT");
      int count = 0;
      while (countResultSet.next()) {
        count = countResultSet.getInt(1);
      }
      return count;
    } catch (Exception e) {
      throw new ApplicationErrorException(
          "Application has went into an Error!!!\n Please Try again");
    }
  }


  public List<Product> list(String searchText) throws ApplicationErrorException {
    try {
      String listQuery =
          "SELECT * FROM PRODUCT WHERE ( NAME ILIKE '"
              + searchText
              + "' OR CODE ILIKE '"
              + searchText
              + "' OR UNITCODE ILIKE '"
              + searchText
              + "' OR TYPE ILIKE '"
              + searchText
              + "' OR CAST(ID AS TEXT) ILIKE '"
              + searchText
              + "' OR CAST(STOCK AS TEXT) ILIKE '"
              + searchText
              + "' OR CAST(PRICE AS TEXT) ILIKE '"
              + searchText
              + "' )"+" AND ISDELETED=FALSE";
      ResultSet listresultSet =productConnection.createStatement().executeQuery(listQuery);
      return listHelper(listresultSet);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new ApplicationErrorException(
          "Application has went into an Error!!!\n Please Try again");
    }
  }


  @Override
  public List<Product> list(String attribute, String searchText, int pageLength, int offset)
      throws ApplicationErrorException, PageCountOutOfBoundsException {
    try {
      String EntryCount="SELECT COUNT(*) OVER() FROM PRODUCT WHERE " + attribute + "= COALESCE("+searchText+"," + attribute + ")" + "AND ISDELETED=FALSE ORDER BY ID";
      String listQuery = "SELECT * FROM PRODUCT WHERE " + attribute + "= COALESCE("+searchText+"," + attribute + ")" + "AND ISDELETED=FALSE ORDER BY ID LIMIT " + pageLength + "  OFFSET " + offset;
      PreparedStatement countStatement = productConnection.prepareStatement(EntryCount);
      PreparedStatement listStatement = productConnection.prepareStatement(listQuery);
      ResultSet countResultSet=countStatement.executeQuery();
      if(countResultSet.next() && countResultSet.getInt(1)<=offset)
        checkPagination(countResultSet.getInt(1),offset,pageLength);
      ResultSet resultSet = listStatement.executeQuery();
      return listHelper(resultSet);
    } catch (SQLException e) {
      throw new ApplicationErrorException(e.getMessage());
    }
  }
    private void checkPagination(int count,int offset,int pageLength) throws PageCountOutOfBoundsException {
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
    }
  /**
   * This method serves the ListDAO function.
   * @param resultSet ListQuery results.
   * @return List - Product
   * @throws SQLException Exception thrown based on SQL syntax.
   */
  private List<Product> listHelper(ResultSet resultSet) throws SQLException {
    while (resultSet.next()) {
      productList.add(getProductFromResultSet(resultSet));
    }
    return productList;
  }

  @Override
  public Product edit(Product product) throws SQLException, ApplicationErrorException, UniqueConstraintException, UnitCodeViolationException {
    try {
      String editQuery =
          "UPDATE PRODUCT SET CODE= COALESCE(?,CODE),NAME= COALESCE(?,NAME),UNITCODE= COALESCE(?,UNITCODE),TYPE= COALESCE(?,TYPE),PRICE= COALESCE(?,PRICE) WHERE ID=? RETURNING *";
      PreparedStatement editStatement = productConnection.prepareStatement(editQuery);
      setParameters(editStatement, product);
      if (product.getPrice() == 0) {
        editStatement.setNull(5, Types.NUMERIC);
      } else {
        editStatement.setDouble(5, product.getPrice());
      }
      editStatement.setInt(6, product.getId());
      ResultSet editProductResultSet=editStatement.executeQuery();
      return getProductFromResultSet(editProductResultSet);
    } catch (SQLException e) {
      productConnection.rollback();
      handleException(e);
      return null;
    }
  }


  @Override
  public int delete(String parameter) throws ApplicationErrorException {
    try {
      Statement deleteStatement = productConnection.createStatement();
      if (deleteStatement.executeUpdate("UPDATE PRODUCT SET ISDELETED='TRUE' WHERE (CAST(ID AS TEXT) ILIKE '%" + parameter + "%'"+"OR CODE='"+parameter+"') AND STOCK=0 ")>0)
        return 1;
      else
        return -1;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ApplicationErrorException(
          "Application has went into an Error!!!\n Please Try again");
    }
  }


  @Override
  public Product findByCode(String code) throws ApplicationErrorException {
    try {
      ResultSet getProductResultSet =
              productConnection.createStatement()
                      .executeQuery("SELECT * FROM PRODUCT  WHERE CODE='" + code + "'");
      getProductResultSet.next();
      return getProductFromResultSet(getProductResultSet);
    } catch (Exception e) {
      throw new ApplicationErrorException(e.getMessage());
    }
  }

  public int updateStock(String code,float stock) throws ApplicationErrorException {
    try{
    return productConnection.createStatement().executeUpdate("UPDATE PRODUCT SET STOCK=STOCK+"+stock+" WHERE CODE='"+code+"'");
    }catch(Exception e)
    {
      throw new ApplicationErrorException(e.getMessage());
    }
  }

}
