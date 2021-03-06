package com.innova.care4u.Model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Treatment implements Serializable {
    public Patient patient;
    public String date;
    public String complaint;
    public  String prescription;
    public String cost;
    public String tid;

    public Treatment(Patient pat, String id, String comp, String pres, String doc) {
        // get current date and  convert to simple readable format
        Date today = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        date = dateFormat.format(today);

        patient = pat;
        tid = id;
        if (comp != null)
            complaint = comp.replace("'","");
        if (pres != null)
            prescription = pres.replace("'","");
        cost = doc;
    }
}