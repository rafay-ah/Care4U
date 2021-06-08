package com.innova.care4u;

import android.app.Application;

import java.util.ArrayList;


public class ApplicationInstance extends Application {
    private static ArrayList<HistoricalPojo> historicalPojoList;
    @Override
    public void onCreate() {
        super.onCreate();
        historicalPojoList = new ArrayList<>();

        historicalPojoList.add(new HistoricalPojo("Malaria and fever","#12,000", "Dambold matnix hospical", "4th July 2018"));
        historicalPojoList.add(new HistoricalPojo("Cough and catarrh", "#34,000", "Dambold matnix hospital", "9th September 2017" ));
        historicalPojoList.add(new HistoricalPojo("Intense recurring headache", "18,000",  "Amatesh hospital", "2nd, february, 2017"));
        historicalPojoList.add(new HistoricalPojo("Slight Malaria and fever", "#4000", "Dickson clinic", "8th October, 2016"));
        historicalPojoList.add(new HistoricalPojo("Coke sugar addiction", "$4,500", "Emirys medical teaching school", "23rd April 2017"));
        historicalPojoList.add(new HistoricalPojo("Typhoid fever", "$800", "Emirys medical teaching school", "19th November, 2016"));
        historicalPojoList.add(new HistoricalPojo("Leg bone fracture", "#16,000", "Amatesh hospital", "4th, February, 2015"));
        historicalPojoList.add(new HistoricalPojo("Immunization", "Free", "Mobile health pratitioners", " 3rd August 2001"));

    }

    public static ArrayList<HistoricalPojo> getHistoricalPojoList() {
        return historicalPojoList;
    }
}
