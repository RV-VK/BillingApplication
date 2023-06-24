package Servlets;

import DAO.ApplicationErrorException;
import DAO.UnitDAO;
import Entity.Product;
import Entity.PurchaseItem;
import Service.ProductService;
import Service.ProductServiceImplementation;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.*;

@WebServlet("/addToPurchase")
public class addToPurchase extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Set<PurchaseItem> selectedList = new HashSet<>();
		double grandTotal;
		float quantity = 0;
		double price = 0;
		int invoice = 0;
		String date = null;
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("purchase.jsp");
		HttpSession session = request.getSession();
		if(session.getAttribute("selectedList") != null)
			selectedList = (Set<PurchaseItem>)session.getAttribute("selectedList");
		String parameter = request.getParameter("searchBar");
		if(request.getParameter("quantity") != null) quantity = Float.parseFloat(request.getParameter("quantity"));
		if(request.getParameter("price") != null) price = Double.parseDouble(request.getParameter("price"));
		if(request.getParameter("invoice") != null)
			invoice = Integer.parseInt(request.getParameter("invoice"));
		if(request.getParameter("currentDate") != null)
			date = request.getParameter("currentDate");
		Product product;
		if(parameter != null) {
			HashMap<String, String> listAttributes = new HashMap<>();
			listAttributes.put("Attribute", "id");
			listAttributes.put("Searchtext", null);
			listAttributes.put("Pagelength", String.valueOf(Integer.MAX_VALUE));
			listAttributes.put("Pagenumber", "1");
			List<Product> productList = new ArrayList<>();
			ProductService productService = new ProductServiceImplementation();
			try {
				productList = productService.list(listAttributes);
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
			product = productList.stream().filter(product1 -> parameter.equals(product1.getName())).findAny().orElse(null);
			if(product == null) {
				product = productList.stream().filter(product1 -> parameter.equals(product1.getCode())).findAny().orElse(null);
			}
			try {
				if(! validate(product, quantity)) {
					request.setAttribute("Error", product.getName() + " is not a Dividable Entity");
				} else {
					selectedList.add(new PurchaseItem(product, quantity, price));
				}
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		if(request.getParameter("deleteCode") != null) {
			String deleteCode = request.getParameter("deleteCode");
			selectedList.removeIf(purchaseItem -> purchaseItem.getProduct().getCode().equals(deleteCode));
		}
		grandTotal = selectedList.stream().mapToDouble(purchaseItem -> purchaseItem.getUnitPurchasePrice() * purchaseItem.getQuantity()).sum();
		request.setAttribute("selectedList", selectedList);
		request.setAttribute("grandTotal", grandTotal);
		request.setAttribute("invoice", invoice);
		request.setAttribute("date", date);
		requestDispatcher.forward(request, response);
	}

	public boolean validate(Product product, float quantity) throws ApplicationErrorException {
		UnitDAO unitDAO = new UnitDAO();
		boolean isDividable;
		isDividable = unitDAO.findByCode(product.getunitcode()).getIsDividable();
		return isDividable || quantity % 1 == 0;
	}

}
