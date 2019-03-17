package com.cpic.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;

import com.cpic.utils.LinuxCmd;

public class FileUtils {

	/***
	 * 该方法只能 移动文本文件 source目录文件移动到target
	 * 
	 * @param source
	 * @param target
	 * @return
	 * @throws InterruptedException
	 */

	public static boolean moveTextFile(String source, String target) throws InterruptedException {
		System.out.println("移动文本文件moveTextFile");
		boolean flag = true;
		FileReader fr = null;
		FileWriter fw = null;

		boolean oexists = (new File(target.substring(0, target.lastIndexOf("/")))).isDirectory();
		if (!oexists) {
			(new File(target.substring(0, target.lastIndexOf("/")))).mkdirs();
		}

		try {
			fr = new FileReader(source);
			fw = new FileWriter(target);

			char[] chs = new char[1024];

			int len = 0;

			while ((len = fr.read(chs)) != -1) {
				fw.write(chs, 0, len);
			}

			String cmd = "chmod 755  " + target;
			System.out.println("赋权-----------::" + cmd);
			String[] cmds = { "/bin/sh", "-c", cmd };
			LinuxCmd.exec(cmds);

		} catch (IOException e) {
			flag = false;
			System.out.println(e.toString());
		} finally {
			try {
				if (fw != null)
					fw.close();
			} catch (IOException e) {
				System.out.println(e.toString());
			}
			try {
				if (fr != null)
					fr.close();
			} catch (IOException e) {
				System.out.println(e.toString());
			}
		}
		System.out.println(flag);
		return flag;
	}

	/**
	 * 把String 字符串写入到文件中
	 * 
	 * @param fileDir
	 * @param fileName
	 * @param fileContent
	 * @throws IOException
	 * @throws InterruptedException
	 */

	public static void write2File(String fileDir, String fileName, String fileContent) throws IOException, InterruptedException {
		boolean oexists = (new File(fileDir)).isDirectory();
		if (!oexists) {
			(new File(fileDir)).mkdirs();
		}
		FileWriter fw = new FileWriter(new File(fileDir, fileName), false);
		String cmd = "chmod 755  " + fileDir + File.separator + fileName;
		System.out.println("赋权-----------::" + cmd);
		String[] cmds = { "/bin/sh", "-c", cmd };
		LinuxCmd.exec(cmds);

		BufferedWriter bufw = new BufferedWriter(fw);

		bufw.write(fileContent);
		bufw.newLine();
		bufw.flush();

		bufw.close();
	}

	/***
	 * 列出目录下的所有文件
	 */
	public static void method_list() {
		File dir = new File("c:\\");

		File[] files = dir.listFiles();

		for (File file : files) {
			if (!file.isHidden())
				System.out.println(file);
		}

		System.out.println("------------------");
		String[] names = dir.list();// 列出指定目的下当前的文件和文件夹名称。并列出隐藏文件。

		for (String name : names) {
			System.out.println(name);
		}

		/*
		 * File[] roots = File.listRoots();
		 * 
		 * for(File root : roots) { System.out.println(root); }
		 */
	}

	public static void method_filter() {
		File dir = new File("c:\\Users");

		String[] names = dir.list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				System.out.println(dir + "......" + name);
				return true;
			}
		});

		if (names.length < 1) {
			return;
		}

		for (String name : names) {

			System.out.println(name);
		}
	}

	public static boolean moveStreamFile(String source, String target) throws IOException {
		System.out.println("移动二进制文件moveStreamFile");
		boolean flag = true;
		FileInputStream fis = new FileInputStream(source);
		FileOutputStream fos = new FileOutputStream(new File(target), true);
		byte[] a = new byte[fis.available()];
		System.out.println("avaiable length:" + fis.available());
		int len = 0;
		while (-1 != (len = fis.read(a))) {
			fos.write(a, 0, len);
		}

		// fos.write("abcdef".getBytes());
		fos.flush();
		fos.close();
		System.out.println(flag);
		return flag;
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println(System.getProperty("file.encoding"));
		// method_list();
		// method_filter();
		// moveTextFile("g:\\tony_chen.txt","d:\\tony_chen.txt");
		moveStreamFile("D:\\myjava\\very.log", "d:\\very.log");
		// "D:\\Workspaces1\\java_io\\fos1.txt"
		// "D:\\Workspaces1\\java_io\\fos2.txt"
		// write2File("", "", "hello ");
	}
}
