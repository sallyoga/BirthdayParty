package com.example.birthdayparty;

import java.util.ArrayList;
import java.util.Date;

public class BirthdayParty {

    private ArrayList<BirthdayInfo> birthdayInfoArrayList = new ArrayList<>();

    private Date weekendDay;

    public BirthdayParty(Date weekendDay) {
        this.weekendDay = weekendDay;
    }

    public ArrayList<BirthdayInfo> getBirthdayInfoArrayList() {
        return birthdayInfoArrayList;
    }


    public Date getWeekendDay() {
        return weekendDay;
    }

    public void setWeekendDay(Date weekendDay) {
        this.weekendDay = weekendDay;
    }
}
