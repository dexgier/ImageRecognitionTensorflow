package com.socialbrothers.android.imageRecognitionSB;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.socialbrothers.android.imageRecognitionSB.Otherthings.Product;
import com.socialbrothers.android.imageRecognitionSB.Otherthings.ProductManager;
import com.socialbrothers.android.imageRecognitionSB.View.ShoppingCartActivity;

import java.io.Serializable;
import java.util.Observable;

public class Alternatives extends AppCompatActivity {

    private TextView mProduct;
    private Button toShoppingCart, scanAgain;
    public static String EDIT_PRODUCT  = "EDIT_PRODUCT";
    private Product Apple, Banana, Pear;
    private ImageView imgViewAlternative1, imgViewAlternative2, imgViewAlternative3;
    public static final String KEY_PRODUCT = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alternatives);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);


        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.9),(int)(height*.7));
        imgViewAlternative1 = findViewById(R.id.imgViewAlternative1);
        imgViewAlternative2 = findViewById(R.id.imgViewAlternative2);
        imgViewAlternative3 = findViewById(R.id.imgViewAlternative3);

        Apple = ProductManager.getProduct("apple");
        imgViewAlternative1.setImageResource(Apple.getResourceId());

        Banana = ProductManager.getProduct("banana");
        imgViewAlternative2.setImageResource(Banana.getResourceId());

        Pear = ProductManager.getProduct("pear");
        imgViewAlternative3.setImageResource(Pear.getResourceId());

        mProduct = findViewById(R.id.productName);
        toShoppingCart = findViewById(R.id.toShoppingCart);
        scanAgain = findViewById(R.id.scanAgain);
        Intent intent = getIntent();
        String product = intent.getStringExtra(EDIT_PRODUCT);
        mProduct.setText(product);
        buttonActivity();


    }
    private void buttonActivity(){
        toShoppingCart.setOnClickListener(v -> {
            Intent intent = new Intent(this, ShoppingCartActivity.class);
            this.startActivity(intent);
        });
        scanAgain.setOnClickListener(v -> {
            Intent intent = new Intent(this, CameraActivity.class);
            this.startActivity(intent);
        });

        imgViewAlternative1.setOnClickListener(v -> {
            Intent intent = new Intent(this, ShoppingCartActivity.class);
            intent.putExtra(KEY_PRODUCT, Apple);
            this.startActivity(intent);
        });
        imgViewAlternative2.setOnClickListener(v -> {
            Intent intent = new Intent(this, ShoppingCartActivity.class);
            intent.putExtra(KEY_PRODUCT, Banana);
            this.startActivity(intent);
        });
        imgViewAlternative3.setOnClickListener(v -> {
            Intent intent = new Intent(this, ShoppingCartActivity.class);
            intent.putExtra(KEY_PRODUCT, Pear);
            this.startActivity(intent);
        });
    }


}
