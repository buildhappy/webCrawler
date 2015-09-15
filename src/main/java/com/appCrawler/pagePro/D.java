package com.appCrawler.pagePro;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.D_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 当乐安致 http://www.d.cn/
 * 类名 #134
 * 有三种不同的页面分类，分别是网游、应用和游戏
 * @author DMT
 */
public class D implements PageProcessor{
	private static  Logger logger = LoggerFactory.getLogger(D.class);
	Site site = Site.me().setCharset("utf-8").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		logger.info("call in D.process()" + page.getUrl());
		//index page				http://android.d.cn/search/app?keyword=%E5%88%80%E5%A1%94%E4%BC%A0%E5%A5%87
		if(page.getUrl().regex("http://android\\.d\\.cn/search/app\\?keyword.*").match()){
			//app的具体介绍页面		http://android.d.cn/software/3142.html				http://ng.d.cn/wangpaidaluandou/		http://android.d.cn/game/51251.html			
			List<String> url1 = page.getHtml().links("//div[@class='app-tab']").regex("http://ng\\.d\\.cn/.*").all();//网游
			List<String> url3 = page.getHtml().links("//div[@class='app-tab']").regex("http://android\\.d\\.cn/game.*html").all();
			List<String> url4 = page.getHtml().links("//div[@class='app-tab']").regex("http://android\\.d\\.cn/software.*").all();
	
			
			//添加下一页url(翻页)
			List<String> url2 = page.getHtml().links("//div[@class='page']").regex("http://android\\.d\\.cn/search/app\\?keyword.*").all();
			
			url1.addAll(url2);
			url1.addAll(url3);
			url1.addAll(url4);
			
			//remove the duplicate urls in list
			HashSet<String> urlSet = new HashSet<String>(url1);
			
			//add the urls to page
			Iterator<String> it = urlSet.iterator();
			while(it.hasNext()){
				page.addTargetRequest(it.next());
				
			}

		}
		
		//the app detail page
		else if(page.getUrl().regex("http://android\\.d\\.cn/game.*html").match()
				|| page.getUrl().regex("http://android\\.d\\.cn/software.*").match()){
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
//			String appDescription =null;        //app的详细介绍
//			
//				appName =page.getHtml().xpath("//div[@class='de-app-des']/h1/text()").toString();			
//			if(appName == null )
//				appName =page.getHtml().xpath("//div[@class='de-head-l']/h1/text()").toString();		
//			appDetailUrl = page.getUrl().toString();
//			
//
//			appDownloadUrl = page.getHtml().xpath("//ul[@class='de-down']/li[1]/a/@onclick").toString();
//			
//			
//		
//			appVersion = page.getHtml().xpath("//ul[@class='de-game-info clearfix']/li[2]/text()").toString();
//			
//			appSize = page.getHtml().xpath("//ul[@class='de-game-info clearfix']/li[4]/text()").toString();
//					
//			appUpdateDate = page.getHtml().xpath("//ul[@class='de-game-info clearfix']/li[3]/text()").toString();
//			
//			String typeString = page.getHtml().xpath("//ul[@class='de-app-tip clearfix']/li[4]/text()").toString();
//				appType =typeString;
//			String tempString = page.getHtml().xpath("//ul[@class='de-game-info clearfix']").toString();  
//			if(tempString.contains("热度"))	
//			{
//				osPlatform = page.getHtml().xpath("//ul[@class='de-game-info clearfix']/li[9]/text()").toString();
//				 appvender = page.getHtml().xpath("//ul[@class='de-game-info clearfix']/li[10]/a/text()").toString();				
//				 if(null == appvender) 
//						appvender = page.getHtml().xpath("//ul[@class='de-game-info clearfix']/li[10]/text()").toString();	
//			}	
//			else
//			{
//				osPlatform = page.getHtml().xpath("//ul[@class='de-game-info clearfix']/li[8]/text()").toString();
//				appvender = page.getHtml().xpath("//ul[@class='de-game-info clearfix']/li[9]/a/text()").toString();
//				if(null == appvender) 
//					appvender = page.getHtml().xpath("//ul[@class='de-game-info clearfix']/li[9]/text()").toString();
//			
//			}
//			
//			String descriptionString = page.getHtml().xpath("//div[@class='de-intro-inner']/text()").toString();
//				appDescription = descriptionString;	
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
//			System.out.println("appDescription="+appDescription);
//		
//			if(appName != null && appDownloadUrl != null){
//				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//			}
			logger.info("return from D.process()");
			return D_Detail.getApkDetail(page);
		}
		else if(page.getUrl().regex("http://ng\\.d\\.cn/.*").match()){
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
//			String appDescription =null;        //app的详细介绍
//
//					
//			appName =page.getHtml().xpath("//div[@class='zgameName']/h2/text()").toString();			
//			
//			appDetailUrl = page.getUrl().toString();
//			
//			String allinfoString = page.getHtml().xpath("//div[@class='rigame fl']").toString();
//			if(allinfoString == null) 
//			{
//				logger.info("return from D.process()");
//				return null;
//			}
//			while(allinfoString.contains("<"))
//				if(allinfoString.indexOf("<") == 0) allinfoString = allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
//				else if(allinfoString.contains("<!--")) allinfoString = allinfoString.substring(0,allinfoString.indexOf("<!--")) + allinfoString.substring(allinfoString.indexOf("-->")+3,allinfoString.length());
//				else allinfoString = allinfoString.substring(0,allinfoString.indexOf("<")) + allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
//				
//			
//			appDownloadUrl = page.getHtml().xpath("//div[@class='downAnd mb10']/a/@href").toString();
//					
//			appVersion = allinfoString.substring(allinfoString.indexOf("版本号")+4,allinfoString.indexOf("更新")-1).replace("\n", "");
//			
//					
//			appUpdateDate = allinfoString.substring(allinfoString.indexOf("更新")+5,allinfoString.length()).replace("\n", "");
//			
//			
//			String descriptionString = page.getHtml().xpath("//div[@class='zgamejs']/p/text()").toString();
//				appDescription = descriptionString;	
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
//			System.out.println("appDescription="+appDescription);
//		
//			if(appName != null && appDownloadUrl != null){
//				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//			}
			logger.info("return from D.process()");
			return D_Detail.getApkDetail(page);
		}
		logger.info("return from D.process()");
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
