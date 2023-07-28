package org.example.RestController;


import org.example.DAO.ApplicationErrorException;
import org.example.Entity.Store;
import org.example.Service.InvalidTemplateException;
import org.example.Service.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
public class StoreController {

	private final Logger logger = LoggerFactory.getLogger(StoreController.class);
	@Autowired
	private StoreService storeService;

	@GetMapping(path = "/store", produces = "application/json")
	public Store view() throws ApplicationErrorException {
		Store store;
		try {
			store = storeService.view();
		} catch(ApplicationErrorException exception) {
			logger.error("Error retrieving data from the database, {} ", exception.getMessage());
			throw exception;
		}
		logger.info("Store returned successfully!");
		return store;
	}

	@PostMapping(path = "/store", produces = "application/json")
	public Store add(@RequestBody Store store) throws SQLException, ApplicationErrorException, InvalidTemplateException {
		Store createdStore;
		try {
			createdStore = storeService.create(store);
		} catch(SQLException | ApplicationErrorException exception) {
			logger.error("Store Creation failed, {}", exception.getMessage());
			throw exception;
		}
		if(createdStore == null) {
			logger.warn("Store Creation failed!, Store already Exists!");
			throw new InvalidTemplateException("Store Already Exists!");
		} else {
			logger.info("Store Created successfully! Created Store : {} ", createdStore);
			return createdStore;
		}
	}

	@PutMapping(path = "/store", produces = "application/json")
	public Store edit(@RequestBody Store store) throws SQLException, ApplicationErrorException, InvalidTemplateException {
		Store editedStore;
		try {
			editedStore = storeService.edit(store);
		} catch(Exception exception) {
			logger.error("Store edit failed!, {} ", exception.getMessage());
			throw exception;
		}
		logger.info("Store edited Successfully! Edited Store: {} ", editedStore);
		return editedStore;
	}

	@DeleteMapping(path = "/deleteStore/{userName}/{passWord}", produces = "application/json")
	public Integer delete(@PathVariable String userName, @PathVariable String passWord) throws ApplicationErrorException, InvalidTemplateException {
		Integer statusCode;
		try {
			statusCode = storeService.delete(userName, passWord);
		} catch(ApplicationErrorException exception) {
			logger.error("Store deletion failed!");
			throw exception;
		}
		if(statusCode == - 1) {
			logger.error("Store deletion failed!, Invalid Admin Password.");
			throw new InvalidTemplateException("Invalid Admin Password!");
		} else if(statusCode == 0) {
			logger.warn("Warning! No Store exists to delete!");
			throw new InvalidTemplateException("No Store Exists to delete!");
		} else {
			logger.info("Store deleted Successfully!");
			return statusCode;
		}
	}


}
