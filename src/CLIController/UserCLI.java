package CLIController;

import DAO.ApplicationErrorException;
import DAO.PageCountOutOfBoundsException;
import Entity.User;
import Service.UserService;
import Service.UserServiceImplementation;

import java.util.*;

public class UserCLI {
	private final UserService userService = new UserServiceImplementation();
	private final Scanner scanner = new Scanner(System.in);
	private final HashMap<String, String> listAttributesMap = new HashMap<>();
	private final List<String> userAttributes = Arrays.asList("id", "usertype", "username", "password", "firstname", "lastname", "phonenumber");
	private int id;
	private String userType;
	private String userName;
	private String passWord;
	private String firstName;
	private String lastName;
	private long phoneNumber;
	private int pageLength;
	private int pageNumber;
	private String attribute;
	private String searchText;
	private List<User> userList;

	/**
	 * This method handles the presentation layer of the Create function.
	 *
	 * @param arguments - List of command arguments.
	 */
	public void create(List<String> arguments) {
		Scanner scanner = new Scanner(System.in);
		if(arguments.size() == 3 && arguments.get(2).equals("help")) {
			FeedBackPrinter.printUserHelp("create");
			return;
		} else if(arguments.size() == 2) {
			System.out.print("> ");
			String parameters = scanner.nextLine();
			List<String> userAttributes = List.of(parameters.split(","));
			createHelper(userAttributes);
			return;
		}
		createHelper(arguments.subList(2, arguments.size()));
	}

	/**
	 * This method serves the create function.
	 *
	 * @param userAttributes Attributes of User for Creation.
	 */
	private void createHelper(List<String> userAttributes) {
		if(userAttributes.size() < 6) {
			System.out.println("Insufficient arguments for command \"user create\"");
			FeedBackPrinter.printHelpMessage("user", "create");
			return;
		}
		if(userAttributes.size() > 6) {
			System.out.println("Too many arguments for command \"user create\"");
			FeedBackPrinter.printHelpMessage("user", "create");
			return;
		}
		userType = userAttributes.get(0).trim();
		userName = userAttributes.get(1).trim();
		passWord = userAttributes.get(2).trim();
		firstName = userAttributes.get(3).trim();
		lastName = userAttributes.get(4).trim();
		phoneNumber = 0;
		try {
			phoneNumber = Long.parseLong(userAttributes.get(5).trim());
		} catch(Exception e) {
			System.out.println(">> Invalid format for 6th argument \"phonenumber\"");
			FeedBackPrinter.printHelpMessage("user", "create");
		}
		User user = new User(userType, userName, passWord, firstName, lastName, phoneNumber);
		User createdUser;
		try {
			createdUser = userService.create(user);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return;
		}
		if(createdUser != null) {
			System.out.println(">> User Creation Successfull!!");
			System.out.println(createdUser);
		}
	}

