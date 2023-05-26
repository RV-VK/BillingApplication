package CLIController;

import java.util.List;

public class FeedBackPrinter {
	public static void printProductHelp(String operation) {
		switch(operation) {
			case "create" ->
					System.out.println("""
							>> create product using the following template
							>> code, name, unit, type, price, stock
							\t
							\tcode - text, min - 2 - 6, mandatory
							\tname - text, min 3 - 30 char, mandatory
							\tunitcode - text, kg/l/piece/combo, mandatory
							\ttype - text, between enumerated values, mandatory\s
							\tprice - number, mandatory
							\tstock - number, default 0
							\t
							>\tproduct create code, productname, unitcode, type, price, stock
							                         or
							> product create :enter
							code, name, unitcode, type, price, stock
							""");
			case "list" ->
					System.out.println("""
							>> List product with the following options
							>> product list - will list all the products default to maximum upto 20 products
							>> product list -p 10 - pageable list shows 10 products as default
							>> product list -p 10 3 - pagable list shows 10 products in 3rd page, ie., product from 21 to 30
							>> product list -s searchtext - search the product with the given search text in all the searchable attributes
							>> product list -s <attr>: searchtext - search the product with the given search text in all the given attribute
							>> product list -s <attr>: searchtext -p 10 6 - pagable list shows 10 products in 6th page with the given search text in the given attribute
							""");
			case "edit" ->
					System.out.println("""
							>> Edit product using following template. Copy the product data from the list, edit the attribute values.\s
							>> id: <id - 6>, name: <name-edited>, unitcode: <unitcode>,  type: <type>, price: <price>

							>> You can also restrict the product data by editable attributes. Id attribute is mandatory for all the edit operation.
							>> id: <id - 6>, name: <name-edited>, unitcode: <unitcode-edited>

							>> You can not give empty or null values to the mandatory attributes.
							>> id: <id - 6>, name: , unitcode: null
							>>
							\s
							 \tid\t - number, mandatory\t
							\tname - text, min 3 - 30 char, mandatory
							\tunitcode - text, kg/l/piece/combo, mandatory
							\ttype - text, between enumerated values, mandatory\s
							\tcostprice - numeric, mandatory
							\t
							>\tproduct edit id:<id - 6>, name: <name-edited>, unitcode: <unitcode>,  type: <type>, price: <price>
							                         or
							> product edit :enter
							> id: <id - 6>, name: <name-edited>, unitcode: <unitcode>,  type: <type>, price: <price>""");
			case "delete" ->
					System.out.println("""
							> product delete help\s
							>> delete product using the following template
							\t
							\t\tproductid - numeric, existing
							>> product delete -c <code>
							\t\s

							> product delete <id>""");
		}
	}

