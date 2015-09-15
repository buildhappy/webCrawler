package com.appCrawler.pagePro.apkDetails;

/**
 * 齐齐乐  http://www.77l.com/
 * Www77l #123
 * (1)该网站的应用和游戏的详细页种类较多，有游戏，应用和网游三种，需要分类写
 * (2)2015年3月3日11:32:57
 * @author DMT
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;

public class Www77l_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory
			.getLogger(Www77l_Detail.class);

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

		if (page.getUrl().regex("http://www\\.77l\\.com/game/.*").match()
				|| page.getUrl().regex("http://www\\.77l\\.com/Game/.*").match()) {
			 appName = page.getHtml()
					.xpath("//div[@class='app_title']/h1/strong/text()")
					.toString();
			if(appName == null)  return null;

			appDetailUrl = page.getUrl().toString();

			appDownloadUrl = page.getHtml()
					.xpath("//div[@class='appdown']/a/@href").toString();

			String platFormString = page.getHtml()
					.xpath("//div[@id='divdetails']/text()").toString();
			osPlatform = platFormString.substring(
					platFormString.indexOf("系统要求") + 5,
					platFormString.indexOf("人气") - 1);
			// System.out.println(platFormString);

			String versionString = page.getHtml()
					.xpath("//div[@class='app_title']/em[2]/text()").toString();
			appVersion = versionString.substring(
					versionString.indexOf("v") + 1, versionString.length());

			String sizeString = page.getHtml()
					.xpath("//div[@class='appinfo']/text()").toString();
			appSize = sizeString.substring(sizeString.indexOf("大小") + 3,
					sizeString.length());

			String updateString = page.getHtml()
					.xpath("//div[@id='divdetails']/text()").toString();
			appUpdateDate = updateString.substring(
					updateString.indexOf("更新时间") + 5,
					updateString.indexOf("系统要求") - 1);

			String typeString = "apk";
			appType = typeString;

			String venderString = page.getHtml()
					.xpath("//span[@class='fn-left appinfo-kfs']/text()")
					.toString();
			appVenderName = venderString.substring(4, venderString.length());

			String DownloadedTimeString = page.getHtml()
					.xpath("//div[@id='divdetails']/script/@src").toString();
			String line = null;
			try {
				// 打开一个网址，获取源文件，这个网址里面是一个document.write("****")
				URL url = new URL(DownloadedTimeString);
				BufferedReader reader;
				reader = new BufferedReader(new InputStreamReader(
						url.openStream()));
				line = reader.readLine();
				// line=document.write('30168');
			} catch (Exception e) {
			}
			if (line != null)
				appDownloadedTime = line.substring(line.indexOf("(") + 2,
						line.indexOf(")") - 1);
			appDescription = usefulInfo(page.getHtml().xpath("//div[@id='consummary']").toString());
			appScrenshot = page.getHtml().xpath("//div[@class='appimg']//img/@src").all();

			List<String> list = new ArrayList<String>();
			for (String temp : appScrenshot) {				
				list.add("http://www.77l.com"+temp);				
			}
			appScrenshot = list;
		
			appCategory = page.getHtml().xpath("//div[@class='fn-left']/a[4]/text()").toString();
			appTag = page.getHtml().xpath("//p[@class='appinfo-tag']//a/text()").all().toString();
		
		}

		else if (page.getUrl().regex("http://www\\.77l\\.com/App/.*").match()
				|| page.getUrl().regex("http://www\\.77l\\.com/app/.*").match()) {
			
			
			 appName = page.getHtml()
						.xpath("//div[@class='app_xx_right']/h1/text()").toString();
				if(appName == null)  return null;

			// allinfoString里面有appSize,appUpdateDate,osPlatform,appvender
			String allinfoString = page.getHtml()
					.xpath("//div[@class='app_xx_right']").toString();

			String DownloadedTimeString = allinfoString.substring(
					allinfoString.indexOf("script") + 12,
					allinfoString.indexOf("/script") - 3);
			DownloadedTimeString = DownloadedTimeString.replace("amp;", "");
			String line = null;
			try {
				// 打开一个网址，获取源文件，这个网址里面是一个document.write("****")
				URL url = new URL(DownloadedTimeString);
				BufferedReader reader;
				reader = new BufferedReader(new InputStreamReader(
						url.openStream()));
				line = reader.readLine();
				// line=document.write('30168');
			} catch (Exception e) {
			}
			if (line != null)
				appDownloadedTime = line.substring(line.indexOf("(") + 2,
						line.indexOf(")") - 1);

			// 去掉allinfoString中的标签
			while (allinfoString.contains("<"))
				if (allinfoString.indexOf("<") == 0)
					allinfoString = allinfoString.substring(
							allinfoString.indexOf(">") + 1,
							allinfoString.length());
				else
					allinfoString = allinfoString.substring(0,
							allinfoString.indexOf("<"))
							+ allinfoString.substring(
									allinfoString.indexOf(">") + 1,
									allinfoString.length());

			
			

			appDetailUrl = page.getUrl().toString();

			appDownloadUrl = page.getHtml()
					.xpath("//div[@class='app_xx_left']/a[1]/@href").toString();

			osPlatform = allinfoString.substring(
					allinfoString.indexOf("要求") + 3,
					allinfoString.indexOf("包名") - 2);
			// System.out.println(platFormString);

			String versionString = page.getHtml()
					.xpath("//div[@class='app_xx_right']/h1/i/text()")
					.toString();
			appVersion = versionString.substring(
					versionString.indexOf("v") + 1, versionString.length());

			appSize = allinfoString.substring(allinfoString.indexOf("大小") + 3,
					allinfoString.indexOf("推荐") - 2);

			appUpdateDate = allinfoString.substring(
					allinfoString.indexOf("更新") + 3,
					allinfoString.indexOf("要求") - 2);

			String typeString = "apk";
			appType = typeString;

			appVenderName = allinfoString.substring(
					allinfoString.indexOf("开发") + 3,
					allinfoString.indexOf("更新") - 2);
			appTag = allinfoString.substring(allinfoString.indexOf("标签")+3,allinfoString.length());
			appDescription = usefulInfo(page.getHtml().xpath("//div[@class='left']/div[4]").toString());
			appCategory = page.getHtml().xpath("//div[@class='wz']/a[3]/text()").toString();
			appScrenshot = page.getHtml().xpath("//div[@class='appimg']//img/@src").all();			
			List<String> list = new ArrayList<String>();
			for (String temp : appScrenshot) {				
				list.add("http://www.77l.com"+temp);				
			}
			appScrenshot = list;
			
			
		}
		else if(page.getUrl().regex("http://wy\\.77l\\.com/.*").match()){
			
				
			 appName = page.getHtml().xpath("//h2[@class='nt-3-name']/text()").toString();			
				if(appName == null)  return null;
			String allinfoString = page.getHtml().xpath("//ul[@class='aside-game-info']").toString();
			while(allinfoString.contains("<"))
				if(allinfoString.indexOf("<") == 0) allinfoString = allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
				else if(allinfoString.contains("<!--")) allinfoString = allinfoString.substring(0,allinfoString.indexOf("<!--")) + allinfoString.substring(allinfoString.indexOf("-->")+3,allinfoString.length());
				else allinfoString = allinfoString.substring(0,allinfoString.indexOf("<")) + allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
						
			
				
			appDetailUrl = page.getUrl().toString();
			
			appDownloadUrl = page.getHtml().xpath("//div[@class='nt-3-bt']/a[1]/@href").toString();
			
			String platFormString =allinfoString.substring(allinfoString.indexOf("要求")+2,allinfoString.indexOf("以上")+2);
				platFormString = platFormString.replace(" ", "");
				osPlatform = platFormString.replace("\n", "");
				
			String versionString = page.getHtml().xpath("//h2[@class='aside-title icon-game-info']/text()").toString();
				appVersion = versionString.substring(versionString.indexOf("v")+1,versionString.indexOf("（"));
			
			String sizeString = allinfoString.substring(allinfoString.indexOf("大小")+2,allinfoString.indexOf("热度"));
				sizeString = sizeString.replace(" ", "");
				appSize = sizeString.replace("\n", "");
			
			String updateString = allinfoString.substring(allinfoString.indexOf("更新")+2,allinfoString.indexOf("要求"));
				updateString = updateString.replace(" ", "");
				appUpdateDate = updateString.replace("\n", "");
			
			String typeString = "apk";
				appType =typeString;
			
			String venderString = allinfoString.substring(allinfoString.indexOf("开发")+2,allinfoString.indexOf("包名"));
				venderString = venderString.replace(" ", "");
				appVenderName = venderString.replace("\n", "");
				
			String DownloadedTimeString = allinfoString.substring(allinfoString.indexOf("热度")+2,allinfoString.indexOf("分类"));
				DownloadedTimeString = DownloadedTimeString.replace(" ", "");
				DownloadedTimeString = DownloadedTimeString.replace("\n", "");
				appDownloadedTime =DownloadedTimeString.substring(0,DownloadedTimeString.length()-1);
			appTag = allinfoString.substring(allinfoString.indexOf("分类")+2,allinfoString.indexOf("开发")).replace("\n", "");
			appDescription = usefulInfo(page.getHtml().xpath("//div[@class='nt-3-js-con']").toString());
			appScrenshot = page.getHtml().xpath("//div[@class='pic_list']//img/@src").all();
			List<String> list = new ArrayList<String>();
			for (String temp : appScrenshot) {				
				list.add("http://wy.77l.com"+temp);				
			}
			appScrenshot = list;
		
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
