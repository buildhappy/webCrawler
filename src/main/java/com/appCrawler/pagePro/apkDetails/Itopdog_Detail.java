package com.appCrawler.pagePro.apkDetails;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;


public class Itopdog_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Itopdog_Detail.class);
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
		//有的名字里面包含版本号，有的不包含
		String nameString=page.getHtml().xpath("//font[@class='h2_css']/text()").toString();
		if(nameString != null && nameString.contains("V"))
		{
			appName=nameString.substring(0,nameString.indexOf("V")-1);
			appVersion = nameString.substring(nameString.indexOf("V")+1,nameString.length());
		}
		else if(nameString != null && nameString.contains("v"))
		{
			appName=nameString.substring(0,nameString.indexOf("v")-1);
			appVersion = nameString.substring(nameString.indexOf("V")+1,nameString.length());
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
			
		appDetailUrl = page.getUrl().toString();
		
		appDownloadUrl = page.getHtml().xpath("//div[@class='down-btn']/a/@href").toString();
		
		osPlatform = page.getHtml().xpath("//dl[@class='clearfix appinfo']/dd[4]/text()").toString();
		
		String sizeString = page.getHtml().xpath("//dl[@class='clearfix appinfo']/dd[1]/text()").toString();
			appSize = sizeString;
		
		String updatedateString = page.getHtml().xpath("//div[@class='six code2d']/strong/text()").toString();
			appUpdateDate = updatedateString.substring(updatedateString.indexOf("：")+1,updatedateString.length());
		
		String typeString = "apk";
			appType =typeString;
		
		appVenderName = null;

		//下载次数是动态获取的，使用downloadTimeUrl构造出获取下载次数的链接
		String id=page.getHtml().toString();
		id = id.substring(id.indexOf("var id"),id.indexOf("ac=get_updown_api"));
		id = id.substring(10,id.indexOf(";")-1);
		
		String downloadTimeUrl="http://www.itopdog.cn/home.php?ct=home&ac=get_updown_api&id="+id;			
		String line=null;
		String allString ="";
		try {
		
			URL url=new URL(downloadTimeUrl);
			BufferedReader reader;
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			 while ((line = reader.readLine()) != null){
				
				 	allString=allString+line;
				 	
				}
			
		} catch (Exception e) {
		}
		if(allString != null)
		{
			if(allString.contains("down_all"))
			appDownloadedTime =allString.substring(allString.indexOf("down_all")+11,allString.length()-2);
		}
//		if(appDownloadedTime == null) 
//			appDownloadedTime = page.getHtml().xpath("//div[@class='nine down_all']/text").toString();
					
//		String DownloadedTimeString = page.getHtml().xpath("//ul[@class='mdccs']/li[9]/text()").toString();
//			appDownloadedTime = DownloadedTimeString.substring(DownloadedTimeString.indexOf("：")+1,DownloadedTimeString.length());		
		appDescription = page.getHtml().xpath("//div[@class='soft-detail-content six']/text()").toString();
		
		appScrenshot = page.getHtml().xpath("//div[@class='J_switch_panel_wrap']//img/@src").all();
		
		appCategory = page.getHtml().xpath("//p[@class='l bread-left']/a[2]/text()").toString();
			
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
