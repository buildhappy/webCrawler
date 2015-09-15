package com.appCrawler.pagePro.apkDetails;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;


public class Duote_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Duote_Detail.class);
	public static Apk getApkDetail(Page page){
		Apk apk = null;
		String appName = null;				//app名字
		String appDetailUrl = null;			//具体页面url
		String appDownloadUrl = null;		//app下载地址
		String osPlatform = null ;			//运行平台
		String appVersion = null;			//app版本
		String appSize = null;				//app大小
		String appUpdateDate = null;		//更新日期
		String appType = null;				//下载的文件类型 apk？zip？rar？
		String appVenderName = null;			//app开发者  APK这个类中尚未添加
		String appDownloadedTime=null;		//app的下载次数
		String appDescription = null;		//app的详细描述
		List<String> appScrenshot = null;			//app的屏幕截图
		String appTag = null;				//app的应用标签
		String appCategory = null;			//app的应用类别 
		String nameString=page.getHtml().xpath("//div[@class='areaSoftinfo']/h1/text()").toString();			
		appName =nameString;
		
	appDetailUrl = page.getUrl().toString();
	
	appDownloadUrl = page.getHtml().xpath("//a[@class='btn_down_to_pc']/@href").toString();
	
	osPlatform = page.getHtml().xpath("//ul[@class='prop_area']/li[4]/text()").toString();
	
	String versionString = page.getHtml().xpath("//ul[@class='prop_area']/li[1]/text()").toString();
		appVersion = versionString;
		
	String sizeString = page.getHtml().xpath("//div[@class='prop']/span[2]/text()").toString();
		appSize = sizeString;
	
	String updatedateString = page.getHtml().xpath("//ul[@class='prop_area']/li[2]/text()").toString();
	appUpdateDate = updatedateString;
	
	String typeString = "apk";
		appType =typeString;
	
	String venderString = page.getHtml().xpath("//ul[@class='prop_area']/li[5]/text()").toString();
	appVenderName = venderString;
	
	String DownloadedTimeString = page.getHtml().xpath("//div[@class='prop']/span[1]/text()").toString();
		appDownloadedTime = DownloadedTimeString;
	appDescription = usefulInfo(page.getHtml().xpath("//div[@class='desc_area']").toString());
	appTag = page.getHtml().xpath("//div[@class='bd appTag']//a/text()").all().toString();
	appScrenshot = page.getHtml().xpath("//div[@class='areaOverview']//img/@src").all();
	
		System.out.println("appName="+appName);
		System.out.println("appDetailUrl="+appDetailUrl);
		System.out.println("appDownloadUrl="+appDownloadUrl);
		System.out.println("osPlatform="+osPlatform);
		System.out.println("appVersion="+appVersion);
		System.out.println("appSize="+appSize);
		System.out.println("appUpdateDate="+appUpdateDate);
		System.out.println("appType="+appType);
		System.out.println("appVenderName="+appVenderName);
		System.out.println("appDownloadedTime="+appDownloadedTime);
		System.out.println("appDescription="+appDescription);
		System.out.println("appTag="+appTag);
		System.out.println("appScrenshot="+appScrenshot);
		System.out.println("appCategory="+appCategory);

		if(appName != null && appDownloadUrl != null){
			apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//			Apk(String appName,String appMetaUrl,String appDownloadUrl,String osPlatform ,
//					String appVersion,String appSize,String appTsChannel, String appType,String cookie){	
			
			apk.setAppDownloadTimes(appDownloadedTime);
			apk.setAppVenderName(appVenderName);
			apk.setAppTag(appTag);
			apk.setAppScreenshot(appScrenshot);
			apk.setAppDescription(appDescription);
			apk.setAppCategory(appCategory);
						
		}
		
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
