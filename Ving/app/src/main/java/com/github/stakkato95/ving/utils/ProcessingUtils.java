package com.github.stakkato95.ving.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Artyom on 03.02.2015.
 */
public class ProcessingUtils {

    private static final int UNIX_DAY = 86400 * 1000;
    private static final String DATE_FORMAT_PATTERN = "dd MMM yyyy в H:mm";

    public static String getDate(long rawDate) {
        Date date = new Date(rawDate *= 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        String stringDate = dateFormat.format(date);
        long currentTime = new Date().getTime();

        if ((currentTime - rawDate) <= UNIX_DAY) {
            String[] splittedDate = stringDate.split(" ",4);
            stringDate = "сегодня " + splittedDate[3];
        } else if ((currentTime - rawDate) <= UNIX_DAY * 2) {
            String[] splittedDate = stringDate.split(" ",4);
            stringDate = "вчера " + splittedDate[3];
        }
        return stringDate;
    }

}
