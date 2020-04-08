package com.example.birthdayparty;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.birthdays_list)
    EditText birthdaysListEditText;

    @BindView(R.id.get_parties_button)
    Button getPartiesButton;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.birthDay_parties)
    TextView partiesText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getPartiesButton.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            getPartiesButton.setEnabled(false);
            String birthdaysListStr = birthdaysListEditText.getText().toString();
            if (!birthdaysListStr.isEmpty()) {
                String partiesString =
                        findNextMonthParties(birthdaysListStr, new Date());
                if (partiesString.isEmpty()) {
                    birthdaysListEditText.setError("Input is not valid!");
                } else {
                    partiesText.setText(partiesString);
                }
            }
            progressBar.setVisibility(View.GONE);
            getPartiesButton.setEnabled(true);
        });
    }

    public String findNextMonthParties(String birthdaysListStr, Date currentDate) {
        if (currentDate == null || birthdaysListStr.isEmpty()) {
            return null;
        }
        ArrayList<BirthdayInfo> birthdayInfoArrayList;
        birthdayInfoArrayList = getBirthdaysArrayList(birthdaysListStr, currentDate);
        if (birthdayInfoArrayList != null) {
            ArrayList<BirthdayParty> parties = findParties(currentDate, birthdayInfoArrayList);
            preferSaturday(parties);
            return prepareOutputString(parties);
        } else {
            return null;
        }
    }

    private String prepareOutputString(ArrayList<BirthdayParty> parties) {

        StringBuilder partiesOutput = new StringBuilder();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (BirthdayParty party : parties) {
            StringBuilder partyStr = new StringBuilder();
            if (party.getBirthdayInfoArrayList() != null && party.getBirthdayInfoArrayList().size() > 0) {
                partyStr = new StringBuilder(dateFormat.format(party.getWeekendDay()));
                for (BirthdayInfo birthdayInfo : party.getBirthdayInfoArrayList()) {
                    partyStr.append(" ").append(birthdayInfo.getName());
                    partyStr.append(",");
                }
            }
            if (partyStr.length() > 0) {
                partyStr.deleteCharAt(partyStr.length() - 1); // remove comma at the end of the line
                partiesOutput.append(partyStr);
            }

            partiesOutput.append("\n");
        }

        return partiesOutput.toString();
    }

    private void preferSaturday(ArrayList<BirthdayParty> parties) {
        for (BirthdayParty party : parties) {
            boolean isSunday = false;
            for (BirthdayInfo birthdayInfo : party.getBirthdayInfoArrayList()) {

                if (birthdayInfo.getThisYearBirthDay().getDay() == 0) {
                    isSunday = true;
                    break;
                }
            }
            if (!isSunday) {
                // early to Saturday
                party.setWeekendDay(CalenderHelper.getPreviousDay(party.getWeekendDay().getTime()));
            }
        }
    }


    private ArrayList<BirthdayParty> findParties(Date currentDate, ArrayList<BirthdayInfo> birthdayInfoArrayList) {

        ArrayList<BirthdayParty> birthdayPartyArrayList = new ArrayList<>();
        ArrayList<Date> weekendsArrayList = getWeekendsArrayList(currentDate);

        int lastBirthdayIndex = 0;
        for (int i = 0; i < weekendsArrayList.size(); i++) {

            BirthdayParty birthdayParty = new BirthdayParty(weekendsArrayList.get(i));

            while (lastBirthdayIndex < birthdayInfoArrayList.size() &&
                    (birthdayInfoArrayList.get(lastBirthdayIndex).getThisYearBirthDay().before(weekendsArrayList.get(i)) ||
                            (birthdayInfoArrayList.get(lastBirthdayIndex).getThisYearBirthDay().equals(weekendsArrayList.get(i))))) {

                if (i == 0) {
                    final long days = CalenderHelper.getDaysBetween(weekendsArrayList.get(i),
                            birthdayInfoArrayList.get(lastBirthdayIndex).getThisYearBirthDay());
                    if (days <= 5) {
                        birthdayParty.getBirthdayInfoArrayList().add(birthdayInfoArrayList.get(lastBirthdayIndex));
                    }

                } else {
                    birthdayParty.getBirthdayInfoArrayList().add(birthdayInfoArrayList.get(lastBirthdayIndex));
                }
                lastBirthdayIndex++;
            }
            if (birthdayParty.getBirthdayInfoArrayList().size() > 0) {
                birthdayPartyArrayList.add(birthdayParty);
            }
        }

        return birthdayPartyArrayList;
    }

    private ArrayList<BirthdayInfo> getBirthdaysArrayList(String birthdaysListStr, Date currentDate) {

        ArrayList<BirthdayInfo> birthdayInfoArrayList = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("YYYY-MM-dd")
                .withResolverStyle(ResolverStyle.STRICT);
        DateValidator validator = new DateValidator(dateFormatter);

        String[] lines = birthdaysListStr.split("\\r?\\n");

        for (String line : lines) {
            String[] arr = line.split(" ", 2);

            if (arr.length < 2 || arr[0] == null || arr[1] == null) {
                return null;
            }
            String birthDate = arr[0];
            String name = arr[1];


            if (validator.isValid(birthDate) && name.matches("[a-zA-Z]+")) {
                String thisYearBdStr;
                if (birthDate.substring(5, 7).equals("12")) {
                    thisYearBdStr = CalenderHelper.getYear(currentDate) - 1+
                            "-" + birthDate.substring(5, birthDate.length());
                } else {
                    thisYearBdStr = CalenderHelper.getYear(currentDate) +
                            "-" + birthDate.substring(5, birthDate.length());
                }
                try {
                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(birthDate);
                    Date thisYearBdDate = new SimpleDateFormat("yyyy-MM-dd").parse(thisYearBdStr);

                    if (date != null &&
                            (date.getMonth() == CalenderHelper.getNextMonth(currentDate)
                                    || date.getMonth() == CalenderHelper.getMonth(currentDate))) {
                        birthdayInfoArrayList.add(new BirthdayInfo(name, date, thisYearBdDate));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    return null;
                }

            } else {
                return null;
            }
        }

        Collections.sort(birthdayInfoArrayList);
        return birthdayInfoArrayList;
    }


    private ArrayList<Date> getWeekendsArrayList(Date currentDate) {

        int year = CalenderHelper.getYear(currentDate);
        int nextMonth = CalenderHelper.getNextMonth(currentDate);

        ArrayList<Date> weekendsArrayList = new ArrayList<>();

        Calendar cal = new GregorianCalendar(year, nextMonth, 1);
        do {
            // get the day of the week for the current day
            int day = cal.get(Calendar.DAY_OF_WEEK);
            if (/*day == Calendar.SATURDAY ||*/  day == Calendar.SUNDAY) {
                // print the day - but you could add them to a list or whatever
                weekendsArrayList.add(cal.getTime());
            }
            // advance to the next day
            cal.add(Calendar.DAY_OF_YEAR, 1);
        } while (cal.get(Calendar.MONTH) == nextMonth);

        return weekendsArrayList;
    }
}