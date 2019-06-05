package com.socialbrothers.android.imageRecognitionSB.Model;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.socialbrothers.android.imageRecognitionSB.Otherthings.ShoppingListDao;
import com.socialbrothers.android.imageRecognitionSB.Otherthings.ShoppingListItem;

// Added exportSchema = false, because else Android Studio would change version number which resulted in errors
@Database(entities = ShoppingListItem.class, version = 1, exportSchema = false)
public abstract class ShoppingListDatabase extends RoomDatabase {

    private final static String NAME_DATABASE = "shoppinglist_database";
    private static ShoppingListDatabase instance;

    public abstract ShoppingListDao shoppingListDao();

    public static ShoppingListDatabase getDatabase(Context context){

        if (instance == null) {
            // Use synchronized to first execute this block of code before other methods can adjust the variables inside
            synchronized (ShoppingListDatabase.class) {
                if (instance == null){
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            ShoppingListDatabase.class,
                            NAME_DATABASE)
                            .build();
                }
            }
        }

        return instance;
    }
}
