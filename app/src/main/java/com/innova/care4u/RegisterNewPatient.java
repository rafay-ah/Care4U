package com.innova.care4u;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.yazeed44.imagepicker.model.ImageEntry;
import net.yazeed44.imagepicker.util.Picker;

import java.util.ArrayList;

import com.innova.care4u.fingerPrintDeals.FingerPrintActivity;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by Oluwatobi on 7/16/2037.
 */

public class RegisterNewPatient extends AppCompatActivity {
    EditText fullname, parentName,phoneNumber,dateOfBirth,locationEdit;
    Button registerButton;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_patient);

        progressDialog  = new ProgressDialog(this);
        progressDialog.setMessage("Registering a new patient");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

        fullname = (EditText) findViewById(R.id.fullname);
        parentName = (EditText) findViewById(R.id.parents_name);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        dateOfBirth = (EditText) findViewById(R.id.dateOfBirth);
        locationEdit  = (EditText) findViewById(R.id.location);

        registerButton = (Button) findViewById(R.id.registerId);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startActivity(new Intent(RegisterNewPatient.this, FingerPrintActivity.class));
            }
        });

        TextView upload    =     (TextView) findViewById(R.id.uploadPhoto);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            pickImages();
            }
        });
    }

    private void pickImages(){
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)){

            ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                    0);

        }
        else new Picker.Builder(this, new MyPickListener(),R.style.myGreyTheme)
                .setAlbumBackgroundColor(Color.WHITE)
                .setLimit(2)
                .setImageBackgroundColorWhenChecked(getResources().getColor(R.color.PPR_PURPLE_ACCENT_700))
                .setBackBtnInMainActivity(true)
                .setDoneFabIconTintColor(Color.WHITE)
                .setVideosEnabled(false)
                .setCheckIconTintColor(Color.WHITE)
                .setFabBackgroundColor(getResources().getColor(R.color.PPR_PURPLE_ACCENT_700))
                .setFabBackgroundColorWhenPressed(getResources().getColor(R.color.PPR_PURPLE500))
                .setAlbumNameTextColor(Color.BLACK)
                .setAlbumImagesCountTextColor(Color.BLACK)
                .build()
                .startActivity();
    }

    private class MyPickListener implements Picker.PickListener {
        @Override
        public void onPickedSuccessfully(ArrayList<ImageEntry> images) {
            TextView upload    =     (TextView) findViewById(R.id.uploadPhoto);
            upload.setText("Photo selected");

        }

        @Override
        public void onCancel() {

        }
    }
}
