package org.example.RestController;

import org.example.DAO.ApplicationErrorException;
import org.example.DAO.PageCountOutOfBoundsException;
import org.example.Entity.User;
import org.example.Service.InvalidTemplateException;
import org.example.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private ListAttributeMapValidator validator;
	private HashMap<String, String> listAttributes = new HashMap<>();

	@GetMapping(path = "/users", produces = "application/json")
	public List<User> getAll() throws PageCountOutOfBoundsException, ApplicationErrorException {
		listAttributes.put("Pagelength", String.valueOf(Integer.MAX_VALUE));
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", "id");
		listAttributes.put("Searchtext", null);

		return userService.list(listAttributes);
	}

	@GetMapping(path = "/users/{pageLength}", produces = "application/json")
	public List<User> getByPageLength(@PathVariable String pageLength) throws PageCountOutOfBoundsException, ApplicationErrorException, InvalidTemplateException {
		listAttributes.put("Pagelength", pageLength);
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", "id");
		listAttributes.put("Searchtext", null);
		validator.validate(listAttributes, UserController.class);
		return userService.list(listAttributes);
	}

	@GetMapping(path = "/users/{pageLength}/{pageNumber}", produces = "application/json")
	public List<User> getByPageLengthAndPageNumber(@PathVariable String pageLength, @PathVariable String pageNumber) throws InvalidTemplateException, PageCountOutOfBoundsException, ApplicationErrorException {
		listAttributes.put("Pagelength", pageLength);
		listAttributes.put("Pagenumber", pageNumber);
		listAttributes.put("Attribute", "id");
		listAttributes.put("Searchtext", null);
		validator.validate(listAttributes, UserController.class);
		return userService.list(listAttributes);
	}

	@GetMapping(path = "/users/find/{searchText}", produces = "application/json")
	public List<User> getBySearchText(@PathVariable String searchText) throws PageCountOutOfBoundsException, ApplicationErrorException {
		listAttributes.put("Pagelength", null);
		listAttributes.put("Pagenumber", null);
		listAttributes.put("Attribute", null);
		listAttributes.put("Searchtext", searchText);
		return userService.list(listAttributes);
	}

	@GetMapping(path = "/users/find/{attribute}/{searchText}", produces = "application/json")
	public List<User> getByAttributeAndSearchText(@PathVariable String attribute, @PathVariable String searchText) throws InvalidTemplateException, PageCountOutOfBoundsException, ApplicationErrorException {
		listAttributes.put("Pagelength", String.valueOf(Integer.MAX_VALUE));
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", attribute);
		listAttributes.put("Searchtext", searchText);
		validator.validate(listAttributes, UserController.class);
		return userService.list(listAttributes);
	}

	@GetMapping(path = "/users/find/{attribute}/{searchText}/{PageLength}", produces = "application/json")
	public List<User> getByAttributeAndSearchTextWithPageLength(@PathVariable String attribute, @PathVariable String searchText, @PathVariable String PageLength) throws InvalidTemplateException, PageCountOutOfBoundsException, ApplicationErrorException {
		listAttributes.put("Pagelength", PageLength);
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", attribute);
		listAttributes.put("Searchtext", searchText);
		validator.validate(listAttributes, UserController.class);
		return userService.list(listAttributes);
	}

	@GetMapping(path = "/users/find/{attribute}/{searchText}/{PageLength}/{PageNumber}", produces = "application/json")
	public List<User> getByAttributeAndSearchTextWithPageLengthAndPageNumber(@PathVariable String attribute, @PathVariable String searchText, @PathVariable String PageLength, @PathVariable String PageNumber) throws InvalidTemplateException, PageCountOutOfBoundsException, ApplicationErrorException {
		listAttributes.put("Pagelength", PageLength);
		listAttributes.put("Pagenumber", PageNumber);
		listAttributes.put("Attribute", attribute);
		listAttributes.put("Searchtext", searchText);
		validator.validate(listAttributes, UserController.class);
		return userService.list(listAttributes);
	}

	@PostMapping(path = "/user", produces = "application/json")
	public User add(@RequestBody User user) throws Exception {
		return userService.create(user);
	}

	@GetMapping(path = "/countUsers", produces = "application/json")
	public Integer count() throws ApplicationErrorException {
		String attribute = "id";
		String searchText = null;
		return userService.count(attribute, searchText);
	}

	@PutMapping(path = "/user", produces = "application/json")
	public User edit(@RequestBody User user) throws Exception {
		User editedtUser = userService.edit(user);
		if(editedtUser == null)
			throw new InvalidTemplateException("The Id doesnt exists to edit! Please Give an Existing Id");
		else
			return editedtUser;
	}

	@DeleteMapping(path = "/deleteUser/{username}", produces = "application/json")
	public Integer delete(@PathVariable String username) throws ApplicationErrorException, InvalidTemplateException {
		Integer statusCode = userService.delete(username);
		if(statusCode == 1)
			return statusCode;
		else
			throw new InvalidTemplateException("Username doesnt Exists!");
	}

}
