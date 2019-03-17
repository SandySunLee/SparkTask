package com.cpic.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Cmd {
	public String exec(String bashCommand) {
		String result ="";
		try {
			System.out.println(bashCommand);
			Runtime runtime = Runtime.getRuntime();
			Process pro = runtime.exec(bashCommand);
			int status = pro.waitFor();
			if (status != 0) {
				System.out.println("Failed to call shell  command    执行失败    " + bashCommand);
				return "0";
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			StringBuffer strbr = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				strbr.append(line).append("\n");
			}

			result = strbr.toString();
			System.out.println(result);
			pro.waitFor();

		} catch (IOException ec) {
//			ec.printStackTrace();
			return "0";
		} catch (InterruptedException ex) {
//			ex.printStackTrace();
			return "0";
		}
		if(result.contains("#")){
			return result.substring(result.indexOf( "###"),result.lastIndexOf("###")).replaceAll("#","");
		}else{
			return "0";
		}
	}
}
