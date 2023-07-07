package Entity;

import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class Product {
	private Integer id;
	private String code;
	private String name;
	private String unitcode;
	private String type;
	private Float stock;
	private Double price;

	public Product() {
	}

	public Product(int id, String code, String name, String unitcode, String type, float availableQuantity, double price) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.unitcode = unitcode;
		this.type = type;
		this.stock = availableQuantity;
		this.price = price;
	}


	public Product(String code, String name, String unitcode, String type, float availableQuantity, double price) {
		this.code = code;
		this.name = name;
		this.unitcode = unitcode;
		this.type = type;
		this.stock = availableQuantity;
		this.price = price;
	}

	public Product(String code) {
		this.code = code;
	}

	public Product(String code, String name) {
		this.code = code;
		this.name = name;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getunitcode() {
		return unitcode;
	}

	public void setunitcode(String unitcode) {
		this.unitcode = unitcode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public float getAvailableQuantity() {
		return stock;
	}

	public void setAvailableQuantity(float availableQuantity) {
		this.stock = availableQuantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Product product = (Product)o;
		return id.equals(product.id) && code.equals(product.code) && name.equals(product.name) && unitcode.equals(product.unitcode) && type.equals(product.type) && stock.equals(product.stock) && price.equals(product.price);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, code, name, unitcode, type, stock, price);
	}

	@Override
	public String toString() {
		return "Product{" + "id=" + id + ", code='" + code + '\'' + ", name='" + name + '\'' + ", unitcode='" + unitcode + '\'' + ", type='" + type + '\'' + ", availableQuantity=" + stock + ", price=" + price + '}';
	}

}