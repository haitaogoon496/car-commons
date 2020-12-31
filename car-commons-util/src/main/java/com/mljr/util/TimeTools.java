package com.mljr.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 日期工具类<br>
 * @version V1.0  2017/4/29 17:45  by 石冬冬-Seig Heil（dd.shi02@zuche.com）创建
 */
public final class TimeTools {
    private static final Logger logger = LoggerFactory.getLogger(TimeTools.class);
    private static final Object lockObj = new Object();
    /** 存放不同的日期模板格式的sdf的Map */
    private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<String, ThreadLocal<SimpleDateFormat>>();
    /**
     * 时间汇率
     */
    public static long MISECOND = 1000;
    public static long SECOND = 1*1000;
    public static long MINITE = 60*1*1000;
    public static long HOUR = 60*60*1*1000;

    private static final String YYYY_MM = "yyyy-MM";
    private static final String YYYY_MM_DD = "yyyy-MM-dd";
    private static final String YEARMONTHDAY = "yyyy年MM月dd日";
    private static final String YYYY_MM_DD_HH_MI_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MI = "yyyy-MM-dd HH:mm";
    private static final String YYYY_MM_DD_HH_00 = "yyyy-MM-dd HH:00";
    public static final String HH_MI_SS = "HH:mm:ss";
    private static final String BEGIN_OF_DAY = "00:00:00";
    private static final String END_OF_DAY = "23:59:59";
    private static final String BEGIN_OF_AFTERNOON = "12:00:00";
    private static final String END_OF_MORNING = "11:59:59";
    //声明：禁止声明static SimpleDateFormat实例初始化，都由本类getSdf()方法初始化相关实例，由于SimpleDateFormat是非线程安全的 modify by 石冬冬 on 2017/4/21
    /**
     * 返回一个ThreadLocal的sdf,每个线程只会new一次sdf
     * @param pattern
     * @return
     */
    private static SimpleDateFormat getSdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> sdf = sdfMap.get(pattern);
        // 此处的双重判断和同步是为了防止sdfMap这个单例被多次put重复的sdf
        if (sdf == null) {
            synchronized (lockObj) {
                sdf = sdfMap.get(pattern);
                if (sdf == null) {
                    // 只有Map中还没有这个pattern的sdf才会生成新的sdf并放入map
                    //logger.info("put new sdf of pattern " + pattern + " to map");
                    // 这里是关键,使用ThreadLocal<SimpleDateFormat>替代原来直接new SimpleDateFormat
                    sdf =  new ThreadLocal<SimpleDateFormat>() {
                        @Override
                        protected SimpleDateFormat initialValue() {
                            //logger.info("thread: " + Thread.currentThread() + " init pattern: " + pattern);
                            return new SimpleDateFormat(pattern);
                        }
                    };
                    sdfMap.put(pattern, sdf);
                }
            }
        }
        return sdf.get();
    }
    /**
     * Description: 该入口作为所有formate的统一入口<br>
     * @version V1.0 2017/4/21 10:49  by 石冬冬-Heil Hilter（dd.shi02@zuche.com)
     * @param date
     * @param format
     * @return
     */
    public static String format(Date date, String format) {
        if (date == null || format == null) {
            return null;
        }
        return getSdf(format).format(date);
    }

    public static String format4YEARMONTHDAY(Date date) {
        return format(date, YEARMONTHDAY);
    }

    public static String format4YYYYMMDD(Date date) {
        return format(date, YYYY_MM_DD);
    }

    public static String format4YYYYMMDDHHMISS(Date date) {
        return format(date, YYYY_MM_DD_HH_MI_SS);
    }
    public static String format4YYYYMMDDHHMI(Date date) {
        return format(date, YYYY_MM_DD_HH_MI);
    }
    public static String format4YYYYMMDDHH00(Date date) {
        return format(date, YYYY_MM_DD_HH_00);
    }
    public static String format4HHMISS(Date date) {
        return format(date, HH_MI_SS);
    }

    public static Date parse(String time, String format) {
        if (StringTools.isEmpty(time)) {
            return null;
        }
        try {
            return getSdf(format).parse(time);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parseYYYY_MM_DD(String time) {
        return parse(time, YYYY_MM_DD);
    }

    public static Date parseYYYY_MM_DD_HH_MI_SS(String time) {
        return parse(time, YYYY_MM_DD_HH_MI_SS);
    }

    public static Date parseYYYY_MM_DD_HH_MI(String time) {
        return parse(time, YYYY_MM_DD_HH_MI);
    }

    public static Date parseYYYY_MM_DD_HH_00(String time) {
        return parse(time, YYYY_MM_DD_HH_00);
    }

    /**
     * 克隆一个新的时间对象
     *
     * @param date
     * @return
     */
    public static Date cloneDate(Date date) {
        return new Date(date.getTime());
    }

    /**
     * 创建一个当前时间的时间对象
     *
     * @return
     */
    public static Date createNowTime() {
        return getCalendar().getTime();
    }

    /**
     * 根据年月日创建时间 时分秒默认为0
     *
     * @param year
     * @param month
     * @param date
     * @return
     */
    public static Date createTime(int year, int month, int date) {
        return createTime(year, month, date, 0, 0, 0);
    }

    /**
     * 根据年月日时分秒创建时间
     *
     * @param year
     * @param month
     * @param date
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static Date createTime(int year, int month, int date, int hour, int minute, int second) {
        Calendar calendar = getCalendar();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 时间设置到明天的0点0分0秒
     *
     * @param date
     */
    public static void setNextDay0H0M0S0MS(Date date) {
        Calendar calendar = getCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, 1);
        date.setTime(calendar.getTimeInMillis());
    }

    /**
     * 设置Date到当天的0点0分0秒0毫秒
     *
     * @param date
     */
    public static void set0H0M0S0MS(Date date) {
        Calendar calendar = getCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        date.setTime(calendar.getTimeInMillis());
    }

    /**
     * 设置Date到当天的0秒0毫秒
     *
     * @param date
     */
    public static void set0S0MS(Date date) {
        Calendar calendar = getCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        date.setTime(calendar.getTimeInMillis());
    }
    /**
     * 设置Date到当天的23点59分59秒
     *
     * @param date
     */
    public static void set23H59M59S(Date date) {
        Calendar calendar = getCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        date.setTime(calendar.getTimeInMillis());
    }

    /**
     * 时间设置到今天的23点59分59秒
     *
     * @param date
     */
    public static void set23H59M59S0MS(Date date) {
        Calendar calendar = getCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        date.setTime(calendar.getTimeInMillis());
    }

    public static void set1D0H0M0S0MS(Date date) {
        Calendar calendar = getCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        date.setTime(calendar.getTimeInMillis());
    }


    public static void addTimeField(Date date, int field, int value) {
        Calendar calendar = getCalendar();
        calendar.setTime(date);

        calendar.add(field, value);

        date.setTime(calendar.getTimeInMillis());
    }
    public static void setLastDay0H0M0S(Date date) {
        Calendar calendar = getCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, -1);
        date.setTime(calendar.getTimeInMillis());
    }

    public static void setLastMonth1D0H0M0S(Date date) {
        Calendar calendar = getCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MONTH, -1);
        date.setTime(calendar.getTimeInMillis());
    }

    public static void setMonth1D0H0M0S(Date date) {
        Calendar calendar = getCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        date.setTime(calendar.getTimeInMillis());
    }

    public static void setDate(Date date, int field, int value) {
        Calendar calendar = getCalendar();
        calendar.setTime(date);
        calendar.set(field, value);
        date.setTime(calendar.getTimeInMillis());
    }

    /**
     * 得到当前时期是几号
     *
     * @param date
     * @return
     */
    public static int getMonthDate(Date date) {
        Calendar calendar = getCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
    public static int getWeek(Date date) {
        Calendar calendar = getCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }
    public static int getMonth(Date date) {
        Calendar calendar = getCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }

    public static int getYear(Date date) {
        Calendar calendar = getCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static int getHour(Date date) {
        Calendar calendar = getCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute(Date date) {
        Calendar calendar = getCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }

    public static int getSecond(Date date) {
        Calendar calendar = getCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.SECOND);
    }

    public static Calendar getCalendar() {
        return Calendar.getInstance();
    }

    /**
     *
     * Description: 获取月份最大天数
     * @Version1.0 2016-10-26 下午5:39:08 by zhoumian@@zuche.com
     * @param date
     * @return
     */
    public static int getMonthMaxDay(Date date){
        Calendar calendar = getCalendar();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DATE);
    }
    /**
     * 获取本周周一零点的时间
     * */
    public static Date getThisMonday(){
        Calendar cal = getCalendar();
        cal.setFirstDayOfWeek(Calendar.MONDAY);//设置中文时间的第一天是星期一，而不是星期天
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return  cal.getTime();
    }

    /**
     * 获取本周周天晚上24点的时间
     * */
    public static Date getThisSunday(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(getThisMonday());
        cal.add(Calendar.DAY_OF_WEEK, 7);
        return cal.getTime();
    }

    /**
     * 返回指定年月的月的第一天00:00:00
     * 参数为null，则返回当年、当月
     * 月份参数为-1则返回上一个月,-2则返回上上个月
     * @param year
     * @param month
     * @return
     */
    public static Date getFirstDayOfMonth(Integer year, Integer month) {
        Calendar calendar = Calendar.getInstance();
        if (year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        if (month == null) {
            month = calendar.get(Calendar.MONTH);
        }
        if(month==-1){
            month=calendar.get(Calendar.MONTH)-1;
        }
        if(month==-2){
            month=calendar.get(Calendar.MONTH)-2;
        }
        calendar.set(year, month, 1,0,0,0);
        Date temp=calendar.getTime();
        try {
            SimpleDateFormat sdf = getSdf(YYYY_MM_DD_HH_MI_SS);
            return sdf.parse(sdf.format(temp));
        } catch (ParseException e) {
            logger.error("",e);
            return null;
        }
    }
    /**
     * 返回指定年月的月的最后一天23:59:59
     * 参数为null，则返回当年、当月
     * 月份参数为-1则返回上一个月，参数为-2则返回上上个月
     * @param year
     * @param month
     * @return
     */
    public static Date getLastDayOfMonth(Integer year, Integer month) {
        Calendar calendar = Calendar.getInstance();
        if (year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        if (month == null) {
            month = calendar.get(Calendar.MONTH);
        }
        if(month==-1){
            month=calendar.get(Calendar.MONTH)-1;
        }
        if(month==-2){
            month=calendar.get(Calendar.MONTH)-2;
        }
        calendar.set(year,month+1,1,0,0,-1);
        Date temp=calendar.getTime();
        try {
            SimpleDateFormat sdf = getSdf(YYYY_MM_DD_HH_MI_SS);
            return sdf.parse(sdf.format(temp));
        } catch (ParseException e) {
            logger.error("");
            return null;
        }
    }

    private TimeTools() {
    }

    /**
     * Description: 获得N天前时间
     * @Version1.0 2016-5-3 下午11:10:51 by 康文（kangwen@10101111.com）创建
     * @param time 当前时间
     * @param days 提前天数
     * @return
     */
    public static String getPreNDay(String time,Integer days){
        if(StringTools.isEmpty(time)){
            return null;
        }
        if(null == days){
            return time;
        }
        days = days-1;
        Date date = parseYYYY_MM_DD_HH_MI_SS(time);
        Calendar calendar = getCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_YEAR, -days);
        date.setTime(calendar.getTimeInMillis());
        return format(date, YYYY_MM_DD_HH_MI_SS);
    }

    /**
     * 获取时间的明天的0点0分0秒
     *
     * @param str
     */
    public static String getNextDay0H0M0S0MS(String str) {
        if(StringTools.isEmpty(str)){
            return null;
        }
        Date date = parseYYYY_MM_DD(str);
        setNextDay0H0M0S0MS(date);
        return format4YYYYMMDD(date);
    }

    /**
     *
     * Description: 两时间相差天数
     * @Version1.0 2016-10-26 下午6:50:24 by zhoumian@@zuche.com
     * @param endDate
     * @param startDate
     * @return
     */
    public static int diffDays(Date endDate,Date startDate){
        try {
            SimpleDateFormat sdf = getSdf(YYYY_MM_DD);
            endDate=sdf.parse(sdf.format(endDate));
            startDate=sdf.parse(sdf.format(startDate));
            Calendar cal = Calendar.getInstance();
            cal.setTime(endDate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(startDate);
            long time2 = cal.getTimeInMillis();
            long between_days=(time1-time2)/(1000*3600*24);
            return Integer.parseInt(String.valueOf(between_days))+1;
        } catch (ParseException e) {
            logger.error("",e);
        }
        return 0;


    }

    /**
     * 获取本周周日晚上23:59:59
     * */
    public static Date getLastDayOfThisWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 6);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }


    /**
     * 比较两个时间的大小如果第一个时间大则返回1,如果第一个时间小则返回-1,异常返回0
     * @auhor 郑旭(zhengxu@zuche.com)
     * @date 2016-11-1 下午4:05:19
     * @param dateTime1
     * @param dateTime2
     * @param format
     * @return
     */
    public static int compareDate(String dateTime1,String dateTime2,String format){
        SimpleDateFormat sdf = null;
        if(YYYY_MM_DD.equals(format)){
            sdf = getSdf(YYYY_MM_DD);
        }else if(YYYY_MM_DD_HH_MI_SS.equals(format)){
            sdf = getSdf(YYYY_MM_DD_HH_MI_SS);
        }else{
            return 0;
        }
        try {
            Date date1=sdf.parse(dateTime1);
            Date date2=sdf.parse(dateTime2);
            if(date1.getTime()>=date2.getTime()){
                return 1;
            }else if(date1.getTime()<=date2.getTime()){
                return -1;
            }
        } catch (ParseException e) {
            logger.error("",e);
        }
        return 0;
    }

    /**
     *
     * Description:给司机发送语音push，格式化语音push中的时间
     * @Version1.0 2016-6-17 下午4:55:41 by zhangshitao（st.zhang02@zuche.com）创建
     * @param boardTime
     * @param showMinute
     * @return
     */
    public static String getVoiceTime(Date boardTime, boolean showMinute) {
        Calendar now = Calendar.getInstance();// 当前时间
        Calendar bTime = Calendar.getInstance();
        bTime.setTime(boardTime);
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        bTime.set(Calendar.HOUR_OF_DAY, 0);
        bTime.set(Calendar.MINUTE, 0);
        bTime.set(Calendar.SECOND, 0);
        bTime.set(Calendar.MILLISECOND, 0);
        String minAndSec = "";
        if (showMinute) {
            minAndSec = format(boardTime, "HH:mm");
        }
        long intervalMilli = bTime.getTimeInMillis() - now.getTimeInMillis();
        int xcts = (int) (intervalMilli / (24 * 60 * 60 * 1000));
        switch (xcts) {
            case -2:
                return "前天  " + minAndSec;
            case -1:
                return "昨天  " + minAndSec;
            case 0:
                return "今天  " + minAndSec;
            case 1:
                return "明天  " + minAndSec;
            case 2:
                return "后天  " + minAndSec;
            default:
                return format(boardTime, "MM月dd日 HH:mm");
        }
    }

    /**
     *
     * Description: 根据offset获取前几天或者后几天
     * @Version1.0 2016-11-2 上午10:47:05 by zhoumian@@zuche.com
     * @param time
     * @param offset
     * @return
     */
    public static Date getPreOrNextDate(Date time,int offset){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(time);
        calendar.add(Calendar.DATE,offset);
        time=calendar.getTime();
        return time;
    }
    public static Date getPreOrNextMonth(Date time,int offset){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(time);
        calendar.add(Calendar.MONTH,offset);
        time=calendar.getTime();
        return time;
    }
    public static Date getPreOrNextMinute(Date time,int offset){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(time);
        calendar.add(Calendar.MINUTE,offset);
        time=calendar.getTime();
        return time;
    }

    /**
     * 获得当前时间半年前时间
     * @param date
     * @return
     */
    public static String getHighYear(Date date){

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        calendar.add(Calendar.MONTH, -6);

        String time= format(calendar.getTime(),YYYY_MM_DD);

        return time;
    }

    /**
     * Description: 判断两个时间是不是在同一天
     * @param date1
     * @param date2
     * @return
     * @version 1.0  2016-10-25 下午6:36:16  by 杜鸿蕾（hl.du@zuche.com）创建
     */
    public static boolean isTheSameDay(Date date1, Date date2) {
        SimpleDateFormat sdf = getSdf(YYYY_MM_DD);
        return sdf.format(date1).equals(sdf.format(date2));
    }

    /**
     * Description: 取得指定时间的年月
     * @param date
     * @return yyyy-MM
     * @version 1.0  2016-10-25 下午6:36:16  by 杜鸿蕾（hl.du@zuche.com）创建
     */
    public static String getYearMonth(Date date) {
        return getSdf(YYYY_MM).format(date);
    }

    /**
     * 获得日期几天前/后 的日期
     * @param date
     * @param day
     * @return
     */
    public static Date getDayByTime(Date date ,int day){
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        calendar.add(Calendar.DATE, day);

        return calendar.getTime();
    }

    /**
     * Description: 取得一个时间在当月中的天数
     * @param date
     * @return 天数，格式：dd
     * @version 1.0  2016-10-25 下午6:36:16  by 杜鸿蕾（hl.du@zuche.com）创建
     */
    public static String getDayInMonth(Date date) {
        return getSdf("dd").format(date);
    }

    /**
     * Description: 取得一个时间的零点零分零秒
     * @param date
     * @return yyyy-MM-dd 00:00:00
     * @version 1.0  2016-10-25 下午6:36:16  by 杜鸿蕾（hl.du@zuche.com）创建
     */
    public static String getBeginOfOneDay(Date date) {
        return format4YYYYMMDD(date) + " " + TimeTools.BEGIN_OF_DAY;
    }

    /**
     * Description: 取得一个时间的23时59分59秒
     * @param date
     * @return yyyy-MM-dd 23:59:59
     * @version 1.0  2016-10-25 下午6:36:16  by 杜鸿蕾（hl.du@zuche.com）创建
     */
    public static String getEndOfOneDay(Date date) {
        return format4YYYYMMDD(date) + " " + TimeTools.END_OF_DAY;
    }

    /**
     * Description: 取得一个时间的下午开始时间 12:00:00
     * @param date
     * @return yyyy-MM-dd 12:00:00
     * @version 1.0  2017-01-10 下午6:36:16  by 杜鸿蕾（hl.du@zuche.com）创建
     */
    public static String getBeginOfOneDayAfternoon(Date date) {
        return format4YYYYMMDD(date) + " " + TimeTools.BEGIN_OF_AFTERNOON;
    }
    /**
     * Description: 取得一个时间的上午结束时间 11:59:59
     * @param date
     * @return yyyy-MM-dd 11:59:59
     * @version 1.0  2017-01-10 下午6:36:16  by 杜鸿蕾（hl.du@zuche.com）创建
     */
    public static String getEndOfOneDayMorning(Date date) {
        return format4YYYYMMDD(date) + " " + TimeTools.END_OF_MORNING;
    }


    /**
     * Description: 取得当前周的零点零分零秒
     * @return yyyy-MM-dd 00:00:00
     * @version 1.0  2016-10-25 下午6:36:16  by 杜鸿蕾（hl.du@zuche.com）创建
     */
    public static String getBeginOfThisWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return getSdf(YYYY_MM_DD).format(cal.getTime()) + " " + TimeTools.BEGIN_OF_DAY;
    }

    /**
     * Description: 取得当前周的23时59分59秒
     * @return yyyy-MM-dd 23:59:59
     * @version 1.0  2016-10-25 下午6:36:16  by 杜鸿蕾（hl.du@zuche.com）创建
     */
    public static String getEndOfThisWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return getSdf(YYYY_MM_DD).format(cal.getTime()) + " " + TimeTools.END_OF_DAY;
    }

    /**
     * 获取datetime类型字符串的HH:mm:ss
     * @auhor 郑旭(zhengxu@zuche.com)
     * @date 2016-11-11 上午10:21:26
     * @param dateTime
     * @return
     */
    public static String getTime(String dateTime) {
        try {
            SimpleDateFormat sdf = getSdf(HH_MI_SS);
            return sdf.format(sdf.parse(dateTime));
        } catch (ParseException e) {
            logger.error("",e);
        }
        return null;
    }

    /**
     *
     * Description: 计算两个日期（yyyy-MM-dd）相差天数
     * @Version1.0 2016-7-27 下午6:07:45 by zhangshitao（st.zhang02@zuche.com）创建
     * @param sdate
     * @param edate
     * @return
     */
    public static int getDiffOfDate(Date sdate,Date edate){
        SimpleDateFormat sdf=getSdf(YYYY_MM_DD);
        try {
            sdate=sdf.parse(sdf.format(sdate));
            edate=sdf.parse(sdf.format(edate));
        } catch (ParseException e) {
            logger.error("",e);
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(edate);
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * getDateSpace:两个日期相差的天数 <br/>
     * @author 李苏毅
     * @email sy.li12@zuche.com
     * @date: 2016-11-23 下午1:56:32 <br>
     * @param date1 (yyyy-MM-dd)
     * @param date2 (yyyy-MM-dd)
     * @return
     * @throws ParseException
     * @since JDK 1.6
     * @Version V1.0
     */
    public static Float getDateSpace(String date1, String date2) {
        Calendar calst = Calendar.getInstance();
        Calendar caled = Calendar.getInstance();
        calst.setTime(parseYYYY_MM_DD(date1));
        caled.setTime(parseYYYY_MM_DD(date2));

        if(getHour(parseYYYY_MM_DD_HH_MI_SS(date1)) >= 12) {
            //上午
            // 设置时间为0时
            calst.set(Calendar.HOUR_OF_DAY, 0);
        } else {
            calst.set(Calendar.HOUR_OF_DAY, 12);
        }
        if(getHour(parseYYYY_MM_DD_HH_MI_SS(date2)) < 12) {
            //上午
            // 设置时间为0时
            caled.set(Calendar.HOUR_OF_DAY, 0);
        } else {
            caled.set(Calendar.HOUR_OF_DAY, 12);
        }
        calst.set(Calendar.MINUTE, 0);
        calst.set(Calendar.SECOND, 0);
        caled.set(Calendar.MINUTE, 0);
        caled.set(Calendar.SECOND, 0);
        // 得到两个日期相差的天数
        Integer days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst.getTime().getTime() / 1000)) / 3600 / 12;
        return  days.floatValue() / 2;
    }

    /**
     * 这里的方法是从Intrdriver拷贝出来的，备注【石冬冬】
     * getDateSpace:两个日期相差的天数 <br/>
     * @author 李苏毅
     * @email sy.li12@zuche.com
     * @date: 2016-11-23 下午1:56:32 <br>
     * @param date1 (yyyy-MM-dd)
     * @param date2 (yyyy-MM-dd)
     * @return
     * @throws ParseException
     * @since JDK 1.6
     * @Version V1.0
     */
    public static Float getDateSpace2(String date1, String date2) {
        Calendar first = Calendar.getInstance();
        Calendar last = Calendar.getInstance();
        first.setTime(parseYYYY_MM_DD(date1));
        last.setTime(parseYYYY_MM_DD(date2));

        first.set(Calendar.MINUTE, 0);
        first.set(Calendar.SECOND, 0);
        first.set(Calendar.HOUR, 0);
        last.set(Calendar.MINUTE, 0);
        last.set(Calendar.SECOND, 0);
        last.set(Calendar.HOUR, 0);
        // 得到两个日期相差的天数
        Float days = ((float) (last.getTime().getTime() / 1000) - (float) (first.getTime().getTime() / 1000)) / 3600 / 24;

        //再单独计算小时问题
        if(getHour(parseYYYY_MM_DD_HH_MI_SS(date1)) >= 12) {
            //如果开始时间大于12点 算半天,如果小于12点算一天
            days = days - 0.5f;
        }
        if(getHour(parseYYYY_MM_DD_HH_MI_SS(date2)) < 12 && !date2.endsWith("00:00:00")) {
            //如果结束时间小于12点 算半天,如果大于12点算一天
            days = days - 0.5f;
        }
        return  days + 1;
    }

    /**
     * getMonthSpace:计算相差月份. <br/>
     *
     * @author 李苏毅
     * @email sy.li12@zuche.com
     * @date: 2016-11-25 上午10:17:26 <br>
     * @param date1
     * @param date2
     * @return
     * @throws ParseException
     * @since JDK 1.6
     * @Version V1.0
     */
    public static int getMonthSpace(String date1, String date2) {
        int result = 0;
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(parseYYYY_MM_DD(date1));
        c2.setTime(parseYYYY_MM_DD(date2));
        int year = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
        result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
        result = result == 0 ? 0 : Math.abs(result);
        if(Math.abs(year) > 0) {
            result = result + (12 * Math.abs(year));
        }
        return result;
    }


    /**
     *
     * Description:校验时间合法行
     * @Version1.0 2017-1-6 上午11:07:21 by zhoumian@@zuche.com
     * @param time 格式YYYY-MM-DD
     * @return
     */
    public static boolean validateTime(String time){
        //校验时间合法
        try{
            Integer.parseInt(time.replaceAll("-", ""));
        }catch (Exception e) {
            return false;
        }
        Date date = parseYYYY_MM_DD(time);
        Calendar calendar =Calendar.getInstance();
        calendar.setTime(date);
        int day = Integer.parseInt(time.split("-")[2]);
        return day==calendar.get(Calendar.DATE);
    }


    /**
     * 得到两个年份差几年
     * @return
     */
    public static Integer getYearDifference(Date compara ,Date beCompared) {
        Calendar beginCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        beginCal.setTime(compara);
        endCal.setTime(beCompared);
        Integer bYear=beginCal.get(Calendar.YEAR);
        Integer eYear=endCal.get(Calendar.YEAR);
        if (bYear<=eYear){
            return 0;
        }else {
            Integer bMonth=beginCal.get(Calendar.MONTH);//06
            Integer eMonth=endCal.get(Calendar.MONTH);//07
            if (bMonth<eMonth){
                return (bYear-eYear)-1;
            }else if (bMonth>eMonth){
                return (bYear-eYear)+1;
            }else if (NumberUtil.isEquals(bMonth,eMonth)){
                Integer bDay=beginCal.get(Calendar.DAY_OF_MONTH);//06
                Integer eDay=endCal.get(Calendar.DAY_OF_MONTH);//07
                if (bDay<eDay){
                    return (bYear-eYear)-1;
                }else if (bDay>eDay){
                    return (bYear-eYear)+1;
                }
                return (bYear-eYear);
            }
            return (bYear-eYear);
        }

    }

    /**
     * 返回指定年第一天00:00:00
     * 参数为null，则返回当年
     * @param year
     * @return
     * @version 1.0  2016-10-25 下午6:36:16  by 杜鸿蕾（hl.du@zuche.com）创建
     */
    public static Date getFirstDayOfYear(Integer year) {
        Calendar calendar = Calendar.getInstance();
        if(year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        calendar.set(year, 0, 1, 0, 0, 0);
        Date tmp = calendar.getTime();
        try {
            SimpleDateFormat sdf = getSdf(YYYY_MM_DD_HH_MI_SS);
            return sdf.parse(sdf.format(tmp));
        }
        catch (ParseException e) {
            return null;
        }
    }

    /**
     * 返回指定年最后一天23:59:59
     * 参数为null，则返回当年
     * @param year
     * @return
     * @version 1.0  2016-10-25 下午6:36:16  by 杜鸿蕾（hl.du@zuche.com）创建
     */
    public static Date getLastDayOfYear(Integer year) {
        Calendar calendar = Calendar.getInstance();
        if(year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        calendar.set(year+1, 0, 1, 0, 0, -1);
        Date tmp = calendar.getTime();
        try {
            SimpleDateFormat sdf = getSdf(YYYY_MM_DD_HH_MI_SS);
            return sdf.parse(sdf.format(tmp));
        }
        catch (ParseException e) {
            return null;
        }
    }

    /**
     * Description: 比较传入的时间是否早于当前时间 minute分钟<br/>
     * @Version1.0 2016-7-28 下午5:28:16 by kangwen
     * @param date
     * @param minute
     * @return true早于， false不早于
     */
    public static boolean compNow(Date date,long minute){
        if(date == null){
            return false;
        }
        TimeTools.set0S0MS(date);
        Long nowMiliSec = System.currentTimeMillis();
        Long compMiliSec = date.getTime()+(minute*60000);
        if(nowMiliSec>compMiliSec){
            return true;
        }
        return false;
    }


    /**
     *
     * Description: 校验时间合法性
     * @Version1.0 2017-1-16 下午2:39:55 by zhoumian@@zuche.com
     * @param date
     * @param template
     * @return
     */
    public static boolean isValidDate(String date,String template){
        SimpleDateFormat format = new SimpleDateFormat(template);
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }

    }
    /**
     * 传入方法得到的英文日历，星期天=1...星期六=7
     * 转换成枚举定义星期一=1....星期天=7
     * @param week
     * @return
     */
    public static Integer convertDayEnum(Integer week){
        //英文日历、从周日开始
        if(NumberUtil.isEquals(week, 1)){
            week = 7;
        }else{
            week = week - 1;
        }
        return week;
    }
    public static List<String> getBetweenMonth(Date startDate, Date endDate){
        List<String> calList = new ArrayList<String>();
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(startDate);
        end.setTime(endDate);
        //比较两个日期,如果开始比结束小就添加到集合中然后加1月份
        while (start.compareTo(end)<=0) {
            calList.add(getYearMonth(start.getTime()));
            start.add(Calendar.MONTH, 1);
        }
        return calList;
    }


    /**
     * @description: 输出时间
     * @Date : 2018/1/2 下午4:14
     * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
     */
    public static String spend(long spend){
        String out = "";
        int hour=(int)spend/(60*60*1000);//时
        int min=(int)spend/(60*1000)%60;//分
        int sec=(int)spend/1000%60; //秒
        int ms=(int)spend%1000;//毫秒
        if(hour>0){out+=hour+"时";}
        if(min>0){out+=min+"分";}
        if(sec>0){out+=sec+"秒";}
        if(ms>0){out+=ms+"毫秒";}
        return out;
    }
    /**
     * @description: 输出时间
     * @Date : 2018/1/2 下午4:14
     * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
     */
    public static String spend(long spend,String msg){
        if(0==spend){
            return "★★★"+msg+"★★★";
        }else{
            return "★★★"+msg+"，共花费"+spend(spend)+"★★★";
        }
    }


    public static void main(String[] args) {
        Date start = new Date();//获取开始日期
        Date endTime = new Date();
        TimeTools.addTimeField(start, Calendar.MONTH, -2);//得到一个截至时间
        Date startTime = TimeTools.getFirstDayOfMonth(TimeTools.getYear(start), TimeTools.getMonth(start));//获取一个月的第一天
        TimeTools.setNextDay0H0M0S0MS(endTime);
        TimeTools.set0H0M0S0MS(startTime);
        System.out.println(TimeTools.format(startTime, YYYY_MM_DD_HH_MI_SS));
        System.out.println(TimeTools.format(endTime, YYYY_MM_DD_HH_MI_SS));
    }
}

