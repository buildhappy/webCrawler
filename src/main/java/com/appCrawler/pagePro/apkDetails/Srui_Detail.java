package com.appCrawler.pagePro.apkDetails;


import java.util.List;

import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;


public class Srui_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Srui_Detail.class);
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
		String allinfoString = page.getHtml().toString();
//		System.out.println("allinfoString="+allinfoString);
		while(allinfoString.contains("<"))
			if(allinfoString.indexOf("<") == 0) allinfoString = allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
			else if(allinfoString.contains("<!--")) allinfoString = allinfoString.substring(0,allinfoString.indexOf("<!--")) + allinfoString.substring(allinfoString.indexOf("-->")+3,allinfoString.length());
			else allinfoString = allinfoString.substring(0,allinfoString.indexOf("<")) + allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
		allinfoString = allinfoString.replace("\n", "");
//		System.out.println("allinfoString="+allinfoString);	
		osPlatform =allinfoString.substring(allinfoString.indexOf("运行环境")+4,allinfoString.indexOf("软件等级"));

		if(osPlatform.contains("Android") == false && osPlatform.contains("android") == false)
			return null;
		osPlatform = osPlatform.replace("\n", "");
		osPlatform = osPlatform.replace(" ", "");
		//名字和版本号
		String nameString=page.getHtml().xpath("//td[@colspan='3']/h1/text()").toString();		
		if(nameString == null) 
			nameString=page.getHtml().xpath("//td[@colspan='3']/strong/text()").toString();	
		if(nameString != null && nameString.contains("|"))
		{
			appName=nameString.substring(0,nameString.indexOf("|")-1);
			appVersion = null;
		}
		else if(nameString != null && nameString.contains(")"))
		{
			appName=nameString.substring(0,nameString.indexOf(")")+1);
			appVersion = nameString.substring(nameString.indexOf(")")+1,nameString.length());
		}
		else if(nameString != null && nameString.contains("V"))
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

		//allinfoString = allinfoString.replace("\n", "");
		//allinfoString = allinfoString.replace(" ", "");
		appDetailUrl = page.getUrl().toString();
		
		String downloadurlString = page.getHtml().toString().substring(0,page.getHtml().toString().lastIndexOf("相关软件"));
			downloadurlString = downloadurlString.substring(downloadurlString.lastIndexOf("免费下载"));	
			if(downloadurlString.contains("apk")) downloadurlString=downloadurlString.substring(0,downloadurlString.lastIndexOf("apk")+3);
		if(downloadurlString.contains("http"))
			appDownloadUrl = downloadurlString.substring(downloadurlString.lastIndexOf("http"));
		if(appDownloadUrl != null && appDownloadUrl.contains(">")) 
			appDownloadUrl = appDownloadUrl.substring(0, appDownloadUrl.indexOf(">")-1);
		if(appDownloadUrl == null)
			appDownloadUrl = page.getHtml().xpath("//html/body/center/table[7]/tbody/tr/td[4]/table/tbody/tr[14]/td[2]/a[1]/@href").toString();
				
		String sizeString = allinfoString.substring(allinfoString.indexOf("软件大小")+4,allinfoString.indexOf("授权方式"));
		sizeString = sizeString.replace("\n", "");			
		appSize = sizeString.replace(" ", "");
		
		String updatedateString = null;
		if(allinfoString.indexOf("解压密码") < allinfoString.indexOf("推荐"))
			 updatedateString = allinfoString.substring(allinfoString.indexOf("更新时间")+4,allinfoString.indexOf("解压密码"));
		else updatedateString = allinfoString.substring(allinfoString.indexOf("更新时间")+4,allinfoString.indexOf("推荐"));
		updatedateString = updatedateString.replace("\n", "");
		while(updatedateString.indexOf(" ") == 0) updatedateString= updatedateString.substring(1);
		while(updatedateString.lastIndexOf(" ") == updatedateString.length()-1) updatedateString= updatedateString.substring(0,updatedateString.length()-1);
		
		appUpdateDate = updatedateString;
		
		String typeString = "apk";
			appType = typeString;
		
		appVenderName = null;
			
		String DownloadedTimeString = null;
			appDownloadedTime = DownloadedTimeString;
		
		appDescription = page.getHtml().xpath("//html/body/center/table[7]/tbody/tr/td[4]/table/tbody/tr[12]/td[2]/p/text()").toString();
			
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
