package Servlets;

import Entity.Purchase;
import Entity.PurchaseItem;
import Service.PurchaseService;
import Service.PurchaseServiceImplementation;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@WebServlet("/createPurchase")
public class createPurchase extends HttpServlet {
	private HttpServletRequest request;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		PurchaseService purchaseService = new PurchaseServiceImplementation();
		double grandTotal;
		int invoice;
		String date;
		HttpSession session = request.getSession();
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("purchase.jsp");
		date = request.getParameter("dateValue");
		invoice = Integer.parseInt(request.getParameter("invoiceValue"));
		Set<PurchaseItem> purchaseItemSet;
		purchaseItemSet = (Set<PurchaseItem>)session.getAttribute("selectedList");
		List<PurchaseItem> purchaseItemList = new ArrayList<>(purchaseItemSet);
		grandTotal = purchaseItemList.stream().mapToDouble(purchaseItem -> purchaseItem.getUnitPurchasePrice() * purchaseItem.getQuantity()).sum();
		Purchase purchase = new Purchase(date, invoice, purchaseItemList, grandTotal);
		try {
			purchaseService.create(purchase);
		} catch(Exception e){
			System.out.println(e.getMessage());
			request.setAttribute("Error",e.getMessage());
			requestDispatcher.forward(request, response);
		}
		session.removeAttribute("selectedList");
		response.sendRedirect("purchaseList?Success="+"Purchase Added Successfully!");
	}
}
