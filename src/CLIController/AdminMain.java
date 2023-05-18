package CLIController;

import DAO.ApplicationErrorException;
import DAO.PageCountOutOfBoundsException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class AdminMain {
  static Scanner scanner;

  public static void AdminView()
      throws ApplicationErrorException, PageCountOutOfBoundsException, SQLException {
    scanner = new Scanner(System.in);
    System.out.println(" TO THE BILLING SOFTWARE_____________________");
    System.out.println(">> Try \"help\" to know better!\n");
    do {
      System.out.print("> ");
      String command = scanner.nextLine();
      String[] parts;
      String[] commandlet;
      if (command.contains(",")) {
        parts = command.split("[,:]");
        commandlet = parts[0].split(" ");
      } else {
        parts = command.split(",");
        commandlet = command.split(" ");
      }
      ArrayList<String> commandlist = new ArrayList<>();
      if (parts.length == 1) {
        Collections.addAll(commandlist, commandlet);
      } else {
        Collections.addAll(commandlist, commandlet);
        commandlist.addAll(Arrays.asList(parts).subList(1, parts.length));
      }
      String commandString = commandlist.get(0);
      String operationString = "";
      if (commandlist.size() > 1) operationString = commandlist.get(1);
      switch (commandString) {
        case "product":
          ProductCLI productCLI = new ProductCLI();
          switch (operationString) {
            case "create":
              productCLI.Create(commandlist);
              break;
            case "count":
              productCLI.count(commandlist);
              break;
            case "list":
              productCLI.list(commandlist);
              break;
            case "edit":
              productCLI.edit(commandlist, command);
              break;
            case "delete":
              productCLI.delete(commandlist);
              break;
            default:
              System.out.println("Invalid operation for command " + "\"" + commandString + "\"");
              System.out.println("Try \"help\" for proper syntax");
          }
          break;
        case "user":
          UserCLI userCLI = new UserCLI();
          switch (operationString) {
            case "create":
              userCLI.create(commandlist);
              break;
            case "count":
              userCLI.count(commandlist);
              break;
            case "list":
              userCLI.list(commandlist);
              break;
            case "edit":
              userCLI.edit(commandlist, command);
              break;
            case "delete":
              userCLI.delete(commandlist);
              break;
            default:
              System.out.println(">> Invalid operation for command " + "\"" + commandString + "\"");
              System.out.println(">> Try \"help\" for proper syntax");
          }
          break;
        case "store":
          StoreCLI storeCLI = new StoreCLI();
          switch (operationString) {
            case "create":
              storeCLI.create(commandlist);
              break;
            case "edit":
              storeCLI.edit(commandlist, command);
              break;
            case "delete":
              storeCLI.delete(commandlist);
              break;
            default:
              System.out.println(">> Invalid operation for command " + "\"" + commandString + "\"");
              System.out.println(">> Try \"help\" for proper syntax");
          }
          break;
        case "unit":
          UnitCLI unitCLI = new UnitCLI();
          switch (operationString) {
            case "create":
              unitCLI.create(commandlist);
              break;
            case "list":
              unitCLI.list(commandlist);
              break;
            case "edit":
              unitCLI.edit(commandlist, command);
              break;
            case "delete":
              unitCLI.delete(commandlist);
              break;
            default:
              System.out.println("Invalid operation for command " + "\"" + commandString + "\"");
              System.out.println("Try \"help\" for proper syntax");
          }
          break;
        case "purchase":
          PurchaseCLI purchaseCLI = new PurchaseCLI();
          switch (operationString) {
            case "count":
              purchaseCLI.Count(commandlist);
              break;
            case "list":
              purchaseCLI.List(commandlist);
              break;
            case "delete":
              purchaseCLI.Delete(commandlist);
              break;
            case "help":
              System.out.println(
                  ">> purchase products using following command\n"
                      + "purchase date, invoice, [code1, quantity1, costprice1], [code2, quantity2, costprice2]....\n"
                      + "\n"
                      + "\t  date - format( YYYY-MM-DD ), mandatory\n"
                      + "\t\tinvoice - numbers, mandatory\n"
                      + "\t\t\n"
                      + "\t\tThe following purchase items should be given as array of items\n"
                      + "\t\tcode - text, min 2 - 6 char, mandatory\n"
                      + "\t\tquantity - numbers, mandatory\n"
                      + "\t\tcostprice - numbers, mandatory");
            default:
              if (operationString.matches("([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))")) {
                purchaseCLI.Create(command);
              } else {
                System.out.println("Invalid operation for command " + "\"" + commandString + "\"");
                System.out.println(
                    "Try either \"help\" for proper syntax or \"purchase help\" if you are trying to start a purchase!");
              }
          }
          break;
        case "sales":
          SalesCLI salesCLI = new SalesCLI();
          switch (operationString) {
            case "count":
              salesCLI.count(commandlist);
              break;
            case "list":
              salesCLI.list(commandlist);
              break;
            case "delete":
              salesCLI.delete(commandlist);
              break;
            case "help":
              System.out.println(
                  ">> sell products using following command\n"
                      + "\n"
                      + "sales date, [code1, quantity1], [code2, quantity2]....\n"
                      + "\n"
                      + "\t\tcode - text, min 3 - 30 char, mandatory\n"
                      + "\t\tquantity - numbers, mandatory");
              break;
            default:
              if (operationString.matches("([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))")) {
                salesCLI.Create(command);
              } else {
                System.out.println("Invalid operation for command " + "\"" + commandString + "\"");
                System.out.println(
                    "Try either \"help\" for proper syntax or \"sales help\" if you are trying to start a purchase!");
              }
          }
          break;
        case "price":
          StockPriceCLI stockPriceCLI = new StockPriceCLI();
          switch (operationString) {
            case "update":
              stockPriceCLI.updatePrice(commandlist);
              break;
            default:
              System.out.println(">> Invalid Command! Not found!");
          }
          break;
        case "stock":
          StockPriceCLI stockPriceCLI1 = new StockPriceCLI();
          switch (operationString) {
            case "update":
              stockPriceCLI1.updateStock(commandlist);
              break;
            default:
              System.out.println(">> Invalid Command! Not found!");
          }
          break;
        case "help":
          String help =
              "\t\tstore\n"
                  + "\t\t\tcreate  - name, phone number, address, gst number\n"
                  + "\t\t\tedit - name, phone number, address, gst number\n"
                  + "\t\t\tdelete - y/n with admin password\n"
                  + "\t\t\n"
                  + "\t\tuser\n"
                  + "\t\t\tcreate - usertype, username,  password, first name, last name, phone number\n"
                  + "\t\t\tcount \n"
                  + "\t\t\tlist \n"
                  + "\t\t\tedit - usertype, username,  password, first name, last name, phone number\n"
                  + "\t\t\tdelete - y/n with username\n"
                  + "\t       \n"
                  + "\t       product\n"
                  + "\t\t    \tcreate - productname,unit,type,costprice\n"
                  + "\t\t    \tcount\n"
                  + "\t\t    \tlist\n"
                  + "\t\t    \tedit - productname,unit,type,costprice\n"
                  + "\t\t    \tdelete - y/n with productname or productid\n"
                  + "\t       \n"
                  + "\t       unit\n"
                  + "\t \t\tcreate - name, code, description, isdividable\n"
                  + "\t \t\tlist -\n"
                  + "\t \t\tedit - name, code, description, isdividable\n"
                  + "\t \t\tdelete - code\n"
                  + "\t       \n"
                  + "\t       stock\n"
                  + "\t    \t\tupdate - code, quantity\n"
                  + "\t       \n"
                  + "\t       price\n"
                  + "\t    \t\tupdate - code, price";
          System.out.println(help);
          break;
        default:
          System.out.println("Invalid Command! Not Found!");
      }
    } while (true);
  }
}
