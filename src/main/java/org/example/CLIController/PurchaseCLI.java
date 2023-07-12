package org.example.CLIController;

import org.example.DAO.ApplicationErrorException;
import org.example.DAO.PageCountOutOfBoundsException;
import org.example.Entity.Product;
import org.example.Entity.Purchase;
import org.example.Entity.PurchaseItem;
import org.example.Service.InvalidTemplateException;
import org.example.Service.PurchaseService;
import org.example.Service.PurchaseServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PurchaseCLI {
	private final List<String> purchaseAttributes = Arrays.asList("id", "date", "invoice");
	@Autowired
	private PurchaseService purchaseService;
	private final Scanner scanner = new Scanner(System.in);
	private final HashMap<String, String> listAttributesMap = new HashMap<>();
	private String purchaseDate;
	private int invoice;
	private double grandTotal;
	private  List<PurchaseItem> purchaseItemList = new ArrayList<>();
	private String code;
	private float quantity;
	private double costPrice;
	private int pageLength;
	private int pageNumber;
	private String attribute;
	private String searchText;

	private Purchase createdPurchase;

	/**
	 * This method handles the presentation Layer of the Create function.
	 *
	 * @param command Command String.
	 */
	public void create(String command) {
		String productCodeRegex = "^[a-zA-Z0-9]{2,6}$";
		String[] commandEntities = command.split(",\\s*(?=\\[)");
		if(commandEntities.length < 1) {
			System.out.println(">> Insufficient arguments to start a purchase!!!");
			System.out.println(">> Try \"purchase help\" for proper syntax");
		} else {
			String[] commandArguments = commandEntities[0].split("\\s+");
			purchaseDate = commandArguments[1].trim().replace(",", "");
			try {
				invoice = Integer.parseInt(commandArguments[2].trim());
			} catch(Exception e) {
				System.out.println(">> Invoice must be a number!!!");
				System.out.println(">> Try \"purchase help\" for proper syntax!!");
				return;
			}
			purchaseItemList.clear();
			for(int i = 1 ; i < commandEntities.length ; i++) {
				String item = commandEntities[i].replaceAll("[\\[\\]]", "");
				String[] itemVariables = item.split(",");
				if(itemVariables.length < 3) {
					System.out.println(">> Please provide sufficient details for Product " + i);
					System.out.println(">> Try \"purchase help\" for proper syntax");
					return;
				}
				if(itemVariables.length > 3) {
					System.out.println(">> Improper format of product details given");
					System.out.println(">> Try \"purchase help\" for proper syntax");
					return;
				}
				code = itemVariables[0].trim();
				if(! code.matches(productCodeRegex)) {
					System.out.println("Invalid format for product code!!");
					System.out.println("Try \"purchase help\" for proper syntax");
				}
				try {
					quantity = Float.parseFloat(itemVariables[1].trim());
					costPrice = Double.parseDouble(itemVariables[2].trim());
				} catch(Exception e) {
					System.out.println(">> Quantity (or) Costprice must be a number!! Error in Product " + i);
					System.out.println(">> Try \"purchase help\" for proper syntax!!!");
					return;
				}
				purchaseItemList.add(new PurchaseItem(new Product(code), quantity, costPrice));
				grandTotal += costPrice * quantity;
			}
			Purchase purchase = new Purchase(purchaseDate, invoice, purchaseItemList, grandTotal);
			try {
				createdPurchase = purchaseService.create(purchase);
			} catch(Exception e) {
				System.out.println(e.getMessage());
				return;
			}
			printPurchaseBill();
		}
	}

	/**
	 * This method handles the presentation layer of the Count function.
	 *
	 * @param arguments Command arguments
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	public void count(List<String> arguments) throws ApplicationErrorException, InvalidTemplateException {
		int purchaseCount;
		if(arguments.size() == 3) {
			if(arguments.get(2).equals("help")) {
				FeedBackPrinter.printPurchaseHelp("count");
			} else {
				System.out.println(">> Invalid command given!!!");
				System.out.println(">> Try \"purchase count help\" for proper syntax!!");
			}
			return;
		}
		if(arguments.size() == 2) {
			purchaseCount = purchaseService.count("id", null);
			System.out.println(">> PurchaseCount " + purchaseCount);
			return;
		}
		if(arguments.size() == 4) {
			if(arguments.get(2).equals("-d")) {
				String parameter = arguments.get(3);
				try {
					purchaseCount = purchaseService.count("date", parameter);
				} catch(Exception e) {
					System.out.println(e.getMessage());
					return;
				}
				if(purchaseCount > 0) System.out.println(">> PurchaseCount " + purchaseCount);
				else {
					System.out.println(">> Given Date not found!!!");
					System.out.println(">>Please Try with an existing searchtext");
				}
			} else {
				System.out.println(">> Invalid extension Given");
				System.out.println(">> Try \"purchase count help\" for proper syntax!!");
			}
		} else {
			System.out.println(">> Invalid Command Format!!!");
			System.out.println(">> Try \"purchase count help\" for proper syntax");
		}
	}

	/**
	 * This method handles the presentation layer of the List function.
	 *
	 * @param arguments Command arguments
	 * @throws PageCountOutOfBoundsException Exception thrown when the input page count exceeds the records in Purchase table.
	 * @throws ApplicationErrorException     Exception thrown due to Persistence problems.
	 */
	public void list(List<String> arguments) throws PageCountOutOfBoundsException, ApplicationErrorException {
		setMap(listAttributesMap, null, null, null, null);
		if(arguments.size() == 3) if(arguments.get(2).equals("help")) {
			FeedBackPrinter.printPurchaseHelp("list");
			return;
		}
		if(arguments.size() == 2) {
			setMap(listAttributesMap, "20", "1", "id", null);
			listHelper(listAttributesMap);
		} else if(arguments.size() == 4) {
			pageLength = 0;
			if(arguments.get(2).equals("-p")) {
				if((pageLength = validateNumber(arguments.get(3), "PageLength")) < 0) return;
				setMap(listAttributesMap, String.valueOf(pageLength), "1", "id", null);
				listHelper(listAttributesMap);
			} else if(arguments.get(2).equals("-s")) {
				searchText = arguments.get(3).trim();
				setMap(listAttributesMap, null, null, null, searchText);
				listHelper(listAttributesMap);
			} else {
				FeedBackPrinter.printInvalidExtension("purchase");
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
				if(purchaseAttributes.contains(attribute)) {
					setMap(listAttributesMap, "20", "1", attribute, searchText);
					listHelper(listAttributesMap);
				} else {
					FeedBackPrinter.printNonSearchableAttribute("purchase", purchaseAttributes);
				}
			} else {
				FeedBackPrinter.printInvalidExtension("purchase");
			}
		} else if(arguments.size() == 7) {
			if(arguments.get(2).equals("-s")) {
				attribute = arguments.get(3);
				attribute = attribute.replace(":", "");
				searchText = arguments.get(4);
				if(purchaseAttributes.contains(attribute)) {
					if(arguments.get(5).equals("-p")) {
						if((pageLength = validateNumber(arguments.get(6), "PageLength")) < 0) return;
						setMap(listAttributesMap, String.valueOf(pageLength), "1", attribute, searchText);
						listHelper(listAttributesMap);
					} else {
						System.out.println(">> Invalid Command Extension format !!!");
						System.out.println("Try \"purchase list help\" for proper syntax");
					}
				} else {
					FeedBackPrinter.printNonSearchableAttribute("purchase", purchaseAttributes);
				}
			} else {
				FeedBackPrinter.printInvalidExtension("purchase");
			}
		} else if(arguments.size() == 8) {
			if(arguments.get(2).equals("-s")) {
				attribute = arguments.get(3);
				attribute = attribute.replace(":", "");
				searchText = arguments.get(4);
				if(purchaseAttributes.contains(attribute)) {
					if(arguments.get(5).equals("-p")) {
						if((pageLength = validateNumber(arguments.get(6), "PageLength")) < 0) return;
						if((pageNumber = validateNumber(arguments.get(7), "PageNumber")) < 0) return;
						setMap(listAttributesMap, String.valueOf(pageLength), String.valueOf(pageNumber), attribute, searchText);
						listHelper(listAttributesMap);
					} else {
						FeedBackPrinter.printInvalidExtension("purchase");
					}
				} else {
					FeedBackPrinter.printNonSearchableAttribute("purchase", purchaseAttributes);
				}
			} else {
				FeedBackPrinter.printInvalidExtension("purchase");
			}

		} else {
			FeedBackPrinter.printInvalidFormat("purchase");
		}
	}

	/**
	 * This method serves the List function.
	 *
	 * @param listAttributesMap Attribute List of the list function.
	 */
	private void listHelper(HashMap<String, String> listAttributesMap) {
		try {
			List<Purchase> purchaseList = purchaseService.list(listAttributesMap);
			if(purchaseList.size() == 0) {
				if(listAttributesMap.get("Searchtext") != null) {
					System.out.println(">>Given SearchText does not exist!!!");
				}
				return;
			}
			for(Purchase purchase: purchaseList) {
				System.out.print("id: " + purchase.getId() + ", date: " + purchase.getDate() + ", invoice: " + purchase.getInvoice() + ", ");
				System.out.print("[");
				for(PurchaseItem purchaseItem: purchase.getPurchaseItemList()) {
					System.out.print("[name: " + purchaseItem.getProduct().getName() + ", quantity: " + purchaseItem.getQuantity() + ", price: " + purchaseItem.getUnitPurchasePrice() + "], ");
				}
				System.out.print(purchase.getGrandTotal() + " ");
				System.out.print("]");
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
		PurchaseService purchaseDeleteService = new PurchaseServiceImplementation();
		String numberRegex = "^[0-9]{1,10}$";
		if(arguments.size() == 3) {
			if(arguments.get(2).equals("help")) {
				FeedBackPrinter.printPurchaseHelp("delete");
			} else if(arguments.get(2).matches(numberRegex)) {
				System.out.println(">> Are you sure want to delete the Purchase Entry y/n ? : ");
				String prompt = scanner.nextLine();
				if(prompt.equals("y")) {
					int resultCode = purchaseDeleteService.delete(arguments.get(2));
					if(resultCode == 1) {
						System.out.println(">> Purchase Deleted Successfully!!");
					} else if(resultCode == 0) {
						System.out.println(">> Purchase Deletion Failed!!!");
						System.out.println(">> Please check the invoice you have entered!!!");
						System.out.println(">> Try \"purchase delete help\" for proper syntax");
					} else {
						System.out.println(">> Invoice cannot be Null!!");
					}
				} else if(prompt.equals("n")) {
					System.out.println(">> Delete operation cancelled!!!");
				} else {
					System.out.println(">> Invalid delete prompt!!! Please select between y/n");
				}
			} else {
				System.out.println(">> Invalid format for invoice!!");
				System.out.println("Try \"purchase delete help\" for proper syntax!!!");
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

	private void printPurchaseBill() {
		System.out.println("**********************************************************************************");
		System.out.println("\t\tPURCHASE BILL " + createdPurchase.getId() + "\t\tINVOICE NO " + createdPurchase.getInvoice());
		System.out.println("**********************************************************************************");
		System.out.println("SNO\t\tPRODUCT NAME\t\t\tQTY\t\tPRICE\t\tTOTAL");
		System.out.println("----------------------------------------------------------------------------------");
		for(int j = 0 ; j < createdPurchase.getPurchaseItemList().size() ; j++) {
			System.out.printf("%d\t\t%-15s\t\t\t%.1f\t\t%.2f\t\t%.2f%n", j + 1, createdPurchase.getPurchaseItemList().get(j).getProduct().getName(), createdPurchase.getPurchaseItemList().get(j).getQuantity(), createdPurchase.getPurchaseItemList().get(j).getUnitPurchasePrice(), (createdPurchase.getPurchaseItemList().get(j).getQuantity() * createdPurchase.getPurchaseItemList().get(j).getUnitPurchasePrice()));
		}
		System.out.println("----------------------------------------------------------------------------------");
		System.out.printf("GRAND TOTAL\t\t\t\t\t\t\t\t\t\t\t%.2f%n", createdPurchase.getGrandTotal());
		System.out.println("----------------------------------------------------------------------------------");
	}
}
