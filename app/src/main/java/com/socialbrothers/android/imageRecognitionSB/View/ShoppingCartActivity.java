package com.socialbrothers.android.imageRecognitionSB.View;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.socialbrothers.android.imageRecognitionSB.Alternatives;
import com.socialbrothers.android.imageRecognitionSB.Controller.MainViewModel;
import com.socialbrothers.android.imageRecognitionSB.Controller.ProductAdapter;
import com.socialbrothers.android.imageRecognitionSB.Otherthings.Product;
import com.socialbrothers.android.imageRecognitionSB.Otherthings.ProductList;
import com.socialbrothers.android.imageRecognitionSB.R;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

//This class keeps track of all the products that the user has scanned
public class ShoppingCartActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener {
	
	private TextView mTotalPriceView;
	private RecyclerView mRecyclerView;
	private ProductAdapter mProductAdapter;
	private List<ProductList> mProducts;
	private MainViewModel mMainViewModel;
	private java.util.Observer mObserver;
	private AlertDialog.Builder mAlert;
	private boolean alerted = false;
	private Product mScannedProduct;
	
	public static final String VIEW = "View";
	public static final int VIEWCODE = 4321;
	private boolean started = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_shopping_cart);
		initObserver();
		initViewModel();
		mTotalPriceView = findViewById(R.id.totalPriceView);
		mAlert = new AlertDialog.Builder(this);
		mProducts = new ArrayList<>();
		
		mScannedProduct = (Product) getIntent().getSerializableExtra(Alternatives.KEY_PRODUCT);
		if (mScannedProduct == null) {
			Log.d("ShoppingCart: ", "Product is null");
		}
	}
	
	private void initObserver() {
		mObserver = new java.util.Observer() {
			@Override
			public void update(Observable o, Object arg) {
				ProductList p = (ProductList) o;
				if (p.getProductCount() <= 0) {
					Log.d("Debuggie", String.valueOf(p.getProductCount()));
					mAlert.setMessage("Do you want to delete this product?")
							.setCancelable(false)
							.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									//If the user presses yes item gets deleted from database
									mMainViewModel.delete(p);
									alerted = false;
								}
							})
							.setNegativeButton("No", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									//If the user presses no productcount gets reset to the minimal of one
									p.setProductCount(1);
									mMainViewModel.update(p);
									dialog.cancel();
									alerted = false;
								}
							});
					
					if (!alerted) {
						alerted = true;
						AlertDialog alert = mAlert.create();
						alert.setTitle("Deleting product!");
						alert.show();
					}
				} else {
					//Update the recycler view after item gets deleted
					mMainViewModel.update(p);
				}
				setTotalPrice();
			}
		};
	}
	
	private void initViewModel() {
		mRecyclerView = findViewById(R.id.recyclerViewShoppingCart);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
		mRecyclerView.setAdapter(mProductAdapter);
		mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
		mMainViewModel.getProducts().observe(this, new Observer<List<ProductList>>() {
			@Override
			public void onChanged(@Nullable List<ProductList> products) {
				mProducts = products;
				setTotalPrice();
				updateUI();
				for (ProductList p : products) {
					p.deleteObservers();
					p.addObserver(mObserver);
				}
				//We wait for the items to get retrieved form the database before we add the product
				if (!started) {
					addProduct(products);
					started = true;
				}
			}
		});
	}
	
	//This method will check if the added product already exists, if it does the productcount of a ProductList gets increased by one
	private void addProduct(List<ProductList> products) {
		if (mScannedProduct == null) return;
		for (ProductList p : products) {
			if (mScannedProduct.getName().trim().compareToIgnoreCase(p.getName().trim()) == 0) {
				p.setProductCount(p.getProductCount() + 1);
				mMainViewModel.update(p);
				return;
			}
		}
		final ProductList productList = new ProductList(mScannedProduct.getName(), mScannedProduct.getCurrentPrice(), mScannedProduct.getResourceId(), 1, mScannedProduct.getDescription());
		productList.addObserver(mObserver);
		mMainViewModel.insert(productList);
	}
	
	//Compute the total price everytime a product gets added or removed
	private void setTotalPrice() {
		double totalPrice = 0;
		DecimalFormat df = new DecimalFormat("€0.00");
		df.setRoundingMode(RoundingMode.CEILING);
		if (mProducts.size() != 0) {
			for (ProductList productList : mProducts) {
				totalPrice += productList.getTotalPrice();
			}
			mTotalPriceView.setText(df.format(totalPrice));
		} else {
			mTotalPriceView.setText(getString(R.string.noMoney));
		}
	}
	
	//Update the recyclerview
	private void updateUI() {
		if (mProductAdapter == null) {
			mProductAdapter = new ProductAdapter(this, mProducts);
			mRecyclerView.setAdapter(mProductAdapter);
		} else {
			mProductAdapter.swapList(mProducts);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
		switch (item.getItemId()) {
			case R.id.action_delete_item:
				mMainViewModel.deleteAll(mProducts);
				return true;
			case R.id.home:
				finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
		return false;
	}
	
	@Override
	public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
	}
	
	@Override
	public void onRequestDisallowInterceptTouchEvent(boolean b) {
	}
}

