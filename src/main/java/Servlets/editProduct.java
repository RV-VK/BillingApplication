package Servlets;

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

@WebServlet("/editProduct")
public class editProduct extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ProductService productService = new ProductServiceImplementation();
		int id = Integer.parseInt(request.getParameter("id"));
		String editCode = request.getParameter("editCode");
		String code = request.getParameter("code");
		String name = request.getParameter("name");
		String unitcode = request.getParameter("unitcode");
		String type = request.getParameter("type");
		float stock = Float.parseFloat(request.getParameter("stock"));
		double price = Double.parseDouble(request.getParameter("price"));
		Product product = new Product(id ,code, name, unitcode, type, stock, price);
		RequestDispatcher errorRequestDispatcher = request.getRequestDispatcher("productForm.jsp?editCode="+editCode);
		try{
			productService.edit(product);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			request.setAttribute("Error",e.getMessage());
			errorRequestDispatcher.forward(request, response);
		}
		response.sendRedirect("productList?Success="+"Product edited Successfully!&page="+request.getParameter("page"));
	}
}
