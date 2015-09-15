package com.appCrawler.pagePro.apkDetails;

/**
 * 豆豆软件下载站 http://www.ddxia.com
 * Ddxia #138
 * 无站内搜索接口，搜索跳到百度搜索
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

public class Ddxia_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory
			.getLogger(Ddxia_Detail.class);

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
		

		osPlatform = page.getHtml().xpath("//div[@class='rjxx_boxr']/ul/li[6]/text()").toString();
		if(osPlatform == null){
			return null;
		}
		
		if(!osPlatform.contains("android") && !osPlatform.contains("Android") ){
			return null;
		}
		
		String nameString=page.getHtml().xpath("//div[@class='rjxx_box']/h1/text()").toString();	
		
		if(nameString != null && nameString.contains("V")){
			appName=nameString.substring(0,nameString.indexOf("V")-1);
			appVersion = nameString.substring(nameString.indexOf("V")+1,nameString.length());
		}
		else if(nameString != null && nameString.contains("v"))
		{
			appName=nameString.substring(0,nameString.indexOf("v")-1);
			appVersion = nameString.substring(nameString.indexOf("v")+1,nameString.length());
		}
		else if(nameString != null && nameString.contains("."))
		{
			appName=nameString.substring(0,nameString.indexOf(".")-1);
			appVersion = nameString.substring(nameString.indexOf(".")-1,nameString.length());
		}
		else{
			appName = nameString;
			appVersion = null;
		}

			
		appDetailUrl = page.getUrl().toString();
		
		appDownloadUrl = page.getHtml().xpath("//div[@class='xzdz_boxl']/ul[1]/li[1]/p/a/@href").toString();
		
		String sizeString = page.getHtml().xpath("//div[@class='ljxz']/a/div/text()").toString();
		appSize = sizeString.substring(sizeString.indexOf("（")+1,sizeString.length()-1);
		
		appUpdateDate = page.getHtml().xpath("//div[@class='rjxx_boxr']/ul/li[9]/text()").toString();
		
		appType = "apk";
		
		String venderString = page.getHtml().xpath("//div[@class='rjxx_boxr']/ul/li[4]/a/@href").toString();
		appVenderName = "官方网站"+venderString; 
			
		appDownloadedTime = page.getHtml().xpath("//div[@class='xzcs']/a/font/text()").toString();
		
		String descriptionString = page.getHtml().xpath("//div[@class='rjjs_box']").toString();
		String allinfoString = descriptionString;	
		while(allinfoString.contains("<"))
			if(allinfoString.indexOf("<") == 0) allinfoString = allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
			else if(allinfoString.contains("<!--")) allinfoString = allinfoString.substring(0,allinfoString.indexOf("<!--")) + allinfoString.substring(allinfoString.indexOf("-->")+3,allinfoString.length());
			else allinfoString = allinfoString.substring(0,allinfoString.indexOf("<")) + allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());					
		appDescription = allinfoString.replace("\n", "");
		
		appTag = page.getHtml().xpath("//div[@class='rjxx_boxr']/ul/li[10]/a/text()").all().toString();
		appScrenshot = page.getHtml().xpath("//div[@id='List1']//a/@href").all();
		appCategory = page.getHtml().xpath("//div[@class='position']/a[3]/text()").toString();
		
//		System.out.println("appName=" + appName);
//		System.out.println("appDetailUrl=" + appDetailUrl);
//		System.out.println("appDownloadUrl=" + appDownloadUrl);
//		System.out.println("osPlatform=" + osPlatform);
//		System.out.println("appVersion=" + appVersion);
//		System.out.println("appSize=" + appSize);
//		System.out.println("appUpdateDate=" + appUpdateDate);
//		System.out.println("appType=" + appType);
//		System.out.println("appVenderName=" + appVenderName);
//		System.out.println("appDownloadedTime=" + appDownloadedTime);
//		System.out.println("appDescription=" + appDescription);
//		System.out.println("appTag=" + appTag);
//		System.out.println("appScrenshot=" + appScrenshot);
//		System.out.println("appCategory=" + appCategory);

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
