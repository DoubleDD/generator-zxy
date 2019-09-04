package <%= package %>.<%= project %>.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author <%= user %>
 */
public class DateUtils {

    private DateUtils() {
    }

    /**
     * 日期时间格式
     */
    public static final String YYYYMMDD            = "yyyyMMdd";
    public static final String YYMMDDHHMMSS        = "yyMMddHHmmss";
    public static final String YYYYMMDDHHMMSS      = "yyyyMMddHHmmss";
    public static final String YYYY_MM_DD          = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_MM    = "yyyy-MM-dd HH:mm";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";


    /**
     * 字符串格式的时间转时间戳。
     * 支持以下格式的时间字符串，* 代表任意特殊字符
     * <p>
     * yyyyMMdd <br/>
     * yyMMddHHmmss <br/>
     * yyyyMMddHHmmss <br/>
     * yyyy{*}MM{*}dd <br/>
     * yyyy{*}MM{*}dd{*}HH{*}mm <br/>
     * yyyy{*}MM{*}dd{*}HH{*}mm{*}ss <br/>
     * <p>
     * 以上格式最终都会转为 yyyyMMddHHmmss 格式
     *
     * @param dateTimeStr 字符串格式的时间<br/>
     * @return
     */
    public static Long getLong(String dateTimeStr) {
        if (isEmpty(dateTimeStr)) {
            return 0L;
        }
        String str    = dateTimeStr.replaceAll("[\\W_]+", "");
        int    length = str.length();
        int    max    = YYYYMMDDHHMMSS.length();
        int    min    = YYYYMMDD.length();
        if (max > length && length >= min) {
            str += String.format("%0" + (max - length) + "d", 0);
        }
        return getMilli(str, YYYYMMDDHHMMSS);
    }

    /**
     * 获取时间的毫秒数<br/>
     * 注意这里的时间格式，中间的月份必须要是两位<br/>
     * 例如：2019-08-10 12:00:00<br/>
     * 2019-8-10 12:00:00 这种是错误的<br/>
     *
     * @param dateTimeStr 格式：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Long getMilli(String dateTimeStr) {
        return getMilli(dateTimeStr, YYYY_MM_DD_HH_MM_SS);
    }

    public static Long getMilli(String dateTimeStr, String format) {
        if (isEmpty(dateTimeStr)) {
            return 0L;
        }
        DateTimeFormatter formatter = getFormatter(format);
        if (YYYY_MM_DD.equals(format)) {
            LocalDate localDate = LocalDate.parse(dateTimeStr, formatter);
            return getMilli(localDate);
        } else {
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);
            return getMilli(dateTime);
        }
    }

    /**
     * 获取时间的毫秒数
     *
     * @param dateTime
     * @return
     */
    public static Long getMilli(LocalDateTime dateTime) {
        return dateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 日期转时间戳
     *
     * @param localDate
     * @return
     */
    public static Long getMilli(LocalDate localDate) {
        Date date = Date.from(localDate.atStartOfDay(ZoneOffset.ofHours(8)).toInstant());
        return date.getTime();
    }

    public static String formatTime(String format) {
        return formatTime(format, LocalDateTime.now());
    }


    public static String formatTime(String format, Long timestamp) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
        return formatTime(format, dateTime);
    }

    public static String formatTime(String format, LocalDateTime dateTime) {
        DateTimeFormatter formatter = getFormatter(format);
        return dateTime.format(formatter);
    }

    private static DateTimeFormatter getFormatter(String format) {
        return DateTimeFormatter.ofPattern(format);
    }

    private static boolean isEmpty(String str) {
        return null == str || "".equals(str);
    }
}