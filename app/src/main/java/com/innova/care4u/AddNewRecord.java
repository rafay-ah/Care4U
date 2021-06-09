package com.innova.care4u;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.innova.care4u.Model.Patient;
import com.innova.care4u.Model.Treatment;
import com.innova.care4u.Model.TreatmentDB;


public class AddNewRecord extends AppCompatActivity {
    EditText title, content, cost;
    Button newRecord;
    ProgressDialog progressDialog;
    private Treatment mCurrTreatment;
    private TreatmentDB mTreatmentDB;
    Patient mCurrPatient;
    private String TAG = "AddTreatment";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_health_record);
        getIntentData();

        mTreatmentDB = new TreatmentDB(this);
        title  = (EditText) findViewById(R.id.treatmentTitle);
        content = (EditText) findViewById(R.id.treatmentContent);
        cost = (EditText) findViewById(R.id.treatmentCost);
        AddNewTreatment();

        progressDialog  = new ProgressDialog(this);
        progressDialog.setMessage("Registering a new patient");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

        newRecord  = (Button) findViewById(R.id.addTreatment);
        newRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.dismiss();
                progressDialog.cancel();
                Toast.makeText(AddNewRecord.this, "Adding new record successful", Toast.LENGTH_SHORT).show();
                finish();

            }
        });

    }
    void getIntentData()
    {
        mCurrPatient = (Patient) getIntent().getSerializableExtra("patient");
        Toast.makeText(AddNewRecord.this, " Patient : " + mCurrPatient.Name,
                Toast.LENGTH_LONG).show();
    }

    public void AddNewTreatment() {
        // Add date according to the format defined in Treatment class
        Treatment treat = new Treatment(null, null, null, null, null);
        EditText date = (EditText) findViewById(R.id.treat_date);
        date.setText(treat.date);
        EditText compl = (EditText) findViewById(R.id.treatmentTitle);
        compl.requestFocus();

    }

    public void SaveTreatmentRecord(View view) {
        EditText comp, pres, date;

        comp = (EditText)findViewById(R.id.treatmentTitle);
        pres = (EditText)findViewById(R.id.treatmentContent);
        date = (EditText)findViewById(R.id.treat_date);

        if (comp.getText().toString().length() == 0 && pres.getText().toString().length() == 0) {
            return;
        }


            Treatment treat = new Treatment(mCurrPatient, "1", comp.getText().toString(),
                    pres.getText().toString(), cost.getText().toString());
            treat.date = date.getText().toString();
            mTreatmentDB.AddTreatment(treat);
            Log.d(TAG, "Added treatment for " + treat.patient.Name);

//        else if (mMode == Mode.EDIT_TREAT) {
//            Treatment treat = new Treatment(mCurrPatient, mCurrTreatment.tid,
//                    comp.getText().toString(), pres.getText().toString(), mDoctor.name);
//            treat.date = date.getText().toString();
//            mTreatmentDB.UpdateTreatment(treat);
//            Log.d(TAG, "Updated treatment for " + treat.patient.Name);
//        }

        this.onBackPressed();
    }
}
