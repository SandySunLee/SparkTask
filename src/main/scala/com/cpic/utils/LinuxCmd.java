package com.cpic.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LinuxCmd {
	public static int exec(String[] cmds) throws IOException, InterruptedException {
//		String[] cmds = { "/bin/sh", "-c", "ps -ef|grep exec_daily_02.sh" };
		Process pro = Runtime.getRuntime().exec(cmds);
		pro.waitFor();
		InputStream in = pro.getInputStream();
		BufferedReader read = new BufferedReader(new InputStreamReader(in));
		String line = null;
		List list = new ArrayList<String>();
		while ((line = read.readLine()) != null) {
			System.out.println(line);
			System.out.println("命令状态长度" + line.length());
			list.add(line);
		}
		System.out.println("exec_daily_相关进程个数" + list.size());
		return list.size();
	}
}
