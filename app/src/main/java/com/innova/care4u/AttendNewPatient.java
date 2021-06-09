package com.innova.care4u;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.innova.care4u.Model.Patient;


public class AttendNewPatient extends AppCompatActivity {
    Patient selectedPatient;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attend_to_patient);
        getIntentData();
        populatePatientDetails();
        LinearLayout linearLayoutOne = (LinearLayout)findViewById(R.id.newRecordsId);
//        LinearLayout linearLayoutTwo = (LinearLayout)findViewById(R.id.historicalRecordsId);
        linearLayoutOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AttendNewPatient.this, AddNewRecord.class).putExtra("patient", selectedPatient));
            }
        });
//        linearLayoutTwo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(AttendNewPatient.this, HistoryActivity.class).putExtra("patient", selectedPatient));
//            }
//        });
    }

    public void attendPatient(View view) {
        startActivity(new Intent(AttendNewPatient.this, MainActivity.class));
    }
     void getIntentData()
    {
        selectedPatient = (Patient) getIntent().getSerializableExtra("patient");
        Toast.makeText(AttendNewPatient.this, "Selected Patient : " +selectedPatient.Name,
                Toast.LENGTH_LONG).show();
    }

    public void populatePatientDetails()
    {
        TextView name = findViewById(R.id.patient_name);
        TextView address = findViewById(R.id.patient_address);
        TextView parentName = findViewById(R.id.patient_parentname);
        TextView phone = findViewById(R.id.patient_phonenumber);

        name.setText(selectedPatient.Name);
        address.setText(selectedPatient.Location);
        parentName.setText(selectedPatient.ParentName);
        phone.setText(selectedPatient.Phone);
    }
}
