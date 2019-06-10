package com.socialbrothers.android.imageRecognitionSB.Controller;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.socialbrothers.android.imageRecognitionSB.Model.ProductRepository;
import com.socialbrothers.android.imageRecognitionSB.Otherthings.ProductList;

import java.util.List;

import androidx.annotation.NonNull;

//ViewModel to access the local database
public class MainViewModel extends AndroidViewModel {
	
	private ProductRepository mRepository;
	private LiveData<List<ProductList>> mProducts;
	
	public MainViewModel(@NonNull Application application) {
		super(application);
		mRepository = new ProductRepository(application.getApplicationContext());
		mProducts = mRepository.getAllProducts();
	}
	
	public LiveData<List<ProductList>> getProducts() {return mProducts;}
	
	public void insert(ProductList product) {
		mRepository.insert(product);
	}
	
	public void update(ProductList product) {
		mRepository.update(product);
	}
	
	public void delete(ProductList product) {
		mRepository.delete(product);
	}
	
	public void deleteAll(List<ProductList> products) {
		mRepository.deleteAll(products);
	}
}
