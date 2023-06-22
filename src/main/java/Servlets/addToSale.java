package Servlets;

import DAO.ApplicationErrorException;
import DAO.UnitDAO;
import Entity.Product;
import Entity.SalesItem;
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


@WebServlet("/addToSale")
public class addToSale extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Set<SalesItem> selectedList = new HashSet<>();
		double grandTotal;
		float quantity = 0;
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("sales.jsp");
		HttpSession session = request.getSession();
		if(session.getAttribute("selectedList") != null)
			selectedList = (Set<SalesItem>)session.getAttribute("selectedList");
		String parameter = request.getParameter("searchBar");
		if(request.getParameter("quantity") != null)
			quantity = Float.parseFloat(request.getParameter("quantity"));
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

			if(product.getAvailableQuantity() < quantity) {
				request.setAttribute("Error", product.getName() + " is of lesser Stock!");
			} else {
				try {
					if(!validate(product, quantity)) {
						request.setAttribute("Error", product.getName() + " is not a Dividable Entity");
					} else {
						selectedList.add(new SalesItem(product, quantity, product.getPrice()));
					}
				} catch(ApplicationErrorException e) {
					System.out.println(e.getMessage());
				}
			}
		}
		if(request.getParameter("deleteCode") != null) {
			String deleteCode = request.getParameter("deleteCode");
			selectedList.removeIf(salesItem -> salesItem.getProduct().getCode().equals(deleteCode));
		}
		grandTotal = selectedList.stream()
				.mapToDouble(salesItem -> salesItem.getProduct().getPrice() * salesItem.getQuantity())
				.sum();
		request.setAttribute("selectedList", selectedList);
		request.setAttribute("grandTotal", grandTotal);
		requestDispatcher.forward(request, response);
	}

	public boolean validate(Product product, float quantity) throws ApplicationErrorException {
		UnitDAO unitDAO = new UnitDAO();
		boolean isDividable;
		isDividable = unitDAO.findByCode(product.getunitcode()).getIsDividable();
		return isDividable || quantity % 1 == 0;
	}
}