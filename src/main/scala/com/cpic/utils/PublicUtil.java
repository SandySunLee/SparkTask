package com.cpic.utils;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 功能:公共处理
 * 
 */
public class PublicUtil {
	private static Log log = LogFactory.getLog(PublicUtil.class);

	@SuppressWarnings("rawtypes")
	public static HashMap yearData = new HashMap();

	private static Properties props = new Properties();


	/**
	 * 去掉空值
	 * 
	 * @param str 原值
	 * @param defu 缺省
	 * @return String 处理后值
	 */
	public static String nvlString(String str, String defu) {
		String s_temp = str;
		if (s_temp == null || s_temp.toUpperCase().equals("NULL") || s_temp.length() == 0) {
			s_temp = defu;
		}
		return s_temp;
	}

	/**
	 * 去掉空值
	 * 
	 * @param str 原值
	 * @return String 处理后值
	 */
	public static String nvlString(Object str) {
		String s_temp = "" + str;
		if (s_temp == null || s_temp.toUpperCase().equals("NULL")) {
			s_temp = "";
		}
		return s_temp.trim();
	}

	/**
	 * 将0的字符串转换为空字符串
	 * 
	 * @param value 需要转换的字符串
	 * @return 转换后的字符串 修改日期2009-3-20
	 */
	public final static String nvlZero(String value) {
		String retStr = "";
		if (value != null) {
			if (PublicUtil.parseBigNum(value, 0) == new BigDecimal(0)) {
				retStr = "";
			} else {
				retStr = value;
			}

		}
		return retStr;
	}

	/**
	 * 去掉空值
	 * 
	 * @param str 原值
	 * @return String 处理后值
	 */
	public static BigDecimal nvlBigDecimal(Object str) {
		if (str != null && !str.equals("")) {
			String className = PublicUtil.nvlString(str.getClass());
			if (className.contains("BigDecimal")) {
				return (BigDecimal) str;
			} else if (className.contains("Double")) {
				return BigDecimal.valueOf((Double) str);
			} else if (className.contains("Long")) {
				return BigDecimal.valueOf((Long) str);
			} else {
				return BigDecimal.valueOf(Double.parseDouble(PublicUtil.nvlString(str)));
			}
		} else {
			return new BigDecimal(0);
		}
	}

	/**
	 * 过滤非法文本
	 * 
	 * @param String 原来文本
	 * @return String 过滤后的文本
	 * @author liuyang
	 */
	public static String filterText(String sql) {
		String filter = sql;
		if (filter == null || filter.trim().equals("")) {
			return "";
		}
		filter = filter.replaceAll("\\r", " ");
		filter = filter.replaceAll("\\n", " ");
		filter = filter.replaceAll("<br>", " ");
		filter = filter.replaceAll("<BR>", " ");
		return filter;
	}

	/**
	 * 过滤非法文本
	 * 
	 * @param String 原来文本
	 * @return String 过滤后的文本
	 * @author liuyang
	 */
	public static String filterChar(String srcStr) {
		String rtnStr = srcStr.replaceAll("'", "\\\\'");
		rtnStr = rtnStr.replaceAll("\"", "\\\\\"");
		return rtnStr;
	}

	/**
	 * 合并文本
	 * 
	 * @param args 分割数组
	 * @return String 合并后的文本
	 */
	public static String composeSplit(String[] args) {
		if (args == null) {
			return "";
		}
		String value = "";
		StringBuffer strbuf = new StringBuffer();
		for (int i = 0; i < args.length; i++) {
			strbuf.append("," + args[i]);
		}
		if (strbuf.length() > 0) {
			value = strbuf.toString().substring(1);
		}
		return value;
	}

	/**
	 * 合并文本
	 * 
	 * @param args 分割数组
	 * @return String 合并后的文本
	 */
	public static String composeSplit(Object args) {

		return composeSplit((String[]) args);
	}

