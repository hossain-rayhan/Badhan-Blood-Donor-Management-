package com.appsomehow.badhan.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

public class DateFormatter {
    public static String getStringFromDate(Date date){
        return "" + date.getDate() + "/" + (date.getMonth()+1) + "/" + (date.getYear() + 1900);
    }

    public static Date getDateFromString(String dateInString) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = simpleDateFormat.parse(dateInString);
        return date;
    }

    public static class CalendarDateWithoutTimeComparator implements Comparator<Calendar> {

        public int compare(Calendar cal1, Calendar cal2) {
            if(cal1.get(Calendar.YEAR) != cal2.get(Calendar.YEAR)) {
                return cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
            } else if (cal1.get(Calendar.MONTH) != cal2.get(Calendar.MONTH)) {
                return cal1.get(Calendar.MONTH) - cal2.get(Calendar.MONTH);
            }
            return cal1.get(Calendar.DAY_OF_MONTH) - cal2.get(Calendar.DAY_OF_MONTH);
        }
    }

}
