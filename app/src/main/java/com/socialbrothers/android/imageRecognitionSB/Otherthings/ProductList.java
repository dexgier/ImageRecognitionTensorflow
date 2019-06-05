package com.socialbrothers.android.imageRecognitionSB.Otherthings;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

@Entity
public class ProductList extends Product {
	
	@ColumnInfo(name = "totalPrice")
	private double totalPrice;
	
	@ColumnInfo(name = "productCount")
	private int productCount;
	
	public ProductList(String name, double currentPrice, int resourceId, int productCount) {
		super(name, currentPrice, resourceId);
		this.totalPrice = currentPrice * productCount;
		this.productCount = productCount;
		pricesChanged();
	}
	
	public double getTotalPrice() {
		return totalPrice;
	}
	
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	public int getProductCount() {
		return productCount;
	}
	
	public void setProductCount(int productCount) {
		this.productCount = productCount;
		pricesChanged();
	}
	
	@Override
	public void pricesChanged() {
		synchronized (this) {
			if (productCount > 0)
				totalPrice = productCount * getCurrentPrice();
		}
		setChanged();
		notifyObservers();
	}
}
