package Service;

import DAO.ApplicationErrorException;
import DAO.StoreDAO;
import DAO.StoreDAOImplementation;
import Entity.Store;

import java.sql.SQLException;

public class StoreServiceImplementation implements StoreService {
 private final StoreDAO storeDAO = new StoreDAOImplementation();
  private final String NAME_REGEX = "^[a-zA-Z\\s]{1,30}$";
  private final String PHONE_NUMBER_REGEX = "^[6789]\\d{9}$";
  private final String GST_NUMBER_REGEX = "^[a-zA-Z0-9]{15}$";

    /**
     * This method invokes the DAO of the Store entity and serves the Create function.
     *
     * @param store Input Store.
     * @return Store - Created store.
     * @throws SQLException Exception thrown based on SQL syntax.
     * @throws ApplicationErrorException Exception thrown due to Persistence problems.
     */
  @Override
  public Store create(Store store) throws SQLException, ApplicationErrorException {
    if (validate(store)) return storeDAO.create(store);
    else return new Store();
  }


    /**
     * This method invokes the DAO of the Store entity and serves the Edit function.
     *
     * @param store Edited store.
     * @return resultCode - Integer.
     * @throws SQLException  Exception thrown based on SQL syntax.
     * @throws ApplicationErrorException Exception thrown due to Persistence problems.
     */
  @Override
  public int edit(Store store) throws SQLException, ApplicationErrorException {
    if (!validate(store)) {
      return 0;
    }
    return storeDAO.edit(store);
  }


    /**
     * This method invokes the DAO of the Store entity and serves the delete function.
     *
     * @param adminPassword Password string to allow deletion.
     * @return resultCode - Integer.
     * @throws ApplicationErrorException Exception thrown due to Persistence problems.
     */
  @Override
  public int delete(String adminPassword) throws ApplicationErrorException {
    return storeDAO.delete(adminPassword);
  }

    /**
     * This method validates the Store attributes.
     *
     * @param store Store to be validated
     * @return status - Boolean
     */
  private boolean validate(Store store) {
    if ((store.getName()!=null&&!store.getName().matches(NAME_REGEX))
        || (store.getPhoneNumber()!=0&&!String.valueOf(store.getPhoneNumber()).matches(PHONE_NUMBER_REGEX))
        || (store.getGstCode()!=null&&!String.valueOf(store.getGstCode()).matches(GST_NUMBER_REGEX))) return false;
    else return true;
  }
}
