package com.appCrawler.pagePro.apkDetails;


import java.util.List;

import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
/**
 * 机锋论坛，获取apk详情工具
 * @author DMT
 *
 */
public class ApkGfan_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ApkGfan_Detail.class);
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
		
		appName =page.getHtml().xpath("//h4[@class='curr-tit']/text()").toString();
		appDetailUrl = page.getUrl().toString();
		appDownloadUrl = page.getHtml().xpath("//div[@class='app-view-bt']/a/@href").toString();
		osPlatform = null;
			String versionString = page.getHtml().xpath("//div[@class='app-info']/p[1]/text()").toString();
		appVersion = versionString.substring(versionString.indexOf("：")+1,versionString.length());
			String sizeString = page.getHtml().xpath("//div[@class='app-info']/p[4]/text()").toString();
		appSize = sizeString.substring(sizeString.indexOf("：")+1,sizeString.length());
			String updatedateString = page.getHtml().xpath("//div[@class='app-info']/p[3]/text()").toString();
		appUpdateDate = updatedateString.substring(updatedateString.indexOf("：")+1,updatedateString.length());
		appType = "apk";
			String venderString = page.getHtml().xpath("//div[@class='app-info']/p[2]/text()").toString();
		appVenderName = venderString.substring(venderString.indexOf("：")+1,venderString.length());		
		
		String allinfoString = page.getHtml().xpath("//div[@class='app-intro']").toString();		
		while(allinfoString.contains("<"))
			if(allinfoString.indexOf("<") == 0) allinfoString = allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
			else if(allinfoString.contains("<!--")) allinfoString = allinfoString.substring(0,allinfoString.indexOf("<!--")) + allinfoString.substring(allinfoString.indexOf("-->")+3,allinfoString.length());
			else allinfoString = allinfoString.substring(0,allinfoString.indexOf("<")) + allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
		appDescription = allinfoString.replace("\n", "");		
		
		appScrenshot = page.getHtml().xpath("//div[@class='inner']//img/*@src").all();
		
		appCategory = page.getHtml().xpath("//div[@class='app-content clearfix']/h3/text()").toString();	
		appCategory = appCategory.substring(6,appCategory.indexOf("("));
//		System.out.println(page.getHtml().toString());
//		System.out.println("appName="+appName);
//		System.out.println("appDetailUrl="+appDetailUrl);
//		System.out.println("appDownloadUrl="+appDownloadUrl);
//		System.out.println("osPlatform="+osPlatform);
//		System.out.println("appVersion="+appVersion);
//		System.out.println("appSize="+appSize);
//		System.out.println("appUpdateDate="+appUpdateDate);
//		System.out.println("appType="+appType);
//		System.out.println("appVenderName="+appVenderName);
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
		
		return apk;
	}
}
