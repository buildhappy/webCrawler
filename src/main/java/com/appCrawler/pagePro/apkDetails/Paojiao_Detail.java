package com.appCrawler.pagePro.apkDetails;

/**
 * 泡椒网手机软件下载 www.paojiao.cn/
 * Paojiao #133	网页打不开，未完成
 * (1)有网游和单击软件两种详细页面
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

public class Paojiao_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory
			.getLogger(Paojiao_Detail.class);

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
		
		String nameString=page.getHtml().xpath("//h2[@class='detail-title']/text()").toString();			
		if(nameString != null && nameString.contains("V"))
		{
			appName=nameString.substring(0,nameString.indexOf("V")-1);
		
		}
		else if(nameString != null && nameString.contains("v"))
		{
			appName=nameString.substring(0,nameString.indexOf("v")-1);
			
		}
		else if(nameString != null && nameString.contains("."))
		{
			appName=nameString.substring(0,nameString.indexOf(".")-1);
			
		}
		else 
		{
			appName = nameString;
		}
			
		appDetailUrl = page.getUrl().toString();
		String appdownloadurlString =page.getHtml().xpath("//ul[@class='side_nav']/li[2]/p/a[1]/@href").toString();
		appDownloadUrl = appdownloadurlString;
		
		String versionString = page.getHtml().xpath("//ul[@class='info']/li[3]/text()").toString();
			appVersion = versionString;
		
		String sizeString = page.getHtml().xpath("//ul[@class='info']/li[1]/text()").toString();
			appSize = sizeString;
		
		String updatedateString = page.getHtml().xpath("//ul[@class='info']/li[5]/text()").toString();
			appUpdateDate = updatedateString;
		
		
		appType ="apk";
		
		String DownloadedTimeString = page.getHtml().xpath("//ul[@class='info']/li[6]/text()").toString();
			appDownloadedTime = DownloadedTimeString;
		
		String descriptionString = page.getHtml().xpath("//div[@class='info-content']/div[2]").toString();
		String allinfoString = descriptionString;
		if(allinfoString == null ) return null;
		while(allinfoString.contains("<"))
			if(allinfoString.indexOf("<") == 0) allinfoString = allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
			else if(allinfoString.contains("<!--")) allinfoString = allinfoString.substring(0,allinfoString.indexOf("<!--")) + allinfoString.substring(allinfoString.indexOf("-->")+3,allinfoString.length());
			else allinfoString = allinfoString.substring(0,allinfoString.indexOf("<")) + allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
		appDescription = allinfoString.replace("\n", "").replace("\t", "").trim();
	
		
		appCategory = page.getHtml().xpath("//ul[@class='info']/li[4]/text()").toString();
		appScrenshot = page.getHtml().xpath("//div[@class='gallery_box gallery']//img/@src").all();
		
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
	
	
	private static String usefulInfo(String allinfoString)
	{
		String info = null;
		while(allinfoString.contains("<"))
			if(allinfoString.indexOf("<") == 0) allinfoString = allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
			else if(allinfoString.contains("<!--")) allinfoString = allinfoString.substring(0,allinfoString.indexOf("<!--")) + allinfoString.substring(allinfoString.indexOf("-->")+3,allinfoString.length());
			else allinfoString = allinfoString.substring(0,allinfoString.indexOf("<")) + allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
		info = allinfoString.replace("\n", "").replace(" ", "");
		return info;
	}
	
	
}
