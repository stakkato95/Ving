package com.github.stakkato95.ving.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Artyom on 03.02.2015.
 */
public class ProcessingUtils {

    private static final String TODAY = "сегодня ";
    private static final String YESTERDAY = "вчера ";
    private static final String DAY_BEFORE_YESTERDAY = "позавчера ";
    private static final String JUST_NOW = "только что";

    private static final int UNIX_DAY = 86400;
    private static final int UNIX_HOUR = 3600;
    private static final int UNIX_MINUTE = 60;
    private static final String DATE_FORMAT_PATTERN = "dd MMM yyyy в H:mm";

    public static String getDate(long longDate) {
        Date currentDate = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(currentDate);

        int hoursOfNewDay = calendar.get(GregorianCalendar.HOUR_OF_DAY);
        int minsOfNewDay =  calendar.get(GregorianCalendar.MINUTE);
        int secsOfNewDay = calendar.get(GregorianCalendar.SECOND);
        int timeOfNewDay = hoursOfNewDay * UNIX_HOUR + minsOfNewDay * UNIX_MINUTE + secsOfNewDay;
        long currentTime;
        long newDay = (currentTime = currentDate.getTime() / 1000) - timeOfNewDay;

        Date rawDate = new Date(longDate * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        String stringDate = dateFormat.format(rawDate);

        if ((currentTime - longDate) <= UNIX_MINUTE * 5) {
            stringDate = JUST_NOW;
        } else if ((currentTime - longDate) <= timeOfNewDay) {
            String[] splittedDate = stringDate.split(" ",4);
            stringDate = TODAY + splittedDate[3];
        } else if ((newDay - longDate) <= UNIX_DAY) {
            String[] splittedDate = stringDate.split(" ",4);
            stringDate = YESTERDAY + splittedDate[3];
        } else if ((newDay - longDate) <= UNIX_DAY * 2) {
            String[] splittedDate = stringDate.split(" ",4);
            stringDate = DAY_BEFORE_YESTERDAY + splittedDate[3];
        }
        return stringDate;
    }

}
