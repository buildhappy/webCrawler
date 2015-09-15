package com.appCrawler.pagePro.apkDetails;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;

public class Fpwap_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Fpwap_Detail.class);
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
		String appVenderName = null;		//app开发者  APK这个类中尚未添加
		String appDownloadedTime=null;		//app的下载次数
		String appDescription = null;		//app的详细描述
		List<String> appScrenshot = null;	//app的屏幕截图
		String appTag = null;				//app的应用标签
		String appCategory = null;			//app的应用类别 
		
		
		String nameString=page.getHtml().xpath("//div[@class='gameinfor-left']/dl/dt/h3/text()").toString();
		if(nameString != null && nameString.contains("V"))
		{
			appName=nameString.substring(0,nameString.indexOf("V")-1);
			appVersion = nameString.substring(nameString.indexOf("V")+1,nameString.length());
		}
		else if(nameString != null && nameString.contains("v"))
		{
			appName=nameString.substring(0,nameString.indexOf("v")-1);
			appVersion = nameString.substring(nameString.indexOf("V")+1,nameString.length());
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
		appDownloadUrl = page.getHtml().xpath("//div[@class='dj-gamelaod']/ul/li/a/@href").toString();
		osPlatform = null;			
		String sizeString = page.getHtml().xpath("//div[@class='dj-xx comh3']/div/p[1]/text()").toString();
			appSize = sizeString.substring(sizeString.indexOf("：")+1,sizeString.length());
		String updatedateString = page.getHtml().xpath("//div[@class='dj-xx comh3']/div/p[3]/text()").toString();
			appUpdateDate = updatedateString.substring(updatedateString.indexOf("：")+1,updatedateString.length());
		appType = "apk";
		String venderString = page.getHtml().xpath("//div[@class='dj-xx comh3']/div/p[5]/text()").toString();
		appVenderName = venderString.substring(venderString.indexOf("：")+1,venderString.length());
		String downloadedtimeString=page.getHtml().xpath("//div[@class='dj-xx comh3']/div/p[2]/script/@src").toString();
			appDownloadedTime=downloadedtimeString;
		String downloadedTimeUrl = "http://www.fpwap.com/"+downloadedtimeString;
			String line=null;
			try {
				//打开一个网址，获取源文件，这个网址里面是一个document.write("****")
				URL url=new URL(downloadedTimeUrl);
				BufferedReader reader;
				reader = new BufferedReader(new InputStreamReader(url.openStream()));
				line=reader.readLine();
				//line=document.write('30168');
			} catch (Exception e) {
			}
			if(line != null)
				appDownloadedTime =line.substring(line.indexOf("(")+2,line.indexOf(")")-1);
		appDescription = page.getHtml().xpath("//div[@id='hideText']/div").toString();
		String allinfoString = appDescription;
		while(allinfoString.contains("<"))
			if(allinfoString.indexOf("<") == 0) allinfoString = allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
			else if(allinfoString.contains("<!--")) allinfoString = allinfoString.substring(0,allinfoString.indexOf("<!--")) + allinfoString.substring(allinfoString.indexOf("-->")+3,allinfoString.length());
			else allinfoString = allinfoString.substring(0,allinfoString.indexOf("<")) + allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
		appDescription = allinfoString.replace("\n", "");
		appScrenshot = page.getHtml().xpath("//div[@class='hide']//img/@src").all();
		appCategory = page.getHtml().xpath("//div[@class='navigation']/h3/a[3]/text()").toString();
		System.out.println("appName="+appName);
		System.out.println("appDetailUrl="+appDetailUrl);
		System.out.println("appDownloadUrl="+appDownloadUrl);
		System.out.println("osPlatform="+osPlatform);
		System.out.println("appVersion="+appVersion);
		System.out.println("appSize="+appSize);
		System.out.println("appUpdateDate="+appUpdateDate);
		System.out.println("appType="+appType);
		System.out.println("appVenderName="+appVenderName);
		System.out.println("appDescription="+appDescription);
		System.out.println("appTag="+appTag);
		System.out.println("appScrenshot="+appScrenshot);
		System.out.println("appCategory="+appCategory);

		if(appName != null && appDownloadUrl != null){
			apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//			Apk(String appName,String appMetaUrl,String appDownloadUrl,String osPlatform ,
//					String appVersion,String appSize,String appTsChannel, String appType,String cookie){	
			apk.setAppDownloadTimes(appDownloadedTime);
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