	/**
	 * This method handles the Presentation layer of the Count function.
	 *
	 * @param arguments List of command arguments.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	public void count(List<String> arguments) throws ApplicationErrorException {
		if(arguments.size() > 2) {
			System.out.println(">> Invalid Command!! Try \"help\"");
			return;
		}
		int userCount = userService.count();
		System.out.println(">> User Count " + userCount);
	}


	/**
	 * This method handles the Presentation layer of the List function.
	 *
	 * @param arguments List of Command arguments.
	 * @throws PageCountOutOfBoundsException Exception thrown when the input page count exceeds the records in User table.
	 * @throws ApplicationErrorException     Exception thrown due to Persistence problems.
	 */
	public void list(List<String> arguments) throws PageCountOutOfBoundsException, ApplicationErrorException {
		setMap(listAttributesMap, null, null, null, null);
		if(arguments.size() == 3 && arguments.get(2).equals("help")) {
			FeedBackPrinter.printUserHelp("list");
		} else if(arguments.size() == 2) {
			setMap(listAttributesMap, "20", "1", "id", null);
			listHelper(listAttributesMap);
		} else if(arguments.size() == 4) {
			if(arguments.get(2).equals("-p")) {
				if((pageLength = validateNumber(arguments.get(3), "PageLength")) < 0) {
					return;
				}
				setMap(listAttributesMap, String.valueOf(pageLength), "1", "id", null);
				listHelper(listAttributesMap);
			} else if(arguments.get(2).equals("-s")) {
				searchText = arguments.get(3).trim();
				setMap(listAttributesMap, null, null, null, searchText);
				listHelper(listAttributesMap);
			} else {
				FeedBackPrinter.printInvalidExtension("user");
			}
		} else if(arguments.size() == 5) {
			pageLength = 0;
			pageNumber = 0;
			if(arguments.get(2).equals("-p")) {
				if((pageLength = validateNumber(arguments.get(3), "PageLength")) < 0) return;
				if((pageNumber = validateNumber(arguments.get(4), "PageNumber")) < 0) return;
				setMap(listAttributesMap, String.valueOf(pageLength), String.valueOf(pageNumber), "id", null);
				listHelper(listAttributesMap);
			} else if(arguments.get(2).equals("-s")) {
				attribute = arguments.get(3);
				attribute = attribute.replace(":", "");
				searchText = arguments.get(4);
				if(userAttributes.contains(attribute)) {
					setMap(listAttributesMap, "20", "1", attribute, "'" + searchText + "'");
					listHelper(listAttributesMap);
				} else {
					FeedBackPrinter.printNonSearchableAttribute("user", userAttributes);
				}
			} else {
				FeedBackPrinter.printInvalidExtension("user");
			}
		} else if(arguments.size() == 7) {
			if(arguments.get(2).equals("-s")) {
				attribute = arguments.get(3);
				attribute = attribute.replace(":", "");
				searchText = arguments.get(4);
				if(userAttributes.contains(attribute)) {
					if(arguments.get(5).equals("-p")) {
						if((pageLength = validateNumber(arguments.get(6), "PageLength")) < 0) return;
						setMap(listAttributesMap, String.valueOf(pageLength), "1", attribute, "'" + searchText + "'");
						listHelper(listAttributesMap);
					} else {
						System.out.println(">> Invalid Command Extension format !!!");
						FeedBackPrinter.printHelpMessage("user", "list");
					}
				} else {
					FeedBackPrinter.printNonSearchableAttribute("user", userAttributes);
				}
			} else {
				FeedBackPrinter.printInvalidExtension("user");
			}
		} else if(arguments.size() == 8) {
			if(arguments.get(2).equals("-s")) {
				attribute = arguments.get(3);
				attribute = attribute.replace(":", "");
				searchText = arguments.get(4);
				if(userAttributes.contains(attribute)) {
					if(arguments.get(5).equals("-p")) {
						if((pageLength = validateNumber(arguments.get(6), "PageLength")) < 0) return;
						if((pageNumber = validateNumber(arguments.get(7), "PageNumber")) < 0) return;
						setMap(listAttributesMap, String.valueOf(pageLength), String.valueOf(pageNumber), attribute, "'" + searchText + "'");
						listHelper(listAttributesMap);
					} else {
						FeedBackPrinter.printInvalidExtension("user");
					}
				} else {
					FeedBackPrinter.printNonSearchableAttribute("user", userAttributes);
				}
			} else {
				FeedBackPrinter.printInvalidExtension("user");
			}
		} else if(arguments.size() == 3) {
			FeedBackPrinter.printInvalidFormat("user");
		} else {
			FeedBackPrinter.printInvalidFormat("user");
		}
	}

