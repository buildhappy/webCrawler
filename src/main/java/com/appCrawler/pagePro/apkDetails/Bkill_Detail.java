package com.appCrawler.pagePro.apkDetails;

/** 
 * 必杀客 http://www.bkill.com
 * Bkill #136
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

public class Bkill_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory
			.getLogger(Bkill_Detail.class);

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
		

		 osPlatform =page.getHtml().xpath("//div[@class='soft_Abstract h305 w412 l top7 bd']/ul/li[11]/span/text()").toString();
		
		if(osPlatform == null || (!osPlatform.contains("Android") && !osPlatform.contains("android")))
		{
			return null;
			
		}
		String nameString=page.getHtml().xpath("//h1[@class='title_h1']/text()").toString();			
		System.out.println("nameString="+nameString);
		if(nameString != null && nameString.contains("V"))
				{
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
				else 
				{
					appName = nameString;
					appVersion = null;
				}

			
		appDetailUrl = page.getUrl().toString();
		
		appDownloadUrl = page.getHtml().xpath("//div[@class='down_link_main']/ul/li[5]/a/@href").toString();
		
		
		
		String sizeString = page.getHtml().xpath("//div[@class='soft_Abstract h305 w412 l top7 bd']/ul/li[2]/span/text()").toString();
			appSize = sizeString;
		
		String updatedateString = page.getHtml().xpath("//div[@class='soft_Abstract h305 w412 l top7 bd']/ul/li[8]/span/text()").toString();
			appUpdateDate = updatedateString;
		
		String typeString = "apk";
			appType =typeString;
		
		String venderString = page.getHtml().xpath("//div[@class='soft_Abstract h305 w412 l top7 bd']/ul/li[7]/span/text()").toString();
		appVenderName=venderString;
			
		String DownloadedTimeString = page.getHtml().xpath("//div[@class='soft_Abstract h305 w412 l top7 bd']/ul/li[10]/span/script/@src").toString();
		String line=null;
		try {
			//打开一个网址，获取源文件，这个网址里面是一个document.write("****")
			URL url=new URL(DownloadedTimeString);
			BufferedReader reader;
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			line=reader.readLine();
			//line=document.write('30168');
		} catch (Exception e) {
		}
		if(line != null)
			appDownloadedTime =line.substring(line.indexOf("(")+2,line.indexOf(")")-1);
		
		String descriptionString = page.getHtml().xpath("//div[@class='jieshao']/p/text()").toString();
			appDescription = descriptionString;		
		appScrenshot = page.getHtml().xpath("//center//img/@src").all();
		appTag = page.getHtml().xpath("//div[@class='t']/a/text()").all().toString();
		appCategory = page.getHtml().xpath("//div[@class='postion top3']/a[5]/text()").toString();
		
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
