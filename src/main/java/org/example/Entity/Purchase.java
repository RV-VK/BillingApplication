package org.example.Entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Purchase {
	private Integer id;
	private String date;
	private Integer invoice;
	private List<PurchaseItem> purchaseItemList;
	private Double grandTotal;

	public Purchase() {
	}


	public Purchase(int id, String date, int invoice, List<PurchaseItem> purchaseItemList, double grandTotal) {
		this.id = id;
		this.date = date;
		this.invoice = invoice;
		this.purchaseItemList = purchaseItemList;
		this.grandTotal = grandTotal;
	}

	public Purchase(String date, int invoice, List<PurchaseItem> purchaseItemList, double grandTotal) {
		this.date = date;
		this.invoice = invoice;
		this.purchaseItemList = purchaseItemList;
		this.grandTotal = grandTotal;
	}

	public Purchase(String date, Integer invoice, List<PurchaseItem> purchaseItemList, double grandTotal) {
		this.date = date;
		this.invoice = invoice;
		this.purchaseItemList = purchaseItemList;
		this.grandTotal = grandTotal;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getInvoice() {
		return invoice;
	}

	public void setInvoice(Integer invoice) {
		this.invoice = invoice;
	}

	public List<PurchaseItem> getPurchaseItemList() {
		return purchaseItemList;
	}

	public void setPurchaseItemList(List<PurchaseItem> purchaseItemList) {
		this.purchaseItemList = purchaseItemList;
	}

	public Double getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(Double grandTotal) {
		this.grandTotal = grandTotal;
	}

	@Override
	public String toString() {
		return "Purchase{" + "id=" + id + ", date='" + date + '\'' + ", invoice=" + invoice + ", purchaseItemList=" + purchaseItemList + ", grandTotal=" + grandTotal + '}';
	}
}
