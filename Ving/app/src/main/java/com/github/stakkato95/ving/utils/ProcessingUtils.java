package com.github.stakkato95.ving.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Artyom on 03.02.2015.
 */
public class ProcessingUtils {

    private static final String TODAY = "сегодня ";
    private static final String YESTERDAY = "вчера ";
    private static final String JUST_NOW = "только что";

    private static final int UNIX_DAY = 86400 * 1000;
    private static final int UNIX_MINUTE = 60 * 1000;
    private static final String DATE_FORMAT_PATTERN = "dd MMM yyyy в H:mm";

    public static String getDate(long rawDate) {
        Date date = new Date(rawDate );
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        String stringDate = dateFormat.format(date);
        long currentTime = new Date().getTime();

        if ((currentTime - rawDate) <= UNIX_MINUTE * 5) {
            stringDate = JUST_NOW;
        } else if ((currentTime - rawDate) <= UNIX_DAY) {
            String[] splittedDate = stringDate.split(" ",4);
            stringDate = TODAY + splittedDate[3];
        } else if ((currentTime - rawDate) <= UNIX_DAY * 2) {
            String[] splittedDate = stringDate.split(" ",4);
            stringDate = YESTERDAY + splittedDate[3];
        }
        return stringDate;
    }

}
