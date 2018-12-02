package com.myoung.android.popularmovies.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatUtils {

    public static String convertDatePattern(String dateStr, String srcPattern, String tarPattern) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(srcPattern);
            Date date = dateFormat.parse(dateStr);

            SimpleDateFormat yearFormat = new SimpleDateFormat(tarPattern);
            String result = yearFormat.format(date);  // get year

            return result;
        } catch (ParseException e) {
            return "";
        }
    }

    public static String convertDatePattern(String dateStr, String srcPattern, String tarPattern, Locale locale) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(srcPattern);
            Date date = dateFormat.parse(dateStr);

            SimpleDateFormat yearFormat = new SimpleDateFormat(tarPattern, locale);
            String result = yearFormat.format(date);  // get year

            return result;
        } catch (ParseException e) {
            return dateStr;
        }
    }
}