	/**
	 * 判断报告期是否在开始日期与结束日期之间
	 * 
	 * @param reportDate
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static boolean isReportDateValid(String reportDate, String beginDate, String endDate) {

		int reportDateInt = 0;
		int beginDateInt = 0;
		int endDateInt = 0;

		if (!"null".equals(beginDate) && !"".equals(beginDate)) {
			beginDateInt = Integer.parseInt(beginDate);
		} else {
			return true;
		}

		if (!"null".equals(endDate) && !"".equals(endDate)) {
			endDateInt = Integer.parseInt(endDate);
		} else {
			return true;
		}
		if (!"null".equals(reportDate) && !"".equals(reportDate)) {
			reportDateInt = Integer.parseInt(reportDate);
		} else {
			return false;
		}
		if (reportDateInt >= beginDateInt && reportDateInt <= endDateInt) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * 根据MAP参数得到参数值
	 * 
	 * @param getValues MAP参数
	 * @return String JS赋值
	 */
	@SuppressWarnings("rawtypes")
	public static String getJSResult(Map getValues) {
		return getJSResult(getValues, null);
	}

	/**
	 * 
	 * 根据MAP参数得到参数值
	 * 
	 * @param getValues MAP参数
	 * @param preFixName 参数前缀
	 * @return String JS赋值
	 */
	@SuppressWarnings("rawtypes")
	private static String getJSResult(Map getValues, String preFixName) {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(" \n\r<script language='javascript'>\r\n var FieldNameArray = new Array();var FieldValueArray = new Array();");
			Iterator it = getValues.entrySet().iterator();
			for (int i = 0; it.hasNext(); i++) {
				Map.Entry et = (Map.Entry) it.next();
				String colname = (String) et.getKey();
				Object colValue = et.getValue();
				colname = colname.toUpperCase();
				try {
					colValue = composeSplit((String[]) colValue);
				} catch (Exception ex) {
				}
				colValue = filterChar(colValue.toString());
				colValue = filterText(colValue.toString());
				sb.append(" FieldNameArray[" + i + "]='" + colname + "';");
				sb.append(" FieldValueArray[" + i + "]='" + ((String) colValue).replaceAll("\\r\\n", "<br>").replaceAll("\"", "`") + "';");
			}
			sb.append(" \n\r</script>\n\r");
			return sb.toString();
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return "";
	}

	/**
	 * 得到JS
	 * 
	 * @return String JS
	 */
	public static String getFunction() {
		StringBuffer sb = new StringBuffer();
		sb.append(" \n\r<script language='javascript'>\n\r");
		sb.append(" function getSelectElement(Control,value,flag){try{if(Control.type!='select-one'){return false;}for(var i=0;i<Control.options.length;i++){if(flag){if(Control.options[i].text==value){return true;}}else{if(Control.options[i].value==value){return true;}}}}catch(e){}return false;}\n\r");
		sb.append(" function setIniData(formName,isParent){try{if(FieldNameArray==null){return;}}catch(e){return;}var radioValue='';var formObj;if(isParent!=undefined&&isParent==true){formObj=parent.document.getElementsByName(formName);}else{formObj=document.getElementsByName(formName);}for(i=0;i<formObj[0].elements.length;i++){for(var prop in FieldNameArray){if(FieldNameArray[prop].toUpperCase()==formObj[0].elements[i].name.toUpperCase()&&FieldValueArray[prop]!=null&&FieldValueArray[prop]!='null'){if(formObj[0].elements[i].type==null){formObj[0].elements[i].innerText=FieldValueArray[prop];}if(formObj[0].elements[i].type=='select-one'){radioValue='';if(getSelectElement(formObj[0].elements[i],FieldValueArray[prop])){formObj[0].elements[i].value=FieldValueArray[prop];}}else if(formObj[0].elements[i].type=='checkbox'){radioValue='';if(formObj[0].elements[i].value==FieldValueArray[prop]){formObj[0].elements[i].checked=true;}}else if(formObj[0].elements[i].type=='radio'){radioValue='';if(radioValue==''){radioValue=FieldValueArray[prop];}if(formObj[0].elements[i].value==radioValue){formObj[0].elements[i].checked=true;}}else{radioValue='';formObj[0].elements[i].value=replaceall(replaceall(FieldValueArray[prop],'<br>','\\r\\n'),'\\`','\\\"');}}}}}\n\r");
		sb.append(" function replaceall(s,oldstring,newstring){var strTemp=s;if(strTemp==null||trim(strTemp)==''){return '';}else{var newY=s.split(oldstring);if(newY.length<=1){return s;}strTemp='';for(var i=0;i<newY.length;i++){strTemp+=newY[i]+newstring;if(i==newY.length-2){strTemp+=newY[i+1];return strTemp;}}return strTemp;}}\n\r");
		sb.append(" function trim(v){var s1=v+'';if(s1!=null||s1.length>0){while(s1.charAt(0)==' '){s1=s1.substring(1,s1.length);}while(s1.charAt(s1.length-1)==' '){s1=s1.substring(0,s1.length-1);}}return s1;}\n\r");
		sb.append(" </script>\n\r");
		return sb.toString();
	}

