package com.socialbrothers.android.imageRecognitionSB.Otherthings;

import com.socialbrothers.android.imageRecognitionSB.R;

import java.util.ArrayList;

public class ProductManager {

    private static ArrayList<Product> productArrayList;
    public static Product getProduct(String name) {
        if (productArrayList == null) {
            productArrayList = new ArrayList<>();
            addProducts(productArrayList);
        }

        for (Product p : productArrayList) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    private static void addProducts(ArrayList<Product> productArrayList) {
        productArrayList.add(new Product("apple", 5.6, R.drawable.apple));
        productArrayList.add(new Product("banana", 5.6, R.drawable.banana));
        productArrayList.add(new Product("croissant", 5.6, R.drawable.croissant));
        productArrayList.add(new Product("cucumber", 5.6, R.drawable.cucumber));
        productArrayList.add(new Product("lemon", 5.6, R.drawable.lemon));
        productArrayList.add(new Product("orange", 5.6, R.drawable.orange));
        productArrayList.add(new Product("pear", 5.6, R.drawable.pear));
        productArrayList.add(new Product("tomato", 5.6, R.drawable.tomato));
        productArrayList.add(new Product("zucchini", 5.6, R.drawable.zucchini));


    }


}

























