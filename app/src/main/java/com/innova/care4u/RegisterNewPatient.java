package com.innova.care4u;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import net.yazeed44.imagepicker.model.ImageEntry;
import net.yazeed44.imagepicker.util.Picker;

import java.util.ArrayList;

import com.innova.care4u.Model.Patient;
import com.innova.care4u.Model.PatientDB;
import com.innova.care4u.fingerPrintDeals.FingerPrintActivity;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;



public class RegisterNewPatient extends AppCompatActivity {
    EditText fullname, parentName,phoneNumber,dateOfBirth,locationEdit;
    Button registerButton;
    ProgressDialog progressDialog;

    private PatientDB mPatientDB;
    private Patient mCurrPatient;
    private enum Mode {ADD_PAT, UPDATE_PAT, INVALID_PAT};
    Mode mMode;

    public static final int MY_PERMISSION_REQUEST = 103;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_patient);

        getDynamicFilePermission(); // get permission to read and write files

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
//                if (!RegisterNewPatient.this.isFinishing() && progressDialog != null) {
//                    progressDialog.show();
//                }
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (!RegisterNewPatient.this.isFinishing() && progressDialog != null) {
//                            progressDialog.dismiss();
//                            progressDialog.cancel();
//                        }
//                    }
//                }, 2000);

                createPatient();
                Toast.makeText(getBaseContext(), "Patient Registered!", Toast.LENGTH_SHORT).show();
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

    public void backFromRegistration(View view) {
        startActivity(new Intent(RegisterNewPatient.this, MainActivity.class));
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

    public void createPatient()
    {
        mPatientDB = new PatientDB(this);
        mMode= Mode.ADD_PAT;
        SavePatientRecord();
    }

    public void DeleteCurrPatient(View view) {
        Log.d("RegisterPatient", "DeleteCurrPatient()");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure to delete this patient?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked ok button
                mPatientDB.DeletePatient(mCurrPatient);
                RegisterNewPatient.this.onBackPressed();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                RegisterNewPatient.this.onBackPressed();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void update_mode(Mode mode) {
        Log.d("RegisterPatient", "mMode (old) = " + mMode + ", mMode (new) = " + mode);
        mMode = mode;
    }


    private void hideKeyboard() {
        Context context = this;
        View view = getCurrentFocus().getRootView();

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void CancelPatientRecordEdit(View view) {
        Log.d("RegisterPatient", "CancelPatientRecordEdit()");
        hideKeyboard();
        this.onBackPressed();
    }

    public void SavePatientRecord() {
        EditText patname, patphone, patmail;
        String gender;
        RadioButton patgender;

        Log.d("RegisterPatient", "SavePatientRecord()");
        patname = (EditText)findViewById(R.id.fullname);
        patphone = (EditText)findViewById(R.id.phoneNumber);
        patmail = (EditText)findViewById(R.id.parents_name);
        patgender = (RadioButton)findViewById(R.id.radioMale);

        if (patname.getText().toString().length() == 0) {
            //myOnBackPressed();
            this.onBackPressed();
            return;
        }

        if (patgender.isChecked())
            gender = "Male";
        else
            gender = "Female";


        if (mMode == Mode.ADD_PAT) {
            Patient pat = new Patient(patname.getText().toString(), patphone.getText().toString(),
                    patmail.getText().toString(), gender,dateOfBirth.getText().toString(),locationEdit.getText().toString(),null, null);
            mPatientDB.AddPatient(pat);
            Log.d("RegisterPatient", "Added patient '" + pat.Name + "'");
        }
        else if (mMode == Mode.UPDATE_PAT) {
            Patient pat = new Patient(patname.getText().toString(), patphone.getText().toString(),
                    patmail.getText().toString(), gender, mCurrPatient.Pid, mCurrPatient.Uid);
            mPatientDB.UpdatePatient(pat);
            Log.d("RegisterPatient", " Updated patient '" + pat.Name + "'");
        }
        hideKeyboard();
        this.onBackPressed();
    }

    // getting read and write permission to store
    // patient data in database

    public void getDynamicFilePermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // TODO: Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Log.d("READPERMISSIOM", "getDynamicFilePermission() - Needs Explanation!");
                // FIXME: Let us request again..
                ActivityCompat.requestPermissions(this, new String[] {
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        },
                        MY_PERMISSION_REQUEST
                );
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[] {
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        },
                        MY_PERMISSION_REQUEST
                );
            }
        }
        else {
            //permission already granted, so go to Login Screen
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    // register the patient
                    mMode = Mode.ADD_PAT;

                } else {
                    // permission denied, boo! Lets exit.
                    finish();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);

        String path = null;
        Uri uri = null;
        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.
//        switch (requestCode) {
//            case LOGIN_ACTIVITY:
//                if (resultCode == Activity.RESULT_OK) {
//                    Log.d(TAG, "onActivityResult()::LOGIN_ACTIVITY");
//                    String strLoginResult = resultData.getStringExtra("login result");
//
//                    if (strLoginResult.equalsIgnoreCase("login exit")) {
//                        finish();
//                    } else {
//                        ActivityCompat.invalidateOptionsMenu(MainActivity.this);
//                        update_mode(Mode.REND_PAT); // will be rendered inside onResume
//                    }
//                }
//                break;
//
//            case STATISTICS_ACTIVITY:
//                if (resultCode == Activity.RESULT_OK) {
//                    Log.d(TAG, "onActivityResult()::STATISTICS_ACTIVITY");
//                    PatStatistics pstat = (PatStatistics)
//                            resultData.getSerializableExtra("patient statistics");
//                    Intent intent = new Intent(this, PatStatisticsActivity.class);
//                    intent.putExtra("patient statistics", pstat);
//                    startActivity(intent);
//                }
//                break;
//        }
    }
}
