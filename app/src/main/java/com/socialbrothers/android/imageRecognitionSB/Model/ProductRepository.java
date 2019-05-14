package com.socialbrothers.android.imageRecognitionSB.Model;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.socialbrothers.android.imageRecognitionSB.Otherthings.ProductDao;
import com.socialbrothers.android.imageRecognitionSB.Otherthings.ProductList;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ProductRepository {
	private ProductRoomDatabase mAppDataBase;
	private ProductDao mProductDao;
	private LiveData<List<ProductList>> mProducts;
	private Executor mExecutor = Executors.newSingleThreadExecutor();
	
	public ProductRepository(Context context) {
		mAppDataBase = ProductRoomDatabase.getDatabase(context);
		mProductDao = mAppDataBase.productDao();
		mProducts = mProductDao.getAllProducts();
	}
	
	public LiveData<List<ProductList>> getAllProducts() {
		return mProducts;
	}
	
	public void insert(final ProductList product) {
		mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				mProductDao.insertProduct(product);
			}
		});
	}
	
	public void update(final ProductList product) {
		mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				mProductDao.updateProduct(product);
			}
		});
	}
	
	public void delete(final ProductList product) {
		mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				mProductDao.deleteProduct(product);
			}
		});
	}
	
	public void deleteAll(final List<ProductList> products) {
		mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				mProductDao.deleteAllproducts(products);
				
			}
		});
	}
	
}
