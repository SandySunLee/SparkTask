package com.cpic.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class DateUtils {
	private DateUtils() {
	}

	/**
	 * 日期类型：yyyy-MM-dd
	 */

	public static final String DATE_PATTERN = "yyyy-MM-dd";

	public static final String LINUX_DATE_PATTERN = "yyyyMMdd";
	/**
	 * 日期类型：HH:mm:ss
	 */
	public static final String TIME_PATTERN = "HH:mm:ss";

	/**
	 * 日期类型：HH:mm
	 */
	public static final String SHORT_TIME_PATTERN = "HH:mm";

	/**
	 * 日期类型：yyyy-MM-dd HH:mm:ss
	 */
	public static final String DATE_TIME_PATTERN = DATE_PATTERN + " " + TIME_PATTERN;

	/**
	 * 日期类型：yyyy-MM-dd HH:mm:ss
	 */
	public static final String DATE_TIME_PATTERN_ORACLE = "yyyy-MM-dd HH:mm:ss";

	public static final String DATE_TIME_PATTERN_TRAIL = "yyyy-MM-dd HH:mm:00";
	
	public static final String DATE_TIME_HIVE = "yyyyMMddHHmmss";

	/**
	 * 一天的毫秒数
	 */
	public static final long MILLISECONDS_DAY = 1000 * 3600 * 24;

	/**
	 * 默认日期格式类
	 */
	private static final DateFormat defaultFormat = new SimpleDateFormat(DATE_TIME_PATTERN_ORACLE);

	public static final int DAY = Calendar.DAY_OF_YEAR;

	public static final int MONTH = Calendar.MONTH;

	public static final int YEAR = Calendar.YEAR;

	public static final int MINUTE = Calendar.MINUTE;

	public static DateUtils getInstance() {
		return new DateUtils();
	}

	/**
	 * 静态方法 说明：取得当前时间 采用默认格式 yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String getCurrentlyTime() {
		return getCurrentlyTime(DATE_TIME_PATTERN_ORACLE);
	}

	/**
	 * 静态方法 取得当前的时间
	 * 
	 * @param patten
	 *            返回的时间格式
	 * @return
	 */
	public static String getCurrentlyTime(String patten) {
		Calendar cal = Calendar.getInstance();
		return convertDateToStr(cal.getTime(), patten);
	}

	/**
	 * current
	 * 
	 * @return
	 */
	public static Date getCurrentlyDate() {
		// Calendar cal = Calendar.getInstance();
		// return convertStrToDate(convertDateToStr(cal.getTime()));
		return new Date();
	}

	/**
	 * 根据当前时间获取上一年时间
	 * 
	 * @return
	 */
	public static String getUpDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
		return convertDateToStr(cal.getTime());
	}

	/**
	 * 根据当前时间获取上一月时间
	 * 
	 * @return
	 */
	public static String getUpMonth() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
		return convertDateToStr(cal.getTime());
	}

	/**
	 * 类方法说明：取得当前时间
	 * 
	 * @return
	 */
	public static Date getDate() {
		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}

	/**
	 * day
	 * 
	 * @return
	 */
	public static int getDay() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 类方法说明：日期类型（yyyy-MM-dd）------->字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String convertDateToStr(Date date) {
		if (null == date) {
			return "";
		}
		return defaultFormat.format(date);
	}

	public static String convertMyFormatDate(String date) {
		if (null == date) {
			return "";
		}
		// System.out.println(date);
		Date convertStrToDate = convertStrToDate(date, LINUX_DATE_PATTERN);
		// System.out.println(convertStrToDate);
		return convertDateToStr(convertStrToDate, DATE_PATTERN);
	}

	/**
	 * time
	 * 
	 * @param date
	 * @return
	 */
	public static Timestamp convertUtilDateToTimestamp(Date date) {
		if (null == date) {
			return null;
		}
		Timestamp sDate = new Timestamp(date.getTime());
		return sDate;
	}

	/**
	 * parse
	 * 
	 * @param dateStr
	 * @return
	 * @throws ParseException
	 */
	public static Date parseToDate(String dateStr) throws ParseException {
		dateStr = dateStr.replaceAll("/", "-");
		if(dateStr.length()>=19){
			dateStr=dateStr.substring(0,19);
		}
		if (dateStr.contains("-")) {
			StringBuffer sb = new StringBuffer();
			String[] split = dateStr.split("-");
			if (split.length != 3) {
				throw new IllegalArgumentException(String.format("%s日期格式错误！", dateStr));
			}
			sb.append(split[0]);
			String month = split[1];
			String dayhour = split[2];
			String hour = "";
			String day = "";
			if (month.length() < 2) {
				month = "0" + split[1];
			}
			if (dayhour.length() > 2) {
//				System.out.println(dayhour);
				if (Integer.parseInt(dayhour.substring(0, 2).trim()) < 10) {
					day = "0" + Integer.parseInt(dayhour.substring(0, 2).trim());
				} else {
					day = "" + Integer.parseInt(dayhour.substring(0, 2).trim());
				}
				hour = dayhour.substring(2).trim();
//				System.out.println(hour);
			}
			if (dayhour.length() <= 2) {
				if (Integer.parseInt(dayhour.substring(0, dayhour.length()).trim()) < 10) {

					day = "0" + Integer.parseInt(dayhour.substring(0, dayhour.length()).trim());
				} else {
					day = "" + Integer.parseInt(dayhour.substring(0, dayhour.length()).trim());
				}
			}
			dateStr = sb.append("-").append(month).append("-").append(day).append(" ").append(hour).toString().trim();
//			System.out.println(dateStr);
		}

		Pattern pattern2 = Pattern.compile("^\\d{4}\\-\\d{2}\\-\\d{2} \\d{2}\\:\\d{2}\\:\\d{2}$");
		Matcher matcher2 = pattern2.matcher(dateStr);
		if (matcher2.matches()) {
			DateFormat df = new SimpleDateFormat(DATE_TIME_PATTERN);
			return df.parse(dateStr);
		}

		Pattern pattern = Pattern.compile("^\\d{4}\\-\\d{2}\\-\\d{2}$");
		Matcher matcher = pattern.matcher(dateStr);
		if (matcher.matches()) {
			DateFormat df = new SimpleDateFormat(DATE_PATTERN);
			return df.parse(dateStr);
		}

		Pattern pattern3 = Pattern.compile("^\\d{4}\\d{2}\\d{2}$");
		Matcher matcher3 = pattern3.matcher(dateStr);
		if (matcher3.matches()) {
			DateFormat df = new SimpleDateFormat(LINUX_DATE_PATTERN);
			return df.parse(dateStr);
		}
		
//		20180809111212
		Pattern pattern4 = Pattern.compile("^\\d{4}\\d{2}\\d{2}\\d{2}\\d{2}\\d{2}$");
		Matcher matcher4 = pattern4.matcher(dateStr);
		if (matcher4.matches()) {
			DateFormat df = new SimpleDateFormat(DATE_TIME_HIVE);
			return df.parse(dateStr);
		}
		
		throw new IllegalArgumentException(String.format("%s日期格式错误！", dateStr));
	}

	/**
	 * convert
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date convertCTSStrToDate(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd hh:mm:ss zzz yyyy", Locale.ENGLISH);
		Date date = new Date();
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * convert
	 * 
	 * @param date
	 * @return
	 */
	public static java.sql.Date convertDateToSQLDate(Date date) {
		java.sql.Date sDate = java.sql.Date.valueOf(DateUtils.convertDateToStr(date, DateUtils.DATE_TIME_PATTERN));

		return sDate;
	}

	/**
	 * 类方法说明：根据传入的pattern，日期类型------->字符串
	 * 
	 * @param date
	 * @param pattern
	 *            日期的格式
	 * @return
	 */
	@SuppressWarnings("unused")
	public static String convertDateToStr(Date date, String pattern) {
		if (null == date || null == pattern) {
			return "";
		}
		DateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		if (null == date) {
			date = new Date();
		}
		return simpleDateFormat.format(date);
	}

	/**
	 * 类方法说明：字符串（yyyy-MM-dd）------->日期类型
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date convertStrToDate(String dateStr) {
		Date date = null;
		if (null == dateStr || "".equals(dateStr.trim())) {
			return null;
		}
		if (dateStr.length() > 10) {
			// logger.error(dateStr + "字符串格式错误");
			throw new IllegalArgumentException("字符串格式错误(yyyy-MM-dd)" + dateStr);

		}
		try {
			date = defaultFormat.parse(dateStr);
		} catch (ParseException e) {
			// logger.error("字符串---->日期出错", e);
			throw new IllegalArgumentException("字符串格式错误(yyyy-MM-dd)" + dateStr);
		}
		return date;

	}

	/**
	 * convert
	 * 
	 * @param dateStr
	 * @param pattern
	 * @return
	 */
	public static Date convertStrToDate(String dateStr, String pattern) {
		Date date = null;
		if (null == dateStr || "".equals(dateStr.trim())) {
			return new Date();
		}
		try {
			DateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			date = simpleDateFormat.parse(dateStr);
		} catch (Exception e) {
			return new Date();
			// logger.error("字符串---->日期出错", e);
			// throw new java.lang.IllegalArgumentException("字符串格式错误(" + pattern
			// + ")" + dateStr);
		}
		return date;

	}

	/**
	 * 类方法说明：日期加
	 * 
	 * @param date
	 *            日期
	 * @param day
	 *            天数
	 * @return
	 */
	public static Date add(Date date, int day) {
		return add(date, DateUtils.DAY, day);
	}

	/**
	 * 取得日期的当月最后一天
	 * 
	 * @param sDate1
	 * @return
	 */
	public static Date getLastDayOfMonth(Date sDate1) {
		Calendar cDay1 = Calendar.getInstance();
		cDay1.setTime(sDate1);
		int lastDay = cDay1.getActualMaximum(Calendar.DAY_OF_MONTH);
		cDay1.set(Calendar.DAY_OF_MONTH, lastDay);
		return cDay1.getTime();
	}

	public static Date getLastDayOfYear(Date sDate1) {
		Calendar cDay1 = Calendar.getInstance();
		cDay1.setTime(sDate1);
		int lastDay = cDay1.getActualMaximum(Calendar.DAY_OF_YEAR);
		cDay1.set(Calendar.DAY_OF_YEAR, lastDay);
		return cDay1.getTime();
	}

	public static Date getFirstDayOfYear(Date sDate1) {
		Calendar cDay1 = Calendar.getInstance();
		cDay1.setTime(sDate1);
		int day = cDay1.getActualMinimum(Calendar.DAY_OF_YEAR);
		cDay1.set(Calendar.DAY_OF_YEAR, day);
		cDay1.set(Calendar.HOUR_OF_DAY, 0);
		cDay1.set(Calendar.MINUTE, 0);
		cDay1.set(Calendar.SECOND, 0);
		return cDay1.getTime();
	}

	/**
	 * 取得日期的当月第一天
	 * 
	 * @param sDate1
	 * @return
	 */

	public static Date getFirstDayOfMonth(Date sDate1) {
		Calendar cDay1 = Calendar.getInstance();
		cDay1.setTime(sDate1);
		int day = cDay1.getActualMinimum(Calendar.DAY_OF_MONTH);
		cDay1.set(Calendar.DAY_OF_MONTH, day);
		cDay1.set(Calendar.HOUR_OF_DAY, 0);
		cDay1.set(Calendar.MINUTE, 0);
		cDay1.set(Calendar.SECOND, 0);
		return cDay1.getTime();
	}

	/**
	 * 类方法说明：日期加
	 * 
	 * @param date
	 *            日期
	 * @param minute
	 *            分钟
	 * @return
	 */
	public static Date addMinute(Date date, int minute) {
		return add(date, DateUtils.MINUTE, minute);
	}

	/**
	 * 类方法说明：增加时间 date==null 返回当前时间加
	 * 
	 * @param date
	 *            日期
	 * @param field
	 *            增加的类型（DAY,MONTH,MINUTE）
	 * @param arg
	 *            增量
	 * @return
	 */
	public static Date add(Date date, int field, int arg) {
		if (null == date) {
			date = new Date();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(field, arg);
		return cal.getTime();
	}

	/**
	 * 比较两个时间的大小
	 * 
	 * @param startTime
	 *            开始时间 yyyy-MM-dd HH:mm:SS;
	 * @param endDate
	 *            结束时间 yyyy-MM-dd HH:mm:SS
	 * @return 0为等于，1为大于，-1位小于
	 */
	public static int dateCompareTo(Date startTime, Date endDate) {
		int flag = 0;
		try {
			flag = startTime.compareTo(endDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 类方法说明：开始时间大于结束时间返回true
	 * 
	 * @param startTime
	 * @param endDate
	 * @return
	 */
	public static boolean dateGreater(Date startTime, Date endDate) {
		boolean flag = false;
		int flagInt = dateCompareTo(startTime, endDate);
		if (flagInt == 1) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 类方法说明：开始时间大于或者等于结束时间返回true
	 * 
	 * @param startTime
	 * @param endDate
	 * @return
	 */
	public static boolean dateGreaterEquals(Date startTime, Date endDate) {
		boolean flag = false;
		int flagInt = dateCompareTo(startTime, endDate);
		if (flagInt == 1 || flagInt == 0) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 类方法说明：小于等于
	 * 
	 * @param startTime
	 * @param endDate
	 * @return
	 */

	public static boolean dateLessEquals(Date startTime, Date endDate) {
		boolean flag = false;
		int flagInt = dateCompareTo(startTime, endDate);
		if (flagInt == 0 || flagInt == -1) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 类方法说明：取得当前年份
	 * 
	 * @return
	 */
	public static String getYear() {
		Calendar cal = Calendar.getInstance();
		return String.valueOf(cal.get(Calendar.YEAR));

	}

	/**
	 * 类方法说明：取得当前月份
	 * 
	 * @return
	 */
	public static String getCurrentMonth() {
		Calendar cal = Calendar.getInstance();

		String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
		month = month.length() == 1 ? "0" + month : month;
		return month;

	}

	/**
	 * 类方法说明：取得上个月
	 * 
	 * @return
	 */
	public static String getNextMonth() {
		String date = "";
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DATE, 26);
		date = convertDateToStr(cal.getTime());
		return date;
	}

	/**
	 * new
	 * 
	 * @param date
	 * @return
	 */
	public static String getNewReportMonth(Date date) {
		String reportDate = "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DATE, 10);
		reportDate = convertDateToStr(cal.getTime());
		return reportDate;
	}

	/**
	 * 取得上月第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getUpMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DATE, 1);
		date = cal.getTime();
		return date;
	}

	/**
	 * month
	 * 
	 * @param date
	 * @return
	 */
	public static int getMonthByDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH) + 1;
	}

	/**
	 * month
	 * 
	 * @param date
	 * @return
	 */
	public static int getMonthByDate(String date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateUtils.convertStrToDate(date));
		return cal.get(Calendar.MONTH) + 1;
	}

	/**
	 * year
	 * 
	 * @param date
	 * @return
	 */
	public static int getYearByDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}

	/**
	 * year
	 * 
	 * @param date
	 * @return
	 */
	public static int getYearByDate(String date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateUtils.convertStrToDate(date));
		return cal.get(Calendar.YEAR);
	}

	/**
	 * 类方法说明：根据时间的年月 获得结算时间的开始时间和结束时间
	 * 
	 * @return
	 */
	public static Map<String, String> getSTimeETime(int year, int month) {
		Map<String, String> map = new HashMap<String, String>();
		if (month < 1 && month > 12)
			return map;
		// Calendar cal = Calendar.getInstance();
		// cal.set(year, month - 1, 25);
		// map.put("endTime", defaultFormat.format(cal.getTime()));
		// cal.add(Calendar.MONTH, -1);
		// cal.set(Calendar.DATE, 26);
		// map.put("startTime", defaultFormat.format(cal.getTime()));

		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, 1);
		map.put("startTime", defaultFormat.format(cal.getTime()));
		cal.add(Calendar.MONTH, 1);
		// cal.add(Calendar.DAY_OF_MONTH, -1);
		map.put("endTime", defaultFormat.format(cal.getTime()));
		return map;
	}

	/**
	 * 类方法说明：根据时间的年月 获得结算时间的开始时间和结束时间
	 * 
	 * @return
	 */
	public static Map<String, Object> getSDateEDate(int year, int month) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (month < 1 && month > 12)
			return map;
		// Calendar cal = Calendar.getInstance();
		// cal.set(year, month - 1, 25);
		// map.put("endTime", defaultFormat.format(cal.getTime()));
		// cal.add(Calendar.MONTH, -1);
		// cal.set(Calendar.DATE, 26);
		// map.put("startTime", defaultFormat.format(cal.getTime()));

		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, 1);
		map.put("startTime", cal.getTime());
		cal.add(Calendar.MONTH, 1);
		// cal.add(Calendar.DAY_OF_MONTH, -1);
		map.put("endTime", cal.getTime());
		return map;
	}

	/**
	 * 取得开始时间和结束时间之间的时间
	 * 
	 * @param beginDate
	 *            开始时间 YYYY-MM-DD
	 * @param endDate
	 *            结束时间 YYYY-MM-DD
	 * @return
	 */
	public static String[] getDate(String beginDate, String endDate) {
		String[] dateList = null;
		try {
			// 如果结束日期为空，返回开始日期
			if (endDate == null || endDate.equals("")) {
				return new String[] { defaultFormat.format(defaultFormat.parse(beginDate)) };
			}
			Date bDate = defaultFormat.parse(beginDate);
			Date eDate = defaultFormat.parse(endDate);
			Calendar calB = Calendar.getInstance();
			Calendar calE = Calendar.getInstance();
			calB.setTime(bDate);
			calE.setTime(eDate);
			// 如果结束日期<=开始日期，返回开始日期
			if (calE.compareTo(calB) == 0 || calE.compareTo(calB) == -1) {
				return new String[] { defaultFormat.format(defaultFormat.parse(beginDate)) };
			} else if (calE.compareTo(calB) == 1) {// 如果结束日期>开始日期
				long daterange = calE.getTimeInMillis() - calB.getTimeInMillis();
				int theday = (int) (daterange / MILLISECONDS_DAY) + 1;
				dateList = new String[theday];
				for (int i = 0; i < theday; i++) {
					calB.setTime(bDate);
					calB.add(Calendar.DAY_OF_YEAR, i);
					dateList[i] = defaultFormat.format(calB.getTime());

				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateList;
	}

	/**
	 * 取得开始时间和结束时间之间的时间
	 * 
	 * @param beginDate
	 *            开始时间 YYYY-MM-DD
	 * @param endDate
	 *            结束时间 YYYY-MM-DD
	 * @return
	 */
	public static List<Date> getDate(Date beginDate, Date endDate) {
		List<Date> dateList = null;
		try {
			Calendar calB = Calendar.getInstance();
			Calendar calE = Calendar.getInstance();
			calB.setTime(beginDate);
			calE.setTime(endDate);
			if (calE.compareTo(calB) == -1) { // 如果结束日期<开始日期，返回NULL
				// dateList.add(beginDate);
				dateList = null;
			} else if (calE.compareTo(calB) == 0) { // 如果结束日期=开始日期，返回开始日期
				dateList = new ArrayList<Date>(1);
				dateList.add(beginDate);
			} else if (calE.compareTo(calB) == 1) {// 如果结束日期>开始日期
				long daterange = calE.getTimeInMillis() - calB.getTimeInMillis();
				int theday = (int) (daterange / MILLISECONDS_DAY);
				dateList = new ArrayList<Date>(theday);
				for (int i = 0; i < theday; i++) {
					calB.setTime(beginDate);
					calB.add(Calendar.DAY_OF_YEAR, i);
					dateList.add(calB.getTime());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateList;
	}

	/**
	 * 取得开始时间和结束时间之间的时间
	 * 
	 * @param beginDate
	 *            开始时间 YYYY-MM-DD
	 * @param endDate
	 *            结束时间 YYYY-MM-DD
	 * @return
	 */
	public static List<Date> getDate2(Date beginDate, Date endDate) {
		List<Date> dateList = new ArrayList<Date>();
		try {
			Calendar calB = Calendar.getInstance();
			Calendar calE = Calendar.getInstance();
			calB.setTime(beginDate);
			calE.setTime(endDate);
			// 如果结束日期<=开始日期，返回开始日期
			if (calE.compareTo(calB) == 0 || calE.compareTo(calB) == -1) {
				dateList.add(beginDate);
			} else if (calE.compareTo(calB) == 1) {// 如果结束日期>开始日期
				long daterange = calE.getTimeInMillis() - calB.getTimeInMillis();
				int theday = (int) (daterange / MILLISECONDS_DAY);
				dateList = new ArrayList<Date>(theday);
				for (int i = 0; i < theday; i++) {
					calB.setTime(beginDate);
					calB.add(Calendar.DAY_OF_YEAR, i);
					dateList.add(calB.getTime());
				}
				dateList.add(endDate);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateList;
	}

	/**
	 * hours
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static List<Date> getHours(Date beginDate, Date endDate) {
		List<Date> dateList = null;
		try {
			Calendar calB = Calendar.getInstance();
			Calendar calE = Calendar.getInstance();
			calB.setTime(beginDate);
			calE.setTime(endDate);
			// 如果结束日期<=开始日期，返回开始日期
			if (calE.compareTo(calB) == 0 || calE.compareTo(calB) == -1) {
				// dateList.add(beginDate);
			} else if (calE.compareTo(calB) == 1) {// 如果结束日期>开始日期
				long daterange = calE.getTimeInMillis() - calB.getTimeInMillis();
				int theday = (int) (daterange * 24 / MILLISECONDS_DAY);
				dateList = new ArrayList<Date>(theday);
				for (int i = 0; i < theday; i++) {
					calB.setTime(beginDate);
					calB.add(Calendar.HOUR_OF_DAY, i);
					dateList.add(calB.getTime());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateList;
	}

	/**
	 * 获得两个年份间的时间(跨年份时间)
	 * 
	 * @param beginYear
	 *            YYYY
	 * @param endYear
	 *            YYYY
	 * @return List<String> YYYY-1-1
	 */
	public static List<String> getYears(String beginYear, String endYear) {
		int by = Integer.parseInt(beginYear);
		int ey = Integer.parseInt(endYear);

		List<String> list = new ArrayList<String>();
		for (int i = by; i <= ey; i++)
			list.add(i + "-1-1");
		return list;
	}

	/**
	 * 类方法说明：取得两个时间之间的天数
	 * 
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return
	 */
	public static int getNumberOfDays(Date startTime, Date endTime) {
		int numberDays = 0;
		if (DateUtils.dateGreater(startTime, endTime)) {
			return 0;
		}
		Calendar calS = Calendar.getInstance();
		calS.setTime(startTime);
		Calendar calE = Calendar.getInstance();
		calE.setTime(endTime);
		// 两个时间相差的毫秒
		long daterange = calE.getTimeInMillis() - calS.getTimeInMillis();
		numberDays = (int) (daterange / MILLISECONDS_DAY) + 1;
		return numberDays;
	}

	/**
	 * 类方法说明：取得两个时间之间的相差的分钟数保留2位小数
	 * 
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return
	 */
	public static double getNumberOfMinute(Date startTime, Date endTime) {
		double numberHours = 0;
		if (DateUtils.dateGreater(startTime, endTime)) {
			return 0;
		}
		Calendar calS = Calendar.getInstance();
		calS.setTime(startTime);
		Calendar calE = Calendar.getInstance();
		calE.setTime(endTime);
		// 两个时间相差的毫秒
		long daterange = calE.getTimeInMillis() - calS.getTimeInMillis();
		numberHours = (double) (daterange * new Double(1) / (1000 * 60));
		return numberHours;
	}

	/**
	 * second
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static int getNumberOfSecond(Date startTime, Date endTime) {
		int numberHours = 0;
		if (DateUtils.dateGreater(startTime, endTime)) {
			return 0;
		}
		Calendar calS = Calendar.getInstance();
		calS.setTime(startTime);
		Calendar calE = Calendar.getInstance();
		calE.setTime(endTime);
		// 两个时间相差的毫秒
		long daterange = calE.getTimeInMillis() - calS.getTimeInMillis();
		numberHours = (int) (daterange * new Double(1) / (1000));
		return numberHours;
	}

	/**
	 * 根据时间 改变字符串（时间）格式 如：yyyy-MM-dd HH:mm:SS 转为 yyyy-MM-dd
	 * 
	 * @param dateStr
	 *            字符串（时间）
	 * @param formPattern
	 *            原格式
	 * @param toPattern
	 *            目标格式
	 * @return 字符串（时间）
	 */
	public static String formatStrByDate(String dateStr, String formPattern, String toPattern) {
		return DateUtils.convertDateToStr(convertStrToDate(dateStr, formPattern), toPattern);
	}

	/**
	 * 功能说明：获取当前月第一天的时间
	 * 
	 * @return
	 */
	public static Date getFirstDayOfCurrentDate() {
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_MONTH, 1);
		return ca.getTime();
	}

	/**
	 * 功能说明：获取当前月第一天
	 * 
	 * @return
	 */
	public static Date getFirstDayOfCurrentDay() {
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_MONTH, 1);

		return DateUtils.convertStrToDate(DateUtils.convertDateToStr(ca.getTime()));
	}

	/****
	 * 获取昨天
	 * 
	 * @return
	 */
	public static String getYesterDay() {
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date());
		ca.add(Calendar.DATE, -1);
		return DateUtils.convertDateToStr(ca.getTime(), "yyyyMMdd000000");
	}

	/**
	 * 功能说明：获取当前月最后一天的时间
	 * 
	 * @return
	 */
	public static Date getLastDayOfCurrentDate() {
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.MONTH, 1);
		ca.set(Calendar.DAY_OF_MONTH, 1);
		ca.add(Calendar.DAY_OF_MONTH, -1);
		return ca.getTime();
	}

	/**
	 * 功能说明：获取当前月最后一天的时间
	 * 
	 * @return
	 */
	public static String getLastDayOfCurrentDay() {
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_MONTH, 1);
		ca.add(Calendar.MONTH, 1);
		ca.set(Calendar.DAY_OF_MONTH, 1);
		ca.add(Calendar.DAY_OF_MONTH, -1);

		return DateUtils.convertDateToStr(ca.getTime(), DATE_PATTERN);
	}

	/**
	 * @param date
	 *            获得给定时间的年份数值,月份数值等
	 * @param field
	 *            编号 MONTH or YEAR...
	 * @return
	 */
	public static int getFieldNumber(Date date, int field) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		int i = ca.get(field);
		if (field == Calendar.MONTH)
			i++;
		return i;
	}

	/**
	 * 取得开始时间和结束时间之间的时间 左开 右闭
	 * 
	 * @param beginDate
	 *            开始时间 YYYY-MM-DD
	 * @param endDate
	 *            结束时间 YYYY-MM-DD
	 * @return
	 */
	public static String[] getDate2(String beginDate, String endDate) {
		String[] dateList = null;
		try {
			// 如果结束日期为空，返回开始日期
			if (endDate == null || endDate.equals("")) {
				return new String[] { defaultFormat.format(defaultFormat.parse(beginDate)) };
			}
			Date bDate = defaultFormat.parse(beginDate);
			Date eDate = defaultFormat.parse(endDate);
			Calendar calB = Calendar.getInstance();
			Calendar calE = Calendar.getInstance();
			calB.setTime(bDate);
			calE.setTime(eDate);
			calE.add(Calendar.DAY_OF_YEAR, -1);
			// 如果结束日期<=开始日期，返回开始日期
			if (calE.compareTo(calB) == 0 || calE.compareTo(calB) == -1) {
				return new String[] { defaultFormat.format(defaultFormat.parse(beginDate)) };
			} else if (calE.compareTo(calB) == 1) {// 如果结束日期>开始日期
				long daterange = calE.getTimeInMillis() - calB.getTimeInMillis();
				int theday = (int) (daterange / MILLISECONDS_DAY) + 1;
				dateList = new String[theday];
				for (int i = 0; i < theday; i++) {
					calB.setTime(bDate);
					calB.add(Calendar.DAY_OF_YEAR, i);
					dateList[i] = defaultFormat.format(calB.getTime());

				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateList;
	}

	/**
	 * time
	 * 
	 * @param map
	 * @return
	 */
	public static String geCronexpressionstTime(Map<String, String> map) {
		String type = map.get("type");
		String time = "";
		if ("1".equals(type)) {
			time = map.get("timeS");
			if (StringUtils.isEmpty(time)) {
				time = "0:0";
			}
		} else if ("2".equals(type)) {
			time = map.get("timeT");
			if (StringUtils.isEmpty(time)) {
				time = "0:0:0";
			}
		} else if ("3".equals(type)) {
			time = map.get("timeT");
			String t = map.get("time");
			if (StringUtils.isEmpty(time)) {
				time = "0:0:0";
			}
			time = time + ":" + t;

		} else if ("4".equals(type)) {
			time = map.get("timeT");
			String t = map.get("time");
			if (StringUtils.isEmpty(time)) {
				time = "0:0:0";
			}
			time = time + ":" + t;
		}
		return time;
	}

	// /**
	// * 取得当前数据库时间SQL表达式
	// *
	// * @param date
	// * @return yyyy-MM-dd HH:mm:ss格式
	// */
	// public static String convertDataToDBString(Date date) {
	// String d = DateUtils
	// .convertDateToStr(date, DateUtils.DATE_TIME_PATTERN);
	// if ("oracle".equalsIgnoreCase(PreferencesConstants.DATATYPE)) {
	// return "to_date('" + d + "','" + DateUtils.DATE_TIME_PATTERN_ORACLE
	// + "')";
	// } else {
	// return "'" + d + "'";
	// }
	// }

	/**
	 * 类方法说明：
	 * 
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
		// Date d1 = DateUtils.convertStrToDate("2009-04-01 10:00:00",
		// DateUtils.DATE_TIME_PATTERN);
		// Date d2 = DateUtils.convertStrToDate("2009-04-02 10:00:00",
		// DateUtils.DATE_TIME_PATTERN);
		// Date startTime = DateUtils.convertStrToDate("2010-1-12");
		// Date endTime = DateUtils.convertStrToDate("2010-1-1");
		// // List list = DateUtils.getHours(d1, d2);
		// String s = "other20091112155950.tmp";
		// System.out.println(DateUtils.convertDateToStr((DateUtils
		// .getFirstDayOfCurrentDay())));
		// Iterator i = list.iterator();
		// while (i.hasNext()) {
		// System.out.println(DateUtils.convertDateToStr((Date) i.next(),
		// DateUtils.DATE_TIME_PATTERN));
		// }
		// System.out.println( DateUtils.getNewReportMonth(new Date()) );
		// System.out.println( stampToTrailDateStr("1519863849") );

		// System.out.println( stampToDateStr("1519863849") );
		// System.out.println( parseToDate(stampToTrailDateStr("1519863849") ).getTime() );
		// System.out.println(getYesterDay());
		// System.out.println(getLastDayOfCurrentDay());
		System.out.println(convertDateToStr(add(parseToDate("2018/10/21 12:12:12"), -11), "yyyy-MM-dd"));

		// System.out.println( convertDateToStr ( getLastDayOfMonth( parseToDate("20181021") ),"yyyy-MM-dd"));

	}

	/**
	 * getFirstMinutesOfDay
	 * 
	 * @param strDate
	 * @return
	 */
	public static long getFirstMinutesOfDay(String strDate) {
		return DateUtils.getMinutes(strDate += " 00:00:00");
	}

	/**
	 * getLastMinutesOfDay
	 * 
	 * @param strDate
	 * @return
	 */
	public static long getLastMinutesOfDay(String strDate) {
		return DateUtils.getMinutes(strDate += " 23:59:59");
	}

	/**
	 * getFirstMinutesOfMonth
	 * 
	 * @param strDate
	 * @return
	 */
	public static long getFirstMinutesOfMonth(String strDate) {
		return DateUtils.getMinutes(strDate += "-01 00:00:00");
	}

	/**
	 * getLastMinutesOfMonth
	 * 
	 * @param strDate
	 * @return
	 */
	public static long getLastMinutesOfMonth(String strDate) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		try {
			Date date = sdf.parse(strDate);
			cal.setTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return 0l;
		}
		long lastDay = cal.getActualMaximum(Calendar.DATE);
		return DateUtils.getMinutes(strDate += "-" + lastDay + " 23:59:59");
	}

	/**
	 * getMinutes
	 * 
	 * @param strDate
	 * @return
	 */
	public static long getMinutes(String strDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sdf.parse(strDate).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0l;
		}
	}

	/**
	 * 获取当前考核季度
	 * 
	 * @return
	 */
	public static Map<String, String> getThisExamineQuearterMap() {
		Map<String, String> quarteMap = new HashMap<String, String>();

		// 获取当前年份，dateYear
		String dateYear = null;
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);

		// 获取当前季度，dateQuarter
		String dateQuarter = null;
		if (month == 1 || month == 2 || month == 3) {
			dateQuarter = "04";
			year = year - 1;
		} else if (month == 4 || month == 5 || month == 6) {
			dateQuarter = "01";
		} else if (month == 7 || month == 8 || month == 9) {
			dateQuarter = "02";
		} else if (month == 10 || month == 11 || month == 12) {
			dateQuarter = "03";
		}
		dateYear = String.valueOf(year);
		// 放置值
		quarteMap.put("dateYear", dateYear);
		quarteMap.put("dateQuarter", dateQuarter);
		return quarteMap;
	}

	public static String stampToDateStr(String time) {
		if (time.length() >= 13) {
			return defaultFormat.format(new Date(Long.parseLong(time)));
		} else {
			return defaultFormat.format(new Date(Long.parseLong(time) * 1000));
		}
	}

	public static String stampToTrailDateStr(String time) {
		DateFormat trailFormat = new SimpleDateFormat(DATE_TIME_PATTERN_TRAIL);

		if (time.length() >= 13) {
			return trailFormat.format(new Date(Long.parseLong(time)));
		} else {
			return trailFormat.format(new Date(Long.parseLong(time) * 1000));
		}
	}

	public static Date stampToDate(String time) {
		return new Date(Long.parseLong(time) * 1000);
	}

	/**
	 * 时区 时间转换方法:将传入的时间（可能为其他时区）转化成目标时区对应的时间
	 * @param sourceTime 时间格式必须为：yyyy-MM-dd HH:mm:ss
	 * @param sourceId 入参的时间的时区id 比如：+08:00
	 * @param targetId 要转换成目标时区id 比如：+09:00
	 * @param reFormat 返回格式 默认：yyyy-MM-dd HH:mm:ss
	 * @return string 转化时区后的时间
	 */
	public static String timeConvert(String sourceTime, String sourceId,String targetId,String reFormat){
		//校验入参是否合法
		if (null == sourceId || "".equals(sourceId) || null == targetId
				|| "".equals(targetId) || null == sourceTime
				|| "".equals(sourceTime)){
			return null;
		}
		if(reFormat==null || "".equals(reFormat)){
			reFormat = "yyyy-MM-dd HH:mm:ss";
		}
		//校验 时间格式必须为：yyyy-MM-dd HH:mm:ss
		String reg = "^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}$";
		if (!sourceTime.matches(reg)){
			return null;
		}
		try{
			//时间格式
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//根据入参原时区id，获取对应的timezone对象
			TimeZone sourceTimeZone = TimeZone.getTimeZone("GMT"+sourceId);
			//设置SimpleDateFormat时区为原时区（否则是本地默认时区），目的:用来将字符串sourceTime转化成原时区对应的date对象
			df.setTimeZone(sourceTimeZone);
			//将字符串sourceTime转化成原时区对应的date对象
			java.util.Date sourceDate = df.parse(sourceTime);
			//开始转化时区：根据目标时区id设置目标TimeZone
			TimeZone targetTimeZone = TimeZone.getTimeZone("GMT"+targetId);
			//设置SimpleDateFormat时区为目标时区（否则是本地默认时区），目的:用来将字符串sourceTime转化成目标时区对应的date对象
			df.setTimeZone(targetTimeZone);
			//得到目标时间字符串
			String targetTime = df.format(sourceDate);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.util.Date date = sdf.parse(targetTime);
			sdf = new SimpleDateFormat(reFormat);
			return sdf.format(date);
		}catch (ParseException e){
			e.printStackTrace();
		}
		return null;
	}
}
