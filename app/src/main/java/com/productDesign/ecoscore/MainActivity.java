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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 777;
    private FirebaseAnalytics mFirebaseAnalytics;
    private GoogleSignInClient mGoogleSignInClient;
    BillAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.mainLayout).setVisibility(View.INVISIBLE);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("385417861917-bregl99g63a5088msa5baicvt8r33vn3.apps.googleusercontent.com")
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        Button signIn = findViewById(R.id.googlebtn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        // Gets rid of the action bar
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

        TextView name = findViewById(R.id.greating);
        name.setText("Hello, " + User.Name);

        //Load data
        /*List<Bill> bills = new ArrayList<>();
        if(bills.size() != 0){
            Bill lastBill = bills.get(0);
            TextView consumption = findViewById(R.id.kwhcons);
            TextView co = findViewById(R.id.co2number);
            TextView precentage = findViewById(R.id.Precentage);
            consumption.setText(String.valueOf(lastBill.getConsumption()));
            co.setText(String.valueOf(String.format("%.2f", (lastBill.getCo2()))));
            precentage.setText(String.valueOf(String.format("%.0f", lastBill.getPrecentage())+"%"));
        }*/


        RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(false);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new BillAdapter();
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

    public void updateLast(Bill lastBill){
        if(lastBill != null)
        {
            TextView consumption = findViewById(R.id.kwhcons);
            TextView co = findViewById(R.id.co2number);
            TextView precentage = findViewById(R.id.Precentage);
            consumption.setText(String.valueOf(lastBill.getConsumption()));
            co.setText(String.valueOf(String.format("%.2f", (lastBill.getCo2()))));
            precentage.setText(String.valueOf(String.format("%.0f", lastBill.getPrecentage())+"%"));
        }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            User.setUserName(account.getDisplayName());
            success();
            Toast.makeText(this,"Welcome " + account.getDisplayName(),Toast.LENGTH_LONG).show();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
        }
    }

    private void success() {
        findViewById(R.id.mainLayout).setVisibility(View.VISIBLE);
        findViewById(R.id.hiddenLayout).setVisibility(View.GONE);
        updateLast(adapter.first());

    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView name = findViewById(R.id.greating);
        name.setText("Hello, " + User.Name);
    }

}