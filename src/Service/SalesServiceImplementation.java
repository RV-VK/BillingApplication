package Service;

import DAO.*;
import Entity.Sales;
import Entity.SalesItem;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SalesServiceImplementation implements SalesService {
  private SalesDAO salesDAO = new SalesDAOImplementation();


  /**
   * THis method invokes the DAO of the Sales entity and serves the Sales.
   *
   * @param sales Input Sales entity.
   * @return Sales - Created Sales entity.
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   * @throws SQLException Exception thrown based on SQL syntax.
   * @throws UnDividableEntityException Exception thrown when a Non-dividable unit in a Sales transaction is asked for a decimal quantity.
   */
  @Override
  public Sales create(Sales sales)
      throws ApplicationErrorException, SQLException, UnDividableEntityException {
    boolean isDividable;
    ProductDAO getProductByCode = new ProductDAOImplementation();
    UnitDAO getUnitByCode = new UnitDAOImplementation();
    for (SalesItem salesItem : sales.getSalesItemList()) {
      try {
        isDividable =
            getUnitByCode
                .findByCode(
                    (getProductByCode.findByCode(salesItem.getProduct().getCode())).getunitcode())
                .getIsDividable();
      } catch (NullPointerException e) {
        return new Sales();
      }
      if ((! isDividable && salesItem.getQuantity() % 1 != 0)) {
        throw new UnDividableEntityException(">> Product "+salesItem.getProduct().getCode()+" is not a dividable product");
      }
    }
    Sales createdSale = salesDAO.create(sales);
    return createdSale;
  }


  /**
   * This method invokes the DAO of the Sales entity and serves the Count function.
   *
   * @param parameter Date of Sales.
   * @return Count - Integer.
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */
  @Override
  public int count(String parameter) throws ApplicationErrorException {
    SalesDAO salesCountDAO = new SalesDAOImplementation();
    String dateRegex = "([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))";
    if (parameter != null) {
      if (!parameter.matches(dateRegex)) {
        return -1;
      }
    }
    return salesCountDAO.count(parameter);
  }


  /**
   * This method invokes the DAO of the Sales entity and serves the List function.
   *
   * @param listAttributes Key Value pairs (Map) of List function attributes.
   * @return List - Sales.
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   * @throws PageCountOutOfBoundsException Custom Exception thrown when a non-existing page is given as input in Pageable List.
   */
  @Override
  public List<Sales> list(HashMap<String, String> listAttributes)
      throws ApplicationErrorException, PageCountOutOfBoundsException {
    List<Sales> salesList;
    SalesDAO listSalesDAO = new SalesDAOImplementation();
    if (Collections.frequency(listAttributes.values(), null) == 0
        || Collections.frequency(listAttributes.values(), null) == 1) {
      int pageLength = Integer.parseInt(listAttributes.get("Pagelength"));
      int pageNumber = Integer.parseInt(listAttributes.get("Pagenumber"));
      int offset = (pageLength * pageNumber) - pageLength;
      salesList =
          listSalesDAO.list(
              listAttributes.get("Attribute"),
              listAttributes.get("Searchtext"),
              pageLength,
              offset);
      return salesList;
    } else if (Collections.frequency(listAttributes.values(), null) == listAttributes.size() - 1
        && listAttributes.get("Searchtext") != null) {
      salesList = listSalesDAO.list(listAttributes.get("Searchtext"));
      return salesList;
    }
    return null;
  }


  /**
   * This method invokes the DAO of the Sales entity and serves the Delete function.
   *
   * @param id Input id to perform delete.
   * @return resultCode - Integer.
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */
  @Override
  public int delete(String id) throws ApplicationErrorException {
    SalesDAO salesDeleteDAO = new SalesDAOImplementation();
    return salesDeleteDAO.delete(Integer.parseInt(id));
  }
}
