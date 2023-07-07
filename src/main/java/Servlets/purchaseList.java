package Servlets;

import Entity.Purchase;
import Service.PurchaseService;
import Service.PurchaseServiceImplementation;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@WebServlet("/purchaseList")
public class purchaseList extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int page = 1;
		int recordsPerPage = 5;
		int noOfRecords = 0;
		String searchText;
		String attribute;
		HashMap<String, String> listAttributes = new HashMap<>();
		PurchaseService purchaseService = new PurchaseServiceImplementation();
		List<Purchase> purchaseList = new ArrayList<>();
		List<Purchase> purchaseSubList;
		if(request.getParameter("attribute") == null || request.getParameter("attribute").isEmpty()) attribute = null;
		else attribute = request.getParameter("attribute");
		if(request.getParameter("searchText") == null || request.getParameter("searchText").isEmpty())
			searchText = null;
		else searchText = request.getParameter("searchText");
		System.out.println("Attribute : " + attribute);
		System.out.println("Searchtext : " + searchText);
		if(request.getParameter("page") != null) page = Integer.parseInt(request.getParameter("page"));
		if(attribute == null && searchText != null) {
			listAttributes.put("Attribute", null);
			listAttributes.put("Searchtext", searchText);
			listAttributes.put("Pagelength", null);
			listAttributes.put("Pagenumber", null);
			try {
				purchaseList = purchaseService.list(listAttributes);
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
			noOfRecords = purchaseList.size();
		} else {
			if(attribute == null) {
				listAttributes.put("Attribute", "id");
			} else {
				listAttributes.put("Attribute", attribute);
			}
			listAttributes.put("Searchtext", searchText);
			try {
				listAttributes.put("Pagelength", String.valueOf(Integer.MAX_VALUE));
				listAttributes.put("Pagenumber", "1");
				purchaseList = purchaseService.list(listAttributes);
				noOfRecords = purchaseList.size();
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		int from = (page * recordsPerPage) - recordsPerPage;
		int to = page * recordsPerPage;
		int noOfPages = (int)Math.ceil(noOfRecords * 1.0 / recordsPerPage);
		purchaseSubList = purchaseList.subList(from, (Math.min(to, purchaseList.size())));
		request.setAttribute("purchaseList", purchaseSubList);
		request.setAttribute("noOfPages", noOfPages);
		request.setAttribute("currentPage", page);
		request.setAttribute("Attribute", listAttributes.get("Attribute"));
		request.setAttribute("Searchtext", searchText);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("purchaseList.jsp");
		requestDispatcher.forward(request, response);
	}
}
