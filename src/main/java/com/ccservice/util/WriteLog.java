package com.ccservice.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Calendar;

public class WriteLog {

	public static void write(String fileNameHead, String logString) {

		try {

			String logFilePathName = null;

			Calendar cd = Calendar.getInstance();

			int year = cd.get(Calendar.YEAR);

			String month = addZero(cd.get(Calendar.MONTH) + 1);

			String day = addZero(cd.get(Calendar.DAY_OF_MONTH));

			String hour = addZero(cd.get(Calendar.HOUR_OF_DAY));

			String min = addZero(cd.get(Calendar.MINUTE));

			String sec = addZero(cd.get(Calendar.SECOND));

			File fileParentDir = new File("D:/yufuinsert");

			if (!fileParentDir.exists()) {

				fileParentDir.mkdir();

			}

			if (fileNameHead == null || fileNameHead.equals("")) {

				logFilePathName = "D:/yufuinsert/" + year + month + day + ".log";

			} else {

				logFilePathName = "D:/yufuinsert/" + fileNameHead + year + month + day + ".log";

			}

			PrintWriter printWriter = new PrintWriter(new FileOutputStream(logFilePathName, true));// �����ļ�βд����־�ַ�

			String time = "[" + year + "-" + month + "-" + day + " " + hour + ":" + min + ":" + sec + "] ";

			printWriter.println(time + logString);

			printWriter.flush();

		} catch (FileNotFoundException e) {

			// TODO Auto-generated catch block

			e.getMessage();

		}

	}

	public static String addZero(int i) {

		if (i < 10) {

			String tmpString = "0" + i;

			return tmpString;

		} else {
			return String.valueOf(i);
		}

	}

}
