package Servlets;

import Entity.User;
import Service.UserService;
import Service.UserServiceImplementation;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/createUser")
public class createUser extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserService userService = new UserServiceImplementation();
		String userName = request.getParameter("username");
		String userType = request.getParameter("usertype");
		String passWord = request.getParameter("password");
		String firstName = request.getParameter("firstname");
		String lastname = request.getParameter("lastname");
		long phoneNumber = Long.parseLong(request.getParameter("phonenumber"));
		User user = new User(userType, userName, passWord, firstName, lastname, phoneNumber );
		RequestDispatcher errorRequestDispatcher = request.getRequestDispatcher("userForm.jsp?id=");
		try{
			userService.create(user);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			request.setAttribute("Error",e.getMessage());
			request.setAttribute("user",user);
			errorRequestDispatcher.forward(request,response);
		}
		response.sendRedirect("userList?Success="+"User Created Successfully!!&page="+request.getParameter("page"));

	}
}
