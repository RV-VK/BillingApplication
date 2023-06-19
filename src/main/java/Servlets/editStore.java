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


@WebServlet("/editStore")
public class editStore extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StoreService storeService = new StoreServiceImplementation();
		String name = request.getParameter("name");
		long phoneNumber = Long.parseLong(request.getParameter("phoneNumber"));
		String address = request.getParameter("address");
		String gstCode = request.getParameter("gstNumber");
		Store store = new Store(name, phoneNumber, address, gstCode);
		RequestDispatcher errorRequestDispatcher = request.getRequestDispatcher("store.jsp");
		try{
			storeService.edit(store);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			request.setAttribute("Error",e.getMessage());
			errorRequestDispatcher.forward(request, response);
		}
		response.sendRedirect("store.jsp?Success=Store Edited Successfully!");
	}
}
