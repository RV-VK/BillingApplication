package Service;

import DAO.*;
import Entity.Product;
import Entity.Sales;
import Entity.SalesItem;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SalesServiceImplementation implements SalesService {
  private SalesDAO salesDAO = new SalesDAOImplementation();



  @Override
  public Sales create(Sales sales)
      throws ApplicationErrorException, SQLException, UnDividableEntityException {
    boolean isDividable;
    double grandtotal=0.0;
    ProductDAO getProductByCode = new ProductDAOImplementation();
    UnitDAO getUnitByCode = new UnitDAOImplementation();
    for (SalesItem salesItem : sales.getSalesItemList()) {
      try {
        Product product=getProductByCode.findByCode(salesItem.getProduct().getCode());
        salesItem.setProduct(product);
        isDividable=getUnitByCode.findByCode(product.getunitcode()).getIsDividable();
        grandtotal+=salesItem.getProduct().getPrice()*salesItem.getQuantity();
      } catch (NullPointerException e) {
        return new Sales();
      }
      if ((! isDividable && salesItem.getQuantity() % 1 != 0)) {
        throw new UnDividableEntityException(">> Product "+salesItem.getProduct().getCode()+" is not a dividable product");
      }
    }
    sales.setGrandTotal(grandtotal);
    Sales createdSale = salesDAO.create(sales);
    return createdSale;
  }



  @Override
  public int count(String parameter) throws ApplicationErrorException {
    String dateRegex = "([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))";
    if (parameter != null) {
      if (!parameter.matches(dateRegex)) {
        return -1;
      }
    }
    return salesDAO.count(parameter);
  }



  @Override
  public List<Sales> list(HashMap<String, String> listAttributes)
      throws ApplicationErrorException, PageCountOutOfBoundsException {
    List<Sales> salesList;
    if (Collections.frequency(listAttributes.values(), null) == 0
        || Collections.frequency(listAttributes.values(), null) == 1) {
      int pageLength = Integer.parseInt(listAttributes.get("Pagelength"));
      int pageNumber = Integer.parseInt(listAttributes.get("Pagenumber"));
      int offset = (pageLength * pageNumber) - pageLength;
      salesList =
          salesDAO.list(
              listAttributes.get("Attribute"),
              listAttributes.get("Searchtext"),
              pageLength,
              offset);
      return salesList;
    } else if (Collections.frequency(listAttributes.values(), null) == listAttributes.size() - 1
        && listAttributes.get("Searchtext") != null) {
      salesList = salesDAO.list(listAttributes.get("Searchtext"));
      return salesList;
    }
    return null;
  }



  @Override
  public int delete(String id) throws ApplicationErrorException {
    return salesDAO.delete(Integer.parseInt(id));
  }
}
