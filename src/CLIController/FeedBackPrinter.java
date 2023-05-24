package CLIController;

import java.util.List;

public class FeedBackPrinter {
	public static void printProductHelp(String operation) {
		switch(operation) {
			case "create" ->
					System.out.println(">> create product using the following template\n" + ">> code, name, unit, type, price, stock\n" + "\t\n" + "\tcode - text, min - 2 - 6, mandatory\n" + "\tname - text, min 3 - 30 char, mandatory\n" + "\tunitcode - text, kg/l/piece/combo, mandatory\n" + "\ttype - text, between enumerated values, mandatory \n" + "\tprice - number, mandatory\n" + "\tstock - number, default 0\n" + "\t\n" + ">\tproduct create code, productname, unitcode, type, price, stock\n" + "                         or\n" + "> product create :enter\n" + "code, name, unitcode, type, price, stock\n");
			case "list" ->
					System.out.println(">> List product with the following options\n" + ">> product list - will list all the products default to maximum upto 20 products\n" + ">> product list -p 10 - pageable list shows 10 products as default\n" + ">> product list -p 10 3 - pagable list shows 10 products in 3rd page, ie., product from 21 to 30\n" + ">> product list -s searchtext - search the product with the given search text in all the searchable attributes\n" + ">> product list -s <attr>: searchtext - search the product with the given search text in all the given attribute\n" + ">> product list -s <attr>: searchtext -p 10 6 - pagable list shows 10 products in 6th page with the given search text in the given attribute\n");
			case "edit" ->
					System.out.println(">> Edit product using following template. Copy the product data from the list, edit the attribute values. \n" + ">> id: <id - 6>, name: <name-edited>, unitcode: <unitcode>,  type: <type>, price: <price>\n" + "\n" + ">> You can also restrict the product data by editable attributes. Id attribute is mandatory for all the edit operation.\n" + ">> id: <id - 6>, name: <name-edited>, unitcode: <unitcode-edited>\n" + "\n" + ">> You can not give empty or null values to the mandatory attributes.\n" + ">> id: <id - 6>, name: , unitcode: null\n" + ">>\n" + " \n" + " \tid\t - number, mandatory\t\n" + "\tname - text, min 3 - 30 char, mandatory\n" + "\tunitcode - text, kg/l/piece/combo, mandatory\n" + "\ttype - text, between enumerated values, mandatory \n" + "\tcostprice - numeric, mandatory\n" + "\t\n" + ">\tproduct edit id:<id - 6>, name: <name-edited>, unitcode: <unitcode>,  type: <type>, price: <price>\n" + "                         or\n" + "> product edit :enter\n" + "> id: <id - 6>, name: <name-edited>, unitcode: <unitcode>,  type: <type>, price: <price>");
			case "delete" ->
					System.out.println("> product delete help \n" + ">> delete product using the following template\n" + "\t\n" + "\t\tproductid - numeric, existing\n" + ">> product delete -c <code>\n" + "\t \n" + "\n" + "> product delete <id>");
		}
	}

	public static void printUserHelp(String operation) {
		switch(operation) {
			case "create" ->
					System.out.println(">> create user using following template\n" + ">>  usertype, username,  password, first name, last name, phone number\n" + "\tusertype - text, purchase/sales, mandatory\n" + "\tusername - text, min 3 - 30 char, mandatory\n" + "\tpassword - text, alphanumeric, special char, min 8 char, mandatory\n" + "\tfirstname - text, mandatory with 3 to 30 chars\n" + "\tlastname  - text, optional\n" + "\tphone - number, mandatory, ten digits, digit start with 9/8/7/6\t\t\t\t\n");
			case "list" ->
					System.out.println(">> List user with the following options\n" + ">> user list - will list all the users default to maximum upto 20 users\n" + ">> user list -p 10 - pageable list shows 10 users as default\n" + ">> user list -p 10 3 - pagable list shows 10 users in 3rd page, ie., user from 21 to 30\n" + ">> user list -s searchtext - search the user with the given search text in all the searchable attributes\n" + ">> user list -s <attr>: searchtext - search the user with the given search text in all the given attribute\n" + ">> user list -s <attr>: searchtext -p 10 6 - pagable list shows 10 users in 6th page with the given search text in the given attribute\n");
			case "edit" ->
					System.out.printf(">> Edit user using following template. Copy the user data from the list, edit the attribute values. \n>> id: <id - 6>, usertype: <usertype-edited>, username: <username>,  password: <password>, first name: <first name>, last name: <last name>, phone number: <phone number>\n\n>> You can also restrict the user data by editable attributes. Id attribute is mandatory for all the edit operation.\n>> id: <id - 6>, usertype: <usertype-edited>, username: <username-edited>\n\n>> You can not give empty or null values to the mandatory attributes.\n>> id: <id - 6>, usertype: , username: null\n\t\n\tid\t\t\t - number, mandatory\t\n\tusertype - text, purchase/sales, mandatory\n\t username - text, min 3 - 30 char, mandatory\n\tpassword - text, alphanumeric, special char, min 8 char, mandatory\n\tfirstname - text, mandatory with 3 to 30 chars\n\tlastname  - text, optional\n\tphone - number, mandatory, ten digits, digit start with 9/8/7/6%n");
			case "delete" ->
					System.out.println(">> delete user using the following template\n" + "\t username\n" + "\t \n" + "\t  username - text, min 3 - 30 char, mandatory,existing\n" + "\n");
		}
	}

