package Servlets;

import Entity.Store;
import Service.StoreService;
import Service.StoreServiceImplementation;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


@WebServlet("/createStore")
public class createStore extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StoreService storeService = new StoreServiceImplementation();
		String name = request.getParameter("name");
		long phoneNumber = Long.parseLong(request.getParameter("number"));
		String address = request.getParameter("address");
		String GSTCode = request.getParameter("GSTCode");
		Store store = new Store(name, phoneNumber, address, GSTCode);
		RequestDispatcher errorRequestDispatcher = request.getRequestDispatcher("storeForm.jsp");
		try{
			storeService.create(store);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			request.setAttribute("message",e.getMessage());
			errorRequestDispatcher.forward(request, response);
		}
		response.sendRedirect("adminDashboard.jsp?Success="+"Store Created Successfully!");
	}

}
