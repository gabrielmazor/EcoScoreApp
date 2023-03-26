package com.productDesign.ecoscore;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BillAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get's rid of the acttion bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent data = result.getData();
                newBillResult(data);
            }
        });

        //Load data
        DataPersistencyHelper.context = getApplicationContext();
        List<Bill> bills = DataPersistencyHelper.loadData();

        TextView name = findViewById(R.id.greating);
        name.setText("Hello, " + User.Name);

        if(bills.size() != 0){
            Bill lastBill = bills.get(0);
            TextView consumption = findViewById(R.id.kwhcons);
            TextView co = findViewById(R.id.co2number);
            TextView precentage = findViewById(R.id.Precentage);
            consumption.setText(String.valueOf(lastBill.getConsumption()));
            co.setText(String.valueOf(String.format("%.2f", (lastBill.getCo2()))));
            precentage.setText(String.valueOf(String.format("%.0f", lastBill.getPrecentage())+"%"));
        }

        RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(false);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new BillAdapter(bills);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper helper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        helper.attachToRecyclerView(recyclerView);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NewBillActivity.class);
                activityResultLauncher.launch(intent);
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

    private void updateLast(Bill lastBill){
        TextView consumption = findViewById(R.id.kwhcons);
        TextView co = findViewById(R.id.co2number);
        TextView precentage = findViewById(R.id.Precentage);
        consumption.setText(String.valueOf(lastBill.getConsumption()));
        co.setText(String.valueOf(String.format("%.2f", (lastBill.getCo2()))));
        precentage.setText(String.valueOf(String.format("%.0f", lastBill.getPrecentage())+"%"));
    }

    private void newBillResult(Intent data) {
        if (data != null && data.getExtras() != null) {
            Bundle b = data.getExtras();
            Bill bill = (Bill) b.getSerializable("Bill");
            adapter.addBill(bill);
            updateLast(bill);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView name = findViewById(R.id.greating);
        name.setText("Hello, " + User.Name);
    }

}