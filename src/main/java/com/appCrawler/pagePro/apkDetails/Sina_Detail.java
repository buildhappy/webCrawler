package com.appCrawler.pagePro.apkDetails;
/**
 * 新浪 http://app.sina.com.cn/app_index.php?f=p_dh
 * Sina #98
 * @author DMT
 */


import java.util.List;

import org.slf4j.LoggerFactory;




import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;


public class Sina_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Sina_Detail.class);
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
		String nameString=page.getHtml().xpath("//div[@class='appView']/div[1]/h1/text()").toString();			
		appName =nameString;
		
	appDetailUrl = page.getUrl().toString();
	
	appDownloadUrl = page.getHtml().xpath("//div[@class='avAction']/a[1]/@href").toString();
	
	for(int i=1;i<5;i++)
	{
		String tempString = page.getHtml().xpath("//ul[@class='avInfoList']/li["+i+"]/text()").toString();
		if(tempString == null) continue;
		if(tempString.contains("大小"))
			appSize = tempString.substring(tempString.indexOf("：")+1,tempString.length());
		else if(tempString.contains("版本"))
			appVersion = tempString.substring(tempString.indexOf("：")+1,tempString.length());
		else if(tempString.contains("更新时间"))
			appUpdateDate = tempString.substring(tempString.indexOf("：")+1,tempString.length());
		else if(tempString.contains("系统要求"))
			osPlatform = tempString.substring(tempString.indexOf("：")+1,tempString.length());
	}
	
	String typeString = "apk";
		appType =typeString;
		
	String venderString = page.getHtml().xpath("//div[@class='avOrigin']/text()").toString();
	appVenderName= venderString.substring(venderString.indexOf("：")+1,venderString.length());
		
	String DownloadedTimeString = page.getHtml().xpath("//span[@class='downValue']/text()").toString();
		appDownloadedTime = DownloadedTimeString.substring(DownloadedTimeString.indexOf("(")+1,DownloadedTimeString.indexOf(")"));		
	appScrenshot = page.getHtml().xpath("//ul[@id='screenCont']//img/@src").all();
	appDescription = page.getHtml().xpath("//p[@id='description_p']/text()").toString();
	appCategory = page.getHtml().xpath("//div[@class='mHd']/h3/a[3]/text()").toString();
	
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
