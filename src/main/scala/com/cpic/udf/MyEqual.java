package com.cpic.udf;

import java.text.ParseException;

import org.apache.hadoop.hive.ql.exec.UDF;

import com.cpic.utils.PublicUtil;

public class MyEqual extends UDF {
	public boolean evaluate(String arg1,String arg2) {
		String newArg1 = PublicUtil.nvlString(arg1);
		String newArg2 = PublicUtil.nvlString(arg2);
		if(newArg1.equals("") && newArg1.equals(newArg2 )){
			return false;
		}else{
			
		} return newArg1.equals(newArg2 );
		
	}
	public static void main(String[] args) throws ParseException {
		System.out.println(new MyEqual().evaluate("", "") );
	}
}

