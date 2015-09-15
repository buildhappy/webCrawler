package com.appCrawler.pagePro.apkDetails;
/**
 * playcn爱游戏 http://www.play.cn/
 * Play #96
 * @author DMT
 */

import java.util.List;

import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;


public class Play_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Play_Detail.class);
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

		 appName=page.getHtml().xpath("//div[@class='game_info']//h1/text()").toString();			
		if(appName == null)  return null;
		
	appDetailUrl = page.getUrl().toString();
	
	appDownloadUrl = page.getHtml().xpath("//div[@class='side_b r']/a/@href").toString();
	
	osPlatform = null;
	
	String versionString = null;
		appVersion = versionString;
	
	String sizeString = page.getHtml().xpath("//div[@class='info_item']/p/text()").toString().split("：")[1];
		appSize = sizeString;
	
	String updatedateString = page.getHtml().xpath("//div[@class='info l']/div[1]/p/text()").toString().split("：")[1];
		appUpdateDate = updatedateString;
	
	String typeString = "apk";
		appType =typeString;
	
	appVenderName=page.getHtml().xpath("//div[@class='game_layer f_s fix']/div[2]/div[1]/p/text()").toString().split("：")[1];
		
	String DownloadedTimeString = page.getHtml().xpath("//div[@class='game_layer f_s fix']/div[2]/div[2]/p/text()").toString().split("：")[1];
		appDownloadedTime = DownloadedTimeString;		
	appDescription = page.getHtml().xpath("//div[@class='inner']/text()").toString();
	appScrenshot = page.getHtml().xpath("//ul[@class='fn_scrolling scrolling']//img/@src").all();
	appCategory = page.getHtml().xpath("//div[@class='game_layer f_s fix']/div[1]/div[1]/p/text()").toString().split("：")[1];
	appTag = page.getHtml().xpath("//div[@class='tags l']//a/text()").all().toString();
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
			apk.setAppVenderName(appVenderName);
			apk.setAppTag(appTag);
			apk.setAppScreenshot(appScrenshot);
			apk.setAppDescription(appDescription);
			apk.setAppCategory(appCategory);
						
		}
		
		return apk;
	}
}
