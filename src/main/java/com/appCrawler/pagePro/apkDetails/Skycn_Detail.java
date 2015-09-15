package com.appCrawler.pagePro.apkDetails;

/**
 * 天空软件站 http://sj.skycn.com/
 * Skycn #142
 * 提供的搜索接口无法搜索手机应用
 * 
 * 2015年5月27日20:52:52 搜索接口可用，修改中...
 * @author DMT
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.bcel.generic.RETURN;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

public class Skycn_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory
			.getLogger(Skycn_Detail.class);

	public static Apk getApkDetail(Page page) {
		Apk apk = null;
		String appName = null; // app名字
		String appDetailUrl = null; // 具体页面url
		String appDownloadUrl = null; // app下载地址
		String osPlatform = null; // 运行平台
		String appVersion = null; // app版本
		String appSize = null; // app大小
		String appUpdateDate = null; // 更新日期
		String appType = null; // 下载的文件类型 apk？zip？rar？
		String appVenderName = null; // app开发者 APK这个类中尚未添加
		String appDownloadedTime = null; // app的下载次数
		String appDescription = null; // app的详细描述
		List<String> appScrenshot = null; // app的屏幕截图
		String appTag = null; // app的应用标签
		String appCategory = null; // app的应用类别
		

		String allinfoString = page.getHtml().xpath("//div[@class='info']").toString();		
		if(allinfoString == null || (!allinfoString.contains("Android") && !allinfoString.contains("android"))){
			return null;
		}
			
		appDetailUrl = page.getUrl().toString();
		
		Html html = page.getHtml();
		appName = html.xpath("//h2[@class='title']/text()").toString();		
		appDownloadUrl = html.xpath("//div[@class='download-area']/a/@href").toString();
		
		String sizeString = html.xpath("//div[@class='info']/ul[1]/li[2]/text()").toString();
		appSize = sizeString.substring(sizeString.indexOf("：")+1,sizeString.length());
			
		String updatedateString = html.xpath("//div[@class='info']/ul[1]/li[3]/text()").toString();
		appUpdateDate = updatedateString.substring(updatedateString.indexOf("：")+1,updatedateString.length());
	
		String DownloadedTimeString = html.xpath("//div[@class='info']/ul[2]/li[1]/text()").toString();
		appDownloadedTime = DownloadedTimeString.substring(DownloadedTimeString.indexOf("：")+1,DownloadedTimeString.length());		
		
		String versionString = html.xpath("//div[@class='info']/ul[2]/li[2]/text()").toString();
		appVersion = versionString.substring(versionString.lastIndexOf("：")+1,versionString.length());
			
		String platFormString =html.xpath("//div[@class='info']/ul[2]/li[3]/text()").toString();
		osPlatform = platFormString.substring(platFormString.indexOf("：")+1,platFormString.length());
	
		String descriptionString = html.xpath("//p[@id='j_soft_desc']").toString();
		allinfoString = descriptionString;

		while(allinfoString.contains("<"))
			if(allinfoString.indexOf("<") == 0) allinfoString = allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
			else if(allinfoString.contains("<!--")) allinfoString = allinfoString.substring(0,allinfoString.indexOf("<!--")) + allinfoString.substring(allinfoString.indexOf("-->")+3,allinfoString.length());
			else allinfoString = allinfoString.substring(0,allinfoString.indexOf("<")) + allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
				
		appDescription = allinfoString;	
		
		appCategory = page.getHtml().xpath("//div[@class='breadcrumb']/a[4]/text()").toString();
		appScrenshot = page.getHtml().xpath("//div[@class='slide clearfix']//img/@src").all();
		
		System.out.println("appName=" + appName);
		System.out.println("appDetailUrl=" + appDetailUrl);
		System.out.println("appDownloadUrl=" + appDownloadUrl);
		System.out.println("osPlatform=" + osPlatform);
		System.out.println("appVersion=" + appVersion);
		System.out.println("appSize=" + appSize);
		System.out.println("appUpdateDate=" + appUpdateDate);
		System.out.println("appType=" + appType);
		System.out.println("appVenderName=" + appVenderName);
		System.out.println("appDownloadedTime=" + appDownloadedTime);
		System.out.println("appDescription=" + appDescription);
		System.out.println("appTag=" + appTag);
		System.out.println("appScrenshot=" + appScrenshot);
		System.out.println("appCategory=" + appCategory);

		if (appName != null && appDownloadUrl != null) {
			apk = new Apk(appName, appDetailUrl, appDownloadUrl, osPlatform,
					appVersion, appSize, appUpdateDate, appType, null);
			// Apk(String appName,String appMetaUrl,String appDownloadUrl,String
			// osPlatform ,
			// String appVersion,String appSize,String appTsChannel, String
			// appType,String cookie){
			apk.setAppDownloadTimes(appDownloadedTime);
			apk.setAppVenderName(appVenderName);
			apk.setAppTag(appTag);
			apk.setAppScreenshot(appScrenshot);
			apk.setAppDescription(appDescription);
			apk.setAppCategory(appCategory);

		} else
			return null;

		return apk;
	}
	
	private  static String  getUrlInfo(String urlString) {
		String sourcefile="";
		String lines;		
		try {
			
			URL url=new URL(urlString);		
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			 while ((lines = reader.readLine()) != null){
				 	sourcefile=sourcefile+lines;
				 	
				}
			// System.out.println(sourcefile);
			 return sourcefile;
		} catch (Exception e) {
			return null;
		}
	}
	

	
}
