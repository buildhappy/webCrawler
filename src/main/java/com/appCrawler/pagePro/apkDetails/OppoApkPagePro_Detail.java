package com.appCrawler.pagePro.apkDetails;
/**
 * #171 oppo手机软件：http://store.nearme.com.cn/
 * 伪造下载链接:
 * http://store.nearme.com.cn/product/download.html?id=588252&from=1135_-1
 * 将id和from后的参数进行处理
 * @author DMT
 *
 */
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;


public class OppoApkPagePro_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(OppoApkPagePro_Detail.class);
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
		
		
		String rawId = null;
		String id = null;
		String rawFromId = null;
		String fromId = null;
		List<String> info = null;
		rawId = page.getHtml().xpath("//a[@class='detail_down']/@onclick").toString();
		if(rawId != null){
			id = rawId.split("\\(|\\)")[1];
			if((rawFromId=page.getUrl().toString()).contains("=")){
				fromId = rawFromId.split("=")[1];
			}
			appDownloadUrl	= "http://store.nearme.com.cn/product/download.html?id="+ id + "&from="+fromId;
		}
		info = page.getHtml().xpath("//ul[@class='soft_info_more']/li/text()").all();	
		
		appName = page.getHtml().xpath("//div[@class='soft_info_middle']/h3/text()").toString();
		if(appName == null) return null;
		appDetailUrl = page.getUrl().toString();
		if(info != null && appName != null){
			int size = info.size();
			//String[] cur = null;
			appUpdateDate = size>=0?info.get(0).split("：")[1]:null;
			appSize = size>0?info.get(1).split("：")[1]:null;
			appVersion = size>1?info.get(2).split("：")[1]:null;
			osPlatform = size>3?info.get(4).split("：")[1]:null;
		}
		appDownloadedTime = page.getHtml().xpath("//div[@class='soft_info_nums']/text()").toString();
		if(appDownloadedTime != null) appDownloadedTime = appDownloadedTime.substring(0,appDownloadedTime.length()-3);
		
		appDescription = usefulInfo(page.getHtml().xpath("//input[@id='soft_description']/@value").toString());
		appCategory = page.getHtml().xpath("//div[@class='bread_nav']/span[2]/a/text()").toString();
		appScrenshot = page.getHtml().xpath("//ul[@class='img_list']//img/@dataimg").all();
	
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
