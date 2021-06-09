package com.innova.care4u;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.innova.care4u.Model.ListOrder;
import com.innova.care4u.Model.Patient;
import com.innova.care4u.Model.PatientDB;

import java.util.List;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class ViewPatients extends AppCompatActivity {
    private String mSrchstr;
    private Boolean initialPatRenderingDone = false;
    private PatientDB patientDB;
    private PatRecyclerAdapter mPatRcAdapter;
    private List<Patient> mPatientList;
    private int currLayout;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private Patient mCurrPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patients);
        loadToolabar();

        patientDB = new PatientDB(this);

        renderPatientview();
    }

    public void loadToolabar()
    {
        // to make the visisbility of sigout and
        // QR code generator icon gone..

        Toolbar toolbar = (Toolbar) findViewById(R.id.viewpatient_toolbar);
        ImageView logout = (ImageView) toolbar.findViewById(R.id.logout);
        ImageView qrCode = (ImageView) toolbar.findViewById(R.id.down);

        toolbar.removeView(logout);
        toolbar.removeView(qrCode);
    }

    public void SearchPatNamePhone(View view) {
        EditText srchtxt;

        srchtxt = (EditText) findViewById(R.id.search_txt);
        mSrchstr = srchtxt.getText().toString();

        Log.d("ViewPatients", "SearchPatNamePhone(\"" + mSrchstr + "\")");
        refreshPatRecycleView();

        Button srchbtn = (Button) findViewById(R.id.search_btn);
        if (mSrchstr.length() > 0) {
            srchbtn.setText("Refresh");
        } else {
            srchbtn.setText("Search");
        }

        hideKeyboard();
    }

    public void AddNewPatient(View view) {
        Intent intent = new Intent(this, RegisterNewPatient.class);
        startActivity(intent);
    }

    // interface class
    public static abstract class ClickListener{
        public abstract void onClick(View view, int position);
        public abstract void onLongClick(View view, int position);
    }

    public void renderPatRecycleView(final List<Patient> patlist) {
        // set patient list layout
        currLayout = R.layout.activity_view_patients;
        setContentView(currLayout);
        setTitle("Patient List");
        Log.d("ViewPatients", "renderPatRecycleView()");

        mRecyclerView = (RecyclerView) findViewById(R.id.pat_rv);
        if (mRecyclerView == null) {
            Log.d("ViewPatients", "renderPatRecycleView: mRecylerView is null!!");
            return;
        }
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mPatRcAdapter = new PatRecyclerAdapter(mPatientList);
        mRecyclerView.addItemDecoration(new DividerItemDecorator(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mPatRcAdapter);

        mPatRcAdapter.notifyDataSetChanged();

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                // show patient history view
                Log.d("ViewPatients", "onClick()");
                mCurrPatient = patlist.get(position);
                if (!mCurrPatient.Name.equals("Empty")) {
                    Intent intent = new Intent(ViewPatients.this, AttendNewPatient.class);
                    intent.putExtra("patient", mCurrPatient);
//                    intent.putExtra("doctor", mDoctor);
                    startActivity(intent);
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                // edit patient data
                mCurrPatient = patlist.get(position);
                //EditPatientRecord();
                Intent intent = new Intent(ViewPatients.this, AttendNewPatient.class);
                intent.putExtra(EXTRA_MESSAGE, "update patient");
                intent.putExtra("patient", mCurrPatient);
                startActivity(intent);
            }
        }));
        initialPatRenderingDone = true;

        EditText srchBox = (EditText) findViewById(R.id.search_txt);
        srchBox.setText(mSrchstr);
        srchBox.invalidate();
        srchBox.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                boolean handled = false;
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                        case KeyEvent.KEYCODE_SEARCH:
                            SearchPatNamePhone(v.getRootView());
                            handled = true;
                        default:
                            Log.d("ViewPatients", " setOnKeyListener search patient: unknown key " + keyCode);
                            break;
                    }
                }
                return handled;

            }
        });
    }

    private void hideKeyboard() {
        Context context = this;
        View view = getCurrentFocus().getRootView();

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void refreshPatRecycleView() {
        if (!initialPatRenderingDone) {
            return;
        }

        setTitle("Patients List");
        Log.d("ViewPatient", "refreshPatRecycleView()");

        mPatientList.clear();
        mPatientList.addAll(patientDB.GetPatientList(mSrchstr, ListOrder.REVERSE));
        mPatRcAdapter.notifyDataSetChanged();
//        update_mode(Mode.VIEW_PAT);
    }

    public void renderPatientview() {
        mPatientList = patientDB.GetPatientList(null, ListOrder.REVERSE);
        renderPatRecycleView(mPatientList);
    }
    private void myOnBackPressed() {
        refreshPatRecycleView();
    }

    public void QueryBeforeHandleBackPress() {
        Log.d("ViewPatients", "QueryBeforeHandleBackPress: mMode = " );
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to save this record?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked ok button
                myOnBackPressed();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                myOnBackPressed();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


}