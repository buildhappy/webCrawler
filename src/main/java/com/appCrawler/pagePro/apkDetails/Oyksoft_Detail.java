package com.appCrawler.pagePro.apkDetails;

/**
 * Oyksoft www.oyksoft.com
 * Oyksoft #128
 * osplatform 字段和实际下载到的平台有时不匹配
 * 网站大部分不是手机应用
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

public class Oyksoft_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory
			.getLogger(Oyksoft_Detail.class);

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
		
		
		

		
		String platFormString =page.getHtml().xpath("//div[@id='softinfo']/ul/li[11]/text()").toString();
		osPlatform = platFormString;
		
		String nameString=page.getHtml().xpath("//div[@id='softtitle']/h1/text()").toString();			
		if(nameString != null && nameString.contains("V"))
		{
			appName=nameString.substring(0,nameString.indexOf("V")-1);
			appVersion = nameString.substring(nameString.indexOf("V")+1,nameString.length());
		}
		else if(nameString != null && nameString.contains("v"))
		{
			appName=nameString.substring(0,nameString.indexOf("v")-1);
			appVersion = nameString.substring(nameString.indexOf("v")+1,nameString.length());
		}
		else if(nameString != null && nameString.contains("."))
		{
			appName=nameString.substring(0,nameString.indexOf(".")-1);
			appVersion = nameString.substring(nameString.indexOf(".")-1,nameString.length());
		}
		else 
		{
			appName = nameString;
			appVersion = null;
		}
		
		
		if(!osPlatform.contains("Android") && !osPlatform.contains("android"))
		{
		
			return null;
			
		}
//		else if(!nameString.contains("Android") && !nameString.contains("android"))
//		{
//			return null;
//			
//		}
			
		appDetailUrl = page.getUrl().toString();
		
		appDownloadUrl = page.getHtml().xpath("//div[@class='down_oyksoft']/a[2]/@href").toString();
		
		
		
		String sizeString = page.getHtml().xpath("//div[@id='softinfo']/ul/li[1]/text()").toString();
			appSize = sizeString;
		
		String updatedateString = page.getHtml().xpath("//div[@id='softinfo']/ul/li[8]/text()").toString();
			appUpdateDate = updatedateString.substring(updatedateString.indexOf("：")+1,updatedateString.length());
		
		String typeString = "apk";
			appType =typeString;
		
		String DownloadedTimeString = page.getHtml().xpath("//div[@id='softinfo']/ul/li[2]/text()").toString();
			appDownloadedTime = DownloadedTimeString;
		
		String descriptionString = page.getHtml().xpath("//div[@id='softcontent']").toString();
			
			while(descriptionString.contains("<"))
				if(descriptionString.indexOf("<") == 0) descriptionString = descriptionString.substring(descriptionString.indexOf(">")+1,descriptionString.length());
				else if(descriptionString.contains("<!--")) descriptionString = descriptionString.substring(0,descriptionString.indexOf("<!--")) + descriptionString.substring(descriptionString.indexOf("-->")+3,descriptionString.length());
				else descriptionString = descriptionString.substring(0,descriptionString.indexOf("<")) + descriptionString.substring(descriptionString.indexOf(">")+1,descriptionString.length());
		appDescription = descriptionString.replace("\n", "").replace(" ", "");		
		
		appScrenshot = page.getHtml().xpath("//div[@id='softcontent']//img/@src").all();
		
		
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
