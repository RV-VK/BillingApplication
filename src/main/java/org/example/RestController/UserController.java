package org.example.RestController;

import org.example.DAO.ApplicationErrorException;
import org.example.DAO.PageCountOutOfBoundsException;
import org.example.Entity.User;
import org.example.Service.InvalidTemplateException;
import org.example.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private UserService userService;
	@Autowired
	private ListAttributeMapValidator validator;
	private HashMap<String, String> listAttributes = new HashMap<>();
	private List<User> userList;

	@GetMapping(path = "/users", produces = "application/json")
	@PreAuthorize("hasRole('Admin')")
	public List<User> getAll() throws PageCountOutOfBoundsException, ApplicationErrorException {
		listAttributes.put("Pagelength", String.valueOf(Integer.MAX_VALUE));
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", "id");
		listAttributes.put("Searchtext", null);

		return listHelperFunction(listAttributes);
	}

	@GetMapping(path = "/users/{pageLength}", produces = "application/json")
	@PreAuthorize("hasRole('Admin')")
	public List<User> getByPageLength(@PathVariable String pageLength) throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		listAttributes.put("Pagelength", pageLength);
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", "id");
		listAttributes.put("Searchtext", null);
		validator.validate(listAttributes, UserController.class);
		return listHelperFunction(listAttributes);
	}

	@GetMapping(path = "/users/{pageLength}/{pageNumber}", produces = "application/json")
	@PreAuthorize("hasRole('Admin')")
	public List<User> getByPageLengthAndPageNumber(@PathVariable String pageLength, @PathVariable String pageNumber) throws InvalidTemplateException, PageCountOutOfBoundsException, ApplicationErrorException {
		listAttributes.put("Pagelength", pageLength);
		listAttributes.put("Pagenumber", pageNumber);
		listAttributes.put("Attribute", "id");
		listAttributes.put("Searchtext", null);
		validator.validate(listAttributes, UserController.class);
		return listHelperFunction(listAttributes);
	}

	@GetMapping(path = "/users/find/{searchText}", produces = "application/json")
	@PreAuthorize("hasRole('Admin')")
	public List<User> getBySearchText(@PathVariable String searchText) throws PageCountOutOfBoundsException, ApplicationErrorException {
		listAttributes.put("Pagelength", null);
		listAttributes.put("Pagenumber", null);
		listAttributes.put("Attribute", null);
		listAttributes.put("Searchtext", searchText);
		return listHelperFunction(listAttributes);
	}

	@GetMapping(path = "/users/find/{attribute}/{searchText}", produces = "application/json")
	@PreAuthorize("hasRole('Admin')")
	public List<User> getByAttributeAndSearchText(@PathVariable String attribute, @PathVariable String searchText) throws InvalidTemplateException, PageCountOutOfBoundsException, ApplicationErrorException {
		listAttributes.put("Pagelength", String.valueOf(Integer.MAX_VALUE));
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", attribute);
		listAttributes.put("Searchtext", searchText);
		validator.validate(listAttributes, UserController.class);
		return listHelperFunction(listAttributes);
	}

	@GetMapping(path = "/users/find/{attribute}/{searchText}/{PageLength}", produces = "application/json")
	@PreAuthorize("hasRole('Admin')")
	public List<User> getByAttributeAndSearchTextWithPageLength(@PathVariable String attribute, @PathVariable String searchText, @PathVariable String PageLength) throws InvalidTemplateException, PageCountOutOfBoundsException, ApplicationErrorException {
		listAttributes.put("Pagelength", PageLength);
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", attribute);
		listAttributes.put("Searchtext", searchText);
		validator.validate(listAttributes, UserController.class);
		return listHelperFunction(listAttributes);
	}

	@GetMapping(path = "/users/find/{attribute}/{searchText}/{PageLength}/{PageNumber}", produces = "application/json")
	@PreAuthorize("hasRole('Admin')")
	public List<User> getByAttributeAndSearchTextWithPageLengthAndPageNumber(@PathVariable String attribute, @PathVariable String searchText, @PathVariable String PageLength, @PathVariable String PageNumber) throws InvalidTemplateException, PageCountOutOfBoundsException, ApplicationErrorException {
		listAttributes.put("Pagelength", PageLength);
		listAttributes.put("Pagenumber", PageNumber);
		listAttributes.put("Attribute", attribute);
		listAttributes.put("Searchtext", searchText);
		validator.validate(listAttributes, UserController.class);
		return listHelperFunction(listAttributes);
	}

	@PostMapping(path = "/user", produces = "application/json")
	@PreAuthorize("hasRole('Admin')")
	public User add(@RequestBody User user) throws Exception {
		User createdUser;
		try {
			createdUser = userService.create(user);
		} catch(Exception exception) {
			logger.error("User Creation Failed!, {} ", exception.getMessage());
			throw exception;
		}
		logger.info("User Creation Successfull! : {} ", createdUser);
		return createdUser;
	}

	@GetMapping(path = "/countUsers", produces = "application/json")
	@PreAuthorize("hasRole('Admin')")
	public Integer count() throws ApplicationErrorException {
		String attribute = "id";
		String searchText = null;
		Integer count;
		try {
			count = userService.count(attribute, searchText);
		} catch(ApplicationErrorException exception) {
			logger.error("Error while retrieving data from database, {} ", exception.getMessage());
			throw exception;
		}
		logger.info("Returned Successfully!, User Count : {} ", count);
		return count;
	}

	@PutMapping(path = "/user", produces = "application/json")
	@PreAuthorize("hasRole('Admin')")
	public User edit(@RequestBody User user) throws Exception {
		User editedUser;
		try {
			editedUser = userService.edit(user);
		} catch(Exception exception) {
			logger.error("User Edit Failed!, {} ", exception.getMessage());
			throw exception;
		}
		if(editedUser == null) {
			logger.error("User edit Failed! Given Id doesnt exists : {} ", user.getId());
			throw new InvalidTemplateException("The Id doesnt exists to edit! Please Give an Existing Id");
		} else {
			logger.info("User Edited Successfully!, Edited User : {} ", editedUser);
			return editedUser;
		}
	}

	@DeleteMapping(path = "/deleteUser/{username}", produces = "application/json")
	@PreAuthorize("hasRole('Admin')")
	public Integer delete(@PathVariable String username) throws ApplicationErrorException, InvalidTemplateException {
		Integer statusCode;
		try {
			statusCode = userService.delete(username);
		} catch(ApplicationErrorException exception) {
			logger.error("User Deletion failed!, {} ", exception.getMessage());
			throw exception;
		}
		if(statusCode == 1) {
			logger.info("User deleted Successfully!");
			return statusCode;
		} else {
			logger.error("User deletion failed!, Given username doesnt exists : {} ", username);
			throw new InvalidTemplateException("Username doesnt Exists!");
		}
	}

	private List<User> listHelperFunction(HashMap<String, String> listAttributes) throws PageCountOutOfBoundsException, ApplicationErrorException {
		try {
			userList = userService.list(listAttributes);
		} catch(PageCountOutOfBoundsException exception) {
			logger.warn("Error while returning the list. {} ", exception.getMessage());
			throw exception;
		} catch(ApplicationErrorException exception) {
			logger.error("Error while retrieving data from the Database, {} ", exception.getMessage());
			throw exception;
		}
		logger.info("List returned Successfully!");
		return userList;
	}
}
