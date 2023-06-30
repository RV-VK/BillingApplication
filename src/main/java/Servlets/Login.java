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
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class Login extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		LoginService loginService = new LoginServiceImplementation();
		String userName = request.getParameter("username");
		String passWord = request.getParameter("password");
		User user = new User();
		try {
			user = loginService.login(userName, passWord);
		} catch(Exception e) {
			System.out.println("reached");
			request.setAttribute("Error", "Error");
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("index.jsp");
			requestDispatcher.forward(request, response);
		}
		HttpSession session = request.getSession();
		if(user != null && user.getUserType().equalsIgnoreCase("Admin")) {
			session.setAttribute("username", user.getUserName());
			session.setAttribute("userType", "Admin");
			session.setAttribute("userId", user.getId());
			response.sendRedirect("adminDashboard.jsp");
		} else if(user != null && user.getUserType().equals("Sales")) {
			session.setAttribute("userType", "Sales");
			session.setAttribute("username", user.getUserName());
			session.setAttribute("userId", user.getId());
			response.sendRedirect("salesUserDashBoard.jsp");
		} else if(user != null && user.getUserType().equals("Purchase")) {
			session.setAttribute("userType", "Purchase");
			session.setAttribute("username", user.getUserName());
			session.setAttribute("userId", user.getId());
			response.sendRedirect("purchaseUserDashBoard.jsp");
		} else {
			request.setAttribute("Error", "Error");
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("index.jsp");
			requestDispatcher.forward(request, response);
		}
	}
}

