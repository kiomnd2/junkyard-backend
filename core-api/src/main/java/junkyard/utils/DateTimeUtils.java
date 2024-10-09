package junkyard.utils;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@UtilityClass
public class DateTimeUtils {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static String toString(LocalDateTime dateTime) {
        return dateTime.format(dateFormatter);
    }

}
