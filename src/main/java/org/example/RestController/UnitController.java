package org.example.RestController;

import org.example.DAO.ApplicationErrorException;
import org.example.Entity.Unit;
import org.example.Service.InvalidTemplateException;
import org.example.Service.UnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UnitController {
	private static final Logger logger = LoggerFactory.getLogger(UnitController.class);
	@Autowired
	private UnitService unitService;

	@GetMapping(path = "/units", produces = "application/json")
	public List<Unit> getAll() throws ApplicationErrorException {
		List<Unit> unitList;
		try {
			unitList = unitService.list();
		} catch(ApplicationErrorException exception) {
			logger.error("Error retrieving data from the database!, {}", exception.getMessage());
			throw exception;
		}
		logger.info("List Returned Successfully!");
		return unitList;
	}

	@PostMapping(path = "/unit", produces = "application/json")
	public Unit add(@RequestBody Unit unit) throws Exception {
		Unit createdUnit;
		try {
			createdUnit = unitService.create(unit);
		} catch(Exception exception) {
			logger.error("Unit Creation Failed!, {}", exception.getMessage());
			throw exception;
		}
		logger.info("Unit Created Successfully!");
		return createdUnit;
	}

	@PutMapping(path = "/unit", produces = "application/json")
	public Unit edit(@RequestBody Unit unit) throws Exception {
		Unit editedUnit;
		try {
			editedUnit = unitService.edit(unit);
		} catch(Exception exception) {
			logger.error("Unit Edit failed!, {}", exception.getMessage());
			throw exception;
		}
		if(editedUnit == null) {
			logger.error("Unit Edit failed!, Given unit doesnt exists : {}", unit.getId());
			throw new InvalidTemplateException("The Id doesnt exist to edit! Please give An Existing Id");
		} else {
			logger.info("Unit Edited Successfully!");
			return editedUnit;
		}
	}

	@DeleteMapping(path = "/deleteUnit/{unitCode}", produces = "application/json")
	public Integer delete(@PathVariable String unitCode) throws Exception {
		Integer statusCode;
		try {
			statusCode = unitService.delete(unitCode);
		} catch(Exception exception) {
			logger.error("Unit deletion failed!, {}", exception.getMessage());
			throw exception;
		}
		if(statusCode == 1) {
			logger.info("Unit deleted Successfully!");
			return statusCode;

		} else {
			logger.error("Unit deletion failed!, Given Unit Code doesnt exists : {}", unitCode);
			throw new InvalidTemplateException("UnitCode doesnt Exists!");
		}
	}
}
