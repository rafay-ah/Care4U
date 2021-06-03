package com.innova.care4u;

/**
 * Created by Oluwatobi on 7/16/2037.
 */

public class HistoricalPojo {
    String title, cost,hospital_name, date;

    public HistoricalPojo(String title, String cost, String hospital_name, String date) {
        this.title = title;
        this.cost = cost;
        this.hospital_name = hospital_name;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
