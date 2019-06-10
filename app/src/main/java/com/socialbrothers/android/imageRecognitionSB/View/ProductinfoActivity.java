package com.socialbrothers.android.imageRecognitionSB.View;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.socialbrothers.android.imageRecognitionSB.Controller.ProductAdapter;
import com.socialbrothers.android.imageRecognitionSB.Otherthings.Product;
import com.socialbrothers.android.imageRecognitionSB.Otherthings.ProductManager;
import com.socialbrothers.android.imageRecognitionSB.R;

import java.text.DecimalFormat;

public class ProductinfoActivity extends AppCompatActivity {
    private TextView productName;
    private TextView productPrice;
    private Button buttonBack;
    private ImageView backgroundImage, sideImage;
    private Product chosenProduct;

    private TextView productDescription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_productinfo);
        chosenProduct = (Product) getIntent().getSerializableExtra(ProductAdapter.KEY_PRODUCT);
        Log.d("", chosenProduct.getName()+ " + " + chosenProduct.getDescription());
        initViews();
    }

    private void initViews() {
        productName = findViewById(R.id.productNameText);
        productPrice = findViewById(R.id.productPriceText);
        buttonBack = findViewById(R.id.returnButton);
        backgroundImage = findViewById(R.id.backdropImage);
        productDescription = findViewById(R.id.productDesc);
        productName.setText(chosenProduct.getName());
        DecimalFormat df = new DecimalFormat("€0.00");
        productDescription.setText(chosenProduct.getDescription());
        Log.d("test: ",chosenProduct.getDescription() + "+" + chosenProduct.getName());
        productPrice.setText(df.format(chosenProduct.getCurrentPrice()));
        backgroundImage.setImageResource(chosenProduct.getResourceId());
        sideImage.setImageResource(chosenProduct.getResourceId());
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}