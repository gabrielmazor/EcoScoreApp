package com.productDesign.ecoscore;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NewBillActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bill);

        // Get's rid of the acttion bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Button btn = findViewById(R.id.submitBtnA);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText month = findViewById(R.id.inputMonth);
                EditText consumption = findViewById(R.id.imputKwh);
                Bill newBill = new Bill(month.getText().toString(), Double.parseDouble(consumption.getText().toString()));
                Intent intent = new Intent();
                intent.putExtra("Bill", newBill);
                setResult(1,intent);
                finish();
            }
        });

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent data = result.getData();
            }
        });

        FloatingActionButton fabSet = findViewById(R.id.floatingActionButtonSettings);
        fabSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SettingsActivity.class);
                activityResultLauncher.launch(intent);
            }
        });
    }
}