
package com.appCrawler.pagePro;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 天空软件站 http://sj.skycn.com/
 * Skycn #142
 * 提供的搜索接口无法搜索手机应用
 * @author DMT
 */
public class PageProSkycn implements PageProcessor{
	private static  Logger logger = LoggerFactory.getLogger(PageProSkycn.class);
	Site site = Site.me().setCharset("utf-8").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		logger.info("call in Skycn.process()" + page.getUrl());
		//index page				http://www.skycn.com/s.php?q=qq&st=0
		if(page.getUrl().regex("http://www\\.skycn\\.com/s\\.php\\?.*").match()){
			//app的具体介绍页面											http://sj.skycn.com/soft/22633.html
			List<String> url1 = page.getHtml().links("//ul[@class='main-list']").regex("http://sj\\.skycn\\.com/soft.*").all();

			//添加下一页url(翻页)
			List<String> url2 = page.getHtml().links("//div[@class='list-page']").regex("http://www\\.skycn\\.com/s\\.php\\?.*").all();
			
			url1.addAll(url2);
			
			//remove the duplicate urls in list
			HashSet<String> urlSet = new HashSet<String>(url1);
			
			//add the urls to page
			Iterator<String> it = urlSet.iterator();
			while(it.hasNext()){
				page.addTargetRequest(it.next());
			}
		}
		
		//the app detail page
		if(page.getUrl().regex("http://www\\.ruan8\\.com/soft.*").match()){
			Apk apk = null;
			String appName = null;				//app名字
			String appDetailUrl = null;			//具体页面url
			String appDownloadUrl = null;		//app下载地址
			String osPlatform = null ;			//运行平台
			String appVersion = null;			//app版本
			String appSize = null;				//app大小
			String appUpdateDate = null;		//更新日期
			String appType = null;				//下载的文件类型 apk？zip？rar？ipa?
			String appvender = null;			//app开发者  APK这个类中尚未添加
			String appDownloadedTime=null;		//app的下载次数
			String appDescription =null;        //app的详细介绍

			String nameString=page.getHtml().xpath("//div[@class='mdlme']/h1/text()").toString();			
				appName =nameString.substring(0,nameString.indexOf("v")-1);
				
			appDetailUrl = page.getUrl().toString();
			
			appDownloadUrl = page.getHtml().xpath("//div[@class='mdddblist']/p/a/@href").toString();
			
			String platFormString =page.getHtml().xpath("//div[@class='ViewBox']/div[2]/div[1]/div[1]/div[2]/p[3]/text()").toString();
				osPlatform = platFormString.substring(platFormString.indexOf("：")+1,platFormString.length());
		
			String versionString = page.getHtml().xpath("//div[@class='mdlmenu w750']/h1/text()").toString();
				appVersion = versionString.substring(versionString.indexOf("v")+1,versionString.lastIndexOf(".")+2);
			
			String sizeString = page.getHtml().xpath("//ul[@class='mdccs']/li[7]/text()").toString();
				appSize = sizeString.substring(sizeString.indexOf("：")+1,sizeString.length());
			
			String updatedateString = page.getHtml().xpath("//ul[@class='mdccs']/li[8]/text()").toString();
				appUpdateDate = updatedateString.substring(updatedateString.indexOf("：")+1,updatedateString.length());
			
			String typeString = page.getHtml().xpath("//ul[@class='mdccs']/li[3]/text()").toString();
				appType =typeString.substring(typeString.indexOf("：")+1,typeString.length());
			
			String venderString = page.getHtml().xpath("//ul[@class='mdccs']/li[3]/text()").toString();
				appvender=venderString.substring(venderString.indexOf("：")+1,venderString.length());
				
			String DownloadedTimeString = page.getHtml().xpath("//ul[@class='mdccs']/li[9]/text()").toString();
				appDownloadedTime = DownloadedTimeString.substring(DownloadedTimeString.indexOf("：")+1,DownloadedTimeString.length());		
			
			String descriptionString = page.getHtml().xpath("//ul[@class='mdccs']/li[9]/text()").toString();
				appDescription = descriptionString.substring(descriptionString.indexOf("：")+1,descriptionString.length());		
			
			System.out.println("appName="+appName);
			System.out.println("appDetailUrl="+appDetailUrl);
			System.out.println("appDownloadUrl="+appDownloadUrl);
			System.out.println("osPlatform="+osPlatform);
			System.out.println("appVersion="+appVersion);
			System.out.println("appSize="+appSize);
			System.out.println("appUpdateDate="+appUpdateDate);
			System.out.println("appType="+appType);
			System.out.println("appvender="+appvender);
			System.out.println("appDownloadedTime="+appDownloadedTime);
			System.out.println("appDescription="+appDescription);
		
			if(appName != null && appDownloadUrl != null){
				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
			}
			logger.info("return from Skycn.process()");
			return apk;
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
