
package com.appCrawler.pagePro;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.Skycn_Detail;
import com.gargoylesoftware.htmlunit.html.HtmlNav;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.HtmlNode;
import us.codecraft.webmagic.selector.Selector;

/**
 * 天空软件站 http://sj.skycn.com/
 * Skycn #142
 * 提供的搜索接口无法搜索手机应用
 * 
 * 2015年5月27日20:52:52 搜索接口可用，修改中...
 * @author DMT
 */
public class Skycn implements PageProcessor{
	private static  Logger logger = LoggerFactory.getLogger(Skycn.class);
	Site site = Site.me().setCharset("utf-8").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		logger.info("call in Skycn.process()" + page.getUrl());
		//index page				http://sj.skycn.com/s.php?wd=
		if(page.getUrl().regex("http://sj\\.skycn\\.com/s\\.php\\?.*").match()){
			//app的具体介绍页面											http://sj.skycn.com/soft/22633.html
			if(page.getUrl().toString().contains("p=10")){
				//只取前9页
				return null;
			}
			List<String> url1 = page.getHtml().links("//ul[@class='list-software-dld']").regex("http://sj\\.skycn\\.com/soft.*").all();	
			List<String> url3= page.getHtml().links("//ul[@class='list-software-dld']").regex("http://sj\\.skycn\\.com/game.*").all();			
				//添加下一页url(翻页)
			List<String> url2 = page.getHtml().links("//div[@class='page']").regex("http://sj\\.skycn\\.com/s\\.php\\?.*").all();
			
			url1.addAll(url2);
			url1.addAll(url3);
			
			//remove the duplicate urls in list
			HashSet<String> urlSet = new HashSet<String>(url1);
			
			//add the urls to page
			Iterator<String> it = urlSet.iterator();
			while(it.hasNext()){
				page.addTargetRequest(it.next());
			}
		}
		
		//the app detail page
		else if(page.getUrl().regex("http://sj\\.skycn\\.com/soft.*").match() ){
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
//			String allinfoString = page.getHtml().xpath("//div[@class='info']").toString();		
//			if(!allinfoString.contains("Android") && !allinfoString.contains("android")){
//				return null;
//			}
//				
//			appDetailUrl = page.getUrl().toString();
//			
//			Html html = page.getHtml();
//			appName = html.xpath("//h2[@class='title']/text()").toString();		
//			appDownloadUrl = html.xpath("//div[@class='download-area']/a/@href").toString();
//			
//			String sizeString = html.xpath("//div[@class='info']/ul[1]/li[2]/text()").toString();
//			appSize = sizeString.substring(sizeString.indexOf("：")+1,sizeString.length());
//				
//			String updatedateString = html.xpath("//div[@class='info']/ul[1]/li[3]/text()").toString();
//			appUpdateDate = updatedateString.substring(updatedateString.indexOf("：")+1,updatedateString.length());
//		
//			String DownloadedTimeString = html.xpath("//div[@class='info']/ul[2]/li[1]/text()").toString();
//			appDownloadedTime = DownloadedTimeString.substring(DownloadedTimeString.indexOf("：")+1,DownloadedTimeString.length());		
//			
//			String versionString = html.xpath("//div[@class='info']/ul[2]/li[2]/text()").toString();
//			appVersion = versionString.substring(versionString.lastIndexOf("：")+1,versionString.length());
//				
//			String platFormString =html.xpath("//div[@class='info']/ul[2]/li[3]/text()").toString();
//			osPlatform = platFormString.substring(platFormString.indexOf("：")+1,platFormString.length());
//		
//			String descriptionString = html.xpath("//p[@id='j_soft_desc']").toString();
//			allinfoString = descriptionString;
//
//			while(allinfoString.contains("<"))
//				if(allinfoString.indexOf("<") == 0) allinfoString = allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
//				else if(allinfoString.contains("<!--")) allinfoString = allinfoString.substring(0,allinfoString.indexOf("<!--")) + allinfoString.substring(allinfoString.indexOf("-->")+3,allinfoString.length());
//				else allinfoString = allinfoString.substring(0,allinfoString.indexOf("<")) + allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
//					
//			appDescription = allinfoString;	
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
//			System.out.println("appDescription="+appDescription);
//			*/
//			if(appName != null && appDownloadUrl != null){
//				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//			}
			logger.info("return from Skycn.process()");
			return Skycn_Detail.getApkDetail(page);
		}
		else if(page.getUrl().regex("http://sj\\.skycn\\.com/game.*").match()){
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
//			String allinfoString = page.getHtml().xpath("//div[@class='info']").toString();		
//			if(!allinfoString.contains("Android") && !allinfoString.contains("android") && !allinfoString.contains("安卓")){
//				return null;
//			}
//			
//			Html html = page.getHtml();
//			appDetailUrl = page.getUrl().toString();
//			appName=html.xpath("//h2[@class='title']/text()").toString();		
//			int i=0;
//			
//			for(i=2;i<10;i++)
//				if(html.xpath("//tbody/tr["+i+"]").toString().contains("安卓"))
//					break;
//			if(i<10){
//				appDownloadUrl = page.getHtml().xpath("//tbody/tr["+i+"]").links().toString();
//				
//				appUpdateDate = page.getHtml().xpath("//tbody/tr["+i+"]/td[2]/text()").toString();
//			
//				appDownloadedTime = page.getHtml().xpath("//tbody/tr["+i+"]/td[3]/text()").toString();
//								
//				appSize = html.xpath("//tbody/tr["+i+"]/td[4]/text()").toString();
//			}		
//			String descriptionString = html.xpath("//p[@id='j_soft_desc']").toString();
//			allinfoString = descriptionString;
//			while(allinfoString.contains("<"))
//				if(allinfoString.indexOf("<") == 0) allinfoString = allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
//				else if(allinfoString.contains("<!--")) allinfoString = allinfoString.substring(0,allinfoString.indexOf("<!--")) + allinfoString.substring(allinfoString.indexOf("-->")+3,allinfoString.length());
//				else allinfoString = allinfoString.substring(0,allinfoString.indexOf("<")) + allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());					
//			appDescription = allinfoString;	
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
//			System.out.println("appDescription="+appDescription);
//			 */
//			if(appName != null && appDownloadUrl != null){
//				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//			}
			logger.info("return from Skycn.process()");
			return Skycn_Detail.getApkDetail(page);
		}
		logger.info("return from Skycn.process()");
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
