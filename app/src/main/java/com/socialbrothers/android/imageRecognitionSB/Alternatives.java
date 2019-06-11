package com.socialbrothers.android.imageRecognitionSB;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.socialbrothers.android.imageRecognitionSB.Otherthings.Product;
import com.socialbrothers.android.imageRecognitionSB.Otherthings.ProductManager;
import com.socialbrothers.android.imageRecognitionSB.View.ShoppingCartActivity;

public class Alternatives extends AppCompatActivity {
	
	//variables for alternative screen
	private TextView mProduct;
	private Button toShoppingCart, scanAgain;
	public static String EDIT_PRODUCT = "EDIT_PRODUCT";
	private Product Apple, Banana, Pear;
	String scannedProduct;
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
		getWindow().setLayout((int) (width * .9), (int) (height * .7));
		
		/*
		 * Sets up the alternative screen views.
		 * Also gets the right product from ProductManager to display the right picture.
		 * */
		imgViewAlternative1 = findViewById(R.id.imgViewAlternative1);
		imgViewAlternative2 = findViewById(R.id.imgViewAlternative2);
		imgViewAlternative3 = findViewById(R.id.imgViewAlternative3);
		
		Apple = ProductManager.getProduct("Appel");
		imgViewAlternative1.setImageResource(Apple.getResourceId());
		
		Banana = ProductManager.getProduct("Banaan");
		imgViewAlternative2.setImageResource(Banana.getResourceId());
		
		Pear = ProductManager.getProduct("Peer");
		imgViewAlternative3.setImageResource(Pear.getResourceId());
		
		mProduct = findViewById(R.id.productName);
		toShoppingCart = findViewById(R.id.toShoppingCart);
		scanAgain = findViewById(R.id.scanAgain);
		Intent intent = getIntent();
		scannedProduct = intent.getStringExtra(EDIT_PRODUCT);
		mProduct.setText(scannedProduct);
		buttonActivity();
	}
	
	/*
	 * ButtonActivity function handles all the button clicks done on the alternative screen.
	 * Sends the right intents through, and makes sure that the right activity is called.
	 * */
	private void buttonActivity() {
		toShoppingCart.setOnClickListener(v -> {
			Intent intent = new Intent(this, ShoppingCartActivity.class);
			Product product = ProductManager.getProduct(scannedProduct);
			intent.putExtra(KEY_PRODUCT, product);
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
