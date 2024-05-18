package com.authorizer.helper;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class Helper {

  public static Date stringToDate(String pDate) {
    ZonedDateTime zonedDateTime = ZonedDateTime.parse(pDate).withZoneSameInstant(ZoneId.of("America/Bogota"));
    Date date = Date.from(zonedDateTime.toInstant());

    return date;
  }
}
