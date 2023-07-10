package Entity;

import java.util.Objects;

public class 	SalesItem {
	private Product product;
	private Float quantity;
	private Double unitSalesPrice;

	public SalesItem() {
	}

	public SalesItem(String code, float quantity, double unitSalesPrice) {
		this.product = new Product(code);
		this.quantity = quantity;
		this.unitSalesPrice = unitSalesPrice;
	}

	public SalesItem(String code, String name, float quantity, double unitSalesPrice) {
		this.product = new Product(code, name);
		this.quantity = quantity;
		this.unitSalesPrice = unitSalesPrice;
	}


	public SalesItem(Product product, float quantity, double unitSalesPrice) {
		this.product = product;
		this.quantity = quantity;
		this.unitSalesPrice = unitSalesPrice;
	}

	public SalesItem(Product product, float quantity) {
		this.product = product;
		this.quantity = quantity;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Float getQuantity() {
		return quantity;
	}

	public void setQuantity(Float quantity) {
		this.quantity = quantity;
	}

	public Double getUnitSalesPrice() {
		return unitSalesPrice;
	}

	public void setUnitSalesPrice(Double unitSalesPrice) {
		this.unitSalesPrice = unitSalesPrice;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		SalesItem salesItem = (SalesItem)o;
		return product.equals(salesItem.product) && quantity.equals(salesItem.quantity) && unitSalesPrice.equals(salesItem.unitSalesPrice);
	}

	@Override
	public int hashCode() {
		return Objects.hash(product, quantity, unitSalesPrice);
	}

	@Override
	public String toString() {
		return "SalesItem{" + "product=" + product + ", quantity=" + quantity + ", unitSalesPrice=" + unitSalesPrice + '}';
	}
}
