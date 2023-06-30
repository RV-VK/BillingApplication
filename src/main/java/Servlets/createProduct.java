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

@WebServlet("/createProduct")
public class createProduct extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ProductService productService = new ProductServiceImplementation();
		String code = request.getParameter("code");
		String name = request.getParameter("name");
		String unitcode = request.getParameter("unitcode");
		String type = request.getParameter("type");
		float stock = Float.parseFloat(request.getParameter("stock"));
		double price = Double.parseDouble(request.getParameter("price"));
		Product product = new Product(code, name, unitcode, type, stock, price);
		RequestDispatcher errorRequestDispatcher = request.getRequestDispatcher("productForm.jsp");
		try{
			productService.create(product);
		 } catch(Exception e)
		{
			System.out.println(e.getMessage());
			request.setAttribute("Error",e.getMessage());
			request.setAttribute("product",product);
			errorRequestDispatcher.forward(request,response);
		}
		response.sendRedirect("productList?Success="+"Product Created Successfully!!&page="+request.getParameter("page"));
	}
}
