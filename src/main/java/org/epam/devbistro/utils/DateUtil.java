package org.epam.devbistro.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static String getFormattedDateInFutureFromToday(int days){
        LocalDate date = LocalDate.now().plusDays(days);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(dateFormatter);
    }
}
