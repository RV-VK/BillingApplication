package Servlets;

import Entity.Unit;
import Service.UnitService;
import Service.UnitServiceImplementation;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/createUnit")
public class createUnit extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UnitService unitService = new UnitServiceImplementation();
		String name = request.getParameter("name");
		String code = request.getParameter("code");
		String description = request.getParameter("description");
		boolean isDividable = Boolean.parseBoolean(request.getParameter("isdividable"));
		Unit unit = new Unit(name, code, description, isDividable);
		RequestDispatcher errorRequestDispatcher = request.getRequestDispatcher("unitForm.jsp");
		try{
			unitService.create(unit);
		}catch(Exception e) {
			System.out.println(e.getMessage());
			request.setAttribute("Error",e.getMessage());
			request.setAttribute("unit",unit);
			errorRequestDispatcher.forward(request, response);
		}
		response.sendRedirect("unitList?Success="+"Unit created Successfully!!&page="+request.getParameter("page"));
	}
}
