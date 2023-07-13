package org.example.CLIController;

import org.example.DAO.ApplicationErrorException;
import org.example.Entity.Store;
import org.example.Service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class StoreCLI {
	private final Scanner scanner = new Scanner(System.in);
	@Autowired
	private StoreService storeService;
	private String name;
	private long phoneNumber;
	private String GSTNumber;
	private String address;

	/**
	 * This method handles the presentation layer of the Create function
	 *
	 * @param arguments Command arguments.
	 */
	public void create(List<String> arguments) {
		if(arguments.size() == 3 && arguments.get(2).equals("help")) {
			FeedBackPrinter.printStoreHelp("create");
			return;
		} else if(arguments.size() == 2) {
			System.out.print("> ");
			String parameters = scanner.nextLine();
			List<String> storeAttributes = List.of(parameters.split(","));
			createHelper(storeAttributes);
			return;
		}
		createHelper(arguments.subList(2, arguments.size()));
	}

	/**
	 * This method serves the create function.
	 *
	 * @param storeAttributes Attributes of Store entity.
	 */
	private void createHelper(List<String> storeAttributes) {
		if(storeAttributes.size() < 4) {
			System.out.println(">> Insufficient arguments for command \"store create\"");
			FeedBackPrinter.printHelpMessage("store", "create");
			return;
		}
		if(storeAttributes.size() > 4) {
			System.out.println(">> Too many arguments for command \"store create\"");
			FeedBackPrinter.printHelpMessage("store", "create");
			return;
		}
		name = storeAttributes.get(0).trim();
		phoneNumber = 0L;
		address = storeAttributes.get(2).trim();
		try {
			phoneNumber = Long.parseLong(storeAttributes.get(1).trim());
		} catch(Exception e) {
			System.out.println(">> Invalid format for 2nd argument \"phonenumber\"");
			FeedBackPrinter.printHelpMessage("store", "create");
			return;
		}

		GSTNumber = storeAttributes.get(3).trim();
		Store store = new Store(name, phoneNumber, address, GSTNumber);
		Store createdStore;
		try {
			createdStore = storeService.create(store);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return;
		}
		if(createdStore == null) System.out.println(">> Store already exists!!");
		else if(createdStore.getName() != null) {
			System.out.println(">> Store Created Successfully!!!");
		}
	}

	/**
	 * This method handles the presentation layer of the Edit function
	 *
	 * @param arguments Command arguments.
	 * @param command   Command String.
	 */
	public void edit(List<String> arguments, String command) {
		final String editCommandRegex = "^name:\\s*([A-Za-z0-9\\s]+)(?:,\\s*([A-Za-z]+):\\s*([^,]+))?(?:,\\s*([A-Za-z]+):\\s*([^,]+))?(?:,\\s*([A-Za-z]+):\\s*([^,]+))?(?:,\\s*([A-Za-z]+):\\s*([^,]+))?(?:,\\s*([A-Za-z]+):\\s*([^,]+))?(?:,\\s*([A-Za-z]+):\\s*([^,]+))?$";
		if(arguments.size() == 3 && arguments.get(2).equals("help")) {
			FeedBackPrinter.printStoreHelp("edit");
		} else if(arguments.size() == 2) {
			System.out.print("> ");
			String parameters = scanner.nextLine();
			if(! parameters.matches(editCommandRegex)) {
				System.out.println(">> Invalid command Format!\n>> Try \"store edit help for proper syntax!");
				return;
			}
			List<String> storeAttributes = List.of(parameters.split("[,:]"));
			editHelper(storeAttributes);
		} else if(arguments.size() > 10) {
			System.out.println(">> Too many Arguments for command \"store edit\"");
			FeedBackPrinter.printHelpMessage("store", "edit");
		} else if(arguments.size() < 10) {
			System.out.println(">> Insufficient arguments for command \"store edit\"");
			FeedBackPrinter.printHelpMessage("store", "edit");
		} else {
			if(! command.substring(11).matches(editCommandRegex)) {
				System.out.println(">> Invalid command Format!\n>> Try \"store edit help for proper syntax!");
				return;
			}
			editHelper(arguments.subList(2, arguments.size()));
		}
	}

	/**
	 * This method serves the Edit function.
	 *
	 * @param editAttributes Store attributes to be edited.
	 */
	private void editHelper(List<String> editAttributes) {
		Store store = new Store();
		for(int index = 0 ; index < editAttributes.size() ; index = index + 2) {
			switch(editAttributes.get(index).trim()) {
				case "name" -> store.setName(editAttributes.get(index + 1).trim());
				case "phonenumber" -> {
					try {
						phoneNumber = Long.parseLong(editAttributes.get(index + 1).trim());
					} catch(NumberFormatException e) {
						System.out.println(">> PhoneNumber must be numeric!!");
						FeedBackPrinter.printHelpMessage("store", "edit");
						return;
					}
					store.setPhoneNumber(phoneNumber);
				}
				case "address" -> store.setAddress(editAttributes.get(index + 1).trim());
				case "gstnumber" -> {
					GSTNumber = editAttributes.get(index + 1).trim();
					store.setGstCode(GSTNumber);
				}
				default -> {
					System.out.println(">> Invalid attribute given!!!: " + editAttributes.get(index));
					FeedBackPrinter.printHelpMessage("store", "edit");
					return;
				}
			}
		}
		Store editedStore;
		try {
			editedStore = storeService.edit(store);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return;
		}
		if(editedStore != null) {
			System.out.println(">> Store Edited Successfully!!!");
			System.out.println(editedStore);
		} else {
			System.out.println(">> Store Edit failed!!!");
			System.out.println(">> Please check the name you have entered!!!");
		}
	}

	/**
	 * This method handles the presentation layer of the Delete function.
	 *
	 * @param arguments Command arguments.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	public void delete(List<String> arguments, String userName) throws ApplicationErrorException {
		if(arguments.size() == 3 && arguments.get(2).equals("help")) {
			FeedBackPrinter.printStoreHelp("delete");
		} else if(arguments.size() == 2) {
			System.out.print("Are you sure want to delete the Store? This will delete all you product/purchase/sales data y/n ? : ");
			String prompt = scanner.nextLine();
			if(prompt.equals("y")) {
				System.out.print(">> Enter admin password to delete the store: ");
				String password = scanner.nextLine();
				int resultCode = storeService.delete(userName, password);
				if(resultCode == 1) {
					System.out.println(">> Store deleted Successfully !!! GOOD BYE !");
					System.exit(0);
				} else if(resultCode == - 1) {
					System.out.println(">> Unable to delete Store!");
					System.out.println(">> Invalid Admin Password");
				} else if(resultCode == 0) {
					System.out.println(">> No store exists to delete!!");
				}
			} else if(prompt.equals("n")) {
				System.out.println(">> Delete operation cancelled!!!");
			} else {
				System.out.println(">> Invalid prompt!! Please select between y/n:");
			}
		} else {
			System.out.println(">> Invalid Command!!!");
			FeedBackPrinter.printHelpMessage("store", "delete");
		}
	}
}
