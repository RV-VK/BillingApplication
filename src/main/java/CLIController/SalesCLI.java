package CLIController;

import DAO.ApplicationErrorException;
import Entity.Product;
import Entity.Sales;
import Entity.SalesItem;
import Service.InvalidTemplateException;
import Service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SalesCLI {
	private final List<SalesItem> salesItemList = new ArrayList<>();
	private final HashMap<String, String> listAttributesMap = new HashMap<>();
	private final List<String> saleAttributes = Arrays.asList("id", "date");
	private final Scanner scanner = new Scanner(System.in);
	@Autowired
	private SalesService salesService;
	private double grandTotal;

	private Sales createdSale;


	/**
	 * This method handles the Presentation layer of the Create function.
	 *
	 * @param command Command String.
	 */
	public void create(String command) {
		String productcodeRegex = "^[a-zA-Z0-9]{2,6}$";
		String[] commandEntities = command.split(",\\s*(?=\\[)");
		if(commandEntities.length < 1) {
			System.out.println(">> Insufficient arguments to start a Sale!!!");
			System.out.println(">> Try \"sales help\" for proper Syntax!!!");
		} else {
			String[] commandArguments = commandEntities[0].split("\\s+");
			String salesDate = commandArguments[1].trim().replace(",", "");
			salesItemList.clear();
			for(int i = 1 ; i < commandEntities.length ; i++) {
				String item = commandEntities[i].replaceAll("[\\[\\]]", "");
				String[] itemVariables = item.split(",");
				if(itemVariables.length < 2) {
					System.out.println(">> Please provide sufficient details for product " + i);
					System.out.println(">> Try \"sales help\" for proper syntax");
				}
				if(itemVariables.length > 2) {
					System.out.println(">> Improper format of product details given!!!");
					System.out.println(">> Try \"sales help\" for proper syntax");
					return;
				}
				String code = itemVariables[0].trim();
				if(! code.matches(productcodeRegex)) {
					System.out.println(">> Invalid format for product code in product :" + i);
					System.out.println(">> Try \"sales help\" for proper syntax!!");
				}
				float quantity;
				try {
					quantity = Float.parseFloat(itemVariables[1].trim());
				} catch(Exception e) {
					System.out.println(">> Quantity must be a number !! Error in Product " + i);
					System.out.println(">> Try \"sales help\" for proper syntax");
					return;
				}
				salesItemList.add(new SalesItem(new Product(code), quantity));
				grandTotal = 0;
			}
			Sales sales = new Sales(salesDate, salesItemList, grandTotal);
			try {
				createdSale = salesService.create(sales);
			} catch(Exception e) {
				System.out.println(e.getMessage());
				return;
			}
			printSalesBill();
		}
	}

	/**
	 * This method handles the presentation layer of the count function.
	 *
	 * @param arguments Command arguments.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	public void count(List<String> arguments) throws ApplicationErrorException, InvalidTemplateException {
		int salesCount;
		if(arguments.size() == 3) {
			if(arguments.get(2).equals("help")) {
				FeedBackPrinter.printSalesHelp("count");
			} else {
				System.out.println(">> Invalid command given!!!");
				System.out.println(">> Try \"sales count  help\" for proper syntax!!");
			}
			return;
		}
		if(arguments.size() == 2) {
			salesCount = salesService.count("id", null);
			System.out.println(">> SalesCount :" + salesCount);
			return;
		}
		if(arguments.size() == 4) {
			if(arguments.get(2).equals("-d")) {
				String parameter = arguments.get(3);
				try {
					salesCount = salesService.count("date", parameter);
				} catch(Exception e) {
					System.out.println(e.getMessage());
					return;
				}
				if(salesCount > 0) System.out.println(">> SalesCount " + salesCount);
				else {
					System.out.println(">> Given Date not found!!!");
					System.out.println(">> Please Try with an existing searchtext");
				}
			} else {
				System.out.println(">> Invalid extension Given");
				System.out.println(">> Try \"sales count help\" for proper syntax!!");
			}
		} else {
			System.out.println(">> Invalid Command Format!!!");
			System.out.println(">> Try \"sales count help\" for proper syntax");
		}
	}


	/**
	 * This method handles the presentation layer of the List function.
	 *
	 * @param arguments Command arguments.
	 */
	public void list(List<String> arguments) {
		setMap(listAttributesMap, null, null, null, null);
		if(arguments.size() == 3) {
			if(arguments.get(2).equals("help")) {
				FeedBackPrinter.printSalesHelp("list");
				return;
			}
		}
		int pageLength;
		int pageNumber;
		String attribute;
		String searchText;
		if(arguments.size() == 2) {
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
				FeedBackPrinter.printInvalidExtension("sales");
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
				if(saleAttributes.contains(attribute)) {
					setMap(listAttributesMap, "20", "1", attribute, searchText);
					listHelper(listAttributesMap);
				} else {
					FeedBackPrinter.printNonSearchableAttribute("sales", saleAttributes);
				}
			} else {
				FeedBackPrinter.printInvalidExtension("sales");
			}
		} else if(arguments.size() == 7) {
			if(arguments.get(2).equals("-s")) {
				attribute = arguments.get(3);
				attribute = attribute.replace(":", "");
				searchText = arguments.get(4);
				if(saleAttributes.contains(attribute)) {
					if(arguments.get(5).equals("-p")) {
						if((pageLength = validateNumber(arguments.get(6), "PageLength")) < 0) return;
						setMap(listAttributesMap, String.valueOf(pageLength), "1", attribute, searchText);
						listHelper(listAttributesMap);
					} else {
						System.out.println(">> Invalid Command Extension format !!!");
						System.out.println("Try \"sales list help\" for proper syntax");
					}
				} else {
					FeedBackPrinter.printNonSearchableAttribute("sales", saleAttributes);
				}
			} else {
				FeedBackPrinter.printInvalidExtension("sales");
			}
		} else if(arguments.size() == 8) {
			if(arguments.get(2).equals("-s")) {
				attribute = arguments.get(3);
				attribute = attribute.replace(":", "");
				searchText = arguments.get(4);
				if(saleAttributes.contains(attribute)) {
					if(arguments.get(5).equals("-p")) {
						if((pageLength = validateNumber(arguments.get(6), "PageLength")) < 0) return;
						if((pageNumber = validateNumber(arguments.get(7), "PageNumber")) < 0) return;
						setMap(listAttributesMap, String.valueOf(pageLength), String.valueOf(pageNumber), attribute, searchText);
						listHelper(listAttributesMap);
					} else {
						FeedBackPrinter.printInvalidExtension("sales");
					}
				} else {
					FeedBackPrinter.printNonSearchableAttribute("sales", saleAttributes);
				}
			} else {
				FeedBackPrinter.printInvalidExtension("sales");
			}
		} else {
			FeedBackPrinter.printInvalidFormat("sales");
		}
	}


	/**
	 * This method serves the List function.
	 *
	 * @param listAttributesMap Attribute List for List function.
	 */
	private void listHelper(HashMap<String, String> listAttributesMap) {
		try {
			List<Sales> salesList = salesService.list(listAttributesMap);
			if(salesList.size() == 0) {
				if(listAttributesMap.get("Searchtext") != null) {
					System.out.println(">> Given SearchText does not exist!!!");
				}
				return;
			}
			for(Sales sales: salesList) {
				System.out.print("id: " + sales.getId() + ", date: " + sales.getDate() + ", ");
				System.out.print("[");
				for(SalesItem salesItem: sales.getSalesItemList()) {
					System.out.print("[name: " + salesItem.getProduct().getName() + ", quantity: " + salesItem.getQuantity() + ", price: " + salesItem.getUnitSalesPrice() + "], ");
				}

				System.out.print(sales.getGrandTotal());
				System.out.print("] ");
				System.out.println();
			}

		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * This method handles the presentation layer of the Delete function.
	 *
	 * @param arguments Command arguments.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	public void delete(List<String> arguments) throws ApplicationErrorException {
		String numberRegex = "^[0-9]{1,10}$";
		if(arguments.size() == 3) {
			if(arguments.get(2).equals("help")) {
				FeedBackPrinter.printSalesHelp("delete");
			} else if(arguments.get(2).matches(numberRegex)) {
				System.out.println(">> Are you sure you want to delete the Sales Entry y/n : ");
				String prompt = scanner.nextLine();
				if(prompt.equals("y")) {
					int resultCode = salesService.delete(arguments.get(2));
					if(resultCode == 1) {
						System.out.println(">> Sales Entry Deleted Successfully!!!");
					} else if(resultCode == 0) {
						System.out.println(">> Sales Entry Deletion Failed!!");
						System.out.println(">> Please check the id you have entered!!!");
						System.out.println(">> Try \"sales delete help\" for proper syntax");
					} else {
						System.out.println(">> Id cannot be null!!");
					}
				} else if(prompt.equals("n")) {
					System.out.println(">> Delete operation cancelled!!!");
				} else {
					System.out.println(">> Invalid delete prompt !! Please select between y/n");
				}
			} else {
				System.out.println(">> Invalid format for id");
				System.out.println(">> Try \"sales delete help\" for proper syntax");
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

	private void printSalesBill() {
		System.out.println("**********************************************************************************");
		System.out.println("\t\tSALES BILL " + createdSale.getId());
		System.out.println("**********************************************************************************");
		System.out.println("SNO\t\tPRODUCT NAME\t\t\tQTY\t\tPRICE\t\tTOTAL");
		System.out.println("----------------------------------------------------------------------------------");
		for(int i = 0 ; i < createdSale.getSalesItemList().size() ; i++) {
			System.out.printf("%d\t\t%-15s\t\t\t%.1f\t\t%.2f\t\t%.2f%n", i + 1, createdSale.getSalesItemList().get(i).getProduct().getName(), createdSale.getSalesItemList().get(i).getQuantity(), createdSale.getSalesItemList().get(i).getUnitSalesPrice(), (createdSale.getSalesItemList().get(i).getQuantity() * createdSale.getSalesItemList().get(i).getUnitSalesPrice()));
		}
		System.out.println("----------------------------------------------------------------------------------");
		System.out.printf("GRAND TOTAL\t\t\t\t\t\t\t\t\t\t\t%.2f%n", createdSale.getGrandTotal());
		System.out.println("----------------------------------------------------------------------------------");
	}
}
