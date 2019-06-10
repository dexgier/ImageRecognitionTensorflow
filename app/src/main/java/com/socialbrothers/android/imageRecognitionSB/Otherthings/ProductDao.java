package com.socialbrothers.android.imageRecognitionSB.Otherthings;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao //Dao can change make changes to the database
public interface ProductDao {
	
	@Query("SELECT * FROM productlist")
	LiveData<List<ProductList>> getAllProducts();
	
	@Insert
	void insertProduct(ProductList product);
	
	@Update
	void updateProduct(ProductList product);
	
	@Delete
	void deleteProduct(ProductList product);
	
	@Delete
	void deleteAllproducts(List<ProductList> product);
}
