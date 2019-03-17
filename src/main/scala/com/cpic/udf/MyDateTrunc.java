package com.cpic.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

import com.cpic.utils.DateUtils;
import com.cpic.utils.PublicUtil;

public class MyDateTrunc extends UDF {
	public String evaluate(String arg1, String arg2){
		String newArg1 = PublicUtil.nvlString(arg1).trim();
		String newArg2 = PublicUtil.nvlString(arg2).trim();
		if(PublicUtil.nvl(newArg1) ||PublicUtil.nvl(newArg2)){
			return "";
		}
		try {
			Long time1 = DateUtils.parseToDate(newArg1).getTime();
			Long time2  = DateUtils.parseToDate(newArg2).getTime();
			Long diff = (time1 - time2) / 1000 / 60 / 60 / 24;
			return Integer.parseInt(diff+"") +"";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}
//	2018-11-09 22:22:22
	public static void main(String[] args) {
		System.out.println(new MyDatediff().evaluate("2018/01/09", "2018/11/09 22:22:22") );
		System.out.println(new MyDatediff().evaluate("20181111", "2018-11-11 22:22:22") );
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

// create function udf_datetrunc as 'com.cpic.udf.MyDateTrunc' using JAR "hdfs://hacluster/user/hive/udf/cx_udf.jar"
// create function udf_eq as 'com.cpic.udf.MyEqual' using JAR "hdfs://hacluster/user/hive/udf/cx_udf.jar"




