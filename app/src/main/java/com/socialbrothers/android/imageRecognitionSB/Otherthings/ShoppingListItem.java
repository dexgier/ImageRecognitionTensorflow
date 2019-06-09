package com.socialbrothers.android.imageRecognitionSB.Otherthings;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "shoppingList_table")
public class ShoppingListItem implements Parcelable {

    @PrimaryKey (autoGenerate = true)
    private int id;

    @ColumnInfo(name = "shoppinglist")
    private String item;
    private double price;

    public ShoppingListItem(String item, double price){
        this.item = item;
        this.price = price;
    }

    // getters and setters to modify data
    public int getId() { return id; }
    public void setId(int id ) { this.id = id; }

    public String getItem(){ return item; }
    public void setItem(String item) { this.item = item; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }


    protected ShoppingListItem(Parcel in) {
        id = in.readInt();
        item = in.readString();
        price = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // Very important to use writeInt, writeString etc etc, writeValue will either not write anything
        // or it will write the ID of the item
        dest.writeInt(this.id);
        dest.writeString(this.item);
        dest.writeDouble(this.price);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShoppingListItem> CREATOR = new Creator<ShoppingListItem>() {
        @Override
        public ShoppingListItem createFromParcel(Parcel in) {
            return new ShoppingListItem(in);
        }

        @Override
        public ShoppingListItem[] newArray(int size) {
            return new ShoppingListItem[size];
        }
    };


}
