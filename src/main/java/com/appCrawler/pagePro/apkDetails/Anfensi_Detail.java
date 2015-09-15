package com.appCrawler.pagePro.apkDetails;
/**
 * 安粉丝 http://www.anfensi.com/
 * Anfensi #91
 * @author DMT
 */


import java.util.List;

import org.slf4j.LoggerFactory;



import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;


public class Anfensi_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Anfensi_Detail.class);
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

		String nameString=page.getHtml().xpath("//div[@class='yxxq-left']/h1/text()").toString();			
		appName =nameString;
		
	appDetailUrl = page.getUrl().toString();
	
	appDownloadUrl = page.getHtml().xpath("//div[@class='yxxq-left']/p/b/a/@href").toString();
	
	for(int i=1;i<12;i++)
	{
		String catString = page.getHtml().xpath("//p[@class='info']/b["+i+"]/i/text()").toString();
		if(catString == null) break;
		if(catString.contains("版　本"))
			appVersion = page.getHtml().xpath("//p[@class='info']/b["+i+"]/text()").toString();
		else if(catString.contains("大　小"))
			appSize = page.getHtml().xpath("//p[@class='info']/b["+i+"]/text()").toString();
		else if(catString.contains("平　台"))
			osPlatform = page.getHtml().xpath("//p[@class='info']/b["+i+"]/text()").toString();
		else if(catString.contains("开发商"))
			appVenderName = page.getHtml().xpath("//p[@class='info']/b["+i+"]/text()").toString();
		else if(catString.contains("更新时间"))
			appUpdateDate = page.getHtml().xpath("//p[@class='info']/b["+i+"]/text()").toString();
		else if(catString.contains("标　签"))
			appTag = page.getHtml().xpath("//p[@class='info']/b["+i+"]/a/text()").all().toString();
	}
				
	String typeString = "apk";
		appType = typeString;
		appDescription = usefulInfo(page.getHtml().xpath("//div[@class='jj-main']").toString());
		appScrenshot = page.getHtml().xpath("//div[@class='yxpic']//li/img/@src").all();
		appCategory = page.getHtml().xpath("//p[@id='main-title']/a[2]/text()").toString();
		
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
