package com.appCrawler.pagePro.apkDetails;
/**
 * 新云网络  http://www.newasp.net/
 * Newasp #116
 * @author DMT
 */
import java.util.List;

import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;


public class Newasp_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Newasp_Detail.class);
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
		


		String nameString=page.getHtml().xpath("//div[@class='infobox']/div[2]/h1/text()").toString();			
			appName =nameString.substring(0,nameString.indexOf(" "));
		if(appName == null || appName.contains("ios"))	 
			return null;
		appDetailUrl = page.getUrl().toString();
		
		String downloadurlString = page.getHtml().xpath("//dl[@class='downlist']/script").toString();
		if(!downloadurlString.contains("http") || !downloadurlString.contains("本地高速下载"))	
			return null;
		appDownloadUrl = downloadurlString.substring(downloadurlString.indexOf("http"),downloadurlString.indexOf("本地高速下载")-2);
		
		String platFormString =page.getHtml().xpath("//ul[@class='infolist']/li[9]/text()").toString();
			osPlatform = platFormString;
	
		String versionString = page.getHtml().xpath("//div[@class='infobox']/div[2]/h1/text()").toString();
			appVersion = versionString.substring(versionString.indexOf(" ")+1,versionString.lastIndexOf(" "));
		
		String sizeString = page.getHtml().xpath("//ul[@class='infolist']/li[4]/span/text()").toString();
			appSize = sizeString;
		
		String updatedateString = page.getHtml().xpath("//ul[@class='infolist']/li[7]/text()").toString();
			appUpdateDate = updatedateString;
		
		String typeString = "apk";
			appType =typeString;
		
			appVenderName = page.getHtml().xpath("//ul[@class='infolist']/li[6]/a/text()").toString();
		if(appVenderName == null) appVenderName = page.getHtml().xpath("//ul[@class='infolist']/li[6]/text()").toString();
			
		String DownloadedTimeString = null;
			appDownloadedTime = DownloadedTimeString;	
			
		appDescription = usefulInfo(page.getHtml().xpath("//div[@class='softcontent']").toString());
		appScrenshot = page.getHtml().xpath("//div[@class='softcontent']//img/@src").all();
		appCategory = page.getHtml().xpath("//div[@class='crumb']/a[3]/text()").toString();
//		System.out.println("appName="+appName);
//		System.out.println("appDetailUrl="+appDetailUrl);
//		System.out.println("appDownloadUrl="+appDownloadUrl);
//		System.out.println("osPlatform="+osPlatform);
//		System.out.println("appVersion="+appVersion);
//		System.out.println("appSize="+appSize);
//		System.out.println("appUpdateDate="+appUpdateDate);
//		System.out.println("appType="+appType);
//		System.out.println("appVenderName="+appVenderName);
//		System.out.println("appDownloadedTime="+appDownloadedTime);
//		System.out.println("appDescription="+appDescription);
//		System.out.println("appTag="+appTag);
//		System.out.println("appScrenshot="+appScrenshot);
//		System.out.println("appCategory="+appCategory);

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