	/**
	 * 得到JS
	 * 
	 * @param as 前缀
	 * @return String JS
	 */
	public static String getFunction(String as) {
		StringBuffer sb = new StringBuffer();
		sb.append(" \n\r<script language='javascript'>\n\r");
		sb.append(" function getSelectElement(Control,value,flag){try{if(Control.type!='select-one'){return false;}for(var i=0;i<Control.options.length;i++){if(flag){if(Control.options[i].text==value){return true;}}else{if(Control.options[i].value==value){return true;}}}}catch(e){}return false;}\n\r");
		sb.append(" function setIniData(formName,isParent){try{if(FieldNameArray==null){return;}}catch(e){return;}var radioValue='';var formObj;if(isParent!=undefined&&isParent==true){formObj=parent.document.getElementsByName(formName);}else{formObj=document.getElementsByName(formName);}for(i=0;i<formObj[0].elements.length;i++){for(var prop in FieldNameArray){if('"
				+ as
				+ "'+FieldNameArray[prop].toUpperCase()'==formObj[0].elements[i].name.toUpperCase()&&FieldValueArray[prop]!=null&&FieldValueArray[prop]!='null'){if(formObj[0].elements[i].type==null){formObj[0].elements[i].innerText=FieldValueArray[prop];}if(formObj[0].elements[i].type=='select-one'){radioValue='';if(getSelectElement(formObj[0].elements[i],FieldValueArray[prop])){formObj[0].elements[i].value=FieldValueArray[prop];}}else if(formObj[0].elements[i].type=='checkbox'){radioValue='';if(formObj[0].elements[i].value==FieldValueArray[prop]){formObj[0].elements[i].checked=true;}}else if(formObj[0].elements[i].type=='radio'){radioValue='';if(radioValue==''){radioValue=FieldValueArray[prop];}if(formObj[0].elements[i].value==radioValue){formObj[0].elements[i].checked=true;}}else{radioValue='';formObj[0].elements[i].value=replaceall(replaceall(FieldValueArray[prop],'<br>','\\r\\n'),'\\`','\\\"');}}}}}\n\r");
		sb.append(" function replaceall(s,oldstring,newstring){var strTemp=s;if(strTemp==null||trim(strTemp)==''){return '';}else{var newY=s.split(oldstring);if(newY.length<=1){return s;}strTemp='';for(var i=0;i<newY.length;i++){strTemp+=newY[i]+newstring;if(i==newY.length-2){strTemp+=newY[i+1];return strTemp;}}return strTemp;}}\n\r");
		sb.append(" function trim(v){var s1=v+'';if(s1!=null||s1.length>0){while(s1.charAt(0)==' '){s1=s1.substring(1,s1.length);}while(s1.charAt(s1.length-1)==' '){s1=s1.substring(0,s1.length-1);}}return s1;}\n\r");
		sb.append(" </script>\n\r");
		return sb.toString();
	}

	/**
	 * 得到调用JS
	 * 
	 * @return String JS
	 */
	public static String getCall() {
		return getCall(0);
	}

