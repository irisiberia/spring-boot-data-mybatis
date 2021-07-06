package com.example.springbootdatamybatis.mozart.excel.utils;

/**
 * @Author he.zhou
 * @Date 2019-10-04 16:09
 **/


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class DateFormatUtils {

    private static final ThreadLocal<LoadingCache<String, SimpleDateFormat>> dateFormatThreadLocal = new ThreadLocal<LoadingCache<String, SimpleDateFormat>>() {
        @Override
        protected LoadingCache<String, SimpleDateFormat> initialValue() {
            return CacheBuilder.newBuilder().maximumSize(20).expireAfterAccess(10, TimeUnit.SECONDS).build(new CacheLoader<String, SimpleDateFormat>() {
                @Override
                public SimpleDateFormat load(String key) throws Exception {
                    SimpleDateFormat sdf = new SimpleDateFormat(key);
                    sdf.setLenient(false);
                    return sdf;
                }
            });
        }
    };

    public static SimpleDateFormat getDateFormat(String pattern) {
        return dateFormatThreadLocal.get().getUnchecked(pattern);
    }

    public static void destroy() {
        dateFormatThreadLocal.remove();
    }

    public static Date parse2d2M4y(String source) {
        try {
            return getDateFormat("dd/MM/yyyy").parse(source);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parseyyMMddHHmmss(String source) {
        try {
            return getDateFormat("yyMMddHHmmss").parse(source);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parse4y2M2d(String source) {
        try {
            return getDateFormat("yyyy-MM-dd").parse(source);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parse4y2M2d2h2m2s(String source) {
        try {
            return getDateFormat("yyyy-MM-dd HH:mm:ss").parse(source);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parse4y2M2d2h2m(String source) {
        try {
            return getDateFormat("yyyy-MM-dd HH:mm").parse(source);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parse4y2M2dT2h2m2s(String source) {
        try {
            return getDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(source);
        } catch (Exception e) {
            return null;
        }
    }

    public static String format4y2M2d2h2m2s(long date) {
        if (date <= 0)
            return null;
        try {
            return getDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parseHHmm(String source) {
        try {
            return getDateFormat("HH:mm").parse(source);
        } catch (Exception e) {
            return null;
        }
    }

    public static String formatHHmm(Date date) {
        try {
            return getDateFormat("HH:mm").format(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static String formatHHmmss(Date date) {
        try {
            return getDateFormat("HH:mm:ss").format(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parseHHmmWithoutDelimiter(String source) {
        try {
            return getDateFormat("HHmm").parse(source);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parse(String source, String pattern) {
        try {
            return getDateFormat(pattern).parse(source);
        } catch (Exception e) {
            return null;
        }
    }

    public static String format4y2M2d(Date date) {
        return getDateFormat("yyyy-MM-dd").format(date);
    }

    public static String format2d2M4y(Date date) {
        return getDateFormat("dd/MM/yyyy").format(date);
    }

    public static String format2M2d4y(Date date) {
        return getDateFormat("MM/dd/yyyy").format(date);
    }

    public static String format4y2M2d2h2m2s(Date date) {
        return getDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static String format4y2M2dT2h2m2s(Date date) {
        return getDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(date);
    }

    public static String format4y2M2d2h2m(Date date) {
        return getDateFormat("yyyy-MM-dd HH:mm").format(date);
    }

    public static Calendar dateToCalender(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static int diff(Date startDate, Date endDate) {
        return (int) Math.ceil((endDate.getTime() - startDate.getTime()) / 86400000.0D);
    }

    public static int diffHour(Date startDate, Date endDate) {
        return (int) Math.ceil((endDate.getTime() - startDate.getTime()) / 3600000.0D);
    }

    public static int diffSecond(Date startDate, Date endDate) {
        return (int) Math.ceil((endDate.getTime() - startDate.getTime()) / 1000.0D);
    }

    public static Date dateAddDays(Date date, int days) {
        Calendar cal = dateToCalender(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }

    public static String formatyyyyMMdd(Date date) {
        return getDateFormat("yyyyMMdd").format(date);
    }

}

