package com.innova.care4u;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.innova.care4u.fingerPrintDeals.FingerPrintActivity;

public class MainActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        setContentView(R.layout.activity_main);
        LinearLayout linearLayoutOne = (LinearLayout)findViewById(R.id.registerNew);
        LinearLayout linearLayoutTwo = (LinearLayout)findViewById(R.id.attendPatient);
        LinearLayout linearLayoutThree = (LinearLayout)findViewById(R.id.paymentId);

        progressDialog  = new ProgressDialog(this);
        progressDialog.setMessage("Synchronizing data to blockchain");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);


        linearLayoutOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterNewPatient.class));
            }
        });
        linearLayoutTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FingerPrintActivity.class));
            }
        });
        linearLayoutThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PaymentActivity.class));
            }
        });
        TextView textView = (TextView) findViewById(R.id.sync);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.dismiss();
                progressDialog.cancel();
                Toast.makeText(MainActivity.this, "Successfully synchronized", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
