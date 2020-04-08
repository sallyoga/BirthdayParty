package com.example.birthdayparty;

import android.net.MailTo;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private MainActivity mainActivity = new MainActivity();

    @Test
    public void invalidInputTest() {

        Calendar calendar = new GregorianCalendar(1998, Calendar.DECEMBER, 25);
        Date date = calendar.getTime();

        String birthdaysInfoStr = "";
        String result = mainActivity.findNextMonthParties(birthdaysInfoStr, date);
        assertNull(result);

        birthdaysInfoStr = "1988-12-12 Bob";
        result = mainActivity.findNextMonthParties(birthdaysInfoStr, null);
        assertNull(result);

        birthdaysInfoStr = "1985-02-12";
        result = mainActivity.findNextMonthParties(birthdaysInfoStr, date);
        assertNull(result);

        birthdaysInfoStr = "1988-11-21 1978";
        result = mainActivity.findNextMonthParties(birthdaysInfoStr, date);
        assertNull(result);

        birthdaysInfoStr = "Bob, Alice";
        result = mainActivity.findNextMonthParties(birthdaysInfoStr, date);
        assertNull(result);

        birthdaysInfoStr = "22-05-78 Alice";
        result = mainActivity.findNextMonthParties(birthdaysInfoStr, date);
        assertNull(result);

        birthdaysInfoStr = "22-05-1978 Alice";
        result = mainActivity.findNextMonthParties(birthdaysInfoStr, date);
        assertNull(result);

        birthdaysInfoStr = "1978-05-12, Alice";
        result = mainActivity.findNextMonthParties(birthdaysInfoStr, date);
        assertNull(result);

        birthdaysInfoStr = "1978-05-12 Alice, 1992-03-01 Bob";
        result = mainActivity.findNextMonthParties(birthdaysInfoStr, date);
        assertNull(result);
    }

    @Test
    public void endYearTest() {

        Calendar calendar = new GregorianCalendar(1998, Calendar.DECEMBER, 25);
        Date date = calendar.getTime();

        String birthdaysInfoStr = "1978-01-12 Alice\n1992-01-01 Bob\n1992-01-25 Coral";
        String result = mainActivity.findNextMonthParties(birthdaysInfoStr, date);
        String expected = "1999-01-02 Bob\n" +
                "1999-01-16 Alice\n" +
                "1999-01-30 Coral\n";
        assertEquals(expected, result);
    }

    @Test
    public void endMonthEndYearTest() {
        Calendar calendar = new GregorianCalendar(1998, Calendar.DECEMBER, 25);
        Date date = calendar.getTime();

        String birthdaysInfoStr = "1978-01-12 Alice\n1992-01-01 Bob\n1992-12-29 Coral";
        String result = mainActivity.findNextMonthParties(birthdaysInfoStr, date);
        String expected = "1999-01-02 Coral, Bob\n1999-01-16 Alice\n";
        assertEquals(expected, result);
    }

    @Test
    public void fabruary29Test() {
        Calendar calendar = new GregorianCalendar(2017, Calendar.JANUARY, 25);
        Date date = calendar.getTime();

        String birthdaysInfoStr = "1978-02-01 Alice\n1992-01-29 Bob\n1992-02-29 Coral";
        String result = mainActivity.findNextMonthParties(birthdaysInfoStr, date);
        String expected = "2017-02-04 Alice\n";
        assertEquals(expected, result);
    }

    @Test
    public void saturadySundayTest() {
        Calendar calendar = new GregorianCalendar(2017, Calendar.JANUARY, 25);
        Date date = calendar.getTime();

        String birthdaysInfoStr = "1978-02-05 Alice\n1992-02-05 Bob\n1992-02-02 Coral";
        String result = mainActivity.findNextMonthParties(birthdaysInfoStr, date);
        String expected = "2017-02-05 Coral, Alice, Bob\n";
        assertEquals(expected, result);
    }
}