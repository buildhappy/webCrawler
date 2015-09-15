package com.appCrawler.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 输出2015-01-05 16:56:35格式的时间
 * @author Administrator
 *
 */
public class DateUtil {

	public static String formatDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H:m:s");
		String time = sdf.format(date);
		return time;
	}

}