	/**
	 * 得到调用JS
	 * 
	 * @param i 第几个表单
	 * @return String JS
	 */
	public static String getCall(int i) {
		StringBuffer sb = new StringBuffer();
		sb.append(" \n\r<script language='javascript'>");
		sb.append(" var forms_name=document.forms[").append(i).append("].name;");
		sb.append(" setIniData(forms_name);");
		sb.append(" </script>\n\r");
		return sb.toString();
	}

	/**
	 * 空值转换
	 * 
	 * @param value 原字符
	 * @param defaut 缺省字符
	 * @return String 处理后字符
	 */
	public static String getTxtNvl(Object value, String defaut) {
		if (value == null) {
			return defaut;
		}
		String str = value.toString();
		return str;
	}

	/**
	 * 将文件名中的汉字转为UTF8编码的串,以便下载时能正确显示另存的文件名. 纵横软件制作中心雨亦奇2003.08.01
	 * 
	 * @param s 原文件名
	 * @return 重新编码后的文件名
	 */
	public static String toUtf8String(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 0 && c <= 255) {
				sb.append(c);
			} else {
				byte[] b;
				try {
					b = Character.toString(c).getBytes("utf-8");
				} catch (Exception ex) {
					// System.out.println(ex);
					b = new byte[0];
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0) {
						k += 256;
					}
					// sb.append("%" + Integer.toHexString(k).toUpperCase());
					sb.append("%").append(Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 将文件名中的汉字转为UTF8编码的串,以便下载时能正确显示另存的文件名.
	 * 
	 * @param s 原字符串
	 * @return 重新编码后的文件名
	 */
	public static String toUTF8(String s) {
		String temp = s;
		try {
			temp = new String(temp.getBytes("UTF-8"), "UTF-8");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return temp;
	}

	/**
	 * 将文件名中的汉字转为GBK编码的串,以便下载时能正确显示另存的文件名. 当已经确定是简体汉字的时候
	 * 就可以直接转换为GB2312,如果还包含繁体字，则用GBK
	 * 
	 * @param s 原文件名
	 * @return 重新编码后的文件名
	 */
	public static String toGBK2312(String s) {
		String temp = s;
		try {
			temp = new String(temp.getBytes("GB2312"), "GB2312");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return temp;
	}

	/**
	 * 解析一个时间段 输入：`B0112:~LastDate：33：44` ~LastDate为2009-01-01 输出：B0112 BETWEEN
	 * '1955-01-01' and '1966-01-01'
	 * 
	 * @param caclutString 时间段解析串
	 * @param lastDate 获取时间
	 * @return
	 */
	private static String getAgeUtil(String caclutString, String lastDate) {
		if (caclutString.indexOf("`") == -1) {
			return caclutString;
		}
		String returnAge = "";
		String lastStr_old = caclutString.substring(caclutString.indexOf("`") + 1);
		String lastStr_temp = lastStr_old.substring(0, lastStr_old.indexOf("`"));
		// 解析数组
		/**
		 * 第一个元素为查询字段 第二个元素为查询的基数 第三个为必须大于值 第四个为必须小于的值
		 */
		String[] lastStr_culomn = lastStr_temp.split(":");
		if (lastStr_culomn.length == 0) {
			return "";
		}
		returnAge = " (" + lastStr_culomn[0] + " BETWEEN ";
		int begin = 0;
		int end = 0;
		int year = 0;
		String beginStr = "TO_DATE('1900-01-01','YYYY-MM-DD')";
		String endStr = "TO_DATE('" + lastDate + "','YYYY-MM-DD')";
		year = Integer.parseInt(lastDate.substring(0, 4));
		if (lastStr_culomn.length == 4 && !lastStr_culomn[2].equals("") && !lastStr_culomn[3].equals("")) {
			begin = Integer.parseInt(lastStr_culomn[2]);
			end = Integer.parseInt(lastStr_culomn[3]);
			int tmp = end;
			end = begin;
			begin = tmp;
			begin = year - begin;
			beginStr = "TO_DATE('" + String.valueOf(begin).trim() + "-01-01','YYYY-MM-DD')";
			end = year - end;
			endStr = "TO_DATE('" + String.valueOf(end).trim() + "-12-31','YYYY-MM-DD')";
		} else {
			if (!lastStr_culomn[2].equals("")) {
				begin = Integer.parseInt(lastStr_culomn[2]);
				begin = year - begin;
				beginStr = "TO_DATE('" + String.valueOf(begin).trim() + "-01-01','YYYY-MM-DD')";
			}
			if (lastStr_culomn.length == 4) {
				end = Integer.parseInt(lastStr_culomn[3]);
				end = year - end;
				endStr = "TO_DATE('" + String.valueOf(end).trim() + "-12-31','YYYY-MM-DD')";
			}
		}
		returnAge = returnAge + beginStr + " AND " + endStr + ") ";
		caclutString = caclutString.replaceAll("`" + lastStr_temp + "`", returnAge);
		return caclutString;
	}

	/**
	 * 获取年龄
	 * 
	 * @param caclutString
	 * @param lastDate
	 * @return
	 */
	public static String getAge(String caclutString, String lastDate) {
		String returnSQL = "";
		while (caclutString.indexOf("`") != -1) {
			caclutString = getAgeUtil(caclutString, lastDate);
		}
		returnSQL = caclutString;
		return returnSQL;
	}

	/**
	 * 获取报告期的最后一天
	 * 
	 * @param reportYear
	 * @param reportMonth
	 * @return
	 */
	public final static String getLastDayOfMonth(String reportYear, String reportMonth) {
		String tempS = reportMonth;
		if ("00".equals(reportMonth) || "".equals(reportMonth) || "0".equals(reportMonth)) {
			reportMonth = "12";
		}
		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, Integer.parseInt(reportYear));// 把日期设置为当月第一天
		a.set(Calendar.MONTH, Integer.parseInt(reportMonth) - 1);// 把日期设置为当月第一天
		a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
		String tempDay = String.valueOf(a.get(Calendar.DATE));
		// System.out.println("该月最大天数:"+tempDay);
		String lastDayOfMonth = (reportYear + "-" + tempS).replaceAll("-00", "-12") + "-" + tempDay;// 上报月的最后一天
		return lastDayOfMonth;
	}

	/**
	 * 四舍五入的方法
	 * 
	 * @param v
	 * @param scale
	 * @return
	 */
	public final static String dealWithDecimal(String v, int scale) {
		if (v == null || v.trim().equals("") || v.trim().equals("0"))
			return v;
		BigDecimal b = new BigDecimal(v);
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).toString();
	}

	/**
	 * 将Clob字段中的数据转换为String
	 * 
	 * @param clob
	 * @return
	 */
	public static String transClobToString(Clob clob) {
		String context = "";
		BufferedReader br = null;
		StringWriter out = null;
		try {
			br = new BufferedReader(clob.getCharacterStream());
			out = new StringWriter();
			int c;
			while ((c = br.read()) != -1) {
				out.write(c);
			}
			context = out.toString();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
					e.printStackTrace();
				}
			}
			if (out != null) {
				out.flush();
				try {
					out.close();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
					e.printStackTrace();
				}
			}
		}
		return context;

	}

