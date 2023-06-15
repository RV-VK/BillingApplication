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
import java.util.ArrayList;
import java.util.List;


@WebServlet("/unitList")
public class unitList extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int page = 1;
		int recordsPerPage = 5;
		int noOfRecords;
		UnitService unitService = new UnitServiceImplementation();
		List<Unit> unitList = new ArrayList<>();
		List<Unit> unitSubList;
		if(request.getParameter("page") != null)
			page = Integer.parseInt(request.getParameter("page"));
		try{
			unitList = unitService.list();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		noOfRecords = unitList.size();
		int from = (page * recordsPerPage) - recordsPerPage;
		int to = page * recordsPerPage;
		int noOfPages = (int)Math.ceil(noOfRecords * 1.0 / recordsPerPage);
		unitSubList = unitList.subList(from, Math.min(to, unitList.size()));
		request.setAttribute("unitList",unitSubList);
		request.setAttribute("noOfPages",noOfPages);
		request.setAttribute("currentPage",page);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("unit.jsp");
		requestDispatcher.forward(request, response);

	}

}
