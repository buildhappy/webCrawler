package com.appCrawler.pagePro.apkDetails;

/**
 * 当乐安致 http://www.d.cn/
 * 类名 #134
 * 有三种不同的页面分类，分别是网游、应用和游戏
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

public class D_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory
			.getLogger(D_Detail.class);

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
		
		if(page.getUrl().regex("http://android\\.d\\.cn/game.*html").match()
				|| page.getUrl().regex("http://android\\.d\\.cn/software.*").match()){
			
				appName =page.getHtml().xpath("//div[@class='de-app-des']/h1/text()").toString();			
			if(appName == null )
				appName =page.getHtml().xpath("//div[@class='de-head-l']/h1/text()").toString();		
			appDetailUrl = page.getUrl().toString();
			
			String urlid = page.getUrl().toString();
				urlid = urlid.substring(urlid.lastIndexOf("/")+1,urlid.lastIndexOf("."));
			if(page.getUrl().regex("http://android\\.d\\.cn/game.*html").match())
			{//http://android.d.cn/rm/red/1/48242
				appTag = page.getHtml().xpath("//div[@class='de-tag-wrap clear module-tit-r']/a/span/text()").toString();
				appDownloadUrl = getUrlInfo("http://android.d.cn/rm/red/1/"+urlid);
				System.out.println("urlid="+urlid+" appDownloadUrl="+appDownloadUrl);
				if(appDownloadUrl == null) return null;
				if(appDownloadUrl.contains("qrcode"))
					appDownloadUrl = appDownloadUrl.substring(appDownloadUrl.indexOf("http"),appDownloadUrl.indexOf("qrcode")-3);
				else 
					appDownloadUrl = appDownloadUrl.substring(appDownloadUrl.indexOf("http"),appDownloadUrl.indexOf("name")-3);
			}else{//http://android.d.cn/rm/red/2/48242
				appDownloadUrl = getUrlInfo("http://android.d.cn/rm/red/2/"+urlid);
				appDownloadUrl = appDownloadUrl.substring(appDownloadUrl.indexOf("http"),appDownloadUrl.indexOf("qrcode")-3);
			}
			appVersion = page.getHtml().xpath("//ul[@class='de-game-info clearfix']/li[2]/span[2]/text()").toString();
			
			appSize = page.getHtml().xpath("//ul[@class='de-game-info clearfix']/li[4]/text()").toString();
			
			appCategory = page.getHtml().xpath("//ul[@class='de-game-info clearfix']/li[1]/a/text()").toString();
			
			appUpdateDate = page.getHtml().xpath("//ul[@class='de-game-info clearfix']/li[3]/text()").toString();
			
			String typeString = page.getHtml().xpath("//ul[@class='de-app-tip clearfix']/li[4]/text()").toString();
				appType =typeString;
			String tempString = page.getHtml().xpath("//ul[@class='de-game-info clearfix']").toString();  
			if(tempString.contains("热度"))	
			{
				osPlatform = page.getHtml().xpath("//ul[@class='de-game-info clearfix']/li[9]/text()").toString();
				appVenderName = page.getHtml().xpath("//ul[@class='de-game-info clearfix']/li[10]/a/text()").toString();				
				 if(null == appVenderName) 
					 appVenderName = page.getHtml().xpath("//ul[@class='de-game-info clearfix']/li[10]/text()").toString();	
			}	
			else
			{
				osPlatform = page.getHtml().xpath("//ul[@class='de-game-info clearfix']/li[8]/text()").toString();
				appVenderName = page.getHtml().xpath("//ul[@class='de-game-info clearfix']/li[9]/a/text()").toString();
				if(null == appVenderName) 
					appVenderName = page.getHtml().xpath("//ul[@class='de-game-info clearfix']/li[9]/text()").toString();
			
			}
			
			String descriptionString = page.getHtml().xpath("//div[@class='de-intro-inner']/text()").toString();
				appDescription = descriptionString;	
			appScrenshot = page.getHtml().xpath("//div[@class='snapShotCont']//img/@src").all();
		}else if(page.getUrl().regex("http://ng\\.d\\.cn/.*").match()){
			if(page.getHtml().xpath("//div[@class='zgameName']").toString() == null)	 return null;				
			appName =usefulInfo(page.getHtml().xpath("//div[@class='zgameName']").toString());
			
			appDetailUrl = page.getUrl().toString();
			
			String allinfoString = page.getHtml().xpath("//div[@class='rigame fl']").toString();
			if(allinfoString == null) 
			{
				
				return null;
			}
			while(allinfoString.contains("<"))
				if(allinfoString.indexOf("<") == 0) allinfoString = allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
				else if(allinfoString.contains("<!--")) allinfoString = allinfoString.substring(0,allinfoString.indexOf("<!--")) + allinfoString.substring(allinfoString.indexOf("-->")+3,allinfoString.length());
				else allinfoString = allinfoString.substring(0,allinfoString.indexOf("<")) + allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
							
			appDownloadUrl = page.getHtml().xpath("//div[@class='downAnd mb10']/a/@href").toString();
			
			appTag = allinfoString.substring(allinfoString.indexOf("类型")+3,allinfoString.indexOf("平台")-1).replace("\n", "").replace("&nbsp;", ",");
			
			appVersion = allinfoString.substring(allinfoString.indexOf("版本号")+4,allinfoString.indexOf("更新")-1).replace("\n", "");
								
			appUpdateDate = allinfoString.substring(allinfoString.indexOf("更新")+5,allinfoString.length()).replace("\n", "");
						
			String descriptionString = page.getHtml().xpath("//div[@class='gamejsBox']/text()").toString();
				appDescription = descriptionString;	
						
		}
		
		
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
