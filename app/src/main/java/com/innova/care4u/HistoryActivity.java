package com.innova.care4u;

import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.animation.AnimationUtils;

/**
 * Created by Oluwatobi on 7/16/2037.
 */

public class HistoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historical_layout);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.historicalRecordsRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new HistoricalAdapter(ApplicationInstance.getHistoricalPojoList(), this));
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.abc_slide_in_bottom));
    }
}
