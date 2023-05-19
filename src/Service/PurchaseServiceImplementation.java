package Service;

import DAO.*;
import Entity.Purchase;
import Entity.PurchaseItem;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class PurchaseServiceImplementation implements PurchaseService {

  private final PurchaseDAO purchaseDAO = new PurchaseDAOImplementation();


  /**
   * This method invokes the DAO of the Purchase Entity and serves the Purchase function.
   *
   * @param purchase Input Purchase.
   * @return Purchase - Created Purchase
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   * @throws SQLException Exception thrown based on SQL syntax.
   */
  @Override
  public Purchase create(Purchase purchase)
      throws ApplicationErrorException, SQLException {
    ProductDAO productDAO = new ProductDAOImplementation();
    UnitDAO getUnitByCode = new UnitDAOImplementation();
    boolean isDividable;
    for (PurchaseItem purchaseItem : purchase.getPurchaseItemList()) {
      try {
        isDividable =
            getUnitByCode
                .findByCode(
                    (productDAO.findByCode(purchaseItem.getProduct().getCode())).getunitcode())
                .getIsDividable();
      } catch (NullPointerException e) {
        return new Purchase();
      }
      if (!isDividable && purchaseItem.getQuantity() % 1 != 0) {
        return null;
      }
    }
    return purchaseDAO.create(purchase);
  }

  /**
   * This method invokes the DAO of the Purchase entity and serves the Count function.
   *
   * @param parameter Date of Purchase.
   * @return Count - Integer.
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */
  @Override
  public int count(String parameter) throws ApplicationErrorException {
    String dateRegex = "([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))";
    if (parameter != null) {
      if (!parameter.matches(dateRegex)) return -1;
    }
    return purchaseDAO.count(parameter);
  }


  /**
   * This method invokes the DAO of the Purchase entity and serves the List function.
   *
   * @param listattributes Key Value pairs (Map) of List function attributes.
   * @return List - Purchase.
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */
  @Override
  public List<Purchase> list(HashMap<String, String> listattributes)
      throws ApplicationErrorException {
    List<Purchase> purchaseList;
    if (Collections.frequency(listattributes.values(), null) == 0
        || Collections.frequency(listattributes.values(), null) == 1) {
      int pageLength = Integer.parseInt(listattributes.get("Pagelength"));
      int pageNumber = Integer.parseInt(listattributes.get("Pagenumber"));
      int offset = (pageLength * pageNumber) - pageLength;
      purchaseList =
          purchaseDAO.list(
              listattributes.get("Attribute"),
              listattributes.get("Searchtext"),
              pageLength,
              offset);
      return purchaseList;
    } else if (Collections.frequency(listattributes.values(), null) == listattributes.size() - 1
        && listattributes.get("Searchtext") != null) {
      purchaseList = purchaseDAO.list(listattributes.get("Searchtext"));
      return purchaseList;
    }
    return null;
  }

  /**
   * This method invokes the DAO of the Purchase entity and serves the Delete function.
   * @param invoice Input invoice.
   * @return resultCode - Integer.
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */
  @Override
  public int delete(String invoice) throws ApplicationErrorException {
    return purchaseDAO.delete(Integer.parseInt(invoice));
  }
}