	/**
	 * 将字符串转换为整数,如果转换失败,则返回指定的默认值
	 * 
	 * @param parseStr 需要转换的字符串
	 * @param defaultInt 转换失败返回的默认值
	 * @return 转换后的整数值
	 */
	public final static int parseInt(String parseStr, int defaultInt) {
		int retInt = defaultInt;
		try {
			retInt = Integer.parseInt(parseStr);
		} catch (Exception ex) {
			retInt = defaultInt;
		}
		return retInt;
	}

	/**
	 * 将字符串转换为双精度类型数据,如果转换失败,则返回指定的默认值
	 * 
	 * @param parseStr 需要转换的字符串
	 * @param defaultDouble 转换失败返回的默认值
	 * @return 转换后的双精度值
	 */
	public final static double parseDouble(String parseStr, double defaultDouble) {
		double retDouble = defaultDouble;
		try {
			retDouble = Double.parseDouble(parseStr);
		} catch (Exception ex) {
			retDouble = defaultDouble;
		}
		return retDouble;
	}

	/**
	 * 将字符串转换为长整型,如果转换失败,则返回指定的默认值
	 * 
	 * @param parseStr 需要转换的字符串
	 * @param defaultLong 转换失败返回的默认值
	 * @return 转换后的长整型
	 */
	public final static long parseLong(String parseStr, long defaultLong) {
		long retLong = defaultLong;
		try {
			retLong = Long.parseLong(parseStr);
		} catch (Exception ex) {
			retLong = defaultLong;
		}
		return retLong;
	}

