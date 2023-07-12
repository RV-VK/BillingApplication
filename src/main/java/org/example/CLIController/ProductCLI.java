package org.example.CLIController;

import org.example.DAO.ApplicationErrorException;
import org.example.DAO.PageCountOutOfBoundsException;
import org.example.Entity.Product;
import org.example.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

@Component
public class ProductCLI {
	private final List<String> productAttributes = Arrays.asList("id", "code", "name", "unitcode", "type", "price", "stock", "costprice");
	private final HashMap<String, String> listAttributesMap = new HashMap<>();
	private final Scanner scanner = new Scanner(System.in);
	@Autowired
	private ProductService productService;
	private int id;
	private String code;
	private String name;
	private String unitCode;
	private String type;
	private double price;
	private float stock;

	/**
	 * This method handles the presentation layer for the create function.
	 *
	 * @param arguments - List of Command arguments.
	 */
	public void create(List<String> arguments) {
		if(arguments.size() == 3 && arguments.get(2).equals("help")) {
			FeedBackPrinter.printProductHelp("create");
			return;
		} else if(arguments.size() == 2) {
			System.out.print("> ");
			String parameters = scanner.nextLine();
			List<String> productAttributes = List.of(parameters.split(","));
			createHelper(productAttributes);
			return;
		}
		createHelper(arguments.subList(2, arguments.size()));
	}

	/**
	 * This method serves the create function
	 *
	 * @param productAttributes Attributes of Product for Creation.
	 */
	private void createHelper(List<String> productAttributes) {
		if(productAttributes.size() < 5) {
			System.out.println(">>Insufficient Arguments for command \"product create\"");
			FeedBackPrinter.printHelpMessage("product", "create");
			return;
		} else if(productAttributes.size() > 6) {
			System.out.println(">>Too many arguments for command \"product create \"");
			FeedBackPrinter.printHelpMessage("product", "create");
			return;
		}
		code = productAttributes.get(0).trim();
		name = productAttributes.get(1).trim();
		unitCode = productAttributes.get(2).trim();
		type = productAttributes.get(3).trim();
		try {
			price = Double.parseDouble(productAttributes.get(4).trim());
		} catch(Exception e) {
			System.out.println(">>Invalid format for 4th Argument \"price\"");
			System.out.println();
			return;
		}
		if(productAttributes.size() == 6) {
			try {
				stock = Float.parseFloat(productAttributes.get(5).trim());
			} catch(Exception e) {
				System.out.println(stock);
				System.out.println(">>Invalid format for 5th argument \"stock\"");
				FeedBackPrinter.printHelpMessage("product", "create");
				return;
			}
		}
		Product product = new Product(code, name, unitCode, type, stock, price);
		Product createdProduct;
		try {
			createdProduct = productService.create(product);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return;
		}
		if(createdProduct != null) {
			System.out.println(">> Product Creation Successfull!!");
			System.out.println(createdProduct);
		}
	}

