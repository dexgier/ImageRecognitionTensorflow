package com.socialbrothers.android.imageRecognitionSB.View;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.socialbrothers.android.imageRecognitionSB.Controller.MainViewModel;
import com.socialbrothers.android.imageRecognitionSB.Controller.ProductAdapter;
import com.socialbrothers.android.imageRecognitionSB.ImageClassifier;
import com.socialbrothers.android.imageRecognitionSB.Otherthings.ProductList;
import com.socialbrothers.android.imageRecognitionSB.R;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Observable;

public class ShoppingCartActivity extends AppCompatActivity {
	
	private Spinner mSpinner;
	private Button mButton;
	private TextView mTotalPriceView;
	private RecyclerView mRecyclerView;
	private ProductAdapter mProductAdapter;
	private List<ProductList> mProducts;
	private MainViewModel mMainViewModel;
	private java.util.Observer mObserver;
	private AlertDialog.Builder mAlert;
	private boolean alerted = false;
	private String scannedProduct;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_shopping_cart);
		
		scannedProduct = getIntent().getStringExtra(ImageClassifier.PRODUCT_NAME);
		
		mRecyclerView = findViewById(R.id.recy);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
		mRecyclerView.setAdapter(mProductAdapter);
		mTotalPriceView = findViewById(R.id.totalPriceView);
		mAlert = new AlertDialog.Builder(   this);
		initButton();
		initObserver();
		initViewModel();
		//initSpinner();
		//addProduct();
	}
	
	private void addProduct() {
		for (ProductList p : mProducts) {
			if (scannedProduct.equals(p.getName())) {
				p.setProductCount(p.getProductCount() + 1);
				mMainViewModel.update(p);
				return;
			}
		}
		 
		final ProductList productList = new ProductList(scannedProduct, 1.3, 1, 1);
		productList.addObserver(mObserver);
		mMainViewModel.insert(productList);
	}
	
	private void initButton() {
		mButton = findViewById(R.id.buttonAddProduct);
		mButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addProduct();
				/*for (ProductList p : mProducts) {
					if (mSpinner.getSelectedItem().toString().equals(p.getName()) scannedProduct.equals(p.getName())) {
						p.setProductCount(p.getProductCount() + 1);
						mMainViewModel.update(p);
						return;
					}
				}
				
				final ProductList productList = new ProductList(mSpinner.getSelectedItem().toString() scannedProduct, 1.3, 1, 1);
				productList.addObserver(mObserver);
				mMainViewModel.insert(productList);*/
			}
		});
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
									mMainViewModel.delete(p);
									alerted = false;
								}
							})
							.setNegativeButton("No", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
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
					mMainViewModel.update(p);
				}
				setTotalPrice();
			}
		};
	}
	
	private void initSpinner() {
		// mSpinner = findViewById(R.id.spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.products, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(adapter);
	}
	
	private void initViewModel() {
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
			}
		});
	}
	
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
	
	private void updateUI() {
		if (mProductAdapter == null) {
			mProductAdapter = new ProductAdapter(mProducts);
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
}

