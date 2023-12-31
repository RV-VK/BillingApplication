package CLIController;

import DAO.ApplicationErrorException;
import DAO.UnitCodeViolationException;
import Entity.Unit;
import Service.UnitService;
import Service.UnitServiceImplementation;

import java.util.List;
import java.util.Scanner;

public class UnitCLI {
	private final UnitService unitService = new UnitServiceImplementation();
	private final Scanner scanner = new Scanner(System.in);
	private int id;
	private String name;
	private String unitcode;
	private String description;
	private boolean isDividable;

	/**
	 * This method handles the presentation layer of the Create function.
	 *
	 * @param arguments Command arguments.
	 */
	public void create(List<String> arguments) {
		if(arguments.size() == 3 && arguments.get(2).equals("help")) {
			FeedBackPrinter.printUnitHelp("create");
			return;
		} else if(arguments.size() == 2) {
			System.out.print("> ");
			String parameters = scanner.nextLine();
			List<String> unitAttributes = List.of(parameters.split(","));
			createHelper(unitAttributes);
			return;
		}
		createHelper(arguments.subList(2, arguments.size()));
	}

	/**
	 * This method serves the Create function.
	 *
	 * @param unitAttributes Attributes of Unit entity.
	 */
	private void createHelper(List<String> unitAttributes) {
		if(unitAttributes.size() < 4) {
			System.out.println(">> Insufficient arguments for command \"unit create\"");
			FeedBackPrinter.printHelpMessage("unit", "create");
			return;
		}
		if(unitAttributes.size() > 4) {
			System.out.println(">> Too many arguments for command \"unit create\"");
			FeedBackPrinter.printHelpMessage("unit", "create");
			return;
		}
		name = unitAttributes.get(0).trim();
		unitcode = unitAttributes.get(1).trim();
		description = unitAttributes.get(2).trim();
		if(unitAttributes.get(3).trim().equals("true") || unitAttributes.get(3).trim().equals("false"))
			isDividable = Boolean.parseBoolean(unitAttributes.get(3).trim());
		else {
			System.out.println(">> Invalid Entry for Isdividable!! Must be either true or false!!");
			return;
		}
		Unit unit = new Unit(name, unitcode, description, isDividable);
		Unit createdUnit;
		try {
			createdUnit = unitService.create(unit);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return;
		}
		if(createdUnit != null) {
			System.out.println("Unit creation Successful!!");
			System.out.println(createdUnit);
		}
	}

	/**
	 * This method handles the presentation Layer of the List function.
	 *
	 * @param arguments Command arguments.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	public void list(List<String> arguments) throws ApplicationErrorException {
		List<Unit> unitList;
		if(arguments.size() == 3 && arguments.get(2).equals("help")) {
			FeedBackPrinter.printUnitHelp("list");
		} else if(arguments.size() == 2) {
			unitList = unitService.list();
			for(Unit unit: unitList) {
				System.out.println(">> id: " + unit.getId() + ", name: " + unit.getName() + ", code: " + unit.getCode() + ", description: " + unit.getDescription() + ", isdividable: " + unit.getIsDividable());
			}
		} else {
			FeedBackPrinter.printInvalidFormat("unit");
		}
	}

	/**
	 * Thsi method handles the presentation layer of the Edit function.
	 *
	 * @param arguments Command Arguments.
	 * @param command   Command String.
	 */
	public void edit(List<String> arguments, String command) {
		final String editCommandRegex = "^id:\\s*(\\d+)(?:,\\s*([A-Za-z]+):\\s*([^,]+))?(?:,\\s*([A-Za-z]+):\\s*([^,]+))?(?:,\\s*([A-Za-z]+):\\s*([^,]+))?(?:,\\s*([A-Za-z]+):\\s*([^,]+))?(?:,\\s*([A-Za-z]+):\\s*([^,]+))?(?:,\\s*([A-Za-z]+):\\s*([^,]+))?$";
		if(arguments.size() == 3 && arguments.get(2).equals("help")) {
			FeedBackPrinter.printUnitHelp("edit");
		} else if(arguments.size() == 2) {
			System.out.print("> ");
			String parameters = scanner.nextLine();
			if(! parameters.matches(editCommandRegex)) {
				System.out.println(">> Invalid command Format!\n>> Try \"unit edit help for proper syntax!");
				return;
			}
			List<String> unitAttributes = List.of(parameters.split("[,:]"));
			editHelper(unitAttributes);
		} else if(arguments.size() > 12) {
			System.out.println(">> Too many Arguments for command \"unit edit\"");
			FeedBackPrinter.printHelpMessage("unit", "edit");
		} else if(arguments.size() < 12) {
			System.out.println(">> Insufficient arguments for command \"unit edit\"");
			System.out.println(">> Try \"unit edit help\" for proper syntax");
		} else if(! arguments.get(2).contains("id")) {
			System.out.println(">> Id is a Mandatory argument for every Edit operation");
			System.out.println(">> For every Edit operation the first argument must be unit's ID");
			FeedBackPrinter.printHelpMessage("unit", "edit");
		} else {
			if(! command.substring(10).matches(editCommandRegex)) {
				System.out.println(">> Invalid command Format!\n>> Try \"user edit help for proper syntax!");
				return;
			}
			editHelper(arguments.subList(2, arguments.size()));
		}
	}

