package org.example.RestController;

import org.example.DAO.ApplicationErrorException;
import org.example.Entity.Unit;
import org.example.Service.InvalidTemplateException;
import org.example.Service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UnitController {
	@Autowired
	private UnitService unitService;

	@GetMapping(path = "/units", produces = "application/json")
	public List<Unit> getAll() throws ApplicationErrorException {
		return unitService.list();
	}

	@PostMapping(path = "/unit", produces = "application/json")
	public Unit add(@RequestBody Unit unit) throws Exception {
		return unitService.create(unit);
	}

	@PutMapping(path = "/unit", produces = "application/json")
	public Unit edit(@RequestBody Unit unit) throws Exception {
		return unitService.edit(unit);
	}

	@DeleteMapping(path = "/deleteUnit/{unitCode}", produces = "application/json")
	public Integer delete(@PathVariable String unitCode) throws Exception {
		Integer statusCode = unitService.delete(unitCode);
		if(statusCode == 1)
			return statusCode;
		else throw new InvalidTemplateException("UnitCode doesnt Exists!");
	}
}
