package com.appCrawler.pagePro.apkDetails;
/**
 * 优亿市场[中国] http://partner.eoemarket.com/qq/bibei/index/
 * Eoemarket #666
 * 该网站下载apk时电脑必须安装类似于应用包之类的手机管理软件，点击下载apk后电脑自动启动手机管理软件进行下载
 * 不过可以构造下载链接，但是太过复杂
 * ·通过抓包获取的下载：http://d2.eoemarket.com/app0/17/17896/apk/721873.apk?channel_id=401
 * ·页面中存在的链接1：http://c11.eoemarket.com/app0/17/17896/icon/721873.png
 * ·页面中存在的链接2：http://download.eoemarket.com/app?id=17896%26client_id=146%26channel_id=401%26track=pc_qq_show_index_app17896_2
 *  通过后两个链接拼接成下载链接
 * @author buildhappy&DMT 
 */
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;


public class Eoemarket_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Eoemarket_Detail.class);
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
		
		appName =page.getHtml().xpath("//div[@class='para']/div[1]/h1/text()").toString();			
		
		appDetailUrl = page.getUrl().toString();
		
		String url1 = page.getHtml().xpath("//div[@class='showpara']/img/@src").toString();
		String url2 = page.getHtml().xpath("//div[@class='code']/img/@src").toString();
		if(page.getUrl().toString().contains("appId") && url2 != null && url2.split("\\|").length > 0){
			//System.out.println("url1:" + url1);
			url1 = url1.split("http://c11.eoemarket.com/|.png").length > 0 ? url1.split("http://c11.eoemarket.com/|.png")[1]:null;
			if(url1 != null){			
				url1 = url1.replace("icon", "apk");
				url1 = url1.split("_")[0];
			}
			//System.out.println("url2:" + url2);
			url2 = url2.split("chld\\=S\\|1\\&chl=")[1];
			url2 =url2.split("channel_id=|%").length > 3 ?  url2.split("channel_id=|%")[3]:null;
			appDownloadUrl = "http://d2.eoemarket.com/" + url1 + ".apk?channel_id=" + url2;
		}
		
		appVersion = page.getHtml().xpath("//div[@class='para']/div[1]/span/text()").toString();
		

		appSize = page.getHtml().xpath("//div[@class='txt']/span[2]/text()").toString();
		
		String updatedateString = page.getHtml().xpath("//div[@class='para']/div[4]/text()").toString();
			appUpdateDate = updatedateString.substring(updatedateString.indexOf("：")+1,updatedateString.length());
		
		appType ="apk";
		
			
		String DownloadedTimeString = page.getHtml().xpath("//div[@class='para']/div[2]/span/text()").toString();
			appDownloadedTime = DownloadedTimeString.substring(DownloadedTimeString.indexOf("：")+1,DownloadedTimeString.length());		
		
		String descriptionString = page.getHtml().xpath("//div[@class='apptxt']").toString();
		String allinfoString = descriptionString;
		while(allinfoString.contains("<"))
			if(allinfoString.indexOf("<") == 0) allinfoString = allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
			else if(allinfoString.contains("<!--")) allinfoString = allinfoString.substring(0,allinfoString.indexOf("<!--")) + allinfoString.substring(allinfoString.indexOf("-->")+3,allinfoString.length());
			else allinfoString = allinfoString.substring(0,allinfoString.indexOf("<")) + allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
		appDescription = allinfoString.replace("\n", "");		

		appScrenshot = page.getHtml().xpath("//div[@class='pics']//img/@src").all();
		appCategory = page.getHtml().xpath("div[@class='path']/a[3]/text()").toString();
		
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
	
	
	
}
