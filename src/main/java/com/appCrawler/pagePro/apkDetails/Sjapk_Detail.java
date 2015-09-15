package com.appCrawler.pagePro.apkDetails;
/**
 * 卓乐 http://www.sjapk.com/
 * Sjapk #93
 * @author DMT
 */

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.slf4j.LoggerFactory;




import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;


public class Sjapk_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Sjapk_Detail.class);
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

		String nameString=page.getHtml().xpath("//div[@class='main_r_f']/h1/text()").toString();			
		appName =nameString;
		
	appDetailUrl = page.getUrl().toString();
	
	appDownloadUrl = page.getHtml().xpath("//div[@class='main_r_xiazai5']/a/@href").toString();
	
	osPlatform = page.getHtml().xpath("//div[@class='main_r_f']/p[4]/font[1]/text()").toString();
	
	String versionString = page.getHtml().xpath("//div[@class='main_r_f']/p[4]/font[2]/text()").toString();
		appVersion = versionString;
	
	String sizeString = page.getHtml().xpath("//div[@class='main_r_xiazai3']/div[1]/p[1]/font[1]/text()").toString();
		appSize = sizeString;
	
	String updatedateString = page.getHtml().xpath("//div[@class='main_r_xiazai3']/div[1]/p[1]/font[2]/text()").toString();
		appUpdateDate = updatedateString;
	
	String typeString = "apk";
		appType =typeString;
	
		appVenderName=null;
		
	String DownloadedTimeString = null;
		appDownloadedTime = DownloadedTimeString;	
		
	appDescription = page.getHtml().xpath("//div[@class='jies_f']/text()").toString();
	appTag = page.getHtml().xpath("//div[@class='main_r_xiazai3']/div/p[2]/a/text()").all().toString();
	appScrenshot = page.getHtml().xpath("//div[@class='jietu']/img/@src").all();
	appCategory = page.getHtml().xpath("//div[@class='suozaidi']/a[2]/text()").toString();
	
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
