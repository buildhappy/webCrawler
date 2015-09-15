package com.appCrawler.pagePro.apkDetails;


import java.util.List;

import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;


public class Mobyware_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Mobyware_Detail.class);
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
		
		String nameString=page.getHtml().xpath("//div[@id='program_info_title']/text()").toString();			
		appName =nameString;
		
	appDetailUrl = page.getUrl().toString();
	
	//下载地址手动构造
	String urlidString=page.getHtml().xpath("//div[@class='ratingblock']/div/@id").toString();
	if(urlidString == null) return null;
	urlidString=urlidString.substring(urlidString.indexOf("long")+4);
	appDownloadUrl="http://www.mobyware.net/get-software-"+urlidString+".html";
	
	
	String platFormString =page.getHtml().xpath("//td[@id='program_info']/div[1]/span[1]/span[2]/span/text()").toString();
		osPlatform = platFormString;

	String versionString = page.getHtml().xpath("//td[@id='program_info']/div[1]/span[1]/span[1]/text()").toString();
		appVersion = versionString;
	
	String sizeString = page.getHtml().xpath("//td[@id='program_info']/div[1]/span[6]/span[1]/text()").toString();
	if(sizeString != null && sizeString.contains("："))	
		appSize = sizeString.substring(sizeString.indexOf("：")+1,sizeString.length());
	
	String updatedateString = page.getHtml().xpath("//td[@id='program_info']/div[1]/span[3]/span[1]/text()").toString();
		appUpdateDate = updatedateString;
	
	String typeString = "apk";
		appType =typeString;
	
		appVenderName = page.getHtml().xpath("//td[@id='program_info']/div[1]/span[4]/span[1]/text()").toString();
	
	if(page.getHtml().xpath("//td[@id='program_info']/div[1]/span[5]/text()").toString().contains("Downloads"))
		appDownloadedTime = page.getHtml().xpath("//td[@id='program_info']/div[1]/span[5]/span[1]/text()").toString();
	else 
		appDownloadedTime = page.getHtml().xpath("//td[@id='program_info']/div[1]/span[6]/span[1]/text()").toString();

	appDescription = usefulInfo(page.getHtml().xpath("//td[@id='program_desc']").toString());
	appScrenshot = page.getHtml().xpath("//td[@id='program_img']//img/@src").all();
	
	appCategory = page.getHtml().xpath("//span[@id='program_category']/span/text()").toString();
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
		info = allinfoString.replace("\n", "");
		return info;
	}
	
	

}
