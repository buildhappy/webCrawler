package com.appCrawler.pagePro.apkDetails;
/**
 * APP导航 http://www.appdh.com/
 * Appdh #153
 * @author DMT
 */
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;


public class Appdh_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Appdh_Detail.class);
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
		osPlatform = page.getHtml().xpath("//div[@class='grid_7 alpha omega']/p[5]/text()").toString();
		if(osPlatform == null || !osPlatform.contains("Android")){
		
			return null;
		}
		
		appName=page.getHtml().xpath("//h1[@class='f20 bold']/strong/text()").toString();		
		
			
		appDetailUrl = page.getUrl().toString();
		
		appDownloadUrl = page.getHtml().xpath("//div[@class='left pr_20 pt_2 pl_20']/a/@href").toString();
		
		
	
		appSize = page.getHtml().xpath("//div[@class='left pr_20 pt_2 pl_20']/a/text()").toString();
		if(appSize!= null && appSize.contains("("))
			appSize = appSize.substring(appSize.indexOf("(")+1,appSize.length()-1);
		
		appUpdateDate = page.getHtml().xpath("//div[@class='grid_7 alpha omega']/p[3]/text()").toString();
		appVersion = page.getHtml().xpath("//div[@class='grid_7 alpha omega']/p[2]/text()").toString();
		appVenderName = page.getHtml().xpath("//div[@class='grid_7 alpha omega']/p[6]/text()").toString();
		appDownloadedTime = page.getHtml().xpath("//div[@class='grid_7 alpha omega']/p[8]/text()").toString();

		appType = "apk";
		
		String descriptionString = page.getHtml().xpath("//div[@class='grid_12']/div[5]/div[3]").toString();
		
		String allinfoString = descriptionString;
		while(allinfoString!= null && allinfoString.contains("<"))
			if(allinfoString.indexOf("<") == 0) allinfoString = allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
			else if(allinfoString.contains("<!--")) allinfoString = allinfoString.substring(0,allinfoString.indexOf("<!--")) + allinfoString.substring(allinfoString.indexOf("-->")+3,allinfoString.length());
			else allinfoString = allinfoString.substring(0,allinfoString.indexOf("<")) + allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
		if(allinfoString != null && allinfoString.contains("\n"))
			appDescription = allinfoString.replace("\n", "");
		else appDescription = allinfoString;
		
		appCategory = page.getHtml().xpath("//div[@class='clearfix pt_10 pb_10 line']/a[4]/text()").toString();
		appScrenshot = page.getHtml().xpath("//div[@class='clearfix pl_20 pb_20']//img/@src").all();
		
		
		
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
}
