package Service;

import DAO.ApplicationErrorException;
import DAO.PageCountOutOfBoundsException;
import DAO.UserDAO;
import Entity.User;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class UserServiceImplementation implements UserService {

	private final UserDAO userDAO = new UserDAO();
	private final String NAME_REGEX = "^[a-zA-Z\\s]{3,30}$";
	private final String LAST_NAME_REGEX = "^[a-zA-Z\\s]{0,30}$";
	private final String PASSWORD_REGEX = "^[a-zA-Z0-9]{8,30}$";
	private final List<String> userTypeList = Arrays.asList("Sales", "Purchase", "Admin");
	private final String PHONE_NUMBER_REGEX = "^[6789]\\d{9}$";

	@Override
	public User create(User user) throws Exception {
		validate(user);
		return userDAO.create(user);
	}

	@Override
	public Integer count(String attribute, String searchText) throws ApplicationErrorException {
			return userDAO.count(attribute, searchText);
	}

	@Override
	public List<User> list(HashMap<String, String> listattributes) throws ApplicationErrorException, PageCountOutOfBoundsException {
		List<User> userList;
		if(Collections.frequency(listattributes.values(), null) == listattributes.size() - 1 && listattributes.get("Searchtext") != null) {
			userList = userDAO.searchList(listattributes.get("Searchtext"));
		} else {
			int pageLength = Integer.parseInt(listattributes.get("Pagelength"));
			int pageNumber = Integer.parseInt(listattributes.get("Pagenumber"));
			int offset = (pageLength * pageNumber) - pageLength;
			userList = userDAO.list(listattributes.get("Attribute"), listattributes.get("Searchtext"), pageLength, offset);
		}
		return userList;
	}

	@Override
	public User edit(User user) throws Exception {
		validate(user);
		return userDAO.edit(user);
	}

	@Override
	public Integer delete(String username) throws ApplicationErrorException {
		if(username != null)
			return userDAO.delete(username);
		else
			return - 1;
	}

	/**
	 * This method validates the user attributes.
	 *
	 * @param user user to be validated.
	 */
	private void validate(User user) throws InvalidTemplateException {
		if(user == null)
			throw new NullPointerException(">> User Cannot be Null!!");
		if(user.getUserName() != null && ! user.getUserName().matches(NAME_REGEX))
			throw new InvalidTemplateException(">> Invalid UserName!!");
		if(user.getFirstName() != null && ! user.getFirstName().matches(NAME_REGEX))
			throw new InvalidTemplateException(">> Invalid FirstName!!");
		if(user.getLastName() != null && ! user.getLastName().matches(LAST_NAME_REGEX))
			throw new InvalidTemplateException(">> Invalid LastName!!");
		if(user.getPassWord() != null && ! user.getPassWord().matches(PASSWORD_REGEX))
			throw new InvalidTemplateException(">> Invalid Password!!");
		if(user.getPhoneNumber() != 0 && ! String.valueOf(user.getPhoneNumber()).matches(PHONE_NUMBER_REGEX))
			throw new InvalidTemplateException(">> Invalid Phone-number!!");
		if(user.getUserType() != null && ! userTypeList.contains(user.getUserType()))
			throw new InvalidTemplateException(">> Invalid Usertype!!");
	}
}
