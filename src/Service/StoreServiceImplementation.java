package Service;

import DAO.ApplicationErrorException;
import DAO.StoreDAO;
import Entity.Store;

import java.sql.SQLException;

public class StoreServiceImplementation implements StoreService {
	private final StoreDAO storeDAO = new StoreDAO();
	private final String NAME_REGEX = "^[a-zA-Z0-9\\s]{3,30}$";
	private final String PHONE_NUMBER_REGEX = "^[6789]\\d{9}$";
	private final String GST_NUMBER_REGEX = "^[a-zA-Z0-9]{15}$";

	@Override
	public Store create(Store store) throws SQLException, ApplicationErrorException, InvalidTemplateException {
		validate(store);
		return storeDAO.create(store);
	}

	@Override
	public Store edit(Store store) throws SQLException, ApplicationErrorException, InvalidTemplateException {
		validate(store);
		return storeDAO.edit(store);
	}


	@Override
	public Integer delete(String userName, String adminPassword) throws ApplicationErrorException {
		if(userName != null && adminPassword != null)
			return storeDAO.delete(userName, adminPassword);
		else
			throw new NullPointerException(">> Username Or Password Cannot be Null!!");
	}

	/**
	 * This method validates the Store attributes.
	 *
	 * @param store Store to be validated
	 */
	private void validate(Store store) throws InvalidTemplateException {
		if(store == null)
			throw new NullPointerException(">> Store cannot be Null!!");
		if(store.getName() != null && ! store.getName().matches(NAME_REGEX))
			throw new InvalidTemplateException(">> Invalid Store Name!!");
		if(store.getPhoneNumber() != 0 && ! String.valueOf(store.getPhoneNumber()).matches(PHONE_NUMBER_REGEX))
			throw new InvalidTemplateException(">> Invalid Phone-number!!");
		if(store.getGstCode() != null && ! String.valueOf(store.getGstCode()).matches(GST_NUMBER_REGEX))
			throw new InvalidTemplateException(">> Invalid GstCode!!");
	}
}
