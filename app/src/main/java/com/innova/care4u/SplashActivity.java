package com.innova.care4u;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;

import com.innova.care4u.fingerPrintDeals.FingerPrintActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, FingerPrintActivity.class);
        startActivity(intent);
        finish();
    }
}