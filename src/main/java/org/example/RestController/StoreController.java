package org.example.RestController;


import org.example.DAO.ApplicationErrorException;
import org.example.Entity.Store;
import org.example.Service.InvalidTemplateException;
import org.example.Service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
public class StoreController {

	@Autowired
	private StoreService storeService;

	@GetMapping(path = "/store", produces = "application/json")
	public Store view() throws ApplicationErrorException {
		return storeService.view();
	}

	@PostMapping(path = "/store", produces = "application/json")
	public Store add(@RequestBody Store store) throws SQLException, ApplicationErrorException, InvalidTemplateException {
		Store createdStore = storeService.create(store);
		if(createdStore == null)
			throw new InvalidTemplateException("Store Already Exists!");
		else
			return createdStore;
	}

	@PutMapping(path = "/store", produces = "application/json")
	public Store edit(@RequestBody Store store) throws SQLException, ApplicationErrorException, InvalidTemplateException {
		return storeService.edit(store);
	}

	@DeleteMapping(path = "/deleteStore/{userName}/{passWord}", produces = "application/json")
	public Integer delete(@PathVariable String userName, @PathVariable String passWord) throws ApplicationErrorException, InvalidTemplateException {
		Integer statusCode;
		statusCode = storeService.delete(userName, passWord);
		if(statusCode == - 1) {
			throw new InvalidTemplateException("Invalid Admin Password!");
		} else if(statusCode == 0) {
			throw new InvalidTemplateException("No Store Exists to delete!");
		} else
			return statusCode;
	}


}
