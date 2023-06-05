package Entity;

public class PurchaseItem {
	private Product product;
	private Float quantity;
	private Double unitPurchasePrice;


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
	public String toString() {
		return "PurchaseItem{" + "product=" + product + ", quantity=" + quantity + ", unitPurchasePrice=" + unitPurchasePrice + '}';
	}
}
