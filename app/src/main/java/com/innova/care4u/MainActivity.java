package com.innova.care4u;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.innova.care4u.fingerPrintDeals.FingerPrintActivity;

public class MainActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    Context context;
    TextView contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        setContentView(R.layout.activity_main);
        loadToolabar();
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
                startActivity(new Intent(MainActivity.this, ViewPatients.class));
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

    public void Signout(View view)
    {
        ProgressDialog signoutDialog;
        signoutDialog  = new ProgressDialog(context);
        signoutDialog.setMessage("Signing Out!");
        signoutDialog.setCanceledOnTouchOutside(false);
        signoutDialog.setCancelable(false);
        ImageView signout = findViewById(R.id.logout);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signoutDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        signoutDialog.dismiss();
                        signoutDialog.cancel();

                        Toast.makeText(MainActivity.this, "Key Destroyed!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, Login.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    }
                }, 3000);

            }
        });


    }


    public void goToContactUs(View view) {
        contact = findViewById(R.id.contact);
        startActivity(new Intent(MainActivity.this, ContactUs.class));
    }

    public void loadToolabar()
    {
        // to make the visisbility of sigout and
        // QR code generator icon ..

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImageView logout = (ImageView) toolbar.findViewById(R.id.logout);
        ImageView qrCode = (ImageView) toolbar.findViewById(R.id.down);

        logout.setVisibility(View.VISIBLE);
        qrCode.setVisibility(View.VISIBLE);
//        toolbar.addView(logout);
//        toolbar.addView(qrCode);
    }
}