	/**
	 * This method serves the edit function
	 *
	 * @param editAttributes Attributes of the Unit to be edited.
	 */
	private void editHelper(List<String> editAttributes) {
		Unit unit = new Unit();
		id = 0;
		try {
			id = Integer.parseInt(editAttributes.get(1).trim());
		} catch(Exception e) {
			System.out.println(">> Id must be a number");
			FeedBackPrinter.printHelpMessage("unit", "edit");
			return;
		}
		unit.setId(id);
		for(int index = 2 ; index < editAttributes.size() ; index = index + 2) {
			switch(editAttributes.get(index).trim()) {
				case "name" -> unit.setName(editAttributes.get(index + 1).trim());
				case "code" -> unit.setCode(editAttributes.get(index + 1).trim());
				case "description" -> unit.setDescription(editAttributes.get(index + 1).trim());
				case "isdividable" -> {
					if(editAttributes.get(index + 1).trim().equals("true") || editAttributes.get(index + 1).trim().equals("false")) {
						isDividable = Boolean.parseBoolean(editAttributes.get(index + 1).trim());
					} else {
						System.out.println(">>Invalid Entry for Unitcode!! Must be either true or false ");
						return;
					}
					unit.setIsDividable(isDividable);
				}
				default -> {
					System.out.println(">> Invalid attribute given!!!: " + editAttributes.get(index));
					FeedBackPrinter.printHelpMessage("unit", "edit");
					return;
				}
			}
		}
		Unit editedUnit;
		try {
			editedUnit = unitService.edit(unit);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return;
		}
		if(editedUnit != null) {
			System.out.println(">> Unit Edited Successfully!!!");
			System.out.println(editedUnit);
		} else {
			System.out.println(">> Unit Edit failed!!!");
			System.out.println(">> Please check the Id you have entered!!!");
		}
	}

	/**
	 * This method handles the presentation layer of the delete function.
	 *
	 * @param arguments Command arguments.
	 * @throws ApplicationErrorException Exception thrown due to Persistence problems.
	 */
	public void delete(List<String> arguments) throws ApplicationErrorException, UnitCodeViolationException {
		String codeRegex = "^[a-zA-Z]{1,4}$";
		if(arguments.size() == 3) {
			if(arguments.get(2).equals("help")) {
				FeedBackPrinter.printUnitHelp("delete");
			} else if(arguments.get(2).matches(codeRegex)) {
				System.out.print(">> Are you Sure you want to delete the Unit y/n :");
				String prompt = scanner.nextLine();
				if(prompt.equals("y")) {
					try {
						if(unitService.delete(arguments.get(2)) == 1) {
							System.out.println(">> Unit deleted Successfully!!!");
						} else if(unitService.delete(arguments.get(2)) == 0) {
							System.out.println(">> Unit deletion failed!!!");
							System.out.println(">> Please check the unitcode you have entered!!!");
							FeedBackPrinter.printHelpMessage("unit", "delete");
						} else {
							System.out.println(">> Unitcode cannot be null");
						}
					} catch(Exception e) {
						System.out.println(e.getMessage());
					}
				} else if(prompt.equals("n")) {
					System.out.println(">> Delete operation cancelled");
				} else {
					System.out.println("Invalid delete prompt!!! Please select between y/n");
				}
			} else {
				System.out.println(">> Invalid format for unitCode!!!");
				FeedBackPrinter.printHelpMessage("unit", "delete");
			}
		}
	}
}