	/**
	 * This method handles the Presentation layer of the List function.
	 *
	 * @param arguments - List of Command Arguments.
	 * @throws PageCountOutOfBoundsException Exception thrown when the input page count exceeds the records in Product table.
	 * @throws ApplicationErrorException     Exception thrown due to Persistence problems.
	 */
	public void list(List<String> arguments) throws PageCountOutOfBoundsException, ApplicationErrorException {
		setMap(listAttributesMap, null, null, null, null);
		String searchText;
		String attribute;
		int pageNumber;
		int pageLength;
		if(arguments.size() == 3 && arguments.get(2).equals("help")) {
			FeedBackPrinter.printProductHelp("list");
		} else if(arguments.size() == 2) {
			setMap(listAttributesMap, "20", "1", "id", null);
			listHelper(listAttributesMap);
		} else if(arguments.size() == 4) {
			if(arguments.get(2).equals("-p")) {
				if((pageLength = validateNumber(arguments.get(3), "PageLength")) < 0) return;
				setMap(listAttributesMap, String.valueOf(pageLength), "1", "id", null);
				listHelper(listAttributesMap);
			} else if(arguments.get(2).equals("-s")) {
				searchText = arguments.get(3).trim();
				setMap(listAttributesMap, null, null, null, searchText);
				listHelper(listAttributesMap);
			} else {
				FeedBackPrinter.printInvalidExtension("product");
			}
		} else if(arguments.size() == 5) {
			if(arguments.get(2).equals("-p")) {
				if((pageLength = validateNumber(arguments.get(3), "PageLength")) < 0) return;
				if((pageNumber = validateNumber(arguments.get(4), "PageNumber")) < 0) return;
				setMap(listAttributesMap, String.valueOf(pageLength), String.valueOf(pageNumber), "id", null);
				listHelper(listAttributesMap);
			} else if(arguments.get(2).equals("-s")) {
				attribute = arguments.get(3);
				attribute = attribute.replace(":", "");
				searchText = arguments.get(4);
				if(productAttributes.contains(attribute)) {
					setMap(listAttributesMap, "20", "1", attribute, searchText);
					listHelper(listAttributesMap);
				} else {
					FeedBackPrinter.printNonSearchableAttribute("product", productAttributes);
				}
			} else {
				FeedBackPrinter.printInvalidExtension("product");
			}
		} else if(arguments.size() == 7) {
			if(arguments.get(2).equals("-s")) {
				attribute = arguments.get(3);
				attribute = attribute.replace(":", "");
				searchText = arguments.get(4);
				if(productAttributes.contains(attribute)) {
					if(arguments.get(5).equals("-p")) {
						if((pageLength = validateNumber(arguments.get(6), "PageLength")) < 0) return;
						setMap(listAttributesMap, String.valueOf(pageLength), "1", attribute, searchText);
						listHelper(listAttributesMap);
					} else {
						System.out.println(">> Invalid Command Extension format !!!");
						FeedBackPrinter.printHelpMessage("product", "list");
					}
				} else {
					FeedBackPrinter.printNonSearchableAttribute("product", productAttributes);
				}
			} else {
				FeedBackPrinter.printInvalidExtension("product");
			}
		} else if(arguments.size() == 8) {
			if(arguments.get(2).equals("-s")) {
				attribute = arguments.get(3);
				attribute = attribute.replace(":", "");
				searchText = arguments.get(4);
				if(productAttributes.contains(attribute)) {
					if(arguments.get(5).equals("-p")) {
						if((pageLength = validateNumber(arguments.get(6), "PageLength")) < 0) return;
						if((pageNumber = validateNumber(arguments.get(7), "PageNumber")) < 0) return;
						setMap(listAttributesMap, String.valueOf(pageLength), String.valueOf(pageNumber), attribute, searchText);
						listHelper(listAttributesMap);
					} else {
						FeedBackPrinter.printInvalidExtension("product");
					}
				} else {
					FeedBackPrinter.printNonSearchableAttribute("product", productAttributes);
				}
			} else {
				FeedBackPrinter.printInvalidExtension("product");
			}
		} else if(arguments.size() == 3) {
			FeedBackPrinter.printInvalidFormat("product");
		} else {
			FeedBackPrinter.printInvalidFormat("product");
		}
	}

