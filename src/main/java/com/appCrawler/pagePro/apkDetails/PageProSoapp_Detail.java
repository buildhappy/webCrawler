package com.appCrawler.pagePro.apkDetails;

/**
 * 搜应用 http://www.souapp.com
 * PageProSoapp #145
 * 
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

public class PageProSoapp_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory
			.getLogger(PageProSoapp_Detail.class);

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
		

		   Html html = page.getHtml();

           // 找出对应需要信息
		    appDetailUrl = page.getUrl().toString();
            appName = "育儿专家指导";
            appVersion = null;
            appDownloadUrl = html.xpath("//a[@class='am-btn am-btn-success am-btn-sm']/@href").get();
            osPlatform = null;
            appSize = null;
            appUpdateDate = null;
            appDownloadedTime = null;
            appDescription = html.xpath("//article[@class='am-paragraph am-paragraph-default am-no-layout']/text()").get();
            appType = null;
           
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
