package Entity;

import java.util.Objects;


public class PurchaseItem {
	private Product product;
	private Float quantity;
	private Double unitPurchasePrice;

	public PurchaseItem() {
	}

	public PurchaseItem(String code, float quantity, double unitPurchasePrice) {
		this.product = new Product(code);
		this.quantity = quantity;
		this.unitPurchasePrice = unitPurchasePrice;
	}

	public PurchaseItem(String code, String name, float quantity, double unitPurchasePrice) {
		this.product = new Product(code, name);
		this.quantity = quantity;
		this.unitPurchasePrice = unitPurchasePrice;
	}

	public PurchaseItem(Product product, float quantity, double unitPurchasePrice) {
		this.product = product;
		this.quantity = quantity;
		this.unitPurchasePrice = unitPurchasePrice;
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

	public Double getUnitPurchasePrice() {
		return unitPurchasePrice;
	}

	public void setUnitPurchasePrice(Double unitPurchasePrice) {
		this.unitPurchasePrice = unitPurchasePrice;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		PurchaseItem that = (PurchaseItem)o;
		return product.equals(that.product) && quantity.equals(that.quantity) && unitPurchasePrice.equals(that.unitPurchasePrice);
	}

	@Override
	public int hashCode() {
		return Objects.hash(product, quantity, unitPurchasePrice);
	}

	@Override
	public String toString() {
		return "PurchaseItem{" + "product=" + product + ", quantity=" + quantity + ", unitPurchasePrice=" + unitPurchasePrice + '}';
	}
}
