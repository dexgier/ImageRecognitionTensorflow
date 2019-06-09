package com.socialbrothers.android.imageRecognitionSB.Otherthings;

import android.content.res.Resources;
import android.util.Log;

import com.socialbrothers.android.imageRecognitionSB.R;

import java.util.HashMap;
import java.util.Map;

public class ProductManager {
    private static Map<String, Product> products;
    private static Resources resources;

    public static Product getProduct(String name) {
        if (products.containsKey(name)) {
            Log.d("Test", name);
            return products.get(name);
        }else Log.d("Test", name + " failed!");
        return null;
    }

    private static void addProducts() {
        products.put("apple", new Product("apple", 5.6, R.drawable.apple, resources.getString(R.string.appleDesc)));
        products.put("banana", new Product("banana", 5.6, R.drawable.banana, resources.getString(R.string.bananaDesc)));
        products.put("croissant", new Product("croissant", 5.6, R.drawable.croissant, "1"));
        products.put("cucumber", new Product("cucumber", 5.6, R.drawable.cucumber, "2"));
        products.put("lemon", new Product("lemon", 5.6, R.drawable.lemon, "3"));
        products.put("orange", new Product("orange", 5.6, R.drawable.orange, "4"));
        products.put("pear", new Product("pear", 5.6, R.drawable.pear, resources.getString(R.string.pearDesc)));
        products.put("tomato", new Product("tomato", 5.6, R.drawable.tomato, "5"));
        products.put("zucchini", new Product("zucchini", 5.6, R.drawable.zucchini, "6"));
    }

    public static void Initialize(Resources _resources) {
        resources = _resources;
        products = new HashMap<>();
        addProducts();
    }
}

