	/**
	 * This method serves the List function.
	 *
	 * @param listAttributesMap Attribute list of the List function
	 */
	private void listHelper(HashMap<String, String> listAttributesMap) {
		try {
			List<Product> resultList = productService.list(listAttributesMap);
			if(resultList.size() == 0) {
				if(listAttributesMap.get("Searchtext") != null) {
					System.out.println(">> Given SearchText does not exist!!!");
				}
				return;
			}
			for(Product resultProduct: resultList) {
				System.out.println(">> id: " + resultProduct.getId() + ", code: " + resultProduct.getCode() + ", name: " + resultProduct.getName() + ", type: " + resultProduct.getType() + ", unitcode: " + resultProduct.getunitcode() + ", stock: " + resultProduct.getAvailableQuantity() + ", price: " + resultProduct.getPrice());
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * This method handles the Presentation Layer of the Count function.
	 *
	 * @throws ApplicationErrorException Exception thrown due to Persistence problem.
	 */
	public void count(List<String> arguments) throws ApplicationErrorException {
		if(arguments.size() > 2) {
			System.out.println(">> Invalid Command!! Try \"help\"");
			return;
		}
		int productCount = productService.count("id", null);
		System.out.println(">> ProductCount " + productCount);
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
			FeedBackPrinter.printProductHelp("edit");
		} else if(arguments.size() == 2) {
			System.out.print("> ");
			String parameters = scanner.nextLine();
			if(! parameters.matches(editCommandRegex)) {
				System.out.println(">> Invalid command Format!\n>> Try \"product edit help for proper syntax!");
				return;
			}
			List<String> productAttributes = List.of(parameters.split("[,:]"));
			editHelper(productAttributes);
		} else if(arguments.size() < 14) {
			System.out.println(">>Insufficient Arguments for command \"product edit\"");
		} else if(arguments.size() > 14) {
			System.out.println(">>Too many Arguments for command \"product edit\"");
		} else if(! arguments.get(2).contains("id")) {
			System.out.println(">> Id is a Mandatory argument for every Edit operation");
			System.out.println(">> For every Edit operation the first argument must be product's ID");
			FeedBackPrinter.printHelpMessage("product", "edit");
		} else {
			if(! command.substring(13).matches(editCommandRegex)) {
				System.out.println(">> Invalid command Format!\n>> Try \"product edit help for proper syntax!");
				return;
			}
			editHelper(arguments.subList(2, arguments.size()));
		}
	}

	/**
	 * This method serves the Edit function
	 *
	 * @param editAttributes Attributes of product to be edited.
	 */
	private void editHelper(List<String> editAttributes) {
		if(editAttributes.size() < 12)
			System.out.println(">> Insufficient arguments for edit!!\n Try \"product edit help\" for proper syntax!");
		Product product = new Product();
		try {
			id = Integer.parseInt(editAttributes.get(1).trim());
		} catch(Exception e) {
			System.out.println(">> Id must be a Number!");
			FeedBackPrinter.printHelpMessage("product", "edit");
		}
		product.setId(id);
		if(product.getId() == 0) {
			System.out.println(">> Id should not be null");
			FeedBackPrinter.printHelpMessage("product", "edit");
			return;
		}
		for(int index = 2 ; index < editAttributes.size() ; index = index + 2) {
			switch(editAttributes.get(index).trim()) {
				case "name" -> product.setName(editAttributes.get(index + 1).trim());
				case "code" -> product.setCode(editAttributes.get(index + 1).trim());
				case "unitcode" -> product.setunitcode(editAttributes.get(index + 1).trim());
				case "type" -> product.setType(editAttributes.get(index + 1).trim());
				case "stock" -> {
					try {
						stock = Float.parseFloat(editAttributes.get(index + 1));
					} catch(Exception e) {
						System.out.println("Stock must be numeric!!");
						return;
					}
					product.setAvailableQuantity(stock);
				}
				case "price" -> {
					try {
						price = Double.parseDouble(editAttributes.get(index + 1));
					} catch(Exception e) {
						System.out.println(">> Price attribute must be a number!!");
						return;
					}
					product.setPrice(price);
				}
				default -> {
					System.out.println(">> Invalid attribute given!!! : " + editAttributes.get(index));
					System.out.println(">> Try \"product edit help\" for proper syntax");
					return;
				}
			}
		}
		Product editedProduct;
		try {
			editedProduct = productService.edit(product);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return;
		}
		if(editedProduct != null) {
			System.out.println(">> Product Edited Successfully");
			System.out.println(editedProduct);
		} else {
			System.out.println(">> Product edit failed!!!");
			System.out.println(">>Please check the Id you have entered!!!");
		}
	}

	/**
	 * This method handles the Presentation Layer of the Delete function.
	 *
	 * @param arguments - List of Command arguments.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problem.
	 */
	public void delete(List<String> arguments) throws ApplicationErrorException {
		String productcodeRegex = "^[a-zA-Z0-9]{2,6}$";
		if(arguments.size() == 3) {
			String NUMBER_REGEX = "^\\d+(\\.\\d+)?$";
			if(arguments.get(2).equals("help")) {
				FeedBackPrinter.printProductHelp("delete");
			} else if(arguments.get(2).matches(NUMBER_REGEX)) {
				deleteHelper(arguments.get(2));
			} else {
				System.out.println(">> Invalid format for id!!!");
				FeedBackPrinter.printHelpMessage("product", "delete");
			}
		} else if(arguments.size() == 4 && arguments.get(2).equals("-c")) {
			if(arguments.get(3).matches(productcodeRegex)) {
				deleteHelper(arguments.get(3));
			} else {
				System.out.println(">> Invalid format for product Code!!!");
				FeedBackPrinter.printHelpMessage("product", "delete");
			}
		} else {
			System.out.println("Invalid command format");
			FeedBackPrinter.printHelpMessage("product", "delete");
		}
	}

	/**
	 * This method serves the Delete function
	 *
	 * @param parameter Input parameter to perform delete.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problem.
	 */
	private void deleteHelper(String parameter) throws ApplicationErrorException {
		System.out.println(">> Are you sure want to delete the product y/n ? : ");
		String prompt = scanner.nextLine();
		if(prompt.equals("y")) {
			if(productService.delete(parameter) == 1) {
				System.out.println("Product Deleted Successfully!!!");
			} else if(productService.delete(parameter) == 0) {
				System.out.println(">> Product Deletion Failed!!!");
				System.out.println(">> Please check the Id (or) Code you have entered whether it exists or have any stock left!!");
				System.out.println(">> Try \"product delete help\" for proper syntax");
			} else {
				System.out.println(">> Delete Parameter cant be null!!");
			}
		} else if(prompt.equals("n")) {
			System.out.println(">> Delete operation cancelled");
		} else {
			System.out.println(">> Invalid delete prompt!!! Please select between y/n");
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
