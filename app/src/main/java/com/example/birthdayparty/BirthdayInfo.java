package com.example.birthdayparty;

import java.util.Date;

public class BirthdayInfo implements Comparable<BirthdayInfo> {
    private String name;
    private Date birthDate;
    private Date thisYearBirthDay;

    public BirthdayInfo(String name, Date birthDate, Date thisYearBirthDay) {
        this.name = name;
        this.birthDate = birthDate;
        this.thisYearBirthDay = thisYearBirthDay;
    }

    public String getName() {
        return name;
    }

    public Date getThisYearBirthDay() {
        return thisYearBirthDay;
    }

    @Override
    public int compareTo(BirthdayInfo birthdayInfo) {
        return this.getThisYearBirthDay().compareTo(birthdayInfo.getThisYearBirthDay());
    }
}
