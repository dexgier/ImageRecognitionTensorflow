package com.socialbrothers.android.imageRecognitionSB.Otherthings;

import android.content.res.Resources;
import android.os.Debug;
import android.util.Log;

import com.socialbrothers.android.imageRecognitionSB.R;

import java.util.HashMap;
import java.util.Map;

public class ProductManager {
	private static Map<String, Product> products;
	private static Resources resources;
	
	public static Product getProduct(String name) {
		if (products.containsKey(name.trim().toLowerCase())) {
			Log.d("Test", products.get(name.trim().toLowerCase()).getName());
			return products.get(name.trim().toLowerCase());
		} else Log.d("Test2", name + " failed!");
		return null;
	}
	
	private static void addProducts() {
		products.put("apple", new Product("Appel", 5.6, R.drawable.apple, resources.getString(R.string.appleDesc)));
		products.put("banana", new Product("Banaan", 5.6, R.drawable.banana, resources.getString(R.string.bananaDesc)));
		products.put("croissant", new Product("Croissant", 5.6, R.drawable.croissant, "1"));
		products.put("cucumber", new Product("Komkommer", 5.6, R.drawable.cucumber, "2"));
		products.put("lemon", new Product("Citroen", 5.6, R.drawable.lemon, "3"));
		products.put("orange", new Product("Sinaasappel", 5.6, R.drawable.orange, "4"));
		products.put("pear", new Product("Peer", 5.6, R.drawable.pear, resources.getString(R.string.pearDesc)));
		products.put("tomato", new Product("Tomaat", 5.6, R.drawable.tomato, "5"));
		products.put("zucchini", new Product("Courgette", 5.6, R.drawable.zucchini, "6"));
	}
	
	public static void Initialize(Resources _resources) {
		resources = _resources;
		products = new HashMap<>();
		addProducts();
	}
}

























