package com.innova.care4u;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Oluwatobi on 7/16/2037.
 */

public class AttendNewPatient extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attend_to_patient);
        LinearLayout linearLayoutOne = (LinearLayout)findViewById(R.id.newRecordsId);
        LinearLayout linearLayoutTwo = (LinearLayout)findViewById(R.id.historicalRecordsId);
        linearLayoutOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AttendNewPatient.this, AddNewRecord.class));
            }
        });
        linearLayoutTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AttendNewPatient.this, HistoryActivity.class));
            }
        });
    }
}
