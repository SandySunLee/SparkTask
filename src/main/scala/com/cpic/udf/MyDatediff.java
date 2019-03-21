package com.cpic.udf;

import java.text.ParseException;

import org.apache.hadoop.hive.ql.exec.UDF;

import com.cpic.utils.DateUtils;
import com.cpic.utils.PublicUtil;

/**
 * 比较两个日期的差异
 */
public class MyDatediff  extends UDF {
	public String evaluate(String arg1, String arg2) {
		Long start=System.currentTimeMillis();
		String newArg1 = PublicUtil.nvlString(arg1).trim();
		String newArg2 = PublicUtil.nvlString(arg2).trim();
		if(PublicUtil.nvl(newArg1) ||PublicUtil.nvl(newArg2)){
			return "";
		}
		double time1=0;
		double time2=0;
		try {
			time1 = DateUtils.parseToDate(newArg1).getTime();
			time2 = DateUtils.parseToDate(newArg2).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	
		double diff = (time1 - time2) / 1000 / 60 / 60 / 24;
		Long cha=System.currentTimeMillis()-start;
		System.out.println(cha+"cha");
		return "" + diff;

	}

	public static void main(String[] args) throws ParseException {
//		2018/9/9
		System.out.println(new MyDatediff().evaluate("2018-11-13 22:22:22", "2018/9/9")  );
		System.out.println(new MyDatediff().evaluate("20180809111212", "2018/03/9")  );
		System.out.println(new MyDatediff().evaluate("20180809111212", "2018/03/9")  );
		System.out.println(new MyDatediff().evaluate("2018/11/12 22:22:22", "2018-11-11 22:22:22"));
		System.out.println(new MyDatediff().evaluate("2018/11/12 22:22:22.0", "2018-11-11 22:22:22.0"));
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

