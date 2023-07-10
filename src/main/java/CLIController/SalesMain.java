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
public class SalesMain {
	private final List<String> commandEntityList = Arrays.asList("product", "user", "store", "unit", "sales");
	@Autowired
	ProductCLI productCLI = new ProductCLI();
	@Autowired
	SalesCLI salesCLI = new SalesCLI();


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
	 * Sales user View Control.
	 *
	 * @throws ApplicationErrorException     Exception thrown due to Persistence problems.
	 * @throws PageCountOutOfBoundsException Custom Exception thrown when a non-existing page is given as input in Pageable List.
	 */
	public void SalesView() throws ApplicationErrorException, PageCountOutOfBoundsException, SQLException, UnitCodeViolationException, InvalidTemplateException {
		Scanner scanner = new Scanner(System.in);
		System.out.println("\n\n>> Try \"help\" to know better!\n");
		do {
			System.out.print("> ");
			String command = scanner.nextLine();
			List<String> commandList = splitCommand(command);
			String commandString = commandList.get(0);
			String operationString = "";
			if(commandList.size() > 1) operationString = commandList.get(1);
			switch(commandString) {
				case "sales":
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
					break;
				case "product":
					if(operationString.equals("list")) {
						productCLI.list(commandList);
					} else {
						System.out.println("Invalid operation for command \"" + commandString + "\"");
						System.out.println("Try \"Help\" for proper syntax");
					}
					break;
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
				case "exit":
					System.exit(0);
					break;
				case "logout":
					ApplicationContext context = new AnnotationConfigApplicationContext(AppDependencyConfig.class);
					LoginCLI loginCLI = context.getBean(LoginCLI.class);
					loginCLI.Login();
					break;
				default:
					if(commandEntityList.contains(commandString)) {
						System.out.println("Non-Permitted Action!! These actions are only Permitted for Admin user");
					} else System.out.println("Invalid Command ! Not found!!");

			}
		} while(true);
	}
}