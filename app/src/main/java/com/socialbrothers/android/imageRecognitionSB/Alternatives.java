package com.socialbrothers.android.imageRecognitionSB;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.TextView;

import com.socialbrothers.android.imageRecognitionSB.R;
import com.socialbrothers.android.imageRecognitionSB.View.ShoppingCartActivity;

public class Alternatives extends AppCompatActivity {

    private TextView mProduct;
    private Button toShoppingCart, scanAgain;
    public static String EDIT_PRODUCT  = "EDIT_PRODUCT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alternatives);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.9),(int)(height*.7));

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
    }
}
