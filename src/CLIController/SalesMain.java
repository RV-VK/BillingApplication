package CLIController;

import DAO.ApplicationErrorException;
import DAO.PageCountOutOfBoundsException;

import java.util.*;

public class SalesMain {
	static Scanner scanner;

	/**
	 * Sales user View Control.
	 *
	 * @throws ApplicationErrorException     Exception thrown due to Persistence problems.
	 * @throws PageCountOutOfBoundsException Custom Exception thrown when a non-existing page is given as input in Pageable List.
	 */
	public static void SalesView() throws ApplicationErrorException, PageCountOutOfBoundsException {
		scanner = new Scanner(System.in);
		System.out.println(" TO THE BILLING SOFTWARE_____________________");
		System.out.println(">> Try \"help\" to know better!\n");
		do {
			System.out.print("> ");
			String command = scanner.nextLine();
			List<String> commandList = splitCommand(command);
			String commandString = commandList.get(0);
			String operationString = "";
			if(commandList.size() > 1) operationString = commandList.get(1);
			switch(commandString) {
				case "sales":
					SalesCLI salesCLI = new SalesCLI();
					switch(operationString) {
						case "count" -> salesCLI.count(commandList);
						case "list" -> salesCLI.list(commandList);
						case "delete" -> salesCLI.delete(commandList);
						case "help" ->
								System.out.println("""
										>> sell products using following command

										sales date, [code1, quantity1], [code2, quantity2]....

										\t\tcode - text, min 3 - 30 char, mandatory
										\t\tquantity - numbers, mandatory""");
						default -> {
							if(operationString.matches("([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))")) {
								salesCLI.Create(command);
							} else {
								System.out.println("Invalid operation for command " + "\"" + commandString + "\"");
								System.out.println("Try either \"help\" for proper syntax or \"sales help\" if you are trying to start a purchase!");
							}
						}
					}
				case "product":
					ProductCLI productCLI = new ProductCLI();
					if(operationString.equals("list")) {
						productCLI.list(commandList);
					} else {
						System.out.println("Invalid operation for command \"" + commandString + "\"");
						System.out.println("Try \"Help\" for proper syntax");
					}
				case "help":
					System.out.println("""
							product
							\t    list
							""");
					System.out.println("""
							sales
							\t\t\tcreate - date, [name1, quantity1, costprice1], [name2, quantity2, costprice2]....
							\t\t\tcount
							\t\t\tlist
							\t\t\tdelete - id""");
					break;
				default:
					System.out.println("Invalid Command! Not found!");
			}
		} while(true);
	}

	private static List<String> splitCommand(String command) {
		String[] parts;
		String[] commandlet;
		if(command.contains(",")) {
			parts = command.split("[,:]");
			commandlet = parts[0].split("\\s+");
		} else {
			parts = command.split(",");
			commandlet = command.split("\\s+");
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