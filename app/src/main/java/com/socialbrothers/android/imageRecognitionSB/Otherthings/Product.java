package com.socialbrothers.android.imageRecognitionSB.Otherthings;

import android.annotation.SuppressLint;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Observable;

@Entity(tableName = "product")
public class Product extends Observable {
	
	@PrimaryKey(autoGenerate = true)
	private Long id;
	
	@ColumnInfo(name = "name")
	private String name;
	
	@ColumnInfo(name = "currentPrice")
	private double currentPrice;
	
	@ColumnInfo(name = "standardPrice")
	private double standardPrice;
	//object for discount
	
	@ColumnInfo(name = "resourceId")
	private int resourceId;
	
	@SuppressLint("RestrictedApi")
	public Product(String name, double currentPrice, int resourceId) {
		this.name = name;
		this.currentPrice = currentPrice;

		DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
		DatabaseReference yourRef = rootRef.child("artpics").push();
		Product product = new Product("", 0, 0);
		yourRef.setValue(product);
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public double getCurrentPrice() {
		return currentPrice;
	}
	
	public void setCurrentPrice(double currentPrice) {
		this.currentPrice = currentPrice;
		pricesChanged();
	}
	
	public double getStandardPrice() {
		return standardPrice;
	}
	
	public void setStandardPrice(double standardPrice) {
		this.standardPrice = standardPrice;
		pricesChanged();
	}
	
	public int getResourceId() {
		return resourceId;
	}
	
	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}
	
	public void pricesChanged() { }
}
