package com.cpic.udf;

import java.text.ParseException;

import org.apache.hadoop.hive.ql.exec.UDF;

import com.cpic.utils.DateUtils;

public class MyDateAdd extends UDF {
	public String evaluate(String arg1,Integer days) {
		try {
			
			return DateUtils.convertDateToStr(DateUtils.add(  DateUtils.parseToDate(arg1),days), "yyyy-MM-dd");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

	public static void main(String[] args) throws ParseException {
		System.out.println(new MyDateAdd().evaluate("20080211",-22));
	}
}
/*
 * set role admin;
 * 
 * hadoop fs -put cx_udf.jar /user/hive/udf
 * 
 * add jar /home/software/jar/cx_udf.jar
 * 
 * create temporary function myToUpCase as 'com.cpic.udf.ToUpperUDF'
 * 
 * show functions;
 * 
 * /user/hive/udf/MyUDF.jar create function udf_nvl as 'com.cpic.udf.ToUpperUDF' using JAR "hdfs://hacluster/user/hive/udf/cx_udf.jar" /user/hive/udf/MyUDF.jar
 */

// case when (b.operator_code is null or length(trim(b.operator_code))=0) then ' ' else b.operator_code end operator_code ,

// drop function udf_nvl;

// create function udf_nvl as 'com.cpic.udf.MyNull' using JAR "hdfs://hacluster/user/hive/udf/cx_udf.jar"

