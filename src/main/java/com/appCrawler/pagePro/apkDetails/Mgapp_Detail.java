package com.appCrawler.pagePro.apkDetails;

/**
 * 应用酷  http://www.mgyapp.com/
 * Mgapp #119
 * @author DMT
 */
import java.util.List;

import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;


public class Mgapp_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Mgapp_Detail.class);
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
		
		 appName=page.getHtml().xpath("//h1[@class='det-title']/text()").toString();			
		if(appName == null) return null;
		
	appDetailUrl = page.getUrl().toString();
	
	appDownloadUrl = page.getHtml().xpath("//li[@class='det-butn']/a[1]/@href").toString();
	String platFormString =page.getHtml().xpath("//ul[@class='det-info-list']/li[6]/text()").toString();
		osPlatform = platFormString.substring(platFormString.indexOf("：")+1,platFormString.length());

	String versionString = page.getHtml().xpath("//ul[@class='det-info-list']/li[1]/text()").toString();
		appVersion = versionString.substring(versionString.indexOf("：")+1,versionString.length());
	
	String sizeString = page.getHtml().xpath("//ul[@class='det-info-list']/li[4]/text()").toString();
		appSize = sizeString.substring(sizeString.indexOf("：")+1,sizeString.length());
	
	String updatedateString = page.getHtml().xpath("//ul[@class='det-info-list']/li[3]/text()").toString();
		appUpdateDate = updatedateString.substring(updatedateString.indexOf("：")+1,updatedateString.length());
	
	String typeString = "apk";
		appType =typeString;
	
	String venderString = page.getHtml().xpath("//ul[@class='det-info-list']/li[7]/text()").toString();
		appVenderName = venderString.substring(venderString.indexOf("：")+1,venderString.length());;
		
	String DownloadedTimeString = page.getHtml().xpath("//ul[@class='det-info-list']/li[5]/text()").toString();
		appDownloadedTime = DownloadedTimeString.substring(DownloadedTimeString.indexOf("：")+1,DownloadedTimeString.length());		
	
	appDescription = page.getHtml().xpath("//dd[@class='det-intro']/p/text()").toString();
	appScrenshot = page.getHtml().xpath("//ul[@class='pa det-pic-list']//img/@src").all();
	appCategory = page.getHtml().xpath("//h2[@class='fl']/a[3]/text()").toString();
	appTag = page.getHtml().xpath("//ul[@class='det-info-list']/li[9]/a/text()").all().toString();
	
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
