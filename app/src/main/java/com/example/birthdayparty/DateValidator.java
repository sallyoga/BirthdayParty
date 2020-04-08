package com.example.birthdayparty;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class DateValidator {

    private DateTimeFormatter dateFormatter;

    DateValidator(DateTimeFormatter dateFormatter) {
        this.dateFormatter = dateFormatter;
    }

    public boolean isValid(String dateStr) {
        try {
            this.dateFormatter.parse(dateStr);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }
}
