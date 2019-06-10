package com.socialbrothers.android.imageRecognitionSB.Otherthings;

import android.content.res.Resources;
import android.os.Debug;
import android.util.Log;

import com.socialbrothers.android.imageRecognitionSB.R;

import java.util.HashMap;
import java.util.Map;

/*
 * Class ProductManager fills a Map with every product that's in the model, adds the price image etc.
 * Map is used for product scalability.
 * */
public class ProductManager {
	private static Map<String, Product> products;
	private static Resources resources;
	
	public static Product getProduct(String name) {
		if (products.containsKey(name.trim().toLowerCase())) {
			return products.get(name.trim().toLowerCase());
		} else
			return null;
	}
	
	/*
	 * addProducts functions adds all the products that are in the current model to the products Map.
	 * Also sets the right image, sets the right description string and the price so they are
	 * */
	private static void addProducts() {
		products.put("apple", new Product("Appel", 0.6, R.drawable.apple, resources.getString(R.string.appelDesc)));
		products.put("banana", new Product("Banaan", 0.4, R.drawable.banana, resources.getString(R.string.banaanDesc)));
		products.put("croissant", new Product("Croissant", 5.6, R.drawable.croissant, resources.getString(R.string.croissantDesc)));
		products.put("cucumber", new Product("Komkommer", 5.6, R.drawable.cucumber, resources.getString(R.string.komkommerDesc)));
		products.put("lemon", new Product("Citroen", 5.6, R.drawable.lemon, resources.getString(R.string.citroenDesc)));
		products.put("orange", new Product("Sinaasappel", 5.6, R.drawable.orange, resources.getString(R.string.sinaasappelDesc)));
		products.put("pear", new Product("Peer", 0.5, R.drawable.pear, resources.getString(R.string.peerDesc)));
		products.put("tomato", new Product("Tomaat", 5.6, R.drawable.tomato, resources.getString(R.string.tomaatDesc)));
		products.put("zucchini", new Product("Courgette", 5.6, R.drawable.zucchini, resources.getString(R.string.courgetteDesc)));
	}
	
	/*
	 * Initializes the resources so they can be accessed from this activity.
	 * Also initializes the HashMap and adds all the products to it.
	 * */
	public static void Initialize(Resources _resources) {
		resources = _resources;
		products = new HashMap<>();
		addProducts();
	}
}