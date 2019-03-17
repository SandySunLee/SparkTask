package com.cpic.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

import com.cpic.utils.PublicUtil;

public class MyNull extends UDF {
	public String evaluate(String arg1, String arg2) {
		String newArg1 = PublicUtil.nvlString(arg1).trim();
		String newArg2 = PublicUtil.nvlString(arg2).trim();
		if(PublicUtil.nvl(newArg1)){
			return newArg2;
		}else{
			return newArg1;
		}
		
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
 * /user/hive/udf/MyUDF.jar create function udf_nvl as 'com.cpic.udf.ToUpperUDF' using JAR "hdfs://hacluster/user/hive/udf/cx_udf.jar"
 * /user/hive/udf/MyUDF.jar
 */

// case when (b.operator_code is null or length(trim(b.operator_code))=0) then ' ' else b.operator_code end operator_code ,


//drop function udf_nvl;


//  create function udf_nvl as 'com.cpic.udf.MyNull' using JAR "hdfs://hacluster/user/hive/udf/cx_udf.jar"




