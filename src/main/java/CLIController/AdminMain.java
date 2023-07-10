package CLIController;

import DAO.ApplicationErrorException;
import DAO.PageCountOutOfBoundsException;
import DAO.UnitCodeViolationException;
import Service.InvalidTemplateException;
import Service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.*;

@Component
public class AdminMain {
	@Autowired
	private LoginService loginService;
	@Autowired
	private ProductCLI productCLI;
	@Autowired
	private UserCLI userCLI;
	@Autowired
	private StoreCLI storeCLI;
	@Autowired
	private UnitCLI unitCLI;
	@Autowired
	private PurchaseCLI purchaseCLI;
	@Autowired
	private SalesCLI salesCLI;


	public AdminMain() {
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

	/**
	 * The Admin View Control.
	 *
	 * @throws ApplicationErrorException     Exception thrown due to Persistence problems.
	 * @throws PageCountOutOfBoundsException Custom Exception thrown when a non-existing page is given as input in Pageable List.
	 */
	public void AdminView(String userName) throws ApplicationErrorException, PageCountOutOfBoundsException, UnitCodeViolationException, SQLException, InvalidTemplateException {
		Scanner scanner = new Scanner(System.in);
		System.out.println("\n\n>> Try \"help\" to know better!\n");
		do {
			System.out.print("\n> ");
			String command = scanner.nextLine();
			List<String> commandList = splitCommand(command);
			String commandString = commandList.get(0);
			String operationString = "";
			if(commandList.size() > 1) operationString = commandList.get(1);
			if(! loginService.checkIfInitialSetup()) {
				if(commandString.equals("store") && operationString.equals("create")) storeCLI.create(commandList);
				else {
					System.out.println(">> Please Create a Store to Proceed!! You need to Register your store before Proceeding to further Billing CLI functions!!");
					FeedBackPrinter.printStoreHelp("create");
				}
			} else {
				switch(commandString) {
					case "product" -> {
						switch(operationString) {
							case "create" -> productCLI.create(commandList);
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
						switch(operationString) {
							case "create" -> storeCLI.create(commandList);
							case "edit" -> storeCLI.edit(commandList, command);
							case "delete" -> storeCLI.delete(commandList, userName);
							default -> {
								System.out.println(">> Invalid operation for command " + "\"" + commandString + "\"");
								System.out.println(">> Try \"help\" for proper syntax");
							}
						}
					}
					case "unit" -> {
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
					case "sales" -> {
						switch(operationString) {
							case "count" -> salesCLI.count(commandList);
							case "list" -> salesCLI.list(commandList);
							case "delete" -> salesCLI.delete(commandList);
							case "help" -> System.out.println("""
									>> sell products using following command

									sales date, [code1, quantity1], [code2, quantity2]....

									\t\tcode - text, min 3 - 30 char, mandatory
									\t\tquantity - numbers, mandatory""");
							default -> {
								if(operationString.matches("([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))")) {
									salesCLI.create(command);
								} else {
									System.out.println("Invalid operation for command " + "\"" + commandString + "\"");
									System.out.println("Try either \"help\" for proper syntax or \"sales help\" if you are trying to start a purchase!");
								}
							}
						}
					}
					case "help" -> FeedBackPrinter.mainHelp();
					case "exit" -> System.exit(0);
					case "logout" -> {
						ApplicationContext context = new AnnotationConfigApplicationContext(AppDependencyConfig.class);
						LoginCLI loginCLI = context.getBean(LoginCLI.class);
						loginCLI.Login();}
					default -> System.out.println("Invalid Command! Not Found!");
				}
			}
		} while(true);

	}

}
