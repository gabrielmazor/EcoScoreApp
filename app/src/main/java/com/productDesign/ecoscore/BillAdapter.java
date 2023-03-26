package com.productDesign.ecoscore;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillViewHolder> {

    List<Bill> Bills;
    public BillAdapter(List<Bill> bills) {
        Bills = bills;
    }

    public void addBill(Bill bill) {
        Bills.add(0, bill);
        notifyDataSetChanged();
        DataPersistencyHelper.storeData(Bills);
    }

    public void deleteUser(int position) {
        Bills.remove(position);
        notifyDataSetChanged();
        DataPersistencyHelper.storeData(Bills);
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

    @Override
    public int getItemCount() {
        return (Bills.size());
    }
}
