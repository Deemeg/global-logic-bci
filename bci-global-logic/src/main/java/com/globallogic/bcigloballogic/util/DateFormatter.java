package com.globallogic.bcigloballogic.util;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class DateFormatter {

    public static String formatTimeStamp(Timestamp timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
        Date date = new Date(timestamp.getTime());
        return sdf.format(date);
    }
}
