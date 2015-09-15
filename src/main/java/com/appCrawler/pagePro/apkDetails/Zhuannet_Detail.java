package com.appCrawler.pagePro.apkDetails;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;


public class Zhuannet_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Zhuannet_Detail.class);
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
		String nameString=page.getHtml().xpath("//div[@class='detail2_1_left']/h1/text()").toString();	
		if(nameString.contains("v"))
		{
			appName =nameString.substring(0,nameString.indexOf("v")-1);
			appVersion = nameString.substring(nameString.indexOf("v")+1,nameString.length());
		}
		else {
			appName = nameString;
			appVersion= null;
		}
		appDetailUrl = page.getUrl().toString();
		
		appDownloadUrl = page.getHtml().xpath("//div[@class='detail2_3']/a/@href").toString();
		
		String osPlatformString = page.getHtml().xpath("//div[@class='detail2_1_left']/dl/dd[3]/a/text()").toString();
			osPlatform = osPlatformString;
		
		String sizeString = page.getHtml().xpath("//div[@class='detail2_1_left']/dl/dd[5]/text()").toString();
			appSize = sizeString;
		
		String updatedateString = page.getHtml().xpath("//div[@class='detail2_1_left']/dl/dd[4]/text()").toString();
			appUpdateDate = updatedateString;
		
		String typeString = "apk";
			appType =typeString;
		
			appVenderName=null;
			
		String DownloadedTimeString = page.getHtml().xpath("//div[@class='detail2_1_left']/dl/dd[6]/text()").toString();
			appDownloadedTime = DownloadedTimeString;	
			
		appDescription = page.getHtml().xpath("//div[@class='app_detail_info']/text()").toString();
		appTag = page.getHtml().xpath("//div[@class='detail2_1_left']/dl/dt[1]/span[2]//a/text()").all().toString();
		appScrenshot = page.getHtml().xpath("//div[@class='screenshot-out']/table/tbody/tr//td/img/@src").all();
		appCategory = page.getHtml().xpath("//div[@class='navigate']/a[3]/text()").toString();
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
	
	
	

}