	/**
	 * This method serves the List function.
	 *
	 * @param listAttributesMap - Attribute list of the List function
	 */
	private void listHelper(HashMap<String, String> listAttributesMap) {
		try {
			userList = userService.list(listAttributesMap);
			if(userList == null) {
				if(! listAttributesMap.get("Searchtext").equals("id")) {
					System.out.println(">> Given SearchText does not exist!!!");
				}
				return;
			}
			for(User user: userList) {
				System.out.println(">> id: " + user.getId() + ", usertype: " + user.getUserType() + ", username: " + user.getUserName() + ", password: " + user.getPassWord() + ", firstname: " + user.getFirstName() + ", lastname: " + user.getLastName() + ", phonenumber: " + user.getPhoneNumber());
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}


	/**
	 * This method handles the Presentation Layer of the Edit function.
	 *
	 * @param arguments List of Command arguments
	 * @param command   Command String.
	 */
	public void edit(List<String> arguments, String command) {
		final String editCommandRegex = "^id:\\s*(\\d+)(?:,\\s*([A-Za-z]+):\\s*([^,]+))?(?:,\\s*([A-Za-z]+):\\s*([^,]+))?(?:,\\s*([A-Za-z]+):\\s*([^,]+))?(?:,\\s*([A-Za-z]+):\\s*([^,]+))?(?:,\\s*([A-Za-z]+):\\s*([^,]+))?(?:,\\s*([A-Za-z]+):\\s*([^,]+))?$";
		if(arguments.size() == 3 && arguments.get(2).equals("help")) {
			FeedBackPrinter.printUserHelp("edit");
		} else if(arguments.size() == 2) {
			System.out.print("> ");
			String parameters = scanner.nextLine();
			if(! parameters.matches(editCommandRegex)) {
				System.out.println(">> Invalid command Format!\n>> Try \"user edit help for proper syntax!");
				return;
			}
			List<String> userAttributes = List.of(parameters.split("[,:]"));
			editHelper(userAttributes);
		} else if(arguments.size() > 16) {
			System.out.println(">> Too many Arguments for command \"product edit\"");
		} else if(arguments.size() < 6) {
			System.out.println(">> Insufficient arguments for command \"product edit\"");
		} else if(! arguments.get(2).contains("id")) {
			System.out.println(">> Id is a Mandatory argument for every Edit operation");
			System.out.println(">> For every Edit operation the first argument must be user's ID");
			FeedBackPrinter.printHelpMessage("user", "edit");
		} else {
			if(! command.substring(10).matches(editCommandRegex)) {
				System.out.println(">> Invalid command Format!\n>> Try \"user edit help for proper syntax!");
				return;
			}
			editHelper(arguments.subList(2, arguments.size()));
		}
	}

	/**
	 * This method serves the Edit function
	 *
	 * @param editAttributes Attributes of user to be edited.
	 */
	private void editHelper(List<String> editAttributes) {
		User user = new User();
		try {
			id = Integer.parseInt(editAttributes.get(1).trim());
		} catch(Exception e) {
			System.out.println(">> Id must be a Number!");
			FeedBackPrinter.printHelpMessage("user", "edit");
		}
		user.setId(id);
		for(int index = 2 ; index < editAttributes.size() ; index = index + 2) {
			switch(editAttributes.get(index).trim()) {
				case "username" -> user.setUserName(editAttributes.get(index + 1).trim());
				case "usertype" -> user.setUserType(editAttributes.get(index + 1).trim());
				case "password" -> user.setPassWord(editAttributes.get(index + 1).trim());
				case "firstname" -> user.setFirstName(editAttributes.get(index + 1).trim());
				case "lastname" -> user.setLastName(editAttributes.get(index + 1).trim());
				case "phonenumber" -> {
					try {
						phoneNumber = Long.parseLong(editAttributes.get(index + 1).trim());
					} catch(NumberFormatException e) {
						System.out.println(">> Phonenumber must be numeric!!");
						System.out.println(">> Try \"user edit help\" for proper syntax");
						return;
					}
					user.setPhoneNumber(phoneNumber);
				}
				default -> {
					System.out.println(">> Invalid attribute given!!! : " + editAttributes.get(index));
					System.out.println(">> Try \"user edit help\" for proper syntax");
					return;
				}
			}
		}
		User editedUser;
		try {
			editedUser = userService.edit(user);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return;
		}
		if(editedUser != null) {
			System.out.println(">> User Edited Successfully");
			System.out.println(editedUser);
		} else {
			System.out.println(">> User edit failed!!!");
			System.out.println(">>Please check the Id you have entered!!!");
		}
	}

	/**
	 * This method handles the presentation layer of the Delete function.
	 *
	 * @param arguments List of Command arguments.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problem.
	 */
	public void delete(List<String> arguments) throws ApplicationErrorException {
		String nameregex = "^[a-zA-Z0-9]{3,30}$";
		if(arguments.size() == 3) {
			if(arguments.get(2).equals("help")) {
				FeedBackPrinter.printUserHelp("delete");
			} else if(arguments.get(2).matches(nameregex)) {
				System.out.println(">> Are you sure want to delete the User y/n ? : ");
				String prompt = scanner.nextLine();
				if(prompt.equals("y")) {
					if(userService.delete(arguments.get(2)) == 1) {
						System.out.println("User Deleted Successfull!!!");
					} else if(userService.delete(arguments.get(2)) == - 1) {
						System.out.println(">> User Deletion Failed!!!");
						System.out.println(">> Please check the username you have entered!!!");
						FeedBackPrinter.printHelpMessage("user", "delete");
					}
				} else if(prompt.equals("n")) {
					System.out.println(">> Delete operation cancelled");
				} else {
					System.out.println(">> Invalid delete prompt!!! Please select between y/n");
				}
			} else {
				System.out.println(">> Invalid format for username!!!");
				FeedBackPrinter.printHelpMessage("user", "delete");
			}
		}
	}

	private int validateNumber(String number, String name) {
		int result;
		try {
			result = Integer.parseInt(number);
		} catch(Exception e) {
			System.out.println("Invalid " + name + "! Must be a number");
			return - 1;
		}
		return result;
	}

	private void setMap(HashMap<String, String> listAttributesMap, String PageLength, String PageNumber, String Attribute, String SearchText) {
		listAttributesMap.put("Pagelength", PageLength);
		listAttributesMap.put("Pagenumber", PageNumber);
		listAttributesMap.put("Attribute", Attribute);
		listAttributesMap.put("Searchtext", SearchText);
	}
}
