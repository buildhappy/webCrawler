package com.appCrawler.pagePro.apkDetails;


import java.util.List;

import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;

public class Ruan8_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Fpwap_Detail.class);
	public static Apk getApkDetail(Page page){
		
	
			Apk apk = null;
			String appName = null;				//app名字
			String appDetailUrl = null;			//具体页面url
			String appDownloadUrl = null;		//app下载地址
			String osPlatform = null ;			//运行平台
			String appVersion = null;			//app版本
			String appSize = null;				//app大小
			String appUpdateDate = null;		//更新日期
			String appType = null;				//下载的文件类型 apk？zip？rar？ipa?
			String appVenderName = null;		//app开发者  APK这个类中尚未添加
			String appDownloadedTime=null;		//app的下载次数
			String appDescription = null;		//app的详细描述
			List<String> appScrenshot = null;	//app的屏幕截图
			String appTag = null;				//app的应用标签
			String appCategory = null;			//app的应用类别 
			
			String nameString=page.getHtml().xpath("//div[@class='mdlmenu w750']/h1/text()").toString();	
			if(nameString.contains("v"))
				appName =nameString.substring(0,nameString.indexOf("v")-1);
				
			appDetailUrl = page.getUrl().toString();
			
			appDownloadUrl = page.getHtml().xpath("//div[@class='mdddblist']/p/a/@href").toString();
			
			osPlatform = page.getHtml().xpath("//p[@id='os']/text()").toString();
			
			String versionString = page.getHtml().xpath("//div[@class='mdlmenu w750']/h1/text()").toString();
			if(versionString.contains("v") && versionString.contains("."))
				appVersion = versionString.substring(versionString.indexOf("v")+1,versionString.lastIndexOf(".")+2);
			
			String sizeString = page.getHtml().xpath("//ul[@class='mdccs']/li[7]/text()").toString();
				appSize = sizeString.substring(sizeString.indexOf("：")+1,sizeString.length());
			
			String updatedateString = page.getHtml().xpath("//ul[@class='mdccs']/li[8]/text()").toString();
				appUpdateDate = updatedateString.substring(updatedateString.indexOf("：")+1,updatedateString.length());
			
			String typeString = page.getHtml().xpath("//ul[@class='mdccs']/li[3]/text()").toString();
				appType =typeString.substring(typeString.indexOf("：")+1,typeString.length());
			if(!typeString.contains("APK")) 
				return null;
			
			String DownloadedTimeString = page.getHtml().xpath("//ul[@class='mdccs']/li[9]/text()").toString();
				appDownloadedTime = DownloadedTimeString.substring(DownloadedTimeString.indexOf("：")+1,DownloadedTimeString.length());		
			String descriptionString = page.getHtml().xpath("//div[@class='w760 mgl10 floatr']/div[5]/div[2]").toString();
				String allinfoString = descriptionString;
				while(allinfoString.contains("<"))
					if(allinfoString.indexOf("<") == 0) allinfoString = allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
					else if(allinfoString.contains("<!--")) allinfoString = allinfoString.substring(0,allinfoString.indexOf("<!--")) + allinfoString.substring(allinfoString.indexOf("-->")+3,allinfoString.length());
					else allinfoString = allinfoString.substring(0,allinfoString.indexOf("<")) + allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
				appDescription =allinfoString.replace("\n", "").replace(" ", "");
				
			appScrenshot= page.getHtml().xpath("//div[@id ='thumb']//a/@href").all();	
			appCategory = page.getHtml().xpath("//p[@class='floatl mgl25']/a[3]/text()").toString();
			
			System.out.println("appName="+appName);
			System.out.println("appDetailUrl="+appDetailUrl);
			System.out.println("appDownloadUrl="+appDownloadUrl);
			System.out.println("osPlatform="+osPlatform);
			System.out.println("appVersion="+appVersion);
			System.out.println("appSize="+appSize);
			System.out.println("appUpdateDate="+appUpdateDate);
			System.out.println("appType="+appType);
			System.out.println("appvender="+appVenderName);
			System.out.println("appDownloadedTime="+appDownloadedTime);
			System.out.println("appDescription="+appDescription);
			System.out.println("appTag="+appTag);
			System.out.println("appScrenshot="+appScrenshot);
			System.out.println("appCategory="+appCategory);
		
			if(appName != null && appDownloadUrl != null){
				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
				
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
