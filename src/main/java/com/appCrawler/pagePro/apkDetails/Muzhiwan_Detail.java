package com.appCrawler.pagePro.apkDetails;
/**
 * 拇指玩 http://www.muzhiwan.com/
 * Muzhiwan #106
 * @author DMT
 */

import java.util.List;

import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;


public class Muzhiwan_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Muzhiwan_Detail.class);
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
		
		
		 appName=page.getHtml().xpath("//div[@class='game_name']/h1/text()").toString();			
		 if(appName == null) 
			 return null;
		
	appDetailUrl = page.getUrl().toString();
	
	appDownloadUrl = page.getHtml().xpath("//div[@class='detail_dbtn detail_way_t']/a[1]/@href").toString();
	
	String platformString =page.getHtml().xpath("//div[@class='detail_info']/div[2]/ul/li[8]/text()").toString();
	osPlatform = platformString.substring(platformString.indexOf("：")+1,platformString.length());
	
	String versionString = page.getHtml().xpath("//div[@class='detail_info']/div[2]/ul/li[1]/span/text()").toString();
		appVersion = versionString;
	
	String sizeString = page.getHtml().xpath("//div[@class='detail_info']/div[2]/ul/li[2]/text()").toString();
		appSize = sizeString.substring(sizeString.indexOf("：")+1,sizeString.length());
	
	String updatedateString = page.getHtml().xpath("//div[@class='detail_info']/div[2]/ul/li[5]/text()").toString();
		appUpdateDate = updatedateString.substring(updatedateString.indexOf("：")+1,updatedateString.length());
	
	String typeString = page.getHtml().xpath("//div[@class='detail_info']/div[2]/ul/li[7]/text()").toString();
		appType =typeString.substring(typeString.indexOf("：")+1,typeString.length());
	
		appVenderName=page.getHtml().xpath("//div[@class='detail_info']/div[2]/ul/li[10]/a/text()").toString();
		
	String DownloadedTimeString = page.getHtml().xpath("//div[@class='detail_info']/div[2]/ul/li[4]/span/text()").toString();
		appDownloadedTime = DownloadedTimeString;
	appDescription = page.getHtml().xpath("//p[@itemprop='description']/text()").toString();
	appScrenshot = page.getHtml().xpath("//div[@class='img_screen']//img/@src").all();
	appCategory = page.getHtml().xpath("//div[@class='bread_Nav']/a[2]/text()").toString();
	
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
		else return null;
		
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
