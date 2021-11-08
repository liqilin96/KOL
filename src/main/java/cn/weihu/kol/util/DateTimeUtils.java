package cn.weihu.kol.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期和时间的常用类<br>
 * 方法都是static的,可通过类名调用<br>
 */
@Slf4j
public class DateTimeUtils {

    public static final String   DATEPATTERN      = "yyyy-MM-dd";
    public static final String   DATESTRING       = "yyyyMMdd";
    public static final String   TIMEPATTERN      = "HH:mm";
    public static final String   DATETIMEPATTERN  = "yyyy-MM-dd HH:mm:ss";
    public static final String   DATETIMESPATTERN = "yyyy-MM-dd HH:mm";
    public static final String   yyyyMMddHHmmss   = "yyyyMMddHHmmss";
    public static final String   HH_mm_ss         = "HH:mm:ss";
    public static final String   HH_mm            = "HH:mm";
    private static      String[] PARSEPATTERNS    = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM/dd HH", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM.dd HH", "yyyy.MM", "yyyy"};


    // 不允许实例化该类
    private DateTimeUtils() {
    }

    /**
     * 获得系统当前日期时间
     *
     * @return 格式为 yyyy-MM-dd HH:mm:ss
     */
    public static String getNowDateTime() {
        return new SimpleDateFormat(DATETIMEPATTERN).format(new Date());
    }

    /**
     * 获得系统当前日期时间
     *
     * @return 格式为 yyyy-MM-dd HH:mm:
     */
    public static String getNowDateTimes() {
        return new SimpleDateFormat(DATETIMESPATTERN).format(new Date());
    }

    /**
     * 日期型字符串转化为日期 格式
     * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
     * "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
     * "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
     */
    public static Date parseDate(String str) {
        if(str == null) {
            return null;
        }
        try {
            return DateUtils.parseDate(str, PARSEPATTERNS);
        } catch(ParseException e) {
            return null;
        }
    }


    /**
     * 获得系统当前日期
     *
     * @return 格式为 yyyy-MM-dd
     */
    public static String getDate() {
        return getDate(DATETIMEPATTERN);
    }

    public static String getDate(String pattern) {
        return (new SimpleDateFormat(pattern)).format(new Date());
    }

    /**
     * 获得系统当前日期
     *
     * @return 格式为 yyyyMMdd
     */
    public static String getDateToString() {
        return (new SimpleDateFormat(DATESTRING)).format(new Date());
    }

    /**
     * 获取时间字符串
     *
     * @return 格式为 yyyyMMdd
     */
    public static String getDateStr() {
        return (new SimpleDateFormat(DATESTRING)).format(new Date());
    }

    /**
     * 获得指定时间的标准返回格式
     *
     * @param year  指定的年份
     * @param month 指定的月份
     * @param day   指定的日期
     * @return 格式为 yyyy-MM-dd
     */
    public static String getDate(int year, int month, int day) {
        GregorianCalendar calendar = new GregorianCalendar(year, month - 1, day);
        return (new SimpleDateFormat(DATEPATTERN)).format(calendar.getTime());
    }

    /**
     * 根据起始日期、周次、星期获得具体的日期
     *
     * @param firstDate 起始日期
     * @param zc        第几周
     * @param xq        星期几
     * @return
     */
    public static String getDateByWeek(String firstDate, int zc, int xq) {
        String result = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar         cal = Calendar.getInstance(Locale.CHINA);
            cal.setTime(sdf.parse(firstDate));
            int firstWeek = cal.get(Calendar.WEEK_OF_YEAR);//获得起始的周次
            cal.set(Calendar.WEEK_OF_YEAR, (firstWeek + zc) - 1);
            cal.set(Calendar.DAY_OF_WEEK, getXq(xq));
            result = sdf.format(cal.getTime());
        } catch(Exception e) {
            log.error("DateTimeUtils发生错误", e);
        }
        return result;
    }

    public static String getDateOfLastMonday() {
        String result = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar         cal = Calendar.getInstance(Locale.CHINA);
            cal.add(Calendar.WEEK_OF_YEAR, -1);
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            result = sdf.format(cal.getTime());
        } catch(Exception e) {
            log.error("DateTimeUtils发生错误", e);
        }
        return result;
    }

    public static String getDateOfLastSunday() {
        String result = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar         cal = Calendar.getInstance(Locale.CHINA);
            cal.add(Calendar.WEEK_OF_YEAR, -1);
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            result = sdf.format(cal.getTime());
        } catch(Exception e) {
            log.error("DateTimeUtils发生错误", e);
        }
        return result;
    }

    /**
     * 中国的星期与外国星期转换
     *
     * @param xq
     * @return
     */
    public static int getXq(int xq) {
        int result = 0;
        if(xq == 1) {
            result = Calendar.MONDAY;//中国星期一对应国外星期二
        } else if(xq == 2) {
            result = Calendar.TUESDAY;//星期二
        } else if(xq == 3) {
            result = Calendar.WEDNESDAY;//星期三
        } else if(xq == 4) {
            result = Calendar.THURSDAY;//星期四
        } else if(xq == 5) {
            result = Calendar.FRIDAY;//星期五
        } else if(xq == 6) {
            result = Calendar.SATURDAY;//星期六
        } else if(xq == 7) {
            result = Calendar.SUNDAY;//星期日
        }
        return result;
    }

    /**
     * 得到"yyyy年M月d日"格式的日期
     *
     * @return "yyyy年M月d日"格式的日期
     */
    public static String getChineseDate() {
        return (new SimpleDateFormat("yyyy\u5E74M\u6708d\u65E5"))
                .format(new Date());
    }

    /**
     * 得到当前的时间,格式为"HH:mm:ss"
     *
     * @return 当前时间, 格式为"HH:mm:ss"
     */
    public static String getTime() {
        return getTime(HH_mm_ss);
    }

    public static String getTime(String pattern) {
        return (new SimpleDateFormat(pattern)).format(new Date());
    }

    /**
     * 得到当前时间
     *
     * @return 数组格式的当前时间, array[0]为小时, array[1]为分钟 String[]
     */
    public static String[] getTimeForHourMinute() {
        String time[]  = new String[2];
        String timeStr = (new SimpleDateFormat("HH:mm:ss")).format(new Date());
        String temp[]  = timeStr.split(":");
        time[0] = temp[0];
        time[1] = temp[1];
        return time;
    }

    /**
     * 得到当前日期的星期
     *
     * @return 当前日期的星期 String
     */
    public static String getWeekday() {
        return (new SimpleDateFormat("E")).format(new Date());
    }

    /**
     * 得到指定日期的星期
     *
     * @param year  指定的年份
     * @param month 指定的月份
     * @param day   指定的日期
     * @return 日期 String
     */
    public static String getWeekday(int year, int month, int day) {
        GregorianCalendar calendar = new GregorianCalendar(year, month - 1, day);
        return (new SimpleDateFormat("E")).format(calendar.getTime());
    }

    /**
     * 得到当前日期的年份和月份
     *
     * @return 以"yyyy-MM"表示的年份和月份 String
     */
    public static String getYearAndMonth() {
        return (new SimpleDateFormat("yyyy-MM")).format(new Date());
    }

    /**
     * 得到指定日期的年份和月份
     *
     * @param year  指定的年份
     * @param month 指定的月份
     * @param day   指定的日期
     * @return 年份和月份 String
     */
    public static String getYearAndMonth(int year, int month, int day) {
        GregorianCalendar calendar = new GregorianCalendar(year, month - 1, day);
        return (new SimpleDateFormat("yyyy-MM")).format(calendar.getTime());
    }

    /**
     * 得到当前日期是一年中的第几天
     *
     * @return String
     */
    public static String getDateInYear() {
        return (new SimpleDateFormat("DDD")).format(new Date());
    }

    /**
     * 得到指定日期是一年中的第几天
     *
     * @param year  指定的年份
     * @param month 指定的月份
     * @param day   指定的日期
     * @return 指定日期在一年中的第几天 String
     */
    public static String getDateInYear(int year, int month, int day) {
        GregorianCalendar calendar = new GregorianCalendar(year, month - 1, day);
        return (new SimpleDateFormat("DDD")).format(calendar.getTime());
    }

    /**
     * 得到当前日期是一年中的第几个星期
     *
     * @return String
     */
    public static String getWeekInYear() {
        return (new SimpleDateFormat("ww")).format(new Date());
    }

    /**
     * 得到指定日期是一年中的第几个星期
     *
     * @param year  指定的年份
     * @param month 指定的月份
     * @param day   指定的日期
     * @return 指定日期是一年中的第几个星期 String
     */
    public static String getWeekInYear(int year, int month, int day) {
        GregorianCalendar calendar = new GregorianCalendar(year, month - 1, day);
        return (new SimpleDateFormat("ww")).format(calendar.getTime());
    }

    /**
     * 得到指定日期是当前月的第几个星期
     *
     * @return String
     */
    public static String getWeekInMonth() {
        return (new SimpleDateFormat("WW")).format(new Date());
    }

    /**
     * 得到指定日期是所在月份的第几个星期
     *
     * @param year  指定的年份
     * @param month 指定的月份
     * @param day   指定的日期
     * @return 指定日期是所在月份的第几个星期 String
     */
    public static String getWeekInMonth(int year, int month, int day) {
        GregorianCalendar calendar = new GregorianCalendar(year, month - 1, day);
        return (new SimpleDateFormat("WW")).format(calendar.getTime());
    }

    /**
     * 得到当前日期前beforeNum天的日期
     *
     * @param beforeNum 提前量
     * @return String
     */
    public static String getDateByBefore(int beforeNum) {
        Calendar now = Calendar.getInstance();
        now.add(6, -1 * beforeNum);
        return (new SimpleDateFormat(DATESTRING)).format(now.getTime());
    }

    /**
     * 得到指定日期前beforeNum天的日期
     *
     * @param year      指定的年份
     * @param month     指定的月份
     * @param day       指定的日期
     * @param beforeNum 提前量
     * @return String
     */
    public static String getDateByBefore(int year, int month, int day, int beforeNum) {
        GregorianCalendar calendar = new GregorianCalendar(year, month - 1, day);
        calendar.add(6, -1 * beforeNum);
        return (new SimpleDateFormat("yyyy-MM-dd")).format(calendar.getTime());
    }

    /**
     * 得到指定日期前beforeNum天的日期
     *
     * @param dateString 以"yyyy-MM-dd"格式指定的日期
     * @param beforeNum  提前量
     * @return String
     */
    public static String getDateByBefore(String dateString, int beforeNum) {
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = (new SimpleDateFormat("yyyy-MM-dd")).parse(dateString);
            calendar.setTime(date);
        } catch(Exception e) {
            return "";
        }
        calendar.add(6, -1 * beforeNum);
        return (new SimpleDateFormat("yyyy-MM-dd")).format(calendar.getTime());
    }

    /**
     * 得到指定日期后afterNum天的日期
     *
     * @param dateString 以"yyyy-MM-dd"格式指定的日期
     * @param afterNum   偏移量
     * @return String
     */
    public static String getDateByAfter(String dateString, int afterNum) {
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = (new SimpleDateFormat("yyyy-MM-dd")).parse(dateString);
            calendar.setTime(date);
        } catch(Exception e) {
            return "";
        }
        calendar.add(6, afterNum);
        return (new SimpleDateFormat("yyyy-MM-dd")).format(calendar.getTime());
    }

    public static String getWeekdayByBefore(int beforeNum) {
        Calendar now = Calendar.getInstance();
        now.add(6, -1 * beforeNum);
        return (new SimpleDateFormat("E")).format(now.getTime());
    }

    public static String getWeekdayByBefore(int year, int month, int day,
                                            int beforeNum) {
        GregorianCalendar calendar = new GregorianCalendar(year, month - 1, day);
        calendar.add(6, -1 * beforeNum);
        return (new SimpleDateFormat("E")).format(calendar.getTime());
    }

    public static String getDateByAfter(int afterNum) {
        Calendar now = Calendar.getInstance();
        now.add(6, afterNum);
        return (new SimpleDateFormat("yyyy-MM-dd")).format(now.getTime());
    }

    public static String getDateByAfter(int year, int month, int day,
                                        int afterNum) {
        GregorianCalendar calendar = new GregorianCalendar(year, month - 1, day);
        calendar.add(6, afterNum);
        return (new SimpleDateFormat("yyyy-MM-dd")).format(calendar.getTime());
    }

    public static String getWeekdayByAfter(int afterNum) {
        Calendar now = Calendar.getInstance();
        now.add(6, afterNum);
        return (new SimpleDateFormat("E")).format(now.getTime());
    }

    public static String getWeekdayByAfter(int year, int month, int day,
                                           int afterNum) {
        GregorianCalendar calendar = new GregorianCalendar(year, month - 1, day);
        calendar.add(6, afterNum);
        return (new SimpleDateFormat("E")).format(calendar.getTime());
    }

    public static String getDateAfterDate(String date, int afterNum) throws ParseException {
        return getDateAfterDate(date, "yyyyMMdd", "yyyyMMdd", afterNum);
    }

    public static String getDateAfterDate(String date, String afterFormatStr, int afterNum) throws ParseException {
        return getDateAfterDate(date, "yyyyMMdd", afterFormatStr, afterNum);
    }

    public static String getDateAfterDate(String date, String beforFormatStr, String afterFormatStr, int afterNum) throws ParseException {
        SimpleDateFormat sdf      = new SimpleDateFormat(beforFormatStr);
        Date             dt       = sdf.parse(date);
        Calendar         rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        rightNow.add(Calendar.DAY_OF_YEAR, afterNum);//日期加10天
        Date dt1 = rightNow.getTime();

        if(!beforFormatStr.equals(afterFormatStr)) {
            sdf = new SimpleDateFormat(afterFormatStr);
        }
        return sdf.format(dt1);
    }

    public static String getDateOfWeekend() {
        Calendar now = Calendar.getInstance();
        now.add(6, 7 - now.get(7));
        return (new SimpleDateFormat("yyyy-MM-dd")).format(now.getTime());
    }

    public static String getDateOfWeekend(int year, int month, int day) {
        GregorianCalendar calendar = new GregorianCalendar(year, month - 1, day);
        calendar.add(6, 7 - calendar.get(7));
        return (new SimpleDateFormat("yyyy-MM-dd")).format(calendar.getTime());
    }

    public static String getDateOfWeekstart() {
        Calendar now = Calendar.getInstance();
        now.add(6, 1 - now.get(7));
        return (new SimpleDateFormat("yyyy-MM-dd")).format(now.getTime());
    }

    public static String getDateOfWeekstart(int year, int month, int day) {
        GregorianCalendar calendar = new GregorianCalendar(year, month - 1, day);
        calendar.add(6, 1 - calendar.get(7));
        return (new SimpleDateFormat("yyyy-MM-dd")).format(calendar.getTime());
    }

    public static int getDateOfMonthend() {
        Calendar now = Calendar.getInstance();
        return now.getActualMaximum(5);
    }

    public static int getDateOfMonthend(int year, int month, int day) {
        GregorianCalendar calendar = new GregorianCalendar(year, month - 1, day);
        return calendar.getActualMaximum(5);
    }

    public static String getDateBefore(String timeString, int minute) {
        long min = 0L;
        try {
            Date date = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                    .parse(timeString);
            min = date.getTime();
        } catch(Exception exception) {
            log.error("DateTimeUtils发生错误", exception);
        }
        return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(
                min - (minute * 60 * 1000)));
    }

    public static boolean IsOverTime(String timeString, int rating) {
        try {
            Date date = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                    .parse(timeString);
            Date now = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                    .parse((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                                   .format(new Date()));
            long min    = date.getTime();
            long nowmin = now.getTime();
            return nowmin - min > (rating * 60 * 1000);
        } catch(Exception e) {
            return false;
        }
    }

    public static String switchDateFormat(String dateStr) {
        Date date = null;
        try {
            date = (new SimpleDateFormat("yyyy-MM-dd")).parse(dateStr);
        } catch(Exception exception) {
            log.error("DateTimeUtils发生错误", exception);
        }
        return (new SimpleDateFormat("yyyy\u5E74M\u6708d\u65E5")).format(date);
    }

    public static String switchDateFormat(String firstFormat,
                                          String secondFormat, String dateStr) {
        Date date = null;
        try {
            date = (new SimpleDateFormat(firstFormat)).parse(dateStr);
        } catch(Exception exception) {
            log.error("DateTimeUtils发生错误", exception);
        }
        return (new SimpleDateFormat(secondFormat)).format(date);
    }

    public static String getWeekdayByDateString(String dateString) {
        String weekday = "";
        try {
            Date date = (new SimpleDateFormat("yyyy-MM-dd")).parse(dateString);
            weekday = (new SimpleDateFormat("E")).format(date);
        } catch(Exception exception) {
            log.error("DateTimeUtils发生错误", exception);
        }
        return weekday;
    }

    public static String getWeekdayByDate(Date date) {
        return (new SimpleDateFormat("E")).format(date);
    }

    public static String getDateTimeZone() {
        return (new SimpleDateFormat("yyyyMMddHHmmssS")).format(new Date());
    }

    public static String getYear() {
        return (new SimpleDateFormat("yyyy")).format(new Date());
    }

    public static String getMonth() {
        return (new SimpleDateFormat("MM")).format(new Date());
    }

    /**
     * 根据传入yyyy-MM-dd HH:mm:ss格式的日期,获得季度<br>
     * 返回String数组 分别存放当前季度的开始时间,结束时间,当前季度,当前季度的中文形式<br>
     * 例如:
     * <p>
     * <pre>
     *  2008-10-01
     *  2008-12-31
     *  4
     *  2008年第4季度
     * </pre>
     *
     * @param date yyyy-MM-dd HH:mm:ss格式的日期
     * @return
     */
    public static String[] getQuarter(Date date) {
        String record[]     = new String[4];
        String startDateStr = "";
        String endDateStr   = "";
        String quarter      = null;
        String year         = (new SimpleDateFormat("yyyy")).format(date);
        String month        = (new SimpleDateFormat("MM")).format(date);
        int    monthInteger = Integer.parseInt(month);
        if(monthInteger >= 1 && monthInteger <= 3) {
            startDateStr = "-01-01";
            endDateStr = "-03-31";
            quarter = "1";
        }
        if(monthInteger >= 4 && monthInteger <= 6) {
            startDateStr = "-04-01";
            endDateStr = "-06-30";
            quarter = "2";
        }
        if(monthInteger >= 7 && monthInteger <= 9) {
            startDateStr = "-07-01";
            endDateStr = "-09-30";
            quarter = "3";
        }
        if(monthInteger >= 9 && monthInteger <= 12) {
            startDateStr = "-10-01";
            endDateStr = "-12-31";
            quarter = "4";
        }
        record[0] = year + startDateStr;
        record[1] = year + endDateStr;
        record[2] = quarter;
        record[3] = year + "\u5E74\u7B2C" + quarter + "\u5B63\u5EA6";
        return record;
    }

    /**
     * 获得当前季度的相关信息<br>
     * 返回String数组 分别存放当前季度的开始时间,结束时间,当前季度,当前季度的中文形式<br>
     * 例如:
     * <p>
     * <pre>
     *  2008-10-01
     *  2008-12-31
     *  4
     *  2008年第4季度
     * </pre>
     *
     * @return
     */
    public static String[] getNowQuarter() {
        String record[]     = new String[4];
        String startDateStr = "";
        String endDateStr   = "";
        String quarter      = "";
        Date   date         = new Date();
        String year         = (new SimpleDateFormat("yyyy")).format(date);
        String month        = (new SimpleDateFormat("MM")).format(date);
        int    monthInteger = Integer.parseInt(month);
        if(monthInteger >= 1 && monthInteger <= 3) {
            startDateStr = "-01-01";
            endDateStr = "-03-31";
            quarter = "1";
        }
        if(monthInteger >= 4 && monthInteger <= 6) {
            startDateStr = "-04-01";
            endDateStr = "-06-30";
            quarter = "2";
        }
        if(monthInteger >= 7 && monthInteger <= 9) {
            startDateStr = "-07-01";
            endDateStr = "-09-30";
            quarter = "3";
        }
        if(monthInteger >= 9 && monthInteger <= 12) {
            startDateStr = "-10-01";
            endDateStr = "-12-31";
            quarter = "4";
        }
        record[0] = year + startDateStr;
        record[1] = year + endDateStr;
        record[2] = quarter;
        record[3] = year + "\u5E74\u7B2C" + quarter + "\u5B63\u5EA6";
        return record;
    }

    public static String[] getDownQuarter(String date, String quarter)
            throws Exception {
        String record[]     = new String[4];
        String startDateStr = "";
        String endDateStr   = "";
        int    monthInteger = Integer.parseInt(quarter);
        String year = (new SimpleDateFormat("yyyy"))
                .format((new SimpleDateFormat("yyyy-MM-dd")).parse(date));
        int yearInteger = Integer.parseInt(year);
        if(monthInteger == 1) {
            startDateStr = year + "-04-01";
            endDateStr = year + "-06-30";
            quarter = "2";
        }
        if(monthInteger == 2) {
            startDateStr = year + "-07-01";
            endDateStr = year + "-09-30";
            quarter = "3";
        }
        if(monthInteger == 3) {
            startDateStr = year + "-10-01";
            endDateStr = year + "-12-31";
            quarter = "4";
        }
        if(monthInteger == 4) {
            startDateStr = (yearInteger + 1) + "-01-01";
            endDateStr = (yearInteger + 1) + "-03-31";
            quarter = "1";
            year = Integer.toString(yearInteger + 1);
        }
        record[0] = startDateStr;
        record[1] = endDateStr;
        record[2] = quarter;
        record[3] = year + "\u5E74\u7B2C" + quarter + "\u5B63\u5EA6";
        return record;
    }

    public static String[] getUpQuarter(String date, String quarter)
            throws Exception {
        String record[]     = new String[4];
        String startDateStr = "";
        String endDateStr   = "";
        int    monthInteger = Integer.parseInt(quarter);
        String year = (new SimpleDateFormat("yyyy"))
                .format((new SimpleDateFormat("yyyy-MM-dd")).parse(date));
        int yearInteger = Integer.parseInt(year);
        if(monthInteger == 1) {
            startDateStr = (yearInteger - 1) + "-10-01";
            endDateStr = (yearInteger - 1) + "-12-31";
            quarter = "4";
            year = Integer.toString(yearInteger - 1);
        }
        if(monthInteger == 2) {
            startDateStr = year + "-01-01";
            endDateStr = year + "-03-31";
            quarter = "1";
        }
        if(monthInteger == 3) {
            startDateStr = year + "-04-01";
            endDateStr = year + "-06-30";
            quarter = "2";
        }
        if(monthInteger == 4) {
            startDateStr = year + "-07-01";
            endDateStr = year + "-09-30";
            quarter = "3";
        }
        record[0] = startDateStr;
        record[1] = endDateStr;
        record[2] = quarter;
        record[3] = year + "\u5E74\u7B2C" + quarter + "\u5B63\u5EA6";
        return record;
    }

    public static int getExamTime(String firsttime, String secondtime, int m) {
        long record = 0L;
        try {
            Date first = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                    .parse(firsttime);
            Date second = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                    .parse(secondtime);
            Date now = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                    .parse((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                                   .format(new Date()));
            long m_first  = first.getTime();
            long m_second = second.getTime();
            if(m_first == m_second) {
                record = m * 60 * 1000;
            } else {
                record = (m * 60 * 1000) - (now.getTime() - first.getTime());
            }
        } catch(Exception exception) {
            log.error("DateTimeUtils发生错误", exception);
        }
        return (int) record / 60000;
    }

    public static String switchDateStr(String date) {
        String record = "";
        try {
            Date first = (new SimpleDateFormat("yyyy-MM-dd HH")).parse(date);
            record = (new SimpleDateFormat("yyyyMMddHH")).format(first);
        } catch(Exception e) {
            log.error("DateTimeUtils发生错误", e);
        }
        return record;
    }

    public static String switchDateStrForWorkList(String date) {
        String record = "";
        try {
            Date first = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S"))
                    .parse(date);
            record = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                    .format(first);
        } catch(Exception e) {
            log.error("DateTimeUtils发生错误", e);
        }
        return record;
    }

    public static String uniteDateTime(String date, String time) {
        return date + " " + time;
    }

    public static String getOffsetTime(String format, int field, int offset) {
        Calendar calendar = Calendar.getInstance();

        calendar.add(field, offset);
        return new SimpleDateFormat(format).format(calendar.getTime());
    }

    public static String getOffsetTime(String timeString, String format,
                                       int field, int offset) {
        Calendar calendar = Calendar.getInstance();

        calendar.add(field, offset);
        return new SimpleDateFormat(format).format(calendar.getTime());
    }

    public static String getDatePattern() {
        return DATEPATTERN;
    }

    public static final String getDate(Date aDate) {
        SimpleDateFormat df          = null;
        String           returnValue = "";

        if(aDate != null) {
            df = new SimpleDateFormat(DATEPATTERN);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    public static final Date convertStringToDate(String aMask, String strDate) {
        SimpleDateFormat df   = null;
        Date             date = null;
        df = new SimpleDateFormat(aMask);
        try {
            date = df.parse(strDate);
        } catch(ParseException e) {
            e.printStackTrace();
        }
        return (date);
    }

    public static Date convertLongToDate(Long millions) {
        if(millions == null) {
            return null;
        }
        return new Date(millions);
    }

    public static String convertLongToString(Long millions) {

        if(millions == null) {
            return null;
        }
        if(millions.toString().length() == 10) {
            millions = millions * 1000;
        }
        return DateTimeUtils.convertDateTimeToString(new Date(millions));
    }

    public static String convertLongToString2(Long millions) {

        if(millions == null) {
            return null;
        }
        if(millions.toString().length() == 10) {
            millions = millions * 1000;
        }
        return getDateTime(yyyyMMddHHmmss, new Date(millions));
    }

    //public static void main(String[] args){
//        Long l = 1540956008l;
//
//    System.out.println(DateTimeUtils.convertLongToString(l));
//}
    public static Long convertStringToLong(String strDate) throws ParseException {
        return convertStringToDate(strDate).getTime();
    }

    public static Long convertStringToLong(String strDate, String format) {
        return convertStringToDate(format, strDate).getTime();
    }

    /**
     * 把传入的字符串转换为Date
     *
     * @param aMask   传入的字符串的格式
     * @param strDate 时间日期字符串
     * @return
     * @throws ParseException
     */
    public static final Date convertStringToDateTime(String aMask, String strDate) throws ParseException {
        SimpleDateFormat df   = null;
        Date             date = null;
        df = new SimpleDateFormat(aMask);

        try {
            date = df.parse(strDate);
        } catch(ParseException pe) {
            // log.error("ParseException: " + pe);
            throw new ParseException(pe.getMessage(), pe.getErrorOffset());
        }
        return (date);
    }

    /**
     * This method returns the current date time in the format: yyyy/MM/dd HH:MM
     * a
     *
     * @param theTime the current time
     * @return the current date/time
     */
    public static String getTimeNow(Date theTime) {
        return getDateTime(TIMEPATTERN, theTime);
    }

    /**
     * This method returns the current date in the format: yyyy-MM-dd
     *
     * @return the current date
     * @throws ParseException
     */
    public static Calendar getToday() throws ParseException {
        Date             today = new Date();
        SimpleDateFormat df    = new SimpleDateFormat(DATEPATTERN);

        // This seems like quite a hack (date -> string -> date),
        // but it works ;-)
        String   todayAsString = df.format(today);
        Calendar cal           = new GregorianCalendar();
        cal.setTime(convertStringToDate(todayAsString));

        return cal;
    }

    /**
     * This method generates a string representation of a date's date/time in
     * the format you specify on input
     *
     * @param aMask the date pattern the string is in
     * @param aDate a date object
     * @return a formatted string representation of the date
     * @see SimpleDateFormat
     */
    public static final String getDateTime(String aMask, Date aDate) {
        SimpleDateFormat df          = null;
        String           returnValue = "";

        if(aDate == null) {
            log.info("aDate is null!");
        } else {
            df = new SimpleDateFormat(aMask);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    /**
     * @param aDate
     * @return
     */
    public static final String getDateTime(Date aDate) {
        SimpleDateFormat df          = null;
        String           returnValue = "";
        if(aDate == null) {
            log.info("aDate is null!");
        } else {
            df = new SimpleDateFormat(DATETIMEPATTERN);
            returnValue = df.format(aDate);
        }
        return (returnValue);
    }

    public static final String convertDateTimeToString(Date aDate) {
        return getDateTime(DATETIMEPATTERN, aDate);
    }

    public static Date convertStringToDate(String strDate) {
        Date aDate = convertStringToDate(DATEPATTERN, strDate);

        return aDate;
    }

    /**
     * 将传入的字符串转换为Date类型
     *
     * @param strDate 需要转换的字符串,格式为'yyyy-MM-dd HH:mm:ss'
     * @return
     * @throws ParseException
     */

    /**
     * 将字符串转换成日期类型,自动匹配格式
     *
     * @param date
     * @return
     */
    public static Date stringToDate(String date) {
        Assert.notNull(date);
        Date result = null;
        try {
            result = DateUtils.parseDate(date, PARSEPATTERNS);
        } catch(ParseException e) {
            log.error("DateTimeUtils发生ParseException错误", e);
        }
        return result;
    }

    /**
     * 字符串格式转日期
     *
     * @param date
     * @param parsePatterns
     * @return
     */
    public static Date stringToDate(String date, String... parsePatterns) {
        try {
            return DateUtils.parseDate(date, parsePatterns);
        } catch(ParseException e) {
            log.error("DateTimeUtils发生ParseException错误", e);
        }
        return null;
    }

    /**
     * 日期转字符串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String dateToString(Date date, String pattern) {
        if(date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.format(date);
        }
        return "";
    }

    /**
     * 日期转字符串
     * yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String dateToString(Date date) {
        return dateToString(date, DATETIMEPATTERN);
    }

    /**
     * 得到 当前系统时间一周内的时间
     * 日期转字符串
     * yyyy-MM-dd
     *
     * @return
     */
    public static String getWeekBeforeNow() {

        Calendar         c         = Calendar.getInstance();//此时打印它获取的是系统当前时间
        Date             date      = new Date();
        SimpleDateFormat sdf       = new SimpleDateFormat("yyyyMMdd");
        String           nowDate   = sdf.format(date);
        String           dayBefore = "";
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 6);
        Date beforeDate = c.getTime();
        dayBefore = sdf.format(beforeDate);
        return dayBefore;

    }

    /**
     * 计算 两个时间的差值
     * yyyyMMdd
     *
     * @return
     */
    public static long calculateDifference(String start, String end) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        format.setLenient(false);
        Date date1 = format.parse(start);
        Date date2 = format.parse(end);
		/*//计算差值,分钟数
		long minutes=(date2.getTime()-date1.getTime())/(1000*60);
		System.out.println(minutes);*/
        //计算差值,天数
        long days = (date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24);

        return days;

    }

    public static String convertDateTimeToString(Date date, String pattren) {
        return getDateTime(pattren, date);
    }

    public static String formatLongToTimeStr(Long l) {
        String str    = "";
        int    hour   = 0;
        int    minute = 0;
        int    second = 0;
        second = l.intValue() / 1000;
        if(second > 60) {
            minute = second / 60;
            second = second % 60;
        }

        if(minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        String strtime = hour + ":" + minute + ":" + second;
        return strtime;

    }

    public static String formatDuring(long mss) {
        long days    = mss / (1000 * 60 * 60 * 24);
        long hours   = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        return hours + ":" + minutes + ":" + seconds;
    }

    /**
     * 返回目标时间所在天的结尾
     */
    public static Date toDateEnd() {
        return toDateEnd(new Date());
    }


    /**
     * 返回目标时间所在天的结尾
     */
    public static Date toDateEnd(Date date) {
        if(date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return c.getTime();
    }

    public static Date toDateEnd(String dateStr) {
        return toDateEnd(StringUtils.isBlank(dateStr) ? new Date() : parseDate(dateStr));
    }

    /**
     * 返回目标时间所在天的开始
     */
    public static Date toDateStart() {
        return toDateStart(new Date());
    }

    /**
     * 返回目标时间所在天的开始
     */
    public static Date toDateStart(Date date) {
        if(date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    public static Date toDateStart(String dateStr) {
        return toDateStart(StringUtils.isBlank(dateStr) ? new Date() : parseDate(dateStr));
    }

    /**
     * 返回目标时间所在小时的结尾
     */
    public static Date toHourEnd(Date date) {
        if(date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return c.getTime();
    }

    /**
     * 描述：创建一段时间内的所以字符串列表
     *
     * @param startDate 起始日期 	例如：2017-09-01
     * @param endDate   结束日期 		例如：2017-09-02
     */
    public static List<String> createDateString(String startDate, String endDate) {
        List<String> dates = new ArrayList<>();
        if(StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)) {
            return dates;
        }
        if(startDate.equals(endDate)) {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date   parse  = f.parse(startDate);
                String format = f.format(parse);
                dates.add(format);
            } catch(ParseException e) {
                log.error("DateTimeUtils发生ParseException错误", e);
            }
            return dates;
        }

        SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
        Calendar         startDay  = Calendar.getInstance();
        Calendar         endDay    = Calendar.getInstance();

        try {
            startDay.setTime(FORMATTER.parse(startDate));
            endDay.setTime(FORMATTER.parse(endDate));
        } catch(ParseException e) {
            log.error("DateTimeUtils发生ParseException错误", e);
        }

        // 给出的日期开始日比终了日大则不执行打印
        if(startDay.compareTo(endDay) >= 0) {
            return dates;
        }
        // 现在打印中的日期
        Calendar currentPrintDay = startDay;
        // 打印开始日期
        dates.add(FORMATTER.format(currentPrintDay.getTime()));
        while(true) {
            // 日期加一
            currentPrintDay.add(Calendar.DATE, 1);
            // 日期加一后判断是否达到终了日,达到则终止打印
            if(currentPrintDay.compareTo(endDay) > 0) {
                break;
            }
            // 打印日期
            dates.add(FORMATTER.format(currentPrintDay.getTime()));
        }
        return dates;
    }

    /**
     * 描述：创建一段时间内的所有日期long值列表
     *
     * @param startDate 起始日期
     * @param endDate   结束日期
     */
    public static List<Long> createDateLong(String startDate, String endDate) {
        SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
        Calendar         startDay  = Calendar.getInstance();
        Calendar         endDay    = Calendar.getInstance();

        try {
            startDay.setTime(FORMATTER.parse(startDate));
            endDay.setTime(FORMATTER.parse(endDate));
        } catch(ParseException e) {
            log.error("DateTimeUtils发生ParseException错误", e);
        }

        List<Long> dates = new ArrayList<>();

        // 给出的日期开始日比终了日大则不执行打印
        if(startDay.compareTo(endDay) >= 0) {
            return dates;
        }
        // 现在打印中的日期
        Calendar currentPrintDay = startDay;
        currentPrintDay.add(Calendar.DAY_OF_MONTH, -1);
        while(true) {
            // 日期加一
            currentPrintDay.add(Calendar.DATE, 1);
            // 日期加一后判断是否达到终了日,达到则终止打印
            if(currentPrintDay.compareTo(endDay) == 0) {
                break;
            }
            // 打印日期
            dates.add(currentPrintDay.getTimeInMillis());
        }
        return dates;
    }

    /**
     * 描述：比较两个时间的时分秒
     *
     * @param time1 09:00
     * @param time2 22:00
     * @return true
     * @author fangxin
     * @date 2017-08-23 19:37
     */
    public static boolean timeBefore(String time1, String time2) throws NumberFormatException {
        if(!time1.contains(":") || !time2.contains(":")) {
            throw new NumberFormatException();
        }
        String[] array1 = time1.split(":");
        int      total1 = Integer.valueOf(array1[0]) * 3600 + Integer.valueOf(array1[1]) * 60;
        String[] array2 = time2.split(":");
        int      total2 = Integer.valueOf(array2[0]) * 3600 + Integer.valueOf(array2[1]) * 60;
        return total1 - total2 < 0;
    }

    /**
     * 描述：获取前一个小时的开始日期
     * 例如：2017-09-06 11:23:00 的前一个小时开始日期是 2017-09-06 10:00:00
     */
    public static Date getPriorHourStart() {
        Calendar c2 = Calendar.getInstance();
        c2.setTime(new Date());
        c2.set(Calendar.MILLISECOND, 0);
        c2.set(Calendar.SECOND, 0);
        c2.set(Calendar.MINUTE, 0);
        c2.set(Calendar.HOUR, c2.get(Calendar.HOUR) - 1);
        return c2.getTime();
    }

    public static Date getCurrentHourStart() {
        Calendar c2 = Calendar.getInstance();
        c2.setTime(new Date());
        c2.set(Calendar.MILLISECOND, 0);
        c2.set(Calendar.SECOND, 0);
        c2.set(Calendar.MINUTE, 0);
        return c2.getTime();
    }

    /**
     * 描述：为当前日期 添加一小时
     */
    public static Date addHour(Date startTime) {
        Calendar s = Calendar.getInstance();
        s.setTime(startTime);
        s.add(Calendar.HOUR, 1);
        startTime = s.getTime();
        return startTime;
    }

    /**
     * 毫秒格式化为时分秒
     */
    public static String millisToHour(long millis) {
        int second = (int) (millis / 1000);
        int hour   = second / 3600;
        int minute = (second - hour * 3600) / 60;
        second = second - hour * 3600 - minute * 60;
        return hour + "小时" + minute + "分" + second + "秒";
    }

    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds 精确到秒的字符串
     * @return
     */
    public static String timeStamp2Date(Long seconds) {
        if(seconds == null || seconds.equals("null") || seconds.equals(0L)) {
            return "0000-00-00 00:00:00";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(seconds));
    }

    public static String timeStamp2Date(String seconds) {
        if(seconds == null || seconds.equals("null")) {
            return "0000-00-00 00:00:00";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(Long.valueOf(seconds) * 1000));
    }

    public static Date now() {
        return new Date();
    }

    public static void main(String[] args) {

        /*SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        System.out.println("当前时间：" + getDate());
        Date newDate = addSeconds(now, 25);
        System.out.println("25秒后：" + getDate().compareTo(addSeconds(34)));
        System.out.println(addSeconds(10));*/
        System.out.println(timeStamp2Date(0L));
    }

    public static String addSeconds(int seconds) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(yyyyMMddHHmmss);
        Date             now        = new Date();
        Date             newDate    = addSeconds(now, seconds);
        return dateFormat.format(newDate);
    }

    public static long addSeconds2Long(int seconds) {
        Date             now        = new Date();
        Date             newDate    = addSeconds(now, seconds);
        return newDate.getTime();
    }

    private static Date addSeconds(Date date, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

    public static String millisecond2Date(Long seconds) {
        if(seconds == null || seconds.equals("null")) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(seconds));
    }

    public static String addSeconds(String format, int seconds) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date             now        = new Date();
        Date             newDate    = addSeconds(now, seconds);
        return dateFormat.format(newDate);
    }
}