	/**
	 * 将字符串转换为大数类型数据,如果转换失败,则返回指定的默认值
	 * 
	 * @param parseStr 需要转换的字符串
	 * @param defaultDouble 转换失败返回的默认值
	 * @return 转换后的大数值
	 */
	public final static BigDecimal parseBigNum(String parseStr, double defaultDouble) {

		BigDecimal retDouble = BigDecimal.valueOf(defaultDouble);
		try {
			retDouble = new BigDecimal(parseStr);
		} catch (Exception ex) {
			retDouble = BigDecimal.valueOf(defaultDouble);
		}
		return retDouble;
	}

	/**
	 * 字符串相关转换 <br>
	 * 例如convertValue(1,3,"0"),转换后为001
	 * 
	 * @param converInt 需要转换的整数值
	 * @param strLength 需要转换为的字符串长度
	 * @param defaultStr 如果长度不够,在前面添加的默认字符串
	 * @return 转后后的字符串
	 */
	public final static String convertValue(int converInt, int strLength, String defaultStr) {
		StringBuffer retStr = new StringBuffer();;
		String tempStr = String.valueOf(converInt);
		if (tempStr != null && tempStr.trim().length() > 0) {
			int length = tempStr.trim().length();
			for (int i = length; i < strLength; i++) {
				retStr.append(defaultStr);
			}
			retStr.append(tempStr);
		}
		return retStr.toString();
	}

	/**
	 * 字符串相关转换 <br>
	 * 例如convertValue("1",3,"0"),转换后为001
	 * 
	 * @param converStr 需要转换的字符串
	 * @param strLength 需要转换为的字符串长度
	 * @param defaultStr 如果长度不够,在前面添加的默认字符串
	 * @return 转后后的字符串
	 */
	public final static String converValue(String converStr, int strLength, String defaultStr) {
		StringBuffer retStr = new StringBuffer();
		String tempStr = converStr;
		if (tempStr != null && tempStr.trim().length() > 0) {
			int length = tempStr.trim().length();
			for (int i = length; i < strLength; i++) {
				retStr.append(defaultStr);
			}
			retStr.append(tempStr);
		}
		return retStr.toString();
	}

	/**
	 * 将指定的字符串转换为指定html分割符的字符串<br>
	 * 如：converHtml("test",2,"<br>
	 * ")="te<br>
	 * st"
	 * 
	 * @param converStr 需要转换的字符串
	 * @param length 分割长度
	 * @param middleStr 中间需要查询的html符号
	 * @return 转换后的字符串
	 */
	public final static String converHtml(String converStr, int length, String middleStr) {
		StringBuffer retStr = new StringBuffer();
		if (converStr != null && converStr.trim().length() > 0) {
			String tempStr = converStr.trim();
			int tempLength = tempStr.length();
			int cycleInt = tempLength / length;
			int afterInt = tempLength % length;
			for (int i = 0; i < cycleInt; i++) {
				if (retStr.length() == 0) {
					// retStr = retStr + tempStr.substring(i * length, i *
					// length + length);
					retStr.append(tempStr.substring(i * length, i * length + length));
				} else {
					retStr.append(middleStr);
					retStr.append(tempStr.substring(i * length, i * length + length));
					// retStr = retStr + middleStr+ tempStr.substring(i *
					// length, i * length + length);
				}
			}
			if (retStr.length() == 0) {
				// retStr = retStr + tempStr.substring(tempLength - afterInt,
				// tempLength);
				retStr.append(tempStr.substring(tempLength - afterInt, tempLength));
			} else {
				retStr.append(middleStr);
				retStr.append(tempStr.substring(tempLength - afterInt, tempLength));
				// retStr = retStr + middleStr + tempStr.substring(tempLength -
				// afterInt, tempLength);
			}
		}
		return retStr.toString();
	}

