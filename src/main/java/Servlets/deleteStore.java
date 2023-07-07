package Servlets;

import Service.StoreService;
import Service.StoreServiceImplementation;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/deleteStore")
public class deleteStore extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StoreService storeService = new StoreServiceImplementation();
		HttpSession session = request.getSession();
		String username = (String)session.getAttribute("username");
		String password = request.getParameter("password");
		int rowsAffected = 0;
		RequestDispatcher errorRequestDispatcher = request.getRequestDispatcher("store.jsp");
		try{
			rowsAffected = storeService.delete(username, password);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		if(rowsAffected == -1) {
			request.setAttribute("Error","Invalid Admin Password!");
			errorRequestDispatcher.forward(request, response);
		}
		else if(rowsAffected == 1) {
			response.sendRedirect("index.jsp");
		}
	}
}
