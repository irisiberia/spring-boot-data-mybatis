package com.example.springbootdatamybatis.mozart.common.json;

/**
 * @Author he.zhou
 * @Date 2019-10-04 15:42
 **/
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.function.Supplier;

public class JacksonDateFormat extends DateFormat {
    public static final String PATTERN_YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_YYYYMMDDHHMMSSSSS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String PATTERN_YYYYMMDD = "yyyy-MM-dd";
    public static final String PATTERN_HHMMSS = "HH:mm:ss";
    public static final String PATTERN2_HHMMSSSSS = "HH:mm:ss.SSS";
    public static final String PATTERN_YYYYMMDDHHMMSSSSSZ = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private static final String[] patterns = new String[]{"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd", "HH:mm:ss", "HH:mm:ss.SSS", "yyyy-MM-dd'T'HH:mm:ss.SSSZ"};
    private static final ThreadLocal<SimpleDateFormat> dateFormats = ThreadLocal.withInitial(() -> {
        return new SimpleDateFormat();
    });
    private static Calendar CALENDAR = new GregorianCalendar();
    private static NumberFormat NUMBER_FORMAT = new DecimalFormat();
    private final String formatPattern;

    public JacksonDateFormat(String formatPattern) {
        this.numberFormat = NUMBER_FORMAT;
        this.calendar = CALENDAR;
        this.formatPattern = formatPattern;
    }

    public static Date parseDateStrictly(String str, String... parsePatterns) throws ParseException {
        return parseDateWithLeniency(str, parsePatterns);
    }

    private static Date parseDateWithLeniency(String str, String[] parsePatterns) throws ParseException {
        if (str != null && parsePatterns != null) {
            SimpleDateFormat parser = (SimpleDateFormat)dateFormats.get();
            parser.setLenient(false);
            ParsePosition pos = new ParsePosition(0);
            String[] var4 = parsePatterns;
            int var5 = parsePatterns.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String parsePattern = var4[var6];
                String pattern = parsePattern;
                if (parsePattern.endsWith("ZZ")) {
                    pattern = parsePattern.substring(0, parsePattern.length() - 1);
                }

                parser.applyPattern(pattern);
                pos.setIndex(0);
                String str2 = str;
                if (parsePattern.endsWith("ZZ")) {
                    str2 = str.replaceAll("([-+][0-9][0-9]):([0-9][0-9])$", "$1$2");
                }

                Date date = parser.parse(str2, pos);
                if (date != null && pos.getIndex() == str2.length()) {
                    return date;
                }
            }

            throw new ParseException("Unable to parse the date: " + str, -1);
        } else {
            throw new IllegalArgumentException("Date and Patterns must not be null");
        }
    }

    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        SimpleDateFormat format = (SimpleDateFormat)dateFormats.get();
        format.setLenient(false);
        format.applyPattern(this.formatPattern);
        return format.format(date, toAppendTo, fieldPosition);
    }

    public Date parse(String source, ParsePosition pos) {
        try {
            return this.parse(source);
        } catch (ParseException var4) {
            return null;
        }
    }

    public Date parse(String source) throws ParseException {
        return parseDateStrictly(source, patterns);
    }

    public Object clone() {
        return this;
    }

    public String toString() {
        return this.getClass().getName();
    }
}
