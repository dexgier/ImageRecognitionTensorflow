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
            if (p.getName() == name) {
                return p;
            }
        }
        return null;
    }

    private static void addProducts(ArrayList<Product> productArrayList) {
        productArrayList.add(new Product("appel", 5.6, R.drawable.appel));
        productArrayList.add(new Product("ananas", 5.6, R.drawable.ananas));
        productArrayList.add(new Product("peer", 5.6, R.drawable.peer));
    }


}

























