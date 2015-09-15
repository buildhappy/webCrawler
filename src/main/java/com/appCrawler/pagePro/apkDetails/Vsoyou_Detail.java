package com.appCrawler.pagePro.apkDetails;
/**
 * 威搜游 http://vsoyou.com/
 * Vsoyou #111
 * @author DMT
 */
import java.util.List;

import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;


public class Vsoyou_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Vsoyou_Detail.class);
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
		

		 appName=page.getHtml().xpath("//li[@class='game_li_02']/text()").toString();			
		if(appName == null) return null;
		
	appDetailUrl = page.getUrl().toString();
	
	appDownloadUrl = page.getHtml().xpath("//li[@class='game_li_04']/a/@href").toString();
	
	String allinfoString = page.getHtml().xpath("//li[@class='game_li_03']").toString();
	if(allinfoString != null)	
	{
	if(allinfoString.contains("作者") && allinfoString.contains("类别"))
		appVenderName = allinfoString.substring(allinfoString.indexOf("作者")+3,allinfoString.indexOf("类别"));
	if(allinfoString.contains("版本号"))
		appVersion = allinfoString.substring(allinfoString.indexOf("版本号")+4,allinfoString.indexOf("<br"));
	if(allinfoString.contains("大小") && allinfoString.contains("适用固件"))
		appSize = allinfoString.substring(allinfoString.indexOf("大小")+3,allinfoString.indexOf("适用固件"));
	if(allinfoString.contains("适用固件") && allinfoString.contains("上架时间"))
		osPlatform = allinfoString.substring(allinfoString.indexOf("适用固件")+5,allinfoString.indexOf("上架时间")-8);
	if(allinfoString.contains("上架时间"))
		appUpdateDate = allinfoString.substring(allinfoString.indexOf("上架时间")+5,allinfoString.length()-5);	
	if(allinfoString.contains("类别"))
		appCategory = allinfoString.substring(allinfoString.indexOf("类别")+3,allinfoString.indexOf("版本号"));	
	
	}
	
	appDescription = page.getHtml().xpath("//li[@class='game_li_07']").toString();
	allinfoString = appDescription;
	while(allinfoString.contains("<"))
		if(allinfoString.indexOf("<") == 0) allinfoString = allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
		else if(allinfoString.contains("<!--")) allinfoString = allinfoString.substring(0,allinfoString.indexOf("<!--")) + allinfoString.substring(allinfoString.indexOf("-->")+3,allinfoString.length());
		else allinfoString = allinfoString.substring(0,allinfoString.indexOf("<")) + allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
	appDescription = allinfoString;
	appType = "apk";
	appScrenshot = page.getHtml().xpath("//div[@id='piclistbox']//img/@src").all();
	
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
