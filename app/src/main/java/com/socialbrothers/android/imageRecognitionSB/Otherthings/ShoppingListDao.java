package com.socialbrothers.android.imageRecognitionSB.Otherthings;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ShoppingListDao {

    @Insert
    void insert(ShoppingListItem shoppingListItem);

    @Delete
    void delete(ShoppingListItem shoppingListItem);

    @Delete
    void delete(List<ShoppingListItem> shoppingListItem);

    @Query("SELECT * from shoppingList_table")
    List<ShoppingListItem> getAllShoppingListItems();

}
