package com.innova.care4u;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.innova.care4u.fingerPrintDeals.FingerPrintActivity;

public class MainActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        setContentView(R.layout.activity_main);
        context = this;
        LinearLayout linearLayoutOne = (LinearLayout)findViewById(R.id.registerNew);
        LinearLayout linearLayoutTwo = (LinearLayout)findViewById(R.id.attendPatient);
        LinearLayout linearLayoutThree = (LinearLayout)findViewById(R.id.paymentId);

        progressDialog  = new ProgressDialog(this);
        progressDialog.setMessage("Synchronizing data to blockchain ledgers");
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

    public void generateQr(View view)
    {
        String cipherKey= getIntentData();
        Intent intent=new Intent(context,QRactivity.class);
        intent.putExtra("KEY", cipherKey);
        startActivity(intent);
        finish();
    }

    public String getIntentData()
    {// gets the cipher key from biometric authentication
        Intent intent = getIntent();
        String cipherKey = intent.getStringExtra("KEY");
        return cipherKey;
    }
}
