package Servlets;

import Entity.User;
import Service.LoginService;
import Service.LoginServiceImplementation;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


@WebServlet("/createAdmin")
public class Register extends HttpServlet {
 public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	 LoginService loginService = new LoginServiceImplementation();
	 String userName = request.getParameter("username");
	 String password = request.getParameter("password");
	 String firstName = request.getParameter("firstname");
	 String lastName = request.getParameter("lastname");
	 long phoneNumber = Long.parseLong(request.getParameter("phonenumber"));
	 User user = new User("Admin", userName, password, firstName, lastName, phoneNumber);
	 try{
		 loginService.createUser(user);
	 } catch(Exception e) {
		 request.setAttribute("Error","Error");
		 RequestDispatcher requestDispatcher = request.getRequestDispatcher("index.jsp");
		 requestDispatcher.forward(request,response);
	 }
	 request.setAttribute("Error","Success");
	 RequestDispatcher requestDispatcher = request.getRequestDispatcher("index.jsp");
	 requestDispatcher.forward(request,response);
 }
}
