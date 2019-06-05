package com.socialbrothers.android.imageRecognitionSB.View;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.socialbrothers.android.imageRecognitionSB.Otherthings.ShoppingListItem;
import com.socialbrothers.android.imageRecognitionSB.R;

public class AddShoppingListActivity extends AppCompatActivity {

    public EditText addItem;
    public EditText addPrice;
    public FloatingActionButton save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shopping);

        addItem = findViewById(R.id.et_title);
        addPrice = findViewById(R.id.et_price);
        save = findViewById(R.id.btn_save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShoppingListItem item = new ShoppingListItem(addItem.getText().toString(),
                        Double.parseDouble(addPrice.getText().toString()));
                Intent intent = new Intent();
                intent.putExtra(ShoppingListActivity.ITEM, item);
                setResult(Activity.RESULT_OK, intent);
                finish();

            }
        });

    }
}
