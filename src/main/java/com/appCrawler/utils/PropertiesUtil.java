package com.appCrawler.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
 * get info from properties file
 * @author buildhappy
 *
 */
public class PropertiesUtil {
	private static Properties pro = null;
	private static int turn = 0;
	
	static{
		pro = new Properties();
		InputStream in = PropertiesUtil.class.getResourceAsStream("/baseInfo.properties");
		try {
			pro.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	public static int getRetryTimes(){
		return Integer.parseInt(pro.getProperty("crawler.retry.times"));
	}
	
	public static int getInterval(){
		return Integer.parseInt(pro.getProperty("crawler.interval"));
	}
	
	public static String getCrawlerDataPath(){
		return pro.getProperty("crawler.data.path");
	}
	//use proxy or not
	public static boolean getCrawlerProxyEnable(){
		return pro.getProperty("crawler.proxy.enable").equals("0");
	}
	
//	public static String getCrawlerProxyHost(){
//		
//		return 	pro.getProperty("crawler.proxy.host");
//	}
		
//	public static int getCrawlerProxyPort(){
//		
//		return Integer.parseInt(pro.getProperty("crawler.proxy.port"));
//	}
	
	public static String[] getCrawlerProxyHostAndPort(){
		String[] hostAndPort = pro.getProperty("crawler.proxy.host_port").split(",");		
		return hostAndPort;		
	}
	public static void main(String[] args) {
		//PropertiesUtil property = new PropertiesUtil();
		//System.out.println(property.getRetryTimes());
		System.out.println(PropertiesUtil.getRetryTimes());
		System.out.println(PropertiesUtil.getInterval());
		System.out.println(PropertiesUtil.getCrawlerDataPath());
		System.out.println(PropertiesUtil.getCrawlerProxyEnable());
//		System.out.println(PropertiesUtil.getCrawlerProxyHost());
//		System.out.println(PropertiesUtil.getCrawlerProxyPort());
	}

}
