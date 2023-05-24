package Service;

import DAO.*;
import Entity.User;

import java.sql.SQLException;
import java.util.*;

public class UserServiceImplementation implements UserService {

	private UserDAO userDAO = new UserDAOImplementation();
	private final String NAME_REGEX = "^[a-zA-Z\\s]{1,30}$";
	private final String PASSWORD_REGEX = "^[a-zA-Z0-9]{8,30}$";
	private final List<String> userTypeList = Arrays.asList("Sales", "Purchase", "Admin");
	private final String PHONE_NUMBER_REGEX = "^[6789]\\d{9}$";

	@Override
	public User create(User user)
			throws SQLException,
			ApplicationErrorException,
			UniqueConstraintException,
			InvalidTemplateException {
		if(validate(user)) return userDAO.create(user);
		else return null;
	}

	@Override
	public int count() throws ApplicationErrorException {
		return userDAO.count();
	}

	@Override
	public List<User> list(HashMap<String, String> listattributes)
			throws ApplicationErrorException, PageCountOutOfBoundsException {
		List<User> userList;
		if(Collections.frequency(listattributes.values(), null) == 0
				|| Collections.frequency(listattributes.values(), null) == 1) {
			int pageLength = Integer.parseInt(listattributes.get("Pagelength"));
			int pageNumber = Integer.parseInt(listattributes.get("Pagenumber"));
			int offset = (pageLength * pageNumber) - pageLength;
			userList =
					userDAO.list(
							listattributes.get("Attribute"),
							listattributes.get("Searchtext"),
							pageLength,
							offset);
			return userList;
		} else if(Collections.frequency(listattributes.values(), null) == listattributes.size() - 1
				&& listattributes.get("Searchtext") != null) {
			userList = userDAO.list(listattributes.get("Searchtext"));
			return userList;
		}
		return null;
	}

	@Override
	public User edit(User user)
			throws SQLException,
			ApplicationErrorException,
			UniqueConstraintException,
			InvalidTemplateException {
		validate(user);
		return userDAO.edit(user);
	}

	@Override
	public int delete(String username) throws ApplicationErrorException {
		return userDAO.delete(username);
	}

	/**
	 * This method validates the user attributes.
	 *
	 * @param user user to be validated.
	 * @return status - Boolean.
	 */
	private boolean validate(User user) throws InvalidTemplateException {
		if(user.getUserName() != null && ! user.getUserName().matches(NAME_REGEX))
			throw new InvalidTemplateException(">> Invalid UserName!!");
		if(user.getFirstName() != null && ! user.getFirstName().matches(NAME_REGEX))
			throw new InvalidTemplateException(">> Invalid FirstName!!");
		if(user.getLastName() != null && ! user.getLastName().matches(NAME_REGEX))
			throw new InvalidTemplateException(">> Invalid LastName!!");
		if(user.getPassWord() != null && ! user.getPassWord().matches(PASSWORD_REGEX))
			throw new InvalidTemplateException(">> Invalid Password!!");
		if(user.getPhoneNumber() != 0
				&& ! String.valueOf(user.getPhoneNumber()).matches(PHONE_NUMBER_REGEX))
			throw new InvalidTemplateException(">> Invalid Phone-number!!");
		if(user.getUserType() != null && ! userTypeList.contains(user.getUserType()))
			throw new InvalidTemplateException(">> Invalid Usertype!!");
		return true;
	}
}
