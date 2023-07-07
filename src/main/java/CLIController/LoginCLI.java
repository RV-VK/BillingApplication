package CLIController;

import DAO.ApplicationErrorException;
import DAO.PageCountOutOfBoundsException;
import DAO.UnitCodeViolationException;
import Entity.User;
import Service.InvalidTemplateException;
import Service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Scanner;

@Component
public class LoginCLI {
	private static final Scanner scanner = new Scanner(System.in);
	private static String userName;
	private static String passWord;
	private static String firstName;
	private static String lastName;
	private static Long phoneNumber;
	@Autowired
	private AdminMain adminMain;
	@Autowired
	private SalesMain salesMain;
	@Autowired
	private PurchaseMain purchaseMain;

	/**
	 * Login View Main
	 **/
	public static void main(String[] args) throws SQLException, ApplicationErrorException, PageCountOutOfBoundsException, UnitCodeViolationException, InvalidTemplateException {
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppDependencyConfig.class);
		LoginCLI loginCLI = applicationContext.getBean(LoginCLI.class);
		LoginService loginService = applicationContext.getBean(LoginService.class);
		if(! loginService.checkIfInitialSetup()) {

			System.out.println("""
					\t\t\t\t\t\t*********************************************************
					\t\t\t\t\t\t*********************   WELCOME   ***********************
					\t\t\t\t\t\t*********************************************************
					""");
			System.out.println("> Welcome to the Billing software setup. You have to create admin user to continue with the setup.\n\n");
			do {
				System.out.println("""
						>> create user using following template
						>>  usertype, username,  password, first name, last name, phone number
						\tusertype - text, purchase/sales, mandatory
						\tusername - text, min 3 - 30 char, mandatory
						\tpassword - text, alphanumeric, special char, min 8 char, mandatory
						\tfirstname - text, mandatory with 3 to 30 chars
						\tlastname  - text, optional
						\tphone - number, mandatory, ten digits, digit start with 9/8/7/6\t\t\t\t
						""");
				System.out.println("\n");
				System.out.print(">> Enter the Admin Username: ");
				userName = scanner.nextLine();
				do {
					System.out.print(">> Enter the password: ");
					passWord = scanner.nextLine();
					System.out.print(">> Renter the password: ");
					System.out.println("Passwords do not Match!-- Re-enter");
				} while(! scanner.nextLine().equals(passWord));
				System.out.print(">> Enter Your Firstname: ");
				firstName = scanner.nextLine();
				System.out.print(">> Enter Your LastName: ");
				lastName = scanner.nextLine();
				System.out.print(">> Enter your phone-number: ");
				while(true) {
					try {
						phoneNumber = Long.parseLong(scanner.nextLine());
						break;
					} catch(Exception e) {
						System.out.println("Phone number must be numeric! -- Re-enter");
					}
				}
				User user = new User("Admin", userName, passWord, firstName, lastName, phoneNumber);
				User createdUser;
				try {
					createdUser = loginService.createUser(user);
				} catch(Exception e) {
					System.out.println(e.getMessage());
					continue;
				}
				if(createdUser != null) {
					System.out.println(">> Admin Created Successfully!!");
					System.out.println(">> You need to Login to Continue to the Billing Software Setup");
					loginCLI.Login();
				}
			} while(true);
		} else {
			loginCLI.Login();
		}
	}

	/**
	 * The Presentation Layer of the Login function.
	 *
	 * @throws PageCountOutOfBoundsException Custom Exception thrown when a non-existing page is given as input in Pageable List.
	 * @throws ApplicationErrorException     Exception thrown due to Persistence problems.
	 */
	public void Login() throws PageCountOutOfBoundsException, ApplicationErrorException, UnitCodeViolationException, SQLException, InvalidTemplateException {
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppDependencyConfig.class);
		LoginService loginService = applicationContext.getBean(LoginService.class);
		System.out.println("Please Login To continue to the Billing Software!!\n");
		do {
			System.out.print(">> Enter UserName: ");
			userName = scanner.nextLine();
			System.out.print(">> Enter the Password: ");
			passWord = scanner.nextLine();
			User user;
			try {
				user = loginService.login(userName, passWord);
			} catch(Exception e) {
				System.out.println(e.getMessage());
				return;
			}
			if(user != null) {
				System.out.print("\n\n\n\t\t\t\t\t____________WELCOME " + userName + " TO THE BILLING SOFTWARE_____________________");
				//Split User control Here
				if(user.getUserType().equalsIgnoreCase("Admin")) {
					adminMain.AdminView(userName);
				} else if(user.getUserType().equalsIgnoreCase("Sales")) {
					salesMain.SalesView();
				} else if(user.getUserType().equalsIgnoreCase("Purchase")) {
					purchaseMain.PurchaseView();
				}
			} else {
				System.out.println(">> Login credentials invalid ! You should try with a valid user name and password. If you have any issues contact software administrator.");
			}
		} while(true);
	}

}

