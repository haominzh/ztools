package org.mytoolset.utils.date;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAccessor;

public enum DateFormatEnum {
    LOCAL_MMddyy_DATE_FORMAT("MMddyy"),
    UTC_MMddyy_DATE_FORMAT(LOCAL_MMddyy_DATE_FORMAT, ZoneOffset.UTC),
    LOCAL_INT_DATE_FORMAT("yyyyMMdd"),
    UTC_INT_DATE_FORMAT(LOCAL_INT_DATE_FORMAT, ZoneOffset.UTC),
    LOCAL_ISO8601_WITH_MILLIS_FORMAT("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"), // This is VS timestamp format
    UTC_ISO8601_WITH_MILLIS_FORMAT(LOCAL_ISO8601_WITH_MILLIS_FORMAT, ZoneOffset.UTC),
    LOCAL_ISO8601_FORMAT("yyyy-MM-dd'T'HH:mm:ss'Z'"),
    UTC_ISO8601_DATE_FORMAT(LOCAL_ISO8601_FORMAT, ZoneOffset.UTC),
    LOCAL_ISO8601_DATE_ONLY_FORMAT("yyyy-MM-dd"),
    UTC_ISO8601_DATE_ONLY_FORMAT(LOCAL_ISO8601_DATE_ONLY_FORMAT, ZoneOffset.UTC),
    LOCAL_yyyyMMddHHmmss_DATE_FORMAT("yyyyMMddHHmmss"),
    UTC_yyyyMMddHHmmss_DATE_FORMAT(LOCAL_yyyyMMddHHmmss_DATE_FORMAT, ZoneOffset.UTC),
    LONG_FORMAT_IN_DEFAULT_ZONE("FormatStyle.LONG", FormatStyle.LONG, ZoneId.systemDefault()),
    ISO_INSTANT_IN_DEFAULT_ZONE("DateTimeFormatter.ISO_INSTANT", DateTimeFormatter.ISO_INSTANT, ZoneId.systemDefault());

    private final DateTimeFormatter dateTimeFormatter;
    private final String formatDisplayName;

    DateFormatEnum(String format) {
        this.formatDisplayName = format;
        this.dateTimeFormatter =  DateTimeFormatter.ofPattern(format);
    }

    DateFormatEnum(String displayName, FormatStyle formatStyle, ZoneId zoneId) {
        this.formatDisplayName = displayName;
        this.dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(formatStyle)
                                                  .withZone(zoneId);
    }

    DateFormatEnum(DateFormatEnum dateFormatEnum, ZoneId zoneId) {
        this.formatDisplayName = dateFormatEnum.formatDisplayName;
        this.dateTimeFormatter = dateFormatEnum.getFormatter().withZone(zoneId);
    }

    DateFormatEnum(String displayName, DateTimeFormatter dateTimeFormatter, ZoneId zoneId) {
        this.formatDisplayName = displayName;
        this.dateTimeFormatter = dateTimeFormatter.withZone(zoneId);
    }

    public DateTimeFormatter getFormatter() {
        return this.dateTimeFormatter;
    }

    public String format(TemporalAccessor temporal) {
        return this.dateTimeFormatter.format(temporal);
    }

    public String getFormatDisplayName() {
        return formatDisplayName;
    }

    public boolean canParse(String input) {
        try {
            LocalDate.parse(input, this.dateTimeFormatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
