package com.productDesign.ecoscore;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BillAdapter extends RecyclerView.Adapter<BillViewHolder> {

    private List<Bill> Bills = new ArrayList<>();
    private FirebaseFirestore db;

    public BillAdapter() {
        Bills = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("bills")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                try {
                                    Bill m = new Bill(
                                            document.get("Month").toString(),
                                            document.getDouble("Consumption"),
                                            document.get("id").toString()

                                    );
                                    Bills.add(m);
                                } catch (Exception e) {
                                }
                            }
                            Collections.sort(Bills, new Comparator<Bill>() {
                                @Override
                                public int compare(Bill bill1, Bill bill2) {
                                    return bill2.getDocumentId().compareTo(bill1.getDocumentId());
                                }
                            });
                            notifyDataSetChanged();
                        }
                    }
                });
        db.collection("bills").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentSnapshot document : value.getDocuments()) {
                    try {
                        Bill m = new Bill(
                                document.get("Month").toString(),
                                document.getDouble("Consumption"),
                                document.get("id").toString()
                        );
                    } catch (Exception e) {
                    }
                }
                notifyDataSetChanged();
            }
        });
    }

    public void addBill(Bill m) {
        Bills.add(0, m);
        Map<String, Object> bill = new HashMap<>();
        bill.put("Month", m.Month);
        bill.put("Consumption", m.Consumption);
        bill.put("id", m.documentId);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String id = m.getDocumentId();
        db.collection("bills").document(id)
                .set(bill);

        notifyDataSetChanged();
    }


    public void deleteBill(int position) {
        Bill bill = Bills.get(position);
        String id = bill.getDocumentId();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("bills").document(bill.getDocumentId())
                .delete();
        Bills.remove(position);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_card, parent, false);
        BillViewHolder viewHolder = new BillViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        Bill bill = Bills.get(position);
        holder.month.setText(bill.getMonth());
        holder.consumption.setText(String.valueOf(bill.getConsumption()));
        holder.co.setText(String.valueOf(String.format("%.2f", (bill.getCo2()))));
        holder.precentile.setText(String.valueOf(String.format("%.0f", bill.getPrecentage())+"%"));
        if(position < Bills.size()-1 && Bills.get(position+1) != null){
            Bill previous = Bills.get(position + 1);
            double compare = (((bill.getConsumption() - previous.getConsumption()) / previous.getConsumption()) * 100);
            holder.compared.setText(String.valueOf(String.format("%.0f", compare) + "%"));
        }
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ViewBillActivity.class);
                intent.putExtra("Bill", bill);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) view.getContext(), holder.cv, "cardan");
                view.getContext().startActivity(intent, options.toBundle());
            }
        });
    }

    public Bill first(){
        if(Bills != null) {
            return (Bills.get(0));
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return (Bills.size());
    }
}