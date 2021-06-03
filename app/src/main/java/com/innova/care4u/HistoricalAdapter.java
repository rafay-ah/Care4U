package com.innova.care4u;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by Oluwatobi on 7/16/2037.
 */

public class HistoricalAdapter extends RecyclerView.Adapter<HistoricalAdapter.HistoricalViewHolder> {
    ArrayList<HistoricalPojo> historicalPojos;
    Context context;
    public HistoricalAdapter(ArrayList<HistoricalPojo> historicalPojos, Context context) {
        this.historicalPojos = historicalPojos;
        this.context = context;
    }

    @Override
    public HistoricalViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.historical_items, viewGroup, false);
        return new HistoricalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoricalViewHolder historicalViewHolder, final int i) {
        historicalViewHolder.title.setText(historicalPojos.get(i).getTitle());
        historicalViewHolder.hospital_name.setText(historicalPojos.get(i).getHospital_name());
        historicalViewHolder.cost.setText(historicalPojos.get(i).getCost());
        historicalViewHolder.date.setText(historicalPojos.get(i).getDate());

        historicalViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LargerView.class);
                intent.putExtra("me", historicalPojos.get(i).getTitle());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return historicalPojos.size();
    }

    public class HistoricalViewHolder extends RecyclerView.ViewHolder {
        TextView  title, cost,hospital_name, date;
        public HistoricalViewHolder(View itemView) {
            super(itemView);
            title  = (TextView) itemView.findViewById(R.id.history_title);
            cost = (TextView) itemView.findViewById(R.id.history_cost);
            hospital_name = (TextView) itemView.findViewById(R.id.history_hospital);
            date = (TextView) itemView.findViewById(R.id.history_date);
        }
    }
}
