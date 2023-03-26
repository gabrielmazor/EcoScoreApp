package com.productDesign.ecoscore;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class BillViewHolder extends RecyclerView.ViewHolder {

    CardView cv;
    TextView month;
    TextView co;
    TextView consumption;
    TextView precentile;
    TextView compared;

    public BillViewHolder(@NonNull View itemView) {
        super(itemView);

        cv = itemView.findViewById(R.id.cv);
        month = itemView.findViewById(R.id.monthname);
        consumption = itemView.findViewById(R.id.kWh);
        co = itemView.findViewById(R.id.co2number);
        precentile = itemView.findViewById(R.id.precentile);
        compared = itemView.findViewById(R.id.compare2me);
    }
}
