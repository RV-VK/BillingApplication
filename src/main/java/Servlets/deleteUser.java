package Servlets;

import Service.UserService;
import Service.UserServiceImplementation;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/deleteUser")
public class deleteUser extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		UserService userService = new UserServiceImplementation();
		String username = request.getParameter("username");
		try{
			userService.delete(username);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		response.sendRedirect("userList");
	}
}
