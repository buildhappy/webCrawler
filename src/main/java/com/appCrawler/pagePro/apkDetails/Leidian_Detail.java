package com.appCrawler.pagePro.apkDetails;


import java.util.List;

import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;


public class Leidian_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Leidian_Detail.class);
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
		 appName=page.getHtml().xpath("//h1/text()").toString();			
			
			
			appDetailUrl = page.getUrl().toString();
			
			String downloadUrlString = page.getHtml().toString();
				appDownloadUrl = downloadUrlString.substring(downloadUrlString.indexOf("downurl")+10,downloadUrlString.indexOf("barcode")-16);		
			
			String osPlatformString = page.getHtml().xpath("//div[@class='mod-base-info']/ul/li[3]/text()").toString();
				osPlatform = osPlatformString.substring(osPlatformString.indexOf("：")+1,osPlatformString.length());
						
			String versionString = page.getHtml().xpath("//div[@class='mod-base-info']/ul/li[2]/text()").toString();
				appVersion = versionString.substring(versionString.indexOf("：")+1,versionString.length());
			
			String infoString = page.getHtml().toString();
			String sizeString = infoString.substring(infoString.indexOf("size"),infoString.indexOf("downurl"));
				appSize = sizeString.substring(sizeString.indexOf("：")+8,sizeString.indexOf(",")-2);
			
			String updatedateString = page.getHtml().xpath("//div[@class='soft-extra-info']/span[2]/text()").toString();
				appUpdateDate = updatedateString.substring(updatedateString.indexOf("：")+1,updatedateString.length());
						
			String DownloadedTimeString = page.getHtml().xpath("//div[@class='soft-extra-info']/span[1]/text()").toString();
				appDownloadedTime = DownloadedTimeString.substring(DownloadedTimeString.indexOf("：")+1,DownloadedTimeString.length());
			
			String venderString = page.getHtml().xpath("//ul[@class='clearfix']/li[6]/text()").toString();
			appVenderName = venderString.substring(venderString.indexOf("：")+1,venderString.length());		

			appCategory = page.getHtml().xpath("//div[@class='crumb']/a[2]/text()").toString();
			appScrenshot = page.getHtml().xpath("//div[@class='overview']//img/@_src").all();
			appDescription = page.getHtml().xpath("//div[@class='alldesc']/text()").toString();
			
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
