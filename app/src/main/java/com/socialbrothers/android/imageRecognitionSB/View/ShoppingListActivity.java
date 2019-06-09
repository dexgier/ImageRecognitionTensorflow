package com.socialbrothers.android.imageRecognitionSB.View;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.socialbrothers.android.imageRecognitionSB.Controller.ShoppingListAdapter;
import com.socialbrothers.android.imageRecognitionSB.Model.ShoppingListDatabase;
import com.socialbrothers.android.imageRecognitionSB.Otherthings.ShoppingListItem;
import com.socialbrothers.android.imageRecognitionSB.R;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ShoppingListActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener {

    private RecyclerView recyclerView;
    private GestureDetector gestureDetector;
    private ShoppingListDatabase database;
    private Executor executor;

    private List<ShoppingListItem> shoppingList;
    private ShoppingListAdapter adapter;

    public static final int REQUESTCODE = 1;
    public static final String ITEM = "List item";

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        database = ShoppingListDatabase.getDatabase(this);
        executor = Executors.newSingleThreadExecutor();

        recyclerView = findViewById(R.id.rv_shoppinglist);

        // Add layout, decoration and list to recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(ShoppingListActivity.this));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(new ShoppingListAdapter(shoppingList));

        // Go to screen to add item to shoppinglist
        fab = findViewById(R.id.add_shoppinglist_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShoppingListActivity.this, AddShoppingListActivity.class);
                startActivityForResult(intent, REQUESTCODE);
            }
        });


        // Add gesturedetector to remove items when they are pressed for a longer time
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null) {

                    // Get position to find out which item this is
                    int adapterPosition = recyclerView.getChildAdapterPosition(child);
                    final ShoppingListItem CHECK_ITEM = shoppingList.get(adapterPosition);

                    // Delete item based on position
                    deleteItem(shoppingList.get(adapterPosition));
                    shoppingList.remove(adapterPosition);
                    adapter.notifyItemRemoved(adapterPosition);

                    // Create snackbar (bottom pop up) to give user the option to undo the action
                    Snackbar undoSnackBar = Snackbar.make(child, "Deleted: " + CHECK_ITEM.getItem(), Snackbar.LENGTH_LONG);
                    undoSnackBar.setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            insertItem(CHECK_ITEM);
                        }
                    });
                    undoSnackBar.show();


                }
            }
        });

        recyclerView.addOnItemTouchListener(this);
        getAllItems();

    }

    private void getAllItems(){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                shoppingList = database.shoppingListDao().getAllShoppingListItems();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI();
                    }
                });
            }
        });
    }
    private void insertItem(final ShoppingListItem item) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                database.shoppingListDao().insert(item);
                getAllItems();
            }
        });
    }

    private void deleteItem(final ShoppingListItem item) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                database.shoppingListDao().insert(item);
                getAllItems();
            }
        });
    }

    private void updateUI() {
        if (adapter == null){
            adapter = new ShoppingListAdapter(shoppingList);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.swapList(shoppingList);
        }
    }

    @Override
    @NonNull
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUESTCODE) {
            if (resultCode == RESULT_OK) {

                ShoppingListItem addItem = data.getParcelableExtra(ShoppingListActivity.ITEM);
                insertItem(addItem);

            }
        }
    }


    // The next 3 methods are necessary for implementing Recyclerview
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