	public static void printStoreHelp(String operation) {
		switch(operation) {
			case "create" ->
					System.out.println(">> Create store using the following template,\n" + "     name, phone number, address, gst number\n" + " \n" + "\tname  - text, mandatory with 3 to 30 chars\t\n" + "\tphone - number, mandatory, ten digits, digit start with 9/8/7/6\n" + "\taddress - text, mandatory\n" + "\tgst number - text, 15 digit, mandatory");
			case "edit" ->
					System.out.println(">> Edit store using the following template\t\n" + "\n" + "name, phone number, address, gst number\n" + " \n" + "\tname  - text, mandatory with 3 to 30 chars\t\n" + "\tphone - number, mandatory, ten digits, digit start with 9/8/7/6\n" + "\taddress - text, mandatory\n" + "\tgst number - text, 15 digit, mandatory");
			case "delete" -> System.out.println(">> delete store using the following template\n" + "\tstore delete \n");
		}
	}

	public static void printUnitHelp(String operation) {
		switch(operation) {
			case "create" ->
					System.out.println(">> Create unit using the following template,\n" + "     name, code, description, isdividable\n" + "     \n" + "     name - text, mandatory with 3 to 30 chars\t\n" + "     code - text, maximum 4 char, mandatory\n" + "     description - text\n" + "     isdividable - boolean, mandatory\n" + "    ");
			case "list" ->
					System.out.println(">> List unit with the following options\n" + ">> unit list - will list all the units");
			case "edit" ->
					System.out.println(">> Edit unit using the following template\n" + "id: <id - 6>, name: <name-edited>, code: <code>,  description: <description>, isdividable: <isdividable>\n" + "\n" + ">> You can also restrict the user data by editable attributes. Id attribute is mandatory for all the edit operation.\n" + ">> id: <id - 6>, name: <name>, code: <code>\n" + "\n" + ">> You can not give empty or null values to the mandatory attributes.\n" + ">> id: <id - 6>, name: , code: null\n" + "\n" + "\t\t name - text, mandatory with 3 to 30 chars\t\n" + "     code - text, maximum 4 char, mandatory\n" + "     description - text\n" + "     isdividable - boolean, mandatory");
			case "delete" ->
					System.out.println(">> delete unit using the following template\n" + "\t \tcode\n" + "\t \n" + "\t  code - text, min 3 - 30 char, mandatory,existing\n");
		}

	}

	public static void printPurchaseHelp(String operation) {
		switch(operation) {
			case "count" ->
					System.out.println("Count Purchase using the following Template\n" + "> purchase count -d <date>\n" + "\n" + ">> count : <number>\n" + "\n" + "> purchase count\n" + "\n" + ">> count : <number>\n" + "\n" + "> purchase count -c <category>\n" + "\n" + ">> count : <number>\n");
			case "list" ->
					System.out.println(">> List purchase with the following options\n" + ">> purchase list - will list all the purchases default to maximum upto 20 purchases\n" + ">> purchase list -p 10 - pageable list shows 10 purchases as default\n" + ">> purchase list -p 10 3 - pageable list shows 10 purchases in 3rd page, ie., purchase from 21 to 30\n" + "\n" + ">> Use only the following attributes: id, date, invoice\n" + ">> purchase list -s <attr>: searchtext - search the purchase with the given search text in all the given attribute\n" + ">> purchase list -s <attr>: searchtext -p 10 6 - pageable list shows 10 purchases in 6th page with the given search text in the given attribute\n" + "\n" + "> purchase list -s <date> : <23-03-2023> -p 5 2 \n" + "> purchase list -s <invoice> : <785263>");
			case "delete" ->
					System.out.println(">> Delete purchase using following command \n" + "\n" + ">> purchase delete <invoice>\n" + "\t\tinvoice - numeric, mandatory\n" + "\t\t");
		}

	}

	public static void printSalesHelp(String operation) {
		switch(operation) {
			case "count" ->
					System.out.println("Count Sales using the Following Template\n sales count -d <date>\n" + "\n" + ">> count : <number>\n" + "\n" + "> sales count\n" + "\n" + ">> count : <number>\n" + "\n" + "> sales count -c <category>\n" + "\n" + ">> count : <number>\n");
			case "list" ->
					System.out.println(" >> List sales with the following options\n" + ">> sales list - will list all the sales default to maximum upto 20 sales\n" + ">> sales list -p 10 - pageable list shows 10 sales as default\n" + ">> sales list -p 10 3 - pagable list shows 10 sales in 3rd page, ie., sale from 21 to 30\n" + "\n" + ">> Use only the following attributes: id, date\n" + ">> sales list -s <attr>: searchtext - search the sale with the given search text in all the given attribute\n" + ">> sales list -s <attr>: searchtext -p 10 6 - pagable list shows 10 sales in 6th page with the given search text in the given attribute\n" + "\n" + "> sales list -s <date> : <23/03/2023> -p 5 2 \n" + "> sales list -s <id> : <10>\n");
			case "delete" ->
					System.out.println(">> >> Delete sales using following command \n" + "\n" + ">> sales delete <id>\n" + "\t\tid - numeric, mandatory");
		}
	}

	public static void printInvalidExtension(String Entity) {
		System.out.println(">> Invalid Extension given");
		System.out.println(">> Try \"" + Entity + " list help\" for proper syntax");
	}

	public static void printNonSearchableAttribute(String Entity, List<String> attributes) {
		System.out.println(">> Given attribute is not a searchable attribute!!");
		System.out.println(">> Searchable Attributes are :" + attributes);
		System.out.println(">> Try \"" + Entity + " list help\" for proper syntax");
	}

	public static void printInvalidFormat(String Entity) {
		System.out.println("Invalid command format!!!");
		System.out.println(">> Try \"" + Entity + " list help\" for proper syntax");
	}

	public static void printHelpMessage(String Entity, String operation) {
		System.out.println("Try \"" + Entity + " " + operation + " help for proper syntax");
	}
}
