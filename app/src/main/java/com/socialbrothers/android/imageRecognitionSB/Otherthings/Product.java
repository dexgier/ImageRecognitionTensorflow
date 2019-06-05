package com.socialbrothers.android.imageRecognitionSB.Otherthings;

import android.annotation.SuppressLint;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Debug;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Observable;

@Entity(tableName = "product")
public class Product extends Observable implements Parcelable {
	
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
		this.resourceId = resourceId;
	}

	protected Product(Parcel in) {
		if (in.readByte() == 0) {
			id = null;
		} else {
			id = in.readLong();
		}
		name = in.readString();
		currentPrice = in.readDouble();
		standardPrice = in.readDouble();
		resourceId = in.readInt();
	}

	public static final Creator<Product> CREATOR = new Creator<Product>() {
		@Override
		public Product createFromParcel(Parcel in) {
			return new Product(in);
		}

		@Override
		public Product[] newArray(int size) {
			return new Product[size];
		}
	};

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
		Log.d("Resource ID:", String.valueOf(resourceId));
		return resourceId;
	}
	
	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}
	
	public void pricesChanged() { }

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		if (id == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);
			dest.writeLong(id);
		}
		dest.writeString(name);
		dest.writeDouble(currentPrice);
		dest.writeDouble(standardPrice);
		dest.writeInt(resourceId);
	}
}
