package org.example.RestController;

import org.example.DAO.ApplicationErrorException;
import org.example.DAO.PageCountOutOfBoundsException;
import org.example.Entity.Product;
import org.example.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(path = "/products")
public class ProductController {
	@Autowired
	private ProductService productService;

	@GetMapping(path = "/", produces = "application/json")
	public List<Product> getProducts() throws PageCountOutOfBoundsException, ApplicationErrorException {
		HashMap<String, String> listAttributes = new HashMap<>();
		listAttributes.put("Pagelength", String.valueOf(Integer.MAX_VALUE));
		listAttributes.put("Pagenumber", "1");
		listAttributes.put("Attribute", "id");
		listAttributes.put("Searchtext", null);

		return productService.list(listAttributes);
	}
}
