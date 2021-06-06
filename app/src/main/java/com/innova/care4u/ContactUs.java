package com.innova.care4u;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ContactUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        loadToolabar();
    }

    public void goBackFromContactUs(View view) {
        startActivity(new Intent(ContactUs.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

    public void loadToolabar()
    {
        // to make the visisbility of sigout and
        // QR code generator icon gone..

        Toolbar toolbar = (Toolbar) findViewById(R.id.contactus_toolbar);
        ImageView logout = (ImageView) toolbar.findViewById(R.id.logout);
        ImageView qrCode = (ImageView) toolbar.findViewById(R.id.down);

        toolbar.removeView(logout);
        toolbar.removeView(qrCode);
    }
}