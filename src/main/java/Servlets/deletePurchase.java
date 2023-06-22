package Servlets;

import Service.PurchaseService;
import Service.PurchaseServiceImplementation;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/deletePurchase")
public class deletePurchase extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PurchaseService purchaseService = new PurchaseServiceImplementation();
		String invoice = request.getParameter("invoice");
		try{
			purchaseService.delete(invoice);
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		response.sendRedirect("purchaseList");
	}
}
