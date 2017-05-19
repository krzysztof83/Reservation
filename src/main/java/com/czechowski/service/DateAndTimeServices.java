package com.czechowski.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class DateAndTimeServices {

    public static Date datePlusMinutes(Date startDate,Long minutes){
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = startDate.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
        localDateTime=localDateTime.plusMinutes(minutes);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        Date date = Date.from(zonedDateTime.toInstant());
        return date;
    }

    public static Date dateMinusMinutes(Date startDate,Long minutes){
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = startDate.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
        localDateTime=localDateTime.minusMinutes(minutes);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        Date date = Date.from(zonedDateTime.toInstant());
        return date;
    }
}
