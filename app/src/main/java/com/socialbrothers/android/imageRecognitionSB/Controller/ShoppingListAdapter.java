package com.socialbrothers.android.imageRecognitionSB.Controller;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.socialbrothers.android.imageRecognitionSB.Otherthings.ShoppingListItem;
import com.socialbrothers.android.imageRecognitionSB.R;

import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {

    List<ShoppingListItem> shoppingList;

    public ShoppingListAdapter(List<ShoppingListItem> shoppingList) {
        this.shoppingList = shoppingList;
    }

    @NonNull
    @Override
    // Add ViewHolder to increase performance, makes use of the content_view_shoppinglist layout
    public ShoppingListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.content_view_shoppinglist, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(row);

        return viewHolder;
    }

    @Override
    // Add onBindViewHolder to display each item in the content_view_layout
    public void onBindViewHolder(@NonNull ShoppingListAdapter.ViewHolder viewHolder, int i) {
        final ShoppingListItem item = shoppingList.get(i);
        (viewHolder).item.setText(item.getItem());
        (viewHolder).price.setText(String.valueOf(item.getPrice()));

        // Method to strike through text when the checkBox is clicked
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    viewHolder.item.setPaintFlags(viewHolder.item.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    viewHolder.price.setPaintFlags(viewHolder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    viewHolder.item.setPaintFlags(viewHolder.item.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                    viewHolder.price.setPaintFlags(viewHolder.price.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                }
            }
        });

    }

    @Override
    // Added if else statement, because else the app would crash: count can be 0 but not Null
    public int getItemCount() {
        int count;
        if (shoppingList != null) {
            count = shoppingList.size();

        } else {
            count = 0;
        }
        return count;
    }

    // Reorders the ShoppingList when changes are made to the list, implemented in ShoppingListActivity
    public void swapList(List<ShoppingListItem> newList) {
        shoppingList = newList;
        if (newList != null) {
            this.notifyDataSetChanged();
        }
    }

    // Add items to class ViewHolder for increased performance
    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;
        TextView item;
        TextView price;

        public ViewHolder(View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.cb_checkBox);
            item = itemView.findViewById(R.id.tv_item);
            price = itemView.findViewById(R.id.tv_price);

        }
    }
}



