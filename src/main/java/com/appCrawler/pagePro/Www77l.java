package com.appCrawler.pagePro;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.appCrawler.pagePro.apkDetails.Www77l_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 齐齐乐  http://www.77l.com/
 * Www77l #123
 * (1)该网站的应用和游戏的详细页种类较多，有游戏，应用和网游三种，需要分类写
 * (2)在搜索结果中点击显示全部后，获得的结果中有的带有翻页
 * (3)2015年3月3日11:32:57
 * @author DMT
 */
public class Www77l implements PageProcessor{
	Site site = Site.me().setCharset("utf-8").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		//index page					http://www.77l.com/so/%E5%88%80%E5%A1%94%E4%BC%A0%E5%A5%87/
		if(page.getUrl().regex("http://www\\.77l\\.com/so/.*").match()){
			//app的具体介绍页面											
			//游戏
			List<String> url1 = page.getHtml().links("div[@class='right']").regex("http://www\\.77l\\.com/game/.*").all();	
			List<String> url2 = page.getHtml().links("div[@class='right']").regex("http://www\\.77l\\.com/Game/.*").all();	
			List<String> url4 = page.getHtml().links("div[@class='right']").regex("http://wy\\.77l\\.com/.*").all();	
			
			//应用
			List<String> url3 = page.getHtml().links("div[@class='right']").regex("http://www\\.77l\\.com/App/.*").all();	
			List<String> url7 = page.getHtml().links("div[@class='right']").regex("http://www\\.77l\\.com/app/.*").all();
					
			//当前目录页未显示完  
			//有的目录页带有翻页
			List<String> url5 = page.getHtml().links("div[@class='right']").regex("http://www\\.77l\\.com/so/.*/app_.*/").all();		
			List<String> url6 = page.getHtml().links("div[@class='right']").regex("http://www\\.77l\\.com/so/.*/game_.*/").all();		
			
			url1.addAll(url2);
			url1.addAll(url3);
			url1.addAll(url4);
			url1.addAll(url5);
			url1.addAll(url6);
			url1.addAll(url7);
			
			//remove the duplicate urls in list
			HashSet<String> urlSet = new HashSet<String>(url1);
			
			//add the urls to page
			Iterator<String> it = urlSet.iterator();
			while(it.hasNext()){
				page.addTargetRequest(it.next());
				
			}

		}
		
