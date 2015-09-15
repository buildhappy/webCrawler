package com.appCrawler.pagePro.apkDetails;
/**
 * 我机网 http://www.5577.com
 * www5577 #97
 * (1)the app detail page有http://www.5577.com/s/[0-9]* 
 * 						  http://www.5577.com/youxi/[0-9]* 
 * 						  http://www.5577.com/azpj/[0-9]*
 * 	三种 ，其中后两种的页面布局相同
 * @author DMT
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import org.slf4j.LoggerFactory;

import com.sun.org.apache.xerces.internal.impl.xpath.XPath;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;


public class Www5577_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Www5577_Detail.class);
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
		if(page.getUrl().regex("http://www\\.5577\\.com/s/[0-9]*\\.html").match()){
			String nameString=page.getHtml().xpath("//div[@class='info_body']/h1/text()").toString();
		//		System.out.println("nameString="+nameString);
				if(nameString != null && nameString.contains("V")){
					appName=nameString.substring(0,nameString.indexOf("V")-1);
					appVersion = nameString.substring(nameString.indexOf("V")+1,nameString.length());
					
				}
				else if(nameString != null && nameString.contains("v")){
					appName=nameString.substring(0,nameString.indexOf("v")-1);
					appVersion = nameString.substring(nameString.indexOf("v")+1,nameString.length());

				}
				else if(nameString != null && nameString.contains(".")){
					appName=nameString.substring(0,nameString.indexOf(".")-1);
					appVersion = nameString.substring(nameString.indexOf(".")-1,nameString.length());
				}
				else{
					appName = nameString;
					appVersion = null;
				}
					
				appDetailUrl = page.getUrl().toString();
				
				appDownloadUrl = page.getHtml().xpath("//div[@class='app_down']/a[1]/@href").toString();
				
				osPlatform = page.getHtml().xpath("//ul[@class='info']/li[5]/b/text()").toString();
						
				String sizeString = page.getHtml().xpath("//ul[@class='info']/li[4]/text()").toString();
					appSize = sizeString;
				
				String updatedateString = page.getHtml().xpath("//ul[@class='info']/li[2]/text()").toString();
					appUpdateDate = updatedateString;
				
				String typeString = "apk";
					appType =typeString;
				
					appVenderName=null;
					
				String DownloadedTimeString = null;
					appDownloadedTime = DownloadedTimeString;	
				appDescription = usefulInfo(page.getHtml().xpath("//div[@id='detail']").toString());
				appCategory = page.getHtml().xpath("//div[@id='path']/a[3]/text()").toString();
		}
		if(page.getUrl().regex("http://www\\.5577\\.com/youxi/[0-9]*\\.html").match()
				|| page.getUrl().regex("http://www\\.5577\\.com/az[a-z]+/[0-9]*\\.html").match()){	
		String nameString=page.getHtml().xpath("//dl[@class='g_msg']/dt[1]/h1/text()").toString();
		//		System.out.println("nameString="+nameString);
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
				
				appDownloadUrl = page.getHtml().xpath("//dl[@class='g_msg']/dd[4]/a/@href").toString();
				
				osPlatform = page.getHtml().xpath("//dl[@class='g_msg']/dd[3]/span/text()").toString();
						
				String sizeString = page.getHtml().xpath("//dl[@class='g_msg']/dd[2]/strong/text()").toString();
					appSize = sizeString;
				
				String updatedateString = page.getHtml().xpath("//dl[@class='g_msg']/dd[3]/text()").toString();
				if(updatedateString.contains("更新时间"))
					appUpdateDate = updatedateString.substring(updatedateString.indexOf("更新时间")+5,updatedateString.length());
				else appUpdateDate = null;
				
				String typeString = "apk";
					appType =typeString;
				String venderString = page.getHtml().xpath("//dl[@class='g_msg']/dd[2]/text()").toString();
				if(venderString.contains("厂商"))
					appVenderName = venderString.substring(venderString.indexOf("厂商")+3,venderString.length());
				else appVenderName = null;
				
				String DownloadedTimeString = null;
					appDownloadedTime = DownloadedTimeString;	
					
				appDescription = page.getHtml().xpath("//dd[@id='content']/p/text()").toString();
				appScrenshot = page.getHtml().xpath("//div[@id='show_img']/img/@src").all();
				appCategory = page.getHtml().xpath("//div[@class='pos_box']/a[3]/text()").toString();
					
				
		}
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
	
	private static String usefulInfo(String allinfoString)
	{
		String info = null;
		while(allinfoString.contains("<"))
			if(allinfoString.indexOf("<") == 0) allinfoString = allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
			else if(allinfoString.contains("<!--")) allinfoString = allinfoString.substring(0,allinfoString.indexOf("<!--")) + allinfoString.substring(allinfoString.indexOf("-->")+3,allinfoString.length());
			else allinfoString = allinfoString.substring(0,allinfoString.indexOf("<")) + allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
		info = allinfoString.replace("\n", "").replace(" ", "");
		return info;
	}
	
	
	
	

}
