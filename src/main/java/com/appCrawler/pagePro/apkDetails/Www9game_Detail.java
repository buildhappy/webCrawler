package com.appCrawler.pagePro.apkDetails;
/**
 * 九游  http://www.9game.cn/
 * Www9game #174
 * @author DMT 
 */
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;


public class Www9game_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Www9game_Detail.class);
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
		
		appName =page.getHtml().xpath("//h1[@class='h1-title']/a/text()").toString();			
		if(appName == null) appName =page.getHtml().xpath("//h2[@class='h1-title']/a/text()").toString();	
		if(appName == null) return null;
		
		appDetailUrl = page.getUrl().toString();
		
		
		appDownloadUrl = page.getHtml().xpath("//div[@class='btn-con']/a[1]/@href").toString();
		
		appType ="apk";
						
		appDescription = page.getHtml().xpath("//div[@class='right-text']/p/text()").toString();
		if(appDescription == null || appDescription.length() == 0)
			appDescription = page.getHtml().xpath("//div[@class='tips']/p/text()").toString();
		
	
		appScrenshot = page.getHtml().xpath("//div[@class='special-img short']//img/@src").all();
		
		if(appScrenshot == null) appScrenshot = page.getHtml().xpath("//div[@class='text-con']//img/@src").all();
		
		appCategory = page.getHtml().xpath("//div[@class='p-des']/p/text()").toString();
		if(appCategory != null && appCategory.contains(":")) 
			appCategory = appCategory.substring(0,appCategory.indexOf(":")-1);
//		System.out.println("appName="+appName);
//		System.out.println("appDetailUrl="+appDetailUrl);
//		System.out.println("appDownloadUrl="+appDownloadUrl);
//		System.out.println("osPlatform="+osPlatform);
//		System.out.println("appVersion="+appVersion);
//		System.out.println("appSize="+appSize);
//		System.out.println("appUpdateDate="+appUpdateDate);
//		System.out.println("appType="+appType);
//		System.out.println("appVenderName="+appVenderName);
//		System.out.println("appDownloadedTime="+appDownloadedTime);
//		System.out.println("appDescription="+appDescription);
//		System.out.println("appTag="+appTag);
//		System.out.println("appScrenshot="+appScrenshot);
//		System.out.println("appCategory="+appCategory);

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
