package CLIController;

import DAO.ApplicationErrorException;
import DAO.PageCountOutOfBoundsException;
import DAO.UnitCodeViolationException;
import Service.InvalidTemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.*;

@Component
public class PurchaseMain {
	private final List<String> commandEntityList = Arrays.asList("product", "user", "store", "unit", "sales");
	@Autowired
	PurchaseCLI purchaseCLI = new PurchaseCLI();
	@Autowired
	ProductCLI productCLI = new ProductCLI();


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

	/**
	 * Purchase user View Control.
	 *
	 * @throws PageCountOutOfBoundsException Custom Exception thrown when a non-existing page is given as input in Pageable List.
	 * @throws ApplicationErrorException     Exception thrown due to Persistence problems.
	 */
	public void PurchaseView() throws PageCountOutOfBoundsException, ApplicationErrorException, SQLException, UnitCodeViolationException, InvalidTemplateException {
		Scanner scanner = new Scanner(System.in);
		System.out.println("\n\n\n>> Try \"help\" to know better!\n");
		do {
			System.out.print("> ");
			String command = scanner.nextLine();
			List<String> commandList = splitCommand(command);
			String commandString = commandList.get(0);
			String operationString = "";
			if(commandList.size() > 1) operationString = commandList.get(1);
			switch(commandString) {
				case "purchase" -> {
					switch(operationString) {
						case "count" -> purchaseCLI.count(commandList);
						case "list" -> purchaseCLI.list(commandList);
						case "delete" -> purchaseCLI.delete(commandList);
						case "help" -> System.out.println("""
								>> purchase products using following command
								purchase date, invoice, [code1, quantity1, costprice1], [code2, quantity2, costprice2]....

								\t  date - format( YYYY-MM-DD ), mandatory
								\t\tinvoice - numbers, mandatory
								\t\t
								\t\tThe following purchase items should be given as array of items
								\t\tcode - text, min 2 - 6 char, mandatory
								\t\tquantity - numbers, mandatory
								\t\tcostprice - numbers, mandatory""");
						default -> {
							if(operationString.matches("([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))")) {
								purchaseCLI.create(command);
							} else {
								System.out.println("Invalid operation for command " + "\"" + commandString + "\"");
								System.out.println("Try either \"help\" for proper syntax or \"purchase help\" if you are trying to start a purchase!");
							}
						}
					}
				}
				case "product" -> {
					if(operationString.equals("list")) {
						productCLI.list(commandList);
					} else {
						System.out.println("Invalid operation for command \"" + commandString + "\"");
						System.out.println("Try \"Help\" for proper syntax");
					}
				}
				case "help" -> {
					System.out.println("""
							product
							\t    list
							""");
					System.out.println("""
							purchase\s
							\t\t\tcreate - date, invoice, [name1, quantity1, costprice1], [name2, quantity2, costprice2]....
							\t\t\tcount
							\t\t\tlist
							\t\t\tdelete - invoice""");
				}
				case "exit" -> System.exit(0);
				case "logout" -> {
					ApplicationContext context = new AnnotationConfigApplicationContext(AppDependencyConfig.class);
					LoginCLI loginCLI = context.getBean(LoginCLI.class);
					loginCLI.Login();
				}
				default -> {
					if(commandEntityList.contains(commandString)) {
						System.out.println("Non-Permitted Action!! These actions are only Permitted for Admin user");
					} else System.out.println("Invalid Command ! Not found!!");
				}
			}
		} while(true);
	}

}