	/**
	 * 把指定的字符串转为与之对应的布尔型数据<br>
	 * 一下字符串用本方法转为，将被转换为true： <li>y(不区分大小写) <li>yes(不区分大小写) <li>true(不区分大小写) <li>
	 * 1(不区分大小写)
	 * 
	 * 其余未被列出的字符串将转换为false<br>
	 * 
	 * @param theString 指定的需要转换的字符串
	 * @return 转换后的布尔型数据
	 */
	public final static boolean toBoolean(String theString) {
		if (theString == null) {
			return false;
		}
		theString = theString.trim();
		if (theString.equalsIgnoreCase("y") || theString.equalsIgnoreCase("yes") || theString.equalsIgnoreCase("true") || theString.equalsIgnoreCase("1")) {
			return true;
		}
		return false;
	}

	/**
	 * 将0的字符串转换为0的大数形
	 * 
	 * @param value 需要转换的字符串
	 * @return 转换后的数据 修改日期2009-3-20
	 */
	public final static BigDecimal retZero(String value) {
		BigDecimal retStr = new BigDecimal(0);
		if (value != null) {
			if (PublicUtil.parseDouble(value, 0) == 0) {
				retStr = new BigDecimal(0);
			} else {
				retStr = new BigDecimal(value);
			}

		}
		return retStr;
	}

