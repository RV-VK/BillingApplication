package Servlets;

import Service.UnitService;
import Service.UnitServiceImplementation;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/deleteUnit")
public class deleteUnit extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		UnitService unitService = new UnitServiceImplementation();
		String code = request.getParameter("code");
		try{
			unitService.delete(code);
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		response.sendRedirect("unitList");
	}
}
