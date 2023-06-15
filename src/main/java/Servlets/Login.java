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
import java.io.PrintWriter;

@WebServlet("/login")
public class Login extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		LoginService loginService = new LoginServiceImplementation();
		String userName = request.getParameter("username");
		String passWord = request.getParameter("password");
		PrintWriter out = response.getWriter();
		User user = new User();
		try{
			user = loginService.login(userName,passWord);
		} catch(Exception e)
		{
			System.out.println("reached");
			request.setAttribute("Error","Error");
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("index.jsp");
			requestDispatcher.forward(request,response);
		}
		if(user != null &&user.getUserType().equalsIgnoreCase("Admin")) {
			HttpSession session = request.getSession();
			session.setAttribute("username",user.getUserName());
			response.sendRedirect("adminDashboard.jsp");
		}
		else {
			request.setAttribute("Error","Error");
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("index.jsp");
			requestDispatcher.forward(request,response);
		}
	}
}
