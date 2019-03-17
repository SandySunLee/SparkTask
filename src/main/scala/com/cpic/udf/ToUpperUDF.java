package com.cpic.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

public class ToUpperUDF extends UDF {
	public String evaluate(String str){
		return str.trim().toUpperCase();
	}
}

/* set role admin;
 * 
 * add jar  hdfs://hacluster/user/hive/udf/cx_udf.jar
 * 
 * create temporary function myToUpCase as 'com.cpic.udf.ToUpperUDF'
 * 
 * show functions;
 * 
 * /user/hive/udf/MyUDF.jar
 *   create function udf_upper as 'com.cpic.udf.ToUpperUDF' using JAR "hdfs://hacluster/user/hive/udf/cx_udf.jar"
 *   
 */





//CREATE FUNCTION default.add AS 'com.cpic.udf.Add' USING JAR 'hdfs://hacluster/user/hive/udf/cx_udf.jar';