	public static void printUserHelp(String operation) {
		switch(operation) {
			case "create" ->
					System.out.println("""
							>> create user using following template
							>>  usertype, username,  password, first name, last name, phone number
							\tusertype - text, purchase/sales, mandatory
							\tusername - text, min 3 - 30 char, mandatory
							\tpassword - text, alphanumeric, special char, min 8 char, mandatory
							\tfirstname - text, mandatory with 3 to 30 chars
							\tlastname  - text, optional
							\tphone - number, mandatory, ten digits, digit start with 9/8/7/6\
							\t> user create userid, usertype, username,  password, first name, last name, phone number
							\t								or
							\t> user create :enter
							\tusertype, username,  password, first name, last name, phone number
							""");
			case "list" ->
					System.out.println("""
							>> List user with the following options
							>> user list - will list all the users default to maximum upto 20 users
							>> user list -p 10 - pageable list shows 10 users as default
							>> user list -p 10 3 - pagable list shows 10 users in 3rd page, ie., user from 21 to 30
							>> user list -s searchtext - search the user with the given search text in all the searchable attributes
							>> user list -s <attr>: searchtext - search the user with the given search text in all the given attribute
							>> user list -s <attr>: searchtext -p 10 6 - pagable list shows 10 users in 6th page with the given search text in the given attribute
							""");
			case "edit" ->
					System.out.printf(">> Edit user using following template. Copy the user data from the list, edit the attribute values. \n>> id: <id - 6>, usertype: <usertype-edited>, username: <username>,  password: <password>, first name: <first name>, last name: <last name>, phone number: <phone number>\n\n>> You can also restrict the user data by editable attributes. Id attribute is mandatory for all the edit operation.\n>> id: <id - 6>, usertype: <usertype-edited>, username: <username-edited>\n\n>> You can not give empty or null values to the mandatory attributes.\n>> id: <id - 6>, usertype: , username: null\n\t\n\tid\t\t\t - number, mandatory\t\n\tusertype - text, purchase/sales, mandatory\n\t username - text, min 3 - 30 char, mandatory\n\tpassword - text, alphanumeric, special char, min 8 char, mandatory\n\tfirstname - text, mandatory with 3 to 30 chars\n\tlastname  - text, optional\n\tphone - number, mandatory, ten digits, digit start with 9/8/7/6%n");
			case "delete" ->
					System.out.println("""
							>> delete user using the following template
							\t username
							\t\s
							\t  username - text, min 3 - 30 char, mandatory,existing
							\t > user delete <username>

							""");
		}
	}

	public static void printStoreHelp(String operation) {
		switch(operation) {
			case "create" -> System.out.println("""
							>> Create store using the following template,
							     name, phone number, address, gst number
							\s
							\tname  - text, mandatory with 3 to 30 chars\t
							\tphone - number, mandatory, ten digits, digit start with 9/8/7/6
							\taddress - text, mandatory
							\tgst number - text, 15 digit, mandatory
							\t> store create name, phone number, address, gst number
							\t					or
							\t> store create :enter
							\t> name, phone number, address, gst number    \s
							""");

			case "edit" ->
					System.out.println("""
							>> Edit store using the following template\t

							name, phone number, address, gst number
							\s
							\tname  - text, mandatory with 3 to 30 chars\t
							\tphone - number, mandatory, ten digits, digit start with 9/8/7/6
							\taddress - text, mandatory
							\tgst number - text, 15 digit, mandatory
														
							\t> store edit name: <name>, phonenumber: <phonenumber>, address: <address>, gst number: <gstnumber>
							\t   					or
							\t> store edit :enter
							\t> name: <name>, phonenumber: <phonenumber>, address: <address>, gstnumber: <gstnumber>
							   
														
														
							""");
			case "delete" -> System.out.println("""
					>> delete store using the following template
					\tstore delete\s
					""");
		}
	}

	public static void printUnitHelp(String operation) {
		switch(operation) {
			case "create" ->
					System.out.println("""
							>> Create unit using the following template,
							     name, code, description, isdividable
							    \s
							     name - text, mandatory with 3 to 30 chars\t
							     code - text, maximum 4 char, mandatory
							     description - text
							     isdividable - boolean, mandatory
							     > unit create name, code, description, isdividable
							     							or
							     	unit create :enter
							     	name, code, description, isdividable
							   \s""");
			case "list" ->
					System.out.println(">> List unit with the following options\n" + ">> unit list - will list all the units");
			case "edit" ->
					System.out.println("""
							>> Edit unit using the following template
							id: <id - 6>, name: <name-edited>, code: <code>,  description: <description>, isdividable: <isdividable>

							>> You can also restrict the user data by editable attributes. Id attribute is mandatory for all the edit operation.
							>> id: <id - 6>, name: <name>, code: <code>

							>> You can not give empty or null values to the mandatory attributes.
							>> id: <id - 6>, name: , code: null

							\t\t name - text, mandatory with 3 to 30 chars\t
							     code - text, maximum 4 char, mandatory
							     description - text
							     isdividable - boolean, mandatory""");
			case "delete" ->
					System.out.println("""
							>> delete unit using the following template
							\t \tcode
							\t\s
							\t  code - text, min 3 - 30 char, mandatory,existing
							\t  > unit delete <code>
							""");
		}

	}

