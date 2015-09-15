package com.appCrawler.pagePro.apkDetails;

/**
 * 亿软网 http://www.yruan.com/
 * Yruan #125	未完成
 * @author DMT
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

public class Yruan_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory
			.getLogger(Yruan_Detail.class);

	public static Apk getApkDetail(Page page) {
		Apk apk = null;
		String appName = null; // app名字
		String appDetailUrl = null; // 具体页面url
		String appDownloadUrl = null; // app下载地址
		String osPlatform = null; // 运行平台
		String appVersion = null; // app版本
		String appSize = null; // app大小
		String appUpdateDate = null; // 更新日期
		String appType = null; // 下载的文件类型 apk？zip？rar？
		String appVenderName = null; // app开发者 APK这个类中尚未添加
		String appDownloadedTime = null; // app的下载次数
		String appDescription = null; // app的详细描述
		List<String> appScrenshot = null; // app的屏幕截图
		String appTag = null; // app的应用标签
		String appCategory = null; // app的应用类别
		
		
		//如果不是安卓平台的直接返回	
		String platFormString =page.getHtml().xpath("//div[@class='right']/div[1]/ul/li[2]/span/text()").toString();
		if(platFormString.contains("Android") || platFormString.contains("android"))
			osPlatform = platFormString.substring(platFormString.indexOf("：")+1,platFormString.length());
		else return null;
		
		String nameString=page.getHtml().xpath("//div[@class='right']/div[1]/ul/li[1]/a/text()").toString();			
			appName =nameString;
			
		appDetailUrl = page.getUrl().toString();
		
		appDownloadUrl = page.getHtml().xpath("//div[@class='right']/div[1]/ul/li[3]/div[2]/a/@href").toString();
		
		
		String versionString = null;
			appVersion = versionString;
			
		String sizeString = page.getHtml().xpath("//div[@class='right']/div[1]/ul/li[1]/text()").toString();
			appSize = sizeString.substring(sizeString.lastIndexOf("：")+1,sizeString.length());
		
		String updatedateString = null;
			appUpdateDate = updatedateString;
		
		String typeString = page.getHtml().xpath("//div[@class='right']/div[1]/ul/li[1]/text()").toString();
			appType =typeString.substring(typeString.indexOf("：")+1,typeString.indexOf("大小")-1);
		
			appVenderName=null;
			
		String DownloadedTimeString = null;
			appDownloadedTime = DownloadedTimeString;	
		System.out.println("appName=" + appName);
		System.out.println("appDetailUrl=" + appDetailUrl);
		System.out.println("appDownloadUrl=" + appDownloadUrl);
		System.out.println("osPlatform=" + osPlatform);
		System.out.println("appVersion=" + appVersion);
		System.out.println("appSize=" + appSize);
		System.out.println("appUpdateDate=" + appUpdateDate);
		System.out.println("appType=" + appType);
		System.out.println("appVenderName=" + appVenderName);
		System.out.println("appDownloadedTime=" + appDownloadedTime);
		System.out.println("appDescription=" + appDescription);
		System.out.println("appTag=" + appTag);
		System.out.println("appScrenshot=" + appScrenshot);
		System.out.println("appCategory=" + appCategory);

		if (appName != null && appDownloadUrl != null) {
			apk = new Apk(appName, appDetailUrl, appDownloadUrl, osPlatform,
					appVersion, appSize, appUpdateDate, appType, null);
			// Apk(String appName,String appMetaUrl,String appDownloadUrl,String
			// osPlatform ,
			// String appVersion,String appSize,String appTsChannel, String
			// appType,String cookie){
			apk.setAppDownloadTimes(appDownloadedTime);
			apk.setAppVenderName(appVenderName);
			apk.setAppTag(appTag);
			apk.setAppScreenshot(appScrenshot);
			apk.setAppDescription(appDescription);
			apk.setAppCategory(appCategory);

		} else
			return null;

		return apk;
	}
	
	
}