		//the app detail page
		//game & Game
		else if(page.getUrl().regex("http://www\\.77l\\.com/game/.*").match() || page.getUrl().regex("http://www\\.77l\\.com/Game/.*").match()){
//			Apk apk = null;
//			String appName = null;				//app名字
//			String appDetailUrl = null;			//具体页面url
//			String appDownloadUrl = null;		//app下载地址
//			String osPlatform = null ;			//运行平台
//			String appVersion = null;			//app版本
//			String appSize = null;				//app大小
//			String appUpdateDate = null;		//更新日期
//			String appType = null;				//下载的文件类型 apk？zip？rar？ipa?
//			String appvender = null;			//app开发者  APK这个类中尚未添加
//			String appDownloadedTime=null;		//app的下载次数
//			
//			String nameString=page.getHtml().xpath("//div[@class='app_title']/h1/strong/text()").toString();			
//				appName =nameString;
//				
//			appDetailUrl = page.getUrl().toString();
//			
//			appDownloadUrl = page.getHtml().xpath("//div[@class='appdown']/a/@href").toString();
//			
//			String platFormString =page.getHtml().xpath("//div[@id='divdetails']/text()").toString();
//				osPlatform = platFormString.substring(platFormString.indexOf("系统要求")+5,platFormString.indexOf("人气")-1);
//			//System.out.println(platFormString);
//				
//			String versionString = page.getHtml().xpath("//div[@class='app_title']/em[2]/text()").toString();
//				appVersion = versionString.substring(versionString.indexOf("v")+1,versionString.length());
//			
//			String sizeString = page.getHtml().xpath("//div[@class='appinfo']/text()").toString();
//				appSize = sizeString.substring(sizeString.indexOf("大小")+3,sizeString.length());
//			
//			String updateString = page.getHtml().xpath("//div[@id='divdetails']/text()").toString();
//				appUpdateDate = updateString.substring(updateString.indexOf("更新时间")+5,updateString.indexOf("系统要求")-1);
//			
//			String typeString = "apk";
//				appType =typeString;
//			
//			String venderString = page.getHtml().xpath("//span[@class='fn-left appinfo-kfs']/text()").toString();
//				appvender = venderString.substring(4,venderString.length());
//				
//			String DownloadedTimeString = page.getHtml().xpath("//div[@id='divdetails']/script/@src").toString();
//			String line=null;
//			try {
//				//打开一个网址，获取源文件，这个网址里面是一个document.write("****")
//				URL url=new URL(DownloadedTimeString);
//				BufferedReader reader;
//				reader = new BufferedReader(new InputStreamReader(url.openStream()));
//				line=reader.readLine();
//				//line=document.write('30168');
//			} catch (Exception e) {
//			}
//			if(line != null)
//				appDownloadedTime =line.substring(line.indexOf("(")+2,line.indexOf(")")-1);
//			/*
//			System.out.println("appName="+appName);
//			System.out.println("appDetailUrl="+appDetailUrl);
//			System.out.println("appDownloadUrl="+appDownloadUrl);
//			System.out.println("osPlatform="+osPlatform);
//			System.out.println("appVersion="+appVersion);
//			System.out.println("appSize="+appSize);
//			System.out.println("appUpdateDate="+appUpdateDate);
//			System.out.println("appType="+appType);
//			System.out.println("appvender="+appvender);
//			System.out.println("appDownloadedTime="+appDownloadedTime);
//			 */
//			if(appName != null && appDownloadUrl != null){
//				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//			}			
			return Www77l_Detail.getApkDetail(page);
		}
		//应用
		else if(page.getUrl().regex("http://www\\.77l\\.com/App/.*").match() || page.getUrl().regex("http://www\\.77l\\.com/app/.*").match()){
//			Apk apk = null;
//			String appName = null;				//app名字
//			String appDetailUrl = null;			//具体页面url
//			String appDownloadUrl = null;		//app下载地址
//			String osPlatform = null ;			//运行平台
//			String appVersion = null;			//app版本
//			String appSize = null;				//app大小
//			String appUpdateDate = null;		//更新日期
//			String appType = null;				//下载的文件类型 apk？zip？rar？ipa?
//			String appvender = null;			//app开发者  APK这个类中尚未添加
//			String appDownloadedTime=null;		//app的下载次数
//			
//			//allinfoString里面有appSize,appUpdateDate,osPlatform,appvender
//			String allinfoString = page.getHtml().xpath("//div[@class='app_xx_right']").toString();
//			
//			String DownloadedTimeString = allinfoString.substring(allinfoString.indexOf("script")+12,allinfoString.indexOf("/script")-3);			
//			DownloadedTimeString = DownloadedTimeString.replace("amp;","");
//			String line=null;
//			try {
//				//打开一个网址，获取源文件，这个网址里面是一个document.write("****")
//				URL url=new URL(DownloadedTimeString);
//				BufferedReader reader;
//				reader = new BufferedReader(new InputStreamReader(url.openStream()));
//				line=reader.readLine();
//				//line=document.write('30168');
//			} catch (Exception e) {
//			}
//			if(line != null)
//				appDownloadedTime =line.substring(line.indexOf("(")+2,line.indexOf(")")-1);
//						
//			//去掉allinfoString中的标签
//			while(allinfoString.contains("<"))
//				if(allinfoString.indexOf("<") == 0) allinfoString = allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
//				else allinfoString = allinfoString.substring(0,allinfoString.indexOf("<")) + allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
//
//			String nameString=page.getHtml().xpath("//div[@class='app_xx_right']/h1/text()").toString();			
//				appName =nameString;
//				
//			appDetailUrl = page.getUrl().toString();
//			
//			appDownloadUrl = page.getHtml().xpath("//div[@class='app_xx_left']/a[1]/@href").toString();
//			
//			osPlatform = allinfoString.substring(allinfoString.indexOf("要求")+3,allinfoString.indexOf("包名")-2);
//			//System.out.println(platFormString);
//				
//			String versionString = page.getHtml().xpath("//div[@class='app_xx_right']/h1/i/text()").toString();
//				appVersion = versionString.substring(versionString.indexOf("v")+1,versionString.length());
//			
//			appSize = allinfoString.substring(allinfoString.indexOf("大小")+3,allinfoString.indexOf("推荐")-2);
//			
//			appUpdateDate = allinfoString.substring(allinfoString.indexOf("更新")+3,allinfoString.indexOf("要求")-2);
//			
//			String typeString = "apk";
//				appType =typeString;
//			
//			appvender = allinfoString.substring(allinfoString.indexOf("开发")+3,allinfoString.indexOf("更新")-2);
//			
//			System.out.println("appName="+appName);
//			System.out.println("appDetailUrl="+appDetailUrl);
//			System.out.println("appDownloadUrl="+appDownloadUrl);
//			System.out.println("osPlatform="+osPlatform);
//			System.out.println("appVersion="+appVersion);
//			System.out.println("appSize="+appSize);
//			System.out.println("appUpdateDate="+appUpdateDate);
//			System.out.println("appType="+appType);
//			System.out.println("appvender="+appvender);
//			System.out.println("appDownloadedTime="+appDownloadedTime);
//		
//			if(appName != null && appDownloadUrl != null){
//				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//			}			
			return Www77l_Detail.getApkDetail(page);	
		}
		//网游
		else if(page.getUrl().regex("http://wy\\.77l\\.com/.*").match()){
//			Apk apk = null;
//			String appName = null;				//app名字
//			String appDetailUrl = null;			//具体页面url
//			String appDownloadUrl = null;		//app下载地址
//			String osPlatform = null ;			//运行平台
//			String appVersion = null;			//app版本
//			String appSize = null;				//app大小
//			String appUpdateDate = null;		//更新日期
//			String appType = null;				//下载的文件类型 apk？zip？rar？ipa?
//			String appvender = null;			//app开发者  APK这个类中尚未添加
//			String appDownloadedTime=null;		//app的下载次数
//			
//			String allinfoString = page.getHtml().xpath("//ul[@class='aside-game-info']").toString();
//			while(allinfoString.contains("<"))
//				if(allinfoString.indexOf("<") == 0) allinfoString = allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
//				else if(allinfoString.contains("<!--")) allinfoString = allinfoString.substring(0,allinfoString.indexOf("<!--")) + allinfoString.substring(allinfoString.indexOf("-->")+3,allinfoString.length());
//				else allinfoString = allinfoString.substring(0,allinfoString.indexOf("<")) + allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
//						
//			String nameString=page.getHtml().xpath("//h2[@class='nt-3-name']/text()").toString();			
//				appName =nameString;
//				
//			appDetailUrl = page.getUrl().toString();
//			
//			appDownloadUrl = page.getHtml().xpath("//div[@class='nt-3-bt']/a[1]/@href").toString();
//			
//			String platFormString =allinfoString.substring(allinfoString.indexOf("要求")+2,allinfoString.indexOf("以上")+2);
//				platFormString = platFormString.replace(" ", "");
//				osPlatform = platFormString.replace("\n", "");
//				
//			String versionString = page.getHtml().xpath("//h2[@class='aside-title icon-game-info']/text()").toString();
//				appVersion = versionString.substring(versionString.indexOf("v")+1,versionString.indexOf("（"));
//			
//			String sizeString = allinfoString.substring(allinfoString.indexOf("大小")+2,allinfoString.indexOf("热度"));
//				sizeString = sizeString.replace(" ", "");
//				appSize = sizeString.replace("\n", "");
//			
//			String updateString = allinfoString.substring(allinfoString.indexOf("更新")+2,allinfoString.indexOf("要求"));
//				updateString = updateString.replace(" ", "");
//				appUpdateDate = updateString.replace("\n", "");
//			
//			String typeString = "apk";
//				appType =typeString;
//			
//			String venderString = allinfoString.substring(allinfoString.indexOf("开发")+2,allinfoString.indexOf("包名"));
//				venderString = venderString.replace(" ", "");
//				appvender = venderString.replace("\n", "");
//				
//			String DownloadedTimeString = allinfoString.substring(allinfoString.indexOf("热度")+2,allinfoString.indexOf("分类"));
//				DownloadedTimeString = DownloadedTimeString.replace(" ", "");
//				DownloadedTimeString = DownloadedTimeString.replace("\n", "");
//				appDownloadedTime =DownloadedTimeString.substring(0,DownloadedTimeString.length()-1);
//			
//			System.out.println("appName="+appName);
//			System.out.println("appDetailUrl="+appDetailUrl);
//			System.out.println("appDownloadUrl="+appDownloadUrl);
//			System.out.println("osPlatform="+osPlatform);
//			System.out.println("appVersion="+appVersion);
//			System.out.println("appSize="+appSize);
//			System.out.println("appUpdateDate="+appUpdateDate);
//			System.out.println("appType="+appType);
//			System.out.println("appvender="+appvender);
//			System.out.println("appDownloadedTime="+appDownloadedTime);		
//			if(appName != null && appDownloadUrl != null){
//				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//			}						
			return Www77l_Detail.getApkDetail(page);
		}							
		return null;
	}
	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public List<Apk> processMulti(Page page) {
		// TODO Auto-generated method stub
		return null;
	}

}
