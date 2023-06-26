package Servlets;

import Entity.Sales;
import Entity.SalesItem;
import Service.SalesService;
import Service.SalesServiceImplementation;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@WebServlet("/createSales")
public class createSales extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		SalesService salesService = new SalesServiceImplementation();
		double grandTotal;
		HttpSession session = request.getSession();
		String date =  request.getParameter("dateValue");
		Set<SalesItem> salesItemSet;
		salesItemSet = (Set<SalesItem>)session.getAttribute("selectedList");
		List<SalesItem> salesItemList = new ArrayList<>(salesItemSet);
		grandTotal = salesItemList.stream().mapToDouble(salesItem -> salesItem.getProduct().getPrice() * salesItem.getQuantity()).sum();
		Sales sales = new Sales(date, salesItemList, grandTotal);
		try{
			salesService.create(sales);
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		session.removeAttribute("selectedList");
		response.sendRedirect("salesList?Success="+"Sales Added Successfully!");
	}
}
