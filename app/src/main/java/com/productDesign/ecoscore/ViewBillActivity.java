package com.productDesign.ecoscore;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ViewBillActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bill);

        // Get's rid of the acttion bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //Getting the data for this activity
        Bundle b = getIntent().getExtras();
        Bill bill = (Bill) b.getSerializable("Bill");

        TextView consumption = findViewById(R.id.kWh);
        TextView month = findViewById(R.id.month);
        TextView co = findViewById(R.id.co2number);
        TextView precentile = findViewById(R.id.Precent);

        consumption.setText(String.valueOf(bill.getConsumption()));
        month.setText(bill.getMonth());
        co.setText(String.valueOf(String.format("%.2f", (bill.getConsumption()*bill.getRatio()))));
        precentile.setText(String.valueOf(String.format("%.0f", bill.getPrecentage())+"%"));

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