	public static void printPurchaseHelp(String operation) {
		switch(operation) {
			case "count" ->
					System.out.println("""
							Count Purchase using the following Template
							> purchase count -d <date>

							>> count : <number>

							> purchase count

							>> count : <number>

							> purchase count -c <category>

							>> count : <number>
							""");
			case "list" ->
					System.out.println("""
							>> List purchase with the following options
							>> purchase list - will list all the purchases default to maximum upto 20 purchases
							>> purchase list -p 10 - pageable list shows 10 purchases as default
							>> purchase list -p 10 3 - pageable list shows 10 purchases in 3rd page, ie., purchase from 21 to 30

							>> Use only the following attributes: id, date, invoice
							>> purchase list -s <attr>: searchtext - search the purchase with the given search text in all the given attribute
							>> purchase list -s <attr>: searchtext -p 10 6 - pageable list shows 10 purchases in 6th page with the given search text in the given attribute

							> purchase list -s <date> : <23-03-2023> -p 5 2\s
							> purchase list -s <invoice> : <785263>""");
			case "delete" ->
					System.out.println("""
							>> Delete purchase using following command\s

							>> purchase delete <invoice>
							\t\tinvoice - numeric, mandatory
							\t\t""");
		}

	}

	public static void printSalesHelp(String operation) {
		switch(operation) {
			case "count" ->
					System.out.println("""
							Count Sales using the Following Template
							 sales count -d <date>

							>> count : <number>

							> sales count

							>> count : <number>

							> sales count -c <category>

							>> count : <number>
							""");
			case "list" ->
					System.out.println("""
							 >> List sales with the following options
							>> sales list - will list all the sales default to maximum upto 20 sales
							>> sales list -p 10 - pageable list shows 10 sales as default
							>> sales list -p 10 3 - pagable list shows 10 sales in 3rd page, ie., sale from 21 to 30

							>> Use only the following attributes: id, date
							>> sales list -s <attr>: searchtext - search the sale with the given search text in all the given attribute
							>> sales list -s <attr>: searchtext -p 10 6 - pagable list shows 10 sales in 6th page with the given search text in the given attribute

							> sales list -s <date> : <23/03/2023> -p 5 2\s
							> sales list -s <id> : <10>
							""");
			case "delete" ->
					System.out.println("""
							>> >> Delete sales using following command\s

							>> sales delete <id>
							\t\tid - numeric, mandatory""");
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
	public static void mainHelp() {
		System.out.println("""
                __________________________________________________________________________________
                store
                        create  - name, phone number, address, gst number
                        edit - name, phone number, address, gst number
                        delete - y/n with admin password
                _________________________________________________________________________________
                ___________________________________________________________________________________
                user
                        create - usertype, username,  password, first name, last name, phone number
                        count
                        list
                        edit - usertype, username,  password, first name, last name, phone number
                        delete - y/n with username
                ____________________________________________________________________________________
				
                ____________________________________________________________________________________
                product
                           create - productname,unit,type,costprice
                        count
                        list
                        edit - productname,unit,type,costprice
                        delete - y/n with productname or productid
                _____________________________________________________________________________________
				
                _____________________________________________________________________________________
                unit
                        create - name, code, description, isdividable
                        list -
                        edit - name, code, description, isdividable
                        delete - code
                _____________________________________________________________________________________
				
                _____________________________________________________________________________________________________
                purchase\s
                        create - date, invoice, [name1, quantity1, costprice1], [name2, quantity2, costprice2]....
                        count
                        list
                        delete - invoice
                _____________________________________________________________________________________________________
				
                ______________________________________________________________________________________________________
                sales
                        create - date, [name1, quantity1, costprice1], [name2, quantity2, costprice2]....
                        count
                        list
                        delete - id
                ______________________________________________________________________________________________________
                        """);
	}
}
