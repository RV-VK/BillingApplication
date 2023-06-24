package Servlets;

import DAO.ProductDAO;
import Entity.Product;
import Service.ProductService;
import Service.ProductServiceImplementation;
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

@WebServlet("/productList")
public class productList extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int page = 1;
		int recordsPerPage = 5;
		int noOfRecords = 0;
		String searchText;
		String attribute;
		HashMap<String, String> listAttributes = new HashMap<>();
		ProductService productService = new ProductServiceImplementation();
		ProductDAO productDAO = new ProductDAO();
		List<Product> productList = new ArrayList<>();
		List<Product> productSubList;
		if(request.getParameter("attribute") == null || request.getParameter("attribute").isEmpty())
			attribute = null;
		else
			attribute = request.getParameter("attribute");
		if(request.getParameter("searchText") == null || request.getParameter("searchText").isEmpty())
			searchText = null;
		else searchText = request.getParameter("searchText");
		System.out.println("Attribute : " + attribute);
		System.out.println("Searchtext : " + searchText);
		if(request.getParameter("page") != null)
			page = Integer.parseInt(request.getParameter("page"));
		if(attribute == null && searchText != null) {
			listAttributes.put("Attribute", null);
			listAttributes.put("Searchtext", searchText);
			listAttributes.put("Pagelength", null);
			listAttributes.put("Pagenumber", null);
			try {
				productList = productService.list(listAttributes);
			} catch(Exception e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
			noOfRecords = productList.size();
		}
		else {
			if(attribute == null) {
				listAttributes.put("Attribute", "id");
			} else {
				listAttributes.put("Attribute", attribute);
			}
			listAttributes.put("Searchtext", searchText);
			try {
				noOfRecords = productDAO.count(listAttributes.get("Attribute"), searchText);
				listAttributes.put("Pagelength", String.valueOf(noOfRecords));
				listAttributes.put("Pagenumber", "1");
				productList = productService.list(listAttributes);
			} catch(Exception e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		}
		int from = (page * recordsPerPage) - recordsPerPage;
		int to = page * recordsPerPage;
		int noOfPages = (int)Math.ceil(noOfRecords * 1.0 / recordsPerPage);
		productSubList = productList.subList(from, (Math.min(to, productList.size())));
		request.setAttribute("productList", productSubList);
		request.setAttribute("noOfPages", noOfPages);
		request.setAttribute("currentPage", page);
		request.setAttribute("Attribute", listAttributes.get("Attribute"));
		request.setAttribute("Searchtext", searchText);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("product.jsp");
		requestDispatcher.forward(request, response);
	}
}
