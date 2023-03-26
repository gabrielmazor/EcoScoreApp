package com.productDesign.ecoscore;

import static com.productDesign.ecoscore.DataPersistencyHelper.context;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ChangeUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user);

        // Get's rid of the acttion bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Button btn = findViewById(R.id.submitBtnA);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name = findViewById(R.id.inputMonth);
                User.Name = (String.valueOf(name.getText()));
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("Name", User.Name);
                editor.apply();
                finish();
            }
        });
    }
}