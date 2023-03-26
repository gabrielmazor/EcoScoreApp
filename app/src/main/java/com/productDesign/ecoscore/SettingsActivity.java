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
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Get's rid of the acttion bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        TextView averageC = findViewById(R.id.avg);
        TextView stdC = findViewById(R.id.stan);
        averageC.setText(String.valueOf(Bill.Mean));
        stdC.setText(String.valueOf(Bill.Std));

        Button btn = findViewById(R.id.submitBtnA);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText avg = findViewById(R.id.inputMean);
                EditText std = findViewById(R.id.imputStd);
                Bill.Mean = Double.parseDouble(avg.getText().toString());
                Bill.Std = Double.parseDouble(std.getText().toString());
                Intent intent = new Intent();
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

        Button btnUser = findViewById(R.id.userChangeBtn);
        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChangeUserActivity.class);
                activityResultLauncher.launch(intent);
            }
        });
    }
}

//userChangeBtn