package com.innova.care4u.fingerPrintDeals;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.innova.care4u.AttendNewPatient;
import com.innova.care4u.R;

/**
 * Created by Oluwatobi on 7/16/2037.
 */

public class FingerPrintActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finger_print_activity);


        progressDialog  = new ProgressDialog(this);
        progressDialog.setMessage("Please wait, verifying finger print data...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

        Button sucess = (Button) findViewById(R.id.yes);
        Button fail  = (Button) findViewById(R.id.no);
        sucess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        progressDialog.cancel();
                        Toast.makeText(FingerPrintActivity.this, "Fingerprint matched", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(FingerPrintActivity.this, AttendNewPatient.class));

                    }
                }, 3000);
            }
        });

        fail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doFailure();
            }
        });
    }

    void doFailure() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Fingerprint not matched or placed incorrectly");
        builder.setMessage("The finger print data provided does not match with any in the database, kindly replace your thumb well or register");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                dialogInterface.cancel();

            }
        });
        builder.show();
    }
}