	/**
	 * deleteFolder
	 * @param filePath
	 */
	public static void deleteFolder(String filePath) {
		try {
			File file = new File(filePath);
			if (file.isDirectory()) {
				for (File child : file.listFiles()) {
					deleteFolder(child.getAbsolutePath());
				}
				file.delete();
			} else {
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 将报告期转换成中文描述
	 * 
	 * @param reportData
	 * @return
	 */
	public final static String transReportDate(String reportDate, String reportDateValue, String dataValueType) throws Exception {
		String reportDateName = "";
		if (reportDate.equals(reportDateValue)) {
			if ("01".equals(dataValueType)) {
				reportDateName = "当期值";
			} else {
				reportDateName = "累计值";
			}
		} else {
			reportDateName = getReportDateDescZh(reportDateValue.substring(4));
		}
		return reportDateName;
	}

	/**
	 * 将报告期转化为中文描述
	 * 
	 * @param month 月或者季度
	 * @return 报表值状态中文描述
	 */
	public final static String getReportDateDescZh(String month) {
		int valueInt = Integer.parseInt(month);
		String retStr = "";
		if (valueInt > 0 && valueInt <= 12) {
			retStr = valueInt + "月";
		} else {
			int season = valueInt - 12;
			retStr = season + "季度";
		}
		return retStr;
	}

	/**
	 * 读取props文件中的key 属性对应的值
	 * 
	 * @param fileDir
	 * @param key
	 * @return
	 */
	public final static String getPropertiesValue(String fileDir, String key) {
		String retStr = null;
		InputStream inStream = null;
		try {
			inStream = new FileInputStream(fileDir);
			props.load(inStream);
			retStr = props.getProperty(key);
			props.clear();
		} catch (FileNotFoundException e) {
			log.error(e);
			e.printStackTrace();
		} catch (IOException e2) {
			log.error(e2);
			e2.printStackTrace();
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
					e.printStackTrace();
				}
			}
		}
		return retStr;
	}



	 

 

	/**
	 * 判断字符串是否为空
	 * 
	 * @param nvl 要判断的字符串
	 * @return true/false
	 */
	public static boolean nvl(Object val) {
		String valStr = val + "";
		if (valStr.trim().length() > 0 && !"NULL".equalsIgnoreCase(valStr) && !"undefined".equalsIgnoreCase(valStr)  ) {
			return false;
		}
		return true;
	}

	/**
	 * 判断字符串是否为空,如果为空，则用第二个参数代替
	 * 
	 * @param nvl 要判断的字符串
	 * @param defaultStr 当第一个参数为空时，替换的值
	 * @return true/false
	 */
	public static String nvl(Object val, String defaultStr) {
		boolean nvl = PublicUtil.nvl(val);
		if (nvl) {
			return defaultStr;
		}
		return val.toString().trim();
	}

	/**
	 * 得到java文件跟目录
	 * 
	 * @param clss
	 * @return
	 */
	public static String getRootPath(@SuppressWarnings("rawtypes") Class clss) {
		URL url = clss.getProtectionDomain().getCodeSource().getLocation();
		return url.getPath();
	}

	/**
	 * 提供一个方法，判断参数是否包含 SQL 关键字，用于防止 SQL 注入攻击。
	 * 
	 * @param p_str 要检查的值。
	 * @return true 通过检查
	 */
	public static boolean isSqlInjection(String str) {
		if (str == null || "".equals(str)) {
			return true;
		}
		String regexSql = "(\\sand\\s)|(\\sor\\s)|(\\slike\\s)|(select\\s)|(insert\\s)|(delete\\s)|(update\\s[\\s\\S].*\\sset)|(create\\s)"
				+ "|(\\stable)|(\\sexec)|(declare)|(\\struncate)|(\\smaster)|(\\sbackup)|(\\smid)|(\\scount)"
				+ "|(\\sadd\\s)|(\\salter\\s)|(\\sdrop\\s)|(\\sfrom\\s)|(\\struncate\\s)|(\\sunion\\s)|(\\sjoin\\s)";
		Pattern p = Pattern.compile(regexSql);
		Matcher m = p.matcher(str.toLowerCase());

		if (m.matches()) {
			return false;
		}
		return true;
	};

	/**
	 * getpOrgCodeArray 
	 * @param provinceId
	 * @return
	 */
	public static String[] getpOrgCodeArray(String provinceId) {
		if (provinceId == null || "".equals(provinceId)) {
			return null;
		}
		if (provinceId.indexOf("_") > 0) {
			return provinceId.split("_");
		}
		return null;
	}
	public static List<String> removeDuplicate(List<String> list) {
		HashSet set =new HashSet<String>(list);		
		list =new ArrayList<String>();
		list.addAll(set);
		return list;
	}
	public static void main(String[] args) {
		boolean flag = false;
//		String accUrl="PlatformConfigUtil.getString("ISC_APPNAME")login/default.jsp";
//		List <String> funTreeList = new ArrayList<String>();
//		funTreeList.add("1111");
//		List<String> publicUrlList= getPublicUrl();
//		for(String e1:publicUrlList){ 
//			 if(!funTreeList.contains(e1)){ 
//				 funTreeList.add(e1); 
//			} 
//		}
//		for(String iscFunUrl:funTreeList){ 
//			if (iscFunUrl != null && !"".equals(iscFunUrl)&& accUrl.startsWith(iscFunUrl)) {
//				flag = true;
//				break;
//			}
//		}
//		if (!flag) {
//			System.out.println("越权" +accUrl);
//		}else{
//			System.out.println("通过" +accUrl);
//		}
//		System.out.println("aaunion".endsWith("union"));
		
//		System.out.println("aaunion".endsWith("union"));
//		System.out.println("aaunion".startsWith("aaa"));
		
//		String str ="/mrda/finModel/sysNotice/index.jsp?type=2&rnd=0.8287063476163894";
//		System.out.println(str);
//		System.out.println(str.substring(0,str.lastIndexOf("&rnd") ));
		
		System.out.println("rnd=0.9658039743117766 and &i= insert".indexOf(" and "));
		
		System.out.println(PublicUtil.nvl(null));
		
		
	}
	
}
