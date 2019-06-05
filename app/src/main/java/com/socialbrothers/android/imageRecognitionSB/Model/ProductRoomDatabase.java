package com.socialbrothers.android.imageRecognitionSB.Model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.socialbrothers.android.imageRecognitionSB.Otherthings.Product;
import com.socialbrothers.android.imageRecognitionSB.Otherthings.ProductDao;
import com.socialbrothers.android.imageRecognitionSB.Otherthings.ProductList;

@Database(entities = {Product.class, ProductList.class}, version = 1, exportSchema = false)
public abstract class ProductRoomDatabase extends RoomDatabase {
	
	private final static String NAME_DATABASE = "product_database";
	
	public abstract ProductDao productDao();
	
	private static volatile ProductRoomDatabase INSTANCE;
	
	public static ProductRoomDatabase getDatabase(final Context context) {
		if (INSTANCE == null) {
			synchronized (ProductRoomDatabase.class) {
				if (INSTANCE == null) {
					INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
							ProductRoomDatabase.class, NAME_DATABASE).build();
				}
			}
		}
		return INSTANCE;
	}
}
