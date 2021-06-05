package com.innova.care4u.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatatypeMismatchException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PatientDB extends SQLiteOpenHelper {
    public static final String PATIENT_LIST = "patientlist";
    public static final String PKEY = "_id";
    public static final String MAIN_DATABASE = "database.db";
    public static final int DATABASE_VERSION = 4;

    private Context mContext;
    private boolean mDbChanged = false;
    private String TAG = "PatientDB";
    private String strStatMajor = "Idle";
    private String strStatMinor = "";


    public PatientDB(Context context) {
        super(context, MAIN_DATABASE, null, DATABASE_VERSION);
        mContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase database) {
        String query = getCreateTableStr(PATIENT_LIST);
        Log.d(TAG, query);
        database.execSQL(query);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(PatientDB.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");

        // TODO: In future we should add saving/exporting old tables before delete
        String query;
        List<Patient> patientList = GetPatientList(null, ListOrder.ASCENDING);

        // Delete all treatment history of all patients
        for (Patient pat: patientList) {
            query = "DROP TABLE IF EXISTS '" + pat.Uid + "'";
            db.execSQL(query);
        }

        // Delete the patient table and then create it in the end
        query = "DROP TABLE IF EXISTS " + PATIENT_LIST;
        db.execSQL(query);
        onCreate(db);
        mDbChanged = true;
    }


    // Patient table creation sql statement
    private String getCreateTableStr(String table) {
        String query, tablename;

        tablename = table;
        query = "CREATE TABLE IF NOT EXISTS " + tablename + " ( " + PKEY +
                " INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(100), phone VARCHAR(30), " +
                " parentname VARCHAR(60),dob VARCHAR(60),location VARCHAR(60), gender VARCHAR(20), uid VARCHAR(100) UNIQUE )";

        return query;
    }


    // return 1 on success, 0 on failure
    public int AddPatient(Patient pat) {
        int retval;
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);
        retval = AddPatientToDB(pat, db);
        db.close();

        return retval;
    }


    private int AddPatientToDB(Patient pat, SQLiteDatabase db) {
        String uid, tablename;
        int retval = 1;

        // new patient added from UI
        tablename = PATIENT_LIST;
        if (pat.Uid == null || pat.Uid.equals("")) {
            uid = (pat.Name + "_" + new Date()).replaceAll("[^a-zA-Z0-9]+", "_");
        }
        else {
            uid = pat.Uid;
        }

        String query = "INSERT INTO " + tablename + " (name, phone, parentname, dob, location, gender, uid) VALUES ('" +
                pat.Name + "', '" + pat.Phone +"', '" + pat.ParentName + "', '" + pat.Gender + "', '" + pat.DOB + "', '" + pat.Location +
                "', '" + uid + "')";

        Log.d(TAG, query); // add new patient to database
        try {
            db.execSQL(query);
            mDbChanged = true;
        }
        catch (SQLException mSQLException) {
            if(mSQLException instanceof SQLiteConstraintException){
                //some toast message to user.
                Log.d(TAG, "AddPatient: SQLiteConstraintException");
            }else if(mSQLException instanceof SQLiteDatatypeMismatchException) {
                //some toast message to user.
                Log.d(TAG, "AddPatient: SQLiteDatatypeMismatchException");
            }else {
                // Getting Disk I/O error. Let us give a 2nd chance!
                db.execSQL(query);
                //throw mSQLException;
            }
            retval = 0; //add failure!!
        }

        /* Let us create treatment table when we add a treatment, not here
        // If this is new patient, then create Treatment Table
        if (retval > 0) {
            String treatTable;
            TreatmentDB treatDB = new TreatmentDB(mContext);
            treatTable = uid;
            treatDB.createTreatmentTableIfNotExist(db, treatTable);
        }
        */

        return retval;
    }


    public void UpdatePatient(Patient pat) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + PATIENT_LIST + " SET name = '" + pat.Name + "', phone = '" + pat.Phone +
                "', parentname = '" + pat.ParentName + "', gender = '" + pat.Gender + "' WHERE uid = '" +
                pat.Uid + "';";

        Log.d(TAG, query);
        db.execSQL(query);
        mDbChanged = true;
        db.close();
    }


    public void DeletePatient(Patient pat) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query;

        // delete treatment history
        query = "DROP TABLE IF EXISTS '" + pat.Uid + "'";
        Log.d(TAG, query);
        db.execSQL(query);

        // delete the patient
        query = "DELETE FROM " + PATIENT_LIST + " WHERE " + PKEY + " = '" + pat.Pid + "';";
        Log.d(TAG, query);
        db.execSQL(query);
        mDbChanged = true;
        db.close();
    }


    public List<Patient> GetPatientList(String search, ListOrder order) {
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db); //// TODO: 15/05/17 Fix this bug!! Following is executed before table is created!!
        List<Patient> list = GetPatientListFromDB(search, order, db);
        db.close();
        return  list;
    }


    private List<Patient> GetPatientListFromDB(String search, ListOrder order, SQLiteDatabase db) {
        List<Patient> patientList = new ArrayList<>();
        String name, phone, parentname, pid, gender, uid, desc;
        String query, tablename;
        Patient pat;
        Cursor res;

        tablename = PATIENT_LIST;

        if (order == ListOrder.REVERSE)
            desc = " DESC";
        else
            desc = "";

        if (search == null)
            query = "SELECT * FROM " + tablename + " ORDER BY " + PKEY + desc;
        else {
            search = search.replaceAll("[^A-Za-z0-9]", "%");
            query = "SELECT * FROM " + tablename +
                    " WHERE name LIKE '%" + search + "%' OR phone LIKE '%" + search + "%'" +
                    " ORDER BY " + PKEY + desc;
        }

        Log.d(TAG, query);
        res = db.rawQuery(query, null);
        res.moveToFirst();

        if (res.getCount() <= 0) {
            Log.d(TAG, "Query on database '" + db.getPath() + "' returned 0 elements!");
            pat = new Patient("Empty", "", "", "", "", "");
            patientList.add(pat);

            res.close();
            return patientList;
        }

        Log.d(TAG, "Number of patients = "+res.getCount());
        while(!res.isAfterLast()){
            name = res.getString(res.getColumnIndex("name"));
            phone = res.getString(res.getColumnIndex("phone"));
            parentname = res.getString(res.getColumnIndex("parentname"));
            gender = res.getString(res.getColumnIndex("gender"));
            uid = res.getString(res.getColumnIndex("uid"));
            pid = res.getString(res.getColumnIndex(PKEY));


            //Log.d(TAG, "Name = "+ name + ", Phone: "+phone+", Email = "+email);
            pat = new Patient(name, phone, parentname, gender, pid, uid);
            patientList.add(pat);

            res.moveToNext();
        }

        res.close();
        return patientList;
    }


    public List<Patient> GetPatientListWith(String complaint, String prescription) {
        List<Patient> patientList = new ArrayList<>();
        String name, phone, parentname, pid, gender, uid, desc;
        String query, tablename;
        Patient pat;
        Cursor res;

        SQLiteDatabase db = this.getWritableDatabase();
        TreatmentDB treatDB = new TreatmentDB(mContext);
        tablename = PATIENT_LIST;

        query = "SELECT * FROM " + tablename + " ORDER BY " + PKEY;
        Log.d(TAG, query);
        res = db.rawQuery(query, null);
        res.moveToFirst();

        if ((res.getCount() <= 0) || ((complaint == null) && (prescription == null))) {
            Log.d(TAG, "Query on database '" + db.getPath() + "' returned 0 elements!");
            pat = new Patient("Empty", "", "", "", "", "");
            patientList.add(pat);

            res.close();
            return patientList;
        }

        Log.d(TAG, "Number of patients = "+res.getCount());
        while(!res.isAfterLast()){
            name = res.getString(res.getColumnIndex("name"));
            phone = res.getString(res.getColumnIndex("phone"));
            parentname = res.getString(res.getColumnIndex("parentname"));
            gender = res.getString(res.getColumnIndex("gender"));
            uid = res.getString(res.getColumnIndex("uid"));
            pid = res.getString(res.getColumnIndex(PKEY));


            //Log.d(TAG, "Name = "+ name + ", Phone: "+phone+", ParentName = "+parentname);
            pat = new Patient(name, phone, parentname, gender, pid, uid);
            String treatTableName = pat.Uid;
            if (treatDB.FindTreatments(db, treatTableName, complaint, prescription)) {
                patientList.add(pat);
            }

            res.moveToNext();
        }

        res.close();
        db.close();

        return patientList;
    }

    private boolean isTableExists(SQLiteDatabase db, String tableName)
    {
        if (tableName == null || db == null) {
            return false;
        }

        //String query = "SELECT * FROM " + tableName;
        String query = "select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'";
        Log.d(TAG, query);

        Cursor res = db.rawQuery(query, null);
        if ((res == null) || (res.getCount() <= 0)) {
            Log.d(TAG, "isTableExists() query on "+db.getPath()+" fails!");
            return false;
        }

        Log.d(TAG, "isTableExists() query on "+db.getPath()+" succeeded!");

        return true;
    }


    public void createPatientTableIfNotExist(SQLiteDatabase db, String tablename) {
        String query = getCreateTableStr(tablename);
        Log.d(TAG, query);
        db.execSQL(query);
    }

    // This function copies all patient data from first argument to destination database (4th arg)
    // This function also merges treatments by checking for duplicate entries
    private int CopyPatListToDB(List<Patient> patList, TreatmentDB treatDB, SQLiteDatabase sdb,
                                SQLiteDatabase ddb) {
        int copy_count = 0;
        String src_table;
        //TreatmentDB treatDB = new TreatmentDB(mContext);
        List<Patient> dstPatList = GetPatientListFromDB(null, null, ddb);
        boolean patient_in_dst_db;

        // Merge treatments for every patients
        int i = 0;
        int total = patList.size();
        for (Patient pat: patList) {
            Log.d(TAG, pat.Name);
            patient_in_dst_db = false;
            if (pat.Name.equals("Empty"))
                continue;

            // Check if the patient already exist in destination database
            for (Patient dpat: dstPatList) {
                if ((dpat.Uid.equals(pat.Uid))) {
                    patient_in_dst_db = true;
                    break;
                }
            }

            if (!patient_in_dst_db) {
                // Add patient details to destination database
                createPatientTableIfNotExist(ddb, PATIENT_LIST);
                copy_count += AddPatientToDB(pat, ddb);
                dstPatList.add(pat); // this will help removing any redundant names in incoming db!
            }

            // Copy treatments if the treatment table is valid
            src_table = pat.Uid;
            if (isTableExists(sdb, src_table)) {
                int treat_cnt = treatDB.mergeTreatmentsToDB(pat, sdb, ddb);
                Log.d(TAG, "Merged " + treat_cnt + " treatments for patient " + pat.Name +
                        " from " + sdb.getPath() + " to " + ddb.getPath() + "!");
            }
            i++;
            strStatMinor = " " + i + " out of " + total;
        }
        Log.d(TAG, "Merged / added " + copy_count + " patients!");

        return copy_count;
    }


    public String mergeDB(String inpath, TreatmentDB treatDB) {
        String storagepath = Environment.getExternalStorageDirectory().toString();
        String newdbpath = storagepath + "/Download/TEMP.db";
        String inputdbpath = storagepath + inpath;
        SQLiteDatabase dbi, dbn; // input, new
        SQLiteDatabase dbm = this.getWritableDatabase(); // main or current

        String query;
        int total_records, copied_records = 0;
        List<Patient> mainPatList, impoPatList;

        // Attach to new and input databases
        dbi = SQLiteDatabase.openDatabase(inputdbpath, null, SQLiteDatabase.OPEN_READONLY);
        dbn = SQLiteDatabase.openOrCreateDatabase(newdbpath, null);

        // Create patientlist table in the new database
        query = getCreateTableStr(PATIENT_LIST);
        Log.d(TAG, query);
        dbn.execSQL(query);

        // Copy patient records to new database
        strStatMajor = "Reading patients (main database): ";
        mainPatList = GetPatientListFromDB(null, ListOrder.ASCENDING, dbm); // from main database
        strStatMajor = "Copying patients (main) to new database: ";
        copied_records += CopyPatListToDB(mainPatList, treatDB, dbm, dbn);
        strStatMajor = "Reading patients (incoming database): ";
        impoPatList = GetPatientListFromDB(null, ListOrder.ASCENDING, dbi); // from input database
        strStatMajor = "Copying patients (incoming) to new database: ";
        copied_records += CopyPatListToDB(impoPatList, treatDB, dbi, dbn);
        total_records = mainPatList.size() + impoPatList.size();
        Log.d(TAG, "Total patients added: "+ copied_records + " out of " + total_records);
        mDbChanged = true;

        dbn.close();
        dbi.close();
        dbm.close();

        return newdbpath;
    }


    public String getStatusString() {
        return strStatMajor + strStatMinor;
    }


    public boolean isDbChanged() {
        Log.d(TAG, "mDbChanged = " + mDbChanged);
        return mDbChanged;
    }


    public void DbSaved() {
        mDbChanged = false;
    }
}
