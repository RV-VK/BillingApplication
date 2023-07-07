package Servlets;

import DAO.ApplicationErrorException;
import Service.ProductService;
import Service.ProductServiceImplementation;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


@WebServlet("/deleteProduct")
public class deleteProduct extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ProductService productService = new ProductServiceImplementation();
		String id = request.getParameter("id");
		try {
			productService.delete(id);
		} catch(ApplicationErrorException e) {
			System.out.println(e.getMessage());
		}
		response.sendRedirect("productList");
	}

}
