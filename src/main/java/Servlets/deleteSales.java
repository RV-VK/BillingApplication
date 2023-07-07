package Servlets;

import Service.SalesService;
import Service.SalesServiceImplementation;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/deleteSales")
public class deleteSales extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		SalesService salesService = new SalesServiceImplementation();
		String id = request.getParameter("id");
		try{
			salesService.delete(id);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		response.sendRedirect("listSales");
	}
}
