package CLIController;

import DAO.ApplicationErrorException;
import DAO.PageCountOutOfBoundsException;

import java.sql.SQLException;
import java.util.*;

public class AdminMain {
	static Scanner scanner;

	/**
	 * The Admin View Control.
	 *
	 * @throws ApplicationErrorException     Exception thrown due to Persistence problems.
	 * @throws PageCountOutOfBoundsException Custom Exception thrown when a non-existing page is given as input in Pageable List.
	 */
	public static void AdminView() throws ApplicationErrorException, PageCountOutOfBoundsException {
		scanner = new Scanner(System.in);
		System.out.println("\n\n>> Try \"help\" to know better!\n");
		do {
			System.out.print("\n> ");
			String command = scanner.nextLine();
			List<String> commandList = splitCommand(command);
			String commandString = commandList.get(0);
			String operationString = "";
			if(commandList.size() > 1) operationString = commandList.get(1);
			switch(commandString) {
				case "product" -> {
					ProductCLI productCLI = new ProductCLI();
					switch(operationString) {
						case "create" -> productCLI.Create(commandList);
						case "count" -> productCLI.count(commandList);
						case "list" -> productCLI.list(commandList);
						case "edit" -> productCLI.edit(commandList, command);
						case "delete" -> productCLI.delete(commandList);
						default -> {
							System.out.println("Invalid operation for command " + "\"" + commandString + "\"");
							System.out.println("Try \"help\" for proper syntax");
						}
					}
				}
				case "user" -> {
					UserCLI userCLI = new UserCLI();
					switch(operationString) {
						case "create" -> userCLI.create(commandList);
						case "count" -> userCLI.count(commandList);
						case "list" -> userCLI.list(commandList);
						case "edit" -> userCLI.edit(commandList, command);
						case "delete" -> userCLI.delete(commandList);
						default -> {
							System.out.println(">> Invalid operation for command " + "\"" + commandString + "\"");
							System.out.println(">> Try \"help\" for proper syntax");
						}
					}
				}
				case "store" -> {
					StoreCLI storeCLI = new StoreCLI();
					switch(operationString) {
						case "create" -> storeCLI.create(commandList);
						case "edit" -> storeCLI.edit(commandList, command);
						case "delete" -> storeCLI.delete(commandList);
						default -> {
							System.out.println(">> Invalid operation for command " + "\"" + commandString + "\"");
							System.out.println(">> Try \"help\" for proper syntax");
						}
					}
				}
				case "unit" -> {
					UnitCLI unitCLI = new UnitCLI();
					switch(operationString) {
						case "create" -> unitCLI.create(commandList);
						case "list" -> unitCLI.list(commandList);
						case "edit" -> unitCLI.edit(commandList, command);
						case "delete" -> unitCLI.delete(commandList);
						default -> {
							System.out.println("Invalid operation for command " + "\"" + commandString + "\"");
							System.out.println("Try \"help\" for proper syntax");
						}
					}
				}
				case "purchase" -> {
					PurchaseCLI purchaseCLI = new PurchaseCLI();
					switch(operationString) {
						case "count":
							purchaseCLI.Count(commandList);
							break;
						case "list":
							purchaseCLI.List(commandList);
							break;
						case "delete":
							purchaseCLI.Delete(commandList);
							break;
						case "help":
							System.out.println(">> purchase products using following command\n" + "purchase date, invoice, [code1, quantity1, costprice1], [code2, quantity2, costprice2]....\n" + "\n" + "\t  date - format( YYYY-MM-DD ), mandatory\n" + "\t\tinvoice - numbers, mandatory\n" + "\t\t\n" + "\t\tThe following purchase items should be given as array of items\n" + "\t\tcode - text, min 2 - 6 char, mandatory\n" + "\t\tquantity - numbers, mandatory\n" + "\t\tcostprice - numbers, mandatory");
						default:
							if(operationString.matches("([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))")) {
								purchaseCLI.Create(command);
							} else {
								System.out.println("Invalid operation for command " + "\"" + commandString + "\"");
								System.out.println("Try either \"help\" for proper syntax or \"purchase help\" if you are trying to start a purchase!");
							}
					}
				}
				case "sales" -> {
					SalesCLI salesCLI = new SalesCLI();
					switch(operationString) {
						case "count" -> salesCLI.count(commandList);
						case "list" -> salesCLI.list(commandList);
						case "delete" -> salesCLI.delete(commandList);
						case "help" ->
								System.out.println(">> sell products using following command\n" + "\n" + "sales date, [code1, quantity1], [code2, quantity2]....\n" + "\n" + "\t\tcode - text, min 3 - 30 char, mandatory\n" + "\t\tquantity - numbers, mandatory");
						default -> {
							if(operationString.matches("([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))")) {
								salesCLI.Create(command);
							} else {
								System.out.println("Invalid operation for command " + "\"" + commandString + "\"");
								System.out.println("Try either \"help\" for proper syntax or \"sales help\" if you are trying to start a purchase!");
							}
						}
					}
				}
				case "help" -> {
					String help = "\t\tstore\n" + "\t\t\tcreate  - name, phone number, address, gst number\n" + "\t\t\tedit - name, phone number, address, gst number\n" + "\t\t\tdelete - y/n with admin password\n" + "\t\t\n" + "\t\tuser\n" + "\t\t\tcreate - usertype, username,  password, first name, last name, phone number\n" + "\t\t\tcount \n" + "\t\t\tlist \n" + "\t\t\tedit - usertype, username,  password, first name, last name, phone number\n" + "\t\t\tdelete - y/n with username\n" + "\t       \n" + "\t       product\n" + "\t\t    \tcreate - productname,unit,type,costprice\n" + "\t\t    \tcount\n" + "\t\t    \tlist\n" + "\t\t    \tedit - productname,unit,type,costprice\n" + "\t\t    \tdelete - y/n with productname or productid\n" + "\t       \n" + "\t       unit\n" + "\t \t\tcreate - name, code, description, isdividable\n" + "\t \t\tlist -\n" + "\t \t\tedit - name, code, description, isdividable\n" + "\t \t\tdelete - code\n" + "\t       \n" + "\t       stock\n" + "\t    \t\tupdate - code, quantity\n" + "\t       \n" + "\t       price\n" + "\t    \t\tupdate - code, price";
					System.out.println(help);
				}
				default -> System.out.println("Invalid Command! Not Found!");
			}
		} while(true);
	}

	private static List<String> splitCommand(String command) {
		String[] parts;
		String[] commandlet;
		if(command.contains(",")) {
			parts = command.split("[,:]");
			commandlet = parts[0].split(" ");
		} else {
			parts = command.split(",");
			commandlet = command.split(" ");
		}
		ArrayList<String> commandList = new ArrayList<>();
		if(parts.length == 1) {
			Collections.addAll(commandList, commandlet);
		} else {
			Collections.addAll(commandList, commandlet);
			commandList.addAll(Arrays.asList(parts).subList(1, parts.length));
		}
		return commandList;
	}
}
